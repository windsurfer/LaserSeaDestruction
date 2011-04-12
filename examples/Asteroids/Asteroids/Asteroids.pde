import com.abielinski.lsd.util.*;
import com.abielinski.lsd.*;
import com.abielinski.lsd.basic.*;

Player ship;
ArrayList<Rocks> rocks;
LSDG lsd;
Maingame mainGame;
int enemyNumber;
Rocks rock;


float bulletSpeed = 0.2;
LSDContainer bullets;

void setup(){
  
  size(600,400);
  smooth();
  background(0); 
  lsd = new LSDG(this);
  enemyNumber = 10;
  mainGame = new Maingame();
  mainGame.init();
  rocks = new ArrayList<Rocks>();
  for(int i =0;i<enemyNumber;i++){
    rocks.add(rock = new Rocks(random(0,width),random(0,height))); 
  }

  ship = new Player(width/2,height/2);
  
  mainGame.add(ship);
  for(Rocks r:rocks){
    mainGame.add(r);
  }
  
  bullets = new LSDContainer();
  mainGame.add(bullets);
  
  LSDG.game = mainGame;
}

void draw(){
  
  background(20);
  LSDG.update();  
  line(0,0,0,0);
  //mainGame.runRockCollide();
  
}

void fireBullet(PVector _pos, PVector _dir){
    if (bullets == null){
      bullets = new LSDContainer();
    }
    PVector direction = _dir.get();
    direction.normalize();
    direction.mult(bulletSpeed);
    Bullet bul = new Bullet(_pos.x, _pos.y);
    bul.vel = direction;
    bullets.add(bul);
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
