package com.mygdx.engine.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Contains the Render Components for the game. Including SpriteBatch and Cameras.
 * @author Ramses Di Perna
 */
public class RenderComponents {

	private SpriteBatch _spriteBatch;
	
	private GameCamera _mainCam;
	private GameCamera _hudCam;
	
	public RenderComponents(SpriteBatch sp, GameCamera mainCam, GameCamera hudCam)
	{
		_spriteBatch = sp;
		_mainCam = mainCam;
		_hudCam = hudCam;
	}
	
	/**
	 * The camera which is responsible of drawing main assets of the game on the screen.
	 * @return Returns the main camera of the game
	 */
	public OrthographicCamera getMainCamera()
	{
		return _mainCam;
	}
	/**
	 * The camera which is responsible of drawing UI on the screen.
	 * @return Returns the main camera of the game
	 */
	public OrthographicCamera getHudCamera()
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
		_mainCam.update();
		_hudCam.update();
	}
}
