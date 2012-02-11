package com.autoStock.database;

import java.sql.*;

import com.autoStock.internal.*;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * @author Graeme Davison
 *
 */
public class DatabaseQuery {
	public String query;

	public ResultSet runQuery( String q )
	{
		query = q;
		DatabaseCore dbCore = ApplicationStates.getDBCore();
		Connection connection = null;
		
		try {
			connection = dbCore.connectionPool.getConnection(); // fetch a connection
		}
		catch ( Exception e )
		{
			
		}

		if ( connection == null )
			return null;
		
		try {
			if (connection != null) {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(q);
				return rs;
			}
		}
		catch ( SQLException se )
		{
			se.printStackTrace();
			return null;
		}
		
		return null;
	}
}
