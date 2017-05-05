package com.mygdx.game.level;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.EventDispatcher;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.events.HealthEvent;

public class Playfield extends EventDispatcher implements IEventReceiver
{
	public static String EVENT_BASE_DESTROYED = "PlayerBaseDestroyedEvent";
	
	public static final int BORDER_TILE_Y_LINE_FROM_END = 3;
	
	private PlayerBase _playerBase;
	private Grid _grid;
	
	public void createLevel()
	{
		_grid = new Grid(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, 6, 15);
		_playerBase = new PlayerBase();
		_playerBase.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
	}
	
	public void destroyLevel()
	{
		_grid.clean();
		_playerBase.destroy();
		
		_grid = null;
		_playerBase.getComponent(HealthComponent.class).removeEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
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

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == HealthComponent.EVENT_HEALTH_DIED)
		{
			onBaseDiedEvent((HealthEvent)event);
		}
	}

	private void onBaseDiedEvent(HealthEvent event) 
	{
		this.dispatchEvent(new Event(EVENT_BASE_DESTROYED));
	}
}
