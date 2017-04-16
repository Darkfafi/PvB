package com.mygdx.game.entities.components.Rendering;

import java.util.HashMap;

public class Animations 
{
	private HashMap<String, RenderInfo> _animations = new HashMap<String, RenderInfo>();
	
	private String _defaultAnimation;
	
	public Animations(String defaultAnimationName, RenderInfo defaultAnimationRenderInfo)
	{
		_defaultAnimation = defaultAnimationName;
		addAnimation(defaultAnimationName, defaultAnimationRenderInfo);
	}
	
	public void addAnimation(String name, RenderInfo renderInfo)
	{
		_animations.put(name, renderInfo);
	}
	
	public void removeAnimation(String name)
	{
		if(name == _defaultAnimation) { return; } // The default animation can not be removed, only replaced.
		_animations.remove(name);
	}
	
	public RenderInfo getAnimation(String name)
	{
		return _animations.get(name);
	}
	
	public String getDefaultAnimationName()
	{
		return _defaultAnimation;
	}
	
	public RenderInfo getDefaultAnimation()
	{
		return getAnimation(_defaultAnimation);
	}
}
