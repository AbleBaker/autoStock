package com.autoStock.menu;

/**
 * @author Kevin Kowalewski
 *
 */
public class MenuDefinitions {
	public static enum MenuStructures {
		menu_main(new MenuArguments[]{MenuArguments.arg_none}),
		menu_shutdown(new MenuArguments[]{MenuArguments.arg_none}),
		menu_startup(new MenuArguments[]{MenuArguments.arg_none}),
		menu_restart(new MenuArguments[]{MenuArguments.arg_restart_delay}),
		menu_request_historical_prices(new MenuArguments[]{MenuArguments.arg_security_type, MenuArguments.arg_symbol, MenuArguments.arg_start_date, MenuArguments.arg_end_date}),
		;
		
		public MenuArguments[] arrayOfMenuArguments;
		
		private MenuStructures(MenuArguments[] arrayOfMenuArguments) {
			this.arrayOfMenuArguments = arrayOfMenuArguments;
		}
		
		public MenuArguments getArgument(MenuArguments menuArgument){
			for (MenuArguments tempMenuArgument : arrayOfMenuArguments){
				if (tempMenuArgument == menuArgument){
					return menuArgument;
				}
			}
			
			return null;
		}
	}
	
	public static enum MenuArguments{
		arg_none(new MenuArgumentTypes[]{MenuArgumentTypes.const_none}, ""),
		arg_restart_delay(new MenuArgumentTypes[]{MenuArgumentTypes.const_now, MenuArgumentTypes.const_safe, MenuArgumentTypes.basic_integer}, "Restart delay"),
		arg_security_type(new MenuArgumentTypes[]{MenuArgumentTypes.const_stk, MenuArgumentTypes.const_opt, MenuArgumentTypes.const_fut}, "Security Type"),
		arg_symbol(new MenuArgumentTypes[]{MenuArgumentTypes.basic_string}, "Symbol"),
		arg_start_date(new MenuArgumentTypes[]{MenuArgumentTypes.basic_date}, "Start Date"),
		arg_end_date(new MenuArgumentTypes[]{MenuArgumentTypes.basic_date, MenuArgumentTypes.basic_period}, "End Date or Period"),
		;
		
		public MenuArgumentTypes[] arrayOfArgumentTypes;
		public String argumentDescription;
		public String value;
		
		MenuArguments(MenuArgumentTypes[] arrayOfArgumentTypes, String argumentDescription){
			this.arrayOfArgumentTypes = arrayOfArgumentTypes;
			this.argumentDescription = argumentDescription;
		}
	}
	
	public static enum MenuArgumentTypes {
		basic_integer,
		basic_float,
		basic_string,
		basic_date,
		basic_period,
		const_now,
		const_safe,
		const_stk,
		const_opt,
		const_fut,
		const_none
	}
}
