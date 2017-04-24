package com.mygdx.game.engine.tweening;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;

import aurelienribon.tweenengine.TweenAccessor;

public class RenderAccessor implements TweenAccessor<RenderComponent>
{
	public static final int COLOR = 0;
	public static final int ALPHA = 1;
	
	
	@Override
	public int getValues(RenderComponent target, int tweenType, float[] returnValues) 
	{
		switch(tweenType)
		{
		case COLOR:
			Color c = target.getColor();
			returnValues[0] = c.r;
			returnValues[1] = c.g;
			returnValues[2] = c.b;
			returnValues[3] = c.a;
			return 4;
			
		case ALPHA:
			returnValues[0] = target.getAlpha();
			return 1;
			
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(RenderComponent target, int tweenType, float[] newValues) 
	{
		switch(tweenType)
		{
		case COLOR:
			target.setColor(new Color(newValues[0], newValues[1], newValues[2], newValues[3]));
			break;
		case ALPHA:
			target.setAlpha(newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
