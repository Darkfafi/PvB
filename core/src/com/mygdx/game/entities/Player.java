package com.mygdx.game.entities;

import com.mygdx.game.engine.Engine;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.weapons.BaseWeapon;

public class Player extends BaseEntity 
{	
	public static final int SPECIAL_ARROW_EXPLOSIVE = 0;
	public static final int SPECIAL_ARROW_FREEZE = 1;
	public static final int SPECIAL_ARROW_POISON = 2;
	
	public static final int[] ALL_SPECIAL_ARROWS = { SPECIAL_ARROW_EXPLOSIVE, SPECIAL_ARROW_FREEZE, SPECIAL_ARROW_POISON };
	
	private BaseWeapon _currentWeapon;
	
	public Player(BaseWeapon startWeapon)
	{
		switchWeapon(startWeapon);
	}
	
	public void switchWeapon(BaseWeapon weapon)
	{
		if(_currentWeapon != null)
			_currentWeapon.clean();
		
		_currentWeapon = weapon;
		
		_currentWeapon.getTransformComponent().setPosition(Engine.getWidth() / 2, _currentWeapon.getComponent(RenderComponent.class).getRealHeight() / 2 + 20);
	}
	
	public BaseWeapon getCurrentWeapon()
	{
		return _currentWeapon;
	}
	
	public void giveSpecial(int specialType)
	{
		_currentWeapon.activateSpecial(specialType);
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
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void destroyed() 
	{
		_currentWeapon = null;

	}

}
