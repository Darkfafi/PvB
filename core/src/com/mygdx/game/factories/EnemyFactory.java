package com.mygdx.game.factories;

import static com.mygdx.game.Engine.getTextureResources;

import com.mygdx.game.GameTextureResources;
import com.mygdx.game.components.BaseEnemyAIComponent;
import com.mygdx.game.components.BasicEnemyAIComponent;
import com.mygdx.game.components.attacking.BaseEnemyAttackComponent;
import com.mygdx.game.components.attacking.HeavyBanditAttackComponent;
import com.mygdx.game.components.attacking.LightBanditAttackComponent;
import com.mygdx.game.components.attacking.MediumBanditAttackComponent;
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
		LightBandit,
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
		BaseEnemyAttackComponent attackComponent = getAttackComponentForType(enemyType);
		Enemy enemy = new Enemy(
				getRandomSkinForType(enemyType), 
				getHealthAmountForType(enemyType),
				getScoreAmountForType(enemyType),
				attackComponent
		);
		
		enemy.addComponent(attackComponent);
		
		return enemy;
	}
	
	/**
	 * This creates an instance of the enemy type given with its set AI onto it.
	 * @param enemyType to create
	 * @param playfield to give to the AI component
	 * @return The instance of the given type with a random skin
	 */
	public static Enemy createEnemyOfTypeWithAI(EnemyType enemyType, Playfield playfield)
	{
		Enemy enemy = createEnemyOfType(enemyType);
		enemy.addComponent(createEnemyAIInstance(enemyType, playfield));
		return enemy;
	}
	
	/**
	 * Returns an instance of the AI Component for the given EnemyType
	 * @param enemyType to create AI Component instance for.
	 * @param playfield to give to the AI component
	 * @return The instance of the AI Component for the given EnemyType
	 */
	private static BaseEnemyAIComponent createEnemyAIInstance(EnemyType enemyType, Playfield playfield) 
	{
		float movementSpeed = getMovementSpeedForType(enemyType);
		String[] unwalkableTags = getUnwalkableTagsForType(enemyType);
		switch(enemyType)
		{
		case HeavyBandit:
			return new BasicEnemyAIComponent(
					playfield, 
					movementSpeed,
					unwalkableTags);
		case MediumBandit:
			return new BasicEnemyAIComponent(
					playfield, 
					movementSpeed,
					unwalkableTags);
		case LightBandit:
			return new BasicEnemyAIComponent(
					playfield, 
					movementSpeed,
					unwalkableTags);
		default:
			System.out.println("Type AI Component not set! Please do in the EnemyFactory");
			return null;
		
		}
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
		
		boolean isLoopingAttackAnimation = true;
		
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
			
			animations.setAnimation("charge", getTextureResources().getRenderInfo(GameTextureResources.ANIMATION_HEAVY_BANDIT_0_CHARGE), false);
			break;
		case LightBandit:
			idleKeys = 	new String[]{ 
					GameTextureResources.ANIMATION_LIGHT_BANDIT_0_IDLE	
					};
			runKeys = 	new String[]{ 
					GameTextureResources.ANIMATION_LIGHT_BANDIT_0_RUN	
					};
			deathKeys = new String[]{ 
					GameTextureResources.ANIMATION_LIGHT_BANDIT_0_DEATH
					};
			
			attackKeys = new String[]{
					GameTextureResources.ANIMATION_LIGHT_BANDIT_0_ATTACK
			};
			
			isLoopingAttackAnimation = false;
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
		animations.setAnimation("attack", getTextureResources().getRenderInfo(attackKeys[skinToSelect]), isLoopingAttackAnimation);
		
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
		case LightBandit:
			return 20f;
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
		case LightBandit:
			return 3f;
		case MediumBandit:
			return 2.25f;
		case HeavyBandit:
			return 1f;
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
		case LightBandit:
			return 5;
		case MediumBandit:
			return 10;
		case HeavyBandit:
			return 20;
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
		case LightBandit:
			tags = new String[] {GridTags.OCCUPY_TAG_DAMAGING, GridTags.OCCUPY_TAG_BLOCKED };
			break;
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
	 * Returns an attack component instance which contains all the actions has all the information set needed for attack handling
	 * @param enemyType to get attack component instance for
	 * @return The created AttackComponent instance
	 */
	public static BaseEnemyAttackComponent getAttackComponentForType(EnemyType enemyType)
	{
		switch(enemyType)
		{
		case LightBandit:
			return new LightBanditAttackComponent(10f, 1);
		case MediumBandit:
			return new MediumBanditAttackComponent(1f, 3,0.25f);
		case HeavyBandit:
			return new HeavyBanditAttackComponent(0.5f, 3,0.05f);
		default:
			System.out.println("Type has no defined attack component! Please do in the EnemyFactory");
			return null;
		
		}
	}
}
