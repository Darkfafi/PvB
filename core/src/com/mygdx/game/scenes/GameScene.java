package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.resources.PhysicsWorld;
import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.entities.BowWeapon;
import com.mygdx.game.level.Playfield;
import com.mygdx.game.ui.WaveUI;
import com.mygdx.game.waves.GameWaveDesigns;
import com.mygdx.game.waves.WaveSystem;

/**
 * This scene is the main game scene. 
 * This will spawn and set the world for the game related classes.
 * @author Ramses Di Perna
 */
public class GameScene extends BaseScene implements IEventReceiver
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
		this.getRenderComponents().getSpriteBatch().setProjectionMatrix(this.getRenderComponents().getMainCamera().combined);
		//_playfield.debugRender(getRenderComponents());
		getRenderComponents().getSpriteBatch().begin();
		Texture t = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_BACKGROUND_01).getTextureToDraw();
		float offsetX = (t.getWidth() - MyGdxGame.WIDTH) / 2;
		float offsetY = (t.getHeight() - MyGdxGame.HEIGHT) / 2;
		if(offsetX <= 0) { offsetX = 0;}
		if(offsetY <= 0) { offsetY = 0;}
		getRenderComponents().getSpriteBatch().draw(t, -offsetX, -offsetY, t.getWidth(), t.getHeight());
		getRenderComponents().getSpriteBatch().end();
		
	}

	@Override
	protected void created() 
	{
		Gdx.gl.glClearColor(0, 0.1f, 0, 1);
		_physicsWorld = new PhysicsWorld();
		MyGdxGame.getAudioResources().getMusic(GameAudioResources.MUSIC_WAVE_SOUNDTRACK).play();
		
		_playfield.createLevel();
		_playfield.addEventListener(Playfield.EVENT_BASE_DESTROYED, this);
		
		_waveSystem = new WaveSystem(_playfield, new GameWaveDesigns());
		
		BowWeapon bow = new BowWeapon();
		bow.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, bow.getComponent(AnimationComponent.class).getRealHeight() / 2 + 20));
		
		// UI
		WaveUI waveUI = new WaveUI(_waveSystem);
	}
	
	@Override
	public void destroyed() 
	{
		MyGdxGame.getAudioResources().stopAllMusic();
		MyGdxGame.getAudioResources().stopAllSounds();
		
		_playfield.destroyLevel();
		_playfield.removeEventListener(Playfield.EVENT_BASE_DESTROYED, this);
		_playfield = null;
		
		_physicsWorld.clean();
		_physicsWorld = null;
		
		_waveSystem.clean();
		_waveSystem = null;
	}

	@Override
	public void onReceiveEvent(Event event)
	{
		if(event.getType() == Playfield.EVENT_BASE_DESTROYED)
		{
			onBaseDestroyedEvent(event);
		}
	}

	private void onBaseDestroyedEvent(Event event) 
	{
		this.getScenesManager().setScene(GameScenesManager.MENU_SCENE);
	}
}
