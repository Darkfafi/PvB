package com.mygdx.game.entities.components.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.events.Event;

/**
 * This Event and all extending events classes contain the information for collision.
 * This Event type is triggered when a collision has occured.
 * @author Ramses Di Perna
 *
 */
public class CollisionEvent extends Event 
{
	private Fixture _myCollidedFixture;
	private Fixture _otherCollidedFixture;
	private Contact _contactInfo;
	private CollisionComponent _otherCollisionComponent;
	
	/**
	 * 
	 * @param type of event which represents the collision type
	 * @param myFixture the collided fixture of dispatching class
	 * @param otherFixture the collided fixture of other collided class
	 * @param otherCollisionComponent the collision component of other collided class
	 * @param contactInfo the full LibGdx contact info of the collision
	 */
	public CollisionEvent(String type, Fixture myFixture, Fixture otherFixture, CollisionComponent otherCollisionComponent, Contact contactInfo)
	{
		super(type);
		_myCollidedFixture = myFixture;
		_otherCollidedFixture = otherFixture;
		_contactInfo = contactInfo;
		_otherCollisionComponent = otherCollisionComponent;
	}
	
	/**
	 * Returns the collided fixture of dispatching class
	 * @return the collided fixture.
	 */
	public Fixture getMyCollidedFixture()
	{
		return _myCollidedFixture;
	}
	
	/**
	 * Returns the collided fixture of other collided class
	 * @return the collided fixture.
	 */
	public Fixture getOtherCollidedFixture()
	{
		return _otherCollidedFixture;
	}
	
	/**
	 * Returns the full LibGdx contact info of the collision
	 * @return The Contact collision info
	 */
	public Contact getFullContactInfo()
	{
		return _contactInfo;
	}
	
	/**
	 * Returns the collision component of other collided class
	 * With this you can access the parent of the component and check its tag for example.
	 * @return The collision component linked to the collided fixture of the other class
	 */
	public CollisionComponent getOtherCollisionComponent()
	{
		return _otherCollisionComponent;
	}
}