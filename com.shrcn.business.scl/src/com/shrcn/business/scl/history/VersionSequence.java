/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.history;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.MathExtend;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 获取版本序号工具类
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-12-8
 */
/**
 * $Log: VersionSequence.java,v $
 * Revision 1.1  2013/03/29 09:38:26  cchun
 * Add:创建
 *
 * Revision 1.9  2013/03/11 06:20:39  cchun
 * Update:为init添加continue
 *
 * Revision 1.8  2012/09/10 09:01:50  cchun
 * Fix Bug:修复init()字符转int失败导致easy50启动失败的bug
 *
 * Revision 1.7  2012/06/04 03:40:26  cchun
 * Update:兼容版本号
 *
 * Revision 1.6  2012/03/14 02:19:31  cchun
 * Update:添加对reversion为1位数字的处理
 *
 * Revision 1.5  2012/01/13 09:32:32  cchun
 * Update:统一历史记录管理
 *
 * Revision 1.4  2012/01/13 08:39:49  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.3  2010/05/06 02:22:40  cchun
 * Update:添加对currVersion=-1的处理
 *
 * Revision 1.2  2009/12/11 07:08:53  cchun
 * Update:调整序号获取机制
 *
 * Revision 1.1  2009/12/09 07:16:11  cchun
 * Add:添加历史记录工具类
 *
 */
public class VersionSequence {
	
	private static final float VERSION_START = (float) 1.0;
	private static final float REVERSION_START = (float) 1.0;
	private static final int STEP_MAX = 1;
	
	private static volatile VersionSequence instance = null;
	
	private float currVersion = -1;
	private float currReVersion = -1;
	private float versionStep = (float) 0.1;
	private float reversionStep = (float) 0.1;
	private int step = 1;
	
	private VersionSequence(){
		init();
	}
	
	/**
	 * 获取单实例方法
	 * @return
	 */
	public static VersionSequence getInstance() {
		if(null == instance) {
			synchronized (VersionSequence.class) {
				if(null == instance) {
					instance = new VersionSequence();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		List<Element> hitems = new ArrayList<>();
		if(Constants.XQUERY) {
			String xq = "for $hitem in " + XMLDBHelper.getDocXPath()
					+ "/scl:SCL/scl:Header/scl:History/scl:Hitem"
					+ " return <Hitem version='{$hitem/@version}' revision='{$hitem/@revision}'/>";
			hitems = XMLDBHelper.queryNodes(xq);
		} else {
			hitems = XMLDBHelper.selectNodes("/scl:SCL/scl:Header/scl:History/scl:Hitem");
		}
		float max = -1;
		float reMax = -1;
		for(Element hitem : hitems) {
			String version = hitem.attributeValue("version");
			String revision = hitem.attributeValue("revision");
			try {
				if (version!=null && version.indexOf(".") > 0 && revision!=null && revision.indexOf(".") > 0) {
					float verNum = Float.parseFloat(version);
					float verRNum = Float.parseFloat(revision);
					if (max < verNum) {
						max = verNum;
						reMax = verRNum;
					} else if ((max == verNum) && (reMax < verRNum)) {
						reMax = verRNum;
					}
				}
			} catch(NumberFormatException e) {
				SCTLogger.warn("发现异常的版本号格式‘" + version + "’！");
				continue;
			}
		}
		currVersion = max;
		currReVersion = reMax;
		step = 1;
	}
	
	/**
	 * 刷新方法（新建、打开、打开历史工程时需调用）。
	 */
	public void refresh() {
		init();
	}
	
	/**
	 * 获取下一版本号
	 * @return
	 */
	synchronized public float[] next(boolean isUp) {
		if (currVersion == -1)
			currVersion = VERSION_START;
		if (currReVersion == -1) {
			currReVersion = REVERSION_START;
		} else {
			if (step > STEP_MAX) { // 超过一定次数，就和数据库同步一下
				init();
			}
			if (currVersion == -1)
				currVersion = VERSION_START;
			if (currReVersion == -1) {
				currReVersion = REVERSION_START;
			} else if (isUp) {
				currReVersion = REVERSION_START;
				currVersion = MathExtend.add(currVersion, versionStep);
			} else {
				currReVersion = MathExtend.add(currReVersion, reversionStep);
			}
			step++;
		}
		
		return new float[] { currVersion, currReVersion };
	}
	synchronized public float[] nextVer(float curVer,float rever) {
		return new float[] { MathExtend.add(curVer, versionStep), REVERSION_START };
	}
	synchronized public float[] nextReVer(float curVer,float rever) {
		return new float[] { curVer, MathExtend.add(rever,reversionStep) };
	}
	/**
	 * 获取下一版本号
	 * @return
	 */
	synchronized public float[] next(boolean isVersionUp, boolean isReVersionUp) {
		init();
		if (currVersion == -1) {
			currVersion = VERSION_START;
		} if (currReVersion == -1) {
			currReVersion = REVERSION_START;
		} else {
			if(isVersionUp) {
				currVersion = MathExtend.add(currVersion, versionStep);
				currReVersion = REVERSION_START;
			} else if(isReVersionUp) {
				if (MathExtend.subtract(currReVersion, (float)9.9) == 0) {
					currReVersion = REVERSION_START;
					currVersion = MathExtend.add(currVersion, versionStep);
				} else {
					currReVersion = MathExtend.add(currReVersion, reversionStep);
				}
			}
		}
		
		return new float[] { currVersion, currReVersion };
	}

	public float getCurrVersion() {
		return currVersion;
	}

	public void setCurrVersion(float currVersion) {
		this.currVersion = currVersion;
	}

	public float getCurrReVersion() {
		return currReVersion;
	}

	public void setCurrReVersion(float currReVersion) {
		this.currReVersion = currReVersion;
	}
}
