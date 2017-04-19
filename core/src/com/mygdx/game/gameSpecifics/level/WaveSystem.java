package com.mygdx.game.gameSpecifics.level;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameSpecifics.components.EnemyPlayfieldAIComponent;
import com.mygdx.game.gameSpecifics.entities.Enemy;
import com.mygdx.game.gameSpecifics.factories.EnemyFactory;

public class WaveSystem 
{
	private Playfield _playfield;
	private int _currentWave = 0;
	
	private Wave _wave = null;
	
	private BaseWaveDesignes _designes = null;
	
	public WaveSystem(Playfield playfield)
	{
		_playfield = playfield;
		_designes = new GameWaveDesignes(this);
		_wave = _designes.getWaveDesign(_currentWave, 1);
		_wave.startWave();
	}
	
	public void updateWaveSystem(float deltaTime)
	{
		if(_wave != null)
		{
			_wave.updateWave(deltaTime);
			if(_wave.isWaveOver())
			{
				_wave.clean();
				_wave = _designes.getWaveDesign(_currentWave, 1);
				_wave.startWave();
			}
		}
	}

	public void waveCreateEnemy(Wave wave, EnemyFactory.EnemyType enemyType)
	{
		if(wave != _wave) { return ;}
		
		createEnemy(enemyType);
	}
	
	public void clean()
	{
		_designes.clean();
		_designes = null;
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
	
	private Wave selectNewWave() 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
