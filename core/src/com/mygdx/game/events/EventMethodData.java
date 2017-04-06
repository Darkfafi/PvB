package com.mygdx.game.events;

import com.badlogic.gdx.utils.reflect.Method;

/**
 * This class contains all the data about the methods which should be triggered by events
 * This holds the method and the holder of the method.
 * @author Ramses Di Perna
 *
 */
public class EventMethodData 
{
	public EventDispatcher ObjectAddedTo;
	
	private Method _method;
	private EventDispatcher _holder;
	
	public EventMethodData(Method method, EventDispatcher holderMethod) 
	{
		_method = method;
		_holder = holderMethod;
	}
	
	/**
	 * The method which should be triggered by the linked event.
	 * @return Method in EventMethodData
	 */
	public Method getMethod()
	{
		return _method;
	}
	
	/**
	 * The Class which holds the method which should be triggered by the linked event.
	 * @return EventDispatcher in EventMethodData
	 */
	public EventDispatcher getMethodHolder()
	{
		return _holder;
	}
}
