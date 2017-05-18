package com.mygdx.game.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.components.PlayerWeaponControlComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.weapons.BaseWeapon;
import com.mygdx.game.entities.weapons.BowWeapon;
import com.mygdx.game.events.TutorialEvent;
import com.mygdx.game.factories.EnemyFactory;
import com.mygdx.game.level.Playfield;

import aurelienribon.tweenengine.Timeline;

/**
 * This entity will handle the full course of a bow demonstration animation
 * @author Djamali Jones
 *
 */
public class BowDemonstrationTutorial extends BaseEntity implements IEventReceiver
{
	/*
	 * This Event is Fired by the Tutorial when it is done.
	 * */
	public static final String TUTORIAL_DONE = "TutorialDone";
	
	private enum TutorialStates
	{
		TutorialNotRunningState, 
		ShootEnemyState, 
		ShootTrapState, 
		TutorialOverState
	}
	
	private Player _player;
	private BaseWeapon _bowWeapon;
	private Playfield _playfield;
	
	private TutorialStates _tutorialStates;
	
	private boolean _tutorialOver = false;
	private boolean _checkedForState = false;
	private boolean _pulling = false;
	private boolean _release = false;

	private int _enemyCounter = 3;
	
	private Enemy _enemy1;
	private Enemy _enemy2;
	private Enemy _enemy3;
	
	public boolean isTutorialOver()
	{
		return _tutorialOver;
	}
	
	public BowDemonstrationTutorial(Player player, BaseWeapon bow, Playfield playfield)
	{
		this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_UP), true)).setPivot(new Vector2(0f, 1), false);
		this.getComponent(RenderComponent.class).setActiveState(false);
		_player = player;
		_bowWeapon = bow;
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
			_tutorialOver = true;
			this.dispatchEvent(new TutorialEvent(TUTORIAL_DONE, _tutorialOver));
			break;
		default:
			break;
		}
	}
	
	private void shootEnemyState()
	{
		_enemy1 = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.MediumBandit);
		_enemy1.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() + 100);
		
		Timeline tl = Timeline.createSequence();
		tl.beginSequence();
		
		//Tween Enemy to specific position.
		tl.push(_enemy1.getTransformComponent().doPosition(Engine.getWidth() / 2, 600, 2, true).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) 
				{
					_enemy1.setEnemyState(Enemy.EnemyState.IdleState, true);
				}
			}
		).getTween());
		
		//Wait for 1 second.
		tl.delay(1f);
		
		//Show Finger Dragging on the Screen.
		getTransformComponent().setPosition(-100, -100);
		getComponent(RenderComponent.class).setActiveState(true);
		tl.push(this.getTransformComponent().doPosition(Engine.getWidth() / 2, 900, 2, true).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) 
				{
					//Activate Player Bow Input
					_player.getComponent(PlayerWeaponControlComponent.class).setActiveState(true);
					//Change Hand Sprite to Dragging Hand Sprite.
					getComponent(RenderComponent.class).setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_DOWN));
				}	
			}
		).getTween());
		//Wait for 2 seconds.
		tl.delay(2f);
		//Tween Hand to yPos - Max Draw Length.
		tl.push(this.getTransformComponent().doPosition(Engine.getWidth() / 2, 900 - BowWeapon.MAX_DRAW_LENGTH, 4, true).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween)
				{
					getComponent(RenderComponent.class).setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_UP));
				}
			}
		).getTween().repeat(3, 1f));
		
		tl.delay(1f);
		tl.push(this.getTransformComponent().doPosition(-100, -100, 2, true).getTween());
		
		_enemy1.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		
		tl.end();
		this.getTransformComponent().startTimelineOnComponent(tl, true);
	}
	
	private void shootTrapState()
	{		
		Vector2 enemy2Pos = new Vector2(Engine.getWidth() / 2 + 200, Engine.getHeight() + 100);
		Vector2 enemy3Pos = new Vector2(Engine.getWidth() / 2 + 250, Engine.getHeight() + 100);
		
		_enemy2 = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.MediumBandit);
		_enemy2.getTransformComponent().setPosition(enemy2Pos);
		
		_enemy3 = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.MediumBandit);
		_enemy3.getTransformComponent().setPosition(enemy3Pos);
		
		Timeline tl = Timeline.createSequence();
		tl.beginSequence();
		
		tl.push(_enemy2.getTransformComponent().doPosition(enemy2Pos.x, 500, 2, true).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) {
					_enemy2.setEnemyState(Enemy.EnemyState.IdleState, true);
				}
			}
		).getTween());
		
		tl.push(_enemy3.getTransformComponent().doPosition(enemy3Pos.x, 450, 2, true).setCallbackMethod(
			new IEngineTweenMethod()
			{
				@Override
				public void onMethod(int tweenEventType, EngineTween tween) {
					_enemy3.setEnemyState(Enemy.EnemyState.IdleState, true);
				}
			}
		).getTween());
		
		_enemy2.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		_enemy3.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		
		tl.end();
		this.getTransformComponent().startTimelineOnComponent(tl, true);
	}
	
	/**
	 * Changes the State of the Tutorial to the one given in the parameter.
	 * @param ts
	 */
	private void changeState(TutorialStates ts)
	{
		_tutorialStates = ts;
	}
	
	/**
	 * Makes the computer shoot a target in the given direction using the given coordinates
	 * @param x position to start shooting at
	 * @param y position to start shooting at 
	 * @param duration to how long the shot must take
	 * @param delay on how long to wait until it does this animation.
	 */
	public void shootUsingTarget(final float x, final float y, final float duration, float delay)
	{
		_release = false;
		_pulling = false;
		
		this.getTransformComponent().doPosition(x, y, duration / 2f, true).setCallbackMethod(
				
				new IEngineTweenMethod()	
				{

					@Override
					public void onMethod(int tweenEventType, EngineTween tween) 
					{
						_bowWeapon.startControl((int)getTransformComponent().getPositionX(), (int)getTransformComponent().getPositionY());
						_pulling = true;
						getComponent(RenderComponent.class).setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_DOWN));
						
						getTransformComponent().doPosition(x, y - BowWeapon.MAX_DRAW_LENGTH, duration / 2f, true).setCallbackMethod(
								
								new IEngineTweenMethod()	
								{

									@Override
									public void onMethod(int tweenEventType, EngineTween tween) 
									{
										_release = true;
									}
								}
						);
					}
				}
		).getTween().delay(delay);
	}

	@Override
	protected void awake() 
	{
		
	}

	@Override
	protected void updated(float dt) 
	{
		if(_pulling)
		{
			_bowWeapon.inControl((int)getTransformComponent().getPositionX(), (int)getTransformComponent().getPositionY());
			if(_release)
			{
				_bowWeapon.endControl((int)getTransformComponent().getPositionX(), (int)getTransformComponent().getPositionY());
				getComponent(RenderComponent.class).setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TOUCH_UP));
				_release = false;
				_pulling = false;
			}
		}
		
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
		_bowWeapon = null;
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
