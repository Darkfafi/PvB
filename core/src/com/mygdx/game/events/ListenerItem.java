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
	private IEventReceiver _listener;
	
	public ListenerItem(String type, IEventReceiver listener) 
	{
		_type = type;
		_listener = listener;
	}
	
	/**
	 * Returns the listener linked to the ListenerItem
	 * @return IEventReceiver of ListenerItem
	 */
	public IEventReceiver getListener()
	{
		return _listener;
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
