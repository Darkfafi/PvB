package com.mygdx.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.engine.entities.BaseEntity;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.entities.weapons.BaseWeapon;
import com.mygdx.game.entities.weapons.IWeapon;

public class Player extends BaseEntity 
{	
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
		
		_currentWeapon.getTransformComponent().setPosition(new Vector2(Engine.getWidth() / 2, _currentWeapon.getComponent(RenderComponent.class).getRealHeight() / 2 + 20));

	}
	
	public IWeapon getCurrentWeapon()
	{
		return _currentWeapon;
	}
	
	public void giveSpecial()
	{
		System.out.println("TODO: Make current projectile special projectile");
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
	protected void destroyed() {
		// TODO Auto-generated method stub

	}

}
