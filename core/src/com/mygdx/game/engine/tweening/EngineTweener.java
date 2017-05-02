package com.mygdx.game.engine.tweening;

import aurelienribon.tweenengine.TweenManager;

/**
 * This class keeps track of all the different kinds of TweenManager classes which are needed in the project.
 * It generates the TweenManager channels which are not active.
 * It also creates EngineTween instances for tweens and puts them on the given channel.
 * Note: The TweenManagers must be manually updated! 
 * @author Ramses Di Perna
 *
 */
public class EngineTweener 
{
	public static final int COMPONENT_CHANNEL = 0;
	
	private static TweenManager[] _tweenManagers = new TweenManager[1]; 
	
	/**
	 * Starts a Tween on the given channel and returns an EngineTween which represents the started tween.
	 * @param tween to start through the Engine and to create an EngineTween for
	 * @param channel to start the tween on
	 * @return The EngineTween instance which represents the Tween started.
	 */
	public static EngineTween startTween(EngineTween tween, int channel)
	{
		tween.getTween().start(getTweenManager(channel));
		return tween;
	}
	
	/**
	 * Gets a TweenManager which represetns the given channel.
	 * If there is no TweenManager which represents the channel, then the TweenManger is automatically created. 
	 * @param channel to get a TweenManager for
	 * @return TweenManager which represetns the given channel
	 */
	public static TweenManager getTweenManager(int channel)
	{
		if(channel < 0 || channel >= _tweenManagers.length) 
		{
			System.out.println("CHANNEL " + channel + " NOT DEFINED!");
			return null;
		}
		
		if(_tweenManagers[channel] == null)
		{
			_tweenManagers[channel] = new TweenManager();
		}
		
		return _tweenManagers[channel];
	}
}
