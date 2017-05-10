package com.mygdx.game.touchinput;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.events.Event;

/**
 * This class or an extending class is an event which is dispatched for touch events.
 * @author Ramses Di Perna
 *
 */
public class TouchEvent extends Event 
{
	/**
	 * The type of touch which the touch represents
	 * Inc. Started, Dragged and Ended
	 * @author Ramses Di Perna
	 *
	 */
	public enum TouchType
	{
		/**
		 * When the touch has just been started
		 */
		Started,
		/**
		 * When the already started touch position changed
		 */
		Dragged,
		/**
		 * When the touch has been ended
		 */
		Ended
	}
	
	private int _pointer = -1;
	private Vector2 _location;
	private TouchType _touchType;
	private int _touchLayer;
	
	/**
	 * The touch event with all info
	 * @param location the touch is on the screen
	 * @param pointer to the specific touch. Can be seen as an Id for the touch
	 * @param touchType to identify what kind of touch it is.
	 * @param type of event which the event represents.
	 */
	public TouchEvent(Vector2 location, int pointer, TouchType touchType, String type, int touchLayer) 
	{
		super(type);
		_pointer = pointer;
		_location = location;
		_touchType = touchType;
		_touchLayer = touchLayer;
	}
	
	/**
	 * Gets the X position of the touch
	 * @return The x position of the touch.
	 */
	public int getTouchX()
	{
		return (int)_location.x;
	}
	
	/**
	 * Gets the Y position of the touch
	 * @return The y position of the touch.
	 */
	public int getTouchY()
	{
		return (int)_location.y;
	}
	
	/**
	 * The Pointer which represents the specific touch. Can be seen as an Id for the touch
	 * @return The pointer of the touch
	 */
	public int getPointer()
	{
		return _pointer;
	}
	
	/**
	 * The type of the touch represents to what happened in the touch.
	 * @return the touchType of the touch
	 */
	public TouchType getTouchType()
	{
		return _touchType;
	}
	
	/**
	 * Returns the layer in which the touch occured.
	 * @return The Touch layer as integer.
	 */
	public int getTouchLayer()
	{
		return _touchLayer;
	}
	
	@Override
	public void clean()
	{
		super.clean();
		_location = null;
	}
}
