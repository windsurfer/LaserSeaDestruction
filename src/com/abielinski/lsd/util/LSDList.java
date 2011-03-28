package com.abielinski.lsd.util;

import com.abielinski.lsd.LSDSprite;

/**
 * @author Adam
 * A simple linked list
 */
public class LSDList {
	/**
	* Stores a reference to a <code>FlxObject</code>.
	*/
	public LSDSprite object;
	/**
	* Stores a reference to the next link in the list.
	*/
	public LSDList next;

	/**
	* Creates a new link, and sets <code>object</code> and <code>next</code> to <code>null</null>.
	*/
	public LSDList(){
		object = null;
		next = null;
	}
}
