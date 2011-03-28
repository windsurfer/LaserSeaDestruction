package com.abielinski.lsd;


import java.util.ArrayList;

/**
 * @author Adam A container class to hold an animation sequence.
 */
public class LSDAnimation {
	
	/**
	 * Miliseconds per frame.
	 */
	public double				fRate;	// Milliseconds per frame
										
	/**
	 * whether this is a looping animation
	 */
	public boolean				loops;
	
	/**
	 * These are an array of integers (NOT PImages) relating to the animation
	 * frames.
	 */
	public ArrayList<Integer>	frames;
	
	/**
	 * Creates a new animation.
	 * 
	 * @param fR
	 *            frames per second
	 * @param f
	 *            array of integers relating to the frames to show
	 */
	LSDAnimation(double fR, ArrayList<Integer> f) {
		fRate = 1000 / fR; // 1000 Milliseconds in a second
		frames = f;
		loops = true;
	}
	
	/**
	 * Creates a new animation.
	 * 
	 * @param fR
	 *            frames per second
	 * @param f
	 *            array of integers relating to the frames to show
	 * @param looping
	 *            whether it loops
	 */
	LSDAnimation(double fR, ArrayList<Integer> f, boolean looping) {
		fRate = 1000 / fR; // 1000 Milliseconds in a second
		frames = f;
		loops = looping;
	}
}
