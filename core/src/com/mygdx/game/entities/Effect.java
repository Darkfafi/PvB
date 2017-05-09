package com.mygdx.game.entities;

import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EngineTween;

public class Effect extends BaseEntity implements IEventReceiver
{
	private float _fadeOutDuration;
	
	public Effect(RenderInfo effectAnimation, boolean ui,  float fadeOutDuration)
	{
		initialize(effectAnimation, fadeOutDuration, ui);
	}
	
	public Effect(RenderInfo effectAnimation, boolean ui)
	{
		initialize(effectAnimation, 0, ui);
	}
	
	public AnimationComponent getAnimationComponent()
	{
		return this.getComponent(AnimationComponent.class);
	}
	
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
				this.getAnimationComponent().doAlpha(0, _fadeOutDuration, true).addEventListener(EngineTween.ENGINE_TWEEN_EVENT, this);
			}
		}
		
		if(event.getType() == EngineTween.ENGINE_TWEEN_EVENT)
		{
			this.destroy();
		}
	}

}
