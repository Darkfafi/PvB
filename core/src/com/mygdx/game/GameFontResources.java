package com.mygdx.game;

import com.mygdx.game.engine.resources.BaseFontResources;

/**
 * All the Game specific Fonts are loaded here
 * @author Ramses Di Perna
 *
 */
public class GameFontResources extends BaseFontResources 
{
	public static final String REGULAR_WHITE_BANDIDOS = "RWB_Font";
	public static final String SCORE_FONT_BANDIDOS = "Score_Font";
	public static final String WAVE_FONT_BANDIDOS = "Wave_Font";
	
	@Override
	public void load() 
	{
		this.loadResource(REGULAR_WHITE_BANDIDOS, "fonts/font_bandidos_white.fnt", "fonts/font_bandidos_white.png");
		this.loadResource(SCORE_FONT_BANDIDOS, "fonts/font_bandidos_score.fnt", "fonts/font_bandidos_score.png");
		this.loadResource(WAVE_FONT_BANDIDOS, "fonts/font_bandidos_wave.fnt", "fonts/font_bandidos_wave.png");
	}

}
