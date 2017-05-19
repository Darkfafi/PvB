package com.mygdx.game.popUps;

import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.entities.ButtonEntity;
import com.mygdx.game.globals.ButtonGlobals;
import com.mygdx.game.scenes.GameScenesManager;

/**
 * This is a pause pop-up. It will cause the Time Scale to hit 0 when opened and puts it back to its original value when closed.
 * @author Ramses Di Perna
 *
 */
public class PausePopUp extends BaseGamePopUp implements IEventReceiver 
{
	private float _timeScaleOnOpen;
	private ButtonEntity _continueButton;
	private ButtonEntity _quitButton;
	
	public PausePopUp(boolean isCoverPopUp) 
	{
		super(isCoverPopUp);
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == ButtonGlobals.BUTTON_DOWN_EVENT)
		{
			this.closePopUp();

			if( ((ButtonEntity)event.getDispatcher()).hasTag("quitButton") )
			{
				Engine.getSceneManager().setScene(GameScenesManager.MENU_SCENE);
			}
		}
	}

	@Override
	protected void onPopUpAwake() 
	{
		_timeScaleOnOpen = Engine.TimeScale;
		Engine.TimeScale = 0;
		this.getRenderComponent().setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_POP_UP_PAUSE));
		
		_continueButton = new ButtonEntity(GameTextureResources.UI_BUTTON_CONTINUE);
		_continueButton.getTransformComponent().setParent(getTransformComponent());
		_continueButton.getTransformComponent().translatePosition(-(this.getRenderComponent().getCurrentTexture().getWidth() / 8), -this.getRenderComponent().getCurrentTexture().getHeight() * 0.175f);
		
		_quitButton = new ButtonEntity(GameTextureResources.UI_INGAME_QUIT_BTN);
		_quitButton.getTransformComponent().setParent(getTransformComponent());
		_quitButton.getTransformComponent().translatePosition((this.getRenderComponent().getCurrentTexture().getWidth() / 5.5f), -this.getRenderComponent().getCurrentTexture().getHeight() * 0.19f);
		_quitButton.addTag("quitButton");
		
		_continueButton.addEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		_quitButton.addEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		super.onPopUpAwake();
	}
	
	
	
	@Override
	protected void onPopUpDestroyed() 
	{
		_continueButton.removeEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		_continueButton = null;
		
		_quitButton.removeEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		_quitButton = null;
		
		Engine.TimeScale = _timeScaleOnOpen;
	}

}
