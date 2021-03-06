package com.mygdx.game.traps.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.engine.entities.components.BaseEntityComponent.TweenStartType;
import com.mygdx.game.engine.entities.components.rendering.RenderComponent;
import com.mygdx.game.engine.scenes.RenderComponents;
import com.mygdx.game.engine.tweening.EaseType;
import com.mygdx.game.factories.TrapFactory.Direction;
import com.mygdx.game.globals.GridTags;
import com.mygdx.game.level.Grid;

/**
 * Spawns 3 explosive barrels which all do 1/3 of the total damage given to this trap.
 * They will all explode with a short delay of 0.2 seconds dealing the given damage in total.
 * @author Ramses Di Perna
 *
 */
public class ExplosiveBarrelDamageTrap extends BaseTrap 
{
	private static final boolean DEVELOPMENT_DEBUG_RADIUS_DRAW = false; 
	
	private ShapeRenderer _shapeRenderer;
	private float _radius;
	private float _damage;
	
	private float _delayExplosionTracker = -1;
	private int _currentIndex = 0;
	
	private ExplosiveBarrel[] _explosiveBarrels = new ExplosiveBarrel[3];
	
	public ExplosiveBarrelDamageTrap(Grid grid, Direction direction, float damage, float radius) 
	{
		super(grid, direction);
		this.addComponent(new RenderComponent(null, false));
		_radius = radius;
		_damage = damage;
		_shapeRenderer = new ShapeRenderer();
		_shapeRenderer.setAutoShapeType(true);
		this.getGridUserComponent().addGridOccupyTag(GridTags.OCCUPY_TAG_BLOCKED);
	}
	
	@Override
	public int place(int x, int y)
	{
		int value = super.place(x, y);
		this.getTransformComponent().translatePosition(this.getGrid().getTileWidth() / 2, this.getGrid().getTileHeight() / 2);
		return value;
	}
	
	@Override
	public int getSizeX() 
	{
		return 1;
	}

	@Override
	public int getSizeY() 
	{
		return 1;
	}

	@Override
	public boolean canBeActive() 
	{
		if(_explosiveBarrels[0] == null) { return true; }
		
		for(int i = 0; i < _explosiveBarrels.length; i++)
		{
			if((_explosiveBarrels[i].hasExploded() && !_explosiveBarrels[i].isDestroyed()) || !_explosiveBarrels[i].hasExploded())
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	protected void doEffect() 
	{
		_delayExplosionTracker = 0;
	}

	@Override
	protected void awake() 
	{
		
	}

	@Override
	protected void updated(float dt) 
	{
		if(_delayExplosionTracker >= 0)
		{
			_delayExplosionTracker += dt;
			if(_delayExplosionTracker > 0.2f || _currentIndex == 0)
			{
				_explosiveBarrels[_currentIndex].triggerBarrel();
				_currentIndex++;
				_delayExplosionTracker = 0.05f;
				if(_currentIndex >= _explosiveBarrels.length)
				{
					_delayExplosionTracker = -1;
				}
			}
		}
	}

	@Override
	protected void rendered(RenderComponents renderComponents) 
	{	
		if(!DEVELOPMENT_DEBUG_RADIUS_DRAW) { return; }
		
		renderComponents.getSpriteBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		_shapeRenderer.setProjectionMatrix(renderComponents.getMainCamera().combined);
		_shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		_shapeRenderer.setColor(0.65f, 0f, 0f, 0.14f);
		_shapeRenderer.circle(this.getTransformComponent().getPositionX(), this.getTransformComponent().getPositionY(), _radius);
		_shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		renderComponents.getSpriteBatch().begin();
	}
	
	@Override
	protected void destroyed()
	{
		super.destroyed();
		_shapeRenderer.dispose();
		_shapeRenderer = null;
		
		clearBarrels();
		_explosiveBarrels = null;
	}

	@Override
	public void refresh() 
	{
		_currentIndex = 0;
		spawnBarrels();
	}
	
	/**
	 * Spawns the barrels with a nice fade bounce effect.
	 */
	private void spawnBarrels()
	{
		float fallHeight = 50;
		
		_delayExplosionTracker = -1;
		for(int i = 0; i < _explosiveBarrels.length; i++)
		{
			Vector2 deltaBarrel = new Vector2();
			_explosiveBarrels[i] = new ExplosiveBarrel(_damage / 3f, _radius);
			_explosiveBarrels[i].getTransformComponent().setParent(this.getTransformComponent());
			switch(i)
			{
			case 0:				
				deltaBarrel.x = 0;
				deltaBarrel.y = 2;
				break;
			case 1:
				deltaBarrel.x = 25;
				deltaBarrel.y = 20;
				break;
			case 2:
				deltaBarrel.x = -25;
				deltaBarrel.y = 20;
				break;
			}
			
			deltaBarrel.y += fallHeight;
			deltaBarrel.y -= _explosiveBarrels[i].getComponent(RenderComponent.class).getRealHeight() / 2;
			
			_explosiveBarrels[i].getTransformComponent().translatePosition(deltaBarrel.x, deltaBarrel.y);

			_explosiveBarrels[i].getComponent(RenderComponent.class).setAlpha(0);
			_explosiveBarrels[i].getComponent(RenderComponent.class).doAlpha(1, 0.75f, TweenStartType.GameTime);
			_explosiveBarrels[i].getTransformComponent().doPosition(
					_explosiveBarrels[i].getTransformComponent().getLocalPositionX(), 
					_explosiveBarrels[i].getTransformComponent().getLocalPositionY() - fallHeight, 1.15f, TweenStartType.GameTime).ease(EaseType.BounceOut);
		}
	}
	
	/**
	 * Removes the barrels.
	 */
	private void clearBarrels()
	{
		for(int i = 0; i < _explosiveBarrels.length; i++)
		{
			if(_explosiveBarrels[i] != null)
				_explosiveBarrels[i].destroy();
			_explosiveBarrels[i] = null;
		}
	}
}
