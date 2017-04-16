package com.mygdx.game;

import com.mygdx.game.resources.BaseTextureResources;

public class GameTextureResources extends BaseTextureResources 
{

	@Override
	public void load() 
	{
		this.loadResource("testImage", "badlogic.jpg");
		
		// Bow
		this.loadResource("bowDraw","weapon/bowDraw.png", "weapon/bowDraw.xml");
		this.loadResource("arrow", "weapon/arrow.png");
		
		// Enemies
		this.loadResource("light_Bandit_0_Run","enemies/banditARun.png", "enemies/banditARun.xml");
		this.loadResource("light_Bandit_1_Run","enemies/banditBRun.png", "enemies/banditBRun.xml");

	}
}
