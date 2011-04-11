class Player extends LSDSprite{
 
  int index;
  
  public Player(float xpos, float ypos){
     super(xpos,ypos);
     index = 0;
     init(); 
  }
  
  void init(){
   println("player here"); 
   this.createGraphic(25,25,"ship.png");
   ArrayList<Integer> a = new ArrayList();
   a.add(0);
   
   this.addAnimation("idle",1,a);
   
   a = new ArrayList();
   a.add(1);
   a.add(2);
   this.addAnimation("moving",2,a);
   setAnimation("idle");
   
   drag.x = 0.03;
    drag.y = 0.005;
    accel.y = 0.0;
//   onGround = false;
   
  }
  
  void run(){
   super.run();
  
   if(pos.y > height)pos.y = 0; 
   if(pos.y < 0)pos.y = height; 
   if(pos.x < 0)pos.x = width;
   if(pos.y > width)pos.x = 0;
  
   if (controls.playerMap.get(index) != null){
     
   angle = (Float)((ArrayList)controls.playerMap.get(index)).get(0); 
     
     if((Boolean)((ArrayList)controls.playerMap.get(index)).get(1)){
       accel.y = 0.002;
     }
     if((Boolean)((ArrayList)controls.playerMap.get(index)).get(2)){
       println("bang");
     }
      
    }
  
  } 
  
}
