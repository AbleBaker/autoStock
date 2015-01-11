package com.autoStock.memoryCache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Kevin Kowalewski
 *
 */
public class DiskCache {
	private final String CACHE_ROOT = "./cache/";
	private final File fileForCacheRoot = new File(CACHE_ROOT);
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public DiskCache(){
		if (fileForCacheRoot.exists() == false){
			fileForCacheRoot.mkdir();
		}
	}

	public boolean containsKey(String queryHash) {
		if (new File(CACHE_ROOT + queryHash + ".sql").exists()){
			return true;
		}
		return false;
	}

	public ArrayList<?> getValue(String queryHash, Type classForGson) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(CACHE_ROOT + queryHash + ".sql"));
			return gson.fromJson(bufferedReader, classForGson);
		}catch(Exception e){e.printStackTrace();}
		
		return null;
	}

	public void addValue(String queryHash, ArrayList<Object> listOfResults) {
		try {
			File file = new File(CACHE_ROOT + queryHash + ".tmp");
			Writer output = new BufferedWriter(new FileWriter(file));
			output.write(gson.toJson(listOfResults));
			output.close();
			
			if (file.renameTo(new File(CACHE_ROOT + queryHash + ".sql")) == false){
				file.delete();
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
