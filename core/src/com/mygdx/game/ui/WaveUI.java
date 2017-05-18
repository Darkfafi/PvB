package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.engine.tweening.TweenableFloat;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.waves.WaveSystem;

import aurelienribon.tweenengine.Timeline;

/**
 * UI for the Wave the Player is on. This Class handles the Wave UI.
 * @author Djamali
 *
 */
public class WaveUI extends BaseEntity implements IEventReceiver
{
	private BasicEntity _waveIndicatorUI;
	private WaveSystem _waveSystem;
	private TextEntity _waveText;
	
	private ShapeRenderer _shapeRenderer;
	private TweenableFloat _bgSizeValue;
	private TextEntity _waveMidScreenDisplayText;
	
	public WaveUI(WaveSystem waveSystem)
	{
		_waveSystem = waveSystem;
		_shapeRenderer = new ShapeRenderer();
		_bgSizeValue = new TweenableFloat(1);
		_shapeRenderer.setAutoShapeType(true);
		_waveSystem.addEventListener(WaveSystem.EVENT_WAVE_STARTED, this);
		
		_waveMidScreenDisplayText = new TextEntity("TEST", true);
		_waveMidScreenDisplayText.getTransformComponent().setPosition(-_waveMidScreenDisplayText.getTextBounds().x, (Engine.getHeight() * 0.55f) + 50);
	}
	

	@Override
	protected void awake()
	{
		this.addComponent(new RenderComponent(null, true));
		_waveIndicatorUI = new BasicEntity();
		_waveIndicatorUI.addComponent(new RenderComponent( Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_INGAME_WAVE_FIELD), true));
		_waveIndicatorUI.getTransformComponent().setParent(this.getTransformComponent());
		
		_waveText = new TextEntity(true);
		_waveText.getTransformComponent().setParent(_waveIndicatorUI.getTransformComponent());
		_waveText.getRenderComponent().setPivot(new Vector2(0.5f, 0.5f), false);
		_waveText.getTransformComponent().setPosition(
			new Vector2(
				22, 
				5
			)
		);
		_waveText.setFontSize(9);
		_waveText.setFont(Engine.getFontResources().getFontData(GameFontResources.WAVE_FONT_BANDIDOS));
	}

	public Vector2 getBackgroundSize()
	{
		return new Vector2(_waveIndicatorUI.getComponent(RenderComponent.class).getRealWidth(), _waveIndicatorUI.getComponent(RenderComponent.class).getRealHeight());
	}
	
	@Override
	protected void updated(float dt) 
	{

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		if(_bgSizeValue.getValue() != 1)
		{
			renderComponents.getSpriteBatch().end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			_shapeRenderer.begin(ShapeType.Filled);
			_shapeRenderer.setColor(0,0,0,0.4f);
			_shapeRenderer.rect(0 - (Engine.getWidth() * _bgSizeValue.getValue()), Engine.getHeight() * 0.55f, Engine.getWidth(), 100);
			_shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			renderComponents.getSpriteBatch().begin();
		}
	}

	@Override
	protected void destroyed() 
	{
		_waveSystem.removeEventListener(WaveSystem.EVENT_WAVE_STARTED, this);
		_waveSystem = null;
		_waveText = null;
		_waveIndicatorUI = null;
		
		_shapeRenderer.dispose();
		_shapeRenderer = null;
		_bgSizeValue = null;
		_waveMidScreenDisplayText.destroy();
		_waveMidScreenDisplayText = null;
	}


	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == WaveSystem.EVENT_WAVE_STARTED)
		{
			_waveText.setText(Integer.toString(_waveSystem.getCurrentWave()));
			_waveMidScreenDisplayText.setText("Wave " + Integer.toString(_waveSystem.getCurrentWave()));
			setCurrentMidScreenTextFont();
			showWaveStartedEffect();
		}
	}


	private void setCurrentMidScreenTextFont()
	{
		String fontKey = GameFontResources.SCORE_FONT_BANDIDOS;
		switch(_waveSystem.getCurrentWaveType())
		{
		case 0:
			fontKey = GameFontResources.MULTIPLIER_FONT_BANDIDOS;
			break;
		case 1:
			fontKey = GameFontResources.SCORE_FONT_BANDIDOS;
			break;
		case 2:
			fontKey = GameFontResources.WAVE_FONT_BANDIDOS;
			break;
		}
		
		_waveMidScreenDisplayText.setFont(Engine.getFontResources().getFontData(fontKey));
		_waveMidScreenDisplayText.getTransformComponent().setPosition(-_waveMidScreenDisplayText.getTextBounds().x, (Engine.getHeight() * 0.55f) + 50);
	}


	private void showWaveStartedEffect() 
	{
		Timeline tl = Timeline.createSequence();
		tl.push(this.getTransformComponent().doFloat(_bgSizeValue, 0, 0.3f, TweenStartType.Timeline).getTween())
		.push(_waveMidScreenDisplayText.getTransformComponent().doPosition(Engine.getWidth() / 2, (Engine.getHeight() * 0.55f) + 50, 0.4f, TweenStartType.Timeline).ease(EaseType.BackOut).getTween())
		.pushPause(1f)
		.push(_waveMidScreenDisplayText.getTransformComponent().doPosition(Engine.getWidth() + _waveMidScreenDisplayText.getTextBounds().x, (Engine.getHeight() * 0.55f) + 50, 0.4f, TweenStartType.Timeline).ease(EaseType.BackIn).getTween())
		.push(this.getTransformComponent().doFloat(_bgSizeValue, -1, 0.3f, TweenStartType.Timeline).setCallbackMethod(new IEngineTweenMethod(){

			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				_bgSizeValue.setValue(1);
				_waveMidScreenDisplayText.getTransformComponent().setPosition(-_waveMidScreenDisplayText.getTextBounds().x, (Engine.getHeight() * 0.55f) + 50);
			}}
		).getTween());
		
		this.getTransformComponent().startTimelineOnComponent(tl, true);
		
	}

}
