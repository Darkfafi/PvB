package com.mygdx.game.ui;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.score.GameScoreSystem;

/**
 * UI for the Player's Score. This Class handles the Score UI.
 * @author Djamali
 *
 */
public class ScoreUI extends BaseEntity
{
	private TextEntity _scoreText;
	private BasicEntity _scoreIndicatorUI;
	
	@Override
	protected void awake() {
		this.addComponent(new RenderComponent(null, true));
		_scoreIndicatorUI = new BasicEntity(); 
		_scoreIndicatorUI.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_INGAME_SCORE_FIELD), true)); 
		_scoreIndicatorUI.getTransformComponent().setParent(this.getTransformComponent());
		_scoreText = new TextEntity(true);
		_scoreText.getTransformComponent().setParent(_scoreIndicatorUI.getTransformComponent());
		_scoreText.getTransformComponent().setPosition(
			new Vector2(
				(- (0.1f * _scoreIndicatorUI.getComponent(RenderComponent.class).getRealWidth())), 
				5
			)
		);
		_scoreText.getRenderComponent().setPivot(new Vector2(0f, 0.5f), false);
		_scoreText.setFontSize(9);
		_scoreText.setFont(Engine.getFontResources().getFontData(GameFontResources.SCORE_FONT_BANDIDOS));
	}

	public Vector2 getBackgroundSize()
	{
		return new Vector2(_scoreIndicatorUI.getComponent(RenderComponent.class).getRealWidth(), _scoreIndicatorUI.getComponent(RenderComponent.class).getRealHeight());
	}
	
	@Override
	protected void updated(float dt) 
	{
		_scoreText.setText(Integer.toString(GameScoreSystem.getInstance().getScore()));
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
	}

	@Override
	protected void destroyed() 
	{
		_scoreText = null;
		_scoreIndicatorUI = null;
	}
}
