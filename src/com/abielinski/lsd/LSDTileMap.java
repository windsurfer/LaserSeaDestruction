package com.abielinski.lsd;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.abielinski.lsd.util.Rectangle;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * A Tilemap implementation of the sprite class. Displays a map of tiles as
 * loaded from a PNG and TXT file, comma separated, with each number relating to
 * the index of the tile in the PNG.
 * 
 * @author Adam
 */
public class LSDTileMap extends LSDSprite {
	/*
	 * Largely converted from from flixel
	 * https://github.com/AdamAtomic/flixel/blob/master/org/flixel/FlxTilemap.as
	 */

	/**
	 * No auto-tiling.
	 */
	static public final int			OFF		= 0;
	/**
	 * Platformer-friendly auto-tiling.
	 */
	static public final int			AUTO	= 1;
	/**
	 * Top-down auto-tiling.
	 */
	static public final int			ALT		= 2;
	
	/**
	 * The index on the map that, after which, collisions start
	 */
	public   int					collideIndex;
	protected int					startingIndex;
	
	protected int					drawIndex;
	
	protected int					widthInTiles;
	protected int					heightInTiles;
	protected int					totalTiles;
	
	/**
	 * Set this flag to use one of the 16-tile binary auto-tile algorithms (OFF,
	 * AUTO, or ALT).
	 */
	public int						auto;
	
	protected int					_tileHeight;
	protected int					_tileWidth;
	protected int					_screenRows;
	protected int					_screenCols;
	protected String				_bbKey;
	protected PImage				_pixels;
	protected LSDSprite				_block;
	protected ArrayList<Integer>	_data;
	protected int[]					_frames;
	
	/**
	 * 
	 */
	public LSDTileMap() {
		super();
		
		collideIndex = 1;
		startingIndex = 0;
		drawIndex = 1;
		
		widthInTiles = 0;
		heightInTiles = 0;
		totalTiles = 0;
		
		auto = OFF;
		
		_tileHeight = 16;
		_tileWidth = 16;
		
		fixed = true;
		
		_block = new LSDSprite();
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.abielinski.lsd.LSDSprite#createGraphic(int, int,
	 * java.lang.String)
	 */
	public void createGraphic(int wid, int hei, String src) {
		super.createGraphic(wid, hei, src);
		_tileWidth = wid;
		if (_tileWidth == 0)
			_tileWidth = _pixels.height;
		_tileHeight = hei;
		if (_tileHeight == 0)
			_tileHeight = _tileWidth;
	}
	
	/**
	 * @param mapLocation
	 *            The location of the map data to load.
	 * @return This object
	 */
	public LSDTileMap loadMap(String mapLocation) {
		// Figure out the map dimensions based on the data string
		
		String[] rows = LSDG.theParent.loadStrings(mapLocation);
		return loadRows(rows);
	}
	
	/**
	 * @param rows the rows of the tilemap
	 * @return this object
	 */
	public LSDTileMap loadRows(String[] rows){
		int c;
		String[] cols;
		
		heightInTiles = rows.length;
		_data = new ArrayList<Integer>();
		for (int r = 0; r < heightInTiles; r++){
			cols = rows[r].split(",");
			if (cols.length <= 1){
				heightInTiles--;
				continue;
			}
			if (widthInTiles == 0)
				widthInTiles = cols.length;
			for (c = 0; c < widthInTiles; c++){
				try{
					_data.add(Integer.decode(cols[c]));
				}catch(NumberFormatException e){
					PApplet.println("Error parsing string for tileMap: "+ cols[c]);
					//c--;
					//widthInTiles--;
					//TODO: Warn about it, maybe?
				}
			}

		}
		
		// Pre-process the map data if it's auto-tiled
		int i;
		totalTiles = widthInTiles * heightInTiles;
		if (auto > OFF){
			collideIndex = startingIndex = drawIndex = 1;
			for (i = 0; i < totalTiles; i++){
				autoTile(i);
			}
		}
		
		// Figure out the size of the tiles
		
		_block.w = _tileWidth;
		_block.h = _tileHeight;
		
		// Then go through and create the actual map
		w = widthInTiles * _tileWidth;
		h = heightInTiles * _tileHeight;
		_frames = new int[totalTiles];
		for (i = 0; i < totalTiles; i++){
			updateTile(i);
		}
		
		// Pre-set some helper variables for later
		_screenRows = (int) (Math.ceil(LSDG.theParent.height / _tileHeight) + 1);
		if (_screenRows > heightInTiles)
			_screenRows = heightInTiles;
		_screenCols = (int) (Math.ceil(LSDG.theParent.width / _tileWidth) + 1);
		if (_screenCols > widthInTiles)
			_screenCols = widthInTiles;
		
		// generateBoundingTiles();
		refreshHulls();
		
		// _buffer = new PImage(w, h);
		// render();
		
		return this;
	}
	
