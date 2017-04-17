package com.mygdx.game.gameSpecifics.factories;

import static com.mygdx.game.MyGdxGame.getTextureResources;

import com.mygdx.game.entities.components.Rendering.Animations;
import com.mygdx.game.gameSpecifics.entities.Enemy;

/**
 * This class contains static methods which can be used to create enemies. 
 * The enemies are defined under Types and can have multiple skins under the same type.
 * The stats can be set in this class for the given type.
 * @author Ramses Di Perna
 *
 */
public class EnemyFactory 
{
	/**
	 * The types which define the different types of enemies
	 * @author Ramses Di Perna
	 *
	 */
	public enum EnemyType
	{
		LightBandit
	}
	
	
	/**
	 * This creates an instance of the enemy type given.
	 * @param enemyType to create
	 * @return The instance of the given type with a random skin
	 */
	public static Enemy createEnemyOfType(EnemyType enemyType)
	{
		Enemy enemy = new Enemy(
				getRandomSkinForType(enemyType), 
				getHealthAmountForType(enemyType), 
				getMovementSpeedForType(enemyType));
		
		
		return enemy;
	}
	
	/**
	 * Returns an animations class with All the animations for the given type.
	 * @param enemyType to get animations for
	 * @return The animations for the given enemy Type
	 */
	private static Animations getRandomSkinForType(EnemyType enemyType)
	{
		String[] runKeys;
		Animations animations = new Animations("run", null);
		switch(enemyType)
		{
		case LightBandit:
			runKeys = new String[]{"light_Bandit_0_Run", "light_Bandit_1_Run"};
			break;
		default:
			System.out.println("Type animations not set! Please do in the EnemyFactory");
			runKeys = null;
			break;
		
		}
		int skinToSelect = (int) Math.round(Math.random() * (runKeys.length - 1));
		animations.setAnimation("run", getTextureResources().getRenderInfo(runKeys[skinToSelect]));
		// TODO: Set Death Animation
		
		return animations;
	}
	
	/**
	 * Returns an amount for the given enemy type's health
	 * @param enemyType to get health amount for
	 * @return The health value for the given enemy Type
	 */
	private static float getHealthAmountForType(EnemyType enemyType)
	{
		switch(enemyType)
		{
		case LightBandit:
			return 100f;
		default:
			System.out.println("Type health not set! Please do in the EnemyFactory");
			return 0f;
		}
	}
	
	/**
	 * Returns an amount for the given enemy type's movement speed
	 * @param enemyType to get movement speed amount for
	 * @return The movement speed value for the given enemy Type
	 */
	private static float getMovementSpeedForType(EnemyType enemyType)
	{
		switch(enemyType)
		{
		case LightBandit:
			return 1f;
		default:
			System.out.println("Type health not set! Please do in the EnemyFactory");
			return 0f;
		}
	}
}