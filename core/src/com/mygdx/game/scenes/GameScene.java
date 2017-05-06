package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.resources.PhysicsWorld;
import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.entities.BowWeapon;
import com.mygdx.game.level.DesertLevel;
import com.mygdx.game.level.Playfield;
import com.mygdx.game.ui.WaveUI;
import com.mygdx.game.waves.WaveSystem;

/**
 * This scene is the main game scene. 
 * This will spawn and set the world for the game related classes.
 * @author Ramses Di Perna
 */
public class GameScene extends BaseScene implements IEventReceiver
{
	private PhysicsWorld _physicsWorld;
	private Playfield _playfield;
	private WaveSystem _waveSystem;
			
	@Override
	public void update(float dt) 
	{
		_waveSystem.updateWaveSystem(dt);
		_physicsWorld.update(dt);
		_playfield.update(dt);
	}

	@Override
	public void render() 
	{
		_playfield.render(this.getRenderComponents(), false);
		//_physicsWorld.render(getRenderComponents());
	}

	@Override
	protected void created() 
	{
		Gdx.gl.glClearColor(0, 0.1f, 0, 1);
		_physicsWorld = new PhysicsWorld();
		
		_playfield = new Playfield(new DesertLevel());
		_playfield.addEventListener(Playfield.EVENT_BASE_DESTROYED, this);
		
		_waveSystem = new WaveSystem(_playfield, _playfield.getLevelBlueprint());
		
		BowWeapon bow = new BowWeapon();
		bow.getTransformComponent().setPosition(new Vector2(Engine.getWidth() / 2, bow.getComponent(AnimationComponent.class).getRealHeight() / 2 + 20));
		
		// UI
		WaveUI waveUI = new WaveUI(_waveSystem);
	}
	
	@Override
	public void destroyed() 
	{
		Engine.getAudioResources().stopAllMusic();
		Engine.getAudioResources().stopAllSounds();
		
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
