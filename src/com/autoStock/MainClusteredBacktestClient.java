package com.autoStock;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktestClient {
	public MainClusteredBacktestClient() {
		start();
	}
	
	public void start(){
		Co.println("--> Requesting units...");
		while (requestNextComputeUnit()){
			
		}
	}
	
	public boolean requestNextComputeUnit(){
		
		
		return true;
	}
}
