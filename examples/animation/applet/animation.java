import processing.core.*; 
import processing.xml.*; 

import com.abielinski.lsd.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class animation extends PApplet {

/*
 * Written by Adam Bielinski
 * Hold mouse button to change animation
 * This shows off the time-based animation
 */



String spriteLoc = "man1.png";

LSDSprite man;

public void setup(){
  size(300,300);
  new LSDG(this);// init library
  man = new LSDSprite(width/2, height/2); // center it
  
  // this is the width and height of the sprite's individual frames, *not* the entire PNG
  man.createGraphic(60,100, spriteLoc);
  
  // we don't use this, but it's normal to have an "idle" animation
  man.addAnimation("idle", 1, 0);
  
  // same frames, different frame rate :)
  man.addAnimation("walking", 12, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35);
  man.addAnimation("running", 24, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35);
  
  // we don't actually need to do this, since the sprite's animation defaults to the last animation added
  man.setAnimation("running");
  
  // we need to add the spirte to display it :)
  LSDG.game.add(man);
}

public void draw(){
  background(0);
  if(mousePressed){
    man.flip = true;
    man.setAnimation("running");
  }else{
    man.flip = false;
    man.setAnimation("walking");
  }
  LSDG.update();
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "animation" });
  }
}
