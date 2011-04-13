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
		this.createGraphic(16, 16, "basicPlatformerPlayer.PNG");
		accel.y = 0.0001f;
		drag.x = 0.1f;
		//drag.y = 0.1f;
	}
	
	public void run(){
		super.run();
		if(LSDG.keys(PApplet.LEFT)){
			accel.x = -0.014f;
		}else if(LSDG.keys(PApplet.RIGHT)){
			accel.x = 0.014f;
		}else{
			accel.x = 0;
			vel.x = 0;
		}
		if (LSDG.keys(PApplet.UP) ){
			onFloor = false;
			vel.y = -0.1f;
		}
		if (LSDG.keys(PApplet.DOWN)){
			onFloor = false;
			vel.y = 0.1f;
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
	@Override
	public void collide(LSDSprite s, LSDSprite ground){
		//PApplet.println("Collided");
	}
	
	
	
	
}
