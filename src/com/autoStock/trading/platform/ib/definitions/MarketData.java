package com.autoStock.trading.platform.ib.definitions;

import org.omg.CORBA.TCKind;

/**
 * @author Kevin Kowalewski
 *
 */
public class MarketData {
	public static enum TickPriceFields {
		field_bid(1),
		field_ask(2),
		field_last(4),
		field_high(6),
		field_low(7),
		field_close(9)
		;
		
		public int field;
		
		TickPriceFields(int field){
			this.field = field;
		}
	}
	
	public static TickPriceFields getTickPriceField(int field){
		for (TickPriceFields tickPriceField : TickPriceFields.values()){
			if (tickPriceField.field == field){
				return tickPriceField;
			}
		}
		
		return null;
	}
	
	public static enum TickSizeFields {
		field_bid(0),
		field_ask(3),
		field_last(5),
		field_volume(8)
		;
		
		public int field;
		
		TickSizeFields(int field){
			this.field = field;
		}
	}
	
	public static TickSizeFields getTickSizeField(int field){
		for (TickSizeFields tickSizeField : TickSizeFields.values()){
			if (tickSizeField.field == field){
				return tickSizeField;
			}
		}
		
		return null;
	}
}
