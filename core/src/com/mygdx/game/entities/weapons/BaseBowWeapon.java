package com.mygdx.game.entities.weapons;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.entities.components.rendering.RenderInfo;
import com.mygdx.game.entities.weapons.projectiles.BaseProjectile;
import com.mygdx.game.entities.weapons.projectiles.ExplosiveProjectile;

/**
 * This is the base class for all the Bow like weapons.
 * @author Ramses Di Perna
 *
 */
public abstract class BaseBowWeapon extends BaseWeapon
{
	public enum BowStage
	{
		Idle,		// Waiting for target location to fire at
		Draw  		// Preparing and Doing the Shoot method
	}
	
	public static final float MAX_DRAW_STRENGTH = 30f;
	public static final float MAX_DRAW_LENGTH = 200f;
	
	private BowStage _currentBowStage = BowStage.Idle; 	// The current stage of the bow
	private float _drawStrength = 0f;					// The draw strength to release the arrow with
	private Vector2 _targetLocation = null; 			// The target location to fire at / where the finger started
	private float _aimAngle = 0f;
	private float _radiusToTargetLoc = 0f;
	private BaseProjectile _currentProjectile;
	
	/**
	 * In order to work, the bow needs a RenderInfo which contain different frames of "Pulling the string"
	 * @param renderInfoBow Of a bow with all the frames of pulling the string
	 */
	public BaseBowWeapon(RenderInfo renderInfoBow)
	{
		this.addComponent(new RenderComponent(renderInfoBow, false)).setSortingLayer(2);
		this.setBowIdle();
	}
	
	@Override
	public WeaponType getWeaponType() 
	{
		return WeaponType.BowType;
	}

	@Override
	public void startControl(int x, int y) 
	{
		selectTarget(x, y);
	}

	@Override
	public void inControl(int x, int y) 
	{
		drawMechanic(x, y);
	}

	@Override
	public void endControl(int x, int y) 
	{
		shoot(_drawStrength, 0.1f);
	}
	
	@Override
	public void clean()
	{
		super.clean();
		this.destroy();
	}
	
	/**
	 * Returns the current stage the bow is in.
	 * @return The current BowStage
	 */
	public BowStage getCurrentBowStage()
	{
		return _currentBowStage;
	}
	
	/**
	 * Returns the current loaded projectile instance on the bow
	 * @return The ArrowProjectile instance loaded on the bow.
	 */
	public BaseProjectile getCurrentProjectile()
	{
		return _currentProjectile;
	}
	
	/**
	 * Returns the current draw strength value of the bow. (Normalized value, 0 being weakest and 1 being the strongest)
	 * @return Draw strength value normalized
	 */
	public float getCurrentDrawStrength()
	{
		return _drawStrength;
	}

	@Override
	protected void updated(float dt) 
	{
		if(_currentBowStage == BowStage.Draw)
		{
			this.getTransformComponent().setRotation(_aimAngle);
		}
		
		handleProjectilePlacement();
	}
	
	@Override
	protected void destroyed() 
	{
		_targetLocation = null;
		_currentProjectile.destroy();
		_currentProjectile = null;
	}
	
	/**
	 * Returns the distance the bow can shoot in pixels with its draw strength.
	 * @return Max distance in pixels
	 */
	protected float powerToDistancePower()
	{
		return (float) Math.pow(MAX_DRAW_STRENGTH, 2);
	}
	
	public void activateSpecial(int specialType) 
	{
		if(_currentProjectile != null)
			this._currentProjectile.destroy();
		
		_currentProjectile = this.createRandomSpecialProjectile(specialType);
	}
	
	protected abstract BaseProjectile createRandomSpecialProjectile(int specialType);
	
	/**
	 * Called when a target location is selected to start the aim at
	 * @param x indicates the x position of the selected location to start the aim
	 * @param y indicates the y position of the selected location to start the aim
	 */
	protected abstract void selectedTarget(int x, int y);
	/**
	 * Called every frame when the bow is in its drawing stage
	 * @param x location which the current aim is pointed at
	 * @param y location which the current aim is pointed at
	 */
	protected abstract void drawingBow(int x, int y);
	/**
	 * Called when the bow has shot an ArrowProjectile
	 * @param strengthPercentage is the normalized strength used to fire the projectile
	 * @param minimum is the minimum percentage which the strengthPercentage should be clamped to
	 */
	protected abstract void shotBow(float strengthPercentage, float minimum);
	/**
	 * Called when the bow has been switched in stage
	 * @param stage it has been switched to
	 */
	protected abstract void bowSetToStage(BowStage stage);
	/**
	 * Called to position the projectile on the bow. 
	 * Indicates the distance the projectile should be positioned in delta from its original position. (To match the bow draw animation)
	 * @return The Delta vector in which it should differ the position
	 */
	protected abstract Vector2 projectilePullDistance();
	/**
	 * Called when the bow needs a new ArrowProjectile.
	 * @return ArrowProjectile to load on the bow as new current projectile
	 */
	protected abstract BaseProjectile getProjectileInstance();
	
