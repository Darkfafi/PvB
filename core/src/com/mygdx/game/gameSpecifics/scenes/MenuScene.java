package com.mygdx.game.gameSpecifics.scenes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.gameSpecifics.entities.Enemy;
import com.mygdx.game.gameSpecifics.factories.EnemyFactory;
import com.mygdx.game.resources.PhysicsWorld;
import com.mygdx.game.scenes.BaseScene;

/**
 * This scene is the main menu scene. 
 * This will spawn and set the world for the menu related classes.
 * @author Ramses Di Perna
 */
public class MenuScene extends BaseScene
{
	//private PhysicsWorld _physicsWorld;
	private float t = 0;
	
	@Override
	public void destroyed() {
		// TODO Auto-generated method stub
		MyGdxGame.getAudioResources().stopAllMusic();
		MyGdxGame.getAudioResources().stopAllSounds();
		//_physicsWorld.clean();
		//_physicsWorld = null;
	}

	@Override
	public void update(float dt) {
		//_physicsWorld.update();
		t += dt;
		if(t > 2f)
		{
			this.getScenesManager().setScene(1);
		}
	}

	@Override
	public void render() {
		//_physicsWorld.render(this.getRenderComponents());
	}

	@Override
	protected void created() {
		
		//_physicsWorld = new PhysicsWorld();
		
		//Enemy e = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.LightBandit);
		//e.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, 100));
	}

}
