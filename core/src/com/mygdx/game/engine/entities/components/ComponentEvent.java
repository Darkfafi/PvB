package com.mygdx.game.engine.entities.components;

import com.mygdx.game.engine.events.Event;

/**
 * This Event is an event which also contains a Component.
 * All Events regarding Components use this event or an extension of it.
 * @author Djamali
 */
public class ComponentEvent extends Event {

	private BaseEntityComponent _component;
	
	public ComponentEvent(String type, BaseEntityComponent component) 
	{
		super(type);
		_component = component;
	}
	
	/**
	 * Returns the Component linked to the event.
	 * @return Returns the Component which was linked into the ComponentEvent Constructor
	 */
	public BaseEntityComponent getComponent()
	{
		return _component;
	}
	
	@Override
	public void clean()
	{
		super.clean();
		_component = null;
	}
}