/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.ui;


/**
 * 
 * @author 吴云华(mailto:wyh@shrcn.com)
 * @version 1.0, 2009-8-28
 */
/*
 * 修改历史
 * $Log: FileEditorInput.java,v $
 * Revision 1.1  2013/03/29 09:36:45  cchun
 * Add:创建
 *
 * Revision 1.1  2010/08/10 06:48:58  cchun
 * Refactor:修改editor input类包路径，及继承关系
 *
 * Revision 1.3  2010/08/10 03:37:10  cchun
 * Update:添加getEditorId()
 *
 * Revision 1.2  2009/08/31 00:52:43  wyh
 * 单线图名称为变电站名
 *
 * Revision 1.1  2009/08/28 10:19:19  wyh
 * 用于导入SSD时同时导入同名的Graph文件
 *
 */
public class FileEditorInput extends AbstractEditorInput {
	
	private String file;
	
	public FileEditorInput(String file, String id, String subStationName){
		super(subStationName, id);
		this.file = file;
	}
	
	public String getFile() {
		return file;
	}
}
