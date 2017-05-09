package com.mygdx.game.ui;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.score.GameScoreSystem;

/**
 * UI for the Player's Score. This Class handles the Score UI.
 * @author Djamali
 *
 */
public class ScoreUI extends BaseEntity
{
	private TextEntity _scoreText;
	private RenderInfo _scoreIndicatorUI;

	public ScoreUI()
	{
		//
	}
	
	@Override
	protected void awake() {
		this.addComponent(new RenderComponent(null, true));
		_scoreIndicatorUI = Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_INGAME_SCORE_FIELD);
		
		_scoreText = new TextEntity(true);
		_scoreText.getTransformComponent().setPosition(
			new Vector2(
				(Engine.getWidth() / 2) - (0.1f * _scoreIndicatorUI.getTextureToDraw().getWidth()), 
				Engine.getHeight() - (_scoreIndicatorUI.getTextureToDraw().getHeight() / 2) - 5
			)
		);
		_scoreText.getRenderComponent().setPivot(new Vector2(0f, 0.5f), false);
		_scoreText.setFontSize(9);
		_scoreText.setFont(Engine.getFontResources().getFontData(GameFontResources.SCORE_FONT_BANDIDOS));
	}

	@Override
	protected void updated(float dt) {
		_scoreText.setText(Integer.toString(GameScoreSystem.getInstance().getScore()));
	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		//Draw the Score Indicator
		renderComponents.getSpriteBatch().draw(
			_scoreIndicatorUI.getTextureToDraw(), 
			(Engine.getWidth() / 2) - (_scoreIndicatorUI.getTextureToDraw().getWidth() / 2), 
			(Engine.getHeight() - _scoreIndicatorUI.getTextureToDraw().getHeight()) - 10, 
			_scoreIndicatorUI.getTextureToDraw().getWidth(), 
			_scoreIndicatorUI.getTextureToDraw().getHeight()
		);
	}

	@Override
	protected void destroyed() {
		_scoreText.destroy();
		_scoreText = null;
		_scoreIndicatorUI = null;
	}
}
