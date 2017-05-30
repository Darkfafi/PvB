package com.mygdx.game.waves;

import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.BasicEnemyAIComponent;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.EventDispatcher;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Train;
import com.mygdx.game.factories.EnemyFactory;
import com.mygdx.game.level.GridTile;
import com.mygdx.game.level.Playfield;

/**
 * The wave system is a system which tracks the current wave and switches to the next wave. 
 * It also spawns enemies on request and gives those enemies the components to work as 'wave slaves'.
 * @author Ramses Di Perna
 *
 */
public class WaveSystem extends EventDispatcher
{
	public static final String EVENT_WAVE_STARTED = "WaveStartedEvent";
	
	private Playfield _playfield;
	private int _currentWave = 0;
	private int _currentWaveType = 0;
	private Wave _wave = null;
	private IWaveDesigns _designs = null;
	
	private float _timeWaitedForNextWave = -1;
	private float _waitTimeBetweenWaves = 2f;
	
	private Train _train;
	private BasicEntity _track;
	
	/**
	 * This gives the tools needed for the WaveSystem to work
	 * @param playfield to give to the enemies as reference and to spawn the enemies on
	 * @param waveDesigns to get waves from which are perfectly designed with the information given to them
	 */
	public WaveSystem(Playfield playfield, IWaveDesigns waveDesigns)
	{
		_track = new BasicEntity();
		_track.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TRAIN_RAIL), false));
		_track.getTransformComponent().setPosition(Engine.getWidth() / 2, Engine.getHeight() - 38);
		
		_train = new Train(4, false);
		resetTrainPosition();
		_playfield = playfield;
		_designs = waveDesigns;
		trainDriveIn().setCallbackMethod(new IEngineTweenMethod(){

			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				createNewWave(0);
				dispatchEvent(new Event(EVENT_WAVE_STARTED));
				_timeWaitedForNextWave = 0;
			}}
		);
	}
	
	/**
	 * Returns the wave number which is active
	 * @return The current wave as integer
	 */
	public int getCurrentWave()
	{
		return _currentWave;
	}
	
	/**
	 * Gets the type of wave currently active
	 * @return Wave Type as integer
	 */
	public int getCurrentWaveType()
	{
		return _currentWaveType;
	}
	
	/**
	 * Updates the system and makes it so all functionalities work perfectly together.
	 * NOTE: This method should be called every frame in order for the system to work! 
	 * @param deltaTime represents the time which has passed between the last frame and the current
	 */
	public void updateWaveSystem(float deltaTime)
	{
		if(_wave != null)
		{
			_wave.updateWave(deltaTime);
			if(_wave.isWaveOver())
			{
				createNewWave((_currentWave + 1) % 5 == 0 ? 2 : 1);
				
				if(getCurrentWaveType() != 2)
				{
					dispatchEvent(new Event(EVENT_WAVE_STARTED));
				}
				
				_timeWaitedForNextWave = 0;
			}
			
			if(_timeWaitedForNextWave >= 0)
			{
				_timeWaitedForNextWave += deltaTime;
				if(_timeWaitedForNextWave >=_waitTimeBetweenWaves)
				{
					_timeWaitedForNextWave = -1;
					if(getCurrentWaveType() == 2)
					{
						trainDriveOut().setCallbackMethod(new IEngineTweenMethod(){

							@Override
							public void onMethod(int tweenEventType, EngineTween tween) 
							{
								resetTrainPosition();
								dispatchEvent(new Event(EVENT_WAVE_STARTED));
								trainDriveIn().delay(1f).setCallbackMethod(new IEngineTweenMethod(){

									@Override
									public void onMethod(int tweenEventType, EngineTween tween) 
									{
										startWave();
									}}
								);
							}
							});
					}
					else
					{
						startWave();
					}
				}
			}
		}
	}
	
	/**
	 * Creates an enemy for the current wave if the current wave instance is given. Else it does nothing.
	 * @param wave which is the caller and the current active wave
	 * @param enemyType to spawn
	 */
	public Enemy waveCreateEnemy(Wave wave, EnemyFactory.EnemyType enemyType)
	{
		if(wave != _wave) { return null;}
		
		return createEnemy(enemyType);
	}
	
	/**
	 * Prepares the system for Garbage Collection.
	 */
	public void clean()
	{
		_designs = null;
		_wave = null;
		_playfield = null;
		_train.destroy();
		_train = null;
		_track.destroy();
		_track = null;
	}
	
	/**
	 * Creates an enemy of the given enemy type using the EneyFactory and places it above the playground on a random x axis.
	 * @param enemyType to spawn
	 */
	private Enemy createEnemy(EnemyFactory.EnemyType enemyType)
	{
		Enemy e = EnemyFactory.createEnemyOfTypeWithAI(enemyType, _playfield);
		int spawnX = this.getSpawnPointX();
		e.getComponent(BasicEnemyAIComponent.class).placeSelfOnLocation(spawnX, -1);
		GridTile tile = _playfield.getGrid().getTile(spawnX, -1);
		e.getTransformComponent().setPosition(tile.getWorldPositionX() + tile.getTileWidth() / 2, tile.getWorldPositionY() + tile.getTileHeight() / 2);
		
		return e;
	}
	
	/**
	 * Returns a random tile in the horizontal axis of the grid.
	 * @return The randomly selected horizontal tile grid position.
	 */
	private int getSpawnPointX()
	{
		return (int) Math.round(Math.random() * (float)(_playfield.getGrid().getTileAmountX() - 1));
	}
	
	/**
	 * Creates a new wave and puts it as current wave
	 * @param waveType to create
	 */
	private void createNewWave(int waveType)
	{
		if(_wave != null)
			_wave.clean();
		
		_currentWave++;
		_currentWaveType = waveType;
		_wave = _designs.getWaveDesign(this, _currentWave, waveType);
		
	}
	
	/**
	 * Starts the wave which was created with the 'createNewWave' method
	 */
	private void startWave()
	{
		_wave.startWave();
	}
	
	/**
	 * Resets the train position to its starting position (left, top. Out of screen)
	 */
	private void resetTrainPosition()
	{
		_train.getTransformComponent().setPosition(-_train.getTrainSize(), Engine.getHeight() *  0.95f);
	}
	
	/**
	 * Does the tween animation which makes the train drive into the playfield and stop in the middle top
	 * @return The EngineTween which controls the train tween which was created with this method
	 */
	private EngineTween trainDriveIn()
	{
		return _train.getTransformComponent().doPosition(Engine.getWidth() + _train.getTrainSize(), _train.getTransformComponent().getPositionY(), 2f, TweenStartType.GameTime);
	}
	
	/**
	 * Does the tween animation which makes the train drive out of the playfield and stop at the right top corner out of the screen
	 * @return The EngineTween which controls the train tween which was created with this method
	 */
	private EngineTween trainDriveOut()
	{
		return _train.getTransformComponent().doPosition(Engine.getWidth() + _train.getFullTrainSize(), _train.getTransformComponent().getPositionY(), 2f, TweenStartType.GameTime);
	}
}
