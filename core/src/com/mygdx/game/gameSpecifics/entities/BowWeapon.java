package com.mygdx.game.gameSpecifics.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.BaseEntity;
import com.mygdx.game.entities.components.Rendering.AnimationComponent;
import com.mygdx.game.entities.components.Rendering.Animations;
import com.mygdx.game.events.Event;
import com.mygdx.game.events.IEventReceiver;
import com.mygdx.game.globals.InputGlobals;
import com.mygdx.game.scenes.RenderComponents;
import com.mygdx.game.touchinput.TouchEvent;

public class BowWeapon extends BaseEntity implements IEventReceiver
{
	public enum BowStage
	{
		Idle,		// Waiting for target location to fire at
		Draw  		// Preparing and Doing the Shoot method
	}

	public static final float MAX_DRAW_STRENGTH = 30f;
	public static final float MAX_DRAW_LENGTH = 200f;
	
	private BowStage _currentBowStage = BowStage.Idle; 	// The current stage of the bow
	private float _drawStrength = 0f;					// The draw strength to release the arrow with
	private Vector2 _targetLocation = null; 			// The target location to fire at / where the finger started
	private float _radiusToTargetLoc = 0f;
	private int _pointerControllingTouch = -1;
	
	private ArrowProjectile _currentProjectile;
	
	private long _bowDrawSoundInstance = -10;
	private float _volumeDraw = 0;
	
