/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.view;

import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.view.ViewManager;

/**
 * 通讯报文监视类
 * 
 * @author 王敏(mailto:wm.202039@sieyuan.com)
 * @version 1.0, 2017-1-17
 */
/**
 * $Log$
 */
public class ComMsgManager {
	
	/**
	 * 实例
	 */
	private static ComMsgManager logger = new ComMsgManager();
	
	/**
	 * 报文视图
	 */
	private static ComMsgView log = null;
	
	/**
	 * 构造函数
	 */
	private ComMsgManager() {
	}
	
	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static ComMsgManager getInstance() {
		return logger;
	}
	
	/**
	 * 打开视图
	 */
	private void openView() {
		if (log == null) {
			log = (ComMsgView) ViewManager.findView(ComMsgView.ID);
		}
	}
	
	/**
	 * 清空后再向控制台输出
	 * 
	 * @param msg
	 */
	public void output(final String msg) {
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				openView();
				
				if(log != null){
					log.clearInfo();
					log.appendCheckInfo(msg);
				}
			}
		});
	}
	
	/**
	 * 向控制台输出
	 * 
	 * @param msg
	 */
	public void append(final String msg, final boolean showTime) {
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				openView();
				if(log != null){
					String prefix = showTime ? (StringUtil.getChineseTime() + "：") : "";
					log.appendCheckInfo("\n" + prefix + msg);
				}
			}
		});

	}
	
	/**
	 * 添加消息
	 * 
	 * @param msg
	 */
	public void append(final String msg) {
		append(msg, true);
	}
	
	/**
	 * 清空控制台内容
	 */
	public void clear() {
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				openView();
				if(log != null){
					log.clearInfo();
				}
			}
		});
	}
}
