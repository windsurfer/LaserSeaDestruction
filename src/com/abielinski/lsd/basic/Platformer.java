package com.abielinski.lsd.basic;

import processing.core.PApplet;

import com.abielinski.lsd.LSDLevel;

/**
 *This is an example game implementation for an example platformer.
 * @author Adam
 */
public class Platformer extends LSDLevel {
	
	/**
	 * This is an example level. Normally you would load this from a file, but
	 */
	protected String level = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\n" +
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
	
	
	
	/**
	 * Initilize the platformer with the defaults. Will tell your log you're using it!
	 */
	public Platformer() {
		super();
		PApplet.println("Using the default Platformer class. Extend this class to override defaults!");
	}
	
	
	
}
