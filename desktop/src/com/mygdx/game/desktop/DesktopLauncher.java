package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Engine;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MyGdxGame.WIDTH * MyGdxGame.SCALE;
		config.height = MyGdxGame.HEIGHT * MyGdxGame.SCALE;
		config.title = MyGdxGame.TITLE;
		config.resizable = false;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
