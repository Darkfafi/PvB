package com.mygdx.game.gameSpecifics.level;

import java.util.ArrayList;

import com.mygdx.game.gameSpecifics.factories.EnemyFactory;

public class Wave 
{
	private ArrayList<WaveInfo> _waveInfos = new ArrayList<WaveInfo>();
	private WaveSystem _waveSystem;
	
	private float _timeInSection = 0;
	private boolean _isRunningWave = false;
	private int _currentSection = 0;
	
	private int _amountSpawnedThisSection = 0;
	
	public Wave(WaveSystem waveSystem)
	{
		_waveSystem = waveSystem;	
	}
	
	public void addEnemySection(EnemyFactory.EnemyType enemyType, int amount, float timeForSection)
	{
		_waveInfos.add(new WaveInfo(enemyType, amount, timeForSection));
	}
	
	public int getCurrentSection()
	{
		return _currentSection;
	}
	
	public int getTotalSections()
	{
		return _waveInfos.size();
	}
	
	public void startWave()
	{
		if(_waveInfos.size() > 0)
		{
			_isRunningWave = true;
		}
		else
		{
			System.out.println("WARNING: NO WAVE SECTIONS SET FOR THIS WAVE!");
		}
	}
	
	public void updateWave(float dt)
	{
		if(_isRunningWave)
		{
			_timeInSection += dt;
			
			if(_amountSpawnedThisSection < _waveInfos.get(_currentSection).Amount)
			{
				_waveSystem.waveCreateEnemy(this, _waveInfos.get(_currentSection).EnemyType);
				_amountSpawnedThisSection++;
			}
			
			if(_timeInSection >= _waveInfos.get(_currentSection).TimeForSection)
			{
				setNextSection();
			}
		}
	}
	
	public boolean isWaveOver()
	{
		return getCurrentSection() >= getTotalSections();
	}
	
	public void clean()
	{
		_waveInfos.clear();
		_waveInfos = null;
		_waveSystem = null;
	}
	
	private void setNextSection()
	{
		_timeInSection = 0;
		_amountSpawnedThisSection = 0;
		_currentSection++;
		if(isWaveOver())
		{
			_isRunningWave = false;
		}
	}
	
	private class WaveInfo
	{
		public EnemyFactory.EnemyType EnemyType;
		public int Amount;
		public float TimeForSection;
		
		public WaveInfo(EnemyFactory.EnemyType enemyType, int amount, float timeForSection)
		{
			EnemyType = enemyType;
			Amount = amount;
			TimeForSection = timeForSection;
		}
	}
}
