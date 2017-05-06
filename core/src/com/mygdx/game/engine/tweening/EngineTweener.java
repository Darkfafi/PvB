package com.mygdx.game.engine.tweening;

import java.util.Stack;

import aurelienribon.tweenengine.Tween;
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
	public static final int GAME_TIME_CHANNEL = 0;
	public static final int REAL_TIME_CHANNEL = 1;
	
	private static TweenManager[] _tweenManagers = new TweenManager[2]; 
	
	private static Stack<ChannelTween> _tweensToStartStack = new Stack<ChannelTween>();
	
	/**
	 * Starts a Tween on the given channel and returns an EngineTween which represents the started tween.
	 * @param tween to start through the Engine and to create an EngineTween for
	 * @param channel to start the tween on
	 * @return The EngineTween instance which represents the Tween started.
	 */
	public static EngineTween startTween(Tween tween, int channel)
	{
		EngineTween t = new EngineTween(tween);
		_tweensToStartStack.add(new ChannelTween(t, channel));
		return t;
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
	
	public static void updateTweenEngine(float unScaledDeltaTime, float timeScale)
	{
		while(!_tweensToStartStack.isEmpty())
		{
			ChannelTween ct = _tweensToStartStack.pop();
			
			if(ct.Tween.isCleaned()) { continue; }
			
			ct.Tween.getTween().start(getTweenManager(ct.Channel));
			ct.Tween = null;
		}
		
		for(int i = 0; i < _tweenManagers.length; i++)
		{
			float dt = (i == 1) ? unScaledDeltaTime : unScaledDeltaTime * timeScale;
			if(_tweenManagers[i] != null)
				_tweenManagers[i].update(dt);
		}
	}
	
	private static class ChannelTween
	{
		public EngineTween Tween;
		public int Channel;
		
		public ChannelTween(EngineTween t, int c)
		{
			Tween = t;
			Channel = c;
		}
	}
}
