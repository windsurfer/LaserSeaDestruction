package com.abielinski.lsd;


import java.util.ArrayList;
import java.util.HashMap;

import com.abielinski.lsd.util.QuadTree;
import com.abielinski.lsd.util.Rectangle;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * A class with a collection of helpful functions and references.
 *         Put a reference to your game here, and run everything from here.
 * @author Adam 
 * 
 */
public class LSDG {
	
	/**
	 * The reference to your currently running game mode.
	 */
	public static LSDGame								game;
	
	/**
	 * The time at which the last frame was rendered. Used for phyiscs and animation.
	 */
	public static long									lastFrame;
	
	/**
	 * The current frame time. 
	 */
	public static long									currentFrameTime;
	
	/**
	 * A reference to the currently running PApplet instance. Needed for pretty much everything.
	 */
	public static PApplet								theParent;
	
	/**
	 * An internal collection of currently pressed keys. Used for keeping track of all keyboard stuff.
	 */
	public static HashMap<Object, Boolean>				keys;
	
	// some helper values
	
	/**
	 * The maximum allowed difference between two floats to be considered "equal"
	 */
	public static final float							roundingError	= 0.000001f;
	
	/**
	 * A collection of pre-parsed images. Used to speed up image loading.
	 */
	public static HashMap<String, ArrayList<PImage>>	cachedFrames	= new HashMap<String, ArrayList<PImage>>();
	
	/**
	 * Initializes the library. This is required to happen before pretty much everything else.
	 * @param _theParent a reference to the PApplet instance.
	 */
	public LSDG(PApplet _theParent) {
		LSDG.setApplet(_theParent);
		LSDG.init();
	}
	
	/**
	 * You don't normally need to call this, as it is handled in the constructor.
	 * @param _theParent A reference to the currently PApplet. 
	 */
	static public void setApplet(PApplet _theParent) {
		LSDG.theParent = _theParent;
	}
	
	/**
	 * Reinitializes the engine. Forgets all keys, empties the cache, etc.
	 */
	static public void init() {
		lastFrame = System.currentTimeMillis();
		currentFrameTime = 0;
		keys = new HashMap<Object, Boolean>();
		cachedFrames = new HashMap<String, ArrayList<PImage>>();
		// game.init();
	}
	
	/**
	 * Call this every frame. It runs the game!
	 */
	public static void update() {
		long currentTime = System.currentTimeMillis();
		currentFrameTime = currentTime - lastFrame;
		lastFrame = currentTime;
		LSDG.run();
		LSDG.draw();
	}
	
	
	/**
	 * This is called by update() automatically. Runs the logic for your game.
	 */
	public static void run() {
		if (game != null){
			game.run();
		}
	}
	
	/**
	 * This is called by update() automatically. Draws everything in the correct order.
	 */
	public static void draw() {
		if (game != null){
			game.draw();
		}
	}
	
	/**
	 * @return the current frame time.
	 */
	public static int frameTime() {
		return (int) currentFrameTime;
	}
	
	/**
	 * @param k records the currently pressed key char
	 */
	public static void keyPress(char k) {
		keys.put(Character.toLowerCase(k), true);
	}
	
	/**
	 * @param code records the currently pressed key int
	 */
	public static void keyCodePress(int code) {
		keys.put(code, true);
	}
	
	/**
	 * @param k records the currently un-pressed key char
	 */
	public static void keyRelease(char k) {
		keys.put(Character.toLowerCase(k), false);
	}
	
	/**
	 * @param code records the currently un-pressed key int
	 */
	public static void keyCodeRelease(int code) {
		keys.put(code, false);
	}
	
	/**
	 * @param k A char or int referring to a key (eg SPACE, ENTER, 'c', 'E' and '4' are all valid)
	 * @return whether it's currently pressed
	 */
	public static boolean keys(Object k) {
		return ((keys.containsKey(k)) && (Boolean) (keys.get(k))); // does it
																	// contain k
																	// and is it
																	// true?
	}
	
