package com.autoStock.signal.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ListTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class EncogInputWindow {
	private ArrayList<Double> listOfDouble = new ArrayList<Double>();
	public EncogInputWindow(){}
	public String debug = "";
	
	public void addInputList(List<Double> list){
		listOfDouble.addAll(list);
	}
	
	public void addInputArray(double[] arrayOfDouble){
		listOfDouble.addAll(ListTools.getListFromArray(arrayOfDouble));
	}
	
	public void addInput(double input){
		listOfDouble.add(input);
	}
	
	public double[] getAsWindow(){
		return ArrayTools.getArrayFromListOfDouble(listOfDouble);
	}

	public void eraseDebug() {
		debug = "";
	}
	
	public void addDebug(String string){
		debug += " " + string;
	}
}