package com.mygdx.game.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameAudioResources;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.FontData;
import com.mygdx.game.engine.entities.TextEntity;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.entities.BasicEntity;
import com.mygdx.game.entities.Effect;

/**
 * This class contains static methods which can be used to create effects
 * @author Ramses Di Perna
 *
 */
public class EffectFactory 
{
	/**
	 * The type of the explosion for explosion effects
	 * @author Ramses Di Perna
	 *
	 */
	public enum ExplosionType
	{
		SmallExplosion,
		BigExplosion
	}
	
	/**
	 * Creates a hit effect and deletes it when its finished.
	 * @param xPosition to spawn hit effect
	 * @param yPosition to spawn hit effect
	 * @param scale to make hit effect
	 * @param color to dye the effect in
	 * @return The hit effect instance created
	 */
	public static Effect createHitEffect(float xPosition, float yPosition, float scale, Color color)
	{
		Effect hitEffect = new Effect(Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_EFFECT_HIT), false);
		
		hitEffect.getAnimationComponent().setColor(new Color(color));
		hitEffect.getTransformComponent().setPosition(xPosition, yPosition);
		
		hitEffect.getTransformComponent().setScale(new Vector2(scale, scale));
		hitEffect.getAnimationComponent().setAnimationSpeed(0.4f);
		
