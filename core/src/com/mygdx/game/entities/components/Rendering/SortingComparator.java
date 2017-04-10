package com.mygdx.game.entities.components.Rendering;

import java.util.Comparator;

public class SortingComparator implements Comparator<RenderComponent> 
{
	@Override
	public int compare(RenderComponent component1, RenderComponent component2) 
	{
		return component1.getSortingLayer() - component2.getSortingLayer();
	}
}
