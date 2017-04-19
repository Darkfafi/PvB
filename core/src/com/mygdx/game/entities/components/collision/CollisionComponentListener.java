package com.mygdx.game.entities.components.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.globals.EngineGlobals;

public class CollisionComponentListener implements ContactListener {
	
	/**
	 * Called when two fixtures start to collide
	 */
	@Override
	public void beginContact(Contact contact) 
	{
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		if(fixA.getUserData().getClass() == CollisionComponent.class)
			((CollisionComponent)fixA.getUserData()).dispatchEvent(
				new CollisionEvent(EngineGlobals.GLOBAL_EVENT_COLLISION_ENTER, fixA, fixB, contact)
			);
		
		if(fixB.getUserData().getClass() == CollisionComponent.class)
			((CollisionComponent)fixB.getUserData()).dispatchEvent(
				new CollisionEvent(EngineGlobals.GLOBAL_EVENT_COLLISION_ENTER, fixB, fixA, contact)
			);
	}

	/**
	 * Called when two fixtures have stopped to collide
	 */
	@Override
	public void endContact(Contact contact)
	{
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		if(fixA.getUserData().getClass() == CollisionComponent.class)
			((CollisionComponent)fixA.getUserData()).dispatchEvent(
				new CollisionEvent(EngineGlobals.GLOBAL_EVENT_COLLISION_EXIT, fixA, fixB, contact)
			);

		if(fixB.getUserData().getClass() == CollisionComponent.class)
			((CollisionComponent)fixB.getUserData()).dispatchEvent(
				new CollisionEvent(EngineGlobals.GLOBAL_EVENT_COLLISION_EXIT, fixB, fixA, contact)
			);
	}

	/**
	 * 
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
