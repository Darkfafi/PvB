package com.mygdx.game.touchinput;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.events.Event;

public class TouchEvent extends Event 
{
	private int _pointer = -1;
	private Vector2 _location;
	
	public TouchEvent(Vector2 location, int pointer, String type) 
	{
		super(type);
		_pointer = pointer;
		_location = location;
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
}
