package com.abielinski.lsd.util;

import processing.core.PVector;


/**
 * A rectange class.
 * @author Adam
 *
 */
public class Rectangle {
	/**
	 * The position of the object. Remember to use pos.get() when setting the position of one object to another!
	 */
	public PVector pos;
	/**
	 * The width
	 */
	public float w;
	/**
	 * The height
	 */
	public float h;
	/**
	 * Creates a rectange at 0,0 with a size of 0,0
	 */
	public Rectangle(){
		pos = new PVector();
		w = 0;
		h = 0;
	}
	
	/**
	 * Creates a rect with the specified parameters
	 * @param _x
	 * @param _y
	 * @param _w
	 * @param _h
	 */
	public Rectangle(float _x, float _y, float _w, float _h){
		pos = new PVector(_x,_y);
		w = _w;
		h = _h;
	}
	/**
	 * @return the area of the rect
	 */
	public float area(){
		return w*h;
	}
	/**
	 * @return top left point of the rect
	 */
	public PVector topLeft(){
		return new PVector(pos.x,pos.y);
	}
	/**
	 * @return bottom right point of the rect
	 */
	public PVector bottomRight(){
		return new PVector(pos.x+w,pos.y+h);
	}
}
