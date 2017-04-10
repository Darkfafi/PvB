package com.mygdx.game.gameSpecifics.scenes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.gameSpecifics.entities.Enemy;
import com.mygdx.game.gameSpecifics.entities.Enemy2;
import com.mygdx.game.scenes.BaseScene;

/**
 * This scene is the main menu scene. 
 * This will spawn and set the world for the menu related classes.
 * @author Ramses Di Perna
 */
public class MenuScene extends BaseScene {

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
		Enemy enemy = new Enemy();
		Enemy2 enemy2 = new Enemy2();
		enemy.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2 ,0));
		
	}

}
