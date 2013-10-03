/**
 * 
 */
package com.autoStock.menu;

import com.autoStock.Co;
import com.autoStock.MainActiveAlgorithm;
import com.autoStock.MainBacktest;
import com.autoStock.MainClusteredBacktest;
import com.autoStock.MainClusteredBacktestClient;
import com.autoStock.MainEngagement;
import com.autoStock.MainFilter;
import com.autoStock.MainIndicatorTest;
import com.autoStock.MainMarketIndexData;
import com.autoStock.MainMarketOrder;
import com.autoStock.MainTest;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.database.BuildDatabaseDefinitions;
import com.autoStock.display.DisplayHistoricalPrices;
import com.autoStock.display.DisplayMarketSymbolData;
import com.autoStock.display.DisplayRealtimeData;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.menu.MenuDefinitions.MenuArguments;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.replay.ReplayController;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.MarketIndexData;
import com.autoStock.trading.types.MarketSymbolData;
import com.autoStock.trading.types.RealtimeData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Index;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MenuLauncher {
	public void launchDisplay(MenuStructures menuStructure){
		if (menuStructure == MenuStructures.menu_request_historical_prices){
			new DisplayHistoricalPrices(
					new HistoricalData(
							new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value),
							new Symbol(menuStructure.getArgument(MenuArguments.arg_symbol).value, SecurityType.type_stock), 
							DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_start_date).value), 
							DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_end_date).value),
							Resolution.valueOf(menuStructure.getArgument(MenuArguments.arg_resolution).value)
							)
					).display();
		}
		else if (menuStructure == MenuStructures.menu_request_market_symbol_data){
			new DisplayMarketSymbolData(
					new MarketSymbolData(
							new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value),
							new Symbol(menuStructure.getArgument(MenuArguments.arg_symbol).value, SecurityType.type_stock)
							)
					).display();
			}
		
		else if (menuStructure == MenuStructures.menu_main_market_index_data){
			new MainMarketIndexData(
					new MarketIndexData(
							new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value),
							new Index(menuStructure.getArgument(MenuArguments.arg_index).value)
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
		
		else if (menuStructure == MenuStructures.menu_request_market_order){
			new MainMarketOrder(new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value), PositionType.valueOf(menuStructure.getArgument(MenuArguments.arg_position_type).value) , menuStructure.getArgument(MenuArguments.arg_symbol).value, Integer.valueOf(menuStructure.getArgument(MenuArguments.arg_position_units).value));
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
				DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_start_date).value), 
				DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_end_date).value),
				ListTools.getArrayListFromString(menuStructure.getArgument(MenuArguments.arg_symbol_array).value.replaceAll("\"", ""), " "),
				BacktestType.valueOf(menuStructure.getArgument(MenuArguments.arg_backtest_type).value)
			);
		}
		
		else if (menuStructure == MenuStructures.menu_main_clustered_backtest){
			new MainClusteredBacktest(
				new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value),
				DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_start_date).value), 
				DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_end_date).value),
				ListTools.getArrayListFromString(menuStructure.getArgument(MenuArguments.arg_symbol_array).value.replaceAll("\"", ""), " ")
			);
		}else if (menuStructure == MenuStructures.menu_main_clustered_backtest_client){
			new MainClusteredBacktestClient();
		}
		
		else if (menuStructure == MenuStructures.menu_main_engage){
			new MainEngagement(new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value));
		}
		
		else if (menuStructure == MenuStructures.menu_main_active_algorithm){
			new MainActiveAlgorithm(new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value), new Symbol(menuStructure.getArgument(MenuArguments.arg_symbol).value, SecurityType.type_stock));
		}
		
		else if (menuStructure == MenuStructures.menu_main_market_filter){
			new MainFilter(new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value));
		}
		
		else if (menuStructure == MenuStructures.menu_main_test){
			new MainTest();
		}
		
		else if (menuStructure == MenuStructures.menu_internal_build_database_definitions){
			new BuildDatabaseDefinitions().writeGeneratedJavaFiles();
		}
		
		else if (menuStructure == MenuStructures.menu_internal_build_replay_from_file){
			new ReplayController().buildFromTextFile(new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value), menuStructure.getArgument(MenuArguments.arg_file_name).value);
		}
		
		else if (menuStructure == MenuStructures.menu_main_indicator_test){
			new MainIndicatorTest(
					DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_start_date).value),
					DateTools.getDateFromString(menuStructure.getArgument(MenuArguments.arg_end_date).value),
					new Exchange(menuStructure.getArgument(MenuArguments.arg_exchange).value),
					new Symbol(menuStructure.getArgument(MenuArguments.arg_symbol).value, SecurityType.type_stock)
				);
		}
		
		else if (menuStructure == MenuStructures.menu_quick_command){
			String menuCommand = menuStructure.getArgument(MenuArguments.arg_command).value;
			String command = null;
			
			switch (menuCommand){
				case "1" :
					command = "main_backtest 01/05/2012 01/05/2012 NYSE AIG backtest_adjustment_individual";
					break;
				case "2" :
					command = "main_backtest 01/04/2012 01/04/2012 NYSE AIG backtest_default";
					break;
				case "3" :
					command = "main_backtest 01/20/2012 01/20/2012 NYSE AIG backtest_default";
					break;
				case "4":
					command = "main_clustered_backtest 01/04/2012 01/04/2012 NYSE AIG";
					break;
				case "5":
					command = "main_clustered_backtest_client";
					break;
			}
			
			if (command == null){
				Co.println("--> Error: No Quick Command matched...");
				return;
			}
			
			MenuController menuController = new MenuController();
			MenuStructures menuStructureForQuickCommand = menuController.getRelatedMenu(command.split(" "));
			menuController.handleMenuStructure(menuStructureForQuickCommand, command.split(" "));
			new MenuLauncher().launchDisplay(menuStructureForQuickCommand);
		}
		
		else {
			throw new UnsatisfiedLinkError();
		}
	}
}
