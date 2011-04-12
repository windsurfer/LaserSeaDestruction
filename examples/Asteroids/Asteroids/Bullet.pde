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
    rocks.remove(me); // won't remove if it's not part of rocks
    rocks.remove(anything); // won't remove if it's not part of rocks
  }
}