package com.mygdx.game.entities.components.Rendering;

public class AnimationComponent extends RenderComponent 
{	
	private int _newIndex = 0;
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub
		super.awake();
	}

	@Override
	public void updated(float deltaTime) {
		// TODO Auto-generated method stub
		super.updated(deltaTime);
		
		_newIndex = this.getRenderInfo().getCurrentFrameInfo() + 1;
		if(_newIndex >= this.getRenderInfo().getFramesLength())
		{
			_newIndex = 0;
		}
		this.getRenderInfo().setCurrentFrameInfo(_newIndex);
	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		super.destroyed();
	}

}
