package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.components.PlayerWeaponControlComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.resources.PhysicsWorld;
import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.entities.weapons.BowWeapon;
import com.mygdx.game.level.DesertLevel;
import com.mygdx.game.level.Playfield;
import com.mygdx.game.ui.WaveUI;
import com.mygdx.game.waves.WaveSystem;

import tutorial.BowDemonstrationTutorial;

/**
 * This scene is the main game scene. 
 * This will spawn and set the world for the game related classes.
 * @author Ramses Di Perna
 */
public class GameScene extends BaseScene implements IEventReceiver
{
	public static final boolean DEVELOPMENT_RESET_GAME_PREFS_EVERY_PLAY = true;
	
	public static final String PREFERENCES_GAME = "GamePreferences";
	public static final String PREF_KEY_BOOLEAN_TUTORIAL_DONE = "TutorialDonePrefBooleanKey";
	
	public static float TUTORIAL_DURATION = 8f;
	
	private PhysicsWorld _physicsWorld;
	private Playfield _playfield;
	private WaveSystem _waveSystem;
	
	private BowWeapon _playerBow;
	
	private float _waitForTutorial = -1;
	
	Preferences _preferences;
	
	@Override
	public void update(float dt) 
	{
		if(_waveSystem != null)
			_waveSystem.updateWaveSystem(dt);
		
		_physicsWorld.update(dt);
		_playfield.update(dt);
		
		if(_waitForTutorial != -1)
		{
			_waitForTutorial += dt;
			
			if(_waitForTutorial >= TUTORIAL_DURATION)
			{
				_waitForTutorial = -1;
				startGame();
			}
		}
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
		_preferences = Gdx.app.getPreferences(PREFERENCES_GAME);
		
		Gdx.gl.glClearColor(0, 0.1f, 0, 1);
		_physicsWorld = new PhysicsWorld();
		
		// playfield spawn
		_playfield = new Playfield(new DesertLevel());
		_playfield.addEventListener(Playfield.EVENT_BASE_DESTROYED, this);
		
		// player spawn
		_playerBow = new BowWeapon();
		_playerBow.getTransformComponent().setPosition(new Vector2(Engine.getWidth() / 2, _playerBow.getComponent(AnimationComponent.class).getRealHeight() / 2 + 20));
		
		// Tutorial or direct play?
		if(!_preferences.getBoolean(PREF_KEY_BOOLEAN_TUTORIAL_DONE, false))
		{
			BowDemonstrationTutorial bdt = new BowDemonstrationTutorial(_playerBow);
			bdt.startTutorial(TUTORIAL_DURATION);
			_waitForTutorial = 0;
			_preferences.putBoolean(PREF_KEY_BOOLEAN_TUTORIAL_DONE, true);
		}
		else
		{
			startGame();
		}
		
		if(DEVELOPMENT_RESET_GAME_PREFS_EVERY_PLAY)
			this.resetGamePrefs();
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
		
		_preferences.flush(); // saves the prefs
		_preferences = null;
		
		_playerBow = null;
		
	}

	@Override
	public void onReceiveEvent(Event event)
	{
		if(event.getType() == Playfield.EVENT_BASE_DESTROYED)
		{
			onBaseDestroyedEvent(event);
		}
	}
	
	private void startGame()
	{
		_playerBow.addComponent(new PlayerWeaponControlComponent(_playerBow));
		_waveSystem = new WaveSystem(_playfield, _playfield.getLevelBlueprint());
		
		// UI
		WaveUI waveUI = new WaveUI(_waveSystem);
	}
	
	private void onBaseDestroyedEvent(Event event) 
	{
		this.getScenesManager().setScene(GameScenesManager.MENU_SCENE);
	}
	
	private void resetGamePrefs()
	{
		_preferences.clear();
		_preferences.flush();
	}
}
