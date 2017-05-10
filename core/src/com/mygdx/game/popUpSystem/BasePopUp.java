package com.mygdx.game.popUpSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Engine;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This is the base class for all pop-ups. The pop-up will be rendered above all else.
 * A cover pop-up will also change the touch layer.
 * If a cover pop-up and you want to check for touch, you want to check with the 'getLayer()' method if the touch was in your layer
 * If not a cover pop-up, you want to check if this is the current top pop-up to allow interaction
 * @author Ramses Di Perna
 *
 */
public abstract class BasePopUp extends BaseEntity 
{
	private boolean _isCoverPopUp = false;
	private ShapeRenderer _shapeRenderer;
	private RenderComponent _renderComponent;
	private int _layer = -1;
	private boolean _isTop = false;
	private boolean _isTopCover = false;
	
	/**
	 * This will create a pop-up which work side to side with the PopUpSystem singleton
	 * @param isCoverPopUp means that it will switch the touch layer of the touch channel. It will also render a black alpha cover under the pop up
	 */
	public BasePopUp(boolean isCoverPopUp)
	{
		_isCoverPopUp = isCoverPopUp;
		
		_renderComponent = this.addComponent(new RenderComponent(null, true));
		_renderComponent.setCallsRenderAfterEntityRender(false);
		
		if(_isCoverPopUp)
		{
			_shapeRenderer = new ShapeRenderer();
			_shapeRenderer.setAutoShapeType(true);
		}
		
		PopUpSystem.getInstance().registerPopUp(this);
		

		onPopUpAwake();
	}
	
	/**
	 * Returns whether this pop up is a cover pop up, when true, this pop-up also renders a black alpha cover under the pop up when it is the top cover pop up
	 */
	public boolean isCoverPopUp()
	{
		return _isCoverPopUp;
	}
	
	/**
	 * Returns whether this pop up is the current top pop up of all pop ups which are active on the screen.
	 * @return True when its the top pop-up, else false.
	 */
	public boolean isTopPopUp()
	{
		return _isTop;
	}
	
	/**
	 * Returns whether this pop up is the top of all cover pop ups. Will return false also when its not a cover pop up
	 * @return True when its the top Cover pop up, else false
	 */
	public boolean isTopCoverPopUp()
	{
		return _isTopCover;
	}
	
	/**
	 * Warning: This method may only be called by the PopUpSystem
	 * This method will set this pop-up to the top pop up
	 * @param isTop indicates if this popUp is the current top pop-up
	 */
	public void system_setTopPopUp(boolean isTop)
	{
		_isTop = isTop;
	}
	
	/**
	 * Warning: This method may only be called by the PopUpSystem
	 * This method will set this pop-up to the top cover pop up
	 * @param isTopCover indicates if this popUp is the current highest cover pop-up on the stack
	 */
	public void system_setTopCoverPopUp(boolean isTopCover)
	{
		_isTopCover = isTopCover;
	}
	
	/**
	 * Warning: This method may only be called by the PopUpSystem
	 * This will set the layer in which the pop-up is currently in.
	 * @param layer which represents the touch layer of the pop-up
	 */
	public void system_setLayer(int layer)
	{
		if(_layer != -1) { return; }
		_layer = layer;
	}
	
	/**
	 * Returns the touch layer which this pop-up is in.
	 * @return touch layer index
	 */
	public int getLayer()
	{
		return _layer;
	}
	
	public RenderComponent getRenderComponent()
	{
		return _renderComponent;
	}
	
	@Override
	protected void awake() 
	{
		
	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		if(_shapeRenderer == null || !_isTopCover) { return; }
		renderComponents.getSpriteBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		_shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		_shapeRenderer.setColor(0, 0, 0, 0.65f);
		_shapeRenderer.rect(0, 0, Engine.getWidth(), Engine.getHeight());
		_shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
		renderComponents.getSpriteBatch().begin();
	}

	@Override
	protected void destroyed() 
	{
		onPopUpDestroyed();
		if(_shapeRenderer != null)
		{
			_shapeRenderer.dispose();
			_shapeRenderer = null;
		}
		
		_renderComponent = null;
		
		PopUpSystem.getInstance().unregisterPopUp(this);
	}

	protected abstract void onPopUpDestroyed();
	protected abstract void onPopUpAwake();
}
