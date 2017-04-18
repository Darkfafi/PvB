package com.mygdx.game.gameSpecifics.level;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.scenes.RenderComponents;

public class Playfield
{
	private PlayerBase _playerBase;
	private Grid _grid;
	
	public void createLevel()
	{
		_grid = new Grid(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, 6, 20);
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
