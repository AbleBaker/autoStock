package com.autoStock.adjust;

import java.util.ArrayList;

/**
 * @author Kevin Kowalewski
 *
 */
public class Permutation {
	private ArrayList<IterableBase> listOfIterableBase;
	
	public Permutation(ArrayList<IterableBase> listOfIterableBase){
		this.listOfIterableBase = listOfIterableBase;
	}
	
	public boolean masterIterate(){
		if (allDone()){
			return false;
		}
		
		for (IterableBase iterableBase : listOfIterableBase){
			if (iterableBase.skip()){
				continue;
			}
			
			iterableBase.iterate();
			
			if (iterableBase.hasMore() == false){
				iterableBase.reset();
				continue;
			}
			
			break;
		}
		
		return true;
	}
	
	public boolean allDone(){
		for (IterableBase iterableBase : listOfIterableBase){
			if (iterableBase.skip()){
				continue;
			}
			if (iterableBase.isDone() == false){
				return false;
			}
		}	
		return true;
	}
}
