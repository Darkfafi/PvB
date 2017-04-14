package com.mygdx.game.entities.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.entities.BaseEntityComponent;

/**
 * This component contains information on what collision category the entity has and with what the entity
 * can collide with.
 * @author Djamali Jones
 */
public class CollisionComponent extends BaseEntityComponent {

	private Body _body;
	
	private short _type; 
	private List<Short> _allowedTypes = new ArrayList<Short>();
	private Stack<FixtureDef> _fixDefs = new Stack<FixtureDef>();
	
	/**
	 * Returns the type/collision category for this entity
	 * @return type/collision category
	 */
	public int getType()
	{
		return _type;
	}
	
	/**
	 * Returns the _body for this entity
	 * @return the '_body' for the world. 
	 */
	public Body getBody()
	{
		return _body;
	}
	
	/**
	 * Sets the collider category type for this entity
	 * @param type
	 */
	public void setType(short type)
	{
		_type = type;
	}
	
	/**
	 * Return the types/maskBits this entity is allowed to collide with
	 * @return maskBits value
	 */
	public List<Short> getAllowedTypes()
	{
		return _allowedTypes;
	}
	
	/**
	 * Adds a maskBit type to an array so it can be allowed to be collided with this entity
	 * @param type
	 */
	public void addAllowedType(short type)
	{
		_allowedTypes.add(type);
		
		for(int i = 0; i < _allowedTypes.size(); i++)
		{
			for(int j = 0; j < _fixDefs.size(); j++)
			{
				_fixDefs.get(j).filter.maskBits = _allowedTypes.get(i);
			}
		}
	}
	
	/**
	 * Creates and adds a fixture for this entity. A FixtureDef must be given so that multiple fixtures can be added to the entity.
	 * NOTE: A shape must be created first. This has to be given to the fixDef parameter.
	 * @param fixDef
	 */
	public void createFixture(FixtureDef fixDef)
	{
		fixDef.filter.categoryBits = _type;
		if(_body == null)
		{
			_fixDefs.push(fixDef);
		}
		else
		{
			_body.createFixture(fixDef);
		}
	}
	
	/**
	 * 
	 * @param body
	 */
	public void setBody(Body body)
	{
		_body = body;
		
		for(int i = 0; i < _fixDefs.size(); i++)
		{
			body.createFixture(_fixDefs.get(i));
		}
		_fixDefs.clear();
	}
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updated(float deltaTime) {
		//TODO Auto-generated method stub
	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		
	}

}
