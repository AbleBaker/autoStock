/**
 * 
 */
package com.autoStock.signal.extras;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.PersistNEATNetwork;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.PersistBasicNetwork;

/**
 * @author Kevin
 *
 */
public class EncogNetworkProvider {
	private static final String TRAINED_NETWORK_PATH = "./trained";
	private String networkName;
	
	public static enum NetworkType {
		basic,
		neat,
	}
	
	public EncogNetworkProvider(String networkName) {
		this.networkName = networkName;
	}

	public boolean saveNetwork(BasicNetwork basicNetwork){
		try {
			new PersistBasicNetwork().save(new FileOutputStream(new File(getNetworkPath(NetworkType.basic))), basicNetwork);
			return true;
		}catch(Exception e){e.printStackTrace();}
		
		return false;
	}
	
	public boolean saveNeatNetwork(NEATNetwork neatNetwork){
		try {
			new PersistNEATNetwork().save(new FileOutputStream(new File(getNetworkPath(NetworkType.neat))), neatNetwork);
			return true;
		}catch(Exception e){e.printStackTrace();}
		
		return false;
	}
	
	public BasicNetwork getBasicNetwork(){
		try {
			return (BasicNetwork) new PersistBasicNetwork().read(new FileInputStream(new File(getNetworkPath(NetworkType.basic))));
		}catch(Exception e){}
		
		return null;
	}
	
	public NEATNetwork getNeatNetwork(){
		try {
			return (NEATNetwork) new PersistNEATNetwork().read(new FileInputStream(new File(getNetworkPath(NetworkType.neat))));
		}catch (Exception e){}
		
		return null;
	}
	
	public String getNetworkPath(NetworkType networkType){
		return TRAINED_NETWORK_PATH + "/" + networkName + "-" + networkType.name() + ".ml"; 
	}
}
