/**
 * 
 */
package com.autoStock.trading.platform.ib;

import com.autoStock.trading.platform.ib.core.AnyWrapper;
import com.autoStock.trading.platform.ib.core.EClientSocket;

/**
 * @author Kevin Kowalewski
 *
 */
public class IbClient {
	EClientSocket clientSocket = new EClientSocket(new AnyWrapper() {
		
		@Override
		public void error(int id, int errorCode, String errorMsg) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void error(String str) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void error(Exception e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void connectionClosed() {
			// TODO Auto-generated method stub
			
		}
	});
}
