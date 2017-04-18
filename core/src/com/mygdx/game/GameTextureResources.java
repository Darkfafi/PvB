package com.mygdx.game;

import com.mygdx.game.resources.BaseTextureResources;

public class GameTextureResources extends BaseTextureResources 
{
	public static final String SPRITE_GAME_BACKGROUND_01 = "gameBackground01Sprite";
	
	public static final String ANIMATION_BOW_DRAW = "bowDraw";
	public static final String ANIMATION_BOW_ARROW = "arrowFly";
	
	public static final String ANIMATION_LIGHT_BANDIT_0_RUN = "light_Bandit_0_Run";
	public static final String ANIMATION_LIGHT_BANDIT_1_RUN = "light_Bandit_1_Run";
	
	@Override
	public void load() 
	{
		this.loadResource("testImage", "badlogic.jpg");
		
		this.loadResource(SPRITE_GAME_BACKGROUND_01, "background_01.png");
		
		// Bow
		this.loadResource(ANIMATION_BOW_DRAW,"weapon/bowDraw.png", "weapon/bowDraw.xml");
		this.loadResource(ANIMATION_BOW_ARROW, "weapon/arrowFly.png", "weapon/arrowFly.xml");
		
		// Enemies
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_RUN,"enemies/banditARun.png", "enemies/banditARun.xml");
		this.loadResource(ANIMATION_LIGHT_BANDIT_1_RUN,"enemies/banditBRun.png", "enemies/banditBRun.xml");

	}
}
