package com.autoStock.signal.extras;

import java.util.ArrayList;
import java.util.List;

import com.autoStock.tools.ListTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class EncogInputWindow {
	private List<List<Integer>> list = new ArrayList<List<Integer>>();
	public EncogInputWindow(){}
	
	public void addInputList(List<Integer> listOfInteger){
		list.add(listOfInteger);
	}
	
	public void addInputArray(int[] arrayOfInput){
		list.add((ArrayList<Integer>) ListTools.getListFromArray(arrayOfInput));
	}
	
	public int[] getAsWindow(){
		int items = 0;
		
		for (List<?> arrayList : list){
			for (Object object : arrayList){
				items++;
			}
		}
		
		int[] arrayOfInt = new int[items];
		int i=0;
		
		for (List<Integer> arrayList : list){
			for (Integer integer : arrayList){
				arrayOfInt[i] = integer;
				i++;
			}
		}
		
		return arrayOfInt;
	}
}