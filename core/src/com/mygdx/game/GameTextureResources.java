package com.mygdx.game;

import com.mygdx.game.resources.BaseTextureResources;

public class GameTextureResources extends BaseTextureResources 
{

	@Override
	public void load() 
	{
		// TODO Auto-generated method stub
		this.loadResource("testImage", "badlogic.jpg");
		this.loadResource("cat", "catIdle.png", "catIdleData.xml");		
		this.loadResource("knight", "sprites.png", "sprites.xml");
		
		// Bow
		this.loadResource("bowDraw","weapon/bowDraw.png", "weapon/bowDraw.xml");
		this.loadResource("arrow", "weapon/arrow.png");

	}
}
