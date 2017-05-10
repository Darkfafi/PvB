package com.mygdx.game.popUpSystem;

import java.util.Stack;

import com.mygdx.game.MyGdxGame;

/**
 * This class is a singleton which handles all the setting of the BasePopUp instances which are created. It will handle that they are set in the correct layer and will keep track of their layer status
 * It will also change the TouchInputHandler's layer if a cover pop-up is created
 * @author Ramses Di Perna
 *
 */
public class PopUpSystem
{
	/**
	 * This layer is the layer where the Pop Ups will be rendered on + UI layering
	 */
	public static final int POP_UP_SORT_LAYER_PLUS_UI = 200;
	
	private static PopUpSystem _instance;
	private Stack<BasePopUp> _popUpsActive = new Stack<BasePopUp>();
	private Stack<Integer> _coverIndexes = new Stack<Integer>();
	
	public static PopUpSystem getInstance()
	{
		if(_instance == null)
		{
			_instance = new PopUpSystem();
		}
		
		return _instance;
	}
	
	/**
	 * Registers a pop up into the system. 
	 * The BasePopUp automatically calls this method when created
	 * @param popUp to register.
	 */
	public void registerPopUp(BasePopUp popUp)
	{
		if(_popUpsActive.contains(popUp)) { return; }
		_popUpsActive.add(popUp);
		popUp.getRenderComponent().setSortingLayer(POP_UP_SORT_LAYER_PLUS_UI);
		
		if(popUp.isCoverPopUp())
		{
			_coverIndexes.add(_popUpsActive.indexOf(popUp));
			popUp.system_setLayer(_coverIndexes.size());
			MyGdxGame.getInputHandler().setCurrentTouchLayer(_coverIndexes.size());
		}

		popUp.system_setLayer(0);
		
		setTopPopUps();
	}
	
	/**
	 * Unregisters a pop up from the system. 
	 * The BasePopUp automatically calls this method when destroyed
	 * @param popUp to unregister.
	 */
	public void unregisterPopUp(BasePopUp popUp)
	{
		if(!_popUpsActive.contains(popUp)) { return; }
		Integer index = _popUpsActive.indexOf(popUp);
		_popUpsActive.remove(popUp);
		
		if(_coverIndexes.contains(index))
		{
			_coverIndexes.remove(index);
		}
		
		MyGdxGame.getInputHandler().setCurrentTouchLayer(_coverIndexes.size());
		
		setTopPopUps();
	}
	
	/**
	 * Checks and sets which pop up is currently on top and which cover pop up is on top and sets this information in the pop up itself
	 */
	private void setTopPopUps()
	{
		if(_popUpsActive.isEmpty()) { return; }
		
		for(int i = 0; i < _popUpsActive.size() - 1; i++)
		{
			if(_popUpsActive.get(i).isTopPopUp())
			{
				_popUpsActive.get(i).system_setTopPopUp(false);
			}
		}

		_popUpsActive.peek().system_setTopPopUp(true);
		
		if(_coverIndexes.isEmpty()) { return; }
		
		for(int i = 0; i < _coverIndexes.size() - 1; i++)
		{
			int index = _coverIndexes.get(i);
			if(_popUpsActive.get(index).isTopCoverPopUp())
			{
				_popUpsActive.get(index).system_setTopCoverPopUp(false);
			}
		}
		
		_popUpsActive.get(_coverIndexes.peek()).system_setTopCoverPopUp(true);
	}
}
