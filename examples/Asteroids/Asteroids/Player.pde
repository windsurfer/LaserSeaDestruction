class Player extends LSDSprite{
 
  int index;
  float slowDown;
  float thrust = 0.0001;
  float rotation = 0.003;
  
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
   this.addAnimation("moving",2,a);
   setAnimation("idle");
   
   //drag.x = 0.03;
   // drag.y = 0.03;
    accel.y = 0.0;
    accel.x = 0.0;
//   onGround = false;
   
  }
  
  void run(){
   super.run();
   if(pos.y > height)pos.y = 0; 
   if(pos.y < 0)pos.y = height; 
   if(pos.x < 0)pos.x = width;
   if(pos.y > width)pos.x = 0;
   
   if (LSDG.keys(LEFT)){
     angle += rotation*LSDG.frameTime(); 
   }else if (LSDG.keys(RIGHT)){
     angle -= rotation*LSDG.frameTime(); 
   }
    drag.x = 0.0;
    drag.y = 0.0;
    if(LSDG.keys(UP)){
      bumpForward(thrust);
      setAnimation("moving");
    }else if (LSDG.keys(DOWN)){
      drag.x = 0.1;
      drag.y = 0.1;
    }else{
      setAnimation("idle");
    }
    
    if(LSDG.keys(' ')){
      println("bang");
    }
    
    
  }
  
}
