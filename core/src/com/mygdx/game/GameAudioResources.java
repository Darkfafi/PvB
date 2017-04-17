package com.mygdx.game;

import com.mygdx.game.resources.BaseAudioResources;

public class GameAudioResources extends BaseAudioResources 
{
	public static final String SOUND_BOW_DRAW = "bowDraw";
	public static final String SOUND_BOW_RELEASE = "BowRelease";
	
	@Override
	public void load() 
	{
		this.loadResourceMusic("testMusic", "testMusic.mp3");
		this.loadResourceSound(SOUND_BOW_RELEASE, "audio/effects/AUD_SG_BowRelease_03.wav");
		this.loadResourceSound(SOUND_BOW_DRAW, "audio/effects/AUD_SG_BowPull_Mid.wav");
	}

}
