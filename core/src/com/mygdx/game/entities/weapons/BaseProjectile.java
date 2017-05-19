package com.mygdx.game.entities.weapons;

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

public abstract class BaseProjectile extends BaseEntity implements IEventReceiver
{
	public enum HeightStage
	{
		Idle,			// When it has not been fired yes
		Air,			// When it is in the air
		Landed			// When it has landed
	}

	private final float _GROUND_LIFE_TIME = 3f; 					// The duration the arrow is alive on the ground
	
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
	
	public RenderComponent getRenderComponent()
	{
		return _renderComponent;
	}
	
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
	
	protected abstract void onCollisionEvent(CollisionEvent event);

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
	
	protected void fire(float strength, float drawPower)
	{
		if(this.getTransformComponent() == null) { return ;}
		_landSpot = getLandingPositionWithDrawWeight(strength);
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
	
	protected abstract float getWeight();
	protected abstract void onStageChanged(HeightStage newHeightStage);
	
	@Override
	protected void destroyed()
	{
		this.getComponent(CollisionComponent.class).removeEventListener(EngineGlobals.COLLISION_EVENT_COLLISION_ENTER, this);
		_renderComponent = null;
		_landSpot = null;
		_startPos = null;
	}

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
		
		handleStageSpecifics(heightStage);
		onStageChanged(heightStage);
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
	
	protected abstract void onLandedOnGround();

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
