package com.mygdx.game.engine.tweening;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class EngineTweener 
{
	public static final int COMPONENT_CHANNEL = 0;
	
	private static TweenManager[] _tweenManagers = new TweenManager[1]; 
	
	public static Tween startTween(Tween tween, int channel)
	{
		return tween.start(getTweenManager(channel));
	}
	
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
