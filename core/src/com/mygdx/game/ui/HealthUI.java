package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * UI for the Player's Health. This Class is for handling the health of the given entity's HealthComponent.
 * @author Djamali
 *
 */
public class HealthUI extends BaseEntity 
{
	private RenderInfo _healthBar;
	private TextEntity _healthLabel;
	private TextEntity _healthPercentage;
	private ShapeRenderer _shapeRenderer = new ShapeRenderer();
	private HealthComponent _healthComponent;
	
	private float _normalizedHealth;

	public HealthUI(HealthComponent healthComponent)
	{
		_healthComponent = healthComponent;
	}
	
	@Override
	protected void awake() {
		this.addComponent(new RenderComponent(null, true));
		_healthBar = Engine.getTextureResources().getRenderInfo(GameTextureResources.UI_INGAME_HEALTHBAR);
		
		_healthLabel = new TextEntity(true);
		_healthLabel.getTransformComponent().setPosition(new Vector2(30, _healthBar.getTextureToDraw().getHeight()));
		_healthLabel.getRenderComponent().setPivot(new Vector2(0f, 0.5f), false);
		_healthLabel.setFontSize(5);
		_healthLabel.setFont(Engine.getFontResources().getFontData(GameFontResources.REGULAR_WHITE_BANDIDOS));
		
		_healthPercentage = new TextEntity(true);
		_healthPercentage.getTransformComponent().setPosition(new Vector2((Engine.getWidth() - 30), _healthBar.getTextureToDraw().getHeight()));
		_healthPercentage.getRenderComponent().setPivot(new Vector2(1f, 0.5f), false);
		_healthPercentage.setFontSize(5);
		_healthPercentage.setFont(Engine.getFontResources().getFontData(GameFontResources.REGULAR_WHITE_BANDIDOS));
	}

	@Override
	protected void updated(float dt) {
		_normalizedHealth = _healthComponent.getNormalizedHealth();
		
		_healthLabel.setText("HEALTH");
		_healthPercentage.setText(Integer.toString((int)(_normalizedHealth * 100)) + "%");
	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		
		renderComponents.getSpriteBatch().end();
		
		//Draw the Black Bar behind the Healthbar
		_shapeRenderer.begin(ShapeType.Filled);
		_shapeRenderer.setColor(0, 0, 0, 0.5f);
		_shapeRenderer.rect(
			19, 
			19, 
			Engine.getWidth() - 38, 
			_healthBar.getTextureToDraw().getHeight() + 2
		);
		_shapeRenderer.end();
		
		renderComponents.getSpriteBatch().begin();
		
		//Draw the Healthbar
		renderComponents.getSpriteBatch().draw(
			_healthBar.getTextureToDraw(), 
			20, 
			20, 
			(Engine.getWidth() - 40) * _normalizedHealth, 
			_healthBar.getTextureToDraw().getHeight()
		);
	}

	@Override
	protected void destroyed() {
		_healthLabel.destroy();
		_healthLabel = null;
		
		_healthPercentage.destroy();
		_healthPercentage = null;
		
		_shapeRenderer.dispose();
		_shapeRenderer = null;
		
		_healthComponent = null;
	}
	
}
