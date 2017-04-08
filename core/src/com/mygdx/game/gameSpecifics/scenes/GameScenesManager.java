package com.mygdx.game.gameSpecifics.scenes;

import com.mygdx.game.entities.EntitySystem;
import com.mygdx.game.scenes.BaseScene;
import com.mygdx.game.scenes.BaseScenesManager;
import com.mygdx.game.scenes.RenderComponents;

/**
 * This class is the definer for the game specific scenes.
 * It contains the scenes which are able to be switched to and will set the first scene.
 * @author Ramses Di Perna
 */
public class GameScenesManager extends BaseScenesManager {

	public static final int MENU_SCENE = 0;
	public static final int GAME_SCENE = 1;
	
	public GameScenesManager(RenderComponents renderComponents) {
		super(renderComponents);
		setScene(MENU_SCENE);
	}

	@Override
	protected BaseScene getScene(int sceneId) {
		if(sceneId == MENU_SCENE){return new MenuScene();}
		if(sceneId == GAME_SCENE){return new GameScene();}
		
		return null;
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		EntitySystem.getInstance().updateEntities(dt);
	}
	
	@Override
	public void render()
	{
		EntitySystem.getInstance().renderEntities(this.getRenderComponents());
	}

}
