package com.mygdx.game.hitRegistration;

import java.util.ArrayList;

import com.mygdx.game.Engine;
import com.mygdx.game.GameFontResources;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.factories.EffectFactory;
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
		normalScoreEffects(GameScoreSystem.getInstance().addScore(score), x + width / 2, y + height / 2, x + width * 0.8f, y + height * 0.75f); 

		for (int i = 0; i < scoresToGain.size(); i++) {
			int s = GameScoreSystem.getInstance().addScore(scoresToGain.get(i).Score);
			scoresToGain.get(i).Score = s;
		}
		
		bonusScoreEffects(scoresToGain, x, y, x + width / 2, y + height + 15);
	}

	private ArrayList<BonusScoreToGainInfo> getBonusScoresToGain(int hitTool, int[] hitTypes) {
		ArrayList<BonusScoreToGainInfo> infos = new ArrayList<BonusScoreToGainInfo>();
		
		if (hitTool == HitGlobals.TOOL_TRAP) 
		{
			infos.add(new BonusScoreToGainInfo(10, "Trap Bonus"));
		}
		else if(hitTool == HitGlobals.TOOL_SPECIAL_ARROW)
		{
			infos.add(new BonusScoreToGainInfo(15, "Special Bonus"));
		}
		
		return infos;
	}

	private void normalScoreEffects(int score, float startX, float startY, float x, float y) 
	{
		EffectFactory.createTextEffect(Engine.getFontResources().getFontData(GameFontResources.SCORE_FONT_BANDIDOS), "+" + score, 6, startX, startY, x, y);
	}

	private void bonusScoreEffects(ArrayList<BonusScoreToGainInfo> bonusScoreInfo, float startX, float startY, float x, float y) 
	{
		float orY = y;
		float newY = orY;
		boolean under = false;
		
		for(int i = 0; i < bonusScoreInfo.size(); i++)
		{
			EffectFactory.createTextEffect(Engine.getFontResources().getFontData(GameFontResources.SCORE_FONT_BANDIDOS), "+" + bonusScoreInfo.get(i).Score, 6, startX, startY, x, newY, 0, 0, 0.3f + (0.1f * i));
			EffectFactory.createTextEffect(Engine.getFontResources().getFontData(GameFontResources.SCORE_FONT_BANDIDOS), bonusScoreInfo.get(i).Description, 4f, startX, startY,  x, newY, 0, -30, 0.3f + (0.1f * i));
			newY += 55 * ((under) ? -1 : 1);
			
			if(newY >= Engine.getHeight() - 80)
			{
				newY = orY - 55;
				under = true;
			}
		}
	}
	
	

	private class BonusScoreToGainInfo 
	{
		public String Description;
		public int Score;

		public BonusScoreToGainInfo(int score, String description) 
		{
			Score = score;
			Description = description;
		}
	}
}
