package com.abielinski.lsd;


import java.util.ArrayList;

public class LSDContainer extends LSDSprite {
	
	public ArrayList<LSDSprite>	children;
	public ArrayList<LSDSprite>	toDelete;
	public ArrayList<LSDSprite>	toAdd;
	public float				angle;
	public float				zoom;
	
	LSDContainer() {
		super();
		init(0, 0);
	}
	
	LSDContainer(float x, float y) {
		super();
		init(x, y);
	}
	
	private void init(float x, float y) {
		this.pos.x = x;
		this.pos.y = y;
		angle = 0;
		zoom = 1.0f;
		children = new ArrayList<LSDSprite>();
		toDelete = new ArrayList<LSDSprite>();
		toAdd = new ArrayList<LSDSprite>();
	}
	
	public void add(LSDSprite newChild) {
		addChild(newChild);
	}
	
	public void addChild(LSDSprite newChild) {
		toAdd.add(newChild);
	}
	
	public void remove(LSDSprite oldChild) {
		removeChild(oldChild);
	}
	
	public void removeChild(LSDSprite oldChild) {
		toDelete.add(oldChild);
	}
	
	public void run() {
		super.run(); // move myself... (I can have physisc!)
		for (LSDSprite s : children){
			s.run();
		}
		if (toDelete.size() > 0){
			for (LSDSprite s : toDelete){
				children.remove(s);
			}
			toDelete = new ArrayList<LSDSprite>();
		}
		if (toAdd.size() > 0){
			for (LSDSprite s : toAdd){
				children.add(s);
			}
			toAdd = new ArrayList<LSDSprite>();
		}
	}
	
	public void draw() {
		super.draw();
		// tell everyone else to draw
		LSDG.theParent.pushMatrix();
		LSDG.theParent.rotate(angle); // radians
		LSDG.theParent.scale(zoom);
		for (LSDSprite s : children){
			s.draw();
		}
		LSDG.theParent.popMatrix();
	}
	
}