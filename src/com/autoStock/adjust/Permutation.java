package com.autoStock.adjust;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class Permutation {
	private volatile ArrayList<Iteration> listOfIteration = new ArrayList<Iteration>();
	private volatile boolean prepared;
	private volatile PermutationCore permutationCore;
	private int min = Integer.MAX_VALUE;
	private int max = Integer.MIN_VALUE;
	private volatile int count = 0;
	private volatile String[][] arrayOfStringResults;
	
	public void addIteration(Iteration iteration){
		if (prepared){throw new IllegalStateException("Permuation is already prepared. Create a new instance to add new items");}
		this.listOfIteration.add(iteration);
	}
	
	public void prepare(){
		String[] arrayOfString;
		
		for (Iteration iteration : listOfIteration){
			if (iteration.end >= max){max = iteration.end;}
			if (iteration.start <= min){min = iteration.start;}
		}
		
		if (max <= min){throw new IllegalArgumentException();}
		
		arrayOfString = new String[(max-min)+1];
		
		for (int i=0; i<arrayOfString.length; i++){
			arrayOfString[i] = String.valueOf(min+i);
		}
		
		permutationCore = new PermutationCore(arrayOfString, listOfIteration.size());
		
		prepared = true;
		
		arrayOfStringResults = permutationCore.getVariations();

	}
	
	private void filterArray(){
		for (int indexForPermutation = 0; indexForPermutation < arrayOfStringResults.length; indexForPermutation++){
			for (int indexForIteration = 0; indexForIteration < arrayOfStringResults[indexForPermutation].length; indexForIteration++){
				Iteration iteration = listOfIteration.get(indexForIteration);
				int value = Integer.valueOf(arrayOfStringResults[indexForPermutation][indexForIteration]);
				
//				Co.print("--> " + iteration.adjustment.name() + ", " + value + "     ");
				if (value > iteration.end || value < iteration.start){
//					Co.println("--> Should remove. Out of range of: " + iteration.adjustment.name() + ", " + iteration.start + ", " + iteration.end);
				}
			}
			
//			Co.println("");
			
			if (indexForPermutation > 0){
				Co.println("--> Filtering: " + MathTools.round(((double)indexForPermutation / (double)arrayOfStringResults.length) * 100));
			}
		}		
	}

	public synchronized boolean iterate(){
		LABEL : {
			int i = 0;
			
			if (count == arrayOfStringResults.length){
				return false;
			}

			for (Iteration iteration : listOfIteration){
				int currentValue = Integer.valueOf(arrayOfStringResults[count][i]);
				
				if (currentValue > iteration.end || currentValue < iteration.start){ // || currentValue %2 != 0
					Co.println("Failed at: " + iteration.adjustment.name() + ", " + iteration.start + "," + iteration.end + " / " + currentValue);
					count++;
					break LABEL;
				}
				
				iteration.setCurrentValue(currentValue);
				i++;
			}
			
			count++;
		}
		
		return true;
	}
	
	public synchronized Iteration getIteration(Object request) {
		for (Iteration iteration : listOfIteration){
			if (iteration.adjustment == request){
				return iteration;
			}
		}
		
		return null;
	}
	
	public synchronized ArrayList<Iteration> getListOfIterations(){
		return listOfIteration;
	}
	
	public double getPercentComplete(){
		if (permutationCore == null){
			return 0;
		}
		return (double)count / (double)permutationCore.getPermutationCount();
	}
}
