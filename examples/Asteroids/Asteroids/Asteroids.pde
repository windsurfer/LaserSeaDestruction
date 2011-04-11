import com.abielinski.lsd.util.*;
import com.abielinski.lsd.*;
import com.abielinski.lsd.basic.*;

Player ship;
Controls controls;
LSDG lsd;
Maingame mainGame;

void setup(){
  
  size(600,400);
  background(0); 
  lsd = new LSDG(this);
  lsd.init();

  mainGame = new Maingame();
  mainGame.init();
  
  controls = new Controls(this);

  ship = new Player(width/2,height/2);
  
  mainGame.add(ship);
  LSDG.game = mainGame;
}

void draw(){
  
  background(0);
  LSDG.update();
  controls.run();
  
}

void keyPressed(){
  if (key == CODED) {
    LSDG.keyCodePress(keyCode);
    
  }else{
    LSDG.keyPress(key);
  }
}

void keyReleased(){
  if (key == CODED) {
    LSDG.keyCodeRelease(keyCode);
  }else{
    LSDG.keyRelease(key);
  }
}
