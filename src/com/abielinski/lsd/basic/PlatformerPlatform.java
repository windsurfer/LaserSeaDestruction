/**
 * 
 */
package com.abielinski.lsd.basic;

import com.abielinski.lsd.LSDSprite;

/**
 * @author smarinof
 *
 */
public class PlatformerPlatform extends LSDSprite {
	
	/**
	 * 
	 */
	public PlatformerPlatform() {
		super();
		init();
	}
	
	/**
	 * @param xpos
	 * @param ypos
	 */
	public PlatformerPlatform(float xpos, float ypos) {
		super(xpos, ypos);
		init();
	}
	
	public void init(){
		this.createGraphic(256, 16, "large_block.png");
		//fixed = true;
		//drag.y = 0.1f;
	}
	@Override
	public void collide(LSDSprite s, LSDSprite ground){
		//PApplet.println("Collided");
	}
	
}
