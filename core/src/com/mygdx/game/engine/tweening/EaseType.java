package com.mygdx.game.engine.tweening;

import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Circ;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Quart;
import aurelienribon.tweenengine.equations.Quint;
import aurelienribon.tweenengine.equations.Sine;

/**
 * This enum holds all the Ease types which can be used in the tweening functionality.
 * Use the 'getTweenEquation' method to get the linked Equation of the tween.
 * @author Ramses Di Perna
 *
 */
public enum EaseType
{
	// Default
	Linear(aurelienribon.tweenengine.equations.Linear.INOUT),
	
	// Sine
	SineIn(Sine.IN),
	SineOut(Sine.OUT),
	SineInOut(Sine.INOUT),
	
	// Quad
	QuadIn(Quad.IN),
	QuadOut(Quad.OUT),
	QuadInOut(Quad.INOUT),
	
	// Cubic
	CubicIn(Cubic.IN),
	CubicOut(Cubic.OUT),
	CubicInOut(Cubic.INOUT),
	
	// Quart
	QuartIn(Quart.IN),
	QuartOut(Quart.OUT),
	QuartInOut(Quart.INOUT),
	
	// Quint
	QuintIn(Quint.IN),
	QuintOut(Quint.OUT),
	QuintInOut(Quint.INOUT),
	
	// Expo
	ExpoIn(Expo.IN),
	ExpoOut(Expo.OUT),
	ExpoInOut(Expo.INOUT),
	
	// Circ
	CircIn(Circ.IN),
	CircOut(Circ.OUT),
	CircInOut(Circ.INOUT),
	
	// Back
	BackIn(Back.IN),
	BackOut(Back.OUT),
	BackInOut(Back.INOUT),
	
	// Elastic
	ElasticIn(Elastic.IN),
	ElasticOut(Elastic.OUT),
	ElasticInOut(Elastic.INOUT),
	
	// Bounce
	BounceIn(Bounce.IN),
	BounceOut(Bounce.OUT),
	BounceInOut(Bounce.INOUT);
	
	private TweenEquation _equation;
	
	private EaseType(TweenEquation equation) 
	{
		_equation = equation;
    }

	/**
	 * Returns the TweenEquation this Enum item represents.
	 * @return
	 */
    public TweenEquation getTweenEquation()
    {
    	return _equation;
    }
}
