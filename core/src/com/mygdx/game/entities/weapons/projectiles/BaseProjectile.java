package com.mygdx.game.entities.weapons.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.globals.EngineGlobals;
import com.mygdx.game.engine.resources.CollisionResources;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.globals.Tags;

/**
 * This is the base class for all the projectiles of weapons in the game. This calculates the speed, sorting, collision and landing position of the arrow.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseProjectile extends BaseEntity implements IEventReceiver
{
	/**
	 * The different stages the projectile can be in.
	 * @author Ramses Di Perna
	 *
	 */
	public enum HeightStage
	{
		/**
		 * Has yet to be fired from the weapon
		 */
		Idle,			// When it has not been fired yes
		/**
		 * Has been fired and is now flying through the air.
		 */
		Air,			// When it is in the air
		/**
		 * Has landed by simply hitting the ground or an enemy.
		 */
		Landed			// When it has landed
	}

	private final float _GROUND_LIFE_TIME = 3f;  // The duration the arrow is alive on the ground
	
	private HeightStage _heightStage = null;
	
	private float _startScaleX;
	private float _startScaleY;
	private RenderComponent _renderComponent;
	private float _speed;
	
	// Track data
	private float _currentSpeed = 0;
	private float _drawPower = 0f;
	private Vector2 _landSpot = null;
	private Vector2 _startPos = null;
	
	
	public BaseProjectile(RenderInfo renderSheetProjectile, float speed)
	{
		_speed = speed;
		_renderComponent = this.addComponent(new RenderComponent(renderSheetProjectile, false));
		_renderComponent.setSortingLayer(3);
		_renderComponent.setSortOnY(true);
		
		this.addComponent(new CollisionComponent()).addEventListener(EngineGlobals.COLLISION_EVENT_COLLISION_ENTER, this);
		
		//Create the Fixture for this Arrow Entity
		FixtureDef _fixDef = new FixtureDef();
		_fixDef.filter.maskBits = CollisionResources.BIT_ENEMY | CollisionResources.BIT_TRAP_ACTIVATOR;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(CollisionResources.convertToPPM(10f), CollisionResources.convertToPPM(30f),
						new Vector2(0, CollisionResources.convertToPPM((this.getRenderComponent().getRealHeight() / 2) - 30)), 0);
		_fixDef.shape = shape;
		this.getComponent(CollisionComponent.class).createFixture(_fixDef, CollisionResources.BIT_ARROW);
		
		this.setHeightStage(HeightStage.Idle);
	}
	
	/**
	 * Returns the RenderComponent attached to this Entity
	 * @return RenderComponent attached on the Entity
	 */
	public RenderComponent getRenderComponent()
	{
		return _renderComponent;
	}
	
	/**
	 * Returns the current HeightStage this projectile is in. 
	 * @return The current HeightStage of the projectile
	 */
	public HeightStage getHeightStage()
	{
		return _heightStage;
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == EngineGlobals.COLLISION_EVENT_COLLISION_ENTER)
		{
			if(this.getHeightStage() == HeightStage.Idle || this.getHeightStage() == HeightStage.Landed) { return; }
			onCollisionEvent((CollisionEvent)event);
		}
	}
	
	/**
	 * Returns the landing position in world space calculated with the current drawing weight this projectile would be fired with
	 * @param drawWeight of the weapon
	 * @return The position it would land with that amount of draw-weight
	 */
	public Vector2 getLandingPositionWithDrawWeight(float drawWeight)
	{
		if(getTransformComponent() == null) {return new Vector2(0,0); }
		Vector2 v = this.getTransformComponent().getUpwards();
		drawWeight = drawWeight - (getWeight() / 2);
		drawWeight = (drawWeight < 0.2f) ? 0.2f : drawWeight;
		v.x *= drawWeight;
		v.y *= drawWeight;
		v.x += this.getTransformComponent().getPositionX();
		v.y += this.getTransformComponent().getPositionY();
		return v;
	}
	
	/**
	 * Fires the projectile into the direction its currently facing.
	 * @param drawWeight is the strength the projectile is fired with, this determines its distance.
	 * @param drawPower is the power the projectile is fired with, this determines its speed and is affected by the projectile's weight
	 */
	public void fire(float drawWeight, float drawPower)
	{
		if(this.getTransformComponent() == null) { return ;}
		_landSpot = getLandingPositionWithDrawWeight(drawWeight);
		drawPower = (drawPower - getWeight());
		drawPower = (drawPower <= 5f) ? 5f : drawPower;
		_drawPower = drawPower;
		_currentSpeed = _speed * drawPower;
		_startPos = new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY());
		
		
		Vector2 diff = new Vector2(_landSpot.x, _landSpot.y);
		diff.sub(new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY()));
		if(diff.len() > _currentSpeed && diff.len() < _currentSpeed * 2)
		{
			diff.setLength(_currentSpeed); // Fixes the stuck in velocity bug
		}
		
		diff.nor();
		diff.x *= _currentSpeed;
		diff.y *= _currentSpeed;
		
		CollisionComponent cc = this.getComponent(CollisionComponent.class);
		if(cc.getBody() != null)
		{
			cc.setVelocity(diff.x, diff.y);
			this.setHeightStage(HeightStage.Air);
		}
		else
			this.destroy();
	}
	
	/**
	 * Gets the draw power this projectile has been fired with.
	 * @return Draw power which the projectile has been fired with as float.
	 */
	protected float getDrawPower()
	{
		return _drawPower;
	}
	
	protected void awake()
	{
		this.addTag(Tags.TAG_PROJECTILE);
		_startScaleX = this.getTransformComponent().getScaleX();
		_startScaleY = this.getTransformComponent().getScaleY();
	}

	/**
	 * This will be triggered when the bow collides with an object.
	 * @param event which holds all the collision data
	 */
	protected abstract void onCollisionEvent(CollisionEvent event);
	/**
	 * Returns the weight of the projectile.
	 * @return The arrow it's weight as float.
	 */
	protected abstract float getWeight();
	/**
	 * This will be triggered every time the projectile's height stage has been changed.
	 * @param newHeightStage It has been changed to.
	 */
	protected abstract void onStageChanged(HeightStage newHeightStage);
	/**
	 * This will be triggered when the projectile lands on the ground withot hitting anything.
	 */
	protected abstract void onLandedOnGround();
	
	@Override
	protected void destroyed()
	{
		this.getComponent(CollisionComponent.class).removeEventListener(EngineGlobals.COLLISION_EVENT_COLLISION_ENTER, this);
		_renderComponent = null;
		_landSpot = null;
		_startPos = null;
	}

	/**
	 * This sets the height stage of the projectile and changes its scale depending on it.
	 * @param heightStage
	 */
	protected void setHeightStage(HeightStage heightStage)
	{
		if(_heightStage == heightStage || _heightStage == HeightStage.Landed) { return; }
		_heightStage = heightStage;
		Vector2 newScale = new Vector2(_startScaleX, _startScaleY);
		float multi = 1;
		
		if(heightStage == HeightStage.Idle)
		{
			multi = 1f;
		}
		else if(heightStage == HeightStage.Air)
		{
			multi = 1.1f;
		}
		else
		{
			multi = 0.9f;
		}
		
		newScale.x *= multi;
		newScale.y *= multi;
		
		this.getTransformComponent().setScale(newScale);

		onStageChanged(heightStage);
		handleStageSpecifics(heightStage);
	}

	@Override
	protected void updated(float dt)
	{
		if(_landSpot != null)
		{
			Vector2 diff = new Vector2(_landSpot.x, _landSpot.y);
			diff.sub(new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY()));
			if(diff.len() > _currentSpeed && diff.len() < _currentSpeed * 2)
			{
				diff.setLength(_currentSpeed); // Fixes the stuck in velocity bug
			}
			float distancePercentage = Vector2.dst(_startPos.x, _startPos.y, _landSpot.x, _landSpot.y);
			distancePercentage = diff.len() / distancePercentage;
			
			if(distancePercentage < 0.15f)
			{
				this.setHeightStage(HeightStage.Landed);
				onLandedOnGround();
			}
		}
	}
	
	private void handleStageSpecifics(HeightStage heightStage) 
	{
		if(heightStage == HeightStage.Landed)
		{
			_landSpot = null;
			this.getComponent(CollisionComponent.class).stopVelocity();
			this.getComponent(CollisionComponent.class).setActiveState(false);
			this.getRenderComponent().setCurrentFrameInfo(this.getRenderComponent().getRenderInfo().getFramesLength() - 1);
			this.getRenderComponent().setSortingLayer(1);
			this.getRenderComponent().setPivot(new Vector2(0.5f, 0.7f), true);
			
			this.getRenderComponent().doAlpha(0, 0.25f, BaseEntityComponent.TweenStartType.GameTime).delay(_GROUND_LIFE_TIME).setCallbackMethod(new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) 
				{
					destroy();
				}}
			);
		}
	}
}
