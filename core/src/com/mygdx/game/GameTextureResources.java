package com.mygdx.game;

import com.mygdx.game.engine.resources.BaseTextureResources;

/**
 * All the Game Specific Texture resources are loaded here
 * @author Ramses Di Perna
 *
 */
public class GameTextureResources extends BaseTextureResources 
{
	public static final String UI_GAME_MENU_BACKGROUND = "menu_bg";
	public static final String UI_GAME_MENU_LOGO = "menu_logo";
	public static final String UI_GAME_MENU_PLAY_BTN = "button_play";
	public static final String UI_GAME_MENU_PLAY_BTN_PRESSED = "button_play_pressed";
	
	public static final String UI_INGAME_PAUSE_BTN = "button_pause";
	public static final String UI_INGAME_RETRY_BTN = "button_retry";
	public static final String UI_INGAME_RETRY_BTN_PRESSED = "button_retry_pressed";
	public static final String UI_INGAME_QUIT_BTN = "button_quit";
	public static final String UI_INGAME_QUIT_BTN_PRESSED = "button_quit_pressed";
	public static final String UI_INGAME_GAMEOVER_FIELD = "gameover_field";
	public static final String UI_INGAME_INTERFACE_GRADIENT = "interface_gradient";
	public static final String UI_INGAME_NEW_HIGHSCORE = "new_highscore_icon";
	public static final String UI_INGAME_SCORE_FIELD = "scorefield";
	public static final String UI_INGAME_WAVE_FIELD = "wavefield";
	public static final String UI_INGAME_HEALTHBAR = "health_bar";
	
	public static final String UI_POP_UP_PAUSE = "PausePopUp";
	public static final String UI_POP_UP_GAME_OVER = "GameOverPopUp";
	public static final String UI_BUTTON_CONTINUE = "ContinueButton";
	public static final String UI_ELEMENT_NEW_HIGHSCORE_MARK = "NewHighscoreMark";
	
	public static final String SPRITE_GAME_BACKGROUND_01 = "gameBackground01Sprite";
	public static final String SPRITE_GAME_ROOF = "roofLevel";
	
	public static final String SPRITE_BOW_AIM_TARGET = "bowAimTargetSprite";
	public static final String SPRITE_HP_BAR = "hpBarSprite";
	
	public static final String ANIMATION_BOW_DRAW = "bowDraw";
	public static final String ANIMATION_BOW_ARROW = "arrowFly";
	public static final String ANIMATION_BOW_SPECIAL_ARROW_EXPLOSIVE = "explosiveSpecialArrowFly";
	public static final String ANIMATION_BOW_SPECIAL_ARROW_FREEZE = "freezeSpecialArrowFly";
	
	public static final String ANIMATION_TRAP_ACTIVATOR_TURN = "TrapActivatorTrunAnim";
	
	
	public static final String ANIMATION_LIGHT_BANDIT_0_IDLE = "LIGHT_bandit_0_Idle";
	public static final String ANIMATION_LIGHT_BANDIT_0_RUN = "LIGHT_bandit_0_Run";
	public static final String ANIMATION_LIGHT_BANDIT_0_DEATH = "LIGHT_bandit_0_Death";
	public static final String ANIMATION_LIGHT_BANDIT_0_ATTACK = "LIGHT_bandit_0_Attack";
	
	
	public static final String ANIMATION_MEDIUM_BANDIT_0_IDLE = "MEDIUM_Bandit_0_Idle";
	public static final String ANIMATION_MEDIUM_BANDIT_1_IDLE = "MEDIUM_Bandit_1_Idle";

	public static final String ANIMATION_MEDIUM_BANDIT_0_RUN = "MEDIUM_Bandit_0_Run";
	public static final String ANIMATION_MEDIUM_BANDIT_1_RUN = "MEDIUM_Bandit_1_Run";

	public static final String ANIMATION_MEDIUM_BANDIT_0_DEATH = "MEDIUM_Bandit_0_Death";
	public static final String ANIMATION_MEDIUM_BANDIT_1_DEATH = "MEDIUM_Bandit_1_Death";

	public static final String ANIMATION_MEDIUM_BANDIT_0_ATTACK = "MEDIUM_Bandit_0_Attack";
	public static final String ANIMATION_MEDIUM_BANDIT_1_ATTACK = "MEDIUM_Bandit_1_Attack";
	
	
	public static final String ANIMATION_HEAVY_BANDIT_0_IDLE = "HEAVY_bandit_0_Idle";
	public static final String ANIMATION_HEAVY_BANDIT_0_RUN = "HEAVY_bandit_0_Run";
	public static final String ANIMATION_HEAVY_BANDIT_0_DEATH = "HEAVY_bandit_0_Death";
	public static final String ANIMATION_HEAVY_BANDIT_0_ATTACK = "HEAVY_bandit_0_Attack";
	public static final String ANIMATION_HEAVY_BANDIT_0_CHARGE = "HEAVY_bandit_0_Charge";
	
	
	public static final String ANIMATION_TRAIN_WHEELS = "TrainWheels";
	public static final String SPRITE_TRAIN_BASE = "TrainBase";
	public static final String SPRITE_CART_BASE = "CartBase";
	public static final String SPRITE_TRAIN_RAIL = "TrainRail";
	

