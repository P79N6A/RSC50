package com.synet.tool.rsc;


/**
 * 不是很好
 * @author chunc
 *
 */
public class GlobalData {
	
	private static GlobalData globalData;
	
	private GlobalData() {
	}
	
	public static GlobalData getIntance() {
		if(globalData != null) {
			return globalData;
		} else{
			globalData = new GlobalData();
			return globalData;
		}
	}

}
