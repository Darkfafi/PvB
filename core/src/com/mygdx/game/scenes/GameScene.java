package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.engine.resources.PhysicsWorld;
import com.mygdx.engine.scenes.BaseScene;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BowWeapon;
import com.mygdx.game.level.Playfield;
import com.mygdx.game.waves.GameWaveDesigns;
import com.mygdx.game.waves.WaveSystem;

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
	
	private BitmapFont _font = new BitmapFont();
			
	@Override
	public void update(float dt) 
	{
		_waveSystem.updateWaveSystem(dt);
		_physicsWorld.update(dt);
		_font.getData().scaleX = _font.getData().scaleY = (1.2f);
	}

	@Override
	public void render() 
	{
		this.getRenderComponents().getSpriteBatch().setProjectionMatrix(this.getRenderComponents().getMainCamera().combined);
		//_playfield.debugRender(getRenderComponents());
		getRenderComponents().getSpriteBatch().begin();
		Texture t = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_BACKGROUND_01).getTextureToDraw();
		getRenderComponents().getSpriteBatch().draw(t, 0, 0, t.getWidth(), t.getHeight());
		getRenderComponents().getSpriteBatch().end();
		
		//_physicsWorld.render(getRenderComponents()); // Debug rendering of colliders
		this.getRenderComponents().getSpriteBatch().setProjectionMatrix(this.getRenderComponents().getHudCamera().combined);
		this.getRenderComponents().getSpriteBatch().begin();
		String text = "Wave: " + _waveSystem.getCurrentWave();
		_font.draw(this.getRenderComponents().getSpriteBatch(), text, (MyGdxGame.WIDTH / 2) - _font.getBounds(text).width / 2, MyGdxGame.HEIGHT - 10);
		this.getRenderComponents().getSpriteBatch().end();
	}

	@Override
	protected void created() 
	{
		Gdx.gl.glClearColor(0, 0.1f, 0, 1);
		_physicsWorld = new PhysicsWorld();
		MyGdxGame.getAudioResources().getMusic(GameAudioResources.MUSIC_WAVE_SOUNDTRACK).setVolume(0.3f);
		MyGdxGame.getAudioResources().getMusic(GameAudioResources.MUSIC_WAVE_SOUNDTRACK).play();
		
		_playfield.createLevel();
		_waveSystem = new WaveSystem(_playfield, new GameWaveDesigns());
		
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
		
		_waveSystem.clean();
		_waveSystem = null;
	}
}
