package com.mygdx.game.engine.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.components.rendering.AnimationComponent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This class is a train entity which generates carts when so desired.
 * @author Ramses Di Perna
 *
 */
public class Train extends BaseEntity 
{	
	private int _cartAmount;
	
	private Texture _trainBaseTexture;
	private Texture _cartBaseTexture;
	
	private boolean _xFlipped = false;
	
	/**
	 * This creates a train instance and sets the amount of carts which should be generated behind.
	 * @param cartAmount to generate behind the train
	 * @param flipX means, if true, that the train faces left, else if false, that the train faces right
	 */
	public Train(int cartAmount, boolean flipX)
	{
		_xFlipped = flipX;
		_cartAmount = cartAmount;
		_cartAmount = (_cartAmount < 0) ? 0 : _cartAmount;
		
		Animations anims = new Animations("drive" ,Engine.getTextureResources().getRenderInfo(GameTextureResources.ANIMATIO_TRAIN_WHEELS), true);
		this.addComponent(new AnimationComponent(anims, true, false)).setFlipX(flipX);
		this.getComponent(AnimationComponent.class).setSortingLayer(1);
		this.getComponent(AnimationComponent.class).setPivot(new Vector2(0.5f, 0), false);
		this.getComponent(AnimationComponent.class).setSortOnY(true);
	}
	
	@Override
	protected void awake() 
	{
		_trainBaseTexture = Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_TRAIN_BASE).getTextureToDraw();
		_cartBaseTexture = Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_CART_BASE).getTextureToDraw();
	}

	@Override
	protected void updated(float dt) 
	{

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		renderTrainBase(renderComponents);
		renderCartBases(renderComponents);
	}
	
	/**
	 * Renders the given amount of carts in the constructor behind the train
	 * @param renderComponents
	 */
	private void renderCartBases(RenderComponents renderComponents) 
	{
		Vector2 trainSize = getScaledTextureSize(_trainBaseTexture);
		Vector2 cartSize = getScaledTextureSize(_cartBaseTexture);
		
		float offsetX = (trainSize.x);
		
		if(_xFlipped)
			offsetX -=  cartSize.x * 1.182f;
		
		float offsetY = 0;
		
		for(int i = 0; i < _cartAmount; i++)
		{
			float offsetAdd = ((cartSize.x / 4.2f) * (i + 1)) + ((cartSize.x / 2) * i);
			offsetX += (_xFlipped) ? -offsetAdd : offsetAdd;
			
			renderComponents.getSpriteBatch().draw(_cartBaseTexture, this.getTransformComponent().getPositionX() - offsetX, this.getTransformComponent().getPositionY() - offsetY,
					cartSize.x, cartSize.y, 0, 0, _cartBaseTexture.getWidth(), _cartBaseTexture.getHeight(), _xFlipped, false);
		}
	}

	/**
	 * Renders the base of the train around the wheels
	 * @param renderComponents to render with
	 */
	private void renderTrainBase(RenderComponents renderComponents)
	{
		Vector2 trainSize = getScaledTextureSize(_trainBaseTexture);
		
		float offsetX =  trainSize.x / 2.897f;
		
		if(_xFlipped)
			offsetX +=  trainSize.x / 3.235f;
		
		float offsetY = trainSize.y * 0.01f;
		
		renderComponents.getSpriteBatch().draw(_trainBaseTexture, this.getTransformComponent().getPositionX() - offsetX, this.getTransformComponent().getPositionY() - offsetY,
				trainSize.x, trainSize.y, 0, 0, _trainBaseTexture.getWidth(), _trainBaseTexture.getHeight(), _xFlipped, false);
	}
	
	private Vector2 getScaledTextureSize(Texture t)
	{
		return new Vector2(t.getWidth() * this.getTransformComponent().getScaleX(), t.getHeight() * this.getTransformComponent().getScaleY());
	}
	
	@Override
	protected void destroyed() {
		// TODO Auto-generated method stub

	}

}
