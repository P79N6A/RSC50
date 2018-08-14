package com.shrcn.found.common.event;

public interface EventConstants {

	String OPEN_CONFIG 			= "open config";
	String CLEAR_CONFIG 		= "clear config";
	
	String PROJECT_CREATE 		= "create project";
	String PROJECT_OPEN 		= "open project";
	String PROJECT_CLOSE 		= "close project";
	String PROJECT_OPEN_EXP		= "open export project";
	String PROJECT_OPEN_IMP		= "open import project";
	
	// Device
	String DEVICE_CREATE 		= "create device";
	String DEVICE_OPEN 			= "open device";
	String DEVICE_DELETE 		= "delete device";
	String DEVICE_RENAME 		= "rename device";
	String DEVICE_COPY 			= "copy device";
	String COPY_DEVICES 		= "copy devices";
	String DEVICE_DIR 			= "open device dir";
	String REFRESH_EIDTOR 		= "refresh editor";
	String DEVICE_SAVE			= "save device";
	String DEVICE_EDIT			= "edit device";
	String CLEAR_DELETED		= "clear deleted";
	
	// 系统事件
	String SYS_REFRESH_ICONS    = "refresh system icons";
	String SYS_REFRESH_TOP_BAN  = "refresh topBanner";
	String SYS_RELOAD_TOP_BAN   = "reload topBanner";
	
	String EVENT_RESPONSE_FROM_UDM 	= "response from udm";
	
	// EASY50
	String REFRESH_HISTORY 		= "refresh history";
	String REFRESH_COMM 		= "refresh communication";
	
	//Manager IED  装置管理
	String REFRESH_IED_MANAGER  = "refresh ied manager";
}
