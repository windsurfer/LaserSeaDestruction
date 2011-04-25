import com.abielinski.lsd.util.*;
import com.abielinski.lsd.*;
import com.abielinski.lsd.basic.*;

String spriteLoc = "man1.png";

LSDSprite man;


void setup(){
  size(300,300);
  new LSDG(this);// init library
  man = new LSDSprite(width/2, height/2); // center it
  
  man.createGraphic(60,100, spriteLoc);
  
  ArrayList a = new ArrayList();
  a.add(0);
  man.addAnimation("idle", 1, a);
  
  a = new ArrayList();
}

void draw(){
  background(0);
  man.run();
  man.draw();
  
}
