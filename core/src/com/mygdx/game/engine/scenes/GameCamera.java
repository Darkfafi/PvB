package com.mygdx.game.engine.scenes;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Camera for the Game. Also has a shake function for an added effect to big moments, like explosions or a train going by.
 * @author Djamali
 *
 */
public class GameCamera extends OrthographicCamera 
{
	private float _shakePower;
	private float _currentShakePower = 0;
	private float _time;
	private float _currentTime = 0;
	private boolean _isShaking = true;
	private float x, y;
	private Vector2 _shakePos = new Vector2();
	private Random _randomFactor = new Random();
	
	public boolean isShaking()
	{
		return _isShaking;
	}
	
	/**
	 * 
	 * @param shakePower
	 * @param time
	 * @param delta
	 */
	public void doShake(float shakePower, float time, float delta)
	{
		this._shakePower = shakePower;
		this._time = time;
		
		_isShaking = true;
		shake(delta);
	}
	
	private void shake(float delta)
	{
		if(_currentTime <= _time)
		{
			_currentShakePower = _shakePower * ((_time - _currentTime) / _time);
			
			this.position.x -= _shakePos.x;
			this.position.y -= _shakePos.y;
			
			x = (_randomFactor.nextFloat() - 0.5f) * 2 * _currentShakePower;
			y = (_randomFactor.nextFloat() - 0.5f) * 2 * _currentShakePower;
			
			_shakePos.x = x;
			_shakePos.y = y;
			
			_currentTime += delta;
			translate(_shakePos.x, _shakePos.y);
		}
		else
		{
			_currentTime = 0;
			_isShaking = false;
			this.position.x -= _shakePos.x;
			this.position.y -= _shakePos.y;
		}
	}
	
	public void update(float dt)
	{
		update();	
		doShake(10f, 2f, dt);
	}
	
	@Override
	public void update(boolean updateFrustum)
	{
		super.update(updateFrustum);
	}
}
