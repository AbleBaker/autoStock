package com.autoStock.position;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionHistory {
	public ArrayList<Double> listOfProfitLossPercent = new ArrayList<Double>();
	public Date dateOfCreation;
	
	public void addProfitLoss(double value){
		listOfProfitLossPercent.add(value);
	}
	
	public double getMaxPercentProfitLoss(){
		return MathTools.getMaxDouble(listOfProfitLossPercent);
	}
	
	public double getMinPercentProfitLoss(){
		return MathTools.getMinDouble(listOfProfitLossPercent);
	}
}
