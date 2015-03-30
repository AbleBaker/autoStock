/**
 * 
 */
package com.autoStock.r;

import java.util.Arrays;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import com.autoStock.Co;
import com.autoStock.tools.Benchmark;

/**
 * @author Kevin
 *
 */
public class RJavaController {
	public static RJavaController instance = new RJavaController();
	private boolean useCompiler = false;
	private boolean useTextConsole = false;
	private Rengine rEngine;
	
	private RJavaController(){
		if (Rengine.versionCheck() == false){throw new IllegalStateException("Could not verify REngine version");}
		loadRInstance();
	}
	
	private void loadRInstance() {
		rEngine = new Rengine(new String[]{}, false, useTextConsole ? new TextConsole() : new REmptyCallback());
		if (rEngine.waitForR() == false){throw new IllegalStateException("Could start R properly...");}
		
		//rEngine.eval("install.packages(\"quantmod\")");
		//rEngine.eval("install.packages(\"TTR\")");	
		
		rEngine.eval("library(\"quantmod\")");
		rEngine.eval("library(\"TTR\")");
		
		if (useCompiler){
			rEngine.eval("require(\"compiler\")");
			rEngine.eval("enableJIT(0)");
		}
		
	}
	
	public synchronized Rengine getREngine(){
		return rEngine;
	}

	public static RJavaController getInstance() {
		return instance;
	}
}
