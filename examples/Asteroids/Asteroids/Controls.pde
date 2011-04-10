class Controls{
 
  public HashMap<Integer, ArrayList<Object>> playerMap;
  
  Controls(PApplet p){
    playerMap = new HashMap<Integer, ArrayList<Object>>();
  }
  
void run(){
    
    ArrayList<Object> stats = new ArrayList<Object>();
    if (LSDG.keys(LEFT)){
      stats.add(-1.0);
    }else if (LSDG.keys(RIGHT)){
      stats.add(1.0);
    }else{
      stats.add(0.0);
    }
    if (LSDG.keys(UP)){
      stats.add(true);
    }else{
      stats.add(false);
    }
    
    playerMap.put(1,stats);
    
  }
  
}
