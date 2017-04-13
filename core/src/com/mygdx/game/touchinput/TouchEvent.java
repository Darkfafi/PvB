package com.mygdx.game.touchinput;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.events.Event;

public class TouchEvent extends Event 
{
	public enum TouchType
	{
		Started,
		Dragged,
		Ended
	}
	
	private int _pointer = -1;
	private Vector2 _location;
	private TouchType _touchType;
	
	public TouchEvent(Vector2 location, int pointer, TouchType touchType, String type) 
	{
		super(type);
		_pointer = pointer;
		_location = location;
		_touchType = touchType;
	}
	
	public int getTouchX()
	{
		return (int)_location.x;
	}
	
	public int getTouchY()
	{
		return (int)_location.y;
	}
	
	public int getPointer()
	{
		return _pointer;
	}
	
	public TouchType getTouchType()
	{
		return _touchType;
	}
}
