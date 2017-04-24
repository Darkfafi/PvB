package com.mygdx.game.scenes;

import com.mygdx.engine.scenes.BaseScene;
import com.mygdx.game.MyGdxGame;

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
		//e.getTransformComponent().setRotation(360);
		//e.getTransformComponent().doScale(2, 2, 1).ease(TweenType.BounceOut.getTween());
		//e.getComponent(AnimationComponent.class).doColor(new Color(Color.RED), 2);
	}

}
