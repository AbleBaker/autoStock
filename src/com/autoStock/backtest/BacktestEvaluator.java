package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.autoStock.Co;
import com.autoStock.tools.ListTools;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluator {
	private static final int bufferResults = 128;
	public static final int maxResults = 32;
	
	private ConcurrentHashMap<Symbol, ArrayList<BacktestEvaluation>> hashOfBacktestEvaluation = new ConcurrentHashMap<Symbol, ArrayList<BacktestEvaluation>>();

	public void addResult(Symbol symbol, BacktestEvaluation backtestEvaluation, boolean autoPrune){
		if (hashOfBacktestEvaluation.containsKey(symbol) == false){
			hashOfBacktestEvaluation.put(symbol, new ArrayList<BacktestEvaluation>(Arrays.asList(new BacktestEvaluation[]{backtestEvaluation})));
		}else{
			hashOfBacktestEvaluation.get(symbol).add(backtestEvaluation);
		}
		
		if (autoPrune){
			pruneResults(bufferResults);
		}
		
		Co.println("--> Added: " + symbol.symbolName + ", " + backtestEvaluation.accountBalance + ", " + backtestEvaluation.getScore());
	}
	
	public synchronized void pruneResults(int results){
		for (Symbol symbol : hashOfBacktestEvaluation.keySet()){
			ArrayList<BacktestEvaluation> listOfBacktestEvaluation = hashOfBacktestEvaluation.get(symbol);
			if (listOfBacktestEvaluation.size() <= results){
				continue;
			}
			
			Collections.sort(listOfBacktestEvaluation, new BacktestEvaluationComparator());
			hashOfBacktestEvaluation.put(symbol, new ArrayList<BacktestEvaluation>(listOfBacktestEvaluation.subList(0, results)));
		}
	}

	public void pruneAll() {
		pruneResults(maxResults);
	}

	public ArrayList<BacktestEvaluation> getResults(Symbol symbol) {
		return hashOfBacktestEvaluation.get(symbol);
	}

	public void reverse() {
		for (Symbol symbol : hashOfBacktestEvaluation.keySet()){
			ArrayList<BacktestEvaluation> listOfBacktestEvaluation = hashOfBacktestEvaluation.get(symbol);
			listOfBacktestEvaluation = (ArrayList<BacktestEvaluation>) ListTools.reverseList(listOfBacktestEvaluation);
			hashOfBacktestEvaluation.put(symbol, listOfBacktestEvaluation);
		}
	}
}
