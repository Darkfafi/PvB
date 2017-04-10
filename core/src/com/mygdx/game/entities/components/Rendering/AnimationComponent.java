package com.mygdx.game.entities.components.Rendering;

public class AnimationComponent extends RenderComponent 
{	
	private int _newIndex = 0;
	private float _timePassed = 0;
	private float _animationSpeed = 0.5f;
	
	private boolean _isPlaying = true;
	private boolean _isLooping = true;
	
	public void playAnimation()
	{
		this.getRenderInfo().setCurrentFrameInfo(0);
		_isPlaying = true;
	}
	
	public void stopAnimation() 
	{
		_isPlaying = false;	
	}
	
	@Override
	public void awake() 
	{
		// TODO Auto-generated method stub
		super.awake();
	}
	
	@Override
	public void updated(float deltaTime) {
		// TODO Auto-generated method stub
		super.updated(deltaTime);
		
		if(_isPlaying)
		{
			_timePassed += deltaTime;
			if(_timePassed >= deltaTime / _animationSpeed)
			{
				_timePassed -= (deltaTime / _animationSpeed);
				_newIndex = this.getRenderInfo().getCurrentFrameInfo() + 1;
				if(_newIndex >= this.getRenderInfo().getFramesLength())
				{
					if(_isLooping)
					{
						_newIndex = 0;
					}
					else
					{
						stopAnimation();
						return;
					}
				}
				this.getRenderInfo().setCurrentFrameInfo(_newIndex);
			}
		}
	}

	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub
		super.destroyed();
	}

}
