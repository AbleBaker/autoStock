package com.autoStock.adjust;
import com.autoStock.Co;


/**
 * @author Kevin Kowalewski
 * @param <E>
 *
 */
public class IterableOfEnum<E extends Enum<E>> extends IterableBase {
	private E enumObject;
	
	public IterableOfEnum(E enumObject){
		this.enumObject = enumObject;
		
		for (E enumbObjectLocal : enumObject.getDeclaringClass().getEnumConstants()){
			Co.println("--> Have: " + enumbObjectLocal.name());
		}
	}
	
	public E getEnum() {
		return enumObject.getDeclaringClass().getEnumConstants()[currentIndex];
	}
	
	public Class<E> getEnumObject(){
		return enumObject.getDeclaringClass();
	}
	
	@Override
	public boolean hasMore() {
		return currentIndex <= getMaxIndex();
	}

	@Override
	public int getMaxIndex() {
		return enumObject.getDeclaringClass().getEnumConstants().length-1;
	}

	@Override
	public int getMaxValues() {
		return enumObject.getDeclaringClass().getEnumConstants().length;
	}

	@Override
	public boolean isDone() {
		return currentIndex == getMaxIndex();
	}

	@Override
	public boolean skip() {
		return false;
	}
}
