package com.mygdx.game.gameSpecifics.level;

import java.util.ArrayList;

import com.mygdx.game.scenes.RenderComponents;

/**
 * This class is used to generate GridTile tiles to form a grid.
 * The class can be cleaned with the 'clean' method
 * @author Ramses Di Perna
 *
 */
public class Grid 
{
	private ArrayList<GridTile> _levelGridTiles = new ArrayList<GridTile>();
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
		_tileAmountX = tileAmountX;
		_tileAmountY = tileAmountY;
		
		createGrid();
	}
	
	/**
	 * Gets the tile with the same x and y index.
	 * @param gridXPos is the index of the tile's x axis
	 * @param gridYPos is the index of the tile's y axis
	 * @return The tile on the given indexes. If the indexes are out of bounds, this will return 'null'
	 */
	public GridTile getTile(int gridXPos, int gridYPos)
	{
		GridTile tile;
		for(int i = 0; i < _levelGridTiles.size(); i++)
		{
			tile = _levelGridTiles.get(i);
			if(tile.getPositionX() == gridXPos && tile.getPositionY() == gridYPos)
				return tile;
		}
		return null;
	}
	
	/**
	 * Gets the tile width (The width of the tile will be the width of the grid / tileAmountX)
	 * @return The width of a tile in pixels
	 */
	public float getTileWidth()
	{
		return (float)_gridWidth / (float)_tileAmountX;
	}
	
	/**
	 * Gets the tile height (The height of the tile will be the height of the grid / tileAmountY)
	 * @return The height of a tile in pixels
	 */
	public float getTileHeight()
	{
		return (float)_gridHeight / (float)_tileAmountY;
	}
	
	/**
	 * Gets the amount of tiles in the x axis given in the constructor.
	 * @return The amount of tiles in the x axis
	 */
	public int getTileAmountX()
	{
		return _tileAmountX;
	}
	
	/**
	 * Gets the amount of tiles in the y axis given in the constructor
	 * @return The amount of tiles in the y axis
	 */
	public int getTileAmountY()
	{
		return _tileAmountY;
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
		renderComponents.getSpriteBatch().begin();
		for(int i = 0; i < _levelGridTiles.size(); i++)
		{
			_levelGridTiles.get(i).debugDraw();
		}
		renderComponents.getSpriteBatch().end();
	}
	
	/**
	 * To clean the class's references. After this method, all references of this class should be nulled and the class should not be used.
	 * This method makes the class unusable for future use.
	 */
	public void clean()
	{
		for(int i = 0; i < _levelGridTiles.size(); i++)
		{
			_levelGridTiles.get(i).clean();
		}
		
		_levelGridTiles.clear();
		_levelGridTiles = null;
	}
	
	/**
	 * Generates the grid.
	 */
	private void createGrid()
	{
		for(int yRow = 0; yRow < _tileAmountY; yRow++)
		{
			for(int xRow = 0; xRow < _tileAmountX; xRow++)
			{
				_levelGridTiles.add(new GridTile(xRow, yRow, this));
			}
		}
	}
}