	public static final String SPRITE_TOUCH_UP = "Touch_Up";
	public static final String SPRITE_TOUCH_DOWN = "Touch_Down";

	public static final String SPRITE_ICE_CUBE_EFFECT = "IceCubeEffect";
	
	public static final String ANIMATION_EFFECT_HIT = "hitEffect_animation";
	public static final String ANIMATION_EFFECT_EXPLOSION_SMALL = "ExplosionEffectSmall_Animation";
	public static final String ANIMATION_EFFECT_EXPLOSION_BIG = "ExplosionEffectBig_Animation";
	
	public static final String SHEET_BLOOD_POOL = "bloodPoolSheet";
	public static final String SHEET_EXPLOSIVE_BARREL_TRAP = "ExplosiveBarrelSheetTrap";
	
	@Override
	public void load() 
	{
		
		// UI
		this.loadResource(SPRITE_GAME_BACKGROUND_01, "background_01.png");
		this.loadResource(UI_GAME_MENU_BACKGROUND, "menu_bg.png");
		this.loadResource(UI_GAME_MENU_LOGO, "menu_logo.png");
		
		this.loadResource(UI_GAME_MENU_PLAY_BTN, "ui/button_play_02.png");
		this.loadResource(UI_GAME_MENU_PLAY_BTN_PRESSED, "ui/button_play_02_onpush.png");
		this.loadResource(UI_INGAME_PAUSE_BTN, "ui/button_pause.png");
		this.loadResource(UI_INGAME_RETRY_BTN, "ui/button_retry.png");
		this.loadResource(UI_INGAME_RETRY_BTN_PRESSED, "ui/button_retry_onpush.png");
		this.loadResource(UI_INGAME_QUIT_BTN, "ui/button_quit.png");
		this.loadResource(UI_INGAME_QUIT_BTN_PRESSED, "ui/button_quit_onpress.png");
		this.loadResource(UI_INGAME_GAMEOVER_FIELD, "ui/gameover_field.png");
		this.loadResource(UI_INGAME_INTERFACE_GRADIENT, "ui/interface_gradient.png");
		this.loadResource(UI_INGAME_NEW_HIGHSCORE, "ui/new_highscore_icon.png");
		this.loadResource(UI_INGAME_SCORE_FIELD, "ui/scorefield.png");
		this.loadResource(UI_INGAME_WAVE_FIELD, "ui/wavefield.png");
		this.loadResource(UI_INGAME_HEALTHBAR, "ui/game_healthbar.png");
		this.loadResource(UI_BUTTON_CONTINUE, "ui/button_continue.png");
		
		
		//Other
		this.loadResource(SPRITE_GAME_ROOF, "Roof_01.png");
		this.loadResource(SPRITE_BOW_AIM_TARGET, "target.png");
		
		this.loadResource(ANIMATION_TRAIN_WHEELS,"train/trainWheels.png", "train/trainWheels.xml");
		this.loadResource(SPRITE_TRAIN_BASE, "train/train_base.png");
		this.loadResource(SPRITE_CART_BASE, "train/cart_base.png");
		this.loadResource(SPRITE_TRAIN_RAIL, "train/rails.png");
		
		this.loadResource(UI_ELEMENT_NEW_HIGHSCORE_MARK, "popups/new_highscore_icon.png");
		
		// Placeholders
		this.loadResource("testImage", "placeholders/badlogic.jpg");
		this.loadResource(SPRITE_HP_BAR, "placeholders/hpBarPlaceholder.png");
		
		// Bow
		this.loadResource(ANIMATION_BOW_DRAW,"weapon/bowDraw.png", "weapon/bowDraw.xml");
		this.loadResource(ANIMATION_BOW_ARROW, "weapon/arrowFly.png", "weapon/arrowFly.xml");
		this.loadResource(ANIMATION_BOW_SPECIAL_ARROW_EXPLOSIVE, "weapon/explosiveArrow.png", "weapon/explosiveArrow.xml");
		this.loadResource(ANIMATION_BOW_SPECIAL_ARROW_FREEZE, "weapon/freezeArrow.png", "weapon/freezeArrow.xml");
		
		// Enemies
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_IDLE, "enemies/lightBanditA/lightBanditAIdle.png", "enemies/lightBanditA/lightBanditAIdle.xml");
		this.loadResource(ANIMATION_MEDIUM_BANDIT_0_IDLE,"enemies/mediumBanditA/banditAIdle.png", "enemies/mediumBanditA/banditAIdle.xml");
		this.loadResource(ANIMATION_MEDIUM_BANDIT_1_IDLE,"enemies/mediumBanditB/banditBIdle.png", "enemies/mediumBanditB/banditBIdle.xml");
		this.loadResource(ANIMATION_HEAVY_BANDIT_0_IDLE, "enemies/heavyBanditA/heavyBanditAIdle.png", "enemies/heavyBanditA/heavyBanditAIdle.xml");

