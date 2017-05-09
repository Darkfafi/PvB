package com.mygdx.game.entities.weapons;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.collision.CollisionComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.resources.CollisionResources;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.globals.Tags;

import aurelienribon.tweenengine.Tween;

public class BowWeapon extends BaseEntity implements IWeapon
{
	public enum BowStage
	{
		Idle,		// Waiting for target location to fire at
		Draw  		// Preparing and Doing the Shoot method
	}

	public static final float MAX_DRAW_STRENGTH = 30f;
	public static final float MAX_DRAW_LENGTH = 200f;
	
	private BowStage _currentBowStage = BowStage.Idle; 	// The current stage of the bow
	private float _drawStrength = 0f;					// The draw strength to release the arrow with
	private Vector2 _targetLocation = null; 			// The target location to fire at / where the finger started
	private float _aimAngle = 0f;
	private float _radiusToTargetLoc = 0f;
	
	private ArrowProjectile _currentProjectile;
	
	private long _bowDrawSoundInstance = -10;
	private float _volumeDraw = 0;
	
	private ShapeRenderer _shapeRenderer;
	private World _world;
	
	private BasicEntity _aimTarget;
	
	public BowWeapon(World world) 
	{
		_world = world;
		_shapeRenderer = new ShapeRenderer();
		_shapeRenderer.setAutoShapeType(true);
	}

	@Override
	public void startControl(int x, int y) 
	{
		selectTarget(x, y);
	}

	@Override
	public void inControl(int x, int y) 
	{
		drawMechanic(x, y);
	}

	@Override
	public void endControl(int x, int y) 
	{
		shoot(_drawStrength, 0.1f);
	}
	
