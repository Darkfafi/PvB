package com.mygdx.game.scenes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.resources.PhysicsWorld;
import com.mygdx.game.engine.scenes.BaseScene;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.TransformAccessor;
import com.mygdx.game.engine.tweening.TweenEvent;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.factories.EnemyFactory;

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
		MyGdxGame.getAudioResources().stopAllMusic();
		MyGdxGame.getAudioResources().stopAllSounds();
		_physicsWorld.clean();
		_physicsWorld = null;
	}

	@Override
	public void update(float dt) {
		_physicsWorld.update(dt);
	}

	@Override
	public void render() {
		//_physicsWorld.render(this.getRenderComponents());
	}

	@Override
	protected void created() {
		
		_physicsWorld = new PhysicsWorld();
		
		Enemy e = EnemyFactory.createEnemyOfType(EnemyFactory.EnemyType.LightBandit);
		e.getTransformComponent().setPosition(new Vector2(MyGdxGame.WIDTH / 2, 100));
		e.getTransformComponent().setRotation(360);
		e.getTransformComponent().doPosition(100, 800, 2).ease(EaseType.BounceOut).addEventListener(EngineTween.ENGINE_TWEEN_EVENT, this);
		//e.getComponent(AnimationComponent.class).doColor(new Color(Color.RED), 2);
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == EngineTween.ENGINE_TWEEN_EVENT)	
		{
			onTweenEvent((TweenEvent)event);
		}
	}

	private void onTweenEvent(TweenEvent event) 
	{
		if(event.getEngineTween().getTweenType() == TransformAccessor.POSITION)
		{
			//this.getScenesManager().setScene(1);
		}
	}

}
