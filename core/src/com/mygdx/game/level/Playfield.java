package com.mygdx.game.level;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Engine;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.EventDispatcher;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.events.HealthEvent;
import com.mygdx.game.traps.TrapSpawn;
import com.mygdx.game.traps.TrapSpawnInfo;

public class Playfield extends EventDispatcher implements IEventReceiver
{
	public static String EVENT_BASE_DESTROYED = "PlayerBaseDestroyedEvent";
	
	public static final int BORDER_TILE_Y_LINE_FROM_END = 3;
	
	private PlayerBase _playerBase;
	private Grid _grid;
	private ILevelBlueprint _blueprint;
	private ArrayList<TrapSpawn> _trapSpawns = new ArrayList<TrapSpawn>();
	
	public Playfield(ILevelBlueprint blueprint)
	{
		createLevel(blueprint);
	}
	
	public void createLevel(ILevelBlueprint blueprint)
	{
		destroyLevel();
		_blueprint = blueprint;
		_grid = new Grid(Engine.getWidth(), Engine.getHeight(), blueprint.getGridAmountX(), blueprint.getGridAmountY());
		_playerBase = new PlayerBase();
		_playerBase.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		
		_blueprint.getLevelMusic().setVolume(0.3f);
		_blueprint.getLevelMusic().play();
		
		TrapSpawnInfo[] infos = _blueprint.getTrapSpawnInfos();
		
		for(int i = 0; i < infos.length; i++)
		{
			_trapSpawns.add(new TrapSpawn(infos[i], _grid));
			_trapSpawns.get(i).spawnTrap();
		}
	}
	
	public void destroyLevel()
	{
		if(_blueprint == null) { return; }
		_grid.clean();
		_playerBase.destroy();
		
		_grid = null;
		_playerBase.getComponent(HealthComponent.class).removeEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		_playerBase = null;
		_blueprint = null;
	}
	
	public void update(float dt)
	{
		if(_blueprint == null) { return; }
	}
	
	public void render(RenderComponents rcs, boolean debugRender)
	{
		rcs.getSpriteBatch().setProjectionMatrix(rcs.getMainCamera().combined);
	
		if(_blueprint == null) { return; }
		if(debugRender)
		{
			if(_grid != null)
				_grid.debugRender(rcs);
			
			return;
		}
		
		rcs.getSpriteBatch().begin();
		Texture t = _blueprint.getLevelBackground();
		float offsetX = (t.getWidth() - Engine.getWidth()) / 2;
		float offsetY = (t.getHeight() - Engine.getHeight()) / 2;
		if(offsetX <= 0) { offsetX = 0;}
		if(offsetY <= 0) { offsetY = 0;}
		rcs.getSpriteBatch().draw(t, -offsetX, -offsetY, t.getWidth(), t.getHeight());
		rcs.getSpriteBatch().end();
	}
	
	public ILevelBlueprint getLevelBlueprint()
	{
		return _blueprint;
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
