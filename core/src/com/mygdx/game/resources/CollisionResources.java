package com.mygdx.game.resources;

/**
 * All resources for Collision handling. This contains how any Pixel Per Meters there are for the playfield. 
 * And constant bits for collision usage.
 * @author Djamali
 *
 */
public class CollisionResources {

	//Pixel Per Meter Ratio
	public static final float PIXEL_PER_METER = 100;
	
	//constant bits
	public static final short BIT_FORTRESS = 2;
	public static final short BIT_TRAP = 4;
	public static final short BIT_ARROW = 8;
	public static final short BIT_ENEMY = 16;
}