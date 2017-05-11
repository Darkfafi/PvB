package com.mygdx.game;

import com.mygdx.game.engine.resources.BaseTextureResources;

public class GameTextureResources extends BaseTextureResources 
{
	public static final String UI_GAME_MENU_BACKGROUND = "menu_bg";
	public static final String UI_GAME_MENU_LOGO = "menu_logo";
	public static final String UI_GAME_MENU_PLAY_BTN = "button_play";
	public static final String UI_GAME_MENU_PLAY_BTN_PRESSED = "button_play_pressed";
	
	public static final String UI_INGAME_PAUSE_BTN = "button_pause";
	public static final String UI_INGAME_RETRY_BTN = "button_retry";
	public static final String UI_INGAME_RETRY_BTN_PRESSED = "button_retry_pressed";
	public static final String UI_INGAME_GAMEOVER_FIELD = "gameover_field";
	public static final String UI_INGAME_INTERFACE_GRADIENT = "interface_gradient";
	public static final String UI_INGAME_NEW_HIGHSCORE = "new_highscore_icon";
	public static final String UI_INGAME_SCORE_FIELD = "scorefield";
	public static final String UI_INGAME_WAVE_FIELD = "wavefield";
	public static final String UI_INGAME_HEALTHBAR = "health_bar";
	
	public static final String UI_POP_UP_PAUSE = "PausePopUp";
	public static final String UI_BUTTON_CONTINUE = "ContinueButton";
	
	public static final String SPRITE_GAME_BACKGROUND_01 = "gameBackground01Sprite";
	
	public static final String SPRITE_BOW_AIM_TARGET = "bowAimTargetSprite";
	public static final String SPRITE_HP_BAR = "hpBarSprite";
	
	public static final String ANIMATION_BOW_DRAW = "bowDraw";
	public static final String ANIMATION_BOW_ARROW = "arrowFly";
	
	public static final String ANIMATION_TRAP_ACTIVATOR_TURN = "TrapActivatorTrunAnim";
	
	public static final String ANIMATION_LIGHT_BANDIT_0_IDLE = "light_Bandit_0_Idle";
	public static final String ANIMATION_LIGHT_BANDIT_1_IDLE = "light_Bandit_1_Idle";

	public static final String ANIMATION_LIGHT_BANDIT_0_RUN = "light_Bandit_0_Run";
	public static final String ANIMATION_LIGHT_BANDIT_1_RUN = "light_Bandit_1_Run";

	public static final String ANIMATION_LIGHT_BANDIT_0_DEATH = "light_Bandit_0_Death";
	public static final String ANIMATION_LIGHT_BANDIT_1_DEATH = "light_Bandit_1_Death";

	public static final String ANIMATION_LIGHT_BANDIT_0_ATTACK = "light_Bandit_0_Attack";
	public static final String ANIMATION_LIGHT_BANDIT_1_ATTACK = "light_Bandit_1_Attack";
	
	public static final String ANIMATION_TRAIN_WHEELS = "TrainWheels";
	public static final String SPRITE_TRAIN_BASE = "TrainBase";
	public static final String SPRITE_CART_BASE = "CartBase";
	public static final String SPRITE_TRAIN_RAIL = "TrainRail";
	

	public static final String SPRITE_TOUCH_UP = "Touch_Up";
	public static final String SPRITE_TOUCH_DOWN = "Touch_Down";


	public static final String ANIMATION_EFFECT_HIT = "hitEffect_animation";
	public static final String ANIMATION_EFFECT_EXPLOSION_BIG = "ExplosionEffectBig_Animation";
	
	public static final String SHEET_BLOOD_POOL = "bloodPoolSheet";
	
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
		this.loadResource(UI_INGAME_GAMEOVER_FIELD, "ui/gameover_field.png");
		this.loadResource(UI_INGAME_INTERFACE_GRADIENT, "ui/interface_gradient.png");
		this.loadResource(UI_INGAME_NEW_HIGHSCORE, "ui/new_highscore_icon.png");
		this.loadResource(UI_INGAME_SCORE_FIELD, "ui/scorefield.png");
		this.loadResource(UI_INGAME_WAVE_FIELD, "ui/wavefield.png");
		this.loadResource(UI_INGAME_HEALTHBAR, "ui/game_healthbar.png");
		this.loadResource(UI_BUTTON_CONTINUE, "ui/button_continue.png");
		
		
		//Other
		this.loadResource(SPRITE_BOW_AIM_TARGET, "target.png");
		
		this.loadResource(ANIMATION_TRAIN_WHEELS,"train/trainWheels.png", "train/trainWheels.xml");
		this.loadResource(SPRITE_TRAIN_BASE, "train/train_base.png");
		this.loadResource(SPRITE_CART_BASE, "train/cart_base.png");
		this.loadResource(SPRITE_TRAIN_RAIL, "train/rails.png");
		
		// Placeholders
		this.loadResource("testImage", "placeholders/badlogic.jpg");
		this.loadResource(SPRITE_HP_BAR, "placeholders/hpBarPlaceholder.png");
		
		// Bow
		this.loadResource(ANIMATION_BOW_DRAW,"weapon/bowDraw.png", "weapon/bowDraw.xml");
		this.loadResource(ANIMATION_BOW_ARROW, "weapon/arrowFly.png", "weapon/arrowFly.xml");
		
		// Enemies
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_IDLE,"enemies/banditAIdle.png", "enemies/banditAIdle.xml");
		this.loadResource(ANIMATION_LIGHT_BANDIT_1_IDLE,"enemies/banditBIdle.png", "enemies/banditBIdle.xml");
		
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_RUN,"enemies/banditARun.png", "enemies/banditARun.xml");
		this.loadResource(ANIMATION_LIGHT_BANDIT_1_RUN,"enemies/banditBRun.png", "enemies/banditBRun.xml");
		
		this.loadResource(ANIMATION_LIGHT_BANDIT_0_DEATH,"enemies/banditADeath.png", "enemies/banditADeath.xml");
		this.loadResource(ANIMATION_LIGHT_BANDIT_1_DEATH,"enemies/banditBDeath.png", "enemies/banditBDeath.xml");

		this.loadResource(ANIMATION_LIGHT_BANDIT_0_ATTACK,"enemies/banditAAttack.png", "enemies/banditAAttack.xml");
		this.loadResource(ANIMATION_LIGHT_BANDIT_1_ATTACK,"enemies/banditBAttack.png", "enemies/banditBAttack.xml");
		
		// Traps
		this.loadResource(ANIMATION_TRAP_ACTIVATOR_TURN, "traps/targetTurn.png", "traps/targetTurn.xml");
		
		// Tutorial
		this.loadResource(SPRITE_TOUCH_UP, "tutorial/tutorial_hand_01.png");
		this.loadResource(SPRITE_TOUCH_DOWN, "tutorial/tutorial_hand_02.png");
		
		// Effects
		this.loadResource(ANIMATION_EFFECT_HIT, "effects/hitEffectEpic.png", "effects/hitEffectEpic.xml");
		this.loadResource(SHEET_BLOOD_POOL, "effects/bloodPool.png", "effects/bloodPool.xml");
		this.loadResource(ANIMATION_EFFECT_EXPLOSION_BIG, "effects/bigExplosion.png", "effects/bigExplosion.xml");
		
		// Pop Ups
		this.loadResource(UI_POP_UP_PAUSE, "popups/paused_field.png");
	}
}
