package com.mygdx.game.level;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
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
		
		wave.addSection(EnemyFactory.EnemyType.LightBandit, 2 + countForEachOnWave(2, currentWave), 2 + countForEachOnWave(2, currentWave) * 1.5f);
		
		if(waveType > 0)
		{
			wave.addSection(EnemyFactory.EnemyType.LightBandit, 2, 8);
			wave.addSection(EnemyFactory.EnemyType.LightBandit, 5, 20);
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
				})
		};
	}
	
	private int countForEachOnWave(int value, int wave)
	{
		return (int)Math.floor((float)wave / (float)value);
	}
}
