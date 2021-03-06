package com.mygdx.game.scenes;

import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.engine.scenes.BaseScenesManager;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This class is the definer for the game specific scenes.
 * It contains the scenes which are able to be switched to and will set the first scene.
 * @author Ramses Di Perna
 */
public class GameScenesManager extends BaseScenesManager 
{
	public static final int MENU_SCENE = 1;
	public static final int GAME_SCENE = 2;
	private static final int LOAD_SCENE = 0;
	
	public GameScenesManager(RenderComponents renderComponents) {
		super(renderComponents);
		setScene(LOAD_SCENE);
	}

	@Override
	protected BaseScene getScene(int sceneId) {

		if(sceneId == LOAD_SCENE){return new LoadScene();}
		if(sceneId == MENU_SCENE){return new MenuScene();}
		if(sceneId == GAME_SCENE){return new GameScene();}
		
		return null;
	}
}
