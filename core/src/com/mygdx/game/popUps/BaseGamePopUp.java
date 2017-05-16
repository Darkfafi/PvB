package com.mygdx.game.popUps;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.engine.tweening.EngineTween;
import com.mygdx.game.engine.tweening.IEngineTweenMethod;
import com.mygdx.game.popUpSystem.BasePopUp;

/**
 * This is the base class for all the Game Specific pop-ups. This will handle the effects of opening and closing pop-ups.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseGamePopUp extends BasePopUp 
{
	public BaseGamePopUp(boolean isCoverPopUp) 
	{
		super(isCoverPopUp);
	}
	
	/**
	 * Closes the pop-up with an effect. After the effect the pop-up will be destroyed.
	 */
	public void closePopUp()
	{
		this.getTransformComponent().doScale(0, 0, 0.4f, false).ease(EaseType.BackIn).setCallbackMethod(new IEngineTweenMethod() {

			@Override
			public void onMethod(int tweenEventType, EngineTween tween) 
			{
				destroy();
			}});
	}

	/**
	 * Plays an open effect on awake of the pop-up
	 */
	@Override
	protected void onPopUpAwake() 
	{
		this.getTransformComponent().setScale(new Vector2(0, 0));
		this.getTransformComponent().doScale(1, 1, 0.4f, false).ease(EaseType.BackOut);
	}

}