	/**
	 * Loads but does not cache a file.
	 * @param src the path to the image
	 * @return a reference to the PImage of the file, unless the file doesn't exist
	 */
	public static PImage loadImage(String src) {
		if (theParent == null){
			System.out
					.println("No parent set, it's your own goddamn fault ( ._.)");
			return null;
		}
		return theParent.loadImage(src);
	}
	
	/**
	 * @param name the path to a file
	 * @return the cached frames of that file. Null if it hasn't been cached yet.
	 */
	public static ArrayList<PImage> getFrames(String name) {
		if (name != null && cachedFrames.containsKey(name)){
			return cachedFrames.get(name);
		}
		return null;
	}
	
	/**
	 * Adds frames to the cache
	 * @param name the path to the file the frames came from.
	 * @param frames an ArrayList of PImages
	 */
	public static void addFrames(String name, ArrayList<PImage> frames) {
		if (name != null && cachedFrames != null){
			cachedFrames.put(name, frames);
		}
	}
	
	/**
	 * Call this function to see if one <code>FlxObject</code> collides with another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! It will put everything into a quad tree and then
	 * check for collisions. For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (even bundling groups together!)
	 * NOTE: does NOT take objects' scrollfactor into account.
	 *
	 * @param Object1 The first object or group you want to check.
	 * @param Object2 The second object or group you want to check. If it is the same as the first, flixel knows to just do a comparison within that group.
	 * @return whether they collide
	 */
	static public boolean collide(LSDSprite Object1,LSDSprite Object2){
		if( (Object1 == null) ||	(Object2 == null)){
			return false;
		}
		QuadTree quadTree = new QuadTree();
		quadTree.add(Object1,QuadTree.A_LIST);
		boolean match = Object1 == Object2;
		if(!match){
			quadTree.add(Object2,QuadTree.B_LIST);
		}
		boolean cx = quadTree.overlap(!match);
		 if(cx){ solveXCollision(Object1, Object2);}
		boolean cy = quadTree.overlap(!match);
		 if(cy){ solveYCollision(Object1, Object2);}
		return cx || cy;
	}
	
	
	/**
	 * This quad tree callback function can be used externally as well.
	 * Takes two objects and separates them along their X axis (if possible/reasonable).
	 *
	 * @param Object1 The first object or group you want to check.
	 * @param Object2 The second object or group you want to check.
	 * @return Whether it collides
	 */
	static public boolean solveXCollision(LSDSprite Object1,LSDSprite Object2){
		//Avoid messed up collisions ahead of time
		float o1 = Object1.vel.x;
		float o2 = Object2.vel.x;
		if(o1 == o2){
			return false;
		}
		
		//Basic resolution variables
		float overlap;
		boolean hit = false;
		boolean p1hn2;
		
		//Directional variables
		boolean obj1Stopped = o1 == 0;
		boolean obj1MoveNeg = o1 < 0;
		boolean obj1MovePos = o1 > 0;
		boolean obj2Stopped = o2 == 0;
		boolean obj2MoveNeg = o2 < 0;
		boolean obj2MovePos = o2 > 0;
		
		//Offset loop variables
		int i1;
		int i2;
		Rectangle obj1Hull = Object1.colHullX;
		Rectangle obj2Hull = Object2.colHullX;
		//ArrayList co1 = Object1.colOffsets;
		//ArrayList co2 = Object2.colOffsets;
		int l1 = 1;
		int l2 = 1;
		float ox1;
		float oy1;
		float ox2;
		float oy2;
		float r1;
		float r2;
		float sv1;
		float sv2;
		
		//Decide based on object's movement patterns if it was a right-side or left-side collision
		p1hn2 = ((obj1Stopped && obj2MoveNeg) || (obj1MovePos && obj2Stopped) || (obj1MovePos && obj2MoveNeg) || //the obvious cases
				(obj1MoveNeg && obj2MoveNeg && (((o1>0)?o1:-o1) < ((o2>0)?o2:-o2))) || //both moving left, obj2 overtakes obj1
				(obj1MovePos && obj2MovePos && (((o1>0)?o1:-o1) > ((o2>0)?o2:-o2))) ); //both moving right, obj1 overtakes obj2
		
		
		
		//this looks insane, but we're just looping through collision offsets on each object
		for(i1 = 0; i1 < l1; i1++)
		{
			ox1 = 0;
			oy1 = 0;
			obj1Hull.pos.x += ox1;
			obj1Hull.pos.y += oy1;
			for(i2 = 0; i2 < l2; i2++)
			{
				ox2 = 0;
				oy2 = 0;
				obj2Hull.pos.x += ox2;
				obj2Hull.pos.y += oy2;
				
				//See if it's a actually a valid collision
				if( (obj1Hull.pos.x + obj1Hull.w < obj2Hull.pos.x + roundingError) ||
						(obj1Hull.pos.x + roundingError > obj2Hull.pos.x + obj2Hull.w) ||
						(obj1Hull.pos.y + obj1Hull.h < obj2Hull.pos.y + roundingError) ||
						(obj1Hull.pos.y + roundingError > obj2Hull.pos.y + obj2Hull.h) )
				{
					obj2Hull.pos.x -= ox2;
					obj2Hull.pos.y -= oy2;
					continue;
				}
				
				//Calculate the overlap between the objects
				if(p1hn2)
				{
					if(obj1MoveNeg)
						r1 = obj1Hull.pos.x + Object1.colHullY.w;
					else
						r1 = obj1Hull.pos.x + obj1Hull.w;
					if(obj2MoveNeg)
						r2 = obj2Hull.pos.x;
					else
						r2 = obj2Hull.pos.x + obj2Hull.w - Object2.colHullY.w;
				}
				else
				{
					if(obj2MoveNeg)
						r1 = -obj2Hull.pos.x - Object2.colHullY.w;
					else
						r1 = -obj2Hull.pos.x - obj2Hull.w;
					if(obj1MoveNeg)
						r2 = -obj1Hull.pos.x;
					else
						r2 = -obj1Hull.pos.x - obj1Hull.w + Object1.colHullY.w;
				}
				overlap = r1 - r2;
				
				//Last chance to skip out on a bogus collision resolution
				if( (overlap == 0) ||
						((!Object1.fixed && ((overlap>0)?overlap:-overlap) > obj1Hull.w*0.8)) ||
						((!Object2.fixed && ((overlap>0)?overlap:-overlap) > obj2Hull.w*0.8)) )
				{
					obj2Hull.pos.x -= ox2;
					obj2Hull.pos.y -= oy2;
					continue;
				}
				hit = true;
				
				//Adjust the objects according to their flags and stuff
				sv1 = Object2.vel.x;
				sv2 = Object1.vel.x;
				if(!Object1.fixed && Object2.fixed)
				{
					Object1.pos.x -= overlap;
				}
				else if(Object1.fixed && !Object2.fixed)
				{
					Object2.pos.x += overlap;
				}
				else if(!Object1.fixed && !Object2.fixed)
				{
					overlap /= 2;
					Object1.pos.x -= overlap;
					Object2.pos.x += overlap;
					sv1 /= 2;
					sv2 /= 2;
				}
				if(p1hn2)
				{
					Object1.hitRight(Object2,sv1);
					Object2.hitLeft(Object1,sv2);
				}
				else
				{
					Object1.hitLeft(Object2,sv1);
					Object2.hitRight(Object1,sv2);
				}
				
				//Adjust collision hulls if necessary
				if(!Object1.fixed && (overlap != 0))
				{
					if(p1hn2)
						obj1Hull.w -= overlap;
					else
					{
						obj1Hull.pos.x -= overlap;
						obj1Hull.w += overlap;
					}
					Object1.colHullY.pos.x -= overlap;
				}
				if(!Object2.fixed && (overlap != 0))
				{
					if(p1hn2)
					{
						obj2Hull.pos.x += overlap;
						obj2Hull.w -= overlap;
					}
					else
						obj2Hull.w += overlap;
					Object2.colHullY.pos.x += overlap;
				}
				obj2Hull.pos.x -= ox2;
			obj2Hull.pos.y -= oy2;
		}
		obj1Hull.pos.x -= ox1;
		obj1Hull.pos.y -= oy1;
	}
	
	return hit;
	}
	
