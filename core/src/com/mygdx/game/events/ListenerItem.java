package com.mygdx.game.events;

/**
 * This class is a tag listener tag. It is added when an EventDispatcher listens to another EventDispatcher
 * It contains all the data of what it is listening to, who is listening and what method to trigger.
 * @author Ramses Di Perna
 *
 */
public class ListenerItem 
{

	private String _type;
	private EventMethodData _methodData;
	
	public ListenerItem(String type, EventMethodData methodData) 
	{
		_type = type;
		_methodData = methodData;
	}
	
	/**
	 * Returns the EventMethodData linked to the ListenerItem
	 * @return EventMethodData of ListenerItem
	 */
	public EventMethodData getMethodData()
	{
		return _methodData;
	}
	
	/**
	 * Returns the message type this ListenerItem is linked to
	 * @return Message type of ListenerItem
	 */
	public String getType()
	{
		return _type;
	}

}
