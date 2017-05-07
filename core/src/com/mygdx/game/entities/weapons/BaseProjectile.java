package com.mygdx.game.entities.weapons;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.globals.Tags;

public abstract class BaseProjectile extends BaseEntity 
{
	public enum HeightStage
	{
		Idle,			// Unable to travel
		AboveGround, 	// Hits all
		GoodAir, 		// Misses traps, hits enemies
		AboveAir 		// Misses all
	}
	
	private HeightStage _heightStage;
	
	private float _startScaleX;
	private float _startScaleY;
	
	protected void awake()
	{
		this.addTag(Tags.TAG_PROJECTILE);
		_startScaleX = this.getTransformComponent().getScaleX();
		_startScaleY = this.getTransformComponent().getScaleY();
	}
	
	public abstract void fire(float strength, float drawPower);
	
	public HeightStage getHeightStage()
	{
		return _heightStage;
	}
	
	protected abstract float getWeight();
	
	protected void setHeightStage(HeightStage heightStage)
	{
		_heightStage = heightStage;
		Vector2 newScale = new Vector2(_startScaleX, _startScaleY);
		float multi = 1;
		
		if(heightStage == HeightStage.AboveGround)
		{
			multi = 1.1f;
		}
		else if(heightStage == HeightStage.GoodAir)
		{
			multi = 1.3f;
		}
		else if(heightStage == HeightStage.AboveAir)
		{
			multi = 1.33f;
		}
		else
		{
			multi = 0.9f;
		}
		
		newScale.x *= multi;
		newScale.y *= multi;
		
		this.getTransformComponent().setScale(newScale);
	}
}
