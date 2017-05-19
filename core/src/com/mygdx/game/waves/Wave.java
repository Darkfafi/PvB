package com.mygdx.game.waves;

import java.util.ArrayList;

import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.engine.events.Event;
import com.mygdx.game.engine.events.IEventReceiver;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.events.HealthEvent;
import com.mygdx.game.factories.EnemyFactory;

/**
 * A Wave is something, to design and create wave patterns. 
 * These patterns are created by so called 'sections'
 * Before a wave is started by the 'startWave' method, it should be designed using the 'addSection' method
 * NOTE: A wave instance should be updated by calling the 'updateWave' method every frame!
 * @author Ramses Di Perna
 *
 */
public class Wave implements IEventReceiver
{
	private ArrayList<WaveSectionInfo> _waveInfos = new ArrayList<WaveSectionInfo>();
	private WaveSystem _waveSystem;
	
	private float _timeInSection = 0;
	private boolean _isRunningWave = false;
	private int _currentSection = 0;
	
	private int _amountSpawnedThisSection = 0;
	
	private boolean _isEnemyKillToEndWave = false;
	private ArrayList<Enemy> _enemiesTracking = new ArrayList<Enemy>();
	
	
	/**
	 * A wave should be linked to a waveSystem to be able to create enemies.
	 * @param waveSystem to create enemies with
	 * @param isEnemyKillToEndWave means, when set on true, the wave will not end until all enemies are killed else it will end by the time given.
	 */
	public Wave(WaveSystem waveSystem, boolean isEnemyKillToEndWave)
	{
		_waveSystem = waveSystem;
		_isEnemyKillToEndWave = isEnemyKillToEndWave;
	}
	
	/**
	 * This method designs a wave by sections, every section contains an enemyType to spawn, the amount to spawn and the time to clear the section before the other starts.
	 * When all wave sections ran out of time, the wave will end if isEnemyKillToEndWave is not set to true, else it will end when all enemies are killed. The last section timeForSection will not be used.
	 * @param enemyType to spawn in this section
	 * @param amount of enemies of given type to spawn
	 * @param timeForSection is the time in seconds before the other section starts.
	 */
	public void addSection(EnemyFactory.EnemyType enemyType, int amount, float timeForSection)
	{
		_waveInfos.add(new WaveSectionInfo(enemyType, amount, timeForSection));
	}
	
	/**
	 * Returns the index of the current section
	 * @return Index of current section
	 */
	public int getCurrentSection()
	{
		return _currentSection;
	}
	
	/**
	 * Returns the total amount of sections in this wave
	 * @return Lengths of the list of all sections given
	 */
	public int getTotalSections()
	{
		return _waveInfos.size();
	}
	
	/**
	 * Starts the wave. 
	 * WARNING: This has no effect if the 'updateWave' is not called every frame!
	 */
	public void startWave()
	{
		if(_waveInfos.size() > 0)
		{
			_timeInSection = 0;
			_currentSection = 0;
			_amountSpawnedThisSection = 0;
			clearTrackingEnemies();
			_isRunningWave = true;
		}
		else
		{
			System.out.println("WARNING: NO WAVE SECTIONS SET FOR THIS WAVE!");
		}
	}
	
	/**
	 * Updates the Wave so it can perform the logics it is supposed to do. This should be called every frame in order to work
	 * @param dt is the delta which has passed between frames (Delta Time)
	 */
	public void updateWave(float dt)
	{
		if(_isRunningWave)
		{
			_timeInSection += dt;
			if(!hasSpawnedEntireSection())
			{
				Enemy e = _waveSystem.waveCreateEnemy(this, _waveInfos.get(_currentSection).EnemyType);
				_amountSpawnedThisSection++;
				
				_enemiesTracking.add(e);
				e.getComponent(HealthComponent.class).addEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
				
			}
			
			if(_timeInSection >= _waveInfos.get(_currentSection).TimeForSection || (this.hasSpawnedEntireSection() && this._enemiesTracking.size() == 0))
			{
				setNextSection();
			}
		}
	}
	
	/**
	 * This returns whether the wave has run out of sections to spawn
	 * @return If the wave is out of sections to spawn, true if it is and false if it is still actively working with its sections
	 */
	public boolean isWaveOver()
	{
		if(_isEnemyKillToEndWave)
		{
			return getCurrentSection() >= getTotalSections() - 1 &&  this._enemiesTracking.size() == 0 && hasSpawnedEntireSection();
		}
		else
		{
			return getCurrentSection() >= getTotalSections();
		}
	}
	
	/**
	 * Cleans the wave from all data it was using, this should be called only when the wave is being prepared for garbage collection
	 */
	public void clean()
	{
		_waveInfos.clear();
		_waveInfos = null;
		_waveSystem = null;
		
		clearTrackingEnemies();
		
		_enemiesTracking = null;
	}
	
	private void clearTrackingEnemies()
	{
		for(int i = _enemiesTracking.size() - 1; i >= 0; i--)
		{
			removeFromTrack(_enemiesTracking.get(i));
		}
		
		_enemiesTracking.clear();
	}
	
	/**
	 * This sets the next section to spawn and resets all tracker data to track the new sections time and spawn amount.
	 */
	private void setNextSection()
	{
		_timeInSection = 0;
		_amountSpawnedThisSection = 0;
		_currentSection++;
		if(getCurrentSection() >= getTotalSections())
		{
			_isRunningWave = false;
		}
	}
	
	/**
	 * Contains the information for a wave section to be spawned.
	 * The Wave class uses this for its tracking of WaveSection data given with the 'addSection' method
	 * @author Ramses Di Perna
	 *
	 */
	private class WaveSectionInfo
	{
		public EnemyFactory.EnemyType EnemyType;
		public int Amount;
		public float TimeForSection;
		
		/**
		 * This holds the information to spawn a section for a wave.
		 * @param enemyType to spawn in this section
		 * @param amount of enemies of given type to spawn
		 * @param timeForSection is the time in seconds before the other section starts.
		 */
		public WaveSectionInfo(EnemyFactory.EnemyType enemyType, int amount, float timeForSection)
		{
			EnemyType = enemyType;
			Amount = amount;
			TimeForSection = timeForSection;
		}
	}

	@Override
	public void onReceiveEvent(Event event) 
	{	
		if(event.getType() == HealthComponent.EVENT_HEALTH_DIED)
		{
			onEnemyDiedEvent((HealthEvent)event);
		}
	}

	public boolean hasSpawnedEntireSection()
	{
		if(getCurrentSection() >= getTotalSections()) { return true; }
		return _amountSpawnedThisSection >= _waveInfos.get(_currentSection).Amount;
	}
	
	/**
	 * This is triggered when a health died event is triggered by a tracked enemy
	 * @param event triggered by a tracked enemy
	 */
	private void onEnemyDiedEvent(HealthEvent event) 
	{
		HealthComponent hc = (HealthComponent)event.getDispatcher();
		removeFromTrack((Enemy)hc.getParentOfComponent());
	}
	
	/**
	 * Removes the given enemy from the current tracking enemies
	 * @param enemy to remove and unlisten.
	 */
	private void removeFromTrack(Enemy enemy)
	{
		enemy.getComponent(HealthComponent.class).removeEventListener(HealthComponent.EVENT_HEALTH_DIED, this);
		if(_enemiesTracking.contains(enemy))
		{
			_enemiesTracking.remove(enemy);
		}
	}
}
