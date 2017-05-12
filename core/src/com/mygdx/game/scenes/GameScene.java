package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.components.PlayerWeaponControlComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.resources.PhysicsWorld;
import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.entities.ButtonEntity;
import com.mygdx.game.entities.weapons.BowWeapon;
import com.mygdx.game.globals.ButtonGlobals;
import com.mygdx.game.globals.PreferencesGlobals;
import com.mygdx.game.level.DesertLevel;
import com.mygdx.game.level.Playfield;
import com.mygdx.game.popUps.EndScreenPopUp;
import com.mygdx.game.popUps.PausePopUp;
import com.mygdx.game.score.GameScoreSystem;
import com.mygdx.game.tutorial.BowDemonstrationTutorial;
import com.mygdx.game.ui.HealthUI;
import com.mygdx.game.ui.ScoreUI;
import com.mygdx.game.ui.WaveUI;
import com.mygdx.game.waves.WaveSystem;

/**
 * This scene is the main game scene. 
 * This will spawn and set the world for the game related classes.
 * @author Ramses Di Perna
 */
public class GameScene extends BaseScene implements IEventReceiver
{
	public static final boolean DEVELOPMENT_SKIP_TUTORIAL = false;
	public static final boolean DEVELOPMENT_RESET_GAME_PREFS_EVERY_PLAY = true;
	public static float TUTORIAL_DURATION = 8f;
	
	private PhysicsWorld _physicsWorld;
	private Playfield _playfield;
	private WaveSystem _waveSystem;
	private ButtonEntity _pauseBtn;
	
	private BowWeapon _playerBow;
	
	private float _waitForTutorial = -1;
	
	private Preferences _preferences;
	
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
			
			if(_waitForTutorial >= TUTORIAL_DURATION + 1)
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
		_preferences = Gdx.app.getPreferences(PreferencesGlobals.PREFERENCES_GAME);
		
		Gdx.gl.glClearColor(0, 0.1f, 0, 1);
		_physicsWorld = new PhysicsWorld();
		
		// playfield spawn
		_playfield = new Playfield(new DesertLevel());
		_playfield.addEventListener(Playfield.EVENT_BASE_DESTROYED, this);
		
		// player spawn
		_playerBow = new BowWeapon(_physicsWorld.getWorld());
		_playerBow.getTransformComponent().setPosition(new Vector2(Engine.getWidth() / 2, _playerBow.getComponent(RenderComponent.class).getRealHeight() / 2 + 20));
		
		// pause button
		_pauseBtn = new ButtonEntity(GameTextureResources.UI_INGAME_PAUSE_BTN);
		_pauseBtn.setButtonTouchLayer(0);
		_pauseBtn.getTransformComponent().setPosition(
				(Engine.getWidth() - (_pauseBtn.getRenderComponent().getCurrentTexture().getWidth() / 2)) - 10, 
				(Engine.getHeight() - (_pauseBtn.getRenderComponent().getCurrentTexture().getHeight() / 2)) - 10
		);
		_pauseBtn.addEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		
		if(DEVELOPMENT_SKIP_TUTORIAL)
		{
			startGame();
			return;
		}
		// Tutorial or direct play?
		if(!_preferences.getBoolean(PreferencesGlobals.PREF_KEY_BOOLEAN_TUTORIAL_DONE, false))
		{
			BowDemonstrationTutorial bdt = new BowDemonstrationTutorial(_playerBow, _playfield);
			bdt.startTutorial(TUTORIAL_DURATION);
			_waitForTutorial = 0;
			_preferences.putBoolean(PreferencesGlobals.PREF_KEY_BOOLEAN_TUTORIAL_DONE, true);
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
		
		if(_waveSystem != null)
		{
			_waveSystem.removeEventListener(WaveSystem.EVENT_WAVE_STARTED, this);
			_waveSystem.clean();
		}
		_waveSystem = null;
		
		_preferences.flush(); // saves the prefs
		_preferences = null;
		
		_playerBow = null;
		
		_pauseBtn.removeEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		_pauseBtn = null;
	}

	@Override
	public void onReceiveEvent(Event event)
	{
		if(event.getType() == Playfield.EVENT_BASE_DESTROYED)
		{
			onBaseDestroyedEvent(event);
		}
		
		if(event.getType() == ButtonGlobals.BUTTON_DOWN_EVENT)
		{
			PausePopUp ppu = new PausePopUp(true);
			ppu.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() / 2);
		}
		
		if(event.getType() == WaveSystem.EVENT_WAVE_STARTED)
		{
			if(_playfield != null)
				_playfield.countForResetTraps();
		}
	}
	
	private void startGame()
	{
		_playfield.forceResetTraps();
		
		_playerBow.addComponent(new PlayerWeaponControlComponent(_playerBow));
		_waveSystem = new WaveSystem(_playfield, _playfield.getLevelBlueprint());

		_waveSystem.removeEventListener(WaveSystem.EVENT_WAVE_STARTED, this);
		_waveSystem.addEventListener(WaveSystem.EVENT_WAVE_STARTED, this);
		
		GameScoreSystem.getInstance().endScoreSession(false);
		
		spawnGameUI();
	}
	
	private void spawnGameUI() 
	{
		WaveUI waveUI = new WaveUI(_waveSystem);
		ScoreUI scoreUI = new ScoreUI();
		HealthUI healthUI = new HealthUI(_playfield.getPlayerBase().getComponent(HealthComponent.class));
		
		waveUI.getTransformComponent().translatePosition(new Vector2(waveUI.getBackgroundSize().x / 2 + 10, Engine.getHeight() - (waveUI.getBackgroundSize().y / 2) - 10));
		scoreUI.getTransformComponent().translatePosition(new Vector2(Engine.getWidth() / 2, Engine.getHeight() - scoreUI.getBackgroundSize().y / 2 - 10));
		
		
		waveUI.getTransformComponent().setScale(new Vector2(0, 0));
		scoreUI.getTransformComponent().setScale(new Vector2(0, 0));
		
		waveUI.getTransformComponent().doScale(1, 1, 0.8f, true).ease(EaseType.BackOut);
		scoreUI.getTransformComponent().doScale(1, 1, 0.8f, true).ease(EaseType.BackOut);
	}

	private void onBaseDestroyedEvent(Event event) 
	{
		_playerBow.removeComponent(PlayerWeaponControlComponent.class);

		EndScreenPopUp espp = new EndScreenPopUp(true);
		espp.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() / 2);
	}
	
	private void resetGamePrefs()
	{
		_preferences.clear();
		_preferences.flush();
	}
}
