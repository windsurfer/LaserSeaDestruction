/**
 * 
 */
package com.abielinski.lsd.basic;

import processing.core.PApplet;

import com.abielinski.lsd.LSDG;
import com.abielinski.lsd.LSDSprite;

/**
 * @author Adam
 *
 */
public class PlatformerPlayer extends LSDSprite {

	/**
	 * @param i
	 * @param j
	 */
	public PlatformerPlayer(int i, int j) {
		super(i,j);
		init();
	}
	
	public void init(){
		super.init();
		this.createGraphic(16, 16, "basicPlatformerPlayer.PNG");
		accel.y = 0.001f;
		drag.x = 0.01f;
	}
	
	public void run(){
		super.run();
		if(LSDG.keys(PApplet.LEFT)){
			accel.x = -0.01f;
		}else if(LSDG.keys(PApplet.RIGHT)){
			accel.x = 0.01f;
		}else{
			accel.x = 0;
			vel.x = 0;
		}
		if (onFloor && LSDG.keys(PApplet.UP)){
			onFloor = false;
			vel.y = -0.9f;
		}
		if (pos.x < 0){pos.x = 0; vel.x = 0;}
	    if (pos.x > LSDG.theParent.width){pos.x = LSDG.theParent.width;vel.x = 0;}
	    if (pos.y > LSDG.theParent.height){
	        pos.y = LSDG.theParent.height;
	        vel.y = 0;
	        onFloor = true;
	    }
	    
	    //PApplet.println("Running player");
	}
	public void hitBottom(LSDSprite Contact,float Velocity){
		super.hitBottom(Contact, Velocity);
		PApplet.println("Collision at "+pos.x);
	}
	
	
}
