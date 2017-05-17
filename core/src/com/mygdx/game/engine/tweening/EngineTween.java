package com.mygdx.game.engine.tweening;

import com.mygdx.game.engine.events.EventDispatcher;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;

/**
 * This class contains information on the Tween which is triggered by the 'EngineTweener' including the tween itself. 
 * You can add an eventlistener to this class to listen to the set events which are triggered by the event.
 * You can also add your own callback by calling the 'setCallbackMethod' method
 * @author Ramses Di Perna
 *
 */
public class EngineTween extends EventDispatcher
{
	public static final String ENGINE_TWEEN_EVENT = "EngineTweenEvent";
	
	private Tween _tween;
	private IEngineTweenMethod _method;
	
	
	/**
	 * Sets the EngineTween and sets a callback listener to it so it can listen to the different events.
	 * WARNING: Do not add your own callback to the Tween. If you need one, you can listen to the ENGINE_TWEEN_EVENT or add a callback with the 'setCallbackMethod'
	 * @param tween which this EngineTween represents
	 */
	public EngineTween(Tween tween)
	{
		_tween = tween;
		
		_tween.setCallback(new TweenCallback()
		{			
			@Override
			public void onEvent(int type, BaseTween<?> source) 
			{
				sendTweenEvent(type);
			}
		});
	}
	
	/**
	 * Returns whether the class has been cleaned or not.
	 * @return true when it has been cleaned, else false
	 */
	public boolean isCleaned()
	{
		return _tween == null;
	}
	
	/**
	 * Sets a method which will be triggered on the different callback events which are set in the tween itself.
	 * @param method container to trigger when an event occures.
	 * @return This EngineTween
	 */
	public EngineTween setCallbackMethod(IEngineTweenMethod method)
	{
		_method = method;
		return this;
	}
	
	/**
	 * Sets an ease to the Tween with the given TweenType
	 * @param tweenType represents the Ease type which will set the motion of the tween.
	 * @return This EngineTween
	 */
	public EngineTween ease(EaseType tweenType)
	{
		return ease(tweenType.getTweenEquation());
	}
	
	/**
	 * Sets an ease to the Tween with the given TweenEquation
	 * @param tweenEquation represents the Ease type which will set the motion of the tween.
	 * @return This EngineTween
	 */
	public EngineTween ease(TweenEquation tweenEquation)
	{
		_tween.ease(tweenEquation);
		return this;
	}
	
	/**
	 * This tween will wait the amount of seconds given as value until it starts.
	 * @param delay in seconds to wait before starting tween.
	 * @return This EngineTween
	 */
	public EngineTween delay(float delay)
	{
		_tween.delay(delay);
		return this;
	}
	
	/**
	 * Returns the Tween Type of the Accessor.
	 * For Example the POSITION type of the 'TransformAccessor'
	 * @return The type of the tween accessor linked to the tween
	 */
	public int getTweenType()
	{
		return _tween.getType();
	}
	
	/**
	 * Returns the Tween which this EngineTween is linked to in its constructor.
	 * @return The tween linked in the constructor.
	 */
	public Tween getTween()
	{
		return _tween;
	}
	
	/**
	 * Cleans the class and makes it ready for garbage collection.
	 */
	@Override
	public void clean()
	{
		super.clean();
		if(!_tween.isFinished())
			_tween.kill();
		
		_tween = null;
		_method = null;
	}
	
	/**
	 * Triggeres the tweening method and event listeners which where given / linked to this EngineTween.
	 * @param type of event occurred. This list is given in the 'TweenCallback' class. Default 'COMPLETED' type
	 */
	protected void sendTweenEvent(int type) 
	{
		dispatchEvent(new TweenEvent(ENGINE_TWEEN_EVENT, type, this));

		if(_method != null)
			_method.onMethod(type, this);
	}
}
