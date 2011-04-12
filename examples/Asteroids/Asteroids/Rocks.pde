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
  
  
}
