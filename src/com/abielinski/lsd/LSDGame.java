package com.abielinski.lsd;


/**
 * The main logic for a game mode. Also controls the camera. Extend this class to make your own game.
 * Add all your objects to an instance of this object.
 * @author Adam
 */
public class LSDGame extends LSDContainer{
	/**
	 * The camera for this game.
	 */
	public LSDCamera camera;
	
	/**
	 * Initialize the game with default logic
	 */
	public LSDGame(){
		super();
	}  
	public void init(){
		camera = new LSDCamera();
	}
	
	public void run(){
		super.run();
	}
	
	public void draw(){
		super.draw();
	}
	
}