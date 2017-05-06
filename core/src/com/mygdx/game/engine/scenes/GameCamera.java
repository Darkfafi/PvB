package com.mygdx.game.engine.scenes;

import java.util.Random;
import java.util.Stack;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Camera for the Game. Also has a shake function for an added effect to big moments, like explosions or a train going by.
 * @author Djamali
 *
 */
public class GameCamera extends OrthographicCamera 
{
	private Random _randomFactor = new Random();
	private Stack<ShakeRequest> _shakeRequests = new Stack<ShakeRequest>();
	
	/**
	 * Returns if there is any shake effecting the camera
	 * @return Returns true if any shake is effecting the camera, else false
	 */
	public boolean isShaking()
	{
		return !_shakeRequests.isEmpty();
	}
	
	/**
	 * Puts a shake request on the camera which will effect its current state. (Multiple shakes are possible)
	 * @param shakePower to how much shake effect will take place
	 * @param time is how long the shake must last in seconds.
	 */
	public void doShake(float shakePower, float time)
	{
		_shakeRequests.add(new ShakeRequest(shakePower, time));
	}
	
	private void shake(float delta)
	{
		for(int i = _shakeRequests.size() - 1; i >= 0; i--)
		{
			_shakeRequests.get(i).doEffectOnCamera(this, delta);
			
			if(_shakeRequests.get(i).isOverTime())
			{
				_shakeRequests.get(i).removeLastEffectOnCamera(this);
				_shakeRequests.get(i).clean();
				_shakeRequests.remove(i);
			}
		}
	}
	
	public void update(float dt)
	{
		update();
		if(isShaking())
			shake(dt);
	}
	
	@Override
	public void update(boolean updateFrustum)
	{
		super.update(updateFrustum);
	}
	
	/**
	 * This private class handles a single shake request. This allows for multiple shakes on 1 camera.
	 * @author Ramses Di Perna
	 *
	 */
	private class ShakeRequest
	{
		private Vector2 _shakePos;
		private float _time;
		private float _shakePower;
		private float _timePassed;
		private float _x, _y;
		
		public ShakeRequest(float time, float shakePower)
		{
			_time = time;
			_shakePower = shakePower;
			_shakePos = new Vector2();
			_timePassed = 0;
		}
		
		public boolean isOverTime()
		{
			return _timePassed >= _time;
		}
		
		public void doEffectOnCamera(GameCamera cameraEffecting, float deltaTime)
		{
			float _currentShakePower = _shakePower * ((_time - _timePassed) / _time);
			
			removeLastEffectOnCamera(cameraEffecting);
			
			_x = (_randomFactor.nextFloat() - 0.5f) * 2 * _currentShakePower;
			_y = (_randomFactor.nextFloat() - 0.5f) * 2 * _currentShakePower;
			
			_shakePos.x = _x;
			_shakePos.y = _y;
			
			_timePassed += deltaTime;
			translate(_shakePos.x, _shakePos.y);
		}
		
		public void removeLastEffectOnCamera(GameCamera cameraEffecting)
		{
			cameraEffecting.position.x -= _shakePos.x;
			cameraEffecting.position.y -= _shakePos.y;
		}
		
		public void clean()
		{
			_shakePos = null;
		}
	}
}
