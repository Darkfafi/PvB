package com.mygdx.game.events;

import java.util.ArrayList;

import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * Every class which inherits from the EventDispatcher class has the ability to dispatch and listen to events.
 * When the class dispatches an event, every EventDispatcher listening to this class will receive that event.
 * @author Ramses Di Perna
 */
public abstract class EventDispatcher 
{
	private ArrayList<ListenerItem> _allListeners = new ArrayList<ListenerItem>();
	private ArrayList<ListenerItem> _allListenersListeningTo = new ArrayList<ListenerItem>();
	
	/**
	 * Adds an event listener to this EventDispatcher.
	 * NOTE: You must call the method 'removeEventListener' to remove the listen from the EventDispatcher
	 * @param type of message to listen to.
	 * @param methodData containing the listener and the method to trigger on receiving the event.
	 */
	public void addEventListener(String type, EventMethodData methodData)
	{
		methodData.ObjectAddedTo = this;
		ListenerItem item = new ListenerItem(type, methodData);
		_allListeners.add(item);
		methodData.getMethodHolder()._allListenersListeningTo.add(item);
	}
	
	/**
	 * This dispatches an event which will trigger the effect for all who listen to the type.
	 * @param event to dispatch
	 */
	public void dispatchEvent(Event event)
	{
		try 
		{
			EventQueueRoom.addQueueItem(event, this);
		} 
		catch (ReflectionException e) 
		{	
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the event which the was being listened to by the caller.
	 * @param type of message to remove the listener from
	 * @param methodData containing the listener and method to remove from the event listening link.
	 */
	public void removeEventListener(String type, EventMethodData methodData)
	{
		ListenerItem currentItem;
		if(_allListeners.size() > 0)
		{
			for(int i = 0; i < _allListeners.size(); i++)
			{
				currentItem = _allListeners.get(i);
				if(currentItem.getType().equals(type) && currentItem.getMethodData().getMethod().equals(methodData.getMethod()))
				{
					_allListeners.remove(currentItem);
					methodData.getMethodHolder()._allListenersListeningTo.remove(currentItem);
					break;
				}
			}
		}
	}
	
	/**
	 * This method returns whether the given type of message already has a listener.
	 * @param type of message to check.
	 * @return whether the message is being listened to. 'True' if so else 'False'.
	 */
	public boolean hasEventListener(String type)
	{
		if(_allListeners.size() > 0)
		{
			for(int i = 0; i < _allListeners.size(); i++)
			{
				if(_allListeners.get(i).getType() == type)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns all who listen to this dispatcher.
	 * @return list of listeners.
	 */
	public ArrayList<ListenerItem> getAllListeners()
	{
		return _allListeners;
	}
	
	/**
	 * Destroys all the listeners which where added to this EventDispatcher
	 */
	public void destroyAllListeners()
	{
		for(int i = _allListeners.size() - 1; i >= 0; i--)
		{
			this.removeEventListener(_allListeners.get(i).getType(), _allListeners.get(i).getMethodData());
		}
	}
	
	/**
	 * Destroys all listeners which this EventDispatcher has added to other EventDispatchers.
	 */
	public void destroyAllListenersAdded()
	{
		ListenerItem currentItem;
		EventDispatcher objectAddedTo;
		if(_allListenersListeningTo.size() > 0)
		{
			for(int i = _allListenersListeningTo.size() -1; i >= 0; i--)
			{
				currentItem = _allListenersListeningTo.get(i);
				objectAddedTo = currentItem.getMethodData().ObjectAddedTo;
				objectAddedTo.removeEventListener(currentItem.getType(),currentItem.getMethodData());
			}
		}
	}
}
