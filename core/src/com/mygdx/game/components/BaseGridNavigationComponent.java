package com.mygdx.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.level.Grid;
import com.mygdx.game.level.GridTile;

/**
 * All which extend this class have full access to the navigation abilities on the grid and its own position.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseGridNavigationComponent extends GridUserComponent 
{
	private ArrayList<String> _unwalkableOccupyTags = new ArrayList<String>();
	
	public BaseGridNavigationComponent(Grid grid, String gridOccupyTag, int xSize, int ySize) 
	{
		super(grid, gridOccupyTag, xSize, ySize);
	}
	
	/**
	 * And a tag this instance can not walk over in its orientation.
	 * @param tag represents the occupytag which it can not navigate over.
	 */
	public void addUnwalkableOccupyTag(String tag)
	{
		if(!hasUnwalkableOccupyTag(tag))
			_unwalkableOccupyTags.add(tag);
	}
	
	/**
	 * Checks whether this instance has the given tag as tag it can not walk over. 
	 * @param tag to check
	 * @return True if it has it as "unwalkable tag", else false
	 */
	public boolean hasUnwalkableOccupyTag(String tag)
	{
		return _unwalkableOccupyTags.contains(tag);
	}
	
	/**
	 * Removes a tag which it has set as unwalkable occupy tag
	 * @param tag to remove from the unwalkable occupy tag list.
	 */
	public void removeUnwalkableOccupyTag(String tag)
	{
		if(hasUnwalkableOccupyTag(tag))
			_unwalkableOccupyTags.remove(tag);
	}
	
	/**
	 * Looks for a new target tile from its current location, here it will keep in account the side stepping, forward stepping and row searching in order to find an open tile.
	 * @return The next tile in grid space
	 */
	protected Vector2 getNewTargetTile(float sideStepChance) 
	{
		boolean sideStepPrefered = (float) (Math.random() * 100f) < sideStepChance;
		Vector2 nextTile = new Vector2(this.getLocationX(), this.getLocationY() + 1);
		Vector2 sideStep = getSideStep();
		if(sideStepPrefered && sideStep != null)
		{
			return sideStep;
		}
		if(canWalkOnTile((int)nextTile.x, (int)nextTile.y))
		{
			return nextTile;
		}
		
		Vector2 availableInNextRow = getWalkableTileOnRow((int)nextTile.y);
		if(availableInNextRow == null) {return null; }
		int xDir = (int)availableInNextRow.x - this.getLocationX();
		xDir = Math.abs(xDir) / xDir;
		if(!canWalkOnTile(this.getLocationX() + xDir, this.getLocationY()))
		{
			return null;
		}
		return new Vector2(this.getLocationX() + xDir, this.getLocationY());
	}
	
	/**
	 * Returns a random side step if able to. It will only return null when both side steps are not able to be done (Side means diagonal downwards)
	 * @return The position of a side stepable tile in grid position
	 */
	protected Vector2 getSideStep()
	{
		Vector2 nextTile = new Vector2(this.getLocationX(), this.getLocationY() + 1);
		boolean leftFirst = (float) (Math.random() * 100f) < 50;
		int dir = leftFirst ? -1 : 1;
		Vector2 tile = new Vector2(nextTile.x + dir, nextTile.y);
		if(canWalkOnTile((int)tile.x, (int)tile.y))
		{
			return tile;
		}
		else
		{
			tile = new Vector2(nextTile.x - dir, nextTile.y);
			if(canWalkOnTile((int)tile.x, (int)tile.y))
				return tile;
		}
		return null;
	}
	
	/**
	 * Gets a world position on the current occupying tile.
	 * @return World position random in bounce of the occupying tile
	 */
	protected Vector2 getTargetPosition()
	{
		if(!this.isLocated()) {return null;}
		
		float amountX = (float) Math.random();
		float amountY = (float) Math.random();
		
		if(amountX < 0.3f) { amountX = 0.3f;}
		else if(amountX > 0.7f) { amountX = 0.7f;}
		if(amountY < 0.4f) { amountY = 0.4f;}
		else if(amountY > 0.8f) { amountY = 0.8f;}
		
		float x = this.getLocationX() * this.getGrid().getTileWidth() + (this.getGrid().getTileWidth() * amountX);
		float y = (getGrid().getGridHeight() - this.getLocationY() * this.getGrid().getTileHeight()) + (this.getGrid().getTileHeight() * amountY);
		return new Vector2(x, y);
	}
	
	/**
	 * Check whether the given tile is walkable for this navigator
	 * @param tileX indicates the x position of the tile on the grid (In Grid position)
	 * @param tileY indicates the y position of the tile on the grid (In Grid position)
	 * @return
	 */
	protected boolean canWalkOnTile(int tileX, int tileY)
	{
		if(tileX < 0 || tileX > this.getGrid().getTileAmountX() - 1) { return false;}
		GridTile tile = this.getGrid().getTile(tileX, tileY);
		if(tile == null) { return false; }
		
		if(hasAnyUnwalkableOccupyTag(tile))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the tile grid location of a walkable tile on the given y row
	 * @param yRow to check for walkable tile
	 * @return The grid location of the tile
	 */
	protected Vector2 getWalkableTileOnRow(int yRow)
	{
		ArrayList<GridTile> tiles = (ArrayList<GridTile>) this.getGrid().getAllTilesInYRow(yRow);
		Vector2 v = null;
		for(int i = 0; i < tiles.size(); i++)
		{
			v = new Vector2(tiles.get(i).getPositionX(), tiles.get(i).getPositionY());
			if(canWalkOnTile((int)v.x,(int)v.y))
				return v;
		}
		return null;
	}
	
	/**
	 * Returns whether this component holds any of the occupy tags the given tile has as 'Unwalkable'
	 * @param tile to check if its unwalkable in form of tags
	 * @return True if it has any of the unwalkable tags of this component, else false (true means unwalkable, false means walkable)
	 */
	protected boolean hasAnyUnwalkableOccupyTag(GridTile tile)
	{
		for(int i = 0; i < _unwalkableOccupyTags.size(); i++)
		{
			if(tile.hasOccupyingTag(_unwalkableOccupyTags.get(i)))
				return true;
		}
		return false;
	}
}
