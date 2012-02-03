/**
 * 
 */
package com.autoStock.exchange;

import com.autoStock.trading.platform.ib.IbExchangeInstance;

/**
 * @author Kevin Kowalewski
 *
 */
public class ExchangeController {
	public static IbExchangeInstance ibExchangeInstance;
	
	public static void init(){
		ibExchangeInstance = new IbExchangeInstance();
		ibExchangeInstance.init();
	}
	
	public static IbExchangeInstance getIbExchangeInstance(){
		return ibExchangeInstance;
	}
}
