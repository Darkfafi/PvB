package com.mygdx.game.entities;

import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EngineTween;

/**
 * This is an Entity class which plays the given RenderInfo as animation and destroys itself after the animation has ended.
 * @author Ramses Di Perna
 *
 */
public class Effect extends BaseEntity implements IEventReceiver
{
	private float _fadeOutDuration;
	
	/**
	 * Creates an Effect to render on screen and destroy after the fade has ended which will be triggered after the animation end
	 * @param effectAnimation which to play one time as the effect
	 * @param ui , if true, this effect will be played in the UI, else in the game world
	 * @param fadeOutDuration if left at 0, this effect will be destroyed after its end, else it will also fade out and destroy after the fade effect.
	 */
	public Effect(RenderInfo effectAnimation, boolean ui,  float fadeOutDuration)
	{
		initialize(effectAnimation, fadeOutDuration, ui);
	}
	
	/**
	 * Creates an Effect to render on screen and destroy after the animation has ended.
	 * @param effectAnimation which to play one time as the effect
	 * @param ui , if true, this effect will be played in the UI, else in the game world
	 */
	public Effect(RenderInfo effectAnimation, boolean ui)
	{
		initialize(effectAnimation, 0, ui);
	}
	
	/**
	 * Returns the AnimationComponent attached to this entity
	 * @return AnimationComponent attached to this entity
	 */
	public AnimationComponent getAnimationComponent()
	{
		return this.getComponent(AnimationComponent.class);
	}
	
	/**
	 * Initialized the Effect class
	 * @param effectAnimation which to play one time as the effect
	 * @param fadeOutDuration if left at 0, this effect will be destroyed after its end, else it will also fade out and destroy after the fade effect.
	 * @param ui, if true, this effect will be played in the UI, else in the game world
	 */
	private void initialize(RenderInfo effectAnimation, float fadeOutDuration, boolean ui)
	{
		Animations anims = new Animations("effect", effectAnimation, false);
		this.addComponent(new AnimationComponent(anims, true, ui)).addEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
	}
	
	@Override
	protected void awake() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void destroyed() 
	{
		getAnimationComponent().removeEventListener(AnimationComponent.EVENT_ANIMATION_STOPPED, this);
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == AnimationComponent.EVENT_ANIMATION_STOPPED)
		{
			if(_fadeOutDuration <= 0)
			{
				this.destroy();
			}
			else
			{
				this.getAnimationComponent().doAlpha(0, _fadeOutDuration, TweenStartType.GameTime).addEventListener(EngineTween.ENGINE_TWEEN_EVENT, this);
			}
		}
		
		if(event.getType() == EngineTween.ENGINE_TWEEN_EVENT)
		{
			this.destroy();
		}
	}

}
