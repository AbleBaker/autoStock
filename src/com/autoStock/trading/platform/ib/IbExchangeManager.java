/**
 * 
 */
package com.autoStock.trading.platform.ib;

/**
 * @author Kevin Kowalewski
 *
 */
public class IbExchangeManager {
	static IbExchangeInstance ibExchangeInstance = new IbExchangeInstance();
	
	public IbExchangeManager(){
		ibExchangeInstance.init();
	}
	
	public static IbExchangeInstance getIbExchangeInstance(){
		return ibExchangeInstance;
	}
}
