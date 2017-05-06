package com.mygdx.game.engine.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Contains the Render Components for the game. Including SpriteBatch and Cameras.
 * @author Ramses Di Perna
 */
public class RenderComponents {

	private SpriteBatch _spriteBatch;
	
	private GameCamera _mainCam;
	private GameCamera _hudCam;
	
	private Vector3 _defaultPosMainCam;
	private Vector3 _defaultPosHudCam;
	
	public RenderComponents(SpriteBatch sp, GameCamera mainCam, GameCamera hudCam)
	{
		_spriteBatch = sp;
		_mainCam = mainCam;
		_hudCam = hudCam;
		
		_defaultPosMainCam = new Vector3(_mainCam.position);
		_defaultPosHudCam = new Vector3(_hudCam.position);
	}
	
	/**
	 * Resets all the cams back to their original state and position.
	 */
	public void resetCams()
	{
		_mainCam.position.x = _defaultPosMainCam.x;
		_mainCam.position.y = _defaultPosMainCam.y;
		_mainCam.position.z = _defaultPosMainCam.z;
		
		_hudCam.position.x = _defaultPosHudCam.x;
		_hudCam.position.y = _defaultPosHudCam.y;
		_hudCam.position.z = _defaultPosHudCam.z;
		
		// TODO: Kill all cam effects. (Shake etc)
	}
	
	/**
	 * The camera which is responsible of drawing main assets of the game on the screen.
	 * @return Returns the main camera of the game
	 */
	public GameCamera getMainCamera()
	{
		return _mainCam;
	}
	/**
	 * The camera which is responsible of drawing UI on the screen.
	 * @return Returns the main camera of the game
	 */
	public GameCamera getHudCamera()
	{
		return _hudCam;
	}
	
	/**
	 * The drawing field for the game.
	 * @return Returns the SpriteBatch of the game.
	 */
	public SpriteBatch getSpriteBatch()
	{
		return _spriteBatch;
	}

	public void update(float deltaTime) 
	{
		_mainCam.update(deltaTime);
		_hudCam.update(deltaTime);
	}
}
