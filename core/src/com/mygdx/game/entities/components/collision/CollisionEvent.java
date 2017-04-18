package com.mygdx.game.entities.components.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.events.Event;

public class CollisionEvent extends Event 
{
	public CollisionEvent(String type, Fixture myFixture, Fixture otherFixture, Contact contactInfo)
	{
		super(type);
	}
}