package com.mygdx.game.gameSpecifics.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.gameSpecifics.entities.BowWeapon;
import com.mygdx.game.scenes.BaseScene;

/**
 * This scene is the main game scene. 
 * This will spawn and set the world for the game related classes.
 * @author Ramses Di Perna
 */
public class GameScene extends BaseScene 
{
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
		BowWeapon bow = new BowWeapon();
		bow.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, bow.getComponent(AnimationComponent.class).getRealHeight() / 2 + 20));
	}

}
