package com.mygdx.game.gameSpecifics.scenes;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.scenes.BaseScene;

/**
 * This scene is the main game scene. 
 * This will spawn and set the world for the game related classes.
 * @author Ramses Di Perna
 */
public class GameScene extends BaseScene {

	@Override
	public void destroyed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void created() {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0.1f, 0, 1);

	}

}
