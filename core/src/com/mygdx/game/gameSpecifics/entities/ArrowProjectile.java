package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.components.Rendering.RenderComponent;
import com.mygdx.game.entities.components.collision.CollisionComponent;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.globals.EngineGlobals;
import com.mygdx.game.resources.CollisionResources;

public class ArrowProjectile extends BaseProjectile implements IEventReceiver 
{
	private final float _SPEED = 2f;
	private final float _GROUND_LIFE_TIME = 3f; 
	
	private float _currentSpeed = 0;
	private Vector2 _landSpot = null;
	private Vector2 _startPos = null;
	
	private float _timeOnGround = 0f;
	
	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == EngineGlobals.GLOBAL_EVENT_COLLISION_ENTER)
		{
			System.out.println("fsddf");
		}
	}
	
	public Vector2 getLandingPositionWithDrawWeight(float drawWeight)
	{
		Vector2 v = this.getTransformComponent().getUpwards();
		drawWeight = drawWeight - (getWeight() / 2);
		drawWeight = (drawWeight < 0.2f) ? 0.2f : drawWeight;
		v.x *= drawWeight;
		v.y *= drawWeight;
		v.x += this.getTransformComponent().getPositionX();
		v.y += this.getTransformComponent().getPositionY();
		return v;
	}
	
	@Override
	public void fire(float powerInDistance, float drawPower)
	{
		_landSpot = getLandingPositionWithDrawWeight(powerInDistance);
		drawPower = (drawPower - getWeight());
		drawPower = (drawPower <= 5f) ? 5f : drawPower;
		_currentSpeed = _SPEED * drawPower;
		_startPos = new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY());
		getComponent(RenderComponent.class).setPivot(new Vector2(0.5f, 1f), true);
	}
	
	@Override
	protected float getWeight() {
		// TODO Auto-generated method stub
		return 5f;
	}

	@Override
	protected void awake() {
		// TODO Auto-generated method stub
		this.addComponent(new RenderComponent(MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_ARROW), false)).setSortingLayer(3);
		this.addComponent(new CollisionComponent()).addEventListener(EngineGlobals.GLOBAL_EVENT_COLLISION_ENTER, this);

		
		
		//Create the Fixture for this Arrow Entity
		FixtureDef _fixDef = new FixtureDef();
		_fixDef.filter.maskBits = CollisionResources.BIT_ENEMY | CollisionResources.BIT_TRAP;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(CollisionResources.convertToPPM(10f), CollisionResources.convertToPPM(30f), new Vector2(0, 0), 0);
		_fixDef.shape = shape;
		this.getComponent(CollisionComponent.class).createFixture(_fixDef, CollisionResources.BIT_ARROW);
	
		super.awake();
		this.getComponent(RenderComponent.class).setSortOnY(true);
	}

	@Override
	protected void updated(float dt) 
	{
		if(_landSpot != null)
		{
			if(this.getHeightStage() != HeightStage.Idle)
			{
				Vector2 diff = new Vector2(_landSpot.x, _landSpot.y);
				diff.sub(new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY()));
				if(diff.len() > _currentSpeed && diff.len() < _currentSpeed * 2)
				{
					diff.setLength(_currentSpeed); // Fixes the stuck in velocity bug
				}
				float distancePercentage = Vector2.dst(_startPos.x, _startPos.y, _landSpot.x, _landSpot.y);
				distancePercentage = diff.len() / distancePercentage;
				this.setHeightStage(getHeightStageByDistancePercentage(distancePercentage));
				//System.out.println(this.getHeightStage());
				
				this.getComponent(RenderComponent.class).getRenderInfo().setCurrentFrameInfo(
						Math.round((1 - distancePercentage) * (this.getComponent(RenderComponent.class).getRenderInfo().getFramesLength() - 3))
				);
				
				diff.nor();
				diff.x *= _currentSpeed;
				diff.y *= _currentSpeed;
				this.getTransformComponent().translatePosition(diff);
			}
			else if(_timeOnGround < _GROUND_LIFE_TIME)
			{
				_timeOnGround += dt;
				this.getComponent(RenderComponent.class).getRenderInfo().setCurrentFrameInfo(this.getComponent(RenderComponent.class).getRenderInfo().getFramesLength() - 2);
				this.getComponent(RenderComponent.class).setSortingLayer(1);
				getComponent(RenderComponent.class).setPivot(new Vector2(0.5f, 0.7f), true);
			}
			else
			{
				RenderComponent rc = this.getComponent(RenderComponent.class);
				if(rc.getAlpha() > 0.1f)
				{
					rc.setAlpha(rc.getAlpha() - dt * 5);
				}
				else
				{
					this.destroy();
				}
			}
		}
	}

	@Override
	protected void destroyed() 
	{
		_landSpot = null;	
		_startPos = null;
		this.getComponent(CollisionComponent.class).removeEventListener(EngineGlobals.GLOBAL_EVENT_COLLISION_ENTER, this);
	}
	
	private HeightStage getHeightStageByDistancePercentage(float distancePercentage) 
	{
		if(distancePercentage < 0.15f)
			return HeightStage.Idle;
		if(distancePercentage < 0.2f || distancePercentage > 0.8f)
			return HeightStage.AboveGround;
		if(distancePercentage > 0.49f && distancePercentage < 0.51)
			return HeightStage.AboveAir;
		
		return HeightStage.GoodAir;
	}
}
