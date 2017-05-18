package com.mygdx.game.traps.types;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.collision.CollisionComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.globals.EngineGlobals;
import com.mygdx.game.engine.resources.CollisionResources;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.entities.Train;
import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.factories.TrapFactory.Direction;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.hitRegistration.HitGlobals;
import com.mygdx.game.hitRegistration.HitRegistrationPoint;
import com.mygdx.game.level.Grid;

/**
 * This spawns a train track which will spawn a passing train when triggered. Which blocks the way and kills all in its path of fury <3
 * @author Ramses Di Perna
 *
 */
public class TrainBlockTrap extends BaseBlockTrap implements IEventReceiver
{
	public static final float DURATION = 4f;
	public static final float COOLDOWN_TIME = DURATION + 8f;
	public static final float SPEED = 8;
	
	private Train _train;
	
	public TrainBlockTrap(Grid grid, Direction direction) 
	{
		super(grid, direction);
		this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TRAIN_RAIL), false)).setPivot(new Vector2(0, 0), false);
		this.getComponent(RenderComponent.class).setSortOnY(true);
		
		this.addComponent(new CollisionComponent()).addEventListener(EngineGlobals.COLLISION_EVENT_COLLISION_ENTER, this);
		getComponent(CollisionComponent.class).setActiveState(true);
		
		FixtureDef _fixDef = new FixtureDef();
		_fixDef.filter.maskBits = CollisionResources.BIT_ENEMY;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(CollisionResources.convertToPPM(this.getComponent(RenderComponent.class).getRealWidth()), CollisionResources.convertToPPM(10), new Vector2(0, CollisionResources.convertToPPM(30)), 0);
		_fixDef.shape = shape;
		this.getComponent(CollisionComponent.class).createFixture(_fixDef, CollisionResources.BIT_ARROW);
		this.getComponent(CollisionComponent.class).setActiveState(false);
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
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == EngineGlobals.COLLISION_EVENT_COLLISION_ENTER)
		{
			onCollisionEvent((CollisionEvent)event);
		}	
	}
	
	@Override
	public boolean canBeActive()
	{
		return _train == null;
	}
	
	protected void onCollisionEvent(CollisionEvent event)
	{
		if(event.getOtherCollisionComponent() == null || event.getOtherCollisionComponent().getParentOfComponent() == null) { return ;}
		HealthComponent hc = event.getOtherCollisionComponent().getParentOfComponent().getComponent(HealthComponent.class);
		if(hc != null)
		{
			hc.kill();
			HitRegistrationPoint.getInstance().register(event.getOtherCollisionComponent().getParentOfComponent().getTransformComponent().getPositionX(), event.getOtherCollisionComponent().getParentOfComponent().getTransformComponent().getPositionY(), event.getOtherCollisionComponent().getParentOfComponent(), HitGlobals.TOOL_TRAP, new int[]{ HitGlobals.TYPE_DIRECT_HIT });
		}
	}
	
	@Override
	protected void awake() 
	{

	}

	@Override
	protected void updated(float dt) 
	{
		
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
		
		this.getComponent(CollisionComponent.class).removeEventListener(EngineGlobals.COLLISION_EVENT_COLLISION_ENTER, this);
	}
	
	@Override
	protected void doEffect()
	{
		super.doEffect();
		_train = new Train((int)Math.ceil(DURATION * SPEED), this.getDirection() == TrapFactory.Direction.Left);
		
		Engine.getAudioResources().getSound(GameAudioResources.SOUND_TRAP_TRAIN_EFFECT).play();
		
		float startX = (this.getDirection() == TrapFactory.Direction.Left) ? Engine.getWidth() + _train.getTrainSize() : this.getTransformComponent().getPositionX() - _train.getTrainSize();
		float endX = (this.getDirection() == TrapFactory.Direction.Left) ? 0 - _train.getFullTrainSize() : Engine.getWidth() + _train.getFullTrainSize();
		
		_train.getTransformComponent().setPosition(startX, this.getTransformComponent().getPositionY() + 12);
		_train.getTransformComponent().doPosition(endX, _train.getTransformComponent().getPositionY(), DURATION, TweenStartType.GameTime).ease(EaseType.Linear)
			.setCallbackMethod(new IEngineTweenMethod()
			{

				@Override
				public void onMethod(int tweenEventType, EngineTween tween) 
				{
					endOfEffect();
				}
			
			}
		);
		
		this.getComponent(CollisionComponent.class).setActiveState(true);
		Engine.getSceneManager().getRenderComponents().getMainCamera().doShake(3f, DURATION);
	}
	
	/**
	 * After the train is gone, the train will be destroyed and the grid will be not occupied anymore.
	 */
	protected void endOfEffect() 
	{
		if(_train != null)
		{
			_train.destroy();
			_train = null;
		}
		this.getGridUserComponent().removeGridOccupyTag(GridTags.OCCUPY_TAG_DAMAGING);
		this.getComponent(CollisionComponent.class).setActiveState(false);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
