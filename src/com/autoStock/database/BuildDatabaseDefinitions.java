/**
 * 
 */
package com.autoStock.database;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbExchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class BuildDatabaseDefinitions {
	public boolean writeGeneratedJavaFiles(){
		try {
			Connection connection = DatabaseCore.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet resultSetForTables = metaData.getTables(null, null, "%", null);
			
			FileWriter writer = new FileWriter(new File("gen/com/autoStock/generated/basicDefinitions/BasicTableDefinitions.java"));
			PrintWriter printWriter  = new PrintWriter(writer);
			
			printWriter.println("package com.autoStock.generated.basicDefinitions;\n");
			printWriter.println("import java.util.Date;");
			printWriter.println("public class BasicTableDefinitions {\n");
			
			while (resultSetForTables.next()){
				String classString = resultSetForTables.getString(3).substring(0,1).toUpperCase() + resultSetForTables.getString(3).substring(1,resultSetForTables.getString(3).length()-1);
				printWriter.println("\tpublic class Db" + classString + " {");
				ResultSet resultSetForColumns = metaData.getColumns(null, null, resultSetForTables.getString(3), null);
				while (resultSetForColumns.next()){
					printWriter.println("\t\tpublic " + getStringType(resultSetForColumns.getString(6)) + " " + resultSetForColumns.getString(4) + ";");
				}
				printWriter.println("\t}\n");
				printWriter.println("\tpublic static Db"+ classString +" db"+ classString +" = new BasicTableDefinitions(). new Db"+ classString +"();\n");
			}
			
			printWriter.println("}");
			
			printWriter.close();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static String getStringType(String sqlType){
		if (sqlType.equals("FLOAT")){return "double";}
		if (sqlType.equals("INT")){return "int";}
		if (sqlType.equals("BIGINT")){return "long";}
		if (sqlType.equals("DATETIME")){return "Date";}
		if (sqlType.equals("VARCHAR")){return "String";}
		else {throw new UnsatisfiedLinkError();}
	}
	
	public static Object getJavaType(String sqlType){
		if (sqlType.equals("FLOAT")){return Double.class;}
		if (sqlType.equals("INT")){return Integer.class;}
		if (sqlType.equals("BIGINT")){return Long.class;}
		if (sqlType.equals("DATETIME")){return Date.class;}
		if (sqlType.equals("VARCHAR")){return String.class;}
		else {throw new UnsatisfiedLinkError();}
	}
}
