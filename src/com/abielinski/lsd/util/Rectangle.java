package com.abielinski.lsd.util;

import processing.core.PVector;


public class Rectangle {
	public PVector pos;
	public float w;
	public float h;
	public Rectangle(){
		pos = new PVector();
		w = 0;
		h = 0;
	}
	public Rectangle(float _x, float _y, float _w, float _h){
		pos = new PVector(_x,_y);
		w = _w;
		h = _h;
	}
	public float area(){
		return w*h;
	}
	public PVector topLeft(){
		return new PVector(pos.x,pos.y);
	}
	public PVector bottomRight(){
		return new PVector(pos.x+w,pos.y+h);
	}
}
