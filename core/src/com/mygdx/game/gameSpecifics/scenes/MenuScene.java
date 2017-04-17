package com.mygdx.game.gameSpecifics.scenes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.GlobalDispatcher;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.gameSpecifics.entities.Enemy;
import com.mygdx.game.gameSpecifics.factories.EnemyFactory;
import com.mygdx.game.gameSpecifics.level.Grid;
import com.mygdx.game.globals.EngineGlobals;
import com.mygdx.game.resources.PhysicsWorld;
import com.mygdx.game.scenes.BaseScene;

/**
 * This scene is the main menu scene. 
 * This will spawn and set the world for the menu related classes.
 * @author Ramses Di Perna
 */
public class MenuScene extends BaseScene implements IEventReceiver
{
	private PhysicsWorld _physicsWorld;
	private Grid _levelGrid;
	@Override
	public void destroyed() {
		// TODO Auto-generated method stub
		GlobalDispatcher.getInstance().removeEventListener(EngineGlobals.GLOBAL_EVENT_ENTITY_DESTROYED, this);
		MyGdxGame.getAudioResources().stopAllMusic();
		MyGdxGame.getAudioResources().stopAllSounds();
	}

	@Override
	public void update(float dt) {
		_physicsWorld.update();
	}

	@Override
	public void render() {
		_physicsWorld.render(this.getRenderComponents());
		_levelGrid.debugRender(getRenderComponents());
	}

	@Override
	public void onReceiveEvent(Event event) {
		// TODO Auto-generated method stub
		if(event.getType() == EngineGlobals.GLOBAL_EVENT_ENTITY_DESTROYED)
		{
			this.getScenesManager().setScene(GameScenesManager.GAME_SCENE);
		}
	}

	@Override
	protected void created() {
		
		_physicsWorld = new PhysicsWorld();
		
		Enemy e = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.LightBandit);
		e.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, 100));
		
		MyGdxGame.getAudioResources().getMusic("testMusic").play();
		GlobalDispatcher.getInstance().addEventListener(EngineGlobals.GLOBAL_EVENT_ENTITY_DESTROYED, this);
		_levelGrid = new Grid(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, 6, 15);
		
		
	}

}
