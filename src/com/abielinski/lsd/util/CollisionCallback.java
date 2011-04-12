/**
 * 
 */
package com.abielinski.lsd.util;

import com.abielinski.lsd.LSDSprite;

/**
 * @author Adam
 * A callback to be used for collisions
 */
public interface CollisionCallback {
	/**
	 * @param obj1 the first colliding object
	 * @param obj2 the second colliding object
	 */
	void collide(LSDSprite obj1, LSDSprite obj2);
}
