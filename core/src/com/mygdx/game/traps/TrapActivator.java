package com.mygdx.game.traps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.collision.CollisionComponent;
import com.mygdx.game.engine.entities.components.collision.CollisionEvent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.globals.EngineGlobals;
import com.mygdx.game.engine.resources.CollisionResources;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.factories.EffectFactory;
import com.mygdx.game.globals.Tags;

/**
 * This is an entity which allows for interaction between projectiles and traps.
 * When this entity is hit by a projectile, the linked trap will be triggered.
 * @author Ramses Di Perna
 *
 */
public class TrapActivator extends BaseEntity implements IEventReceiver
{
	private ITrap _linkedTrap;
	private boolean _requestedActivateTrap = false;
	
	/**
	 * The trap activator needs to be linked to a trap in order to trigger it on activation.
	 * @param trapToLink Trap to link to the activator
	 */
	public TrapActivator(ITrap trapToLink)
	{
		this.addTag(Tags.TAG_TRAP_ACTIVATOR);
		linkToTrap(trapToLink);
	}
	
	/**
	 * Links a trap to the activator so it can trigger it on activation. 
	 * @param trapToLink Trap to link to the activator
	 */
	public void linkToTrap(ITrap trapToLink)
	{
		_linkedTrap = trapToLink;
		activateTrap();
	}
	
	public boolean isActivatorActive()
	{
		return this.getComponent(CollisionComponent.class).isActive();
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == EngineGlobals.COLLISION_EVENT_COLLISION_ENTER)
		{
			onCollisionEvent((CollisionEvent)event);
		}
	}
	
	/**
	 * Returns the local position of the target of this activator (The bullseye)
	 * @return The target location in its local space.
	 */
	public Vector2 getLocalTargetLocation()
	{
		return new Vector2(-this.getComponent(AnimationComponent.class).getRealWidth() * 0.25f, this.getComponent(AnimationComponent.class).getRealHeight() * 0.7f);
	}
	
	/**
	 * Activates the trap activator. Making it able to be hit & triggered. 
	 * If the trap is not able to be activated at this frame, this activator will wait until it is and then activate itself.
	 */
	public void activateTrap()
	{
		if(isActivatorActive()) { return; }
		_requestedActivateTrap = true;
	}
	
	/**
	 * Deactivates the trap activator. Making it unable to be hit & triggered. 
	 */
	public void deactivateTrap()
	{
		if(!isActivatorActive()) { return;}
		this.getComponent(AnimationComponent.class).setCurrentAnimation("trigger", true);
		this.getComponent(CollisionComponent.class).setActiveState(false);
		_requestedActivateTrap = false;
	}
	
	@Override
	protected void awake() 
	{
		Animations anims = new Animations("activate", Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_TRAP_ACTIVATOR_TURN), false, true);
		anims.setAnimation("trigger", Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_TRAP_ACTIVATOR_TURN), false, false);
		
		CollisionComponent cc = this.addComponent(new CollisionComponent());
		cc.addEventListener(EngineGlobals.COLLISION_EVENT_COLLISION_ENTER, this);
		AnimationComponent ac = this.addComponent(new AnimationComponent(anims, true, false));
		ac.setAnimationSpeed(0.35f);
		ac.setSortingLayer(1);
		ac.setSortOnY(true);
		ac.addEventListener(EngineGlobals.COLLISION_EVENT_COLLISION_ENTER, this);
		ac.setPivot(0.5f, 0, false);
		
		FixtureDef _fixDef = new FixtureDef();
		_fixDef.filter.maskBits = CollisionResources.BIT_ARROW;
		_fixDef.isSensor = true;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(CollisionResources.convertToPPM(25f), CollisionResources.convertToPPM(25f), new Vector2(CollisionResources.convertToPPM(getLocalTargetLocation().x), CollisionResources.convertToPPM(getLocalTargetLocation().y)), 0);
		_fixDef.shape = shape;
		cc.createFixture(_fixDef, CollisionResources.BIT_TRAP_ACTIVATOR);
		cc.setActiveState(false);
	}
	
	@Override
	protected void updated(float dt) 
	{
		if(_requestedActivateTrap)
		{
			if(_linkedTrap == null) {_requestedActivateTrap = false; return; }
			
			if(_linkedTrap.canBeActive())
			{
				this.getComponent(AnimationComponent.class).setCurrentAnimation("activate", true);
				this.getComponent(CollisionComponent.class).setActiveState(true);	
				_linkedTrap.refresh();
			}
			
			_requestedActivateTrap = !isActivatorActive();
		}
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
	}

	@Override
	protected void destroyed() 
	{
		this.getComponent(CollisionComponent.class).removeEventListener(EngineGlobals.COLLISION_EVENT_COLLISION_ENTER, this);
		_linkedTrap = null;
	}

	private void onCollisionEvent(CollisionEvent event) 
	{
		if(event.getOtherCollisionComponent() == null || event.getOtherCollisionComponent().getParentOfComponent() == null) { return; }
		
		if(event.getOtherCollisionComponent().getParentOfComponent().hasTag(Tags.TAG_PROJECTILE))
		{
			Engine.getAudioResources().getSound(GameAudioResources.SOUND_ARROW_HIT_TARGET).play();
			EffectFactory.createHitEffect(this.getTransformComponent().getPositionX() + getLocalTargetLocation().x, this.getTransformComponent().getPositionY() + getLocalTargetLocation().y, 1.3f, Color.WHITE).getAnimationComponent().setSortingLayer(1);
			
			if(_linkedTrap != null)
				triggerTrap();
		}
	}
	
	
	/**
	 * This activates the trap this Activator is linked to
	 */
	private void triggerTrap() 
	{
		_linkedTrap.trigger();
		deactivateTrap();
	}
}
