/**
 * 
 */
package com.abielinski.lsd;

import com.abielinski.lsd.util.Rectangle;

import processing.core.PVector;

/**
 * @author Adam
 *
 */
public class LSDCamera extends LSDSprite {
	
	/**
	 * The sprite that the camera will follow
	 */
	public LSDSprite target;

	
	/**
	 * The edges of the camera's view
	 */
	public Rectangle bounds;
	
	/**
	 * 
	 */
	public float speed;
	
	/**
	 * 
	 */
	public LSDCamera() {
		super();
	}
	
	/**
	 * Sets initial position of Camera
	 * @param xpos
	 * @param ypos
	 */
	public LSDCamera(float xpos, float ypos) {
		super(xpos, ypos);
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		super.run();
	    if (target!= null){
	      PVector diff = pos.get();
	      diff.sub(target.pos);
	      diff.mult(speed);
	      pos.add(diff);
	    }
	}
	
	/**
	 * Moves the draw matrix according to the camera
	 */
	public void pushCamera(){
		LSDG.theParent.pushMatrix();
		LSDG.theParent.translate(-pos.x, -pos.y);
		LSDG.theParent.rotate(this.angle);
		
	}
	
}
