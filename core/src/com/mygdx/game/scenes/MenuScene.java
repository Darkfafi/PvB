package com.mygdx.game.scenes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
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
	private RenderInfo _menuBG = MyGdxGame.getTextureResources().createRenderInfoCopy(GameTextureResources.SPRITE_GAME_MENU_BACKGROUND);
	private RenderInfo _menuLogo = MyGdxGame.getTextureResources().createRenderInfoCopy(GameTextureResources.SPRITE_GAME_MENU_LOGO);
	@Override
	public void destroyed() {
		MyGdxGame.getAudioResources().stopAllMusic();
		MyGdxGame.getAudioResources().stopAllSounds();
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
		_menuBG.clean();
		_menuLogo.clean();
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
		getRenderComponents().getSpriteBatch().draw(_menuBG.getTextureToDraw(), 0, 0, _menuBG.getTextureToDraw().getWidth(), _menuBG.getTextureToDraw().getHeight());
		
		//Draw the Game Logo on the menu.
		getRenderComponents().getSpriteBatch().draw(_menuLogo.getTextureToDraw(), 10, MyGdxGame.HEIGHT - (_menuLogo.getTextureToDraw().getHeight() + 80), _menuLogo.getTextureToDraw().getWidth(), _menuLogo.getTextureToDraw().getHeight());
		getRenderComponents().getSpriteBatch().end();
	}

	@Override
	protected void created() 
	{	
		MyGdxGame.getInputHandler().addEventListener(InputGlobals.TOUCH_EVENT, this);
		TextEntity startText = new TextEntity("Tap the Screen to Start.", true);
		startText.getTransformComponent().setScale(new Vector2(1.1f, 1.1f));
		startText.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT / 2));
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
	
	@Override
	public void onReceiveEvent(Event event)
	{
		if(event.getType() == InputGlobals.TOUCH_EVENT)
		{
			onTouchEvent((TouchEvent)event);
		}
	}

}
