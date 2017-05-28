package com.mygdx.game.level;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.factories.EnemyFactory.EnemyType;
import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.traps.TrapSpawnInfo;
import com.mygdx.game.waves.Wave;
import com.mygdx.game.waves.WaveSystem;

public class DesertLevel implements ILevelBlueprint
{
	private int _trainSwitchedCounter = 0;
	
	@Override
	public Wave getWaveDesign(WaveSystem waveSystem, int currentWave, int waveType) 
	{
		boolean trainSwitchedWave = waveType == 2;
		
		if(trainSwitchedWave)
		{
			_trainSwitchedCounter++;
		}
		
		Wave wave = new Wave(waveSystem, true);
		
		if(trainSwitchedWave)
		{
			// Train Switched Extra Hard Wave
			if(_trainSwitchedCounter % 2 == 0)
			{
				// Bombers running in between the wave
				wave.addSection(EnemyType.LightBandit, countForEach(4, _trainSwitchedCounter, 1, 4) * 2, 0.5f + (1 * countForEach(2, _trainSwitchedCounter, 1, 8) / 8));
			}
			else
			{
				// Big guys
				wave.addSection(EnemyType.HeavyBandit, countForEach(3, _trainSwitchedCounter, 1, 6), 0.5f + (1 * countForEach(4, _trainSwitchedCounter, 1, 8) / 8));
			}
		}
		
		// Normal Routine
		wave.addSection(EnemyType.MediumBandit, countForEach(2, currentWave, 3, 15), 4 + countForEach(2, currentWave, 2, 6));
		
		if(trainSwitchedWave)
		{
			// Train Switched Extra Hard Wave
			if(_trainSwitchedCounter % 2 == 0 && currentWave % 5 != 0)
			{
				wave.addSection(EnemyType.LightBandit, countForEach(4, currentWave, 2, 6), 1 +countForEach(2, currentWave, 3, 6));
			}
			if(_trainSwitchedCounter % 4 == 0)
			{
				wave.addSection(EnemyType.HeavyBandit, countForEach(4, currentWave, 2, 10), 3 + countForEach(2, currentWave, 3, 7));
			}
		}
		else if(_trainSwitchedCounter > 0)
		{
			// Train Switched Extra Hard Wave
			if(_trainSwitchedCounter > 2 && _trainSwitchedCounter % 2 == 0)
			{
				if(currentWave % 3 == 0)
					wave.addSection(EnemyType.LightBandit, countForEach(4, _trainSwitchedCounter, 1, 8), 1 + countForEach(4, _trainSwitchedCounter, 3, 7));
			}
			
			if(currentWave % 2 == 0)
				wave.addSection(EnemyType.HeavyBandit, countForEach(2, _trainSwitchedCounter, 1, 10), 3 + countForEach(2, _trainSwitchedCounter, 3, 7));
			
		}
		
		// Normal Routine
		wave.addSection(EnemyType.MediumBandit, countForEach(4, currentWave, 4, 20), 4 + countForEach(4, currentWave, 2, 5));
		
		if(_trainSwitchedCounter % 2 == 0 && currentWave % 2 == 0)
		{
			wave.addSection(EnemyType.MediumBandit, countForEach(4, currentWave, 4, 20), 3 + countForEach(4, currentWave, 2, 5));
		}
		
		return wave;
	}

	@Override
	public int getGridAmountX() 
	{
		return 6;
	}

	@Override
	public int getGridAmountY() 
	{
		return 15;
	}

	@Override
	public Texture getLevelBackground() 
	{
		return Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_GAME_BACKGROUND_01).getTextureToDraw();
	}

	@Override
	public Sound getLevelMusic() 
	{
		return Engine.getAudioResources().getSound(GameAudioResources.SOUND_WAVE_SOUNDTRACK);
	}
	
	@Override
	public TrapSpawnInfo[] getTrapSpawnInfos()
	{
		return new TrapSpawnInfo[]
		{
				new TrapSpawnInfo(0, 4, TrapFactory.Direction.Right, 0.05f,
						new TrapFactory.TrapType[]{
								TrapFactory.TrapType.Block_TrainTrap
				}),
				new TrapSpawnInfo(4, 8, TrapFactory.Direction.Left, 0f,
						new TrapFactory.TrapType[]{
								TrapFactory.TrapType.Damage_BarrelExplosionTrap
				})
		};
	}
	
	protected int countForEach(int value, int wave)
	{
		return countForEach(value, wave, 0, 1337);
	}

	protected int countForEach(int each, int total, int minValue, int maxValue)
	{
		int v = (int)Math.floor((float)total / (float)each);
		
		if(v < minValue)
			v = minValue;
		else if(v > maxValue)
			v = maxValue;
		
		return v;
	}
}
