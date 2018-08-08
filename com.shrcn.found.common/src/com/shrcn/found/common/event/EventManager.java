/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.event;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.xml.XMLFileManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-6
 */
/**
 * $Log: EventManager.java,v $ Revision 1.3 2013/05/21 03:23:10 cchun
 * Refactor:将loadEventHandlers()改成启动时调用
 * 
 * Revision 1.2 2013/04/07 12:27:37 cchun Update:完成基础界面框架
 * 
 * Revision 1.1 2013/04/06 11:58:34 cchun Update:完成界面事件框架
 * 
 */
public class EventManager {

	private static final String handler_icons = "com.shrcn.found.ui.util.IconsManager";
	private static final String handler_topbanner = "com.shrcn.found.ui.app.AbstractWorkbenchWindowAdvisor";
	
	/** 单例对象 */
	private static volatile EventManager eventManager = null;
	/**
	 * 事件名称和监听器class name与事件类型集合之间的映射。 即一个事件或监听器可以出现在多个事件类型中。
	 */
	private static Map<String, List<String>> eventHandlerMap = new Hashtable<String, List<String>>();
	/** 事件类型名称与该事件所有监听器对象之间的映射。 */
	private static Map<String, List<IEventHandler>> handlerMap = new Hashtable<String, List<IEventHandler>>();

	/**
	 * 单例模式私有构造函数
	 */
	private EventManager() {
		addRegistryInfo("ICON MANAGER", EventConstants.SYS_REFRESH_ICONS);
		addRegistryInfo("ICON MANAGER", handler_icons);
		addRegistryInfo("Win TopBanner", EventConstants.SYS_REFRESH_TOP_BAN);
		addRegistryInfo("Win TopBanner", EventConstants.SYS_RELOAD_TOP_BAN);
	}

	/**
	 * 获取单例对象
	 */
	public static EventManager getDefault() {
		if (null == eventManager) {
			synchronized (EventManager.class) {
				if (null == eventManager) {
					eventManager = new EventManager();
				}
			}
		}
		return eventManager;
	}

	/**
	 * 加载事件监听器注册信息
	 */
	public void loadEventHandlers(Class<?> clazz, String config) {
		Document doc = XMLFileManager.loadXMLFile(clazz, config);
		if (doc == null) {
			SCTLogger.error("没有找到事件注册定义文件 " + config + " ，请检查。");
			return;
		}
		for (Object obj : doc.getRootElement().elements()) {
			Element configuration = (Element) obj;
			String type = configuration.attributeValue("id");
			List<?> events = configuration.elements();
			for (Object obj1 : events) {
				Element element = (Element) obj1;
				String tagName = element.getName();
				String key = null;
				if ("event".equals(tagName))
					key = element.attributeValue("name");
				else
					key = element.attributeValue("source");
				addRegistryInfo(type, key);
			}
		}
	}

	private void addRegistryInfo(String type, String key) {
		if (eventHandlerMap.containsKey(key)) {
			eventHandlerMap.get(key).add(type);
		} else {
			List<String> types = new ArrayList<String>();
			types.add(type);
			eventHandlerMap.put(key, types);
		}
	}

	/**
	 * 添加editor事件监听器(打开、关闭)
	 * 
	 * @param listener
	 */
	public void registEventHandler(IEventHandler listener) {
		List<String> listenerTypes = getHandlerType(listener);
		for (String listenerType : listenerTypes) {
			if (null != listenerType && !"".equals(listenerType))
				registEventHandler(listener, listenerType);
		}
	}

	/**
	 * 删除editor事件监听器(打开、关闭)
	 * 
	 * @param listener
	 */
	public void removeEventHandler(IEventHandler listener) {
		List<String> listenerTypes = getHandlerType(listener);
		for (String listenerType : listenerTypes) {
			if (null != listenerType && !"".equals(listenerType))
				removeEventHandler(listener, listenerType);
		}
	}

	/**
	 * 触发编辑器监听器事件
	 * 
	 * @param property
	 * @param obj
	 */
	public void notify(String property, Object data) {
		notify(property, null, data);
	}

	public void notify(String property, Object source, Object data) {
		List<String> listenerTypes = getHandlerType(property);
		if (EventConstants.DEVICE_EDIT.equals(property) && listenerTypes == null) {
			return;
		}
		for (String listenerType : listenerTypes) {
			if (null != listenerType && !"".equals(listenerType))
				notify(property, source, data, listenerType);
		}
	}

	/**
	 * 添加事件监听器
	 * 
	 * @param listener
	 */
	private void registEventHandler(IEventHandler listener, String listenerType) {
		List<IEventHandler> editorListeners = handlerMap.get(listenerType);
		if (null == editorListeners) {
			editorListeners = new ArrayList<IEventHandler>();
			handlerMap.put(listenerType, editorListeners);
		}
		if (!editorListeners.contains(listener))
			editorListeners.add(listener);
	}

	/**
	 * 删除事件监听器
	 * 
	 * @param listener
	 */
	private void removeEventHandler(IEventHandler listener, String listenerType) {
		List<IEventHandler> editorListeners = handlerMap.get(listenerType);
		if (null == editorListeners)
			return;
		editorListeners.remove(listener);
	}

	/**
	 * 触发监听器事件
	 * 
	 * @param property
	 * @param oldObj
	 * @param newObj
	 * @param listeners
	 */
	private void notify(String property, Object source, Object data,
			String listenerType) {
		List<IEventHandler> listeners = handlerMap.get(listenerType);
		if (null == listeners)
			return;
		for (IEventHandler handler : listeners)
			handler.execute(new Context(property, source, data));
	}

	/**
	 * 根据监听器class name获取listener类型
	 * 
	 * @param className
	 * @return
	 */
	private List<String> getHandlerType(Object listener) {
		if (isWorkbenchWindowAdvisor(listener)) {
			return getHandlerType(EventConstants.SYS_REFRESH_TOP_BAN);
		}
		return getHandlerType(listener.getClass().getName());
	}

	/**
	 * 根据事件名称获取监听类型集合
	 * 
	 * @param key
	 * @return
	 */
	private List<String> getHandlerType(String key) {
		return eventHandlerMap.get(key);
	}
	
	private boolean isWorkbenchWindowAdvisor(Object listener) {
		return listener.getClass().getSuperclass().getName().equals(handler_topbanner);
	}
	
	/**
	 * 判断当前事件是否存在
	 * 
	 * @param event
	 * @return
	 */
	public boolean isExist(String event){
		return eventHandlerMap.containsKey(event);
	}
}
