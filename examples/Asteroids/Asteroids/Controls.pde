class Controls{
 
  public HashMap<Integer, ArrayList<Object>> playerMap;
  
  Controls(PApplet p){
    playerMap = new HashMap<Integer, ArrayList<Object>>();
  }
  
void run(){
    
    ArrayList<Object> stats = new ArrayList<Object>();
    if (LSDG.keys(LEFT)){
      stats.add(-1.0);
//      println("left");
    }else if (LSDG.keys(RIGHT)){
      stats.add(1.0);
//      println("right");
    }else{
      stats.add(0.0);
    }
    if (LSDG.keys(UP)){
      stats.add(true);
//      println("up");
    }else{
      stats.add(false);
    }
    
    if(LSDG.keys(CONTROL)){
      stats.add(true);
//      println("control");
    }else{
      stats.add(false);
    }
    
    playerMap.put(1,stats);
    
  }
  
}
