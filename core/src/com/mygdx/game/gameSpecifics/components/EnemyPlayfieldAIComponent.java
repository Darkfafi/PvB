package com.mygdx.game.gameSpecifics.components;

import com.mygdx.game.gameSpecifics.level.Playfield;

public class EnemyPlayfieldAIComponent extends GridUserComponent 
{
	public enum AIState
	{
		Movement,
		Attack
	}
	
	private Playfield _playfield;
	private AIState _currentState = AIState.Movement;
	
	public EnemyPlayfieldAIComponent(Playfield playfield)
	{
		super(playfield.getGrid(), "Enemy", 1, 1);
		_playfield = playfield;
	}

	@Override
	public void updated(float dt)
	{
		if(_currentState == AIState.Movement)
		{
			movementLogics();
		}
		else if(_currentState == AIState.Attack)
		{
			attackLogics();
		}
	}

	@Override
	protected void destroyed() 
	{
		_playfield = null;
		super.destroyed();
	}

	private void movementLogics() 
	{
		
	}
	
	private void attackLogics() 
	{
		// TODO Auto-generated method stub
		
	}
}
