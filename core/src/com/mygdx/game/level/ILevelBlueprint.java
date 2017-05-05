package com.mygdx.game.level;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.waves.IWaveDesigns;

public interface ILevelBlueprint extends IWaveDesigns
{
	public int getGridAmountX();
	public int getGridAmountY();
	public Texture getLevelBackground();
	public Music getLevelMusic();
	
	// TODO: SPECIFY TRAP SPAWN LOCATIONS AND SPAWN TYPES
}
