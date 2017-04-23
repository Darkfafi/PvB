package com.mygdx.game.level;

import com.mygdx.engine.scenes.RenderComponents;
import com.mygdx.game.MyGdxGame;

public class Playfield
{
	public static final int BORDER_TILE_Y_LINE_FROM_END = 3;
	
	private PlayerBase _playerBase;
	private Grid _grid;
	
	public void createLevel()
	{
		_grid = new Grid(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, 6, 15);
		_playerBase = new PlayerBase();
	}
	
	public void destroyLevel()
	{
		_grid.clean();
		_playerBase.destroy();
		
		_grid = null;
		_playerBase = null;
	}
	
	public void debugRender(RenderComponents rcs)
	{
		if(_grid != null)
			_grid.debugRender(rcs);
	}
	
	public PlayerBase getPlayerBase()
	{
		return _playerBase;
	}
	
	public Grid getGrid()
	{
		return _grid;
	}
}
