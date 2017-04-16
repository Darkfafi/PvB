package com.mygdx.game.gameSpecifics.scenes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.components.Rendering.Animations;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.GlobalDispatcher;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.gameSpecifics.entities.Enemy;
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
		System.out.println("humma");
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
		
		Enemy enemy = new Enemy(new Animations("run", MyGdxGame.getTextureResources().getRenderInfo("light_Bandit_0_Run")), 0.5f);
		enemy.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, 0));
		Enemy enemy2 = new Enemy(new Animations("run", MyGdxGame.getTextureResources().getRenderInfo("light_Bandit_0_Run")), 0.2f);
		enemy2.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, 100));

//		TestBlock block = new TestBlock();
//		block.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, 0.25f));
		
		MyGdxGame.getAudioResources().getMusic("testMusic").play();
		GlobalDispatcher.getInstance().addEventListener(EngineGlobals.GLOBAL_EVENT_ENTITY_DESTROYED, this);
	}

}
