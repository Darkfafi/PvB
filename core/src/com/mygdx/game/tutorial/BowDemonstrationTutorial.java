package com.mygdx.game.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.components.PlayerWeaponControlComponent;
import com.mygdx.game.engine.Engine;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.weapons.BowWeapon;
import com.mygdx.game.events.TutorialEvent;
import com.mygdx.game.factories.EnemyFactory;
import com.mygdx.game.level.Playfield;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

/**
 * This entity will handle the full course of the interactive tutorial
 * @author Djamali Jones & Ramses Di Perna
 *
 */
public class BowDemonstrationTutorial extends BaseEntity implements IEventReceiver
{
	/*
	 * This Event is Fired by the Tutorial when it is done.
	 * */
	public static final String TUTORIAL_DONE_EVENT = "TutorialDoneEvent";
	
	private enum TutorialStates
	{
		TutorialNotRunningState, 
		ShootEnemyState, 
		ShootTrapState, 
		TutorialOverState
	}
	
	private Player _player;
	private Playfield _playfield;
	
	private TutorialStates _tutorialStates;
	
	private boolean _tutorialOver = false;
	private boolean _checkedForState = false;
	private int _enemyCounter = 3;
	private float _tutorialSpeed = 2;
	
	private Enemy _enemy1;
	private Enemy _enemy2;
	private Enemy _enemy3;
	
	/**
	 * Returns whether the tutorial is over or not.
	 * @return true if the tutorial is over, else false
	 */
	public boolean isTutorialOver()
	{
		return _tutorialOver;
	}
	
