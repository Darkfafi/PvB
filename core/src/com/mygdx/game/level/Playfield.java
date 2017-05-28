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

/**
 * This class represents the entire Playfield which the game is played on.
 * This class creates and holds the grid, the PlayerBase and the level created with the given ILevelBlueprint.
 * This class with also dispatch an even when the base has been destroyed.
 * @author Ramses Di Perna
 *
 */
public class Playfield extends EventDispatcher implements IEventReceiver
{
	public static String EVENT_BASE_DESTROYED = "PlayerBaseDestroyedEvent";
	
	private PlayerBase _playerBase;
	private Grid _grid;
	private ILevelBlueprint _blueprint;
	private ArrayList<TrapSpawn> _trapSpawns = new ArrayList<TrapSpawn>();
	
	/**
	 * Creates a level with the given blueprint.
	 * @param blueprint to create a level with
	 */
	public Playfield(ILevelBlueprint blueprint)
	{
		createLevel(blueprint);
	}
	
	/**
	 * Creates a level with the given blueprint.
	 * @param blueprint to create a level with
	 */
	public void createLevel(ILevelBlueprint blueprint)
	{
		destroyLevel();
		_blueprint = blueprint;
		_grid = new Grid(Engine.getWidth(), Engine.getHeight(), blueprint.getGridAmountX(), blueprint.getGridAmountY());
		_playerBase = new PlayerBase();
		_playerBase.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		TrapSpawnInfo[] infos = _blueprint.getTrapSpawnInfos();
		
		for(int i = 0; i < infos.length; i++)
		{
			_trapSpawns.add(new TrapSpawn(infos[i], _grid));
			_trapSpawns.get(i).spawnTrap(infos[i].getActivatorPosition());
		}
	}
	
	/**
	 * Destroys the level which was created with the ILevelBlueprint using the 'createLevel' method
	 * Including the grid and playerbase
	 */
	public void destroyLevel()
	{
		if(_blueprint == null) { return; }
		_grid.clean();
		_playerBase.destroy();
		
		for(int i = _trapSpawns.size() - 1; i >= 0; i--)
		{
			_trapSpawns.get(i).destroy();
		}
		_trapSpawns.clear();
		_trapSpawns = null;
		
		_grid = null;
		_playerBase.getComponent(HealthComponent.class).removeEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		_playerBase = null;
		_blueprint = null;
	}
	
	/**
	 * Counts a tick for all the traps in order for them to reset on their own pace.
	 */
	public void countForResetTraps()
	{
		for(int i = _trapSpawns.size() - 1; i >= 0; i--)
		{
			_trapSpawns.get(i).countForResetTrap();
		}
	}
	
	/**
	 * Forces all traps in the level to reset.
	 */
	public void forceResetTraps()
	{
		for(int i = _trapSpawns.size() - 1; i >= 0; i--)
		{
			_trapSpawns.get(i).forceResetTrap();
		}
	}
	
	public void forceDisableTraps()
	{
		for(int i = _trapSpawns.size() - 1; i >= 0; i--)
		{
			_trapSpawns.get(i).getTrapActivator().deactivateTrap();
		}
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
