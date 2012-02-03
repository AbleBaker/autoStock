/**
 * 
 */
package com.autoStock.menu;

import com.autoStock.display.DisplayHistoricalPrices;
import com.autoStock.menu.MenuDefinitions.MenuArguments;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class MenuDisplayLauncher {
	public void launchDisplay(MenuStructures menuStructure){
		if (menuStructure == MenuStructures.menu_request_historical_prices){
			new DisplayHistoricalPrices(
					new TypeHistoricalData(menuStructure.getArgument(MenuArguments.arg_symbol).value, 
							DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_start_date).value), 
							DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_end_date).value))).display();
		}else {
			throw new UnsatisfiedLinkError();
		}
	}
}
