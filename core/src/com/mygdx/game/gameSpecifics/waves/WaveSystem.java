package com.mygdx.game.gameSpecifics.waves;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameSpecifics.components.EnemyPlayfieldAIComponent;
import com.mygdx.game.gameSpecifics.entities.Enemy;
import com.mygdx.game.gameSpecifics.factories.EnemyFactory;
import com.mygdx.game.gameSpecifics.level.GridTile;
import com.mygdx.game.gameSpecifics.level.Playfield;

/**
 * The wave system is a system which tracks the current wave and switches to the next wave. 
 * It also spawns enemies on request and gives those enemies the components to work as 'wave slaves'.
 * @author Ramses Di Perna
 *
 */
public class WaveSystem 
{
	private Playfield _playfield;
	private int _currentWave = 1;
	
	private Wave _wave = null;
	private BaseWaveDesigns _designs = null;
	
	
	/**
	 * This gives the tools needed for the WaveSystem to work
	 * @param playfield to give to the enemies as reference and to spawn the enemies on
	 * @param waveDesigns to get waves from which are perfectly designed with the information given to them
	 */
	public WaveSystem(Playfield playfield, BaseWaveDesigns waveDesigns)
	{
		_playfield = playfield;
		_designs = waveDesigns;
		_wave = _designs.getWaveDesign(this, _currentWave, 0);
		_wave.startWave();
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
			}
		}
	}
	
	/**
	 * Creates an enemy for the current wave if the current wave instance is given. Else it does nothing.
	 * @param wave which is the caller and the current active wave
	 * @param enemyType to spawn
	 */
	public void waveCreateEnemy(Wave wave, EnemyFactory.EnemyType enemyType)
	{
		if(wave != _wave) { return ;}
		
		createEnemy(enemyType);
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
	private void createEnemy(EnemyFactory.EnemyType enemyType)
	{
		Enemy e = EnemyFactory.createEnemyOfType(enemyType);
		int spawnX = this.getSpawnPointX();
		e.addComponent(new EnemyPlayfieldAIComponent(_playfield, EnemyFactory.getMovementSpeedForType(enemyType))).placeSelfOnLocation(spawnX, -1);
		GridTile tile = _playfield.getGrid().getTile(spawnX, -1);
		e.getTransformComponent().setPosition(new Vector2(tile.getWorldPositionX() + tile.getTileWidth() / 2, tile.getWorldPositionY() + tile.getTileHeight() / 2));
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
