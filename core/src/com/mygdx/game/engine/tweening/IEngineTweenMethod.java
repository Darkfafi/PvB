package com.mygdx.game.engine.tweening;

/**
 * This interface is used as a callback trigger in the EngineTween class to call methods on Tween events
 * @author Ramses Di Perna
 *
 */
public interface IEngineTweenMethod
{
	/**
	 * The method which will be triggered on the given tween events
	 * @param tweenEventType represents one of the TweenCallback types
	 * @param tween is the tween which is part of the trigger
	 */
	public void onMethod(int tweenEventType, EngineTween tween);
}
