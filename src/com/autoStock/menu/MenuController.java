/**
 * 
 */
package com.autoStock.menu;

import com.autoStock.Co;
import com.autoStock.menu.MenuDefinitions.MenuArgumentTypes;
import com.autoStock.menu.MenuDefinitions.MenuArguments;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.tools.MiscUtils;
import com.autoStock.tools.StringUtils;

/**
 * @author Kevin Kowalewski
 *
 */
public class MenuController {
	public void displayMenu(MenuStructures menuStructure){
		if (menuStructure == MenuStructures.menu_main){
			
			for (MenuStructures menuStructureEntry : MenuStructures.values()){
				Co.print(StringUtils.removePrefix(menuStructureEntry.name(),"_") + " ");
				for (MenuArguments menuArgumentsEntry : menuStructureEntry.arrayOfMenuArguments){
					Co.print(StringUtils.removePrefix(menuArgumentsEntry.name(), "_") + " ( ");
					for (MenuArgumentTypes menuArgumentTypesEntry : menuArgumentsEntry.arrayOfArgumentTypes){
						if (menuArgumentsEntry.arrayOfArgumentTypes.length-1 == MiscUtils.getArrayIndex(menuArgumentsEntry.arrayOfArgumentTypes, menuArgumentTypesEntry)){
							Co.print(StringUtils.removePrefix(menuArgumentTypesEntry.name(), "_") + " ");
						}else{
							Co.print(StringUtils.removePrefix(menuArgumentTypesEntry.name(), "_") + ", ");
						}
					}
					Co.print(")");
				}
				Co.print("\n");
			}
		}
	}
}
