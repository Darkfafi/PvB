package com.mygdx.game.gameSpecifics.components;

import java.util.ArrayList;
import java.util.Collection;

import com.mygdx.game.entities.BaseEntityComponent;
import com.mygdx.game.gameSpecifics.level.Grid;

public class GridUserComponent extends BaseEntityComponent
{
	private ArrayList<String> _gridOccupyTags = new ArrayList<String>();
	private Grid _grid;
	private int _xSize, _ySize;
	private int _locationX = -1;
	private int _locationY = -1;
	
	public GridUserComponent(Grid grid, String gridOccupyTag, int xSize, int ySize)
	{
		_grid = grid;
		_xSize = xSize;
		_ySize = ySize;
		addGridOccupyTag(gridOccupyTag);
	}
	
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
	
	public void placeSelfOnLocation(int x, int y)
	{
		for(int i = 0; i < _xSize; i++)
		{
			for(int j = 0; j < _ySize; j++)
			{
				for(int h = 0; h < this._gridOccupyTags.size(); h++)
				{
					_grid.getTile(x + i, y + j).addOccupyingTag(_gridOccupyTags.get(h));
				}
			}
		}
	}
	
	public void removeSelfFromLocation()
	{
		if(_locationX == -1) { return; }
		for(int i = 0; i < _xSize; i++)
		{
			for(int j = 0; j < _ySize; j++)
			{
				for(int h = 0; h < this._gridOccupyTags.size(); h++)
				{
					_grid.getTile(_locationX + i, _locationY + j).removeOccupyingTag(_gridOccupyTags.get(h));
				}
			}
		}
		_locationX = -1;
		_locationY = -1;
	}
	
	public Collection<String> getGridOccupyTags()
	{
		return _gridOccupyTags;
	}
	
	public void addGridOccupyTag(String tag)
	{
		if(!hasGridOccupyTag(tag))
			_gridOccupyTags.add(tag);
		else
			return;
		
		if(_locationX == -1) { return; }
		for(int i = 0; i < _xSize; i++)
		{
			for(int j = 0; j < _ySize; j++)
			{
				_grid.getTile(_locationX + i, _locationY + j).addOccupyingTag(tag);				
			}
		}	
	}
	
	public void removeGridOccupyTag(String tag)
	{
		if(hasGridOccupyTag(tag))
			_gridOccupyTags.remove(tag);
		else
			return;
		
		if(_locationX == -1) { return; }
		for(int i = 0; i < _xSize; i++)
		{
			for(int j = 0; j < _ySize; j++)
			{
				_grid.getTile(_locationX + i, _locationY + j).removeOccupyingTag(tag);				
			}
		}
	}
	
	public boolean hasGridOccupyTag(String tag)
	{
		return _gridOccupyTags.contains(tag);
	}
	
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
	public void updated(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
}
