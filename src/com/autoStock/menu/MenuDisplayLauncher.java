/**
 * 
 */
package com.autoStock.menu;

import com.autoStock.display.DisplayHistoricalPrices;
import com.autoStock.display.DisplayMarketData;
import com.autoStock.menu.MenuDefinitions.MenuArguments;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalData.Resolution;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.trading.types.TypeMarketData;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MenuDisplayLauncher {
	public void launchDisplay(MenuStructures menuStructure){
		if (menuStructure == MenuStructures.menu_request_historical_prices){
			new DisplayHistoricalPrices(
					new TypeHistoricalData(
							menuStructure.getArgument(MenuArguments.arg_symbol).value, 
							menuStructure.getArgument(MenuArguments.arg_security_type).value,
							DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_start_date).value), 
							DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_end_date).value),
							Resolution.valueOf(menuStructure.getArgument(MenuArguments.arg_resolution).value)
							)
					).display();
		}
		else if (menuStructure == MenuStructures.menu_request_market_data){
			new DisplayMarketData(
					new TypeMarketData(
							menuStructure.getArgument(MenuArguments.arg_symbol).value,
							menuStructure.getArgument(MenuArguments.arg_security_type).value
							)
					).display();
			}
		else {
			throw new UnsatisfiedLinkError();
		}
	}
}
