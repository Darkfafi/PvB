package com.mygdx.engine.events;

import java.util.ArrayList;

import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * This class handles the Event's method calling and order of calling.
 * @author Ramses Di Perna
 */
public class EventQueueRoom 
{
	/**
	 * Adds an event to the queue to be fired by the dispatcher.
	 * @param event to dispatch
	 * @param dispatcher which fires the event
	 * @throws ReflectionException whether the method linked to the event was successfully invoked.
	 */
	public static void addQueueItem(Event event, EventDispatcher dispatcher) throws ReflectionException
	{
		ArrayList<ListenerItem> listListeners = dispatcher.getAllListeners();
		event.setDispatcher(dispatcher);
		callMethodsInListOfEvent(listListeners, event);
	}
	
	/**
	 * 
	 * @param listToLoop for all listenerItems attached to the dispatcher.
	 * @param event which is linked to the listnerItems to call their methods for.
	 * @throws ReflectionException whether the method linked to the event was successfully invoked.
	 */
	private static void callMethodsInListOfEvent(ArrayList<ListenerItem> listToLoop, Event event) throws ReflectionException
	{
		ArrayList<ListenerItem> items = listToLoop;
		
		if(items.size() > 0)
		{
			for(int i = 0; i < items.size(); i++)
			{
				if(listToLoop.get(i).getType() == event.getType())
					items.get(i).getListener().onReceiveEvent(event);
			}
		}
		
		event.clean();
	}
}
