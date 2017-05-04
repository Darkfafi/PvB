package com.mygdx.game.ui;

import com.mygdx.game.engine.events.Event;

/**
 * 
 * @author Djamali
 *
 */
public class ButtonEvent extends Event 
{
	
	public enum ButtonState
	{
		/*
		 * When the button is held down.
		 */
		Down, 
		/*
		 * When the button has been released.
		 */
		Up
	}

	private ButtonState _buttonState;
	
	public ButtonEvent(ButtonState buttonState, String type) {
		super(type);
		_buttonState = buttonState;
		// TODO Auto-generated constructor stub
	}
	
	public ButtonState getButtonState()
	{
		return _buttonState;
	}
	
	@Override
	public void clean()
	{
		super.clean();
	}
	
}
