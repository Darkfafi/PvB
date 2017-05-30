package com.mygdx.game.engine.resources;

import com.badlogic.gdx.math.Vector2;

/**
 * All resources for Collision handling. This contains how any Pixel Per Meters there are for the playfield. 
 * And constant bits for collision usage.
 * @author Djamali Jones
 *
 */
public class CollisionResources {

	//Pixel Per Meter Ratio
	public static final float PIXEL_PER_METER = 100f;
	
	//constant bits
	public static final short BIT_FORTRESS = 2;
	public static final short BIT_TRAP_ACTIVATOR = 4;
	public static final short BIT_ARROW = 8;
	public static final short BIT_ENEMY = 16;
	
	public static float convertToPPM(float nrInPixels)
	{
		return nrInPixels / PIXEL_PER_METER;
	}
	
	public static Vector2 convertToPPM(Vector2 nrInPixels)
	{
		return new Vector2(convertToPPM(nrInPixels.x), convertToPPM(nrInPixels.y));
	}
	
	public static float convertFromPPM(float nrInMeters)
	{
		return nrInMeters * PIXEL_PER_METER;
	}
	
	public static Vector2 convertFromPPM(Vector2 nrInMeters)
	{
		return new Vector2(convertFromPPM(nrInMeters.x), convertFromPPM(nrInMeters.y));
	}
}