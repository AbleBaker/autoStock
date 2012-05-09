/**
 * 
 */
package com.autoStock.menu;

import com.autoStock.MainBacktest;
import com.autoStock.database.BuildDatabaseDefinitions;
import com.autoStock.display.DisplayHistoricalPrices;
import com.autoStock.display.DisplayMarketData;
import com.autoStock.display.DisplayRealtimeData;
import com.autoStock.menu.MenuDefinitions.MenuArguments;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.MarketData;
import com.autoStock.trading.types.RealtimeData;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MenuDisplayLauncher {
	public void launchDisplay(MenuStructures menuStructure){
		if (menuStructure == MenuStructures.menu_request_historical_prices){
			new DisplayHistoricalPrices(
					new HistoricalData(
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
					new MarketData(
							menuStructure.getArgument(MenuArguments.arg_symbol).value,
							menuStructure.getArgument(MenuArguments.arg_security_type).value
							)
					).display();
			}
		
		else if (menuStructure == MenuStructures.menu_request_realtime_data){
			new DisplayRealtimeData(
					new RealtimeData(
							menuStructure.getArgument(MenuArguments.arg_symbol).value,
							menuStructure.getArgument(MenuArguments.arg_security_type).value
							)
					).display();
		}
		
		else if (menuStructure == MenuStructures.menu_test_market_data){
			int[] arrayOfTestSymbols = new int[]{9437,8306,8035,8058,9104,5401,3382,6752,7751,4503,7203,7011,7270,1878,6301,8001,6971,6954,1605,7731,8002,9984,9831,5202,7267,4452,7201,9101,8411,8750,9107,9020,4689,6501,8136,9432,8053,6367,5938,9433,1963,6460,9022,8332,4543,9064,8031,8316,4005,6481,1802,3407,6302,4063,2914,9983,6861,6701,5108,5233,6762,4507,7261,6502,8830,3436,5332,1801,8591,3401,5214,9843,7752,8309,4502,4902,2502,6857,3402,7912,9502,8601,3405};
			for (int symbol : arrayOfTestSymbols){
				new DisplayMarketData(
						new MarketData(
								String.valueOf(symbol),
								"STK"
								)
						).display();
			}
		}
		
		else if (menuStructure == MenuStructures.menu_test_realtime_data){
			int[] arrayOfTestSymbols = new int[]{9437,8306,8035,8058,9104,5401,3382,6752,7751,4503,7203,7011,7270,1878,6301,8001,6971,6954,1605,7731,8002,9984,9831,5202,7267,4452,7201,9101,8411,8750,9107,9020,4689,6501,8136,9432,8053,6367,5938,9433,1963,6460,9022,8332,4543,9064,8031,8316,4005,6481,1802,3407,6302,4063,2914,9983,6861,6701,5108,5233,6762,4507,7261,6502,8830,3436,5332,1801,8591,3401,5214,9843,7752,8309,4502,4902,2502,6857,3402,7912,9502,8601,3405};
			
			for (int symbol : arrayOfTestSymbols){
				new DisplayRealtimeData(
						new RealtimeData(
								String.valueOf(symbol),
								"STK"
								)
						).display();
			}
		}
		
		else if (menuStructure == MenuStructures.menu_main_backtest){
			new MainBacktest(
					new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value),
					new HistoricalData(
							null, 
							"STK", 
							new DateTools().getDateFromString(menuStructure.getArgument(MenuArguments.arg_start_date).value), 
							new DateTools().getDateFromString(menuStructure.getArgument(MenuArguments.arg_end_date).value), 
							Resolution.valueOf(menuStructure.getArgument(MenuArguments.arg_resolution).value)
						)
					);
		}
		
		else if (menuStructure == MenuStructures.menu_internal_build_database_definitions){
			new BuildDatabaseDefinitions().writeGeneratedJavaFiles();
		}
		
		else {
			throw new UnsatisfiedLinkError();
		}
	}
}
