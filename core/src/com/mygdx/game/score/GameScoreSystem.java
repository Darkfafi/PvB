package com.mygdx.game.score;

import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.TweenEvent;

/**
 * This class is a singleton which handles game specific score handling and score effects.
 * @author Ramses Di Perna
 *
 */
public class GameScoreSystem extends ScoreSystem implements IEventReceiver
{
	private static GameScoreSystem _instance;
	
	public static GameScoreSystem getInstance()
	{
		if(_instance == null)
			_instance = new GameScoreSystem();
		return _instance;
	}
	
	/**
	 * Adds score and spawns a text hover effect on the given location
	 * @param score to add
	 * @param effectXPos is the X position to place score text hover effect
	 * @param effectYPos is the Y position to place score text hover effect
	 */
	public void addScore(int score, float effectXPos, float effectYPos)
	{
		int scoreAdded = this.addScore(score);
		TextEntity te = new TextEntity(Engine.getFontResources().getFontData(GameFontResources.REGULAR_WHITE_BANDIDOS), scoreAdded + "+", true);
		te.setFontSize(4);
		te.getTransformComponent().setPosition(effectXPos, effectYPos);
		te.getTransformComponent().doPosition(effectXPos, effectYPos + 20, 0.8f, true).ease(EaseType.BackOut);
		te.getRenderComponent().doAlpha(0.1f, 0.7f, true).addEventListener(EngineTween.ENGINE_TWEEN_EVENT, this);
	}


	@Override
	public void onReceiveEvent(Event event) 
	{
		if(event.getType() == EngineTween.ENGINE_TWEEN_EVENT)
		{
			onTextEffectEndEvent((TweenEvent)event);
		}
	}


	private void onTextEffectEndEvent(TweenEvent event) 
	{
		RenderComponent rc = (RenderComponent)event.getEngineTween().getTween().getTarget();
		rc.getParentOfComponent().destroy();
	}
}
