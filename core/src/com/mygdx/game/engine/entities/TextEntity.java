package com.mygdx.game.engine.entities;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;

/**
 * This class is an entity which renders Text on the screen.
 * @author Ramses Di Perna
 *
 */
public class TextEntity extends BaseEntity 
{
	private static final int DEFAULT_FONT_SIZE = 12;
	
	private BitmapFont _bitMapFontData = new BitmapFont();
	private String _currentText = "";
	private RenderComponent _renderComponent;
	private Matrix4 _mx4Font = new Matrix4();
	private int _fontSize = DEFAULT_FONT_SIZE;
	
	/**
	 * Creates an empty text with the default font.
	 * @param isUI indicates if this entity should be rendered on the HudCam. If true it will be rendered on the HudCam else on the MainCam
	 */
	public TextEntity(boolean isUI)
	{
		initialize("", isUI);
	}
	/**
	 * Creates a text entity with the given text and default font
	 * @param text to give the text
	 * @param isUI indicates if this entity should be rendered on the HudCam. If true it will be rendered on the HudCam else on the MainCam
	 */
	public TextEntity(String text, boolean isUI)
	{
		initialize(text, isUI);
	}
	
	/**
	 * Creates an empty text with the given font.
	 * @param fontData of the font to give this textEntity
	 * @param isUI indicates if this entity should be rendered on the HudCam. If true it will be rendered on the HudCam else on the MainCam
	 */
	public TextEntity(FontData fontData, boolean isUI)
	{
		initialize("", isUI);
		setFont(fontData);
	}
	
	/**
	 * Creates a text entity with the given text and the given font
	 * @param fontData of the font to give this textEntity
	 * @param text to give the text
	 * @param isUI indicates if this entity should be rendered on the HudCam. If true it will be rendered on the HudCam else on the MainCam
	 */
	public TextEntity(FontData fontData, String text, boolean isUI)
	{
		initialize(text, isUI);
		setFont(fontData);
	}
	
	/**
	 * Sets the font for this TextEntity
	 * @param fontData of the font to give this textEntity
	 */
	public void setFont(FontData fontData)
	{
		_bitMapFontData.dispose();
		_bitMapFontData = new BitmapFont(fontData.getFont(), fontData.getFontPng(), false);
		_bitMapFontData.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	/**
	 * Sets the text for this TextEntity
	 * @param text to display
	 */
	public void setText(String text)
	{
		_currentText = text;
	}
	
	/**
	 * Sets the fontsize for this TextEntity. (Default: 12)
	 * @param size for font to display as.
	 */
	public void setFontSize(int size)
	{
		_fontSize = size;
	}
	
	/**
	 * Returns the RenderComponent for the TextEntity
	 * WARNING: Do not remove the RenderComponent
	 * @return The RenderComponent attached to the TextEntity
	 */
	public RenderComponent getRenderComponent()
	{
		return _renderComponent;
	}
	
	public Vector2 getTextBounds()
	{
		return new Vector2(_bitMapFontData.getBounds(_currentText).width, _bitMapFontData.getBounds(_currentText).height);
	}
	
	@Override
	protected void awake() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{
		
		_mx4Font.setToRotation(new Vector3(0, 0, 1), -this.getTransformComponent().getRotation());
		_mx4Font.trn(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY(), 0);
		renderComponents.getSpriteBatch().setTransformMatrix(_mx4Font);
		_bitMapFontData.setColor(this.getRenderComponent().getColor());
		
		float scaleX = this.getTransformComponent().getScaleX() * ((float)_fontSize / (float)DEFAULT_FONT_SIZE);
		float scaleY = this.getTransformComponent().getScaleY() * ((float)_fontSize / (float)DEFAULT_FONT_SIZE);
		
		if(scaleX <= 0)
			scaleX = 0.1f;
		
		if(scaleY <= 0)
			scaleY = 0.1f;
		
		_bitMapFontData.setScale(scaleX, scaleY);
		
		_bitMapFontData.draw(renderComponents.getSpriteBatch(), 
				_currentText,
				+ (1 -_bitMapFontData.getBounds(_currentText).width * this.getRenderComponent().getPivotX()), 
				+ _bitMapFontData.getBounds(_currentText).height * this.getRenderComponent().getPivotY());
		
		renderComponents.getSpriteBatch().setTransformMatrix(new Matrix4());
	}
	
	

	@Override
	protected void destroyed() 
	{
		_renderComponent = null;
		_bitMapFontData.dispose();
		_bitMapFontData = null;
		_currentText = null;
		_mx4Font = null;
	}
	
	private void initialize(String text, boolean isUI) 
	{
		_renderComponent = this.addComponent(new RenderComponent(null, isUI));
		_bitMapFontData.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setText(text);
	}
}
