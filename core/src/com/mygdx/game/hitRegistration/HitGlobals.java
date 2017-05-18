package com.mygdx.game.hitRegistration;

/**
 * This class contains all the Tool indexes and Type indexes needed for hit indication registration at the HitRegistationPoint
 * @author Ramses Di Perna
 *
 */
public class HitGlobals 
{
	// Tool Types
	public static final int TOOL_ARROW = 0;
	public static final int TOOL_TRAP = 1;
	public static final int TOOL_SPECIAL_ARROW = 2;
	
	// Hit Types
	public static final int TYPE_CONSECUTIVE_HIT_TRACKING = -1; // Means this hit wants to be checked for consecutive hit tracking
	public static final int TYPE_DIRECT_HIT = 0;
	public static final int TYPE_EXPLOSION_HIT = 1;
	
	public static boolean isHitType(int[] hitTypes, int hitType)
	{
		for(int i = 0; i < hitTypes.length; i++)
		{
			if(hitTypes[i] == hitType)
				return true;
		}
		return false;
	}
}
