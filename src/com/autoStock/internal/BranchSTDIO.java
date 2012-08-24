package com.autoStock.internal;

import java.io.File;
import java.io.PrintStream;

/**
 * @author Kevin Kowalewski
 *
 */
public class BranchSTDIO {
	
	public static final String defaultFileName = "C:\\Users\\Kevin\\Desktop\\autoStock.txt";
	
	public void branchSTDOUTToFile(String fileName) throws Exception{
		System.setOut(new PrintStream(new File(fileName)));
	}
}
