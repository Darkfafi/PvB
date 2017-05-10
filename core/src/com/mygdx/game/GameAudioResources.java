package com.mygdx.game;

import com.mygdx.game.engine.resources.BaseAudioResources;

public class GameAudioResources extends BaseAudioResources 
{
	public static final String MUSIC_WAVE_SOUNDTRACK = "MusicWaveSoundtrack";
	public static final String MUSIC_MENU_SOUNDTRACK = "MusicMenuSoundtrack";
	
	public static final String SOUND_BOW_DRAW = "bowDraw";
	public static final String SOUND_BOW_RELEASE = "BowRelease";
	
	public static final String SOUND_ARROW_HIT_NOTHING = "ArrowHitNothing";
	public static final String SOUND_ARROW_HIT_ENEMY = "ArrowHitEnemy";
	public static final String SOUND_ARROW_HIT_TARGET = "ArrowHitTarget";
	
	public static final String SOUND_TRAP_TRAIN_EFFECT = "TrainEffect";
	
	@Override
	public void load() 
	{
		this.loadResourceMusic(MUSIC_MENU_SOUNDTRACK, "audio/AUD_MU_MenuLoop.wav");
		this.loadResourceMusic(MUSIC_WAVE_SOUNDTRACK, "audio/AUD_MU_WaveMusic.wav");
		
		this.loadResourceSound(SOUND_BOW_RELEASE, "audio/effects/AUD_SG_BowRelease_Light.wav");
		this.loadResourceSound(SOUND_BOW_DRAW, "audio/effects/AUD_SG_BowPull_Loop.wav");
		
		this.loadResourceSound(SOUND_ARROW_HIT_NOTHING, "audio/effects/AUD_SG_ArrowHit_Wood.wav");
		this.loadResourceSound(SOUND_ARROW_HIT_ENEMY, "audio/effects/AUD_SG_ArrowHit_Enemy.wav");
		this.loadResourceSound(SOUND_ARROW_HIT_TARGET, "audio/effects/AUD_SG_ArrowHit_Target.wav");
		
		this.loadResourceSound(SOUND_TRAP_TRAIN_EFFECT, "audio/effects/trainSoundEffect.mp3");
	}

}
