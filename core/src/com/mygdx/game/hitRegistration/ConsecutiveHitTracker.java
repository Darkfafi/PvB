package com.mygdx.game.hitRegistration;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
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

/**
 * This class tracks all the consecutive hits.
 * It will call the upgrade weapon method on the player when it hits the consecutive hits mark
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

	public void resetConsecutiveHitCounter()
	{
		_consecutiveHits = 0;
	}
	
	private void onHitRegistrationEvent(HitRegistrationEvent event) 
	{	
		if(!HitGlobals.isHitType(event.getHitTypes(), HitGlobals.TYPE_CONSECUTIVE_HIT_TRACKING)){ return; }
		
		if(event.getHitEntity() != null)
			consecutiveHitRegistration(ConsecutiveHitType.Hit, event.getRegistrationX(), event.getRegistrationY() + 80, event.getHitEntity());
		else
			consecutiveHitRegistration(ConsecutiveHitType.Exit, event.getRegistrationX(), event.getRegistrationY(), event.getHitEntity());
	}
	
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
						_player.giveSpecial();
					}
				}
				);
			}
		}
		else if(type == ConsecutiveHitType.Exit)
		{
			resetConsecutiveHitCounter();
		}
	}
}