	/**
	 * This quad tree callback function can be used externally as well.
	 * Takes two objects and separates them along their Y axis (if possible/reasonable).
	 *
	 * @param Object1 The first object or group you want to check.
	 * @param Object2 The second object or group you want to check.
	 * @return Whether they collide
	 */
	static public boolean solveYCollision(LSDSprite Object1, LSDSprite Object2){
		//Avoid messed up collisions ahead of time
		float o1 = Object1.vel.y;
		float o2 = Object2.vel.y;
		if (o1 == o2)
			return false;
		
		// Basic resolution variables
		float overlap;
		boolean hit = false;
		boolean p1hn2;
		
		// Directional variables
		boolean obj1Stopped = o1 == 0;
		boolean obj1MoveNeg = o1 < 0;
		boolean obj1MovePos = o1 > 0;
		boolean obj2Stopped = o2 == 0;
		boolean obj2MoveNeg = o2 < 0;
		boolean obj2MovePos = o2 > 0;
		
		// Offset loop variables
		int i1;
		int i2;
		Rectangle obj1Hull = Object1.colHullY;
		Rectangle obj2Hull = Object2.colHullY;
		int l1 = 1;
		int l2 = 1;
		float ox1;
		float oy1;
		float ox2;
		float oy2;
		float r1;
		float r2;
		float sv1;
		float sv2;
		
		// Decide based on object's movement patterns if it was a top or bottom
		// collision
		p1hn2 = ((obj1Stopped && obj2MoveNeg) || (obj1MovePos && obj2Stopped) || (obj1MovePos && obj2MoveNeg) || // the
				// obvious
				// cases
				(obj1MoveNeg && obj2MoveNeg && (((o1 > 0) ? o1 : -o1) < ((o2 > 0) ? o2 : -o2))) || // both
				// moving
				// up,
				// obj2
				// overtakes
				// obj1
				(obj1MovePos && obj2MovePos && (((o1 > 0) ? o1 : -o1) > ((o2 > 0) ? o2 : -o2)))); // both
		// moving
		// down,
		// obj1
		// overtakes
		// obj2
		
		// this looks insane, but we're just looping through collision offsets
		// on each object
		for (i1 = 0; i1 < l1; i1++){
			ox1 = 0;
			oy1 = 0;
			obj1Hull.pos.x += ox1;
			obj1Hull.pos.y += oy1;
			for (i2 = 0; i2 < l2; i2++){
				ox2 = 0;
				oy2 = 0;
				obj2Hull.pos.x += ox2;
				obj2Hull.pos.y += oy2;
				
				// See if it's a actually a valid collision
				if ((obj1Hull.pos.x + obj1Hull.w < obj2Hull.pos.x + roundingError)
						|| (obj1Hull.pos.x + roundingError > obj2Hull.pos.x + obj2Hull.w)
						|| (obj1Hull.pos.y + obj1Hull.h < obj2Hull.pos.y + roundingError)
						|| (obj1Hull.pos.y + roundingError > obj2Hull.pos.y + obj2Hull.h)){
					obj2Hull.pos.x -= ox2;
					obj2Hull.pos.y -= oy2;
					continue;
				}
				
				// Calculate the overlap between the objects
				if (p1hn2){
					if (obj1MoveNeg)
						r1 = obj1Hull.pos.y + Object1.colHullX.h;
					else
						r1 = obj1Hull.pos.y + obj1Hull.h;
					if (obj2MoveNeg)
						r2 = obj2Hull.pos.y;
					else
						r2 = obj2Hull.pos.y + obj2Hull.h - Object2.colHullX.h;
				}else{
					if (obj2MoveNeg)
						r1 = -obj2Hull.pos.y - Object2.colHullX.h;
					else
						r1 = -obj2Hull.pos.y - obj2Hull.h;
					if (obj1MoveNeg)
						r2 = -obj1Hull.pos.y;
					else
						r2 = -obj1Hull.pos.y - obj1Hull.h + Object1.colHullX.h;
				}
				overlap = r1 - r2;
				
				// Last chance to skip out on a bogus collision resolution
				if ((overlap == 0) || ((!Object1.fixed && ((overlap > 0) ? overlap : -overlap) > obj1Hull.h * 0.8))
						|| ((!Object2.fixed && ((overlap > 0) ? overlap : -overlap) > obj2Hull.h * 0.8))){
					obj2Hull.pos.x -= ox2;
					obj2Hull.pos.y -= oy2;
					continue;
				}
				hit = true;
				
				// Adjust the objects according to their flags and stuff
				sv1 = Object2.vel.y;
				sv2 = Object1.vel.y;
				if (!Object1.fixed && Object2.fixed){
					Object1.pos.y -= overlap;
				}else if (Object1.fixed && !Object2.fixed){
					Object2.pos.y += overlap;
				}else if (!Object1.fixed && !Object2.fixed){
					overlap /= 2;
					Object1.pos.y -= overlap;
					Object2.pos.y += overlap;
					sv1 /= 2;
					sv2 /= 2;
				}
				if (p1hn2){
					Object1.hitBottom(Object2, sv1);
					Object2.hitTop(Object1, sv2);
				}else{
					Object1.hitTop(Object2, sv1);
					Object2.hitBottom(Object1, sv2);
				}
				
				// Adjust collision hulls if necessary
				if (!Object1.fixed && (overlap != 0)){
					if (p1hn2){
						obj1Hull.pos.y -= overlap;
						
					}else{
						obj1Hull.pos.y -= overlap;
						obj1Hull.h += overlap;
					}
				}
				if (!Object2.fixed && (overlap != 0)){
					if (p1hn2){
						obj2Hull.pos.y += overlap;
						obj2Hull.h -= overlap;
					}else{
						obj2Hull.h += overlap;
						
					}
				}
				obj2Hull.pos.x -= ox2;
				obj2Hull.pos.y -= oy2;
			}
			obj1Hull.pos.x -= ox1;
			obj1Hull.pos.y -= oy1;
	}
	
	return hit;
	}
	
