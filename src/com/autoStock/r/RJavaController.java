/**
 * 
 */
package com.autoStock.r;

import java.io.*;
import java.awt.Frame;
import java.awt.FileDialog;
import java.util.Enumeration;

import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.RMainLoopCallbacks;

import com.autoStock.Co;


/**
 * @author Kevin
 *
 */
public class RJavaController {
	public static RJavaController instance = new RJavaController();
	private boolean useCompiler = false;
	private boolean useTextConsole = true;
	private Rengine rEngine;
	
	private RJavaController(){
		if (Rengine.versionCheck() == false){throw new IllegalStateException("Could not verify REngine version");}
		loadRInstance();
	}
	
	private void loadRInstance() {
		Co.println("--> ?");
		rEngine = new Rengine(new String[]{}, false, useTextConsole ? new TextConsoleX() : new REmptyCallback());
		if (rEngine.waitForR() == false){throw new IllegalStateException("Could start R properly...");}
		
//		rEngine.eval("install.packages(\"quantmod\")");
		//rEngine.eval("install.packages(\"TTR\")");	
		
		rEngine.eval("library(\"quantmod\")");
		rEngine.eval("library(\"TTR\")");
		
		if (useCompiler){
			rEngine.eval("require(\"compiler\")");
			rEngine.eval("enableJIT(0)");
		}
		
		Co.println("--> Started instance");
	}
	
	public synchronized Rengine getREngine(){
		return rEngine;
	}

	public static RJavaController getInstance() {
		return instance;
	}
}
