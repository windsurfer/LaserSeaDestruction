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
