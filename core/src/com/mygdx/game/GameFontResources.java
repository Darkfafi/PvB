package com.mygdx.game;

import com.mygdx.game.engine.resources.BaseFontResources;

public class GameFontResources extends BaseFontResources 
{
	public static final String REGULAR_WHITE_BANDIDOS = "RWB_Font";
	@Override
	public void load() 
	{
		// TODO:  Load Game Fonts with the 'loadResource' method
		this.loadResource(REGULAR_WHITE_BANDIDOS, "fonts/testFont.fnt", "fonts/testFont.png");
	}

}
