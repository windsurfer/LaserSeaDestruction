/*
 * Written by Adam Bielinski
 * Hold mouse button to change animation
 * This shows off the time-based animation
 */

import com.abielinski.lsd.*;

String spriteLoc = "man1.png";

LSDSprite man;

void setup(){
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

void draw(){
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
