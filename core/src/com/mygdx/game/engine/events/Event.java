package com.mygdx.game.engine.events;

/**
 * An event is an information container which can be dispatched and listened to by EventDispatchers.
 * @author Ramses Di Perna
 */
public class Event 
{
	private EventDispatcher _dispatcher;
	private String _type;
	
	public Event(String type)
	{
		_type = type;
	}
	
	/**
	 * Name of the event to determine its event type.
	 * @return Type of event.
	 */
	public String getType()
	{
		return _type;
	}
	
	/**
	 * The EventDispatcher which dispatched this event
	 * @return The EventDispatcher linked to the event
	 */
	public EventDispatcher getDispatcher()
	{
		return _dispatcher;
	}
	
	/**
	 * Sets the EventDispatcher which dispatched this event.
	 * If it already has a value, it can not be changed to another.
	 * @param The EventDispatcher to link to the event.
	 */
	public void setDispatcher(EventDispatcher dispatcherValue)
	{
		if(_dispatcher != null){ return; }
		_dispatcher = dispatcherValue;
	}

	/**
	 * Cleans The event from any references after it is used.
	 */
	public void clean()
	{
		_dispatcher = null;
		_type = null;
	}
}
