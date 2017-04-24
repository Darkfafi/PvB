package com.mygdx.engine.events;

/**
 * A CoreDispatcher is a Global Event Dispatcher. 
 * Here all the global events can be dispatched from.
 * @author Ramses Di Perna
 *
 */
public class GlobalDispatcher extends EventDispatcher 
{
	private static GlobalDispatcher _instance;
	
	public static GlobalDispatcher getInstance()
	{
		if(_instance == null)
		{
			_instance = new GlobalDispatcher();
		}
		
		return _instance;
	}
}
