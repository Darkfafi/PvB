package com.mygdx.game.gameSpecifics.level;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameSpecifics.components.EnemyPlayfieldAIComponent;
import com.mygdx.game.gameSpecifics.entities.Enemy;
import com.mygdx.game.gameSpecifics.factories.EnemyFactory;

public class WaveSystem 
{
	private Playfield _playfield;

	private float _timeSinceSpawnedEnemy = 1f;
	
	public WaveSystem(Playfield playfield)
	{
		_playfield = playfield;
		System.out.println(playfield.getGrid());
	}
	
	public void updateWaveSystem(float deltaTime)
	{
		_timeSinceSpawnedEnemy += deltaTime;
		if(_timeSinceSpawnedEnemy >= 1.5f)
		{
			createEnemy(EnemyFactory.EnemyType.LightBandit);
			_timeSinceSpawnedEnemy = 0f;
		}
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
