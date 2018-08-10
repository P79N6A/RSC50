package com.synet.tool.rsc;

import java.util.List;

import com.shrcn.found.ui.model.ITreeEntry;
import com.synet.tool.rsc.model.Tb1042BayEntity;

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
	
	
		private List<Tb1042BayEntity> bayEntities;
		
		private ITreeEntry currentSelEntry;
		
		public ITreeEntry getCurrentSelEntry() {
			return currentSelEntry;
		}
		
		public void setCurrentSelEntry(ITreeEntry currentSelEntry) {
			this.currentSelEntry = currentSelEntry;
		}
		
		public List<Tb1042BayEntity> getBayEntities() {
			return bayEntities;
		}
		
		public void setBayEntities(List<Tb1042BayEntity> bayEntities) {
			this.bayEntities = bayEntities;
		}

}
