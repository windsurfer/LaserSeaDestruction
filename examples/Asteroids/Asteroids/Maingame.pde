class Maingame extends LSDGame{
  
  public Maingame(){
    
  }
  
  void runRockCollide(){
     for(int i =0;i<children.size();i++){
       for(LSDSprite s:children){
         //if(s.overlaps(s,children.get(i))){
           //s.bump();
           //children.get(i).bump();
        // }
       }
     }
  } 
  
}
