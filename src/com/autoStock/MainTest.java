package com.autoStock;

import java.util.ArrayList;

import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbWhitelist;
import com.autoStock.tools.DateTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;


/**
 * @author Kevin Kowalewski
 *
 */
public class MainTest {

	public MainTest(){
		Co.println("--> Main Test!");
		
		ArrayList<DbWhitelist> listOfWhitelist = (ArrayList<DbWhitelist>) new DatabaseQuery().getQueryResults(BasicQueries.basic_get_whitelist, new QueryArg(QueryArgs.reason, "manual"));
		
		Co.println("--> Have whitelist: " + listOfWhitelist.size());
		
		ArrayList<Symbol> listOfSymbols = new ArrayList<Symbol>();
		
		for (DbWhitelist dbWhitelist : listOfWhitelist){
			listOfSymbols.add(new Symbol(dbWhitelist.symbol, SecurityType.type_stock));
		}
		
		new MainBacktest(new Exchange("NYSE"), DateTools.getDateFromString("01/19/2012"), DateTools.getDateFromString("01/19/2012"), listOfSymbols, BacktestType.backtest_adjustment_individual);
		
	}
}
