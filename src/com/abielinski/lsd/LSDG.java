package com.abielinski.lsd;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class LSDG{
	  
	  public static LSDGame game;
	  public static long lastFrame;
	  public static long currentFrameTime;
	  public static PApplet theParent;
	  
	  public static HashMap<Object, Boolean> keys;
	  
	  public static HashMap<String, ArrayList<PImage>> cachedFrames= 
		  new HashMap<String, ArrayList<PImage>>();;
	  
	  public LSDG(){
		  LSDG.init();
	  }
	  
	  public LSDG(PApplet _theParent){
		  LSDG.setApplet(_theParent);
		  LSDG.init();
	  }
	  
	  static public void setApplet(PApplet _theParent){
		  LSDG.theParent = _theParent;
	  }
	  
	  static public void init(){
	    lastFrame = System.currentTimeMillis();
	    currentFrameTime = 0;
	    keys = new HashMap<Object, Boolean>();
	    cachedFrames = new HashMap<String, ArrayList<PImage>>();
	    //game.init();
	  }
	  
	  public static void update(){
	    long currentTime = System.currentTimeMillis();
	    currentFrameTime = currentTime-lastFrame;
	    lastFrame = currentTime; 
	    LSDG.run();
	    LSDG.draw();
	  }
	  
	  public static void run(){
	    if (game != null){
	      game.run();
	    }
	  }
	  public static void draw(){
	    if (game != null){
	      game.draw();
	    }
	  }
	  
	  public static int frameTime(){
	    return (int)currentFrameTime;
	  }
	  
	  public static void keyPress(char k){
	    keys.put(Character.toLowerCase(k),true);
	  }
	  public static void keyCodePress(int code){
	    keys.put(code,true);
	  }
	  public static void keyRelease(char k){
	    keys.put(Character.toLowerCase(k),false);
	  }
	  public static void keyCodeRelease(int code){
	    keys.put(code,false);
	  }
	  
	  public static boolean keys(Object k){
	    return ((keys.containsKey(k)) && (Boolean)(keys.get(k))); // does it contain k and is it true?
	  }
	  
	  public static PImage loadImage(String src){
		  if (theParent == null){
			 System.out.println("No parent set, it's your own goddamn fault ( ._.)");
			 return null;
		  }
		  return theParent.loadImage(src);
	  }
	  
	  
	  public static ArrayList<PImage> getFrames(String name){
	    if (name!=null && cachedFrames.containsKey(name)){
	      return cachedFrames.get(name);
	    }
	    return null;
	  }
	  
	  public static void addFrames(String name, ArrayList<PImage> frames){
	    if (name!=null && cachedFrames != null){
	      cachedFrames.put(name, frames);
	    }
	  }
	  
	  public static boolean collide(LSDSprite obj1, LSDSprite obj2){
	    if (obj1 == null || obj2 == null){
	      return false;
	    }
	    PVector[] area1 = obj1.collideArea();
	    PVector[] area2 = obj2.collideArea();
	    
	    boolean overlapX = 
	     (((area1[0].x > area2[0].x && 
	        area1[0].x < area2[1].x) ||
	       (area1[0].x < area2[0].x && 
	        area1[0].x > area2[1].x))||
	      ((area1[1].x > area2[0].x && 
	        area1[1].x < area2[1].x) ||
	       (area1[1].x < area2[0].x && 
	        area1[1].x > area2[1].x)));
	    boolean overlapY = 
	     (((area1[0].y > area2[0].y && 
	        area1[0].y < area2[1].y) ||
	       (area1[0].y < area2[0].y && 
	        area1[0].y > area2[1].y))||
	      ((area1[1].y > area2[0].y && 
	        area1[1].y < area2[1].y) ||
	       (area1[1].y < area2[0].y && 
	        area1[1].y > area2[1].y)));
	      
	    
	    return overlapX && overlapY;
	  }
	  
	  public static PVector collideLevel(LSDSprite sprite, LSDLevel level){
	    if (level == null || sprite == null){
	      return null;
	    }
	    PVector[] area = sprite.collideArea();
	    int w = (int)(area[1].x - area[0].x);
	    int h = (int)(area[1].y - area[0].y);
	    ArrayList<PVector> points= new ArrayList<PVector>();;
	    for (int i = (int)area[0].x; i<(int)area[1].x;i++){
	      for (int j = (int)area[0].y ; j<(int)area[1].y;j++){
	        if (level.collide(i,j)){
	          points.add(new PVector(i,j));
	        }
	      }
	    }
	    if(points.size()>0){
	      PVector closest = points.get(0);
	      float distance = closest.dist(sprite.pos);
	      for (PVector p : points){
	        float temp = p.dist(sprite.pos);
	        if(temp <distance){
	          closest = p;
	          distance = temp;
	        }
	      }
	      return closest;
	    }
	    
	    return null;
	  }
	  
	  // Code from Seb Lee-Delisle:
	  // http://sebleedelisle.com/2009/05/super-fast-trianglerectangle-intersection-test/
	 
	  public static boolean lineRectangleIntersect(float x1, float y1, float x2, float y2,
	                               float rx, float ry, float rw, float rh) {
	                                  
	    float topIntersection;
	    float bottomIntersection;
	    float topPoint;
	    float bottomPoint;
	   
	    // Calculate m and c for the equation for the line (y = mx+c)
	    float m = (y2-y1) / (x2-x1);
	    float c = y1 -(m*x1);
	   
	    // If the line is going up from right to left then the top intersect point is on the left
	    if(m > 0) {
	      topIntersection = (m*rx  + c);
	      bottomIntersection = (m*(rx+rw)  + c);
	    }
	    // Otherwise it's on the right
	    else {
	      topIntersection = (m*(rx+rw)  + c);
	      bottomIntersection = (m*rx  + c);
	    }
	   
	    // Work out the top and bottom extents for the triangle
	    if(y1 < y2) {
	      topPoint = y1;
	      bottomPoint = y2;
	    } else {
	      topPoint = y2;
	      bottomPoint = y1;
	    }
	   
	    float topOverlap;
	    float botOverlap;
	   
	    // Calculate the overlap between those two bounds
	    topOverlap = topIntersection > topPoint ? topIntersection : topPoint;
	    botOverlap = bottomIntersection < bottomPoint ? bottomIntersection : bottomPoint;
	   
	    return (topOverlap<botOverlap) && (!((botOverlap<ry) || (topOverlap>ry+rh)));
	   
	  }
	  
	  public static LSDSprite laserSmash(PVector laserOrigin,PVector laserTarget, LSDContainer smashed){
	    if (laserOrigin == null || laserTarget == null ||  smashed == null){
	      System.out.print("stop giving me null, man\n");
	      return null;
	    }
	    
	    for (LSDSprite s : smashed.children){
	      PVector[] p = s.collideArea();
	      if (lineRectangleIntersect(laserOrigin.x, laserOrigin.y, laserTarget.x, laserTarget.y,
	                                p[0].x,p[0].y,p[1].x,p[1].y)){
	         
	          return s;
	      }
	    }
	    return null;
	  }
	  
	  public static PVector getLaserPoint(PVector laserOrigin,PVector laserTarget, LSDLevel level){
	    PVector laserEnd = laserTarget.get();
	    laserEnd.sub(laserOrigin);
	    PVector laserNorm = laserEnd.get();
	    laserNorm.normalize();
	    PVector testPoint = laserOrigin.get();
	    int magnitude = (int) Math.ceil(laserEnd.mag());
	    int i = 0;
	    while(i<3000){ // this SHOULD exit at some point
	      i++;
	      laserNorm.normalize();
	      laserNorm.mult(i);
	      if (level.collide((int)(laserNorm.x+laserOrigin.x),(int)(laserNorm.y+laserOrigin.y))){
	        laserEnd = laserNorm.get();
	        laserEnd.add(laserOrigin);
	        break;
	      }
	    }
	    return laserEnd.get();
	  }
	}