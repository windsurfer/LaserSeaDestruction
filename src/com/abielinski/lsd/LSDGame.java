package com.abielinski.lsd;

import processing.core.PVector;

public class LSDGame extends LSDContainer{
	  public PVector cam;
	  public LSDSprite cam_target;
	  public float cam_speed;
	  
	  LSDGame(){
	    super();
	  }  
	  public void init(){
	    cam_speed = 0.1f;
	  }
	  
	  public void run(){
	    if (cam_target!= null){
	      PVector diff = cam.get();
	      diff.sub(cam_target.pos);
	      diff.mult(cam_speed);
	      cam.add(diff);
	    }
	      
	    super.run();
	  }
	  
	  public void draw(){
	    super.draw();
	  }
	  
	}