package com.abielinski.lsd;

import java.util.ArrayList;
import java.util.HashMap;

import com.abielinski.lsd.util.Rectangle;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class LSDSprite {
	public PVector pos;
	public PVector prevPos;
	public PVector vel;
	public PVector accel;
	public PVector drag;
	
	public Rectangle colHullX;
	public Rectangle colHullY;
	
	public int w, h;
	public float mass = 1.0f;
	public float life;
	public float scale = 1.0f;
	
	public boolean flip;
	
	// references to all the PImages in order from the sprite map
	public ArrayList<PImage> frames;
	
	// references to animation objects
	public HashMap<String, LSDAnimation> animations;
	public int curAnimationTime;
	public LSDAnimation curAnimation;
	public LSDAnimation newAnimation;
	
	LSDSprite() {
		init();
	}
	LSDSprite(float xpos, float ypos) {
		init();
		pos.x = xpos;
		pos.y = ypos;
	}
	
	private void init() {
		pos = new PVector();
		prevPos = new PVector();
		vel = new PVector();
		accel = new PVector();
		drag = new PVector(0.01f, 0.01f); //  we're under da sea!
		w = 0;
		h = 0;
		mass = 1.0f;
		flip = false;
		animations = new HashMap<String, LSDAnimation>();
		life = 1;
		colHullX = new Rectangle();
		colHullY = new Rectangle();
	}
	public void createGraphic(int wid, int hei, String src ) {
		w = wid;
		h = hei;
		frames = LSDG.getFrames(src);
		if (frames == null){
			frames = new ArrayList<PImage>();
			PImage img;
			img = LSDG.loadImage(src);
			for (int i = 0; i< img.height; i+=h) {
				for (int j = 0; j < img.width; j+=w){
					frames.add(img.get(i, j,w,h));
				}
			}
		}
	}
	public void addAnimation(String anim,float fRate,ArrayList<Integer> frames) {
		LSDAnimation newAnimation = new LSDAnimation(fRate,frames);
		animations.put(anim, newAnimation);
		if (curAnimation == null) {
			setAnimation(anim);
		}else{
			System.out.print("Animation wasn't null,weird.\n");
		}
	}
	
	public void setAnimation(String anim) {
		if (animations.containsKey(anim)) {
			newAnimation=(LSDAnimation)(animations.get(anim));
			
			
		}else{
			System.out.print("That animation doesn't exist\n");
		}
	}
	private int curFrame() {
		if (curAnimation == null) {
			return 0;
		}
		int frame = curAnimationTime;
		int scaledFrame = (int) Math.floor(frame/curAnimation.fRate);
		
		int index = (int)(scaledFrame % (float)curAnimation.frames.size());
		
		return curAnimation.frames.get(index);
	}
	
	
	public  void run() {
		vel.x+= accel.x*LSDG.frameTime();
		vel.y+= accel.y*LSDG.frameTime();
		vel.x /= (drag.x*LSDG.frameTime()+1);
		vel.y /= (drag.y*LSDG.frameTime()+1);
		prevPos = pos.get();
		pos.x += vel.x*LSDG.frameTime();
		pos.y += vel.y*LSDG.frameTime();
		// you need to draw later
		curAnimationTime += LSDG.frameTime(); // time since last frame
		if (newAnimation!=curAnimation){
			curAnimation = newAnimation;
			curAnimationTime = 0;
		}
	}
	public void draw() {
		LSDG.theParent.translate(pos.x,pos.y);
		LSDG.theParent.imageMode(PApplet.CENTER);
		if (frames != null){
			if(flip){
				LSDG.theParent.scale(-1.0f,1.0f);
			}
			LSDG.theParent.scale(this.scale, this.scale);
			LSDG.theParent.image(frames.get(curFrame()), 0,0);
		}
		
	}
	
	
	/*      PHYSICS     */
	
	public void refreshHulls(){
		colHullX.x = pos.x;
		colHullX.y = pos.y;
		colHullX.width = w;
		colHullX.height = h;
		colHullY.x = pos.x;
		colHullY.y = pos.y;
		colHullY.width = w;
		colHullY.height = h;
	}
	public void attract(PVector p, float strength) {
		float magnitude = (float) (Math.pow(pos.x-p.x, 2)+Math.pow(pos.y-p.y,2)); //sqrt^2 == nothing
		if (magnitude >65) {
			vel.x += (this.mass * strength)/magnitude*(pos.x-p.x);
			vel.y += (this.mass * strength)/magnitude*(pos.y-p.y);
		}
	}
	public void push(PVector p, float strength) {
		attract(p, -strength);
	}
	public void setPos(PVector p) {
		this.pos.x = p.x;
		this.pos.y = p.y;
	}
	public float speed(){
		return vel.mag();
	}
	
	public PVector[] collideArea(){
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
	
	public void bump(){
		pos = prevPos.get();
		vel.mult(-1);
	}
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
	
	public void getScreenXY(PVector p){
		p.x = 0;
		p.y = 0;
	}
	
}
