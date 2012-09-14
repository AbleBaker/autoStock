package com.autoStock.cluster;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeUnitSupplier {
	private AtomicInteger atomicIntForRequestId = new AtomicInteger();
	
	public ComputeUnitForBacktest getNextComputeUnit(){
		ComputeUnitForBacktest computeUnitForBacktest = new ComputeUnitForBacktest();
		
		return computeUnitForBacktest;
	}
}
