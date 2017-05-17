package com.mygdx.game.hitRegistration;

import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
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
		if(type == ConsecutiveHitType.Hit)
		{
			_consecutiveHits ++;
			if(_consecutiveHits % CONSECUTIVE_HITS_FOR_UPGRADE == 0)
			{
				_player.giveSpecial();
				font = GameFontResources.WAVE_FONT_BANDIDOS;
				size = 8;
			}
			
			EffectFactory.createTextEffect(Engine.getFontResources().getFontData(font),  _consecutiveHits + " Hits", size, x - 30, y + 50, x - 60, y + 80);
		}
		else if(type == ConsecutiveHitType.Exit)
		{
			resetConsecutiveHitCounter();
		}
	}
}

