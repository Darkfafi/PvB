package com.mygdx.game.engine.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * This class can be inherited to load in audio from the folders and link them to keys.
 * This audio can also be disposed with this class.
 * The audio is put into 2 categories. Music and Sound.
 * @author Ramses Di Perna
 */
public abstract class BaseAudioResources extends BaseResource
{
	private HashMap<String, Music> _loadedMusic = new HashMap<String, Music>();
	private HashMap<String, Sound> _loadedSounds = new HashMap<String, Sound>();

	/**
	 * Gets the sound which was loaded in the 'load' method with the given key
	 * @param soundKey linked to the Sound
	 * @return The sound linked to the soundKey
	 */
	public Sound getSound(String soundKey)
	{
		return _loadedSounds.get(soundKey);
	}
	
	/**
	 * Gets the music which was loaded in the 'load' method with the given key
	 * @param musicKey linked to the music
	 * @return The music linked to the musicKey
	 */
	public Music getMusic(String musicKey)
	{
		return _loadedMusic.get(musicKey);
	}
	
	public void stopAllSounds()
	{
		for(Map.Entry<String, Sound> item : _loadedSounds.entrySet())
		{
			item.getValue().stop();
		}
	}
	
	public void stopAllMusic()
	{
		for(Map.Entry<String, Music> item : _loadedMusic.entrySet())
		{
			item.getValue().stop();
		}
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
	
	/**
	 * Disposes all the sounds
	 */
	public void disposeSounds()
	{
		for(Map.Entry<String, Sound> item : _loadedSounds.entrySet())
		{
			item.getValue().stop();
		}
	}
	
	/**
	 * Disposes all the music
	 */
	public void disposeMusic()
	{
		for(Map.Entry<String, Music> item : _loadedMusic.entrySet())
		{
			item.getValue().stop();
		}
	}
	
	/**
	 * Loads a resource into memory. This creates a Music type out of an audio clip.
	 * Music is automatically set to looping  
	 * @param key to link the Music under
	 * @param path to the audio file to load as Music
	 */
	protected void loadResourceMusic(String key, String path)
	{
		Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
		m.setLooping(true);
		_loadedMusic.put(key, m);
		this.getAssetManager().load(path, Music.class);
	}
	
	/**
	 * Loads a resource into memory. This creates a Sound type out of an audio clip.
	 * @param key to link the Sound under
	 * @param path to the audio file to load as Sound
	 */
	protected void loadResourceSound(String key, String path)
	{
		_loadedSounds.put(key, Gdx.audio.newSound(Gdx.files.internal(path)));
		this.getAssetManager().load(path, Sound.class);
	}
}
