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
	
	public void init(){
		
	}
	
	public boolean collide(int i, int j){
		
		return false;
	}
	
}