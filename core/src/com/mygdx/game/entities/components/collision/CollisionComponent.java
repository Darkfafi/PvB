package com.mygdx.game.entities.components.collision;

import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.entities.BaseEntityComponent;
import com.mygdx.game.resources.CollisionResources;

/**
 * This component contains information on what collision category the entity has and with what the entity
 * can collide with.
 * @author Djamali Jones
 */
public class CollisionComponent extends BaseEntityComponent {

	private Body _body;
	private Stack<FixtureDef> _fixDefs = new Stack<FixtureDef>();
	
	private Vector2 _velocity = new Vector2(0,0); 
	
	
	/**
	 * Returns the _body for this entity
	 * @return the '_body' for the world. 
	 */
	public Body getBody()
	{
		return _body;
	}
	
	/**
	 * Sets the current body and puts the given fixtures to it.
	 * @param body
	 */
	public void setBody(Body body)
	{
		_body = body;
		
		if(_body == null) { return; }
		for(int i = 0; i < _fixDefs.size(); i++)
		{
			createFixtureForBody(_fixDefs.get(i), body);
		}
		_fixDefs.clear();
	}
	
	/**
	 * Creates and adds a fixture for this entity. A FixtureDef must be given so that multiple fixtures can be added to the entity.
	 * NOTE: A shape must be created first. This has to be given to the fixDef parameter.
	 * @param fixDef
	 */
	public void createFixture(FixtureDef fixDef, short _type)
	{
		fixDef.filter.categoryBits = _type;
		if(_body == null)
		{
			_fixDefs.push(fixDef);
		}
		else
		{
			createFixtureForBody(fixDef, _body);
		}
	}
	
	/**
	 * Creates a Fixture for the entity's Body using the FixtureDef def
	 * And also sets the fixture's userData as this object
	 * @param def
	 * @param body
	 */
	private void createFixtureForBody(FixtureDef def, Body body)
	{
		Fixture newFix = body.createFixture(def);
		newFix.setUserData(this);
	}
	
	public Vector2 getVelocity()
	{
		return new Vector2(_velocity.x, _velocity.y);
	}
	
	public void setVelocity(float xVelocity, float yVelocity)
	{
		this.getBody().setLinearVelocity(xVelocity, yVelocity);
		_velocity.x = xVelocity;
		_velocity.y = yVelocity;
	}
	
	public void stopVelocity()
	{
		this.getBody().setLinearVelocity(0, 0);
		_velocity.x = 0;
		_velocity.y = 0;
	}
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updated(float deltaTime) 
	{
		if(_velocity.len() > 0)
		{
			this.getParentOfComponent().getTransformComponent().setPosition(new Vector2(this.getBody().getPosition().x * CollisionResources.PIXEL_PER_METER, this.getBody().getPosition().y * CollisionResources.PIXEL_PER_METER));
			this.getParentOfComponent().getTransformComponent().setRotation((float) -Math.toDegrees(this.getBody().getAngle()));
		}
	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		_body = null;
		_fixDefs.clear();
		_fixDefs = null;
	}

}
