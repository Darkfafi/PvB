package com.mygdx.game.components;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.entities.weapons.IWeapon;
import com.mygdx.game.globals.InputGlobals;
import com.mygdx.game.touchinput.TouchEvent;

/**
 * This component allows a user to take control over an IWeapon using its touch input
 * @author Ramses Di Perna
 *
 */
public class PlayerWeaponControlComponent extends BaseEntityComponent implements IEventReceiver
{
	private static final int TOUCH_UNUSED = -1337;
	
	private IWeapon _weaponControlling;
	private int _touchInControll = TOUCH_UNUSED;
	
	/**
	 * Sets the current weapon the player controls with its touch input
	 * @param weapon to control
	 */
	public PlayerWeaponControlComponent(IWeapon weaponControlling)
	{
		MyGdxGame.getInputHandler().addEventListener(InputGlobals.TOUCH_EVENT, this);
		setWeaponControlling(weaponControlling);
	}
	
	/**
	 * Sets the current weapon the player controls with its touch input
	 * @param weapon to control
	 */
	public void setWeaponControlling(IWeapon weapon)
	{
		_weaponControlling = weapon;
	}
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == InputGlobals.TOUCH_EVENT)
		{
			onTouchEvent((TouchEvent)event);
		}
	}
	
	protected void onTouchEvent(TouchEvent event) 
	{
		int x = event.getTouchX();
		int y = event.getTouchY();
		
		switch(event.getTouchType())
		{
		case Dragged:
			if(_touchInControll != TOUCH_UNUSED && event.getPointer() == _touchInControll)
			{
				_weaponControlling.inControl(x, y);
			}
			break;
		case Ended:
			if(_touchInControll != TOUCH_UNUSED && event.getPointer() == _touchInControll)
			{
				_weaponControlling.endControl(x, y);
				_touchInControll = TOUCH_UNUSED;
			}
			break;
		case Started:
			if(_touchInControll == TOUCH_UNUSED)
			{
				_weaponControlling.startControl(x, y);
				_touchInControll = event.getPointer();
			}
			break;
		default:
			return;
		}
	}

	@Override
	protected void updated(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroyed() 
	{
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
		_weaponControlling = null;
	}

	@Override
	protected void activeStateChanged() 
	{
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
		
		if(this.isActive())
		{
			MyGdxGame.getInputHandler().addEventListener(InputGlobals.TOUCH_EVENT, this);
		}
	}
}
