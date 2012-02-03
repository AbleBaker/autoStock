/**
 * 
 */
package com.autoStock.comServer;

import java.lang.reflect.Type;

import com.autoStock.com.CommandHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 *
 */
public class CommandReceiver {
	public void receiveGsonString(String string){
		//Co.println("Received GSON String: " + string);
		
		Type type = new TypeToken<CommandHolder>(){}.getType();
		Runnable runnable = new Gson().fromJson(string, type);
		runnable.run();
	}
}
