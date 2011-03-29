package com.abielinski.lsd;

import processing.core.PVector;

/**
 * The main logic for a game mode. Also controls the camera. Extend this class to make your own game.
 * Add all your objects to an instance of this object.
 * @author Adam
 */
public class LSDGame extends LSDContainer{
	  /**
	 * The camera's location.
	 */
	public PVector cam;
	  /**
	 * What the camera is following
	 */
	public LSDSprite cam_target;
	  /**
	 * The "speed" the camera will follow it's target
	 */
	public float cam_speed;
	  
	  /**
	 * Initialize the game with default logic
	 */
	public LSDGame(){
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