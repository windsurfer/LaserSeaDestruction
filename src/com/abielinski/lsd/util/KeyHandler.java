/**
 * 
 */
package com.abielinski.lsd.util;

import java.awt.event.KeyEvent;

import processing.core.PApplet;

import com.abielinski.lsd.LSDG;

/**
 * @author Adam
 *
 */
public class KeyHandler {
	
	/**
	 * handler for LSDG
	 * @param event
	 */
	public void keyEvent(KeyEvent event){
		event.getID();
		
		char key = event.getKeyChar();
	    int keyCode = event.getKeyCode();
	    
	    switch (event.getID()) {
		    case KeyEvent.KEY_PRESSED:
		      if(key == PApplet.CODED){
		    	  LSDG.keyCodePress(keyCode);
		      }else{
		    	  LSDG.keyPress(key);
		      }
		      break;
		    case KeyEvent.KEY_RELEASED:
		      if(key == PApplet.CODED){
		    	  LSDG.keyCodeRelease(keyCode);
		      }else{
		    	  LSDG.keyRelease(key);
		      }
		      break;
		    case KeyEvent.KEY_TYPED:
		      // I don't know what to do here...?
		      break;
	    }
	}
	
}
