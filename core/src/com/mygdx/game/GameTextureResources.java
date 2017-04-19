package com.mygdx.game;

import com.mygdx.game.resources.BaseTextureResources;

public class GameTextureResources extends BaseTextureResources 
{
	public static final String SPRITE_GAME_BACKGROUND_01 = "gameBackground01Sprite";
	public static final String SPRITE_BOW_AIM_TARGET = "bowAimTarget";
	
	public static final String ANIMATION_BOW_DRAW = "bowDraw";
	public static final String ANIMATION_BOW_ARROW = "arrowFly";
	
	public static final String ANIMATION_LIGHT_BANDIT_0_IDLE = "light_Bandit_0_Idle";
	public static final String ANIMATION_LIGHT_BANDIT_1_IDLE = "light_Bandit_1_Idle";

	public static final String ANIMATION_LIGHT_BANDIT_0_RUN = "light_Bandit_0_Run";
	public static final String ANIMATION_LIGHT_BANDIT_1_RUN = "light_Bandit_1_Run";

	public static final String ANIMATION_LIGHT_BANDIT_0_DEATH = "light_Bandit_0_Death";
	public static final String ANIMATION_LIGHT_BANDIT_1_DEATH = "light_Bandit_1_Death";
	
	@Override
	public void load() 
	{
		this.loadResource("testImage", "badlogic.jpg");
		
		this.loadResource(SPRITE_GAME_BACKGROUND_01, "background_01.png");
		this.loadResource(SPRITE_BOW_AIM_TARGET, "target.png");
		
		// Bow
		this.loadResource(ANIMATION_BOW_DRAW,"weapon/bowDraw.png", "weapon/bowDraw.xml");
		this.loadResource(ANIMATION_BOW_ARROW, "weapon/arrowFly.png", "weapon/arrowFly.xml");
		
		// Enemies
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_IDLE,"enemies/banditAIdle.png", "enemies/banditAIdle.xml");
		this.loadResource(ANIMATION_LIGHT_BANDIT_1_IDLE,"enemies/banditBIdle.png", "enemies/banditBIdle.xml");
		
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_RUN,"enemies/banditARun.png", "enemies/banditARun.xml");
		this.loadResource(ANIMATION_LIGHT_BANDIT_1_RUN,"enemies/banditBRun.png", "enemies/banditBRun.xml");
		
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_DEATH,"enemies/banditADeath.png", "enemies/banditADeath.xml");
		this.loadResource(ANIMATION_LIGHT_BANDIT_1_DEATH,"enemies/banditBDeath.png", "enemies/banditBDeath.xml");

	}
}
