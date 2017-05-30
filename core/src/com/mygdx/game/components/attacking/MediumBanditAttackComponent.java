package com.mygdx.game.components.attacking;

import com.mygdx.game.components.HealthComponent;

/**
 * The attack logics of the Medium bandit (Dual Revolver attacking bandit)
 * @author Ramses Di Perna
 *
 */
public class MediumBanditAttackComponent extends BaseEnemyAttackComponent 
{
	private float _dmgDelayTime;
	private float _timePassedUntilDmg = 0;
	private HealthComponent _healthHitting;
	
	public MediumBanditAttackComponent(float dmg, int rangeInTiles, float attackDelayTime) 
	{
		super(dmg, rangeInTiles);
		_dmgDelayTime = attackDelayTime;
	}


	@Override
	public boolean isAttacking()
	{
		return _healthHitting != null;
	}

	@Override
	protected void attackCall(HealthComponent hc) 
	{
		if(hc.isAlive())
			this.getAnimationComponent().setCurrentAnimation("attack", true);
		
		_healthHitting = hc;
	}

	@Override
	protected void stopAttackingCall() 
	{
		_healthHitting = null;
	}
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updated(float deltaTime) 
	{
		if(!this.isActive()) { return; }
		if(_healthHitting != null)
		{
			_timePassedUntilDmg += deltaTime;
			if(_timePassedUntilDmg >= _dmgDelayTime)
			{
				_timePassedUntilDmg = 0;

				if(	this._healthHitting == null 
					|| 	this._healthHitting.isDestroyed()
					|| !this._healthHitting.isAlive()) 
				{
					this.stopAttacking();
					return;
				}
				this._healthHitting.damage(this.getDamage());
				
				if(!this._healthHitting.isAlive())
				{
					this.stopAttacking();
					return;
				}
			}
		}
	}

	@Override
	protected void activeStateChanged() {
		// TODO Auto-generated method stub
		
	}

}
