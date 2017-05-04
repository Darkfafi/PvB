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
		boolean isKillTillEndWave = waveType != 2;
		
		Wave wave = new Wave(waveSystem, isKillTillEndWave);
		
		wave.addSection(EnemyFactory.EnemyType.LightBandit, 2 + countForEachOnWave(2, currentWave), 2 + countForEachOnWave(2, currentWave) * 1.5f);
		
		if(waveType > 0)
		{
			wave.addSection(EnemyFactory.EnemyType.LightBandit, 2, 8);
			wave.addSection(EnemyFactory.EnemyType.LightBandit, 5, 20);
		}
		
		return wave;
	}

	
	private int countForEachOnWave(int value, int wave)
	{
		return (int)Math.floor((float)wave / (float)value);
	}
}
