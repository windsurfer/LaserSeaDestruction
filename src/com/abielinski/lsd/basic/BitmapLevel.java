package com.abielinski.lsd.basic;

import java.util.ArrayList;

import processing.core.PImage;

import com.abielinski.lsd.LSDG;
import com.abielinski.lsd.LSDLevel;
import com.abielinski.lsd.LSDSprite;

/**
 * @author Adam
 * An implementation of a level can use a bitmap for collisions
 */
public class BitmapLevel extends LSDLevel {
	/*
	 * A map of brightness values corrisponding to the collision layer
	 */
	int[][] collisionMap;
	
	/**
	 * the width in number of tiles
	 */
	protected int widthNum;
	
	protected int heightNum;
	
	/**
	 * The brightness that is "nothing" - used to inizialize the default state of the collisionMap
	 */
	public final int NOTHING = 0;
	/**
	 * The brightness at which destructable stuff starts (anything below this brightness is not collidable either)
	 */
	public final int DESTRUCTABLE = 100; // anything below destructable is not collidable
	/**
	 * Any brightness above this is collidable and not destructable
	 */
	public final int INDESTRUCTABLE = 101;
	
	/**
	 * A reference to the player
	 */
	public LSDSprite player;
	
	
	protected ArrayList<PImage> layers;
	
	
	/**
	 * Initialized the BitmapLevel to the width and hight of the screen
	 */
	public BitmapLevel() {
		// put in the middle of the screen
		super(LSDG.theParent.width/2,LSDG.theParent.height/2);
		this.w = LSDG.theParent.width;
		this.h = LSDG.theParent.height;
		
		layers = new ArrayList<PImage>();
		collisionMap = new int[heightNum][heightNum];
	}
	
	/**
	 * 
	 * Initilizes the BitmapLevel to a custom size
	 * @param wi Width
	 * @param he Height
	 */
	public BitmapLevel(int wi, int he) {
		// put in the middle of the screen
		super(wi/2,he/2);
		//this.w = wi;
		//this.h = he;
		widthNum = wi;
		heightNum = he;
		
		layers = new ArrayList<PImage>();
		collisionMap = new int[widthNum][heightNum];
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
	/**
	 * Call this to load a collision map in the form of a file with 
	 * @param file The location of the image to load in.
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
	
	/**
	 * Loads an image file as a displayable (but not collidable!) layer
	 * @param file The location of the image to load in.
	 */
	public void loadLayer(String file){
		PImage img = LSDG.theParent.loadImage(file);
		addLayer(img);
	}
	
	/**
	 * Sets a PImage as a displayable (but not collidable!) layer
	 * @param img the PImage to user as a layer
	 */
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
