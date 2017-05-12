package com.mygdx.game.popUps;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.entities.ButtonEntity;
import com.mygdx.game.globals.ButtonGlobals;
import com.mygdx.game.scenes.GameScenesManager;

public class EndScreenPopUp extends BaseGamePopUp implements IEventReceiver
{
	private ButtonEntity _continueButton;
	
	public EndScreenPopUp(boolean isCoverPopUp) 
	{
		super(isCoverPopUp);
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == ButtonGlobals.BUTTON_DOWN_EVENT)
		{
			this.closePopUp();
		}
		
	}
	
	@Override
	protected void onPopUpAwake()
	{
		this.getRenderComponent().setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_POP_UP_GAME_OVER));
		
		// Button
		_continueButton = new ButtonEntity(GameTextureResources.UI_BUTTON_CONTINUE);
		_continueButton.getTransformComponent().setParent(getTransformComponent());
		_continueButton.getTransformComponent().translatePosition(new Vector2(0, -this.getRenderComponent().getRealHeight() * 0.34f));
		super.onPopUpAwake();
		
		_continueButton.addEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
	}
	
	@Override
	protected void onPopUpDestroyed() 
	{
		_continueButton.removeEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		_continueButton = null;
		Engine.getSceneManager().setScene(GameScenesManager.MENU_SCENE);
	}
}
