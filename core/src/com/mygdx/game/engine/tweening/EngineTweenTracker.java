package com.mygdx.game.engine.tweening;

import java.util.ArrayList;

/**
 * This class can be used to keep track of EngineTween instances. 
 * If the tracker is updated with the 'updateTracker' method, it removes non active tweens automatically.
 * @author Ramses Di Perna
 *
 */
public class EngineTweenTracker 
{
	public ArrayList<EngineTween> _activeTweens = new ArrayList<EngineTween>();
	
	/**
	 * Registers a Tween to track.
	 * @param tween to track
	 */
	public void registerTween(EngineTween tween)
	{
		if(_activeTweens.contains(tween)) { return; }
		_activeTweens.add(tween);
	}
	
	/**
	 * unregisters a Tween from the tracker.
	 * @param tween to track
	 */
	public void unregisterTween(EngineTween tween)
	{
		if(!_activeTweens.contains(tween)) { return; }
		
		if(!tween.getTween().isFinished())
		{
			tween.clean();
		}
		
		_activeTweens.remove(tween);
	}
	
	/**
	 * Updates the EngineTweenTracker and removes non active tweens automatically.
	 */
	public void updateTracker()
	{
		for(int i = _activeTweens.size() - 1; i >= 0; i--)
		{
			if(_activeTweens.get(i).getTween().isFinished())
				unregisterTween(_activeTweens.get(i));
		}
	}
	
	/**
	 * Stops all tweens which are tracked by the tracker and removes them.
	 */
	public void stopAllTweens()
	{
		for(int i = _activeTweens.size() - 1; i >= 0; i--)
		{
			_activeTweens.get(i).getTween().kill();
		}
		
		_activeTweens.clear();
	}
	
	public void clean()
	{
		stopAllTweens();
		_activeTweens = null;
	}
}

