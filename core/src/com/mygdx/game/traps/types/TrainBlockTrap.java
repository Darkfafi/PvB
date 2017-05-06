package com.mygdx.game.traps.types;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.Train;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.factories.TrapFactory.Direction;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Grid;

public class TrainBlockTrap extends BaseBlockTrap 
{
	public static final float DURATION = 4f;
	public static final float COOLDOWN_TIME = DURATION + 8f;
	public static final float SPEED = 8;
	
	private Train _train;
	
	private float _cooldownWaited = 0;
	
	public TrainBlockTrap(Grid grid, Direction direction) 
	{
		super(grid, direction);
		this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TRAIN_RAIL), false)).setPivot(new Vector2(0, 0), false);
		this.getComponent(RenderComponent.class).setSortOnY(true);
	}

	@Override
	public int getSizeX() 
	{
		return getGrid().getTileAmountX(); // Covers all horizontal tiles on given row
	}

	@Override
	public int getSizeY()
	{
		return 1;
	}

	@Override
	protected void awake() 
	{

	}

	@Override
	protected void updated(float dt) 
	{
		if(!this.canBeTriggered())
		{
			_cooldownWaited += dt;
			if(_cooldownWaited >= COOLDOWN_TIME)
			{
				_cooldownWaited = 0f;
				canBeTriggered = true;
			}
		}
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{

	}
	
	@Override
	protected void destroyed()
	{
		super.destroyed();
		
		if(_train != null)
		{
			_train.destroy();
			_train = null;
		}
	}
	
	@Override
	protected void doEffect()
	{
		super.doEffect();
		_train = new Train((int)Math.ceil(DURATION * SPEED), this.getDirection() == TrapFactory.Direction.Left);
		
		float startX = (this.getDirection() == TrapFactory.Direction.Left) ? Engine.getWidth() + _train.getTrainSize() : this.getTransformComponent().getPositionX() - _train.getTrainSize();
		float endX = (this.getDirection() == TrapFactory.Direction.Left) ? 0 - _train.getFullTrainSize() : Engine.getWidth() + _train.getFullTrainSize();
		
		_train.getTransformComponent().setPosition(startX, this.getTransformComponent().getPositionY() + 12);
		_train.getTransformComponent().doPosition(endX, _train.getTransformComponent().getPositionY(), DURATION, true).ease(EaseType.Linear)
			.setCallbackMethod(new IEngineTweenMethod()
			{

				@Override
				public void onMethod(int tweenEventType, EngineTween tween) 
				{
					endOfEffect();
				}
			
			}
		);
	}

	protected void endOfEffect() 
	{
		if(_train != null)
		{
			_train.destroy();
			_train = null;
			this.getGridUserComponent().removeGridOccupyTag(GridTags.OCCUPY_TAG_BLOCKED);
		}
	}
}