	/**
	 * An internal function used by the binary auto-tilers.
	 * 
	 * @param Index
	 *            The index of the tile you want to analyze.
	 */
	protected void autoTile(int Index) {
		if (_data.get(Index) == 0){
			return;
		}
		_data.set(Index, 0);
		if ((Index - widthInTiles < 0) || (_data.get(Index - widthInTiles) > 0)) // UP
			_data.set(Index, _data.get(Index) + 1);
		if ((Index % widthInTiles >= widthInTiles - 1) || (_data.get(Index + 1) > 0)) // RIGHT
			_data.set(Index, _data.get(Index) + 2);
		if ((Index + widthInTiles >= totalTiles) || (_data.get(Index + widthInTiles) > 0)) // DOWN
			_data.set(Index, _data.get(Index) + 4);
		if ((Index % widthInTiles <= 0) || (_data.get(Index - 1) > 0)) // LEFT
			_data.set(Index, _data.get(Index) + 8);
		if ((auto == ALT) && (_data.get(Index) == 15)) // The alternate algo
		// checks for interior
		// corners
		{
			if ((Index % widthInTiles > 0) && (Index + widthInTiles < totalTiles) && (_data.get(Index + widthInTiles - 1) <= 0))
				_data.set(Index, 1); // BOTTOM LEFT OPEN
			if ((Index % widthInTiles > 0) && (Index - widthInTiles >= 0) && (_data.get(Index - widthInTiles - 1) <= 0))
				_data.set(Index, 2); // TOP LEFT OPEN
			if ((Index % widthInTiles < widthInTiles) && (Index - widthInTiles >= 0) && (_data.get(Index - widthInTiles + 1) <= 0))
				_data.set(Index, 4); // TOP RIGHT OPEN
			if ((Index % widthInTiles < widthInTiles) && (Index + widthInTiles < totalTiles)
					&& (_data.get(Index + widthInTiles + 1) <= 0))
				_data.set(Index, 8); // BOTTOM RIGHT OPEN
		}
		_data.set(Index, _data.get(Index) + 1);
	}
	
	/**
	 * Internal function used in setTileByIndex() and the constructor to update
	 * the map.
	 * 
	 * @param Index
	 *            The index of the tile you want to update.
	 */
	protected void updateTile(int Index) {
		if (_data.get(Index) < drawIndex){
			_frames[Index] = 0;
			return;
		}
		_frames[Index] = _data.get(Index);
	}
	
	@Override
	public ArrayList<Rectangle> getHulls(Rectangle sprite){
		//PApplet.println("Tilemap! ");
		//TODO: cache this somehow
		if (sprite == null){
			PApplet.println("I got a null sprite given to me for collisions");
			return super.getHulls(sprite);
		}
		if (sprite instanceof LSDContainer){
			sprite = new LSDSprite();
			sprite.w = this.w;
			sprite.h = this.h;
		}
		hulls.clear();
		int spriteTileX = (int) Math.floor((sprite.pos.x - sprite.w/2.0f - this.pos.x)/(float)_tileWidth);
		int spriteTileY = (int) Math.floor((sprite.pos.y - sprite.h/2.0f - this.pos.y)/(float)_tileHeight);
		int spriteTileWidth = spriteTileX + (int) Math.ceil(sprite.w/(float)_tileWidth)+2; //TODO: Change this to 1
		int spriteTileHeight = spriteTileY + (int) Math.ceil(sprite.h/(float)_tileHeight)+2; // TODO: Change this to 1
		
		if(spriteTileX < 0){
			spriteTileX = 0;
		}
		if (spriteTileY< 0){
			spriteTileY = 0;
		}
		if (spriteTileWidth > widthInTiles){
			spriteTileWidth = widthInTiles;
		}
		if (spriteTileHeight > heightInTiles){
			spriteTileHeight = heightInTiles;
		}
		int rs = spriteTileY*widthInTiles;
		for(int ry = spriteTileY; ry < spriteTileHeight; ry++){
			for(int cx = spriteTileX; cx < spriteTileWidth; cx++){
				if(_data.get(rs+cx) >= collideIndex){
					hulls.add(new Rectangle(pos.x+cx*_tileWidth, pos.y+ry*_tileHeight, this._tileWidth, this._tileHeight));
				}
			}
			rs += widthInTiles;
		}

		//PApplet.println("tilewidth! " + _tileWidth);
		return hulls;
	}
	
	protected void renderTilemap() {
		LSDG.theParent.pushMatrix();
			
			LSDG.theParent.imageMode(PApplet.CENTER);
			PVector _point = new PVector();
			getScreenXY(_point);
			PVector _flashPoint = new PVector();
			_flashPoint.x = _point.x;
			_flashPoint.y = _point.y;
			
			int tx = (int) Math.floor(-_flashPoint.x / _tileWidth);
			int ty = (int) Math.floor(-_flashPoint.y / _tileHeight);
			if (tx < 0)
				tx = 0;
			if (tx > widthInTiles - _screenCols)
				tx = widthInTiles - _screenCols;
			if (ty < 0)
				ty = 0;
			if (ty > heightInTiles - _screenRows)
				ty = heightInTiles - _screenRows;
			int ri = ty * widthInTiles + tx;
			_flashPoint.x += tx * _tileWidth;
			_flashPoint.y += ty * _tileHeight;
			int opx = (int) _flashPoint.x;
			int c;
			int cri;
			for (int r = 0; r < _screenRows; r++){
				cri = ri;
				for (c = 0; c < _screenCols; c++){
					int i = _frames[cri];
					if (_flashPoint != null){
						LSDG.theParent.image(frames.get(i), _flashPoint.x, _flashPoint.y);
					}
					_flashPoint.x += _tileWidth;
					cri++;
				}
				ri += widthInTiles;
				_flashPoint.x = opx;
				_flashPoint.y += _tileHeight;
			}
		LSDG.theParent.popMatrix();
	}
	