	/**
	 * A simple and ineffecient way of seeing if two LSDSprite objects are colliding.
	 * @param obj1
	 * @param obj2
	 * @return whether they are colliding.
	 */
	public static boolean simpleCollide(LSDSprite obj1, LSDSprite obj2) {
		if (obj1 == null || obj2 == null){
			return false;
		}
		PVector[] area1 = obj1.collideArea();
		PVector[] area2 = obj2.collideArea();
		
		boolean overlapX = (((area1[0].x > area2[0].x && area1[0].x < area2[1].x) || (area1[0].x < area2[0].x && area1[0].x > area2[1].x)) || ((area1[1].x > area2[0].x && area1[1].x < area2[1].x) || (area1[1].x < area2[0].x && area1[1].x > area2[1].x)));
		boolean overlapY = (((area1[0].y > area2[0].y && area1[0].y < area2[1].y) || (area1[0].y < area2[0].y && area1[0].y > area2[1].y)) || ((area1[1].y > area2[0].y && area1[1].y < area2[1].y) || (area1[1].y < area2[0].y && area1[1].y > area2[1].y)));
		
		return overlapX && overlapY;
	}
	
	/**
	 * A simple and ineffecient way of seeing if an LSDSprite and an LSDLevel are colliding.
	 * @param sprite
	 * @param level
	 * @return whether they are colliding
	 */
	public static PVector simpleCollideLevel(LSDSprite sprite, LSDLevel level) {
		if (level == null || sprite == null){
			return null;
		}
		PVector[] area = sprite.collideArea();
		// int w = (int)(area[1].x - area[0].x);
		// int h = (int)(area[1].y - area[0].y);
		ArrayList<PVector> points = new ArrayList<PVector>();
		;
		for (int i = (int) area[0].x; i < (int) area[1].x; i++){
			for (int j = (int) area[0].y; j < (int) area[1].y; j++){
				if (level.collide(i, j)){
					points.add(new PVector(i, j));
				}
			}
		}
		if (points.size() > 0){
			PVector closest = points.get(0);
			float distance = closest.dist(sprite.pos);
			for (PVector p : points){
				float temp = p.dist(sprite.pos);
				if (temp < distance){
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
	
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param rx
	 * @param ry
	 * @param rw
	 * @param rh
	 * @return Whether a line is colliding with a rectangle.
	 */
	public static boolean lineRectangleIntersect(float x1, float y1, float x2,
			float y2, float rx, float ry, float rw, float rh) {
		
		float topIntersection;
		float bottomIntersection;
		float topPoint;
		float bottomPoint;
		
		// Calculate m and c for the equation for the line (y = mx+c)
		float m = (y2 - y1) / (x2 - x1);
		float c = y1 - (m * x1);
		
		// If the line is going up from right to left then the top intersect
		// point is on the left
		if (m > 0){
			topIntersection = (m * rx + c);
			bottomIntersection = (m * (rx + rw) + c);
		}
		// Otherwise it's on the right
		else{
			topIntersection = (m * (rx + rw) + c);
			bottomIntersection = (m * rx + c);
		}
		
		// Work out the top and bottom extents for the triangle
		if (y1 < y2){
			topPoint = y1;
			bottomPoint = y2;
		}else{
			topPoint = y2;
			bottomPoint = y1;
		}
		
		float topOverlap;
		float botOverlap;
		
		// Calculate the overlap between those two bounds
		topOverlap = topIntersection > topPoint ? topIntersection : topPoint;
		botOverlap = bottomIntersection < bottomPoint ? bottomIntersection
				: bottomPoint;
		
		return (topOverlap < botOverlap)
				&& (!((botOverlap < ry) || (topOverlap > ry + rh)));
		
	}
	
	/**
	 * @param laserOrigin
	 * @param laserTarget
	 * @param smashed
	 * @return calculates some laser smashing fun
	 */
	public static LSDSprite laserSmash(PVector laserOrigin,
			PVector laserTarget, LSDContainer smashed) {
		if (laserOrigin == null || laserTarget == null || smashed == null){
			System.out.print("stop giving me null, man\n");
			return null;
		}
		
		for (LSDSprite s : smashed.children){
			PVector[] p = s.collideArea();
			if (lineRectangleIntersect(laserOrigin.x, laserOrigin.y,
					laserTarget.x, laserTarget.y, p[0].x, p[0].y, p[1].x,
					p[1].y)){
				
				return s;
			}
		}
		return null;
	}
	
	/**
	 * @param laserOrigin
	 * @param laserTarget
	 * @param level
	 * @return Position that a "laser" would hit an LSDLevel
	 */
	public static PVector getLaserPoint(PVector laserOrigin,
			PVector laserTarget, LSDLevel level) {
		PVector laserEnd = laserTarget.get();
		laserEnd.sub(laserOrigin);
		PVector laserNorm = laserEnd.get();
		laserNorm.normalize();
		// PVector testPoint = laserOrigin.get();
		// int magnitude = (int) Math.ceil(laserEnd.mag());
		int i = 0;
		while (i < 3000){ // this SHOULD exit at some point
			i++;
			laserNorm.normalize();
			laserNorm.mult(i);
			if (level.collide((int) (laserNorm.x + laserOrigin.x),
					(int) (laserNorm.y + laserOrigin.y))){
				laserEnd = laserNorm.get();
				laserEnd.add(laserOrigin);
				break;
			}
		}
		return laserEnd.get();
	}
}