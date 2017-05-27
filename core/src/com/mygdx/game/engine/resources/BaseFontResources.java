package com.mygdx.game.engine.resources;

import java.util.HashMap;

import com.badlogic.gdx.audio.Music;
import com.mygdx.game.engine.entities.FontData;

/**
 * This class can be inherited to load in fonts from the folders and link them to keys.
 * @author Ramses Di Perna
 */
public abstract class BaseFontResources extends BaseResource 
{
	private HashMap<String, FontData> _fonts = new HashMap<String, FontData>();
	
	/**
	 * Returns a FontData by the name it was given in the 'load' method
	 * @param fontName which is linked to the FontData
	 * @return FontData linked to the given fontName
	 */
	public FontData getFontData(String fontName)
	{
		return _fonts.get(fontName);
	}
	
	@Override
	public void clean() 
	{
		for(int i = _fonts.size() - 1; i >= 0; i--)
		{
			_fonts.get(i).clean();
		}
		_fonts.clear();
		_fonts = null;
	}
	
	/**
	 * Loads a resource into memory. This creates a FontData with the given locations. 
	 * @param fontName to link the FontData with
	 * @param fontLocation is the location of the .fnt file for the font
	 * @param fontPngLocation is the location of the .png file for the font
	 */
	protected void loadResource(String fontName, String fontLocation, String fontPngLocation)
	{
		_fonts.put(fontName, new FontData(fontLocation, fontPngLocation));
	}
}
