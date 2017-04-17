package com.mygdx.game.gameSpecifics.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.gameSpecifics.entities.BowWeapon;
import com.mygdx.game.gameSpecifics.level.Playfield;
import com.mygdx.game.scenes.BaseScene;

/**
 * This scene is the main game scene. 
 * This will spawn and set the world for the game related classes.
 * @author Ramses Di Perna
 */
public class GameScene extends BaseScene 
{
	private Playfield _playfield = new Playfield();
	private float t = 0;
	@Override
	public void destroyed() {
		// TODO Auto-generated method stub
		_playfield.destroyLevel();
		_playfield = null;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		t += dt;
		if(t > 2f)
		{
			//this.getScenesManager().setScene(0);
		}
	}

	@Override
	public void render() 
	{
		_playfield.debugRender(getRenderComponents());
	}

	@Override
	protected void created() {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0.1f, 0, 1);
		_playfield.createLevel();
		BowWeapon bow = new BowWeapon();
		bow.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, bow.getComponent(AnimationComponent.class).getRealHeight() / 2 + 20));
	}

}
