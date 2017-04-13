package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.entities.components.Rendering.RenderInfo;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.globals.InputGlobals;
import com.mygdx.game.touchinput.TouchEvent;

public class BowWeapon extends BaseEntity implements IEventReceiver
{
	public enum BowStage
	{
		Idle,		// Waiting for target location to fire at
		Draw  		// Preparing and Doing the Shoot method
	}
	
	public static final float MAX_DRAW_LENGTH = 250f;
	
	private BowStage _currentBowStage = BowStage.Idle; 	// The current stage of the bow
	private float _drawStrength = 0f;					// The draw strength to release the arrow with
	private Vector2 _targetLocation = null; 			// The target location to fire at / where the finger started
	private int _pointerControllingTouch = -1;
	
	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == InputGlobals.TOUCH_EVENT)
		{
			onTouchEvent((TouchEvent)event);
		}
	}

	@Override
	protected void awake() 
	{
		// TODO Auto-generated method stub
		RenderInfo ri = MyGdxGame.getTextureResources().getRenderInfo("fakeBow");
		this.addComponent(new AnimationComponent(ri, false, false));
		this.getTransformComponent().setScale(new Vector2(1.1f, 0.3f));
		MyGdxGame.getInputHandler().addEventListener(InputGlobals.TOUCH_EVENT, this);
	}

	@Override
	protected void updated(float dt) 
	{
		if(_currentBowStage == BowStage.Draw)
		{
			getTransformComponent().lookAt(_targetLocation, 0.2f);
		}
	}
	
	@Override
	protected void destroyed() 
	{
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
	}
	
	private void onTouchEvent(TouchEvent event) 
	{
		if(_currentBowStage == BowStage.Idle && event.getTouchType() == TouchEvent.TouchType.Started)
		{
			selectTarget(event.getTouchX(), event.getTouchY());
			_pointerControllingTouch = event.getPointer();
		}
		else if(_currentBowStage == BowStage.Draw && event.getPointer() == _pointerControllingTouch)
		{
			if(event.getTouchType() == TouchEvent.TouchType.Dragged)
			{
				drawMechanic(event.getTouchX(), event.getTouchY());
			}
			else if(event.getTouchType() == TouchEvent.TouchType.Ended)
			{
				shoot();
			}
		}
	}
	
	/**
	 * Fires the projectile from the bow with a
	 */
	private void shoot() 
	{
		// Shoot Mechanic
		_currentBowStage = BowStage.Idle;
		_pointerControllingTouch = -1;
		this.getComponent(AnimationComponent.class).getRenderInfo().setCurrentFrameInfo(0); // Reset bow
	}
	
	private void drawMechanic(int posX, int posY) 
	{
		Vector2 currentTouchLocation = new Vector2(posX, posY);
		_drawStrength = currentTouchLocation.sub(_targetLocation).len() / MAX_DRAW_LENGTH;
		_drawStrength *= -(Math.abs(currentTouchLocation.y) / currentTouchLocation.y);  
		
		if(_drawStrength > 1)
		{
			_drawStrength = 1;
		}
		else if(_drawStrength < 0)
		{
			_drawStrength = 0;
		}
		
		AnimationComponent ac = this.getComponent(AnimationComponent.class);
		ac.getRenderInfo().setCurrentFrameInfo((int)((ac.getRenderInfo().getFramesLength() - 1) * _drawStrength));
		handleProjectilePlacement();
	}


	private void handleProjectilePlacement() 
	{
		
	}

	private void selectTarget(int posX, int posY) 
	{
		_targetLocation = new Vector2(posX, posY);
		_currentBowStage = BowStage.Draw;
		// TODO: Quickly turn to spot
	}
}
