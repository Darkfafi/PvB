package com.mygdx.game.engine.entities.components.rendering;

import com.mygdx.game.engine.events.Event;

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
