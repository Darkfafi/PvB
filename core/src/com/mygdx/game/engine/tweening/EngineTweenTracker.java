package com.mygdx.game.engine.tweening;

import java.util.ArrayList;

import aurelienribon.tweenengine.Timeline;

/**
 * This class can be used to keep track of EngineTween instances & Timeline instances. 
 * If the tracker is updated with the 'updateTracker' method, it removes non active tweens automatically.
 * @author Ramses Di Perna
 *
 */
public class EngineTweenTracker 
{
	public ArrayList<EngineTween> _activeTweens = new ArrayList<EngineTween>();
	public ArrayList<Timeline> _activeTimelines = new ArrayList<Timeline>();
	
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
	 * Registers a Timeline to track
	 * @param timeline to track
	 */
	public void registerTimeline(Timeline timeline)
	{
		if(_activeTimelines.contains(timeline)) { return;}
		_activeTimelines.add(timeline);
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
	 * unregisters a Timeline from the tracker.
	 * @param timeline to track to track
	 */
	public void unregisterTimeline(Timeline timeline)
	{
		if(!_activeTimelines.contains(timeline)) { return; }
		
		if(!timeline.isFinished())
		{
			timeline.kill();
		}
		
		_activeTimelines.remove(timeline);
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
		
		for(int i = _activeTimelines.size() - 1; i >= 0; i--)
		{
			if(_activeTimelines.get(i).isFinished())
				this.unregisterTimeline(_activeTimelines.get(i));
		}
	}
	
	/**
	 * Stops all tweens which are tracked by the tracker and removes them.
	 */
	public void stopAllTweens()
	{
		for(int i = _activeTweens.size() - 1; i >= 0; i--)
		{
			_activeTweens.get(i).clean();
		}
		
		for(int i = _activeTimelines.size() - 1; i >= 0; i--)
		{
			_activeTimelines.get(i).kill();
		}
		
		_activeTweens.clear();
		_activeTimelines.clear();
	}
	
	/**
	 * Cleans the class and makes it ready for garbage collection.
	 */
	public void clean()
	{
		stopAllTweens();
		_activeTweens = null;
		_activeTimelines = null;
	}
}

