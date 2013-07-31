package com.autoStock.memoryCache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 *
 */
public class DiskCache {
	private final String cacheRoot = "./cache/";
	private final File fileForCacheRoot = new File(cacheRoot);
	private Gson gson = new Gson();
	
	public DiskCache(){
		if (fileForCacheRoot.exists() == false){
			fileForCacheRoot.mkdir();
		}
	}

	public boolean containsKey(String queryHash) {
		if (new File(cacheRoot + queryHash + ".sql").exists()){
			return true;
		}
		return false;
	}

	public ArrayList<?> getValue(String queryHash, Type classForGson) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(cacheRoot + queryHash + ".sql"));
			return gson.fromJson(bufferedReader, classForGson);
		}catch(Exception e){e.printStackTrace();}
		
		return null;
	}

	public void addValue(String queryHash, ArrayList<Object> listOfResults) {
		try {
			File file = new File(cacheRoot + queryHash + ".tmp");
			Writer output = new BufferedWriter(new FileWriter(file));
			output.write(gson.toJson(listOfResults));
			output.close();
			file.renameTo(new File(cacheRoot + queryHash + ".sql"));
		}catch(Exception e){e.printStackTrace();}
	}
}
