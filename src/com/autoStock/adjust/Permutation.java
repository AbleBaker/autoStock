package com.autoStock.adjust;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.Collections;

import com.autoStock.Co;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.StringTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class Permutation {
	private ArrayList<Iteration> listOfIteration = new ArrayList<Iteration>();
	private boolean prepared;
	private PermutationCore permutationCore;
	private int min = Integer.MAX_VALUE;
	private int max = Integer.MIN_VALUE;
	private int count = 0;
	private String[][] arrayOfStringResults;
	
	public void addIteration(Iteration iteration){
		if (prepared){throw new IllegalStateException("Permuation is already prepared. Create a new instance to add new items");}
		this.listOfIteration.add(iteration);
	}
	
	public void prepare(){
		String[] arrayOfString;
		
		for (Iteration iteration : listOfIteration){
			if (iteration.end > max){max = iteration.end;}
			if (iteration.start < min){min = iteration.start;}
		}
		
		arrayOfString = new String[max-min+1];
		
		for (int i=0; i<arrayOfString.length; i++){
			arrayOfString[i] = String.valueOf(min+i);
		}
		
		permutationCore = new PermutationCore(arrayOfString, listOfIteration.size());
		
		prepared = true;
		
//		Co.println("Min/max: " + min + "," + max);
		
		arrayOfStringResults = permutationCore.getVariations();
		ArrayTools.sort2DStringArray(arrayOfStringResults);
		
//		for (String[] string : arrayOfStringResults){
//			Co.println(StringTools.arrayOfStringToString(string));
//		}
	}

	public boolean iterate(){
		int i=0;
		
		if (count == arrayOfStringResults.length){
			return false;
		}
		
		for (Iteration iteration : listOfIteration){
			
			int currentValue = Integer.valueOf(arrayOfStringResults[count][i]);
			
			if (currentValue > iteration.end || currentValue < iteration.start){
				//Co.println("Failed at: " + iteration.start + "," + iteration.end + " / " + currentValue);
				count++;
				return iterate();
			}
			
			iteration.current = currentValue;
			i++;
		}
		
		count++;
		
		return true;
	}
	
	public Iteration getIteration(Object request) {
		for (Iteration iteration : listOfIteration){
			if (iteration.request == request){
				return iteration;
			}
		}
		
		return null;
	}
	
	public static class Iteration{
		private int start;
		private int end;
		private int current;
		private Object request;
		
		public Iteration(int start, int end, Object request){
			this.start = start;
			this.end = end;
			this.request = request;
		}
		
		public double getCurrentValue(){
			return this.current;
		}
	}
}
