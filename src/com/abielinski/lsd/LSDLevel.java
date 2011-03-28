package com.abielinski.lsd;



public class LSDLevel extends LSDContainer{
	
	
	
	public LSDLevel(){
		super();
		
		
	}
	public LSDLevel(int wi, int he){
		super(wi/2,he/2);
		this.w = wi;
		this.h = he;
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.abielinski.lsd.LSDSprite#init()
	 */
	public void init(){
		
	}
	
	public boolean collide(int i, int j){
		
		return false;
	}
	
}