package com.mygdx.game.entities.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.mygdx.game.globals.Tags;

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
	
	private Texture _aimTexture;
	private ShapeRenderer _shapeRenderer;
	private World _world;
	
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
		_aimTexture = Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_BOW_AIM_TARGET).getTextureToDraw();
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
			//Vector2 aimLocation = new Vector2(_aimLocation.x, _aimLocation.y);
			//getTransformComponent().lookAt(aimLocation, 0.2f);
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
		_aimTexture = null;
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
		if(_currentProjectile == null || _aimTexture == null) { return;}
		if(_currentBowStage == BowStage.Draw)
		{
			final Vector2 landLocation = _currentProjectile.getLandingPositionWithDrawWeight(powerToDistancePower() * (_drawStrength * 0.88f));
			
			Vector2 turnVec = new Vector2(_currentProjectile.getTransformComponent().getUpwards());
			turnVec.setLength(_currentProjectile.getComponent(RenderComponent.class).getRealHeight() / 2);
			final Vector2 startPosition = new Vector2(_currentProjectile.getTransformComponent().getPositionX() + turnVec.x, _currentProjectile.getTransformComponent().getPositionY() + turnVec.y);
			final Vector2 endPos = new Vector2(landLocation);
			
			// Draw target
			renderComponents.getSpriteBatch().draw(_aimTexture, landLocation.x - _aimTexture.getWidth() / 2, landLocation.y - _aimTexture.getHeight() / 2, _aimTexture.getWidth(), _aimTexture.getHeight());
			
			// Draw Hit line
			_world.rayCast(new RayCastCallback(){

				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) 
				{
					if(fixture.getUserData().getClass() != CollisionComponent.class) { return -1; }
					CollisionComponent cc = (CollisionComponent)fixture.getUserData();
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

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl.glLineWidth(3);
			
			_shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			Color c;
			
			if(endPos.len() != landLocation.len())
				c = new Color(0.1f, 0.75f, 0.1f, 1f);
			else
				c = new Color(0.75f, 0.1f, 0.1f, 1f);
				
			_shapeRenderer.setColor(c.r, c.g, c.b, 0.5f);
			Vector2 diff = new Vector2(endPos.x - startPosition.x, endPos.y - startPosition.y);
			Vector2 cx1 = new Vector2(diff);
			Vector2 cx2 = new Vector2(diff);
			
			cx1.x += 70;
			cx2.x += 20;
			
			cx1.setLength(cx1.len() * 0.2f);
			cx2.setLength(cx2.len() * 0.8f);
			
			cx1.x += startPosition.x;
			cx1.y += startPosition.y;
			cx2.x += startPosition.x;
			cx2.y += startPosition.y;
			
			_shapeRenderer.curve(startPosition.x, startPosition.y, cx1.x, cx1.y, cx2.x, cx2.y, endPos.x, endPos.y, 50);
			_shapeRenderer.end();

			Gdx.gl.glDisable(GL20.GL_BLEND);
			
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
}