	@Override
	protected void awake() 
	{

		_aimTarget = new BasicEntity();
		_aimTarget.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_BOW_AIM_TARGET), true)).setActiveState(false);
		_aimTarget.getComponent(RenderComponent.class).setPivot(new Vector2(0.5f, 0), false);
		_aimTarget.getTransformComponent().doScale(0.8f, 1.2f, 0.8f, true).getTween().repeatYoyo(Tween.INFINITY, 0.1f);
		
		_bowDrawSoundInstance = -1;
		Animations animations = new Animations("draw", Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_DRAW), false);
		this.addComponent(new AnimationComponent(animations, false, false)).setSortingLayer(2);
		this.setBowIdle();
	}

	@Override
	protected void updated(float dt) 
	{
		if(_currentBowStage == BowStage.Draw)
		{
			this.getTransformComponent().setRotation(_aimAngle);
			
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setVolume(_bowDrawSoundInstance, _volumeDraw);
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setPitch(_bowDrawSoundInstance, _drawStrength);
			_volumeDraw = 0.1f;
		}
		
		handleProjectilePlacement();
	}
	
	@Override
	protected void destroyed() 
	{
		_targetLocation = null;
		_currentProjectile.destroy();
		_currentProjectile = null;
		_aimTarget.destroy();
		_aimTarget = null;
		_world = null;
		_shapeRenderer.dispose();
		_shapeRenderer = null;
	}
	
	/**
	 * Returns the distance the bow can shoot in pixels with its draw strength.
	 * @return Max distance in pixels
	 */
	private float powerToDistancePower()
	{
		return (float) Math.pow(MAX_DRAW_STRENGTH, 2);
	}
	
	

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		if(_currentProjectile == null || _aimTarget == null || _currentProjectile.getTransformComponent() == null) { return;}
		if(_currentBowStage == BowStage.Draw)
		{
			final Vector2 landLocation = _currentProjectile.getLandingPositionWithDrawWeight(powerToDistancePower() * (_drawStrength * 0.88f));
			
			Vector2 turnVec = new Vector2(_currentProjectile.getTransformComponent().getUpwards());
			turnVec.setLength(_currentProjectile.getComponent(RenderComponent.class).getRealHeight() / 2);
			final Vector2 startPosition = new Vector2(_currentProjectile.getTransformComponent().getPositionX() + turnVec.x - 2f, _currentProjectile.getTransformComponent().getPositionY() + turnVec.y);
			final Vector2 endPos = new Vector2(landLocation);
			
			_aimTarget.getTransformComponent().setPosition(landLocation);
			_aimTarget.getTransformComponent().setRotation((-turnVec.angle()) + 90);
			_aimTarget.getComponent(RenderComponent.class).setAlpha(_drawStrength);
			
			_world.rayCast(new RayCastCallback(){

				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) 
				{
					if(fixture.getUserData().getClass() != CollisionComponent.class) { return -1; }
					CollisionComponent cc = (CollisionComponent)fixture.getUserData();
					if(cc == null || cc.getParentOfComponent() == null) { return -1; }
					if(!cc.getParentOfComponent().hasTag(Tags.TAG_ENEMY) && !cc.getParentOfComponent().hasTag(Tags.TAG_TRAP_ACTIVATOR)) { return -1;}
					
					Vector2 convertedPoint = CollisionResources.convertFromPPM(point);
					
					if(convertedPoint.len() < endPos.len())
					{
						Vector2 sizeFixture = new Vector2(50, 50);
						endPos.x = convertedPoint.x + (normal.x * -1) * (sizeFixture.x * 0.5f);
						endPos.y = convertedPoint.y + (normal.y * -1) * (sizeFixture.y * 0.5f);
					}
					
					return -1; // ends ray
				}}, CollisionResources.convertToPPM(startPosition), CollisionResources.convertToPPM(landLocation));
			
			renderComponents.getSpriteBatch().end();
			
			this.drawDottedLine(_shapeRenderer,(int)(5 + (15 * _drawStrength)), 1 + 1 *_drawStrength, startPosition.x, startPosition.y, endPos.x, endPos.y);
			
			renderComponents.getSpriteBatch().begin();
		}
	}
	
	// Bow stage handling
	
	/**
	 * Selects a start touch position before draw. This will be used as the start position of the draw value.
	 * @param posX is the x position of the position value.
	 * @param posY is the y position o the position value.
	 */
	private void selectTarget(int posX, int posY) 
	{
		_targetLocation = new Vector2(posX, posY);
		_radiusToTargetLoc = Vector2.dst(_targetLocation.x, _targetLocation.y, this.getTransformComponent().getPositionX(), 0);
		_currentBowStage = BowStage.Draw;
		
		_aimTarget.getComponent(RenderComponent.class).setActiveState(true);
		
		_bowDrawSoundInstance = Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).play();
		Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setLooping(_bowDrawSoundInstance, true);
	}
	
	/**
	 * Handles the complete draw mechanic of the bow.
	 * @param posX is the x position of the touch which is controlling the draw
	 * @param posY is the y position of the touch which is controlling the draw
	 */
	private void drawMechanic(int posX, int posY) 
	{	
		Vector2 lineToTouch = new Vector2(posX - this.getTransformComponent().getPositionX(), posY);
		Vector2 drawTouch = new Vector2(lineToTouch.x, lineToTouch.y);
		
		Vector2 lineToRealTarget = new Vector2(drawTouch.x, drawTouch.y);
		lineToRealTarget.setLength(_radiusToTargetLoc);
		Vector2 lineToRealMaxDraw = new Vector2(lineToRealTarget.x, lineToRealTarget.y);
		lineToRealMaxDraw.setLength(_radiusToTargetLoc - MAX_DRAW_LENGTH);
		
		_aimAngle = lineToTouch.angle() - 90;
		_aimAngle = 360 - _aimAngle;
		
		float preStrength = _drawStrength;
		
		_drawStrength = 1 - (drawTouch.len() - lineToRealMaxDraw.len()) / (lineToRealTarget.len() - lineToRealMaxDraw.len());
		
		if(_drawStrength > 1)
		{
			_drawStrength = 1;
		}
		else if(_drawStrength < 0)
		{
			_drawStrength = 0;
		}
		
		if(preStrength != _drawStrength)
			_volumeDraw = _drawStrength;
		
		AnimationComponent ac = this.getComponent(AnimationComponent.class);
		ac.setCurrentFrameInfo((int)((ac.getRenderInfo().getFramesLength() - 1) * _drawStrength));
		handleProjectilePlacement();
	}
	
	/**
	 * Fires the current arrow from the bow with the strength given.
	 * @param strengthPercentage makes the arrow shoot in distance of powerToDistancePower() * the value and the strength of MAX_DRAW_STRENGTH * the value
	 * @param minimum is the minimum the shooting strength should be. If its below that it will be put onto the minimum value
	 */
	private void shoot(float strengthPercentage, float minimum) 
	{
		if(strengthPercentage < minimum)
			strengthPercentage = minimum;
		
		Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_RELEASE).play(strengthPercentage, ((float)Math.random() * 0.3f) + 0.95f, 0);
		_currentProjectile.fire((powerToDistancePower() * strengthPercentage), MAX_DRAW_STRENGTH * strengthPercentage);
		setBowIdle();
	}
	
	/**
	 * Sets the bow back to its idle state
	 */
	private void setBowIdle()
	{
		_currentBowStage = BowStage.Idle;
		_drawStrength = 0;
		this.getComponent(AnimationComponent.class).reset(); // Reset bow
		_currentProjectile = new ArrowProjectile();
		if(_bowDrawSoundInstance != -1)
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).stop(_bowDrawSoundInstance);
		
		_bowDrawSoundInstance = -1;
		
		_aimTarget.getComponent(RenderComponent.class).setActiveState(false);
	}
	
	// Bow & Projectile animation handling
	
	/**
	 * Places the arrow in the correct orientation on the bow.
	 * This orientation includes position and rotation.
	 * It gets the position form the 'projectilePullDistance' method 
	 */
	private void handleProjectilePlacement() 
	{
		if(_currentProjectile == null || _currentProjectile.getTransformComponent() == null) { return; }
		_currentProjectile.getTransformComponent().setPosition(new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY()));
		_currentProjectile.getTransformComponent().setRotation(this.getTransformComponent().getRotation());
		_currentProjectile.getTransformComponent().translatePosition(projectilePullDistance());
	}
	
	/**
	 * Gives the Arrow location it requires to be in the animation.
	 * NOTE: Must be adjusted when art changes!
	 * @return The Arrow position in sink with the animation.
	 */
	private Vector2 projectilePullDistance()
	{
		Vector2 v = _currentProjectile.getTransformComponent().getUpwards();
		float amount = 72;
		float delta = 4f;
		amount -= delta * this.getComponent(AnimationComponent.class).getCurrentFrameInfo();
		v.x *= amount;
		v.y *= amount;
		return v;
	}
	
	/**
	 * Draws a dotted line between to points (x1,y1) and (x2,y2).
	 * @param shapeRenderer which it needs to draw dots with
	 * @param dotDist (distance between dots)
	 * @param dotScaler indicates the extra scaling it gets towards the middle (0 == original, 1 == 2 times its original size)
	 * @param x1 start x position line
	 * @param y1 start y position line
	 * @param x2 end x position line
	 * @param y2 end y position line
	 */
	private void drawDottedLine(ShapeRenderer shapeRenderer, int dotDist, float dotScaler, float x1, float y1, float x2, float y2) {
	    shapeRenderer.begin(ShapeType.Filled);
	    Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
	    float length = vec2.len();
	    for(int i = dotDist; i < length; i += dotDist) {
	        vec2.clamp(length - i, length - i);
	        float x = x1 + vec2.x;
	        float y = y1 + vec2.y;
	        
	        float norm = i / length;
	        if(norm > 0.5f)
	        	norm = 1 - norm;
	        
	        shapeRenderer.circle(x, y, 2 + (2 * dotScaler) * (norm));
	    }

	    shapeRenderer.end();
	}
}
