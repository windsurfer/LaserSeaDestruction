class Rocks extends LSDSprite{
  
//  int index;
  float slowDown;
  
  public Rocks(float xpos, float ypos){
     super(xpos,ypos);
      colHullX = new Rectangle(xpos,ypos,50,50);
     colHullY = new Rectangle(xpos,ypos,50,50);
     init(); 
     //vel.x = random(0.005,0.05);
     //vel.y = random(0.005,0.05);
  }
  
  void init(){
 
   this.createGraphic(50,50,"rock.png");
   ArrayList<Integer> a = new ArrayList();
   a.add(0);
   
   this.addAnimation("idle",1,a);
   
  
   setAnimation("idle");
   
   drag.x = 0.03;
   drag.y = 0.03;
   accel.x = random(-0.002,0.002);
   accel.y = random(-0.002,0.002);

   
  }
  
  void run(){
   super.run();
  // println("player run");
   if(pos.y > height)pos.y = 0; 
   if(pos.y < 0)pos.y = height; 
   if(pos.x < 0)pos.x = width;
   if(pos.y > width)pos.x = 0;
   //println(controls.playerMap.get(index));
  
    
  } 
  
}
