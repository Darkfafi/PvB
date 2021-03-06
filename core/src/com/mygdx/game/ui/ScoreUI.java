package com.mygdx.game.ui;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.Engine;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.score.GameScoreSystem;

/**
 * UI for the Player's Score. This Class handles the Score UI.
 * @author Djamali Jones & Ramses Di Perna
 *
 */
public class ScoreUI extends BaseEntity
{
	public static float SCORE_COUNT_DURATION = 0.45f;
	
	private TextEntity _scoreText;
	private TextEntity _multiplierText;
	private BasicEntity _scoreIndicatorUI;
	private int _scoreDisplaying = 0;
	private float _countingTracker = -1;
	private int _oldScoreDisplayed = 0;
	private int _lastScoreRecording = 0;
	private int _changeTracker = 0;
	
	@Override
	protected void awake() {
		this.addComponent(new RenderComponent(null, true));
		_scoreIndicatorUI = new BasicEntity(); 
		_scoreIndicatorUI.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_INGAME_SCORE_FIELD), true)); 
		_scoreIndicatorUI.getTransformComponent().setParent(this.getTransformComponent());
		
		_scoreText = new TextEntity(true);
		_scoreText.getTransformComponent().setParent(_scoreIndicatorUI.getTransformComponent());
		_scoreText.getTransformComponent().setPosition((- (0.17f * _scoreIndicatorUI.getComponent(RenderComponent.class).getRealWidth())), 5);
		_scoreText.getRenderComponent().setPivot(0f, 0.5f, false);
		_scoreText.setFontSize(9);
		_scoreText.setFont(Engine.getFontResources().getFontData(GameFontResources.SCORE_FONT_BANDIDOS));
		
		_multiplierText = new TextEntity(true);
		_multiplierText.getTransformComponent().setParent(_scoreIndicatorUI.getTransformComponent());
		_multiplierText.getTransformComponent().setPosition(((_scoreIndicatorUI.getComponent(RenderComponent.class).getRealWidth() / 2) * 0.65f), 5);
		_multiplierText.setFontSize(6.5f);
		_multiplierText.setFont(Engine.getFontResources().getFontData(GameFontResources.MULTIPLIER_FONT_BANDIDOS));
	}
	
	/**
	 * Returns the size of the background which was set for this score ui.
	 * @return The background size of this UI 
	 */
	public Vector2 getBackgroundSize()
	{
		return new Vector2(_scoreIndicatorUI.getComponent(RenderComponent.class).getRealWidth(), _scoreIndicatorUI.getComponent(RenderComponent.class).getRealHeight());
	}
	
	@Override
	protected void updated(float dt) 
	{
		int gameScore = GameScoreSystem.getInstance().getScore();
		if(_scoreDisplaying != gameScore)
		{
			if(_oldScoreDisplayed == _scoreDisplaying)
			{
				_countingTracker = 0;
				_changeTracker = _oldScoreDisplayed;
			}
			
			if(_lastScoreRecording != gameScore)
			{
				_countingTracker = 0;
				_changeTracker = _scoreDisplaying;
			}
			
			_countingTracker += dt;
			float differents = gameScore - _changeTracker;
			_scoreDisplaying = _changeTracker + (int)Math.ceil(differents * (_countingTracker/SCORE_COUNT_DURATION));
			if(_scoreDisplaying >= gameScore)
			{
				_scoreDisplaying = gameScore;
				_oldScoreDisplayed = _scoreDisplaying;
			}
		}
		_lastScoreRecording = gameScore;
		_scoreText.setText(Integer.toString(_scoreDisplaying));
		_multiplierText.setText("x"+Integer.toString(GameScoreSystem.getInstance().getMultiplier()));
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
	}

	@Override
	protected void destroyed() 
	{
		_scoreText = null;
		_multiplierText = null;
		_scoreIndicatorUI = null;
	}
}
