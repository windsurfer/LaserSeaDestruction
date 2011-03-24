package com.abielinski.lsd;


import java.util.ArrayList;

import processing.core.PImage;

import com.abielinski.lsd.util.*;

public class LSDTileMap extends LSDSprite {
	/*
	 * Largely converted from from flixel
	 * https://github.com/AdamAtomic/flixel/blob/master/org/flixel/FlxTilemap.as
	 */

	/**
	 * No auto-tiling.
	 */
	static public final int		OFF		= 0;
	/**
	 * Platformer-friendly auto-tiling.
	 */
	static public final int		AUTO	= 1;
	/**
	 * Top-down auto-tiling.
	 */
	static public final int		ALT		= 2;
	
	public int					collideIndex;
	public int					startingIndex;
	
	public int					drawIndex;
	
	public int					widthInTiles;
	public int					heightInTiles;
	public int					totalTiles;
	
	/**
	 * Set this flag to use one of the 16-tile binary auto-tile algorithms (OFF,
	 * AUTO, or ALT).
	 */
	public int					auto;
	
	public int					_tileHeight;
	public int					_tileWidth;
	public int					_screenRows;
	public int					_screenCols;	
    public String				_bbKey;
	public PImage				_pixels;
	public LSDSprite			_block;
	public ArrayList<Integer>	_data;
	public ArrayList<Rectangle> _rects;
	
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
		
		_block = new LSDSprite();
	}
	
	public LSDTileMap loadMap(String MapData, PImage TileGraphic,
			int TileWidth, int TileHeight) {
		// Figure out the map dimensions based on the data string
		int c;
		String[] cols;
		String[] rows = MapData.split("\n");
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
			for (c = 0; c < widthInTiles; c++)
				_data.add(Integer.decode(cols[c]));
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
		_pixels = TileGraphic;
		_tileWidth = TileWidth;
		if (_tileWidth == 0)
			_tileWidth = _pixels.height;
		_tileHeight = TileHeight;
		if (_tileHeight == 0)
			_tileHeight = _tileWidth;
		_block.w = _tileWidth;
		_block.h = _tileHeight;
		
		// Then go through and create the actual map
		w = widthInTiles * _tileWidth;
		h = heightInTiles * _tileHeight;
		_rects = new ArrayList(totalTiles);
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
		
		
		//generateBoundingTiles();
		refreshHulls();
		
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
		if ((Index % widthInTiles >= widthInTiles - 1)
				|| (_data.get(Index + 1) > 0)) // RIGHT
			_data.set(Index, _data.get(Index) + 2);
		if ((Index + widthInTiles >= totalTiles)
				|| (_data.get(Index + widthInTiles) > 0)) // DOWN
			_data.set(Index, _data.get(Index) + 4);
		if ((Index % widthInTiles <= 0) || (_data.get(Index - 1) > 0)) // LEFT
			_data.set(Index, _data.get(Index) + 8);
		if ((auto == ALT) && (_data.get(Index) == 15)) // The alternate algo
														// checks for interior
														// corners
		{
			if ((Index % widthInTiles > 0)
					&& (Index + widthInTiles < totalTiles)
					&& (_data.get(Index + widthInTiles - 1) <= 0))
				_data.set(Index, 1); // BOTTOM LEFT OPEN
			if ((Index % widthInTiles > 0) && (Index - widthInTiles >= 0)
					&& (_data.get(Index - widthInTiles - 1) <= 0))
				_data.set(Index, 2); // TOP LEFT OPEN
			if ((Index % widthInTiles < widthInTiles)
					&& (Index - widthInTiles >= 0)
					&& (_data.get(Index - widthInTiles + 1) <= 0))
				_data.set(Index, 4); // TOP RIGHT OPEN
			if ((Index % widthInTiles < widthInTiles)
					&& (Index + widthInTiles < totalTiles)
					&& (_data.get(Index + widthInTiles + 1) <= 0))
				_data.set(Index, 8); // BOTTOM RIGHT OPEN
		}
		_data.set(Index, _data.get(Index) + 1);
	}
	
	/**
	 * Internal function used in setTileByIndex() and the constructor to update the map.
	 *
	 * @param Index The index of the tile you want to update.
	 */
	protected void updateTile(int Index)
	{
		if(_data.get(Index) < drawIndex)
		{
			_rects.set(Index, null);
			return;
		}
		int rx = (_data.get(Index)-startingIndex)*_tileWidth;
		int ry = 0;
		if(rx >= _pixels.width)
		{
			ry = (int)(rx/_pixels.width)*_tileHeight;
			rx %= _pixels.width;
		}
		_rects.set(Index, new Rectangle(rx,ry,_tileWidth,_tileHeight));
	}
}