	 /**
	* Check the value of a particular tile.
	*
	* @param X The X coordinate of the tile (in tiles, not pixels).
	* @param Y The Y coordinate of the tile (in tiles, not pixels).
	*
	* @return A uint containing the value of the tile at this spot in the array.
	*/
	public int getTile(int X,int Y){
		return getTileByIndex(Y * widthInTiles + X);
	}

	/**
	* Get the value of a tile in the tilemap by index.
	*
	* @param Index The slot in the data array (Y * widthInTiles + X) where this tile is stored.
	*
	* @return A uint containing the value of the tile at this spot in the array.
	*/
	public int getTileByIndex(int Index){
		return _data.get(Index);
	}
	
	/**
	 * Returns an array of sprites
	 * @param Index which tile to replace 
	 * @param className the exact, fully-qualified class name. MUST be an LSDSprite.
	 * @return The array of sprites in their proper place
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<LSDSprite> replaceTilesByIndex(int Index, String className){
		ArrayList<LSDSprite> _group = new ArrayList<LSDSprite>();
		Class<LSDSprite> c;
		try{
			c = (Class<LSDSprite>) Class.forName(className);
		}catch (ClassNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return _group;
		}
		if (c == null){
			return _group;
		}
		for(int i = 0; i< widthInTiles*heightInTiles;i++){
			if(getTileByIndex(i)==Index){
				LSDSprite newObject;
				@SuppressWarnings("rawtypes")
				Constructor con;
				try{
					con = c.getDeclaredConstructor(new Class[] { LSDG.theParent.getClass() });
				}catch (SecurityException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}catch (NoSuchMethodException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				try{
					newObject= (LSDSprite)con.newInstance(new Object[] { LSDG.theParent });;
				}catch (InstantiationException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}catch (IllegalAccessException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}catch (IllegalArgumentException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}catch (InvocationTargetException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				if(newObject != null){
					_group.add(newObject);
					newObject.pos.x = i%widthInTiles*_tileWidth;
					newObject.pos.y = (float) (Math.floor((float)i/(float)widthInTiles)*_tileHeight);
					_data.set(i, 0);
					updateTile(i);
				}
			}
		}
		return _group;
	}
	
	/**
	 * Returns an LSDContainer instead of an array, placed at 0,0.
	 * @param Index which tile to replace 
	 * @param className the exact, fully-qualified class name. MUST be an LSDSprite.
	 * @return a container filled with the specified sprite
	 */
	public LSDContainer getContainerFromIndexes(int Index, String className){
		ArrayList<LSDSprite> _group  = replaceTilesByIndex(Index, className);
		LSDContainer _container = new LSDContainer();
		for (LSDSprite s : _group){
			_container.add(s);
		}
		return _container;
	}
	
	/**
	 * Draws the tilemap.
	 */
	public void render() {
		renderTilemap();
	}
	
	@Override
	public void run() {
		super.run();
	}
	
	 /**
	* Called by <code>FlxObject.updateMotion()</code> and some constructors to
	* rebuild the basic collision data for this object.
	*/
	@Override 
	public void refreshHulls(){
		colHullX.pos.x = 0;
		colHullX.pos.y = 0;
		colHullX.w = _tileWidth;
		colHullX.h = _tileHeight;
		colHullY.pos.x = 0;
		colHullY.pos.y = 0;
		colHullY.w = _tileWidth;
		colHullY.h = _tileHeight;
	}
	
	@Override
	public void draw() {
		LSDG.theParent.pushMatrix();
			LSDG.theParent.translate(pos.x, pos.y);
			if (frames != null){
				if (flip){
					LSDG.theParent.scale(-1.0f, 1.0f);
				}
				if (this.scale - LSDG.roundingError < 1.0f || this.scale -+LSDG.roundingError > 1.0f ){
					LSDG.theParent.scale(this.scale, this.scale);
				}
				render();
			}
		LSDG.theParent.popMatrix();
	}
	
	@Override
	public void collide(LSDSprite s, LSDSprite a){
		// we don't care about collisions
	}
	
	@Override
	public void hitLeft(LSDSprite Contact,float Velocity)	{
		
	}
	
	@Override
	public void hitRight(LSDSprite Contact,float Velocity)	{
		
	}
	
	@Override
	public void hitTop(LSDSprite Contact,float Velocity){
		
	}
	
	@Override
	public void hitBottom(LSDSprite Contact,float Velocity){
		
	}
	
}
