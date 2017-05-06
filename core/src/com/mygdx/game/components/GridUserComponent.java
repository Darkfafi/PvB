package com.mygdx.game.components;

import java.util.ArrayList;
import java.util.Collection;

import com.mygdx.game.engine.entities.components.BaseEntityComponent;
import com.mygdx.game.level.Grid;
import com.mygdx.game.level.GridTile;

public class GridUserComponent extends BaseEntityComponent
{
	public static final int NOT_LOCATED_VALUE = -1337;
	
	private ArrayList<String> _gridOccupyTags = new ArrayList<String>();
	private Grid _grid;
	private int _xSize, _ySize;
	private int _locationX = NOT_LOCATED_VALUE;
	private int _locationY = NOT_LOCATED_VALUE;
	
	
	
	public GridUserComponent(Grid grid, String gridOccupyTag, int xSize, int ySize)
	{
		_grid = grid;
		_xSize = xSize;
		_ySize = ySize;
		addGridOccupyTag(gridOccupyTag);
	}
	
	/**
	 * Creates the grid user component. It needs a grid to be linked to, starter tags to represent its being and a size to know how many tiles to cover when located.
	 * @param grid to link with the grid user
	 * @param gridOccupyTags tags it represents to indicate its id when placed on a tile.
	 * @param xSize how many tiles it should cover in the x axis. If the size x is > 1, then the tiles right to the given tile are also set. 
	 * @param ySize how many tiles it should cover in the y axis. If the size y is > 1, then the tiles below the given tile are also set. 
	 */
	public GridUserComponent(Grid grid, String[] gridOccupyTags, int xSize, int ySize)
	{
		_grid = grid;
		_xSize = xSize;
		_ySize = ySize;
		for(int i = 0; i < gridOccupyTags.length; i++)	
		{
			addGridOccupyTag(gridOccupyTags[i]);
		}
	}
	
	/**
	 * Returns the current tile this user is located on. If it was not placed, this will return null
	 * @return The current tile this user is placed on.
	 */
	public GridTile getCurrentLocatedTile()
	{
		if(!isLocated()){return null;}
		return _grid.getTile(_locationX, _locationY);
	}
	
	public boolean isLocated()
	{
		return _locationX != NOT_LOCATED_VALUE;
	}
	
	public int getLocationX()
	{
		return _locationX;
	}
	
	public int getLocationY()
	{
		return _locationY;
	}
	
	/**
	 * Adds its tags to the tiles it covers and removes them from the one it covered before.
	 * This represents the starting location. 
	 * If the size x is > 1, then the tiles right to the given tile are also set.
	 * If the size y is > 1, then the tiles below the given tile are also set.
	 * @param x of the tile to start the cover process.
	 * @param y of the tile to start the cover process.
	 */
	public void placeSelfOnLocation(int x, int y)
	{
		this.removeSelfFromLocation();
		_locationX = x;
		_locationY = y;
		for(int i = 0; i < _xSize; i++)
		{
			for(int j = 0; j < _ySize; j++)
			{
				for(int h = 0; h < this._gridOccupyTags.size(); h++)
				{
					GridTile t = _grid.getTile(x + i, y + j);
					if(t != null)
						t.addOccupyingTag(_gridOccupyTags.get(h));
				}
			}
		}
	}
	
	/**
	 * Removes its tags from the tile it is currently positioned on.
	 * This method will do nothing if this component has not been positioned yet.
	 */
	public void removeSelfFromLocation()
	{
		if(!isLocated() || _grid == null) { return; }
		for(int i = 0; i < _xSize; i++)
		{
			for(int j = 0; j < _ySize; j++)
			{
				for(int h = 0; h < this._gridOccupyTags.size(); h++)
				{
					GridTile t = _grid.getTile(_locationX + i, _locationY + j);
					if(t != null)
						t.removeOccupyingTag(_gridOccupyTags.get(h));
				}
			}
		}
		_locationX = NOT_LOCATED_VALUE;
		_locationY = NOT_LOCATED_VALUE;
	}
	
	/**
	 * Returns all the grid occupy tags this component represents.
	 * @return All grid occupy tags of component
	 */
	public Collection<String> getGridOccupyTags()
	{
		return _gridOccupyTags;
	}
	
	/**
	 * Adds a grid occupy tag to the component and the tiles it is currently placed on.
	 * @param tag as occupy tag the component represents
	 */
	public void addGridOccupyTag(String tag)
	{
		if(!hasGridOccupyTag(tag))
			_gridOccupyTags.add(tag);
		else
			return;
		
		if(!isLocated()) { return; }
		for(int i = 0; i < _xSize; i++)
		{
			for(int j = 0; j < _ySize; j++)
			{
				GridTile t = _grid.getTile(_locationX + i, _locationY + j);
				if(t != null)
					t.addOccupyingTag(tag);
			}
		}	
	}
	
	/**
	 * Removes a grid occupy tag from the component and the tiles it is currently placed on.
	 * @param tag as occupy tag the component represents
	 */
	public void removeGridOccupyTag(String tag)
	{
		if(hasGridOccupyTag(tag))
			_gridOccupyTags.remove(tag);
		else
			return;
		
		if(!isLocated()) { return; }
		for(int i = 0; i < _xSize; i++)
		{
			for(int j = 0; j < _ySize; j++)
			{
				GridTile t = _grid.getTile(_locationX + i, _locationY + j);
				if(t != null)
					t.removeOccupyingTag(tag);			
			}
		}
	}
	
	/**
	 * Returns whether this component represents the given tag
	 * @param tag to compare as occupy tag
	 * @return true if this component represents the tag, else false
	 */
	public boolean hasGridOccupyTag(String tag)
	{
		return _gridOccupyTags.contains(tag);
	}
	
	/**
	 * Returns the instance of the grid this component is linked to by its constructor
	 * @return The linked grid of the constructor
	 */
	protected Grid getGrid()
	{
		return _grid;
	}
	
	@Override
	protected void destroyed() 
	{
		removeSelfFromLocation();
		_grid = null;
		_gridOccupyTags.clear();
		_gridOccupyTags = null;
	}

	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updated(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void activeStateChanged() {
		// TODO Auto-generated method stub
		
	}
}
