package com.abielinski.lsd;


import java.util.ArrayList;

/**
 * A container object for organizing objects in a game. 
 * @author Adam
 */
public class LSDContainer extends LSDSprite {
	
	/**
	 * The collection of LSDSprites. Can be other LSDContainers too.
	 */
	public ArrayList<LSDSprite>	children;
	/**
	 * A list of objects that will be deleted next frame.
	 */
	public ArrayList<LSDSprite>	toDelete;
	/**
	 * A list of objects that will be added next frame.
	 */
	public ArrayList<LSDSprite>	toAdd;
	
	/**
	 * Creates a new LSDContainer
	 */
	public LSDContainer() {
		super();
		init(0, 0);
	}
	
	/**
	 * Creates a new LSDContainer at x,y
	 * @param x
	 * @param y
	 */
	public LSDContainer(float x, float y) {
		super();
		init(x, y);
	}
	
	private void init(float x, float y) {
		this.pos.x = x;
		this.pos.y = y;
		//angle = 0;
		//zoom = 1.0f;
		children = new ArrayList<LSDSprite>();
		toDelete = new ArrayList<LSDSprite>();
		toAdd = new ArrayList<LSDSprite>();
	}
	
	/**
	 * @param newChild
	 */
	public void add(LSDSprite newChild) {
		addChild(newChild);
	}
	
	/**
	 * @param newChild
	 */
	public void addChild(LSDSprite newChild) {
		toAdd.add(newChild);
	}
	
	/**
	 * @param oldChild
	 */
	public void remove(LSDSprite oldChild) {
		removeChild(oldChild);
	}
	
	/**
	 * @param oldChild
	 */
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
		//LSDG.theParent.rotate(angle); // radians
		//LSDG.theParent.scale(zoom);
		for (LSDSprite s : children){
			s.draw();
		}
		LSDG.theParent.popMatrix();
	}
	
}