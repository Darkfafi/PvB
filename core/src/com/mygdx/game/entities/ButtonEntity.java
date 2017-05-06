package com.mygdx.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
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
	private RenderComponent _renderComponent;
	private boolean _isButtonPressed;
	
	public ButtonEntity(String buttonAtlas)
	{
		_renderComponent = this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(buttonAtlas), true));
	}
	
	/**
	 * Sets the button state to down and dispatches the event for it.
	 */
	public void onButtonDown()
	{
		this.dispatchEvent(new ButtonEvent(ButtonEvent.ButtonState.Down, ButtonGlobals.BUTTON_DOWN_EVENT));
		this.dispatchEvent(new ButtonEvent(ButtonEvent.ButtonState.Down, ButtonGlobals.BUTTON_EVENT));
		_isButtonPressed = true;
	}
	
	/**
	 * Sets the button state to up and dispatches the event for it.
	 */
	public void onButtonUp()
	{
		this.dispatchEvent(new ButtonEvent(ButtonEvent.ButtonState.Up, ButtonGlobals.BUTTON_UP_EVENT));
		this.dispatchEvent(new ButtonEvent(ButtonEvent.ButtonState.Up, ButtonGlobals.BUTTON_EVENT));
		_isButtonPressed = false;
	}
	
	/**
	 * Returns the RenderComponent for the TextEntity
	 * WARNING: Do not remove the RenderComponent
	 * @return The RenderComponent attached to the TextEntity
	 */
	public RenderComponent getRenderComponent()
	{
		return _renderComponent;
	}
	
	/**
	 * 
	 * @return _isButtonPressed
	 */
	public boolean isButtonPressed()
	{
		return _isButtonPressed;
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
		_renderComponent = null;
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
	}
	
	private void onTouchEvent(TouchEvent event)
	{
		Vector2 touchPos = new Vector2();
		
		if(event.getTouchType() == TouchEvent.TouchType.Started)
		{
			touchPos.x = event.getTouchX();
			touchPos.y = event.getTouchY();
			
			Vector2 position = new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY());
			float pivotX = getRenderComponent().getPivotX();
			float pivotY = getRenderComponent().getPivotY();
			float buttonTextureWidth = getRenderComponent().getCurrentTexture().getWidth();
			float buttonTextureHeight = getRenderComponent().getCurrentTexture().getHeight();
			
			if(touchPos.x > (position.x - buttonTextureWidth * pivotX) && touchPos.x < position.x + buttonTextureWidth)
			{
				if(touchPos.y > (position.y - buttonTextureHeight * pivotY) && touchPos.y < position.y + buttonTextureHeight)
				{
					if(getRenderComponent().getRenderInfo().getFramesLength() > 1)
					{
						getRenderComponent().setCurrentFrameInfo(1);
					}
					
					onButtonDown();
				}
			}
		}
		else if(event.getTouchType() == TouchEvent.TouchType.Ended)
		{
			onButtonUp();

			if(getRenderComponent().getRenderInfo().getFramesLength() > 1)
			{
				getRenderComponent().setCurrentFrameInfo(0);
			}
			else 
			{
				return;
			}
		}
	}
	
}
