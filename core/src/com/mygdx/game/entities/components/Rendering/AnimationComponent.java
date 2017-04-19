package com.mygdx.game.entities.components.Rendering;

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
	private int _newIndex = 0;
	private float _timePassed = 0;
	private boolean _isRunning = false;
	private boolean _isPaused = false;
	private Animations _animations = null;
	private String _currentAnimation = "";
	
	// Options
	private float _animationSpeed = 0.5f; // At 60 fps this will be 30 fps
	private boolean _isLooping = true;
	
	public AnimationComponent(Animations animations, boolean playOnCreation, boolean isUI) 
	{
		super(animations.getDefaultAnimation(), isUI);
		_animations = animations;
		setCurrentAnimation(_animations.getDefaultAnimationName(), playOnCreation);
	}
	
	/**
	 * Sets the current animation to the animation with the given name. 
	 * The animations are defined in the Animations class given in the constructor.
	 * @param animationName of the animation which should be set as current animation
	 * @param playOnSet if 'true', the animation will play after it is set, else it will not
	 */
	public void setCurrentAnimation(String animationName, boolean playOnSet)
	{
		stop();
		this.setRenderInfo(_animations.getAnimation(animationName));
		_currentAnimation = animationName;
		_isLooping = _animations.getLoopAnimation(animationName);
		if(playOnSet)
			play();
	}
	
	/**
	 * Gets the current animation frame.
	 * @return The current frame index displaying
	 */
	public int getCurrentFrame()
	{
		return this.getRenderInfo().getCurrentFrameInfo();
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
		this.getRenderInfo().setCurrentFrameInfo(0);
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
		
		if(isPlaying())
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
						this.getRenderInfo().setCurrentFrameInfo(0);
						this.dispatchEvent(new AnimationEvent(EVENT_ANIMATION_LOOPED, _currentAnimation));
						return;
					}
					else
					{
						stop();
						return;
					}
				}
				this.getRenderInfo().setCurrentFrameInfo(_newIndex);
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
}
