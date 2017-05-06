package com.mygdx.game.traps.types;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Engine;
import com.mygdx.game.GameTextureResources;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.level.Grid;

public class TestBlockTrap extends BaseBlockTrap {

	public TestBlockTrap(Grid grid) {
		super(grid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getSizeX() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int getSizeY() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	protected void awake() {
		// TODO Auto-generated method stub
		this.addComponent(new RenderComponent(Engine.getTextureResources().getRenderInfo(GameTextureResources.SPRITE_BOW_AIM_TARGET), false)).setPivot(new Vector2(0f, 0f), false);
	}

	@Override
	protected void updated(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void rendered(RenderComponents renderComponents) {
		// TODO Auto-generated method stub

	}

}
