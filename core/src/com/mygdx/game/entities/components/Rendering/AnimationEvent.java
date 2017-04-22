package com.mygdx.game.entities.components.Rendering;

import com.mygdx.game.events.Event;

public class AnimationEvent extends Event
{
	private String _animationName;
	
	public AnimationEvent(String type, String animationName) {
		super(type);
		_animationName = animationName;
	}
	
	public String getAnimationName()
	{
		return _animationName;
	}
	
	@Override
	public void clean()
	{
		super.clean();
		_animationName = null;
	}
}
