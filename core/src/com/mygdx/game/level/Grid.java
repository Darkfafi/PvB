package com.mygdx.game.level;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This class is used to generate GridTile tiles to form a grid.
 * The class can be cleaned with the 'clean' method
 * @author Ramses Di Perna
 *
 */
public class Grid 
{
	private ShapeRenderer _debugShapeRenderer = new ShapeRenderer();
	private ArrayList<ArrayList<GridTile>> _levelGridTiles = new ArrayList<ArrayList<GridTile>>();
	private int _gridWidth, _gridHeight, _tileAmountX, _tileAmountY;
	
	/**
	 * By calling the constructor the Grid will be generated with the given information.
	 * @param gridWidth is the total width of the grid in pixels
	 * @param gridHeight is the total height of the grid in pixels
	 * @param tileAmountX is the amount of tiles which should be generated in the X axis. The width of the tile will be the width of the grid / tileAmountX
	 * @param tileAmountY is the amount of tiles which should be generated in the Y axis. The height of the tile will be the height of the grid / tileAmountY
	 */
	public Grid(int gridWidth, int gridHeight, int tileAmountX, int tileAmountY)
	{
		_gridWidth = gridWidth;
		_gridHeight = gridHeight;
		_tileAmountX = tileAmountX + 1;
		_tileAmountY = tileAmountY + 1;
		
		createGrid();
	}
	
	/**
	 * Returns all tiles created by this grid.
	 * @return All GridTile objects made by this grid.
	 */
	public ArrayList<ArrayList<GridTile>> getAllTiles()
	{
		return _levelGridTiles;
	}
	
	/**
	 * Returns all tiles created by this grid.
	 * @return All GridTile objects made by this grid.
	 */
	public ArrayList<GridTile> getAllTilesInYRow(int yRow)
	{
		return _levelGridTiles.get(yRow + 1);
	}
	
	/**
	 * Gets the tile with the same x and y index.
	 * @param gridXPos is the index of the tile's x axis
	 * @param gridYPos is the index of the tile's y axis
	 * @return The tile on the given indexes. If the indexes are out of bounds, this will return 'null'
	 */
	public GridTile getTile(int gridXPos, int gridYPos)
	{
		if(_levelGridTiles == null || (gridYPos + 1) >= _levelGridTiles.size()){ return null; }
		ArrayList<GridTile> rows = _levelGridTiles.get(gridYPos + 1);
		if((gridXPos + 1) >= rows.size()) { return null; }
		GridTile tile = rows.get(gridXPos + 1);
		return tile;
	}
	
	/**
	 * Returns the world position of tile location given.
	 * @param gridXPos is the index of the tile's x axis
	 * @param gridYPos is the index of the tile's y axis
	 * @return The world position of the given tile (The left down corner of it)
	 */
	public Vector2 getTileWorldPosition(int gridXPos, int gridYPos)
	{
		float x = gridXPos * getTileWidth();
		float y = getGridHeight() - gridYPos* getTileHeight();
		
		return new Vector2(x, y);
	}
	
	/**
	 * Gets the tile width (The width of the tile will be the width of the grid / tileAmountX)
	 * @return The width of a tile in pixels
	 */
	public float getTileWidth()
	{
		return (float)_gridWidth / ((float)getTileAmountX());
	}
	
	/**
	 * Gets the tile height (The height of the tile will be the height of the grid / tileAmountY)
	 * @return The height of a tile in pixels
	 */
	public float getTileHeight()
	{
		return (float)_gridHeight / ((float)getTileAmountY());
	}
	
	/**
	 * Gets the amount of tiles in the x axis given in the constructor.
	 * @return The amount of tiles in the x axis
	 */
	public int getTileAmountX()
	{
		return _tileAmountX - 1;
	}
	
	/**
	 * Gets the amount of tiles in the y axis given in the constructor
	 * @return The amount of tiles in the y axis
	 */
	public int getTileAmountY()
	{
		return _tileAmountY - 1;
	}
	
	/**
	 * The width of the entire grid given in the constructor
	 * @return The width of the grid.
	 */
	public float getGridWidth()
	{
		return _gridWidth;
	}
	
	/**
	 * The height of the entire grid given in the constructor
	 * @return The height of the grid.
	 */
	public float getGridHeight()
	{
		return _gridHeight;
	}
	
	/**
	 * Renders the grid for debugging purposes on the screen.
	 * @param renderComponents to render the grid with.
	 */
	public void debugRender(RenderComponents renderComponents)
	{
		Color c;
		GridTile t;
		renderComponents.getSpriteBatch().begin();
		for(int yRow = -1; yRow < _tileAmountY; yRow++)
		{
			for(int xRow = -1; xRow < _tileAmountX; xRow++)
			{
				t = this.getTile(xRow, yRow);
				c = ((xRow % 2 == 0 && yRow % 2 != 0)|| (xRow % 2 != 0 && yRow % 2 == 0)) ? new Color(0,0,0.6f,0.8f) : new Color(0,0,0.8f,0.8f);
				if(xRow == 0 && yRow == 0)
					c = Color.RED;
				_debugShapeRenderer.begin(ShapeType.Filled);

				_debugShapeRenderer.flush();
				_debugShapeRenderer.setColor(c);
				_debugShapeRenderer.rect(t.getWorldPositionX(), t.getWorldPositionY(), this.getTileWidth(), this.getTileHeight());
				_debugShapeRenderer.end();
			}
		}
		renderComponents.getSpriteBatch().end();
	}
	
	/**
	 * To clean the class's references. After this method, all references of this class should be nulled and the class should not be used.
	 * This method makes the class unusable for future use.
	 */
	public void clean()
	{
		if(_levelGridTiles != null)
		{
			for(int yRow = -1; yRow < _tileAmountY; yRow++)
			{
				for(int xRow = -1; xRow < _tileAmountX; xRow++)
				{
					_levelGridTiles.get(yRow + 1).get(xRow + 1).clean();
				}
			}
			_levelGridTiles.clear();
		}
		_levelGridTiles = null;
		
		if(_debugShapeRenderer != null)
			_debugShapeRenderer.dispose();
		
		_debugShapeRenderer = null;
	}
	
	/**
	 * Generates the grid.
	 */
	private void createGrid()
	{
		for(int yRow = -1; yRow < _tileAmountY; yRow++)
		{
			_levelGridTiles.add(new ArrayList<GridTile>());
			for(int xRow = -1; xRow < _tileAmountX; xRow++)
			{
				_levelGridTiles.get(yRow + 1).add(new GridTile(xRow, yRow, this));
			}
		}
	}
}
