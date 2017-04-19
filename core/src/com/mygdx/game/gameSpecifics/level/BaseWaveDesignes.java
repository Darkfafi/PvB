package com.mygdx.game.gameSpecifics.level;

public abstract class BaseWaveDesignes 
{
	protected WaveSystem waveSystem;
	
	public BaseWaveDesignes(WaveSystem ws)
	{
		waveSystem = ws;
	}
	
	public abstract Wave getWaveDesign(int currentWave, int waveType);
	
	public void clean()
	{
		waveSystem = null;
	}
}
