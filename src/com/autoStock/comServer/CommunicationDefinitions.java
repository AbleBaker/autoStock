package com.autoStock.comServer;

/**
 * @author Kevin Kowalewski
 *
 */
public class CommunicationDefinitions {
	public static enum Command {
		accept_unit,
		compute_unit_backtest,
		backtest_results,
		no_units_left,
	}
	
	public static enum CommunicationCommands {
		com_end_communication("COM_END_COMMUNICATION"),
		com_end_command("COM_END_COMMAND"),
		com_ok_command("COM_OK_COMMAND")
		;
		
		public String command;
		
		CommunicationCommands(String command){
			this.command = command;
		}
	}
}
