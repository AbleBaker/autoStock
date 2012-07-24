/**
 * 
 */
package com.autoStock.database;

import java.sql.ResultSet;

import com.autoStock.database.queryResults.QueryResult;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbSymbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultInterpriter { // This class should go away
	public Object interprit(ResultSet resultSet, Class resultClass){
		try {
			if (resultClass == DbStockHistoricalPrice.class){
				return new DatabaseBinder().getDbStockHistoricalPrice(resultSet.getLong(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getDouble(6), resultSet.getInt(7), resultSet.getString(8));
			}else if (resultClass == DbSymbol.class){
				return new DatabaseBinder().getDbSymbol(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
			}else if (resultClass == QueryResult.QrSymbolCountFromExchange.class){
				return new DatabaseBinder().getQrSymbolCountFromExchange(resultSet.getString(1), resultSet.getInt(2), resultSet.getLong(3));
			}else if (resultClass == QueryResult.QrExchange.class){
				return new DatabaseBinder().getDbExchange(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7));
			}else{
				throw new UnsupportedOperationException();
			}
		}catch (Exception e){e.printStackTrace();}
		
		throw new UnsatisfiedLinkError();
	}
}
