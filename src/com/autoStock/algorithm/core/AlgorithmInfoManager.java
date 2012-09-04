package com.autoStock.algorithm.core;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmInfoManager {
	public ArrayList<AlgorithmInfo> listOfAlgorithmInfo = new ArrayList<AlgorithmInfo>();

	public void activatedSymbol(String symbol){
		listOfAlgorithmInfo.add(new AlgorithmInfo(new Date(), symbol));
	}
	
	public void deactivatedSymbol(String symbol){
		for (AlgorithmInfo algorithmInfo : listOfAlgorithmInfo){
			if (algorithmInfo.symbol.equals(symbol)){
				algorithmInfo.dateDeactivated = new Date();
			}
		}
	}
	
	public static class AlgorithmInfo{
		public Date dateActivated;
		public Date dateDeactivated;
		public String symbol;
		
		public AlgorithmInfo(Date arrivalDate, String symbol){
			this.dateActivated = arrivalDate;
			this.symbol = symbol;
		}
	}
}