		this.loadResource(ANIMATION_LIGHT_BANDIT_0_RUN, "enemies/lightBanditA/lightBanditARun.png", "enemies/lightBanditA/lightBanditARun.xml");
		this.loadResource(ANIMATION_MEDIUM_BANDIT_0_RUN,"enemies/mediumBanditA/banditARun.png", "enemies/mediumBanditA/banditARun.xml");
		this.loadResource(ANIMATION_MEDIUM_BANDIT_1_RUN,"enemies/mediumBanditB/banditBRun.png", "enemies/mediumBanditB/banditBRun.xml");
		this.loadResource(ANIMATION_HEAVY_BANDIT_0_RUN, "enemies/heavyBanditA/heavyBanditARun.png", "enemies/heavyBanditA/heavyBanditARun.xml");
		
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_DEATH, "enemies/lightBanditA/lightBanditADeath.png", "enemies/lightBanditA/lightBanditADeath.xml");
		this.loadResource(ANIMATION_MEDIUM_BANDIT_0_DEATH,"enemies/mediumBanditA/banditADeath.png", "enemies/mediumBanditA/banditADeath.xml");
		this.loadResource(ANIMATION_MEDIUM_BANDIT_1_DEATH,"enemies/mediumBanditB/banditBDeath.png", "enemies/mediumBanditB/banditBDeath.xml");
		this.loadResource(ANIMATION_HEAVY_BANDIT_0_DEATH, "enemies/heavyBanditA/heavyBanditADeath.png", "enemies/heavyBanditA/heavyBanditADeath.xml");

		this.loadResource(ANIMATION_LIGHT_BANDIT_0_ATTACK, "enemies/lightBanditA/lightBanditAAttack.png", "enemies/lightBanditA/lightBanditAAttack.xml");
		this.loadResource(ANIMATION_MEDIUM_BANDIT_0_ATTACK,"enemies/mediumBanditA/banditAAttack.png", "enemies/mediumBanditA/banditAAttack.xml");
		this.loadResource(ANIMATION_MEDIUM_BANDIT_1_ATTACK,"enemies/mediumBanditB/banditBAttack.png", "enemies/mediumBanditB/banditBAttack.xml");
		this.loadResource(ANIMATION_HEAVY_BANDIT_0_ATTACK, "enemies/heavyBanditA/heavyBanditAAttack.png", "enemies/heavyBanditA/heavyBanditAAttack.xml").setInnerOffset(0f, -0.38f);

		this.loadResource(ANIMATION_HEAVY_BANDIT_0_CHARGE, "enemies/heavyBanditA/heavyBanditACharge.png", "enemies/heavyBanditA/heavyBanditACharge.xml").setInnerOffset(0f, -0.26f);
		
		// Traps
		this.loadResource(ANIMATION_TRAP_ACTIVATOR_TURN, "traps/targetTurn.png", "traps/targetTurn.xml");
		this.loadResource(SHEET_EXPLOSIVE_BARREL_TRAP, "traps/explosiveBarrel.png", "traps/explosiveBarrel.xml");
		
		// Tutorial
		this.loadResource(SPRITE_TOUCH_UP, "tutorial/tutorial_hand_01.png");
		this.loadResource(SPRITE_TOUCH_DOWN, "tutorial/tutorial_hand_02.png");
		
		// Effects
		this.loadResource(ANIMATION_EFFECT_HIT, "effects/hitEffectEpic.png", "effects/hitEffectEpic.xml");
		this.loadResource(SHEET_BLOOD_POOL, "effects/bloodPool.png", "effects/bloodPool.xml");

		this.loadResource(ANIMATION_EFFECT_EXPLOSION_BIG, "effects/bigExplosion.png", "effects/bigExplosion.xml");
		this.loadResource(ANIMATION_EFFECT_EXPLOSION_SMALL, "effects/smallExplosion.png", "effects/smallExplosion.xml");
		
		this.loadResource(SPRITE_ICE_CUBE_EFFECT, "effects/iceblock.png");
		
		// Pop Ups
		this.loadResource(UI_POP_UP_PAUSE, "popups/paused_field.png");
		this.loadResource(UI_POP_UP_GAME_OVER, "popups/gameover_field.png");
	}
}
