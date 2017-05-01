package com.mygdx.game.engine.tweening;

import com.mygdx.game.engine.events.Event;

/**
 * This event and all extending events are fired by the EngineTween to indicate different events of the tween they are linked to. 
 * @author Ramses Di Perna
 *
 */
public class TweenEvent extends Event 
{
	private int _tweenEventType;
	private EngineTween _engineTween;
	
	/**
	 * This
	 * @param eventType which listeners should listen to.
	 * @param tweenEventType is an integer which is part of the listed TweenCallback
	 * @param engineTween which is part of this context
	 */
	public TweenEvent(String eventType, int tweenEventType, EngineTween engineTween) 
	{
		super(eventType);
		_tweenEventType = tweenEventType;
		_engineTween = engineTween;
	}
	
	/**
	 * This is one of the TweenCallback types which is part of the event to indicate what triggered it.
	 * @return An integer which is part of the listed TweenCallback
	 */
	public int getTweenEventType()
	{
		return _tweenEventType;
	}
	
	/**
	 * Gives the EngineTween which is linked to this event.
	 * @return The EngineTween which is linked to this event.
	 */
	public EngineTween getEngineTween()
	{
		return _engineTween;
	}
	
	/**
	 * Cleans the class and makes it ready for garbage collection.
	 */
	@Override
	public void clean()
	{
		super.clean();
		
		_engineTween.destroyAllListeners();
		_engineTween = null;
	}
}
