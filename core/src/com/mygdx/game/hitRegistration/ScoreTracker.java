package com.mygdx.game.hitRegistration;

import java.util.ArrayList;

import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.FontData;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.TweenEvent;
import com.mygdx.game.score.GameScoreSystem;
import com.mygdx.game.score.ScoreHolderComponent;

/**
 * This class tracks the score using the HiRegistrationPoint and spawns the text effects which represents them.
 * @author Ramses Di Perna
 *
 */
public class ScoreTracker implements IEventReceiver 
{
	
	public ScoreTracker() 
	{
		HitRegistrationPoint.getInstance().addEventListener(HitRegistrationPoint.HIT_REGISTRATED_EVENT, this);
	}

	public void clean() 
	{
		HitRegistrationPoint.getInstance().removeEventListener(HitRegistrationPoint.HIT_REGISTRATED_EVENT, this);
	}

	@Override
	public void onReceiveEvent(Event event) 
	{
		if (event.getType() == HitRegistrationPoint.HIT_REGISTRATED_EVENT) 
		{
			onHitRegistrationEvent((HitRegistrationEvent) event);
		}
		
		if(event.getType() == EngineTween.ENGINE_TWEEN_EVENT)
		{
			onTextEffectEndEvent((TweenEvent)event);
		}
	}

	private void onHitRegistrationEvent(HitRegistrationEvent event) {
		if (event.getHitEntity() == null) {
			return;
		}

		ScoreHolderComponent shc = event.getHitEntity().getComponent(ScoreHolderComponent.class);

		float width = 10 * event.getHitEntity().getTransformComponent().getScaleX();
		float height = 10 * event.getHitEntity().getTransformComponent().getScaleY();
		float offsetX = 0;
		float offsetY = 0;
		if (shc == null) {
			return;
		}

		RenderComponent rc = event.getHitEntity().getComponent(RenderComponent.class);
		if (rc == null)
			rc = event.getHitEntity().getComponent(AnimationComponent.class);

		if (rc != null) {
			width = rc.getRealWidth();
			height = rc.getRealHeight();
			offsetX = rc.getPivotX() * width;
			offsetY = rc.getPivotY() * height;
		}

		if (shc.getScoreGainType() == ScoreHolderComponent.ScoreGainType.Destroy) {
			HealthComponent hc = event.getHitEntity().getComponent(HealthComponent.class);
			if (hc == null) {
				System.out.println("Can not check for destruction type without the healthComponent");
				return;
			}
			if (hc.isAlive()) {
				return;
			}
		}

		doScore(shc.getScoreWorth(), event.getHitTool(), event.getHitTypes(),
				event.getHitEntity().getTransformComponent().getPositionX() - offsetX,
				event.getHitEntity().getTransformComponent().getPositionY() - offsetY, width,
				height);
	}

	private void doScore(int score, int hitTool, int[] hitTypes, float x, float y, float width, float height) {
		ArrayList<BonusScoreToGainInfo> scoresToGain = getBonusScoresToGain(hitTool, hitTypes);
		normalScoreEffects(GameScoreSystem.getInstance().addScore(score), x + width * 0.8f, y + height / 2); 

		for (int i = 0; i < scoresToGain.size(); i++) {
			int s = GameScoreSystem.getInstance().addScore(scoresToGain.get(i).Score);
			scoresToGain.get(i).Score = s;
		}
		
		bonusScoreEffects(scoresToGain, x + width / 2, y + height);
	}

	private ArrayList<BonusScoreToGainInfo> getBonusScoresToGain(int hitTool, int[] hitTypes) {
		ArrayList<BonusScoreToGainInfo> infos = new ArrayList<BonusScoreToGainInfo>();

		if (hitTool == HitGlobals.TOOL_TRAP) {
			infos.add(new BonusScoreToGainInfo(25, "Trap Kill!"));
		}

		return infos;
	}

	private void normalScoreEffects(int score, float x, float y) 
	{
		textEffect(Engine.getFontResources().getFontData(GameFontResources.SCORE_FONT_BANDIDOS), "+" + score, 6, x, y);
	}

	private void bonusScoreEffects(ArrayList<BonusScoreToGainInfo> bonusScoreInfo, float x, float y) {

	}
	
	private void textEffect(FontData font, String text, int size, float x, float y)
	{
		TextEntity te = new TextEntity(font,
				text, true);
		te.setFontSize(size);
		te.getTransformComponent().setPosition(x, y);
		te.getTransformComponent().doPosition(x, y + 22, 1f, true).ease(EaseType.BackOut);
		te.getRenderComponent().doAlpha(0.1f, 0.9f, true).addEventListener(EngineTween.ENGINE_TWEEN_EVENT, this);
	}

	private class BonusScoreToGainInfo {
		public String Description;
		public int Score;

		public BonusScoreToGainInfo(int score, String description) {

		}
	}

	private void onTextEffectEndEvent(TweenEvent event) 
	{
		RenderComponent rc = (RenderComponent)event.getEngineTween().getTween().getTarget();
		rc.getParentOfComponent().destroy();
	}
}
