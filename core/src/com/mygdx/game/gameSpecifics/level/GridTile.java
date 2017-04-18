package com.mygdx.game.gameSpecifics.level;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GridTile 
{
	private int _xPos, _yPos;
	// Idea: string list of things on the tile, "Trap, Enemy, Blockade, DamageArea" etc etc..
	private ShapeRenderer _debugShapeRenderer = new ShapeRenderer();
	private Grid _grid;
	private ArrayList<String> _currentGridOccupyingTags = new ArrayList<String>();
	
	/**
	 * The tile of the grid requires a grid position and the grid itself
	 * @param xPos as x axis index in the grid.
	 * @param yPos as y axis index in the grid
	 * @param grid which built the GridTile.
	 */
	public GridTile(int xPos, int yPos, Grid grid)
	{
		_xPos = xPos;
		_yPos = yPos;
		_grid = grid;
	}
	
	public void addOccupyingTag(String tag)
	{
		_currentGridOccupyingTags.add(tag);
	}
	
	public void removeOccupyingTag(String tag)
	{
		_currentGridOccupyingTags.remove(tag);
	}
	
	public boolean hasOccupyingTag(String tag)
	{
		return _currentGridOccupyingTags.contains(tag);
	}
	
	/**
	 * Returns the x index position on the grid
	 * @return The x index position of this tile on the grid
	 */
	public int getPositionX()
	{
		return _xPos;
	}
	
	/**
	 * Returns the y index position on the grid
	 * @return The y index position of this tile on the grid
	 */
	public int getPositionY()
	{
		return _yPos;
	}
	
	/**
	 * Returns the tile width size in pixels (World space size)
	 * @return The width of this tile in pixels
	 */
	public float getTileWidth()
	{
		return _grid.getTileWidth();
	}
	
	/**
	 * Returns the tile height size in pixels (World space size)
	 * @return The height of this tile in pixels
	 */
	public float getTileHeight()
	{
		return _grid.getTileHeight();
	}
	
	/**
	 * Returns the x position in world space of this tile (Position in the left down corner of the tile)
	 * @return The x position in world space.
	 */
	public float getWorldPositionX()
	{
		return ((float)_xPos * getTileWidth());
	}
	
	/**
	 * Returns the y position in world space of this tile (Position in the left down corner of the tile)
	 * @return The y position in world space.
	 */
	public float getWorldPositionY()
	{
		return _grid.getGridHeight() - ((float)_yPos * getTileHeight()) - this.getTileHeight();
	}
	
	/**
	 * Calls a render to draw the tile on the correct coordinates on the screen.
	 * A SpriteBatch begin, end draw call should be put around this method call.
	 */
	public void debugDraw()
	{
		_debugShapeRenderer.begin(ShapeType.Filled);
		Color c = ((_xPos % 2 == 0 && _yPos % 2 != 0)|| (_xPos % 2 != 0 && _yPos % 2 == 0)) ? new Color(0,0,0.6f,0.8f) : new Color(0,0,0.8f,0.8f);
		if(_xPos == 0 && _yPos == 0)
			c = Color.RED;
		_debugShapeRenderer.setColor(c);
		_debugShapeRenderer.rect(getWorldPositionX(), getWorldPositionY(), this.getTileWidth(), this.getTileHeight());
		_debugShapeRenderer.end();
	}
	
	/**
	 * To clean the class's references. After this method, all references of this class should be nulled and the class should not be used.
	 * This method makes the class unusable for future use.
	 */
	public void clean()
	{
		_debugShapeRenderer = null;
		_grid = null;
		_currentGridOccupyingTags.clear();
		_currentGridOccupyingTags = null;
	}
}
