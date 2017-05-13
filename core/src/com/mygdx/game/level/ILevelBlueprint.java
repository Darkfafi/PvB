package com.mygdx.game.level;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.traps.TrapSpawnInfo;
import com.mygdx.game.waves.IWaveDesigns;

/**
 * This interface represents a blueprint which will be used by the Playfield class to generate a level with and play the suiting music of that level.
 * @author RamsesPC
 *
 */
public interface ILevelBlueprint extends IWaveDesigns
{
	public int getGridAmountX();
	public int getGridAmountY();
	public Texture getLevelBackground();
	public Music getLevelMusic();
	public TrapSpawnInfo[] getTrapSpawnInfos();
}