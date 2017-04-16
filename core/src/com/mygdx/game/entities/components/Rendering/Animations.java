package com.mygdx.game.entities.components.Rendering;

import java.util.HashMap;

/**
 * This is a container for RenderInfos which the AnimationComponent uses to set and play animations by generic names.
 * This allows, for example, a user of the AnimationComponent to call the 'Run' animation and it will play a generic animation under the given name.
 * This class must be cleaned by calling the 'clean' method if it is being disposed.
 * @author Ramses Di Perna
 *
 */
public class Animations
{
	private HashMap<String, RenderInfo> _animations = new HashMap<String, RenderInfo>();
	
	private String _defaultAnimationName;
	
	/**
	 * The animation needs a default starting point. This starting point can be overwritten by 'setAnimation' but can not be removed with 'removeAnimation'
	 * @param defaultAnimationName to call the default animation by. This can be requested by the method 'getDefaultAnimationName'
	 * @param defaultAnimationRenderInfo which is used as animation. This can be requested by the method 'getDefaultAnimation'
	 */
	public Animations(String defaultAnimationName, RenderInfo defaultAnimationRenderInfo)
	{
		_defaultAnimationName = defaultAnimationName;
		setAnimation(defaultAnimationName, defaultAnimationRenderInfo);
	}
	
	/**
	 * Creates or replaces an animation under the given name with the given RenderInfo
	 * @param name to call the animation by
	 * @param renderInfo which is used as animation
	 */
	public void setAnimation(String name, RenderInfo renderInfo)
	{
		_animations.put(name, renderInfo);
	}
	
	/**
	 * Removes an animation under the given name from this Animations class.
	 * @param name to remove
	 */
	public void removeAnimation(String name)
	{
		if(name == _defaultAnimationName) { return; } // The default animation can not be removed, only replaced.
		_animations.remove(name);
	}
	
	/**
	 * Returns an animation added with 'setAnimation' or the default animation name.
	 * If an animation under the given name is not found, there will be a warning message and the return value will be 'null'
	 * @param name of the needed animation
	 * @return The RenderInfo which can be used as animation. Returns 'null' if the given name was not added to this Animations class
	 */
	public RenderInfo getAnimation(String name)
	{
		if(_animations.containsKey(name))
			return _animations.get(name);
		
		System.out.println("WARNING: Animation with name '" + name + "' not defined!");
		return null;
			
	}
	
	/**
	 * Returns the default animation name given in the constructor.
	 * @return Name of default animation
	 */
	public String getDefaultAnimationName()
	{
		return _defaultAnimationName;
	}
	
	/**
	 * Returns the default animation given in the constructor.
	 * @return The RenderInfo which represents the default animation.
	 */
	public RenderInfo getDefaultAnimation()
	{
		return getAnimation(_defaultAnimationName);
	}
	
	/**
	 * This cleans all saved memory from the class and makes it 'null'.
	 * This breaks the class for future use and the class references should be set to 'null' after this method is called
	 */
	public void clean()
	{
		_animations.clear();
		_animations = null;
		_defaultAnimationName = null;
	}
}
