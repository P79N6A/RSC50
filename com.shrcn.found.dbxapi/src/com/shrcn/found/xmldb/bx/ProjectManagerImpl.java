/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.bx;

import java.util.ArrayList;
import java.util.List;

import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.xmldb.api.base.Collection;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.xmldb.PrjMgrBroker;
import com.shrcn.found.xmldb.XMLDBAdmin;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-1-11
 */
/**
 * $Log: ProjectManagerImpl.java,v $
 * Revision 1.2  2013/04/07 12:24:56  cchun
 * Refactor:清理引用
 *
 * Revision 1.1  2013/03/29 09:38:30  cchun
 * Add:创建
 *
 * Revision 1.3  2013/03/07 06:47:46  cchun
 * Update:修改catch类型，避免编译错误
 *
 * Revision 1.2  2012/01/31 01:37:47  cchun
 * Update:删除引用
 *
 * Revision 1.1  2012/01/13 08:39:48  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 */
public class ProjectManagerImpl implements PrjMgrBroker {

	@Override
	public void initPrjectDB(String prjName) {
		Context context = new Context();
		try {
			new CreateDB(prjName).execute(context);
			XMLDBAdmin.startDB();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			context.close();
		}
	}
	
	@Override
	public void clearPrjectDB(String prjName) {
		Context context = new Context();
		try {
			new DropDB(prjName).execute(context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			context.close();
		}
	}

	@Override
	public boolean existProject(String prjName) {
		return XMLDBAdmin.getCollections().contains(prjName);
	}

	@Override
	public List<String> getHistoryItems() {
		List<String> names = XMLDBAdmin.getCollections();
		List<String> hisList = new ArrayList<String>();
		for(String name : names) {
//			name = URLUtil.url2String(name);
			name = StringUtil.unicode2String(name);
			if (name != null)
				hisList.add(name);
		}
		return hisList;
	}
}
