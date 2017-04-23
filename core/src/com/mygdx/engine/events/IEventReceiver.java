package com.mygdx.engine.events;

/**
 * This interface must be implemented for a class to get a trigger on an eventDispatch its listening to.
 * @author Ramses Di Perna
 *
 */
public interface IEventReceiver 
{
	/**
	 * This will be triggered when an event has been fired of type which this implementing class was listening to.
	 * @param event which has one of the event types which is listened to.
	 */
	void onReceiveEvent(Event event);
}
