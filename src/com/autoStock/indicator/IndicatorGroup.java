package com.autoStock.indicator;

import java.util.ArrayList;

import com.autoStock.indicator.results.ResultsBB;
import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.indicator.results.ResultsDI;
import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.indicator.results.ResultsMFI;
import com.autoStock.indicator.results.ResultsROC;
import com.autoStock.indicator.results.ResultsRSI;
import com.autoStock.indicator.results.ResultsTRIX;
import com.autoStock.indicator.results.ResultsWILLR;
import com.autoStock.tools.AnalysisTools;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public class IndicatorGroup {
	public IndicatorOfCCI indicatorOfCCI;
	public IndicatorOfDI indicatorOfDI;
	public IndicatorOfMACD indicatorOfMACD;
	public IndicatorOfBB indicatorOfBB;
	public IndicatorOfTRIX indicatorOfTRIX;
	public IndicatorOfRSI indicatorOfRSI;
	public IndicatorOfROC indicatorOfROC;
	public IndicatorOfMFI indicatorOfMFI;
	public IndicatorOfWILLR indicatorOfWILLR;

	public ResultsCCI resultsCCI;
	public ResultsDI resultsDI;
	public ResultsBB resultsBB;
	public ResultsMACD resultsMACD;
	public ResultsRSI resultsRSI;
	public ResultsTRIX resultsTRIX;
	public ResultsROC resultsROC;
	public ResultsMFI resultsMFI;
	public ResultsWILLR resultsWILLR;

	private ArrayList<IndicatorBase> listOfIndicatorBase = new ArrayList<IndicatorBase>();

	public IndicatorGroup(int periodLength, CommonAnlaysisData commonAnlaysisData) {
		AnalysisTools.setIndicatorPeriodLength(periodLength, listOfIndicatorBase);
		
		indicatorOfCCI = new IndicatorOfCCI(periodLength, commonAnlaysisData);
		indicatorOfDI = new IndicatorOfDI(periodLength, commonAnlaysisData);
		indicatorOfMACD = new IndicatorOfMACD(periodLength, commonAnlaysisData);
		indicatorOfBB = new IndicatorOfBB(periodLength, commonAnlaysisData);
		indicatorOfRSI = new IndicatorOfRSI(periodLength, commonAnlaysisData);
		indicatorOfTRIX = new IndicatorOfTRIX(periodLength, commonAnlaysisData);
		indicatorOfROC = new IndicatorOfROC(periodLength, commonAnlaysisData);
		indicatorOfMFI = new IndicatorOfMFI(periodLength, commonAnlaysisData);
		indicatorOfWILLR = new IndicatorOfWILLR(periodLength, commonAnlaysisData);
		
		listOfIndicatorBase.add(indicatorOfCCI);
		listOfIndicatorBase.add(indicatorOfDI);
		listOfIndicatorBase.add(indicatorOfMACD);
		listOfIndicatorBase.add(indicatorOfBB);
		listOfIndicatorBase.add(indicatorOfRSI);
		listOfIndicatorBase.add(indicatorOfTRIX);
		listOfIndicatorBase.add(indicatorOfROC);
		listOfIndicatorBase.add(indicatorOfMFI);
		listOfIndicatorBase.add(indicatorOfWILLR);
	}
	
	public void setDataSet(ArrayList<QuoteSlice> listOfQuoteSlice, int periodLength){
		for (IndicatorBase indicator : listOfIndicatorBase){
			indicator.setDataSet(listOfQuoteSlice);
			indicator.periodLength = periodLength;
		}
	}

	public void analyize() {
		resultsCCI = indicatorOfCCI.analyize();
		resultsDI = indicatorOfDI.analize();
		resultsBB = indicatorOfBB.analyize();
		resultsMACD = indicatorOfMACD.analize();
		resultsRSI = indicatorOfRSI.analyize();
		resultsTRIX = indicatorOfTRIX.analyize();
		resultsROC = indicatorOfROC.analyize();
		resultsMFI = indicatorOfMFI.analyize();
		resultsWILLR = indicatorOfWILLR.analyize();
	}
	
	public ArrayList<IndicatorBase> getListOfIndicatorBase(){
		return listOfIndicatorBase;
	}
}
