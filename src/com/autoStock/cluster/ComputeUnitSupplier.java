package com.autoStock.cluster;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeUnitSupplier {
	private AtomicInteger atomicIntForRequestId = new AtomicInteger();
	private static ComputeUnitSupplier instance = new ComputeUnitSupplier();
	
	public static ComputeUnitSupplier getInstance(){
		return instance;
	}
	
	public ComputeUnitForBacktest getNextComputeUnit(){
		ComputeUnitForBacktest computeUnitForBacktest = new ComputeUnitForBacktest();
		
		return computeUnitForBacktest;
	}
}
