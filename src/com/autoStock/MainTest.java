package com.autoStock;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentCampaign.AdjustmentDefinitions;
import com.autoStock.adjust.Permutation.Iteration;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainTest {
	ArrayList<Iteration> listOfIteration = new ArrayList<Iteration>();
	ArrayList<ArrayList<String>> listOfResults = new ArrayList<ArrayList<String>>();
	
	public void doSomething(){
//		listOfIteration.add(new Iteration(0, 10, AdjustmentDefinitions.algo_signal_long_entry_cci));
//		listOfIteration.add(new Iteration(0, 10, AdjustmentDefinitions.algo_signal_long_entry_ppc));
//		listOfIteration.add(new Iteration(5, 8, AdjustmentDefinitions.algo_signal_weight_cci));
		
		permutate();
	}
	
	private void permutate(){
		for (Iteration iteration : listOfIteration){
			for (String string : getRange(iteration)){
				Co.print(string);
			}
			
			Co.println("\n");
		}
	}
	
	private ArrayList<String> getRange(Iteration iteration){
		ArrayList<String> listOfString = new ArrayList<String>();
		
		for (int i = iteration.start; i <= iteration.end; i++){
			listOfString.add(String.valueOf(i));
		}
		
		return listOfString;
	}
}
