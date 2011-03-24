package com.abielinski.lsd;

import java.util.ArrayList;

import processing.core.PImage;

public class LSDLevel extends LSDContainer{
	  int[][] collisionMap;
	  
	  public ArrayList<PImage> layers;
	  
	  public final int NOTHING = 0;
	  public final int DESTRUCTABLE = 100; // anything below destructable is not collidable
	  public final int INDESTRUCTABLE = 101;
	  public LSDSprite player;
	  
	  public final int BRIGHTNESS = 128;
	  
	LSDLevel(){
	    super(LSDG.theParent.width/2,LSDG.theParent.height/2);
	    this.w = LSDG.theParent.width;
	    this.h = LSDG.theParent.height;
	    
	    collisionMap = new int[w][h];
	    layers = new ArrayList<PImage>();
	    
	    
	  }
	  LSDLevel(int wi, int he){
	    super(wi/2,he/2);
	    this.w = wi;
	    this.h = he;
	    
	    collisionMap = new int[w][h];
	    layers = new ArrayList<PImage>();
	  }
	  
	  public void init(){
	    for(int i=0;i<w;i++){
	      for(int j=0;j<h;j++){
	        collisionMap[i][j] = NOTHING;
	      }
	    }
	    layers = new ArrayList<PImage>();
	  }
	  
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