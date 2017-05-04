package com.mygdx.game.scenes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.entities.ButtonEntity;
import com.mygdx.game.globals.ButtonGlobals;
import com.mygdx.game.globals.InputGlobals;
import com.mygdx.game.ui.ButtonEvent;

/**
 * This scene is the main menu scene. 
 * This will spawn and set the world for the menu related classes.
 * @author Ramses Di Perna
 */
public class MenuScene extends BaseScene implements IEventReceiver
{
	private RenderInfo _menuBG = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_MENU_BACKGROUND);
	private RenderInfo _menuLogo = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_MENU_LOGO);
	private ButtonEntity _playBtn = new ButtonEntity(GameTextureResources.SPRITE_GAME_MENU_PLAY_BTN);
	
	@Override
	public void destroyed() {
		MyGdxGame.getAudioResources().stopAllMusic();
		MyGdxGame.getAudioResources().stopAllSounds();
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
		_playBtn.removeEventListener(ButtonGlobals.BUTTON_EVENT, this);
		_menuBG = null;
		_menuLogo = null;
		_playBtn = null;
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
		
		_playBtn.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT / 2));
		_playBtn.addEventListener(ButtonGlobals.BUTTON_EVENT, this);
	}
	
	//This function will run when a Button Event is triggered. Namely the button down event. This function will start the game.
	private void onButtonEvent(ButtonEvent event)
	{
		if(event.getButtonState() == ButtonEvent.ButtonState.Down)
		{
			startGame();
		}
	}
	
	/**
	 * This function starts the game.
	 */
	private void startGame()
	{
		this.getScenesManager().setScene(1);
	}
	
	@Override
	public void onReceiveEvent(Event event)
	{
		if(event.getType() == ButtonGlobals.BUTTON_EVENT)
		{
			onButtonEvent((ButtonEvent)event);			
		}
	}

}
