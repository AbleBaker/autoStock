package com.autoStock.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.autoStock.menu.MenuDefinitions.MenuArgumentTypes;
import com.autoStock.menu.MenuDefinitions.MenuArguments;
import com.autoStock.trading.platform.ib.definitions.HistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class ValidilityCheck {
	
	public boolean isValidMenuArgument(MenuArguments menuArgument){
		//Co.println("Checking: " + menuArgument.name());
		for (MenuArgumentTypes menuArgumentType : menuArgument.arrayOfArgumentTypes){
			if (menuArgumentType == MenuArgumentTypes.basic_date){
				return isValidDate(menuArgument.value);
			}
			
			if (menuArgumentType == MenuArgumentTypes.basic_integer){
				return isValidInt(menuArgument.value);
			}
			
			if (menuArgumentType == MenuArgumentTypes.basic_float){
				return isValidFloat(menuArgument.value);
			}
			
			if (menuArgumentType == MenuArgumentTypes.basic_period){
				return isValidPeriod(menuArgument.value);
			}
			
			if (menuArgumentType.name().startsWith("const")){
				return isValidMenuDefinitionConst(menuArgument.value);
			}
			
			if (menuArgumentType == MenuArgumentTypes.basic_string){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isValidInt(String value){
		try {
			Integer.parseInt(value);
			return true;
		}catch (NumberFormatException e){
			return false;
		}
	}
	
	public boolean isValidFloat(String value){
		try {
			Float.parseFloat(value);
			return true;
		}catch (NumberFormatException e){
			return false;
		}
	}
	
	public boolean isValidDate(String value){
		try {
			new SimpleDateFormat("yyyy/MM/dd.HH:mm.a").parse(value);
			return true;
		}catch (ParseException e){
			try {
				new SimpleDateFormat("yyyy/MM/dd.HH:mm.ss a").parse(value);
				return true;
			}catch (ParseException ex){
				return false;
			}
		}
	}
	
	public boolean isValidPeriod(String value){
		for (HistoricalData.Resolution resolution : HistoricalData.Resolution.values()){
			if (value.toLowerCase().equals(resolution.name().toLowerCase())){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isValidMenuDefinitionConst(String value){
		for (MenuArgumentTypes tempArgumentType : MenuArgumentTypes.values()){
			if (tempArgumentType.name().startsWith("const") && StringUtils.removePrefix(tempArgumentType.name(), "_").equals(value)){
				return true;
			}
		}
		return false;
	}
}
