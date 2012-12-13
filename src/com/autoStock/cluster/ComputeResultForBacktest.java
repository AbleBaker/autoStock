package com.autoStock.cluster;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentOfPortable;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeResultForBacktest {
	public long requestId;
	public ArrayList<ComputeResultForBacktestPartial> listOfComputeResultForBacktestPartial = new ArrayList<ComputeResultForBacktestPartial>();

	public ComputeResultForBacktest(long requestId, ArrayList<ComputeResultForBacktestPartial> listOfComputeResultForBacktestPartial) {
		this.listOfComputeResultForBacktestPartial = listOfComputeResultForBacktestPartial;
	}
}
