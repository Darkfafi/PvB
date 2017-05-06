package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mygdx.game.Engine;
import com.mygdx.game.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(MyGdxGame.WIDTH * MyGdxGame.SCALE, MyGdxGame.HEIGHT * MyGdxGame.SCALE);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new MyGdxGame();
        }
}