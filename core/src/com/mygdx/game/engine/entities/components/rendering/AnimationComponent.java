package com.mygdx.game.engine.entities.components.rendering;

import com.mygdx.game.engine.Engine;

/**
 * This component is an extension on the RenderComponent and allows for animations to be played.
 * Note: When this component is on an entity, no RenderComponent is needed.
 * @author Ramses Di Perna
 *
 */
public class AnimationComponent extends RenderComponent 
{	
	public static final String EVENT_ANIMATION_STARTED = "AnimationStartedEvent";
	public static final String EVENT_ANIMATION_LOOPED = "AnimationLoopedEvent";
	public static final String EVENT_ANIMATION_STOPPED = "AnimationStoppedEvent";
	
	// Trackers
	private float _timePassed = 0;
	private boolean _isRunning = false;
	private boolean _isPaused = false;
	private Animations _animations = null;
	private String _currentAnimation = "";
	
	// Options
	private float _animationSpeed = 0.5f; // At 60 fps this will be 30 fps
	private boolean _isLooping = true;
	private boolean _isBackwards = false;
	
	public AnimationComponent(Animations animations, boolean playOnCreation, boolean isUI) 
	{
		super(animations.getDefaultAnimation(), isUI);
		_animations = animations;
		setCurrentAnimation(_animations.getDefaultAnimationName(), playOnCreation);
	}
	
	/**
	 * Returns the animation speed in comparison to the frame rate (DEFAULT: 0.5 (30 fps on 60 fps game))
	 * @return Animation speed in normalized value
	 */
	public float getAnimationSpeed()
	{
		return _animationSpeed;
	}
	
	/**
	 * Sets the animation speed in comparison to the frame rate (DEFAULT: 0.5 (30 fps on 60 fps game))
	 * @param animationSpeed normalized value to set the new animation speed on
	 */
	public void setAnimationSpeed(float animationSpeed)
	{
		_animationSpeed = animationSpeed;
	}
	
	/**
	 * Returns the name of the current animation
	 * @return The name of the current animation
	 */
	public String getCurrentAnimation()
	{
		return _currentAnimation;
	}
	
	/**
	 * Sets the current animation to the animation with the given name. 
	 * The animations are defined in the Animations class given in the constructor.
	 * @param animationName of the animation which should be set as current animation
	 * @param playOnSet if 'true', the animation will play after it is set, else it will not
	 * @param canBeSameAnimation indicates whether the animation can be the same. If not and the animation given is already the current animation, this method will not be executed, else it will always.
	 */
	public void setCurrentAnimation(String animationName, boolean playOnSet, boolean canBeSameAnimation)
	{
		if(animationName == getCurrentAnimation() && !canBeSameAnimation) { return; }
		_isRunning = false;
		this.setRenderInfo(_animations.getAnimation(animationName));
		
		_currentAnimation = animationName;
		_isLooping = _animations.isLoopAnimation(animationName);
		_isBackwards = _animations.isBackwardsAnimation(animationName);
		
		this.reset();
		
		if(playOnSet)
			play();
	}
	
	/**
	 * Sets the current animation to the animation with the given name. 
	 * The animations are defined in the Animations class given in the constructor.
	 * This wont play the animation if its already playing
	 * @param animationName of the animation which should be set as current animation
	 * @param playOnSet if 'true', the animation will play after it is set, else it will not
	 */
	public void setCurrentAnimation(String animationName, boolean playOnSet)
	{
		setCurrentAnimation(animationName, playOnSet, false);
	}
	
	/**
	 * Gets the current animation frame.
	 * @return The current frame index displaying
	 */
	public int getCurrentFrame()
	{
		return this.getCurrentFrameInfo();
	}
	
	/**
	 * Gets the length of the currently set animation
	 * @return The length of the FrameInfo array in the RenderInfo.
	 */
	public int getAnimationLength()
	{
		return this.getRenderInfo().getFramesLength();
	}
	
	/**
	 * Gets the normalized time of the animation. 
	 * (Current Frame is 5 and total frames is 10 then it returns 0.5)
	 * @return Percentage played of current animation
	 */
	public float getNormalizedTime()
	{
		return (float)getCurrentFrame() / (float)getAnimationLength();
	}
	
	/**
	 * Returns whether the animation is running and not paused.
	 * @return If the animation is playing. True if it is else false
	 */
	public boolean isPlaying()
	{
		return _isRunning && !_isPaused;
	}
	
	/**
	 * Pauses the current animation
	 */
	public void pause()
	{
		_isPaused = true;
	}
	
	/**
	 * UnPauses the current animation
	 */
	public void unpause()
	{
		_isPaused = false;
	}
	
	/**
	 * Returns whether the animation is paused or not.
	 * @return If the animation is paused then it returns true else it returns false.
	 */
	public boolean isPaused()
	{
		return _isPaused;
	}
	
	/**
	 * Starts the animation from the start.
	 */
	public void play()
	{
		reset();
		_isRunning = true;
		this.dispatchEvent(new AnimationEvent(EVENT_ANIMATION_STARTED, _currentAnimation));
	}
	
	/**
	 * Stops the animation at the current frame. Can only be undone with the 'play' method
	 */
	public void stop() 
	{
		_isRunning = false;	
		this.dispatchEvent(new AnimationEvent(EVENT_ANIMATION_STOPPED, _currentAnimation));
	}
	
	/**
	 * Stops and resets the animation. Can only be undone with the 'play' method
	 */
	public void stopAndReset() 
	{
		stop();
		reset();
	}
	
	/**
	 * Resets the current animation to the first frame.
	 */
	public void reset()
	{
		if(!_isBackwards)
			this.setCurrentFrameInfo(0);
		else
			this.setCurrentFrameInfo(getAnimationLength() - 1);
	}
	
	@Override
	public void awake() 
	{
		super.awake();
	}
	
	@Override
	public void update(float deltaTime) 
	{
		super.update(deltaTime);
		if(isPlaying())
		{
			_timePassed += deltaTime * _animationSpeed;
			if(_timePassed >= Engine.getFrameStep())
			{
				int newIndex;
				_timePassed -=  Engine.getFrameStep();
				
				if(!_isBackwards)
					newIndex = this.getCurrentFrameInfo() + 1;
				else
					newIndex = this.getCurrentFrameInfo() - 1;
				
				if(isEndFrame(newIndex))
				{
					if(_isLooping)
					{
						reset();
						this.dispatchEvent(new AnimationEvent(EVENT_ANIMATION_LOOPED, _currentAnimation));
						return;
					}
					else
					{
						stop();
						return;
					}
				}
				this.setCurrentFrameInfo(newIndex);
			}
		}
	}

	@Override
	protected void destroyed() 
	{
		super.destroyed();
		_animations.clean();
		_animations = null;
		_currentAnimation = null;
	}
	
	@Override
	protected void activeStateChanged()
	{
		super.activeStateChanged();
		if(this.isActive())
			this.unpause();
		else
			this.pause();
	}
	
	private boolean isEndFrame(int indexFrame)
	{
		if(!_isBackwards)
			return indexFrame >= this.getRenderInfo().getFramesLength();
		
		return indexFrame < 0;
	}
}
