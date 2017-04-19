package com.mygdx.game.gameSpecifics.level;

import com.mygdx.game.gameSpecifics.factories.EnemyFactory;

public class GameWaveDesignes extends BaseWaveDesignes {

	public GameWaveDesignes(WaveSystem ws) 
	{
		super(ws);
		
	}

	@Override
	public Wave getWaveDesign(int currentWave, int waveType) 
	{
		Wave wave = new Wave(this.waveSystem);
		wave.addEnemySection(EnemyFactory.EnemyType.LightBandit, 5, 8);
		wave.addEnemySection(EnemyFactory.EnemyType.LightBandit, 10, 10);
		return wave;
	}

}
