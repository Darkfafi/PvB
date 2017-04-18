package com.mygdx.game;

import com.mygdx.game.resources.BaseAudioResources;

public class GameAudioResources extends BaseAudioResources 
{
	public static final String MUSIC_WAVE_SOUNDTRACK = "MusicWaveSoundtrack";
	
	public static final String SOUND_BOW_DRAW = "bowDraw";
	public static final String SOUND_BOW_RELEASE = "BowRelease";
	
	@Override
	public void load() 
	{
		this.loadResourceMusic(MUSIC_WAVE_SOUNDTRACK, "audio/AUD_MU_WaveMusic_01.wav");
		this.loadResourceSound(SOUND_BOW_RELEASE, "audio/effects/AUD_SG_BowRelease_03.wav");
		this.loadResourceSound(SOUND_BOW_DRAW, "audio/effects/AUD_SG_BowPull_Loop.wav");
	}

}
