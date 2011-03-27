package com.abielinski.lsd.basic;

import java.util.ArrayList;

import processing.core.PImage;

import com.abielinski.lsd.LSDG;
import com.abielinski.lsd.LSDLevel;
import com.abielinski.lsd.LSDSprite;

public class BitmapLevel extends LSDLevel {
	/*
	 * A map of brightness values corrisponding to the collision layer
	 */
	int[][] collisionMap;
	
	/*
	 * The brightness that is "nothing" - used to inizialize the default state of the collisionMap
	 */
	public final int NOTHING = 0;
	/*
	 * The brightness at which destructable stuff starts (anything below this brightness is not collidable either)
	 */
	public final int DESTRUCTABLE = 100; // anything below destructable is not collidable
	/*
	 * Any brightness above this is collidable and not destructable
	 */
	public final int INDESTRUCTABLE = 101;
	
	/*
	 * A reference to the player
	 */
	public LSDSprite player;
	
	
	public ArrayList<PImage> layers;
	
	
	public BitmapLevel() {
		// put in the middle of the screen
		super(LSDG.theParent.width/2,LSDG.theParent.height/2);
		this.w = LSDG.theParent.width;
		this.h = LSDG.theParent.height;
		
		layers = new ArrayList<PImage>();
		collisionMap = new int[w][h];
	}
	
	public BitmapLevel(int wi, int he) {
		// put in the middle of the screen
		super(wi/2,he/2);
		this.w = wi;
		this.h = he;
		
		layers = new ArrayList<PImage>();
		collisionMap = new int[w][h];
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.abielinski.lsd.LSDLevel#init()
	 */
	public void init(){
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				collisionMap[i][j] = NOTHING;
			}
		}
		layers = new ArrayList<PImage>();
	}
	/*
	 * Call this to load a collision map in the form of a file with 
	 */
	public void loadCollisionMap(String file){
		PImage img = LSDG.loadImage(file);
		if (img != null){
			for(int i=0;i<w;i++){
				for(int j=0;j<h;j++){
					collisionMap[i][j] = (int)LSDG.theParent.brightness(img.get(i,j)); 
				}
			}
		}
		//addLayer(img);
	}
	
	public void loadLayer(String file){
		PImage img = LSDG.theParent.loadImage(file);
		addLayer(img);
	}
	
	public void addLayer(PImage img){
		layers.add(img);
	}
	
	public void run(){
		super.run();
		//I don't have anything to do...
	}
	
	public void draw(){
		super.draw();
		for(PImage img : layers){
			LSDG.theParent.image(img,0,0);
		}
	}
	
	public boolean collide(int i, int j){
		if (i<0 || i> w-1 || j<0 || j>h-1){
			return true; //out of bounds is a collision
		}
		return (collisionMap[i][j]<DESTRUCTABLE);
	}
}
