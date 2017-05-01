package com.mygdx.game.engine.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * This class holds the data needed to display a text in the a certain font.
 * @author Ramses Di Perna
 *
 */
public class FontData
{
	private FileHandle _font;
	private FileHandle _fontPng;
	
	/**
	 * Loads the font data from the given locations
	 * @param fontLocation is the location of the .fnt file for the font
	 * @param fontPngLocation is the location of the .png file for the font
	 */
	public FontData(String fontLocation, String fontPngLocation)
	{
		_font = Gdx.files.internal(fontLocation); 
		_fontPng = Gdx.files.internal(fontPngLocation);
	}
	
	/**
	 * Returns the FileHandle loaded from the .fnt file given in the constructor
	 * @return FileHandle which was loaded from the given .fnt
	 */
	public FileHandle getFont()
	{
		return _font;
	}
	
	/**
	 * Returns the FileHandle loaded from the .png file given in the constructor
	 * @return FileHandle which was loaded from the given .png
	 */
	public FileHandle getFontPng()
	{
		return _fontPng;
	}
	
	/**
	 * Cleans the class and makes it ready for garbage collection
	 */
	public void clean()
	{
		_font = null;
		_fontPng = null;
	}
}
