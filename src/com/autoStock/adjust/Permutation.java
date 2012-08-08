package com.autoStock.adjust;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

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
			if (iteration.end > max){max = iteration.end;}
			if (iteration.start < min){min = iteration.start;}
		}
		
		arrayOfString = new String[max-min+1];
		
		for (int i=0; i<arrayOfString.length; i++){
			arrayOfString[i] = String.valueOf(min+i);
		}
		
		permutationCore = new PermutationCore(arrayOfString, listOfIteration.size());
		
		prepared = true;
		
		arrayOfStringResults = permutationCore.getVariations();

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
					Co.println("Failed at: " + iteration.start + "," + iteration.end + " / " + currentValue);
					count++;
					break LABEL;
				}
				
				iteration.current = currentValue;
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
	
	public ArrayList<Iteration> getListOfIterations(){
		return listOfIteration;
	}
	
	public class Iteration{
		private int start;
		private int end;
		private volatile int current;
		private AdjustmentDefinitions adjustment;
		public SignalMetricType signalTypeMetric;
		
		public Iteration(int start, int end, AdjustmentDefinitions adjustment){
			this.start = start;
			this.end = end;
			this.adjustment = adjustment;
		}
		
		public Iteration(AdjustmentDefinitions adjustment, SignalMetricType signalTypeMetric){
			this.start = adjustment.startValue;
			this.end = adjustment.endValue;
			this.adjustment = adjustment;
			this.signalTypeMetric = signalTypeMetric;
		}
		
		public synchronized double getCurrentValue(){
			return this.current;
		}
		
		public Object getRequest(){
			return this.adjustment;
		}
	}
	
	public double getPercentComplete(){
		return (double)count / (double)permutationCore.getPermutationCount();
	}
}
