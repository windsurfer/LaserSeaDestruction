package com.abielinski.lsd;

import java.util.ArrayList;
import java.util.HashMap;

import com.abielinski.lsd.util.CollisionCallback;
import com.abielinski.lsd.util.Rectangle;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 *	A basic sprite object that also has basic physics and animation support
 * @author Adam and Nik
 *
 */
public class LSDSprite extends Rectangle 
	implements CollisionCallback{
	/**
	 * The previous position of the sprite
	 */
	protected PVector prevPos;
	
	
	/**
	 * The difference from the last frame
	 */
	public PVector diffVector;
	
	/**
	 * The velocity of the sprite in x, y, and z
	 */
	public PVector vel;
	/**
	 * The acceleration of the sprite in x,y,z. Useful for gravity.
	 */
	public PVector accel;
	/**
	 * The "drag" of the sprite. It reduces velocity based on velocity. Useful for water.
	 */
	public PVector drag;
	
	/**
	 * The X collision hull
	 */
	public Rectangle colHullX;
	/**
	 * The Y collision hull
	 */
	public Rectangle colHullY;
	
	/**
	 * I'm not really sure how colHullX is better than this
	 */
	ArrayList<Rectangle> hulls;
	
	
	/**
	 * A value used in "push" and "pull" operations. 
	 * Defaults to 1.0
	 */
	public float mass = 1.0f;
	
	/**
	 * The "life" of an object, but this is unused. Defaults to 1.0f.
	 */
	public float life;
	
	/**
	 * How to scale the object. Larger values increase the size. Defaults to 1.0f
	 */
	public float scale = 1.0f;
	
	
	/**
	 * Rotation angle of the sprite
	 */
	public float angle;
	
	/**
	 * Whether the animation is flipped when drawn. Defaults to false.
	 */
	public boolean flip;
	
	/**
	 * References to all the PImages in order from the sprite map.
	 */
	public ArrayList<PImage> frames;
	
	/**
	 * references to animation objects
	 */
	public HashMap<String, LSDAnimation> animations;
	/**
	 * Internal counter for the animation timer.
	 */
	public int curAnimationTime;
	/**
	 * The currently playing animation.
	 */
	public LSDAnimation curAnimation;
	/**
	 * The animation the object is about the switch to. Switching causes the animation timer to reset. If the new animation is the
	 * same as the current animation, the timer is not reset.
	 */
	public LSDAnimation newAnimation;
	
	/**
	* The width of the actual graphic or image being displayed (not necessarily the game object/bounding box).
	* NOTE: Edit at your own risk!! This is intended to be read-only.
	*/
	public int frameWidth; // TODO: DO THIS
	/**
	* The height of the actual graphic or image being displayed (not necessarily the game object/bounding box).
	* NOTE: Edit at your own risk!! This is intended to be read-only.
	*/
	public int frameHeight; // TODO: DO THIS
	
	
	/**
	 * Whether this object is solid and can collide
	 */
	public boolean solid;
	
	/**
	 * Whether the object moves
	 */
	public boolean fixed;
	
	/**
	 * Whether the object is on the floor (only useful for platformers, really)
	 */
	public boolean onFloor;
	
	
	
	/**
	 * Uses defaults and places sprite at 0,0
	 */
	public LSDSprite() {
		construct();
	}
	
	/**
	 * Sets sprite at xpos,ypos
	 * @param xpos 
	 * @param ypos 
	 */
	public LSDSprite(float xpos, float ypos) {
		construct();
		pos.x = xpos;
		pos.y = ypos;
	}
	
	/**
	 * Resets the sprite. Calls an internal reset function by default, but can be overriden to change behaviour
	 */
	public void init(){
		construct();
	}
	
	private void construct() {
		pos = new PVector();
		prevPos = new PVector();
		diffVector = new PVector();
		vel = new PVector();
		accel = new PVector();
		drag = new PVector(0.0f, 0.0f); //  we're in outer space!
		w = 0;
		h = 0;
		
		
		fixed = false;
		solid = true;
		onFloor = true;
		mass = 1.0f;
		flip = false;
		angle = 0.0f;
		animations = new HashMap<String, LSDAnimation>();
		life = 1;
		colHullX = new Rectangle();
		colHullY = new Rectangle();
		hulls = new ArrayList<Rectangle>();
	}
	
	/**
	 * Loads a specified graphic and sends the parsed frames to LSDG.
	 * @param wid Width of each frame
	 * @param hei Height of each frame
	 * @param src The path to the sprite map
	 */
	public void createGraphic(int wid, int hei, String src ) {
		w = wid;
		h = hei;
		frames = LSDG.getFrames(src);
		if (frames == null){
			frames = new ArrayList<PImage>();
			PImage img;
			img = LSDG.loadImage(src);
			if (img == null){
				PApplet.println("Couldn't load image. Problem? :D");
				return;
			}
			for (int i = 0; i<= img.height-h; i+=h) {
				for (int j = 0; j <= img.width-w; j+=w){
					//PApplet.println("data:" + i+":"+  j+":"+ w+":"+ h);
					PImage g = img.get(j, i,wid,hei);
					frames.add(g);
				}
			}
		}
	}
	/**
	 * Adds a new animation sequence named name and also sets the current animation to it if nothing is playing.
	 * @param name The name of the new animation
	 * @param fRate The framerate of the animation in frames per second (fps)
	 * @param frames The frames of the animation in the form of an ArrayList of integers.
	 */
	public void addAnimation(String name,float fRate,ArrayList<Integer> frames) {
		if(frames == null || name == null|| animations == null){
			PApplet.println("Invalid arguments to addAnimation");
		}
		if (animations == null){
			animations = new HashMap<String, LSDAnimation>();
		}
		LSDAnimation newAnimation = new LSDAnimation(fRate,frames);
		
		animations.put(name, newAnimation);
		if (curAnimation == null) {
			setAnimation(name);
		}else{
			PApplet.print("Animation wasn't null,weird.\n");
		}
	}
	
	/**
	 * Sets the animation to play. If the animation is already running, this does nothing. If it doesn't exist, this will print
	 * a warning in your log.
	 * @param anim The name of the animation as it was set by @see addAnimation()
	 */
	public void setAnimation(String anim) {
		if (animations.containsKey(anim)) {
			newAnimation=(LSDAnimation)(animations.get(anim));
			
			
		}else{
			PApplet.print("That animation doesn't exist\n");
		}
	}
	private int curFrame() {
		if (curAnimation == null) {
			return 0;
		}
		int frame = curAnimationTime;
		int scaledFrame = (int) Math.floor(frame/curAnimation.fRate);
		
		if (curAnimation.loops == false && curAnimation.frames.size()<scaledFrame &&
				animations.containsKey("idle")){
			setAnimation("idle");
		}
		
		int index = (int)(scaledFrame % (float)curAnimation.frames.size());
		
		return curAnimation.frames.get(index);
	}
	
	
	/**
	 * This function is intended to be called every frame before the draw() function. It calculates physics and sets up any 
	 * animations.
	 */
	public  void run() {
		prevPos = pos.get();
		if (solid){
			refreshHulls();
		}
		onFloor = false;
		
		if (!fixed){
			pos.x += vel.x*LSDG.frameTime();
			pos.y += vel.y*LSDG.frameTime();
			

			diffVector.x = vel.x;
			diffVector.y = vel.y;
			vel.x+= accel.x*LSDG.frameTime();
			vel.y+= accel.y*LSDG.frameTime();
			vel.x /= (drag.x*LSDG.frameTime()+1);
			vel.y /= (drag.y*LSDG.frameTime()+1);
			

			diffVector.x = vel.x+(diffVector.x - vel.x)/2.0f;
			diffVector.y = vel.y+(diffVector.y - vel.y)/2.0f;
			
		}
		
		colHullX.w += ((diffVector.x > 0) ? diffVector.x : -diffVector.x);
		if (diffVector.x < 0)
			colHullX.pos.x += diffVector.x;
		colHullY.pos.x = pos.x;
		
		colHullY.h += ((diffVector.y > 0) ? diffVector.y : -diffVector.y);
		if (diffVector.y < 0)
			colHullY.pos.y += diffVector.y;
		// you need to draw later
		curAnimationTime += LSDG.frameTime(); // time since last frame
		if (newAnimation!=curAnimation){
			curAnimation = newAnimation;
			curAnimationTime = 0;
			newAnimation= curAnimation;
		}
	}
	/**
	 * This function draws the Sprite to the default buffer.
	 */
	public void draw() {
		LSDG.theParent.pushMatrix();
			LSDG.theParent.translate(pos.x,pos.y);
			if (angle < -LSDG.roundingError || angle > LSDG.roundingError){
				LSDG.theParent.rotate(angle);
			}
			LSDG.theParent.imageMode(PApplet.CENTER);
			if (frames != null){
				if(flip){
					LSDG.theParent.scale(-1.0f,1.0f);
				}
				LSDG.theParent.scale(this.scale, this.scale);
				LSDG.theParent.image(frames.get(curFrame()), 0,0);
			}
		LSDG.theParent.popMatrix();
	}
	
	
	/*      PHYSICS     */
	
	/**
	 * Refreshes the hulls used for physics calculations based on the sprite as it was loaded.
	 */
	public void refreshHulls(){
		colHullX.pos.x = pos.x;
		colHullX.pos.y = pos.y;
		colHullX.w = w;
		colHullX.h = h;
		colHullY.pos.x = pos.x;
		colHullY.pos.y = pos.y;
		colHullY.w = w;
		colHullY.h = h;
		
	}
	
	
	/**
	 * Returns the generated hulls used for physics calculations
	 * @param sprite The sprite that will be colliding with this sprite. Can be blank
	 * @return All the hulls that could possible collide with sprite
	 */
	public ArrayList<Rectangle> getHulls(LSDSprite sprite){
		hulls.clear();
		hulls.add(new Rectangle(0,0,w,h));
	    return hulls;
	}
	/**
	 * Make sure you call refreshHulls() before this! Normally that's called every run() cycle, but
	 * if you overrode that, it could be a problem.
	 * @return The hulls this object's physics work with.
	 */
	public Rectangle[] getHulls(){
		return new Rectangle[]{colHullX, colHullY};
	}
	
	/**
	 * Pulls the sprite towards point p based on strength. The mass of this object affects this.
	 * @param p The point to get this object pulled towards
	 * @param strength The strength of the attraction
	 */
	public void attract(PVector p, float strength) {
		float magnitude = (float) (Math.pow(pos.x-p.x, 2)+Math.pow(pos.y-p.y,2)); //sqrt^2 == nothing
		if (magnitude >65) {
			vel.x += (this.mass * strength)/magnitude*(pos.x-p.x);
			vel.y += (this.mass * strength)/magnitude*(pos.y-p.y);
		}
	}
	/**
	 * Pushes the sprite away from point p based on strength. The mass of this object affects this.
	 * @param p The point to get this object pushed away from
	 * @param strength The strength of the attraction
	 */
	public void push(PVector p, float strength) {
		attract(p, -strength);
	}
	/**
	 * Moves this object to point p
	 * @param p The point to move this Sprite to
	 */
	public void setPos(PVector p) {
		this.pos.x = p.x;
		this.pos.y = p.y;
	}
	/**
	 * @return the speed of the sprite
	 */
	public float speed(){
		return vel.mag();
	}
	
	protected PVector[] collideArea(){
		//there be dragons
		PVector[] area = new PVector[2];
		area[0] = new PVector();
		area[1] = new PVector();
		area[0].x = (float) (pos.x-w/2.0);
		area[1].x = (float) (pos.x+w/2.0);
		area[0].y = (float) (pos.y-h/2.0);
		area[1].y = (float) (pos.y+h/2.0);
		area[vel.x<0?0:1].x+= vel.x*LSDG.frameTime();
		area[vel.y<0?0:1].y+= vel.y*LSDG.frameTime();
		return area;
	}
	
	/**
	 * Moves the object back to it's previous position and reverses its velocity
	 */
	public void bump(){
		pos = prevPos.get();
		vel.mult(-1);
	}
	/**
	 * Bumps the player forward, based on <code>angle</code>
	 * @param force The force applied
	 */
	public void bumpForward(float force){
		vel.x+= (force/mass)*Math.cos(angle)*LSDG.frameTime();
		vel.y+= (force/mass)*Math.sin(angle)*LSDG.frameTime();
	}
	
	/**
	 * Bumps the player forward, based on <code>angle</code> and a supplied angle.
	 * @param force The force applied
	 * @param _angle The angle you wish to bump this at
	 */
	public void bumpAtAngle(float force, float _angle){
		vel.x+= (force/mass)*Math.cos(angle+_angle)*LSDG.frameTime();
		vel.y+= (force/mass)*Math.sin(angle+_angle)*LSDG.frameTime();
	}
	/**
	 * Tries to bump an object based on point p. Probably buggy.
	 * @param p The position that we're bumping from 
	 */
	public void bumpFrom(PVector p){
		// ANGLES ARE WRONG
		pos = prevPos.get();
		PVector e = p.get();
		e.sub(pos);
		float theta = PVector.angleBetween(vel,e);
		
		float z;
		if(vel.mag()<0.1|| Double.isNaN(Math.asin(theta))){
			z = 0;
		}else{
			z = (float) (Math.asin(theta)/vel.mag());
		}
		PVector Evn;
		if(theta>PApplet.PI){
			Evn = new PVector(e.y, -e.x); //HALF_PI counter clockwise
		}else{
			Evn = new PVector(-e.y, e.x); //HALF_PI clockwise
		}
		Evn.normalize();
		Evn.mult((float) (z/5.0));
		
		vel = Evn.get();
		
	}
	
	/**
	 * Called when this object's left side collides with another <code>LSDSprite</code>'s right.
	 *
	 * @param Contact The <code>LSDSprite</code> you just ran into.
	 * @param Velocity The suggested new velocity for this object.
	 */
	public void hitLeft(LSDSprite Contact,float Velocity)	{
		if(!fixed)
			vel.x = Velocity;
	}
	
	/**
	 * Called when this object's right side collides with another <code>LSDSprite</code>'s left.
	 *
	 * @param Contact The <code>LSDSprite</code> you just ran into.
	 * @param Velocity The suggested new velocity for this object.
	 */
	public void hitRight(LSDSprite Contact,float Velocity)	{
		hitLeft(Contact,Velocity);
	}
	
	/**
	 * Called when this object's top collides with the bottom of another <code>LSDSprite</code>.
	 *
	 * @param Contact The <code>LSDSprite</code> you just ran into.
	 * @param Velocity The suggested new velocity for this object.
	 */
	public void hitTop(LSDSprite Contact,float Velocity){
		if(!fixed)
			vel.y = Velocity;
	}
	
	/**
	 * Called when this object's bottom edge collides with the top of another <code>LSDSprite</code>.
	 *
	 * @param Contact The <code>LSDSprite</code> you just ran into.
	 * @param Velocity The suggested new velocity for this object.
	 */
	public void hitBottom(LSDSprite Contact,float Velocity){
		onFloor = true;
		if(!fixed){
			vel.y = Velocity;
		}
	}
	
	
	
	/**
	 * TODO: make this work ( I don't think it ever will without a graphics pipeline rework)
	 * @param p
	 */
	public void getScreenXY(PVector p){
		p.x = pos.x;
		p.y = pos.y;
	}
	
	/**
	 * This is called when the object is overlapping something. Override this if you want different functionality from dying.
	 * If you want to check for a collision, use <code>LSDG.collide()</code>
	 * @param Object1 the first object
	 * @param Object2 the second object
	 * @return whether this is overlapping
	 */
	public boolean overlapping(LSDSprite Object1,LSDSprite Object2){
		
		return true;
	}
	
	/**
	 * Whether this sprite is overlapping another, using width and height
	 * @param Obj The object to overlap
	 * @return whether they overlap
	 */
	public boolean overlaps(LSDSprite Obj){
		float tx = pos.x;
		float ty = pos.y;
		float tw = w;
		float th = h;
		float ox = Obj.pos.x;
		float oy = Obj.pos.y;
		float ow = Obj.w;
		float oh = Obj.h;
		if((ox <= tx-ow) || (ox >= tx+tw) || (oy <= ty-oh) || (oy >= ty+th)){
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.abielinski.lsd.util.CollisionCallback#collide(com.abielinski.lsd.LSDSprite, com.abielinski.lsd.LSDSprite)
	 */
	@Override
	public void collide(LSDSprite obj1, LSDSprite obj2) {
		PApplet.println("I've detected a collision, but I haven't done anything about it.");
		PApplet.println("in " + this.getClass().getName() );
		
	}


}
