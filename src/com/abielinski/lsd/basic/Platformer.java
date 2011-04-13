package com.abielinski.lsd.basic;

import processing.core.PApplet;

import com.abielinski.lsd.LSDG;
import com.abielinski.lsd.LSDGame;
import com.abielinski.lsd.LSDTileMap;

/**
 *This is an example game implementation for an example platformer.
 * @author Adam
 */
public class Platformer extends LSDGame {
	
	/**
	 * This is an example level. Normally you would load this from a file, but
	 */
	protected static final String[] level1_rows = new String[]{
		    "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
			"0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
			"0,0,0,0,0,0,0,9,8,7,7,6,6,6,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,4,4,4,4,4,0,0,0,0,0,0",
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3",
			"0,0,2,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3",
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,0,0,0",
			"1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1",
			"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"};
	
	/**
	 * The width of this game
	 */
	public final int gameWidth = 600;
	
	/**
	 * The height of this game
	 */
	public final int gameHeight = 268;
	
	
	protected PlatformerLevel level1;
	
	protected PlatformerPlayer player;
	
	protected PlatformerPlatform platform;
	
	
	/**
	 * Initilize the platformer with the defaults. Will tell your log you're using it!
	 */
	public Platformer() {
		super();
		PApplet.println("Using the default Platformer class. Extend this class to override defaults!");
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.abielinski.lsd.LSDSprite#init()
	 */
	public void init(){
		super.init();
		//LSDG.showHulls = true;
		
		level1 = new PlatformerLevel();
		
		level1.collisionMap= new LSDTileMap(); 
		level1.collisionMap.createGraphic(16,16,"basicPlatformerTiles.PNG");
		level1.collisionMap.loadRows(level1_rows);
		level1.widgetMap = new LSDTileMap();
		
		level1.init();
		this.add(level1);
		
		player = new PlatformerPlayer(32,32);
		
		this.add(player);
		
		platform = new PlatformerPlatform(128, 192);
		
		this.add(platform);
		
	}
	
	public void draw(){
		super.draw();
	}
	
	public void run(){
		super.run();
		LSDG.collide(player, level1.collisionMap);
		LSDG.collide(player, platform);
		if (player.pos.y > LSDG.theParent.height){
			player.pos.y = LSDG.theParent.height;
			player.vel.y = -0.1f;
		}
	}
	
}
