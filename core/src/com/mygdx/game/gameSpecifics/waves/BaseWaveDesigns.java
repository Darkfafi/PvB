package com.mygdx.game.gameSpecifics.waves;

/**
 * This is the base class for wave designs. The WaveSystem uses this to ask for the next wave to spawn.
 * This class will design and return the correct wave with the given information
 * You can request for a new wave by calling the 'getWaveDesign' method
 * @author Ramses Di Perna
 *
 */
public abstract class BaseWaveDesigns 
{	
	/**
	 * Returns a new designed wave with the given information the WaveSystem has provided it with.
	 * @param waveSystem to give the wave as reference to spawn enemies
	 * @param currentWave is an index which represents what the current wave amount is. This will influence the wave returned
	 * @param waveType represents a custom usable integer to communicate 'this is a tutorial wave' and stuff like that
	 * @return The Wave instance which is designed for the new wave request.
	 */
	public abstract Wave getWaveDesign(WaveSystem waveSystem, int currentWave, int waveType);
}