		return hitEffect;
	}
	
	/**
	 * Creates a blood pool and makes it fade away after a given amount of time
	 * @param xPosition to spawn the blood pool
	 * @param yPosition to spawn the blood pool
	 * @param size to scale blood pool to
	 * @param timeOnGround is the time this blood pool is on the ground in seconds before it fades.
	 * @return The blood pool instance created
	 */
	public static BasicEntity createBloodPool(float xPosition, float yPosition, final float size, final float timeOnGround)
	{
		final BasicEntity bloodBlood = new BasicEntity();
		
		bloodBlood.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SHEET_BLOOD_POOL), false)).setPivot(new Vector2(0.5f, 0.5f), false);
		bloodBlood.getComponent(RenderComponent.class).setCurrentFrameInfo((Math.random() < 0.5f) ? 0 : 1);
		bloodBlood.getTransformComponent().setPosition(xPosition, yPosition);
		bloodBlood.getTransformComponent().setScale(new Vector2(0, 0));
		bloodBlood.getTransformComponent().doScale(size, size, 0.2f, TweenStartType.GameTime).setCallbackMethod(
		new IEngineTweenMethod() {

			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				bloodBlood.getComponent(RenderComponent.class).doAlpha(0, 2f, TweenStartType.GameTime).getTween().delay(timeOnGround);
			}}).getTween().delay(0.25f);
		
		return bloodBlood;
	}
	
	/**
	 * Creates an explosion effect on the given position and deletes when it is finished playing
	 * @param explosionType is the type of explosion effect to spawn
	 * @param xPosition is the x position to spawn the explosion effect on
	 * @param yPosition is the y position to spawn the explosion effect on
	 * @param scale is how big the explosion effect should be scaled. This will also affect the screen shake of this effect
	 * @return The explosion effect instance.
	 */
	public static Effect createExplosionEffect(ExplosionType explosionType, float xPosition, float yPosition, final float scale)
	{
		float shakeValue = 0;
		String explosionRenderInfoKey = null;
		String explosionSoundKey = null;
		float scaleMultiplier = 1;
		switch(explosionType)
		{
		case BigExplosion:
			shakeValue = 7;
			scaleMultiplier = 1.5f;
			explosionRenderInfoKey = GameTextureResources.ANIMATION_EFFECT_EXPLOSION_BIG;
			explosionSoundKey = GameAudioResources.SOUND_SMALL_EXPLOSION_EFFECT;
			break;
		case SmallExplosion:
			shakeValue = 3.5f;
			scaleMultiplier = 1.5f;
			explosionRenderInfoKey = GameTextureResources.ANIMATION_EFFECT_EXPLOSION_SMALL;
			explosionSoundKey = GameAudioResources.SOUND_SMALL_EXPLOSION_EFFECT;
			break;
		default:
			System.out.println("Warning: ExplosionType: " + explosionType + " not defined. Please do in the EffectFactory!");
			shakeValue = 0;
			explosionSoundKey = null;
			explosionRenderInfoKey = null;
			break;
		
		}
		
		Effect explosionEffect = new Effect(Engine.getTextureResources().getRenderInfo(explosionRenderInfoKey), false);
		
		explosionEffect.getTransformComponent().setPosition(xPosition, yPosition);
		
		explosionEffect.getTransformComponent().setScale(new Vector2(scale * scaleMultiplier, scale * scaleMultiplier));
		explosionEffect.getAnimationComponent().setPivot(new Vector2(0.5f, 0f),  false);
		explosionEffect.getAnimationComponent().setAnimationSpeed(0.4f);
		
		Engine.getAudioResources().getSound(explosionSoundKey).play(1, 0.95f + (float)Math.random() + 0.10f, 0);
		
		shakeValue *= scale;
		
		if(shakeValue > 20)
		{
			shakeValue = 20;
		}
		
		Engine.getSceneManager().getRenderComponents().getMainCamera().doShake(shakeValue, 0.7f);
		return explosionEffect;	
	}
	
	/**
	 * Creates a text effect which pops out of a position and fades away afterwards.
	 * @param font to give the text
	 * @param text is the string to give the text
	 * @param size as fontsize
	 * @param startX where the effect will start
	 * @param startY where the effect will start
	 * @param x where the effect will lerp to
	 * @param y where the effect will lerp to
	 * @return The TextEntity instance which was created to do the effect with.
	 */
	public static TextEntity createTextEffect(FontData font, String text, float size, final float startX, final float startY, float x, float y)
	{
		return createTextEffect(font, text, size, startX, startY, x, y, 0, 0, 0);
	}
	
	/**
	 * Creates a text effect which pops out of a position and fades away afterwards.
	 * @param font to give the text
	 * @param text is the string to give the text
	 * @param size as fontsize
	 * @param startX where the effect will start
	 * @param startY where the effect will start
	 * @param x where the effect will lerp to
	 * @param y where the effect will lerp to
	 * @param offsetX is the forced offset despite the bounds onto the x param
	 * @param offsetY is the forced offset despite the bounds onto the y param
	 * @return The TextEntity instance which was created to do the effect with.
	 */
	public static TextEntity createTextEffect(FontData font, String text, float size, final float startX, final float startY, float x, float y, float offsetX, float offsetY, float waitTillFade)
	{	
		final TextEntity te = new TextEntity(font, text, true);
		
		te.setFontSize(size);
		te.getTransformComponent().setPosition(startX, startY);
		
		if(x < 0 + te.getTextBounds().x / 2)
		{
			x = te.getTextBounds().x / 2;
		}
		else if(x > Engine.getWidth() - te.getTextBounds().x / 2)
		{
			x = Engine.getWidth() - te.getTextBounds().x / 2;
		}
		if(y < 0 + te.getTextBounds().y / 2)
		{
			y = te.getTextBounds().y / 2;
		}
		else if(y > Engine.getHeight() - 80 - te.getTextBounds().y / 2)
		{
			y = Engine.getHeight() - 80 - te.getTextBounds().y / 2;
		}
		
		x += offsetX;
		y += offsetY;
		
		te.getTransformComponent().setScale(new Vector2(0,0));
		te.getTransformComponent().doScale(1, 1, 0.2f, TweenStartType.GameTime);
		te.getRenderComponent().setSortingLayer(-1);
		te.getTransformComponent().doPosition(x, y, 0.4f, TweenStartType.GameTime).ease(EaseType.QuadOut).setCallbackMethod(new IEngineTweenMethod()
		{	
			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				te.getTransformComponent().doPosition(te.getTransformComponent().getPositionX(), te.getTransformComponent().getPositionY() + 35, 0.8f, TweenStartType.GameTime).delay(waitTillFade).ease(EaseType.CubicIn);
				te.getRenderComponent().doAlpha(0.1f, 0.4f, TweenStartType.GameTime).delay(0.4f + waitTillFade).setCallbackMethod(new IEngineTweenMethod()
				{
					@Override
					public void onMethod(int tweenEventType, EngineTween tween) 
					{
						te.destroy();
					}}
				);
			}}
		);
		return te;
	}
}
