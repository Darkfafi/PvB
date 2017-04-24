package com.mygdx.game.waves;

import com.mygdx.game.factories.EnemyFactory;

/**
 * This is a general wave designer which is made for the general game its entire flow to work.
 * @author Ramses Di Perna
 *
 */
public class GameWaveDesigns extends BaseWaveDesigns 
{
	@Override
	public Wave getWaveDesign(WaveSystem waveSystem, int currentWave, int waveType) 
	{
		Wave wave = new Wave(waveSystem);
		
		wave.addSection(EnemyFactory.EnemyType.LightBandit, 2 + (2 * currentWave % 2), 8);
		
		if(waveType > 0)
		{
			wave.addSection(EnemyFactory.EnemyType.LightBandit, 5, 8);
			wave.addSection(EnemyFactory.EnemyType.LightBandit, 5, 10);
		}
		return wave;
	}

}