	/**
	 * Selects a start touch position before draw. This will be used as the start position of the draw value.
	 * @param posX is the x position of the position value.
	 * @param posY is the y position o the position value.
	 */
	private void selectTarget(int posX, int posY) 
	{
		_targetLocation = new Vector2(posX, posY);
		_radiusToTargetLoc = Vector2.dst(_targetLocation.x, _targetLocation.y, this.getTransformComponent().getPositionX(), 0);
		_currentBowStage = BowStage.Draw;
		
		selectedTarget(posX, posY);
		bowSetToStage(_currentBowStage);
	}
	
	/**
	 * Handles the complete draw mechanic of the bow.
	 * @param posX is the x position of the touch which is controlling the draw
	 * @param posY is the y position of the touch which is controlling the draw
	 */
	private void drawMechanic(int posX, int posY) 
	{	
		Vector2 lineToTouch = new Vector2(posX - this.getTransformComponent().getPositionX(), posY);
		Vector2 drawTouch = new Vector2(lineToTouch.x, lineToTouch.y);
		
		Vector2 lineToRealTarget = new Vector2(drawTouch.x, drawTouch.y);
		lineToRealTarget.setLength(_radiusToTargetLoc);
		Vector2 lineToRealMaxDraw = new Vector2(lineToRealTarget.x, lineToRealTarget.y);
		lineToRealMaxDraw.setLength(_radiusToTargetLoc - MAX_DRAW_LENGTH);
		
		_aimAngle = lineToTouch.angle() - 90;
		_aimAngle = 360 - _aimAngle;
		
		_drawStrength = 1 - (drawTouch.len() - lineToRealMaxDraw.len()) / (lineToRealTarget.len() - lineToRealMaxDraw.len());
		
		if(_drawStrength > 1)
		{
			_drawStrength = 1;
		}
		else if(_drawStrength < 0)
		{
			_drawStrength = 0;
		}
		RenderComponent rc = this.getComponent(RenderComponent.class);
		rc.setCurrentFrameInfo((int)((rc.getRenderInfo().getFramesLength() - 1) * _drawStrength));
		handleProjectilePlacement();
		
		drawingBow(posX, posY);
		
	}
	
	/**
	 * Fires the current arrow from the bow with the strength given.
	 * @param strengthPercentage makes the arrow shoot in distance of powerToDistancePower() * the value and the strength of MAX_DRAW_STRENGTH * the value
	 * @param minimum is the minimum the shooting strength should be. If its below that it will be put onto the minimum value
	 */
	private void shoot(float strengthPercentage, float minimum) 
	{
		if(strengthPercentage < minimum)
			strengthPercentage = minimum;
		
		_currentProjectile.fire((powerToDistancePower() * strengthPercentage), MAX_DRAW_STRENGTH * strengthPercentage);
		shotBow(strengthPercentage, minimum);

		setBowIdle();
	}
	
	/**
	 * Sets the bow back to its idle state
	 */
	private void setBowIdle()
	{
		_currentBowStage = BowStage.Idle;
		_drawStrength = 0;
		this.getComponent(RenderComponent.class).setCurrentFrameInfo(0); // Reset bow
		_currentProjectile = getProjectileInstance();
		bowSetToStage(_currentBowStage);
	}
	
	/**
	 * Places the arrow in the correct orientation on the bow.
	 * This orientation includes position and rotation.
	 * It gets the position form the 'projectilePullDistance' method 
	 */
	private void handleProjectilePlacement() 
	{
		if(_currentProjectile == null || _currentProjectile.getTransformComponent() == null) { return; }
		_currentProjectile.getTransformComponent().setPosition(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY());
		_currentProjectile.getTransformComponent().setRotation(this.getTransformComponent().getRotation());
		_currentProjectile.getTransformComponent().translatePosition(projectilePullDistance().x, projectilePullDistance().y);
	}
}
