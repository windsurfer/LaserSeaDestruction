package com.abielinski.lsd.basic;

import processing.core.PApplet;

import com.abielinski.lsd.LSDLevel;

public class Platformer extends LSDLevel {
	
	/*
	 * This is an example level. Normally you would load this from a file, but
	 */
	String level = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\n" +
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\n" +
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\n" +
			"0,0,0,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\n" +
			"0,0,0,0,0,0,0,9,8,7,7,6,6,6,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\n" +
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,4,4,4,4,4,0,0,0,0,0,0\n" +
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3\n" +
			"0,0,2,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3\n" +
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,0,0,0\n" +
			"1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1\n" +
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\n";
	
	
	
	public Platformer() {
		super();
		PApplet.println("Using the default Platformer class. Extend this class to override defaults!");
	}
	
	
	public void nothing(){
		
	}
	
}
