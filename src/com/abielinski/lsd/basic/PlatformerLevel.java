/**
 * 
 */
package com.abielinski.lsd.basic;


import java.util.ArrayList;

import processing.core.PImage;

import com.abielinski.lsd.LSDLevel;
import com.abielinski.lsd.LSDTileMap;

/**
 * @author Adam
 *
 */
public class PlatformerLevel extends LSDLevel {
	/**
	 * the width in number of tiles
	 */
	protected int widthNum;
	
	protected int heightNum;
	
	protected int frameHeight, frameWidth;
	protected ArrayList<PImage> frames;
	
	/**
	 * The actual layer that players can collide with, but can also have background objects.
	 */
	public LSDTileMap collisionMap;
	
	/**
	 * The player does not collide with this, but tiles can be replaced with object instances
	 */
	public LSDTileMap widgetMap;
	
	
	public void init(){
		super.init();
		this.add(collisionMap);
		this.add(widgetMap);
	}
	
	
	
}
