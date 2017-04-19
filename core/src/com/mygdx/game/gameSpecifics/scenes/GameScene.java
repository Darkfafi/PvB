package com.mygdx.game.gameSpecifics.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.gameSpecifics.entities.BowWeapon;
import com.mygdx.game.gameSpecifics.level.Playfield;
import com.mygdx.game.gameSpecifics.level.WaveSystem;
import com.mygdx.game.resources.PhysicsWorld;
import com.mygdx.game.scenes.BaseScene;

/**
 * This scene is the main game scene. 
 * This will spawn and set the world for the game related classes.
 * @author Ramses Di Perna
 */
public class GameScene extends BaseScene 
{
	private PhysicsWorld _physicsWorld;
	private Playfield _playfield = new Playfield();
	private WaveSystem _waveSystem;

	@Override
	public void update(float dt) 
	{
		_waveSystem.updateWaveSystem(dt);
		_physicsWorld.update(dt);
	}

	@Override
	public void render() 
	{
		_playfield.debugRender(getRenderComponents());
		getRenderComponents().getSpriteBatch().begin();
		Texture t = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_BACKGROUND_01).getTextureToDraw();
		getRenderComponents().getSpriteBatch().draw(t, 0, 0, t.getWidth(), t.getHeight());
		getRenderComponents().getSpriteBatch().end();
		
		//_physicsWorld.render(getRenderComponents()); // Debug rendering of colliders
		
	}

	@Override
	protected void created() 
	{
		Gdx.gl.glClearColor(0, 0.1f, 0, 1);
		_physicsWorld = new PhysicsWorld();
		MyGdxGame.getAudioResources().getMusic(GameAudioResources.MUSIC_WAVE_SOUNDTRACK).play();
		
		_playfield.createLevel();
		_waveSystem = new WaveSystem(_playfield);
		
		BowWeapon bow = new BowWeapon();
		bow.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, bow.getComponent(AnimationComponent.class).getRealHeight() / 2 + 20));
	}
	
	@Override
	public void destroyed() 
	{
		_playfield.destroyLevel();
		_playfield = null;
		
		_physicsWorld.clean();
		_physicsWorld = null;
		
		_waveSystem = null;
	}
}
