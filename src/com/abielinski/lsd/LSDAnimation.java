package com.abielinski.lsd;

import java.util.ArrayList;

public class LSDAnimation {
	 public double fRate; // Milliseconds per frame
	 
	 // here is integers, but not for 
	 public ArrayList<Integer> frames;
	  
	 LSDAnimation(double fR, ArrayList<Integer> f){
	  fRate = 1000/fR; // 1000 Milliseconds in a second
	  frames = f;
	 } 
}
