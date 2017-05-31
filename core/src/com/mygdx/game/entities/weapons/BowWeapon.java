package com.mygdx.game.entities.weapons;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.Engine;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.collision.CollisionComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.resources.CollisionResources;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.weapons.projectiles.BaseProjectile;
import com.mygdx.game.entities.weapons.projectiles.ExplosiveProjectile;
import com.mygdx.game.entities.weapons.projectiles.FreezeProjectile;
import com.mygdx.game.entities.weapons.projectiles.NormalProjectile;
import com.mygdx.game.entities.weapons.projectiles.PoisonProjectile;
import com.mygdx.game.globals.Tags;

import aurelienribon.tweenengine.Tween;

/**
 * A Normal Bow Weapon.
 * @author Ramses Di Perna
 *
 */
public class BowWeapon extends BaseBowWeapon
{
	private long _bowDrawSoundInstance = -10;
	private float _volumeDraw = 0;
	
	private ShapeRenderer _shapeRenderer;
	private World _world;
	
	private BasicEntity _aimTarget;
	
	/**
	 * In order to be able to draw its line of aim, it needs to have a world to raycast in.
	 * @param world which all the physics are handled in.
	 */
	public BowWeapon(World world) {
		super(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_DRAW));
		_world = world;
		_shapeRenderer = new ShapeRenderer();
		_shapeRenderer.setAutoShapeType(true);
	}
	
	@Override
	protected void awake() 
	{
		_aimTarget = new BasicEntity();
		_aimTarget.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_BOW_AIM_TARGET), true)).setActiveState(false);
		_aimTarget.getComponent(RenderComponent.class).setPivot(0.5f, 0, false);
		_aimTarget.getTransformComponent().doScale(0.8f, 1.2f, 0.8f, TweenStartType.GameTime).getTween().repeatYoyo(Tween.INFINITY, 0.1f);
		
		_bowDrawSoundInstance = -1;
	}
	
	@Override
	protected void updated(float dt)
	{
		super.updated(dt);
		if(this.getCurrentBowStage() == BowStage.Draw)
		{
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setVolume(_bowDrawSoundInstance, _volumeDraw);
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setPitch(_bowDrawSoundInstance, this.getCurrentDrawStrength());
			_volumeDraw = 0.1f;
		}
	}
	
	protected void drawingBow(int x, int y)
	{
		_volumeDraw = this.getCurrentDrawStrength();
	}
	
	@Override
	protected void destroyed() 
	{
		super.destroyed();
		_aimTarget.destroy();
		_aimTarget = null;
		_world = null;
		_shapeRenderer.dispose();
		_shapeRenderer = null;
	}
	
	

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		if(this.getCurrentProjectile() == null || _aimTarget == null || this.getCurrentProjectile().getTransformComponent() == null) { return;}
		if(this.getCurrentBowStage() == BowStage.Draw)
		{
			final Vector2 landLocation = this.getCurrentProjectile().getLandingPositionWithDrawWeight(powerToDistancePower() * (this.getCurrentDrawStrength() * 0.88f));
			
			Vector2 turnVec = new Vector2(this.getCurrentProjectile().getTransformComponent().getUpwards());
			turnVec.setLength(this.getCurrentProjectile().getComponent(RenderComponent.class).getRealHeight() / 2);
			final Vector2 startPosition = new Vector2(this.getCurrentProjectile().getTransformComponent().getPositionX() + turnVec.x - 2f, this.getCurrentProjectile().getTransformComponent().getPositionY() + turnVec.y);
			final Vector2 endPos = new Vector2(landLocation);
			
			_aimTarget.getTransformComponent().setPosition(landLocation);
			_aimTarget.getTransformComponent().setRotation((-turnVec.angle()) + 90);
			_aimTarget.getComponent(RenderComponent.class).setAlpha(this.getCurrentDrawStrength());
			
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
			
			this.drawDottedLine(_shapeRenderer,(int)(5 + (15 * this.getCurrentDrawStrength())), 1 + 1 * this.getCurrentDrawStrength(), startPosition.x, startPosition.y, endPos.x, endPos.y);
			
			renderComponents.getSpriteBatch().begin();
		}
	}
	@Override
	protected void selectedTarget(int x, int y)
	{
		_aimTarget.getComponent(RenderComponent.class).setActiveState(true);
		stopBowLoopSound();
		_bowDrawSoundInstance = Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).play();
		Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setLooping(_bowDrawSoundInstance, true);
	}
	
	/**
	 * Fires the current arrow from the bow with the strength given.
	 * @param strengthPercentage makes the arrow shoot in distance of powerToDistancePower() * the value and the strength of MAX_DRAW_STRENGTH * the value
	 * @param minimum is the minimum the shooting strength should be. If its below that it will be put onto the minimum value
	 */
	@Override
	protected void shotBow(float strengthPercentage, float minimum) 
	{
		Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_RELEASE).play(strengthPercentage, ((float)Math.random() * 0.3f) + 0.95f, 0);
	}
	
	@Override
	protected void bowSetToStage(BowStage stage)
	{
		if(stage == BowStage.Idle)
		{
			stopBowLoopSound();
			_aimTarget.getComponent(RenderComponent.class).setActiveState(false);
		}
	}
	
	/**
	 * Gives the Arrow location it requires to be in the animation.
	 * NOTE: Must be adjusted when art changes!
	 * @return The Arrow position in sink with the animation.
	 */
	@Override
	protected Vector2 projectilePullDistance()
	{
		Vector2 v = this.getCurrentProjectile().getTransformComponent().getUpwards();
		float amount = 72;
		float delta = 4f;
		amount -= delta * this.getComponent(RenderComponent.class).getCurrentFrameInfo();
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

	@Override
	protected BaseProjectile getProjectileInstance() 
	{
		return new NormalProjectile(25f, 15f);
	}
	
	@Override
	protected BaseProjectile getSpecialProjectile(int specialType)
	{
		switch(specialType)
		{
		case Player.SPECIAL_ARROW_EXPLOSIVE:
			return new ExplosiveProjectile();
		case Player.SPECIAL_ARROW_FREEZE:
			return new FreezeProjectile();
		case Player.SPECIAL_ARROW_POISON:
			return new PoisonProjectile();
		default:
			System.out.println("Warning: No Projectile type found for specialType: " + specialType);
			return null;
		}
	}
	
	private void stopBowLoopSound()
	{
		if(_bowDrawSoundInstance != -1)
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).stop(_bowDrawSoundInstance);
		
		_bowDrawSoundInstance = -1;
	}
}
