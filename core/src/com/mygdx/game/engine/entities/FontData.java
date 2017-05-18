package com.mygdx.game.engine.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * This class holds the data needed to display a text in the a certain font.
 * @author Ramses Di Perna
 *
 */
public class FontData
{	
	private BitmapFont _bitmapFont;
	
	/**
	 * Loads the font data from the given locations and creates a BitmapFont for it.
	 * @param fontLocation is the location of the .fnt file for the font
	 * @param fontPngLocation is the location of the .png file for the font
	 */
	public FontData(String fontLocation, String fontPngLocation)
	{
		_bitmapFont = new BitmapFont(Gdx.files.internal(fontLocation), Gdx.files.internal(fontPngLocation), false); 
		_bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	/**
	 * Returns the BitmapFont which was made for the given font.
	 * @return BitmapFont which was loaded from the given data.
	 */
	public BitmapFont getFont()
	{
		return _bitmapFont;
	}
	
	/**
	 * Cleans the class and makes it ready for garbage collection
	 */
	public void clean()
	{
		_bitmapFont.dispose();
		_bitmapFont = null;
	}
}
