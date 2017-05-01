package com.mygdx.game.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.globals.InputGlobals;
import com.mygdx.game.touchinput.TouchEvent;

/**
 * This scene is the main menu scene. 
 * This will spawn and set the world for the menu related classes.
 * @author Ramses Di Perna
 */
public class MenuScene extends BaseScene implements IEventReceiver
{
	private BitmapFont _font = new BitmapFont();
	
	@Override
	public void onReceiveEvent(Event event)
	{
		if(event.getType() == InputGlobals.TOUCH_EVENT)
		{
			onTouchEvent((TouchEvent)event);
		}
	}
	
	@Override
	public void destroyed() {
		MyGdxGame.getAudioResources().stopAllMusic();
		MyGdxGame.getAudioResources().stopAllSounds();
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
	}

	@Override
	public void update(float dt) {
		//
	}

	@Override
	public void render() 
	{
		this.getRenderComponents().getSpriteBatch().setProjectionMatrix(this.getRenderComponents().getMainCamera().combined);
		getRenderComponents().getSpriteBatch().begin();
		
		//Draw the Background on the menu.
		Texture menuBG = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_MENU_BACKGROUND).getTextureToDraw();
		getRenderComponents().getSpriteBatch().draw(menuBG, 0, 0, menuBG.getWidth(), menuBG.getHeight());
		
		//Draw the Game Logo on the menu.
		Texture menuLogo = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_MENU_LOGO).getTextureToDraw();
		getRenderComponents().getSpriteBatch().draw(menuLogo, 10, MyGdxGame.HEIGHT - (menuLogo.getHeight() + 80), menuLogo.getWidth(), menuLogo.getHeight());
		
		String text = "Tap the Screen to Start.";
		_font.setScale(1.1f);
		_font.draw(this.getRenderComponents().getSpriteBatch(), text, ((MyGdxGame.WIDTH/2) - (_font.getBounds(text).width/2)), MyGdxGame.HEIGHT/2);
		
		getRenderComponents().getSpriteBatch().end();
	}

	@Override
	protected void created() 
	{	
		MyGdxGame.getInputHandler().addEventListener(InputGlobals.TOUCH_EVENT, this);
	}
	
	private void onTouchEvent(TouchEvent event)
	{
		if(event.getTouchType() == TouchEvent.TouchType.Started)
		{
			startGame();
		}
	}
	
	private void startGame()
	{
		this.getScenesManager().setScene(1);
	}

}
