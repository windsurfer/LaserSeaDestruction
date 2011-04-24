/**
* Written by Nik
  Debugged by Adam
*
*/


import com.abielinski.lsd.util.*;
import com.abielinski.lsd.*;
import com.abielinski.lsd.basic.*;

Player ship;
LSDContainer rocks;
LSDG lsd;
Maingame mainGame;
int enemyNumber;
Rocks rock;


float bulletSpeed = 0.4;
LSDContainer bullets;

void setup(){
  
  size(600,400);
  smooth();
  background(0); 
  lsd = new LSDG(this);
  LSDG.showHulls = true;
  enemyNumber = 10;
  mainGame = new Maingame();
  mainGame.init();
  rocks = new LSDContainer();
  for(int i =0;i<enemyNumber;i++){
    rocks.add(rock = new Rocks(random(0,width),random(0,height))); 
  }

  ship = new Player(width/2,height/2);
  
  mainGame.add(ship);
  mainGame.add(rocks);

  
  bullets = new LSDContainer();
  mainGame.add(bullets);
  
  LSDG.game = mainGame;
}

void draw(){
  
  background(20);
  LSDG.update();
  if(LSDG.collide(bullets, rocks)){
    println("Something hit");
  }
  if(LSDG.collide(ship,rocks)){
    println("Shields up Mr Sulu") ;
  }
  
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

class Bullet extends LSDSprite{
  float life;
  
 
  
  public Bullet(float xpos, float ypos){
     super(xpos,ypos);
     init(); 
  }
  
  void init(){
    this.createGraphic(12,12,"bullet.png");
    life = 600.0;
  }
  void run(){
    super.run();
    life-=LSDG.frameTime();
    if (life <=0){
      bullets.remove(this);
    }
  }
  
  void kill(){
    life = -1.0;
  }
  
  void collide(LSDSprite me, LSDSprite anything){
    this.kill();
    rocks.remove(anything); // won't remove if it's not part of rocks
  }
}

class Maingame extends LSDGame{
  
  public Maingame(){
    super();
  }
  
  void runRockCollide(){
     for(int i =0;i<children.size();i++){
       for(LSDSprite s:children){
         if(s.overlaps(children.get(i))){
           println("overlapping"); 
           //s.bump();
            //children.get(i).bump();
         }
       }
     }
  } 
  
  
}

class Player extends LSDSprite{
 
  int index;
  float slowDown;
  float thrust = 0.0005;
  float rotation = 0.005;
  
  
  float shotTimer;
  
  public Player(float xpos, float ypos){
     super(xpos,ypos);
     index = 1;
     slowDown = .98;
     init(); 
  }
  
  void init(){
   this.createGraphic(25,25,"ship.png");
   
   ArrayList<Integer> a = new ArrayList();
   a.add(0);
   
   this.addAnimation("idle",1,a);
   
   a = new ArrayList();
   a.add(1);
   a.add(2);
   this.addAnimation("moving",12,a);
   setAnimation("idle");
   
   drag.x = 0.001;
   drag.y = 0.001;
    accel.y = 0.0;
    accel.x = 0.0;
   shotTimer = 0.0;
   
  }
  
  void run(){
   super.run();
   if(pos.y > height)pos.y = 0; 
   if(pos.y < 0)pos.y = height; 
   if(pos.x < 0)pos.x = width;
   if(pos.x > width)pos.x = 0;
   
   if (LSDG.keys(LEFT)){
     angle -= rotation*LSDG.frameTime(); 
   }
   if (LSDG.keys(RIGHT)){
     angle += rotation*LSDG.frameTime(); 
   }
    if(LSDG.keys(UP)){
      bumpAtAngle(thrust,-PI/2.0);
      setAnimation("moving");
    }else if (LSDG.keys(DOWN)){
      bumpAtAngle(thrust,PI/2.0);
    }else{
      setAnimation("idle");
    }
    
    if(LSDG.keys(' ') && shotTimer <= 0){
      println("bang");
      fireBullet(pos,
        new PVector(cos(angle-PI/2.0), sin(angle-PI/2.0)));
      shotTimer = 150.0;
    }else if (shotTimer >0){
      shotTimer-=LSDG.frameTime();
    }
    
    if(LSDG.keys(CONTROL)){
      println("Sulu, prepare for warp manoeuvres!");
      pos.x = random(0,width);
      pos.y = random(0,height);
    }
  }
  
}

class Rocks extends LSDSprite{
  
//  int index;
  float slowDown;
  
  public Rocks(float xpos, float ypos){
     super(xpos,ypos);
     init();
  }
  
  void init(){
   this.createGraphic(50,50,"rock.png");
   ArrayList<Integer> a = new ArrayList();
   a.add(0);
   
   this.addAnimation("idle",1,a);
   
  
   setAnimation("idle");
   
   vel.x = random(-0.004,0.004);
   vel.y = random(-0.004,0.004);

  }
  
  void run(){
   super.run();
   //println("asteroids run");
   if(pos.y > height)pos.y = 0; 
   if(pos.y < 0)pos.y = height; 
   if(pos.x < 0)pos.x = width;
   if(pos.x > width)pos.x = 0;
  } 
    void collide(LSDSprite me, LSDSprite anything){
    //this.kill();
    rocks.remove(me); // won't remove if it's not part of rocks
  }
  
}
