package com.autoStock;

import java.sql.SQLException;
import java.util.Date;

import com.autoStock.finance.Account;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.internal.Global.Mode;
import com.autoStock.menu.MenuController;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.menu.MenuDisplayLauncher;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;
import com.autoStock.signal.SignalMetric;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.tools.MiscUtils;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {	
	public static void main(String[] args) throws SQLException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Co.println("******** Welcome to autoStock ********\n");
			
		ApplicationStates.startup(Mode.client);

		MenuController menuController = new MenuController();
		if (args.length == 0){menuController.displayMenu(MenuStructures.menu_main); ApplicationStates.shutdown();}
		MenuStructures menuStructure = menuController.getRelatedMenu(args);
		menuController.handleMenuStructure(menuStructure, args);
		
		new MenuDisplayLauncher().launchDisplay(menuStructure);
		
		while (Global.callbackLock.isWaitingForCallbacks()){
			Co.println("...");
			try{Thread.sleep(1*1000);}catch(InterruptedException e){return;}
		}
		
//		Co.println("Test positions...");
//		
//		Co.println("Current account balance is: " + MiscUtils.getCommifiedValue(Account.instance.getBankBalance()));
//		
//		TypeQuoteSlice typeQuoteSlice = new TypeQuoteSlice();
//		typeQuoteSlice.priceClose = 25.50;
//		typeQuoteSlice.symbol = "BTU";
//		typeQuoteSlice.dateTime = new Date();
//		
//		Signal signal = new Signal(SignalSource.from_manual);
//		
//		Co.println("Trying to buy...");
//	
//		signal.currentSignalType = SignalType.type_trend_up;
//		signal.addSignalMetrics(new SignalMetric(100, SignalTypeMetric.metric_cci));
//		PositionGovernor.instance.informGovener(typeQuoteSlice, signal);
//		
//		Co.println("Current account balance is: " + MiscUtils.getCommifiedValue(Account.instance.getBankBalance()));
//		
//		Co.println("Tyring to sell...");
//		
//		typeQuoteSlice.priceClose = 24.50;
//		signal.reset();
//		signal.currentSignalType = SignalType.type_trend_down;
//		signal.addSignalMetrics(new SignalMetric(-100, SignalTypeMetric.metric_cci));
//		PositionGovernor.instance.informGovener(typeQuoteSlice, signal);
//		
//		Co.println("Current account balance is: " + MiscUtils.getCommifiedValue(Account.instance.getBankBalance()));
//		
//		Co.println("Trying to short...");
//		
//		typeQuoteSlice.priceClose = 24.50;
//		signal.reset();
//		signal.currentSignalType = SignalType.type_trend_down;
//		signal.addSignalMetrics(new SignalMetric(-100, SignalTypeMetric.metric_cci));
//		PositionGovernor.instance.informGovener(typeQuoteSlice, signal);
//		
//		Co.println("Current account balance is: " + MiscUtils.getCommifiedValue(Account.instance.getBankBalance()));
//		
//		Co.println("Trying to end short...");
//		
//		PositionManager.instance.executeSellAll();
//		
//		Co.println("Current account balance is: " + MiscUtils.getCommifiedValue(Account.instance.getBankBalance()));
		
		Co.println("\n******** Finished ********");
		//System.exit(0);
	}
}
