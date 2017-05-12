package com.mygdx.game.factories;

import static com.mygdx.game.Engine.getTextureResources;

import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.BasicEnemyAIComponent;
import com.mygdx.game.engine.entities.components.rendering.Animations;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Playfield;

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
		MediumBandit,
		HeavyBandit
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
				getScoreAmountForType(enemyType),
				getDamageAmountForType(enemyType),
				getDamageRateForType(enemyType)
		);
					
		
		return enemy;
	}
	
	/**
	 * This creates an instance of the enemy type given with its set AI onto it.
	 * @param enemyType to create
	 * @return The instance of the given type with a random skin
	 */
	public static Enemy createEnemyOfTypeWithAI(EnemyType enemyType, Playfield playfield)
	{
		Enemy enemy = createEnemyOfType(enemyType);
		
		enemy.addComponent(new BasicEnemyAIComponent(
				playfield, 
				getMovementSpeedForType(enemyType),
				getUnwalkableTagsForType(enemyType))
		);
		
		return enemy;
	}
	
	/**
	 * Returns an animations class with All the animations for the given type.
	 * @param enemyType to get animations for
	 * @return The animations for the given enemy Type
	 */
	private static Animations getRandomSkinForType(EnemyType enemyType)
	{
		String[] idleKeys;
		String[] runKeys;
		String[] deathKeys;
		String[] attackKeys;
		
		Animations animations = new Animations("run", null, true);
		switch(enemyType)
		{
		case MediumBandit:
			idleKeys = 	new String[]{ 
					GameTextureResources.ANIMATION_MEDIUM_BANDIT_0_IDLE, 
					GameTextureResources.ANIMATION_MEDIUM_BANDIT_1_IDLE 	};
			runKeys = 	new String[]{ 
					GameTextureResources.ANIMATION_MEDIUM_BANDIT_0_RUN, 
					GameTextureResources.ANIMATION_MEDIUM_BANDIT_1_RUN 	};
			deathKeys = new String[]{ 
					GameTextureResources.ANIMATION_MEDIUM_BANDIT_0_DEATH, 
					GameTextureResources.ANIMATION_MEDIUM_BANDIT_1_DEATH };
			
			attackKeys = new String[]{
					GameTextureResources.ANIMATION_MEDIUM_BANDIT_0_ATTACK,
					GameTextureResources.ANIMATION_MEDIUM_BANDIT_1_ATTACK
			};
			break;
		case HeavyBandit:
			idleKeys = 	new String[]{ 
					GameTextureResources.ANIMATION_HEAVY_BANDIT_0_IDLE	
					};
			runKeys = 	new String[]{ 
					GameTextureResources.ANIMATION_HEAVY_BANDIT_0_RUN	
					};
			deathKeys = new String[]{ 
					GameTextureResources.ANIMATION_HEAVY_BANDIT_0_DEATH
					};
			
			attackKeys = new String[]{
					GameTextureResources.ANIMATION_HEAVY_BANDIT_0_ATTACK
			};
			break;
		default:
			System.out.println("Type animations not set! Please do in the EnemyFactory");
			idleKeys = 	null;
			runKeys = 	null;
			deathKeys = null;
			attackKeys = null;
			break;
		
		}
		int skinToSelect = (int) Math.round(Math.random() * (runKeys.length - 1));
		animations.setAnimation("idle", getTextureResources().getRenderInfo(idleKeys[skinToSelect]), true);
		animations.setAnimation("run", getTextureResources().getRenderInfo(runKeys[skinToSelect]), true);
		animations.setAnimation("death", getTextureResources().getRenderInfo(deathKeys[skinToSelect]), false);
		animations.setAnimation("attack", getTextureResources().getRenderInfo(attackKeys[skinToSelect]), true);
		
		return animations;
	}
	
	/**
	 * Returns an amount for the given enemy type's health
	 * @param enemyType to get health amount for
	 * @return The health value for the given enemy Type
	 */
	public static float getHealthAmountForType(EnemyType enemyType)
	{
		switch(enemyType)
		{
		case MediumBandit:
			return 25f;
		case HeavyBandit:
			return 200;
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
	public static float getMovementSpeedForType(EnemyType enemyType)
	{
		switch(enemyType)
		{
		case MediumBandit:
			return 2f;
		case HeavyBandit:
			return 0.85f;
		default:
			System.out.println("Type health not set! Please do in the EnemyFactory");
			return 0f;
		}
	}
	
	/**
	 * Returns an amount for the given enemy type's score
	 * @param enemyType to get score amount for
	 * @return The score value for the given enemy Type
	 */
	public static int getScoreAmountForType(EnemyType enemyType)
	{
		switch(enemyType)
		{
		case MediumBandit:
			return 50;
		case HeavyBandit:
			return 200;
		default:
			System.out.println("Type score not set! Please do in the EnemyFactory");
			return 0;
		}
	}
	
	/**
	 * Returns the unwalkable tiles for the given enemy type
	 * @param enemyType to get the unwalkable tile tags for
	 * @return GridOccupyTags of the unwalkable tiles for this enemy 
	 */
	public static String[] getUnwalkableTagsForType(EnemyType enemyType)
	{
		String[] tags;
		switch(enemyType)
		{
		case MediumBandit:
			tags = new String[] {GridTags.OCCUPY_TAG_DAMAGING, GridTags.OCCUPY_TAG_ENEMY, GridTags.OCCUPY_TAG_BLOCKED };
			break;
		case HeavyBandit:
			tags = new String[] {GridTags.OCCUPY_TAG_DAMAGING, GridTags.OCCUPY_TAG_ENEMY, GridTags.OCCUPY_TAG_BLOCKED };
			break;
		default:
			System.out.println("Type unwalkable tags not set! Please do in the EnemyFactory");
			tags = null;
			break;
		
		}
		return tags;
	}
	
	/**
	 * Returns the damage amount this enemy does with its attacks
	 * @param enemyType to get damage amount from
	 * @return The amount of damage this enemy does every damage tik.
	 */
	public static float getDamageAmountForType(EnemyType enemyType)
	{
		switch(enemyType)
		{
		case MediumBandit:
			return 1f;
		case HeavyBandit:
			return 0.5f;
		default:
			System.out.println("Type damage amount not set! Please do in the EnemyFactory");
			return 0f;
		
		}
	}
	
	/**
	 * Returns the frequency this enemy type does damage in seconds (dmg : DamageRate).
	 * @param enemyType to get damage rate from
	 * @return The frequency of damage this EnemyType does
	 */
	public static float getDamageRateForType(EnemyType enemyType)
	{
		switch(enemyType)
		{
		case MediumBandit:
			return 0.25f;
		case HeavyBandit:
			return 0.05f;
		default:
			System.out.println("Type damage rate not set! Please do in the EnemyFactory");
			return 0f;
		
		}
	}
}