	private Texture _aimTexture;
	
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
		_aimTexture = MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_BOW_AIM_TARGET).getTextureToDraw();
		_bowDrawSoundInstance = -1;
		Animations animations = new Animations("draw", MyGdxGame.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_BOW_DRAW), false);
		this.addComponent(new AnimationComponent(animations, false, false)).setSortingLayer(2);
		MyGdxGame.getInputHandler().addEventListener(InputGlobals.TOUCH_EVENT, this);
		this.setBowIdle();
	}

	@Override
	protected void updated(float dt) 
	{
		if(_currentBowStage == BowStage.Draw && _pointerControllingTouch != -1)
		{
			Vector2 aimLocation = new Vector2(MyGdxGame.getInputHandler().getX(_pointerControllingTouch),MyGdxGame.getInputHandler().getY(_pointerControllingTouch));
			getTransformComponent().lookAt(aimLocation, 0.2f);
			
			MyGdxGame.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setVolume(_bowDrawSoundInstance, _volumeDraw);
			MyGdxGame.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setPitch(_bowDrawSoundInstance, _drawStrength);
			_volumeDraw = 0.1f;
		}
		handleProjectilePlacement();
	}
	
	@Override
	protected void destroyed() 
	{
		MyGdxGame.getInputHandler().removeEventListener(InputGlobals.TOUCH_EVENT, this);
		_targetLocation = null;
		_currentProjectile.destroy();
		_currentProjectile = null;
		_aimTexture = null;
	}
	
	/**
	 * Redirection of the Event to TouchEvent
	 * @param event received which is casted to a TouchEvent
	 */
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
				shoot(_drawStrength, 0.1f);
			}
		}
	}
	
	/**
	 * Fires the current arrow from the bow with the strength given.
	 * @param strengthPercentage makes the arrow shoot in distance of powerToDistancePower() * the value and the strength of MAX_DRAW_STRENGTH * the value
	 * @param minimum is the minimum the shooting strength should be. If its below that it will be put onto the minimum value
	 */
	private void shoot(float strengthPercentage, float minimum) 
	{
		if(strengthPercentage < minimum)
			strengthPercentage = minimum;
		
		MyGdxGame.getAudioResources().getSound(GameAudioResources.SOUND_BOW_RELEASE).play(strengthPercentage, ((float)Math.random() * 0.3f) + 0.95f, 0);
		_currentProjectile.fire((powerToDistancePower() * strengthPercentage), MAX_DRAW_STRENGTH * strengthPercentage);
		setBowIdle();
	}
	
	private void setBowIdle()
	{
		_currentBowStage = BowStage.Idle;
		_pointerControllingTouch = -1;
		_drawStrength = 0;
		this.getComponent(AnimationComponent.class).getRenderInfo().setCurrentFrameInfo(0); // Reset bow
		_currentProjectile = new ArrowProjectile();
		if(_bowDrawSoundInstance != -1)
			MyGdxGame.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).stop(_bowDrawSoundInstance);
		
		_bowDrawSoundInstance = -1;
	}
	
	/**
	 * Handles the complete draw mechanic of the bow.
	 * @param posX is the x position of the touch which is controlling the draw
	 * @param posY is the y position of the touch which is controlling the draw
	 */
	private void drawMechanic(int posX, int posY) 
	{
		Vector2 lineToTouch = new Vector2(posX - this.getTransformComponent().getPositionX(), posY - this.getTransformComponent().getPositionY());
		Vector2 lineToRealTarget = new Vector2(lineToTouch.x, lineToTouch.y);
		lineToRealTarget.setLength(_radiusToTargetLoc);
		Vector2 lineToRealMaxDraw = new Vector2(lineToRealTarget.x, lineToRealTarget.y);
		lineToRealMaxDraw.setLength(_radiusToTargetLoc - MAX_DRAW_LENGTH);
		
		float preStrength = _drawStrength;
		
		_drawStrength = 1 - (lineToTouch.len() - lineToRealMaxDraw.len()) / (lineToRealTarget.len() - lineToRealMaxDraw.len());
		
		if(_drawStrength > 1)
		{
			_drawStrength = 1;
		}
		else if(_drawStrength < 0)
		{
			_drawStrength = 0;
		}
		
		if(preStrength != _drawStrength)
			_volumeDraw = _drawStrength;
		
		AnimationComponent ac = this.getComponent(AnimationComponent.class);
		ac.getRenderInfo().setCurrentFrameInfo((int)((ac.getRenderInfo().getFramesLength() - 1) * _drawStrength));
		handleProjectilePlacement();
	}
	
	/**
	 * Places the arrow in the correct orientation on the bow.
	 * This orientation includes position and rotation.
	 * It gets the position form the 'projectilePullDistance' method 
	 */
	private void handleProjectilePlacement() 
	{
		if(_currentProjectile == null || _currentProjectile.getTransformComponent() == null) { return; }
		_currentProjectile.getTransformComponent().setPosition(new Vector2(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY()));
		_currentProjectile.getTransformComponent().setRotation(this.getTransformComponent().getRotation());
		_currentProjectile.getTransformComponent().translatePosition(projectilePullDistance());
	}
	
	/**
	 * Gives the Arrow location it requires to be in the animation.
	 * NOTE: Must be adjusted when art changes!
	 * @return The Arrow position in sink with the animation.
	 */
	private Vector2 projectilePullDistance()
	{
		Vector2 v = _currentProjectile.getTransformComponent().getUpwards();
		float amount = 72;
		float delta = 4f;
		amount -= delta * this.getComponent(AnimationComponent.class).getRenderInfo().getCurrentFrameInfo();
		v.x *= amount;
		v.y *= amount;
		return v;
	}
	
	/**
	 * Returns the distance the bow can shoot in pixels with its draw strength.
	 * @return Max distance in pixels
	 */
	private float powerToDistancePower()
	{
		return (float) Math.pow(MAX_DRAW_STRENGTH, 2);
	}
	
	/**
	 * Selects a start touch position before draw. This will be used as the start position of the draw value.
	 * @param posX is the x position of the position value.
	 * @param posY is the y position o the position value.
	 */
	private void selectTarget(int posX, int posY) 
	{
		_targetLocation = new Vector2(posX, posY);
		_radiusToTargetLoc = Vector2.dst(_targetLocation.x, _targetLocation.y, this.getTransformComponent().getPositionX(),this.getTransformComponent().getPositionY());
		_currentBowStage = BowStage.Draw;
		
		_bowDrawSoundInstance = MyGdxGame.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).play();
		MyGdxGame.getAudioResources().getSound(GameAudioResources.SOUND_BOW_DRAW).setLooping(_bowDrawSoundInstance, true);
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		if(_currentProjectile == null || _aimTexture == null) { return;}
		if(_currentBowStage == BowStage.Draw && _pointerControllingTouch != -1)
		{
			Vector2 landLocation = _currentProjectile.getLandingPositionWithDrawWeight(powerToDistancePower() * (_drawStrength * 0.88f));
			renderComponents.getSpriteBatch().draw(_aimTexture, landLocation.x - _aimTexture.getWidth() / 2, landLocation.y - _aimTexture.getHeight() / 2, _aimTexture.getWidth(), _aimTexture.getHeight());
		}
	}
}
