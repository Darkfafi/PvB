package com.mygdx.game.resources;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public abstract class BaseAudioResources extends BaseResource
{
	private HashMap<String, Music> _loadedMusic = new HashMap<String, Music>();
	private HashMap<String, Sound> _loadedSounds = new HashMap<String, Sound>();

	public Sound getSound(String soundKey)
	{
		return _loadedSounds.get(soundKey);
	}
	
	public Music getMusic(String musicKey)
	{
		return _loadedMusic.get(musicKey);
	}
	
	@Override
	public void clean() 
	{
		// TODO Auto-generated method stub
		disposeSounds();
		disposeMusic();
		_loadedSounds.clear();
		_loadedMusic.clear();
		
		_loadedSounds = null;
		_loadedMusic = null;
	}
	
	public void disposeSounds()
	{
		for(int i = 0; i < _loadedSounds.size(); i++)
		{
			_loadedSounds.get(i).dispose();
		}
	}
	
	public void disposeMusic()
	{
		for(int i = 0; i < _loadedMusic.size(); i++)
		{
			_loadedMusic.get(i).dispose();
		}
	}

	protected void loadResourceMusic(String key, String path)
	{
		Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
		m.setLooping(true);
		_loadedMusic.put(key, m);
	}
	
	protected void loadResourceSound(String key, String path)
	{
		_loadedSounds.put(key, Gdx.audio.newSound(Gdx.files.internal(path)));
	}
}
