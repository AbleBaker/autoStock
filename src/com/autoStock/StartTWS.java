package com.autoStock;

import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JPasswordFieldOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

import com.autoStock.comServer.ConnectionServer;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Config;
import com.autoStock.internal.Global;
import com.autoStock.internal.Global.Mode;
import com.autoStock.trading.platform.ib.tws.TWSSupervisor;

/**
 * @author Graeme Davison
 * 
 *  * 
 */
public class StartTWS {
	public static void main(String[] args) {
		Global.mode = Mode.server;
		Co.println("Welcome to autoStock\n");
		
		ApplicationStates.startup();
		
		new TWSSupervisor().launchTws();
//		new ConnectionServer().startServer();
		
		Co.println("Done");
	}
}
