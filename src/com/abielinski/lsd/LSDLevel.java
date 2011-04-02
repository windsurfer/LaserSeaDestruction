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
	 * Generates a default (empty) level with a specific width and height
	 * @param wi width
	 * @param he height
	 */
	public LSDLevel(int wi, int he){
		super(wi/2,he/2);
		this.w = wi;
		this.h = he;
		fixed = true;
	}
	/**
	 * (non-Javadoc)
	 * @see com.abielinski.lsd.LSDSprite#init()
	 */
	public void init(){
		super.init();
		fixed = true;
	}
	
	/**
	 * Returns whether or not a point at x,y collides with the level.
	 * @param x
	 * @param y
	 * @return whether it collides
	 */
	public boolean collide(int x, int y){
		
		return false;
	}
	
}