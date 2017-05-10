package com.mygdx.game.waves;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.BasicEnemyAIComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.EventDispatcher;
import com.mygdx.game.entities.Enemy;
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
	private int _currentWave = 1;
	
	private Wave _wave = null;
	private IWaveDesigns _designs = null;
	
	
	/**
	 * This gives the tools needed for the WaveSystem to work
	 * @param playfield to give to the enemies as reference and to spawn the enemies on
	 * @param waveDesigns to get waves from which are perfectly designed with the information given to them
	 */
	public WaveSystem(Playfield playfield, IWaveDesigns waveDesigns)
	{
		_playfield = playfield;
		_designs = waveDesigns;
		_wave = _designs.getWaveDesign(this, _currentWave, 0);
		_wave.startWave();
		this.dispatchEvent(new Event(EVENT_WAVE_STARTED));
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
				_wave.clean();
				_currentWave++;
				_wave = _designs.getWaveDesign(this, _currentWave, 1);
				_wave.startWave();
				this.dispatchEvent(new Event(EVENT_WAVE_STARTED));
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
		e.getTransformComponent().setPosition(new Vector2(tile.getWorldPositionX() + tile.getTileWidth() / 2, tile.getWorldPositionY() + tile.getTileHeight() / 2));
		
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
}
