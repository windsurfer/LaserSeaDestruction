package com.abielinski.lsd;



/**
 * The base level class.
 * @author Adam
 *
 */
public class LSDLevel extends LSDContainer{
	
	
	
	/**
	 * Generates a default (empty) level
	 */
	public LSDLevel(){
		super();
		fixed = true;
	}
	
	/**
	 * Doesn't work yet
	 * @param i
	 * @param j
	 * @return nothing yet
	 */
	public boolean collision(int i, int j){
		return false;
	}
	
	
}