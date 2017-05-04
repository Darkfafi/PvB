package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.globals.ButtonGlobals;
import com.mygdx.game.globals.InputGlobals;
import com.mygdx.game.touchinput.TouchEvent;
import com.mygdx.game.ui.ButtonEvent;

/**
 * An entity for a Button. This can be used for whenever a button is needed.
 * For example a play button for on the main menu, or a pause button.
 * @author Djamali
 *
 */
public class ButtonEntity extends BaseEntity implements IEventReceiver 
{
	Texture _buttonTexture;
	private boolean _buttonDown;
	private boolean _buttonUp;
	
	public ButtonEntity(String backgroundImageName)
	{
		_buttonTexture = MyGdxGame.getTextureResources().getRenderInfo(backgroundImageName).getTextureToDraw();
	}
	
	/**
	 * Sets the button state to down and dispatches the event for it.
	 */
	public void onButtonDown()
	{
		this.dispatchEvent(new ButtonEvent(ButtonEvent.ButtonState.Down, ButtonGlobals.BUTTON_DOWN_EVENT));
		this.dispatchEvent(new ButtonEvent(ButtonEvent.ButtonState.Down, ButtonGlobals.BUTTON_EVENT));
		_buttonDown = true;
		_buttonUp = false;
	}
	
	/**
	 * Sets the button state to up and dispatches the event for it.
	 */
	public void onButtonUp()
	{
		this.dispatchEvent(new ButtonEvent(ButtonEvent.ButtonState.Up, ButtonGlobals.BUTTON_UP_EVENT));
		this.dispatchEvent(new ButtonEvent(ButtonEvent.ButtonState.Up, ButtonGlobals.BUTTON_EVENT));
		_buttonUp = true;
		_buttonDown = false;
	}

	/**
	 * 
	 * @return _buttonDown
	 */
	public boolean getButtonDown()
	{
		return _buttonDown;
	}
	
	/**
	 * 
	 * @return _buttonUp
	 */
	public boolean getButtonUp()
	{
		return _buttonUp;
	}
	
	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == InputGlobals.TOUCH_EVENT)
		{
			onTouchEvent((TouchEvent)event);
		}
	}

	@Override
	protected void awake() {
		MyGdxGame.getInputHandler().addEventListener(InputGlobals.TOUCH_EVENT, this);
	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void rendered(RenderComponents renderComponents) {	
		//
	}

	@Override
	protected void destroyed() {
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
		_buttonTexture = null;
	}
	
	private void onTouchEvent(TouchEvent event)
	{
		Vector2 touchPos = new Vector2();
		
		if(event.getTouchType() == TouchEvent.TouchType.Started)
		{
			touchPos.x = event.getTouchX();
			touchPos.y = event.getTouchY();
			
			Vector2 position = new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY());
			float pivotX = this.getComponent(RenderComponent.class).getPivotX();
			float pivotY = this.getComponent(RenderComponent.class).getPivotY();
			float buttonTextureWidth = this.getComponent(RenderComponent.class).getCurrentTexture().getWidth();
			float buttonTextureHeight = this.getComponent(RenderComponent.class).getCurrentTexture().getHeight();
			
			if(touchPos.x > (position.x - buttonTextureWidth * pivotX) && touchPos.x < position.x + buttonTextureWidth)
			{
				if(touchPos.y > (position.y - buttonTextureHeight * pivotY) && touchPos.y < position.y + buttonTextureHeight)
				{
					System.out.println("hiyah");
					onButtonDown();
				}
			}
		}
		else if(event.getTouchType() == TouchEvent.TouchType.Ended)
		{
			onButtonUp();
		}
	}
	
}
