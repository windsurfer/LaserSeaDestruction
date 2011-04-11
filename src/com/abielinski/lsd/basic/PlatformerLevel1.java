/**
 * 
 */
package com.abielinski.lsd.basic;

/**
 * @author abielins
 *
 */
public class PlatformerLevel1 extends PlatformerLevel {
	public void init(){
		super.init();
		collisionMap.replaceTilesByIndex(0, "com.abielinski.lsd.LSDSprite");
	}
}
