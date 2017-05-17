package com.mygdx.game.events;

import com.mygdx.game.engine.events.Event;

public class TutorialEvent extends Event 
{
	private boolean _tutorialCompleted;
	
	public TutorialEvent(String type, boolean tutorialCompleted)
	{
		super(type);
		_tutorialCompleted = tutorialCompleted;
	}
}
