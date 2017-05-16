package com.mygdx.game.popUps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.tweening.TweenableFloat;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.entities.ButtonEntity;
import com.mygdx.game.globals.ButtonGlobals;
import com.mygdx.game.scenes.GameScenesManager;

import aurelienribon.tweenengine.Timeline;

public class EndScreenPopUp extends BaseGamePopUp implements IEventReceiver
{
	private ButtonEntity _continueButton;
	private TextEntity _scoreValueText;
	private TextEntity _wavesValueText;
	private TextEntity _highscoreValueText;
	
	private TweenableFloat _scoreValue;
	private TweenableFloat _waveValue;
	private TweenableFloat _highscoreValue;
	
	private int _scoreToDisplay, _waveToDisplay, _highscoreToDisplay;
	private boolean _isDisplaying = false;
	
	public EndScreenPopUp(boolean isCoverPopUp) 
	{
		super(isCoverPopUp);
	}

	public void displayEndScreen(int score, int wave, int highscore)
	{
		if(_isDisplaying) { return; }
		_scoreToDisplay = score;
		_waveToDisplay = wave;
		_highscoreToDisplay = highscore;
		startDisplay();
 	}
	
	private void startDisplay() 
	{
		_isDisplaying = true;
		spawnTexts();
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == ButtonGlobals.BUTTON_DOWN_EVENT)
		{
			this.closePopUp();
		}
		
	}
	
	@Override
	protected void onPopUpAwake()
	{
		this.getRenderComponent().setRenderInfo(Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_POP_UP_GAME_OVER));
		
		// Button
		_continueButton = new ButtonEntity(GameTextureResources.UI_INGAME_RETRY_BTN);
		_continueButton.getTransformComponent().setParent(getTransformComponent());
		_continueButton.getTransformComponent().translatePosition(new Vector2(0, -this.getRenderComponent().getRealHeight() * 0.34f));
		
		_continueButton.addEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		super.onPopUpAwake();
	}
	
	private void spawnTexts() 
	{
		_scoreValue = new TweenableFloat(0);
		_waveValue = new TweenableFloat(0);
		_highscoreValue = new TweenableFloat(0);
		
		_scoreValueText = new TextEntity(Engine.getFontResources().getFontData(GameFontResources.SCORE_FONT_BANDIDOS), Integer.toString(_scoreToDisplay), true);
		_wavesValueText = new TextEntity(Engine.getFontResources().getFontData(GameFontResources.WAVE_FONT_BANDIDOS), Integer.toString(_waveToDisplay), true);
		_highscoreValueText = new TextEntity(Engine.getFontResources().getFontData(GameFontResources.REGULAR_WHITE_BANDIDOS), Integer.toString(_highscoreToDisplay), true);
		
		int fontSize = 7;
		int titleFontSize = 4;
		
		Color titleColor = new Color(0.99f, 1f, 213f/255f, 1f);
		Color highscoreColor = new Color(1f, 228f/255f, 0, 1);
		
		TextEntity scoreTitle = new TextEntity(Engine.getFontResources().getFontData(GameFontResources.REGULAR_WHITE_BANDIDOS), "SCORE:", true);
		TextEntity waveTitle = new TextEntity(Engine.getFontResources().getFontData(GameFontResources.REGULAR_WHITE_BANDIDOS), "WAVE:", true);
		TextEntity highscoreTitle = new TextEntity(Engine.getFontResources().getFontData(GameFontResources.REGULAR_WHITE_BANDIDOS), "HIGHSCORE:", true);
		
		_scoreValueText.getTransformComponent().setParent(getTransformComponent());
		_wavesValueText.getTransformComponent().setParent(getTransformComponent());
		_highscoreValueText.getTransformComponent().setParent(getTransformComponent());
		
		scoreTitle.getTransformComponent().setParent(getTransformComponent());
		waveTitle.getTransformComponent().setParent(getTransformComponent());
		highscoreTitle.getTransformComponent().setParent(getTransformComponent());
		
		scoreTitle.getRenderComponent().setColor(titleColor);
		waveTitle.getRenderComponent().setColor(titleColor);
		
		highscoreTitle.getRenderComponent().setColor(titleColor);
		_highscoreValueText.getRenderComponent().setColor(titleColor);
		
		scoreTitle.getRenderComponent().setPivot(new Vector2(1, 0.5f), true);
		waveTitle.getRenderComponent().setPivot(new Vector2(1, 0.5f), true);
		highscoreTitle.getRenderComponent().setPivot(new Vector2(1, 0.5f), true);
		
		scoreTitle.setFontSize(titleFontSize);
		waveTitle.setFontSize(titleFontSize);
		highscoreTitle.setFontSize(titleFontSize);
		
		_scoreValueText.setFontSize(fontSize);
		_wavesValueText.setFontSize(fontSize);
		_highscoreValueText.setFontSize(fontSize);
		
		_scoreValueText.getRenderComponent().setPivot(new Vector2(0,0.5f), false);
		_wavesValueText.getRenderComponent().setPivot(new Vector2(0,0.5f), false);
		_highscoreValueText.getRenderComponent().setPivot(new Vector2(0,0.5f), false);
		
		float yDeltaScore = this.getRenderComponent().getCurrentTexture().getHeight() * 0.08f; 
		
		_scoreValueText.getTransformComponent().translatePosition(new Vector2(-10,yDeltaScore));
		_wavesValueText.getTransformComponent().translatePosition(new Vector2(-10,yDeltaScore - (_scoreValueText.getTextBounds().y / 2 + 22)));
		_highscoreValueText.getTransformComponent().setPosition(new Vector2(-10, _wavesValueText.getTransformComponent().getLocalPositionY() - 100));
		
		scoreTitle.getTransformComponent().setPosition(_scoreValueText.getTransformComponent().getLocalPositionX() - 30, _scoreValueText.getTransformComponent().getLocalPositionY());
		waveTitle.getTransformComponent().setPosition(_wavesValueText.getTransformComponent().getLocalPositionX() - 30, _wavesValueText.getTransformComponent().getLocalPositionY());
		highscoreTitle.getTransformComponent().setPosition(_highscoreValueText.getTransformComponent().getLocalPositionX() - 30, _highscoreValueText.getTransformComponent().getLocalPositionY());
		
		
		if(_highscoreToDisplay == _scoreToDisplay)
		{
			BasicEntity newHighscoreSprite = new BasicEntity();
			newHighscoreSprite.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_ELEMENT_NEW_HIGHSCORE_MARK), true));
			newHighscoreSprite.getTransformComponent().setParent(getTransformComponent());
			newHighscoreSprite.getTransformComponent().setPosition(this.getRenderComponent().getCurrentTexture().getWidth() * 0.36f,  -this.getRenderComponent().getCurrentTexture().getHeight() * 0.25f);
			
			highscoreTitle.getRenderComponent().setColor(highscoreColor);
			_highscoreValueText.getRenderComponent().setColor(highscoreColor);
		}
		
		// Animation
		
		scoreTitle.getRenderComponent().setAlpha(0);
		waveTitle.getRenderComponent().setAlpha(0);
		highscoreTitle.getRenderComponent().setAlpha(0);
		
		_scoreValueText.getRenderComponent().setAlpha(0);
		_wavesValueText.getRenderComponent().setAlpha(0);
		_highscoreValueText.getRenderComponent().setAlpha(0);
		
		Timeline tl = Timeline.createSequence();
		tl.beginSequence();

		tl.push(scoreTitle.getRenderComponent().doAlpha(1, 0.6f, false).getTween());
		tl.push(_scoreValueText.getRenderComponent().doAlpha(1, 0.6f, false).getTween());
		tl.push(this.getTransformComponent().doFloat(_scoreValue, _scoreToDisplay, 1f, false).getTween());
		
		tl.push(waveTitle.getRenderComponent().doAlpha(1, 0.6f, false).getTween());
		tl.push(_wavesValueText.getRenderComponent().doAlpha(1, 0.6f, false).getTween());
		tl.push(this.getTransformComponent().doFloat(_waveValue, _waveToDisplay, 1f, false).getTween());
		
		tl.push(highscoreTitle.getRenderComponent().doAlpha(1, 0.6f, false).getTween());
		tl.push(_highscoreValueText.getRenderComponent().doAlpha(1, 0.6f, false).getTween());
		tl.push(this.getTransformComponent().doFloat(_highscoreValue, _highscoreToDisplay, 1f, false).getTween());
		
		tl.end();
		
		this.getTransformComponent().startTimelineOnComponent(tl, false);
	}

	@Override
	protected void updated(float dt)
	{
		super.updated(dt);
		if(!_isDisplaying) { return; }
		_scoreValueText.setText(Integer.toString((int)_scoreValue.getValue()));
		_wavesValueText.setText(Integer.toString((int)_waveValue.getValue()));
		_highscoreValueText.setText(Integer.toString((int)_highscoreValue.getValue()));
	}
	
	@Override
	protected void onPopUpDestroyed() 
	{
		_continueButton.removeEventListener(ButtonGlobals.BUTTON_DOWN_EVENT, this);
		_continueButton = null;
		
		_scoreValueText = null;
		_wavesValueText = null;
		_highscoreValueText = null;
		
		_scoreValue = null;
		_waveValue = null;
		_highscoreValue = null;
		
		Engine.getSceneManager().setScene(GameScenesManager.GAME_SCENE);
	}
}