	public BowDemonstrationTutorial(Player player, Playfield playfield)
	{
		this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_UP), true)).setPivot(0f, 1, false);
		this.getComponent(RenderComponent.class).setActiveState(false);
		_player = player;
		_playfield = playfield;
		_tutorialStates = TutorialStates.TutorialNotRunningState;
		_player.getComponent(PlayerWeaponControlComponent.class).setActiveState(false);
	}
	
	/**
	 * Starts the tutorial which displays the computer taking over bow control, spawning and killing an enemy and spawning and killing an enemy with a trap.
	 * @param duration on how long this tutorial animation must take.
	 */
	public void startTutorial()
	{
		_tutorialStates = TutorialStates.ShootEnemyState;		
		tutorialOrder();
	}
	
	private void tutorialOrder()
	{
		if(_checkedForState == true) { return; }
		
		switch(_tutorialStates)
		{
		case TutorialNotRunningState:
			return;
		case ShootEnemyState:
			shootEnemyState();
			break;
		case ShootTrapState:
			shootTrapState();
			break;
		case TutorialOverState:
			this.getTransformComponent().stopAllComponentTweens();
			this.getTransformComponent().doPosition(-100, -100, _tutorialSpeed / 2, TweenStartType.GameTime).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) 
				{
					_tutorialOver = true;
					dispatchEvent(new TutorialEvent(TUTORIAL_DONE_EVENT, _tutorialOver));
				}}
			);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Handles the state where the player is asked to shoot the enemy
	 */
	private void shootEnemyState()
	{
		_playfield.forceDisableTraps();
		_enemy1 = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.MediumBandit);
		_enemy1.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() + 100);
		
		//Tween Enemy to specific position.
		_enemy1.getTransformComponent().doPosition(Engine.getWidth() / 2, 600, _tutorialSpeed, TweenStartType.GameTime).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) 
				{
					_enemy1.setEnemyState(Enemy.EnemyState.IdleState, true);
				}
			}
		);
		
		getTransformComponent().setPosition(-100, -100);
		getComponent(RenderComponent.class).setActiveState(true);
		bowDrawingMotion(Engine.getWidth() / 2, 900, _tutorialSpeed / 2, 2f, new IEngineTweenMethod()
		{
			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				//Activate Player Bow Input
				_player.getComponent(PlayerWeaponControlComponent.class).setActiveState(true);
			}	
		});
		
		_enemy1.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
	}
	
	/**
	 * Handles the state where the player is asked to shoot the trap
	 */
	private void shootTrapState()
	{		
		this.getTransformComponent().stopAllComponentTweens();
		
		getComponent(RenderComponent.class).setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_UP));
		getTransformComponent().doPosition(Engine.getWidth() * 0.4f, Engine.getHeight() * 0.4f, 1f, TweenStartType.GameTime);
		
		bowDrawingMotion(Engine.getWidth() * 0.8f, Engine.getHeight() * 0.8f, 0.5f, 2f, new IEngineTweenMethod()
		{
			@Override
			public void onMethod(int tweenEventType, EngineTween tween) {
				//Activate Player Input.
				_player.getComponent(PlayerWeaponControlComponent.class).setActiveState(true);
				//Reset Traps
				_playfield.forceResetTraps();
			}
		}
		);
		
		Vector2 enemy2Pos = new Vector2(Engine.getWidth() / 2 + 200, Engine.getHeight() + 100);
		Vector2 enemy3Pos = new Vector2(Engine.getWidth() / 2 + 250, Engine.getHeight() + 100);
		
		_enemy2 = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.MediumBandit);
		_enemy2.getTransformComponent().setPosition(enemy2Pos);
		
		_enemy3 = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.MediumBandit);
		_enemy3.getTransformComponent().setPosition(enemy3Pos);
		
		Timeline tl = Timeline.createSequence();
		tl.beginParallel();
		
		//Deactivate Player Input
		_player.getComponent(PlayerWeaponControlComponent.class).setActiveState(false);
		
		tl.push(_enemy2.getTransformComponent().doPosition(enemy2Pos.x, 500, _tutorialSpeed, TweenStartType.Timeline).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) {
					_enemy2.setEnemyState(Enemy.EnemyState.IdleState, true);
				}
			}
		).getTween());
		
		tl.push(_enemy3.getTransformComponent().doPosition(enemy3Pos.x, 450, _tutorialSpeed, TweenStartType.Timeline).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) {
					_enemy3.setEnemyState(Enemy.EnemyState.IdleState, true);
				}
			}
		).delay(0.4f).getTween());
		
		_enemy2.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		_enemy3.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		
		tl.end();
		this.getTransformComponent().startTimelineOnComponent(tl, true);
	}
	
	/**
	 * Makes the demonstration hand go to the requested position and do a bow drawing motion
	 * @param x on where to perform the motion
	 * @param y on where to perform the motion
	 * @param travelDuration on how long it takes to travel to the position
	 * @param delay is how long to wait to start moving to the given location
	 * @param method to invoke when reached the given location. (can be used to turn on player controls etc)
	 */
	private void bowDrawingMotion(final float x, final float y, float travelDuration,float delay, final IEngineTweenMethod method)
	{
		this.getTransformComponent().doPosition(x, y, travelDuration, TweenStartType.GameTime).setCallbackMethod(
				new IEngineTweenMethod()
				{
					@Override
					public void onMethod(int tweenEventType, EngineTween tween) 
					{
						method.onMethod(tweenEventType, tween);
						getComponent(RenderComponent.class).setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_DOWN));
						getTransformComponent().doPosition(x, y - BowWeapon.MAX_DRAW_LENGTH, _tutorialSpeed * 0.75f, TweenStartType.GameTime).getTween().repeat(Tween.INFINITY, 0.5f);
					}	
				}
			).delay(delay);
	}
	
	/**
	 * Changes the State of the Tutorial to the one given in the parameter.
	 * @param ts is the state to change it to.
	 */
	private void changeState(TutorialStates ts)
	{
		_tutorialStates = ts;
	}

	@Override
	protected void awake() 
	{
		
	}

	@Override
	protected void updated(float dt) 
	{

		
		if(_enemyCounter == 2)
		{
			changeState(TutorialStates.ShootTrapState);
			tutorialOrder();
			_checkedForState = true;
		}
		else if(_enemyCounter <= 0)
		{
			changeState(TutorialStates.TutorialOverState);
			tutorialOrder();
			_checkedForState = true;
		}
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
	}

	@Override
	protected void destroyed() 
	{
		_player = null;
		_playfield = null;
		_enemy1 = null;
		_enemy2 = null;
		_enemy3 = null;
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == HealthComponent.EVENT_HEALTH_DIED)
		{
			_enemyCounter--;
			_checkedForState = false;
		}
	}
}
