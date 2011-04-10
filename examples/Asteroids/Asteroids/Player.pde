class Player extends LSDSprite{
 
  public Player(float xpos, float ypos){
     super(xpos,ypos);
     init(); 
  }
  
  void init(){
   println("player here"); 
   this.createGraphic(25,75,"ship.png");
   ArrayList<Integer> a = new ArrayList();
   a.add(1);
   
   this.addAnimation("idle",1,a);
   
   a = new ArrayList();
   
  }
  
}
