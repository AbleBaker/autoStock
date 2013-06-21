package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluator {
	private static final int bufferResults = 128;
	public static final int maxResults = 16;
	
	private HashMap<Symbol, ArrayList<BacktestEvaluation>> hashOfBacktestEvaluation = new HashMap<Symbol, ArrayList<BacktestEvaluation>>();

	public void addResult(Symbol symbol, BacktestEvaluation backtestEvaluation, boolean autoPrune){
		if (hashOfBacktestEvaluation.containsKey(symbol) == false){
			hashOfBacktestEvaluation.put(symbol, new ArrayList<BacktestEvaluation>(Arrays.asList(new BacktestEvaluation[]{backtestEvaluation})));
		}else{
			hashOfBacktestEvaluation.get(symbol).add(backtestEvaluation);
		}
		
		if (autoPrune){
			pruneResults(bufferResults);
		}
	}
	
	public synchronized void pruneResults(int results){
		for (Symbol symbol : hashOfBacktestEvaluation.keySet()){
			ArrayList<BacktestEvaluation> listOfBacktestEvaluation = hashOfBacktestEvaluation.get(symbol);
			if (listOfBacktestEvaluation.size() <= results){
				continue;
			}
			
			Collections.sort(listOfBacktestEvaluation, new BacktestEvaluationComparator());
	
			hashOfBacktestEvaluation.remove(symbol);
			hashOfBacktestEvaluation.put(symbol, new ArrayList<BacktestEvaluation>(listOfBacktestEvaluation.subList(0, results)));
		}
	}

	public void pruneAll() {
		pruneResults(maxResults);
	}
}
