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

public class Asteroids extends PApplet {

/**
 * Written by Nik, Debugged by Adam
 * 
 * Controls:
 * Up, down, left, right and space bar
 */




Player ship;
LSDContainer rocks;
LSDG lsd;
Rock rock;


float bulletSpeed = 0.4f;
LSDContainer bullets;

public void setup() {
  size(600, 400);
  smooth();
  // init the library (required)
  lsd = new LSDG(this);

  // the "game" object is where the global logic goes. It's a type of container, too
  LSDG.game = new AsteroidsGame(); 

  // try setting this to true :)
  LSDG.showHulls = false;

  // the number of rocks we're going to create at random
  int enemyNumber = 10;

  // a container is an organizational thing. It's handy for collisions and such.
  rocks = new LSDContainer();
  for (int i =0;i<enemyNumber;i++) {
    rocks.add(rock = new Rock(random(0, width), random(0, height)));
  }

  // place a new player object at the center of the screen
  ship = new Player(width/2, height/2);

  // add everything we've created to game itself
  LSDG.game.add(ship);
  LSDG.game.add(rocks);

  // oh yeah, let's put in a bullet container as well :)
  bullets = new LSDContainer();
  LSDG.game.add(bullets);
}

public void draw() {
  background(0);

  // this is where the actual game happens!
  LSDG.update();
}

public class AsteroidsGame extends LSDGame {
  public void update(){
    LSDG.collide(bullets, rocks);
    LSDG.collide(ship, rocks);
  }
}

// fires a bullet from the specified position in the specified direction
public void fireBullet(PVector _pos, PVector _dir) {
  PVector direction = _dir.get();
  direction.normalize();
  direction.mult(bulletSpeed);
  Bullet bul = new Bullet(_pos.x, _pos.y);
  bul.vel = direction;
  bullets.add(bul);
}

class Bullet extends LSDSprite {
  float life;
  public Bullet(float xpos, float ypos) {
    super(xpos, ypos);
    init();
  }

  public void init() {
    this.createGraphic(12, 12, "bullet.png");
    life = 600.0f;
  }
  public void update(){
    life-=LSDG.frameTime();
    if (life <=0) {
      bullets.remove(this);
    }
  }

  public void kill() {
    life = -1.0f;
  }

  public void collide(LSDSprite me, LSDSprite anything) {
    this.kill(); // kill me :(
  }
}

class Player extends LSDSprite {


  // some "constants" that affect how we control the ship
  float thrust = 0.0005f;
  float rotation = 0.005f;


  float shotTimer;

  public Player(float xpos, float ypos) {
    super(xpos, ypos);
    init();
  }

  public void init() {
    this.createGraphic(25, 25, "ship.png");


    this.addAnimation("idle", 1, 0);

    this.addAnimation("moving", 12, 1, 2);
    setAnimation("idle");

    drag.x = 0.001f;
    drag.y = 0.001f;
    shotTimer = 0.0f;
  }

  public void update(){
    // makes this object loop around the screen
    if (pos.y > height)pos.y = 0; 
    if (pos.y < 0)pos.y = height; 
    if (pos.x < 0)pos.x = width;
    if (pos.x > width)pos.x = 0;

    if (LSDG.keys(LEFT)) {
      angle -= rotation*LSDG.frameTime();
    }
    if (LSDG.keys(RIGHT)) {
      angle += rotation*LSDG.frameTime();
    }
    if (LSDG.keys(UP)) {
      bumpAtAngle(thrust, -PI/2.0f);
      setAnimation("moving");
    }
    else if (LSDG.keys(DOWN)) {
      bumpAtAngle(thrust, PI/2.0f);
    }
    else {
      setAnimation("idle");
    }

    if (LSDG.keys(' ') && shotTimer <= 0) {

      fireBullet(pos, 
      new PVector(cos(angle-PI/2.0f), sin(angle-PI/2.0f)));
      shotTimer = 150.0f;
    }
    else if (shotTimer >0) {
      shotTimer-=LSDG.frameTime();
    }

    if (LSDG.keys(CONTROL)) {
      // super boost ;)
      bumpAtAngle(thrust*10, -PI/2.0f);
    }
  }
}

class Rock extends LSDSprite {

  public Rock(float xpos, float ypos) {
    super(xpos, ypos);
    init();
  }

  public void init() {
    this.createGraphic(50, 50, "rock.png");

    vel.x = random(-0.014f, 0.014f);
    vel.y = random(-0.014f, 0.014f);
  }

  public void update(){
    if (pos.y > height)pos.y = 0; 
    if (pos.y < 0)pos.y = height; 
    if (pos.x < 0)pos.x = width;
    if (pos.x > width)pos.x = 0;
  } 
  public void collide(LSDSprite me, LSDSprite anything) {
    if (scale >0.25f) {
      Rock newRock = new Rock(this.pos.x, this.pos.y);
      newRock.scale = this.scale/2.0f;
      newRock.w = this.w/2.0f;
      newRock.h = this.h/2.0f;
      rocks.add(newRock);
    }
    rocks.remove(me); // won't remove if it's not part of rocks
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "Asteroids" });
  }
}
