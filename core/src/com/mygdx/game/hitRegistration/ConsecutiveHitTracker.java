package com.mygdx.game.hitRegistration;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.engine.Engine;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.entities.Player;
import com.mygdx.game.factories.EffectFactory;
import com.mygdx.game.score.GameScoreSystem;

/**
 * This class tracks all the consecutive hits.
 * It will call the upgrade weapon method on the player when it hits the consecutive hits mark
 * This system will only register hits which had the hit type 'HitGlobals.TYPE_CONSECUTIVE_HIT_TRACKING' in them
 * @author Ramses Di Perna
 *
 */
public class ConsecutiveHitTracker implements IEventReceiver
{
	public static final int CONSECUTIVE_HITS_FOR_UPGRADE = 5;
	
	private enum ConsecutiveHitType
	{
		Hit,
		Exit
	}
	
	private Player _player;
	private int _consecutiveHits = 0;
	private int _newMultiplier = 1;
	
	public ConsecutiveHitTracker(Player player) 
	{
		_player = player;
		HitRegistrationPoint.getInstance().addEventListener(HitRegistrationPoint.HIT_REGISTRATED_EVENT, this);
	}

	public void clean() 
	{
		_player = null;
		HitRegistrationPoint.getInstance().removeEventListener(HitRegistrationPoint.HIT_REGISTRATED_EVENT, this);
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == HitRegistrationPoint.HIT_REGISTRATED_EVENT)
		{
			onHitRegistrationEvent((HitRegistrationEvent)event);
		}
	}

	/**
	 * Resets the consecutive hits counter to 0
	 */
	public void resetConsecutiveHitCounter()
	{
		_consecutiveHits = 0;
	}
	
	/**
	 * Listens to hits and if it contains a hit type 'TYPE_CONSECUTIVE_HIT_TRACKING', it will register it as as consecutive hit data.
	 * @param event
	 */
	private void onHitRegistrationEvent(HitRegistrationEvent event) 
	{	
		if(!HitGlobals.isHitType(event.getHitTypes(), HitGlobals.TYPE_CONSECUTIVE_HIT_TRACKING)){ return; }
		
		if(event.getHitEntity() != null)
			consecutiveHitRegistration(ConsecutiveHitType.Hit, event.getRegistrationX(), event.getRegistrationY() + 80, event.getHitEntity());
		else
			consecutiveHitRegistration(ConsecutiveHitType.Exit, event.getRegistrationX(), event.getRegistrationY(), event.getHitEntity());
	}
	
	/**
	 * Registers a consecutive hit. 
	 * @param type == Consecutive Hit type. Whether the hit was a streak or an ending streak
	 * @param x is the position on where it happened in the x axis
	 * @param y is the position on where it happened in the y axis
	 * @param entity which was hit
	 */
	private void consecutiveHitRegistration(ConsecutiveHitType type, float x, float y, BaseEntity entity)
	{
		String font = GameFontResources.MULTIPLIER_FONT_BANDIDOS;
		float size = 7;
		float wait = 0;
		if(type == ConsecutiveHitType.Hit)
		{
			_consecutiveHits ++;
			if(_consecutiveHits % CONSECUTIVE_HITS_FOR_UPGRADE == 0)
			{
				font = GameFontResources.WAVE_FONT_BANDIDOS;
				size = 8;
				wait = 10;
				final TextEntity me = EffectFactory.createTextEffect(Engine.getFontResources().getFontData(GameFontResources.WHITE_BANDIDOS), " x" + (GameScoreSystem.getInstance().getMultiplier()), size, x - 30, y + 50, x - 70, y + 110, 20, 15, 0.2f);
				_newMultiplier = GameScoreSystem.getInstance().getMultiplier() + 1;
				me.getRenderComponent().setColor(0.6f, 0.6f, 0.6f, 0.7f);
				me.getRenderComponent().doColor(new Color(57f / 255f, 160f / 255f, 69f / 255f, 1), 0.5f, TweenStartType.GameTime).setCallbackMethod(new IEngineTweenMethod(){

					@Override
					public void onMethod(int tweenEventType, EngineTween tween) 
					{
						me.setText(" x" + (GameScoreSystem.getInstance().getMultiplier() + 1));
						GameScoreSystem.getInstance().setMultiplier(_newMultiplier);
					}
				});
			}
			
			final TextEntity e = EffectFactory.createTextEffect(Engine.getFontResources().getFontData(font),  _consecutiveHits + " Hits", size, x - 30, y + 50, x - 60, y + 80, 0, 0, wait);
			if(wait != 0)
			{
				e.getTransformComponent().doPosition(_player.getCurrentWeapon().getTransformComponent().getPositionX(), _player.getCurrentWeapon().getTransformComponent().getPositionY(), 1f, TweenStartType.GameTime).ease(EaseType.CubicIn).delay(0.5f).setCallbackMethod(new IEngineTweenMethod()
				{
					@Override
					public void onMethod(int tweenEventType, EngineTween tween) 
					{
						EffectFactory.createHitEffect(e.getTransformComponent().getPositionX(), e.getTransformComponent().getPositionY() + 30, 1.2f, Color.YELLOW).getAnimationComponent().setSortingLayer(10);
						e.destroy();
						_player.giveSpecial(Player.ALL_SPECIAL_ARROWS[(int)Math.round((Player.ALL_SPECIAL_ARROWS.length - 1) * Math.random())]);
					}
				}
				);
			}
		}
		else if(type == ConsecutiveHitType.Exit)
		{
			resetConsecutiveHitCounter();
			GameScoreSystem.getInstance().setMultiplier(1);
			_newMultiplier = 1;
		}
	}
}

