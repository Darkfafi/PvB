package com.mygdx.game.level;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.factories.EnemyFactory;
import com.mygdx.game.factories.TrapFactory;
import com.mygdx.game.traps.TrapSpawnInfo;
import com.mygdx.game.waves.Wave;
import com.mygdx.game.waves.WaveSystem;

public class DesertLevel implements ILevelBlueprint
{
	@Override
	public Wave getWaveDesign(WaveSystem waveSystem, int currentWave, int waveType) 
	{
		boolean isKillTillEndWave = waveType != 2;
		
		Wave wave = new Wave(waveSystem, isKillTillEndWave);

		if(currentWave % 10 == 0)
		{
			wave.addSection(EnemyFactory.EnemyType.HeavyBandit, countForEachOnWave(10, currentWave), 5.5f);
		}
		
		wave.addSection(EnemyFactory.EnemyType.MediumBandit, 2 + countForEachOnWave(4, currentWave), 4 + countForEachOnWave(4, currentWave) * 1.5f);
		
		if(waveType > 0)
		{	
			wave.addSection(EnemyFactory.EnemyType.MediumBandit, 2 + countForEachOnWave(3, currentWave), 4 + countForEachOnWave(3, currentWave) * 1.5f);
			
			if(currentWave % 5 == 0)
			{
				wave.addSection(EnemyFactory.EnemyType.MediumBandit, 5, 0);			
				wave.addSection(EnemyFactory.EnemyType.HeavyBandit, countForEachOnWave(5, currentWave), 4f);
			}
			
			wave.addSection(EnemyFactory.EnemyType.MediumBandit, 1 + countForEachOnWave(2, currentWave), 4 + countForEachOnWave(2, currentWave) * 1.5f);			
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
	public Music getLevelMusic() 
	{
		return Engine.getAudioResources().getMusic(GameAudioResources.MUSIC_WAVE_SOUNDTRACK);
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
	
	private int countForEachOnWave(int value, int wave)
	{
		return (int)Math.floor((float)wave / (float)value);
	}
}
