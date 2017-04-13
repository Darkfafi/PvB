package com.mygdx.game.touchinput;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.events.EventDispatcher;
import com.mygdx.game.globals.InputGlobals;

public class TouchInputHandler extends EventDispatcher implements ApplicationListener, InputProcessor 
{
	private int _screenHeight = 0;
	private int _screenWidth = 0;
	
	public TouchInputHandler(int screenWidth, int screenHeight)
	{
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		if(!inScreenBounds(screenX, screenY)){return false;}
		Vector2 pos = new Vector2(screenX,_screenHeight - screenY);
		this.dispatchEvent(new TouchEvent(pos, pointer, TouchEvent.TouchType.Started, InputGlobals.TOUCH_STARTED_EVENT));
		this.dispatchEvent(new TouchEvent(pos, pointer, TouchEvent.TouchType.Started, InputGlobals.TOUCH_EVENT));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{
		if(!inScreenBounds(screenX, screenY)){return false;}
		Vector2 pos = new Vector2(screenX,_screenHeight - screenY);
		this.dispatchEvent(new TouchEvent(pos, pointer, TouchEvent.TouchType.Ended, InputGlobals.TOUCH_ENDED_EVENT));
		this.dispatchEvent(new TouchEvent(pos, pointer, TouchEvent.TouchType.Ended, InputGlobals.TOUCH_EVENT));
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) 
	{
		if(!inScreenBounds(screenX, screenY)){return false;}
		Vector2 pos = new Vector2(screenX,_screenHeight - screenY);
		this.dispatchEvent(new TouchEvent(pos, pointer, TouchEvent.TouchType.Dragged, InputGlobals.TOUCH_DRAGGED_EVENT));
		this.dispatchEvent(new TouchEvent(pos, pointer, TouchEvent.TouchType.Dragged, InputGlobals.TOUCH_EVENT));
		return true;
	}
	
	public boolean isTouched()
	{
		return Gdx.input.isTouched();
	}
	
	public boolean isTouched(int pointer)
	{
		return Gdx.input.isTouched(pointer);
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		System.out.println("SSOFHJAFSOISAOHF");
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	private boolean inScreenBounds(int x, int y)
	{
		return (x >= 0 && x <= _screenWidth) && (y >= 0 && y <= _screenHeight);
	}
}
