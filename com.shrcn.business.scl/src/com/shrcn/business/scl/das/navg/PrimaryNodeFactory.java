/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das.navg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Element;

import com.shrcn.business.scl.common.DefaultInfo;
import com.shrcn.business.scl.common.EnumEquipType;
import com.shrcn.business.scl.common.LnInstMap;
import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.DevType;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.BayEntry;
import com.shrcn.business.scl.model.navgtree.ConductingEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.EntryUtil;
import com.shrcn.business.scl.model.navgtree.FunctionEntry;
import com.shrcn.business.scl.model.navgtree.GeneralEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.LNodeEntry;
import com.shrcn.business.scl.model.navgtree.PowerTransformerEntry;
import com.shrcn.business.scl.model.navgtree.SubEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.SubFunctionEntry;
import com.shrcn.business.scl.model.navgtree.SubstationEntry;
import com.shrcn.business.scl.model.navgtree.TransformerWindingEntry;
import com.shrcn.business.scl.model.navgtree.TreeEntryImpl;
import com.shrcn.business.scl.model.navgtree.VoltageLevelEntry;
import com.shrcn.business.scl.util.EquipmentSequence;
import com.shrcn.business.scl.util.PrimaryUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.util.XPathUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-5-21
 */
/*
 * 修改历史
 * $Log: PrimaryNodeFactory.java,v $
 * Revision 1.1  2013/03/29 09:38:32  cchun
 * Add:创建
 *
 * Revision 1.6  2012/03/22 03:04:27  cchun
 * Refactor:简化代码逻辑；Fix Bug:修复setNewLnName()中xpath处理错误
 *
 * Revision 1.5  2012/03/21 00:54:46  cchun
 * Update:将xpath改成*[name()='xx']形式
 *
 * Revision 1.4  2012/02/01 03:24:44  cchun
 * Update:统一使用带有name()函数的xpath
 *
 * Revision 1.3  2012/01/17 08:50:28  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.2  2011/11/22 08:36:31  cchun
 * Update:去掉国际化
 *
 * Revision 1.1  2011/09/09 07:40:08  cchun
 * Refactor:转移包位置
 *
 * Revision 1.65  2011/09/09 01:37:11  cchun
 * Update:去掉多余的线程处理
 *
 * Revision 1.64  2011/09/08 09:11:50  cchun
 * Fix Bug:修复PrimaryUtil.updateTerminal()可能导致bayName错误的bug
 *
 * Revision 1.63  2011/08/31 06:12:06  cchun
 * Update:新建电压等级、间隔、功能提示输入名称
 *
 * Revision 1.62  2011/07/11 08:56:50  cchun
 * Refactor:整理设备模型保存逻辑
 *
 * Revision 1.61  2011/06/09 08:39:56  cchun
 * Refactor:使用nextLnInst()重构inst获取方式
 *
 * Revision 1.60  2011/05/06 09:35:18  cchun
 * Fix Bug:修复insertLN()历史记录内容bug
 *
 * Revision 1.59  2011/03/02 08:29:45  cchun
 * Update:KV改成kV，默认为110kV
 *
 * Revision 1.58  2010/11/12 08:56:32  cchun
 * Update:使用统一图标
 *
 * Revision 1.57  2010/11/08 07:14:15  cchun
 * Update:清理引用
 *
 * Revision 1.56  2010/11/02 07:07:05  cchun
 * Fix Bug:修复功能复制后LNode xpath不正确的bug
 *
 * Revision 1.55  2010/10/15 01:48:11  cchun
 * Fix Bug:修复母线创建后不能改名的bug
 *
 * Revision 1.54  2010/10/14 08:08:50  cchun
 * Update:添加clearCopyMap()
 *
 * Revision 1.53  2010/10/08 03:25:22  cchun
 * Update:接地没有模型
 *
 * Revision 1.52  2010/09/14 08:18:59  cchun
 * Update:添加参数，并保留原功能
 *
 * Revision 1.51  2010/09/08 08:24:36  cchun
 * Fix Bug:修复导入典型间隔后oid重复的bug
 *
 * Revision 1.50  2010/09/07 10:02:36  cchun
 * Update:添加对接地图元的拓扑处理
 *
 * Revision 1.49  2010/08/24 01:24:29  cchun
 * Fix Bug:设置父节点bug
 *
 * Revision 1.48  2010/06/28 02:49:13  cchun
 * Fix Bug:1、修复连接点名称重复bug；2、修复ConnectivityNode顺序错误
 *
 * Revision 1.47  2010/05/26 05:35:08  cchun
 * Update:去掉无效引用
 *
 * Revision 1.46  2010/05/25 08:04:39  cchun
 * Update:增加copyElement()
 *
 * Revision 1.45  2010/05/06 03:30:17  cchun
 * Update:复制间隔时，所有的oid都需更新
 *
 * Revision 1.44  2010/05/06 02:24:30  cchun
 * Update:新增子设备时添加oid
 *
 * Revision 1.43  2010/04/23 10:11:14  cchun
 * Fix Bug:复制设备后没有更新oid
 *
 * Revision 1.42  2010/04/23 03:22:26  cchun
 * Update:历史记录接口添加oid参数
 *
 * Revision 1.41  2010/04/19 11:15:59  cchun
 * Fix Bug:修改母线属性bug
 *
 * Revision 1.40  2010/03/29 02:44:43  cchun
 * Update:提交
 *
 * Revision 1.39  2010/03/05 05:26:04  cchun
 * Update:给变压器卷，互感器sub，bay添加oid
 *
 * Revision 1.38  2010/03/03 09:58:22  cchun
 * Update:为一次设备添加oid
 *
 * Revision 1.37  2010/02/09 03:18:54  cchun
 * Update:为LNodeEntry属性赋值
 *
 * Revision 1.36  2010/01/19 09:02:32  lj6061
 * add:统一国际化工程
 *
 * Revision 1.35  2009/12/11 08:40:15  lj6061
 * Add:1次设备操作添加历史增量标记
 *
 * Revision 1.33  2009/11/05 09:26:38  lj6061
 * 修改电压等级格式
 *
 * Revision 1.30  2009/10/12 02:15:27  cchun
 * Update:为功能、自动能操作方法增加返回值
 *
 * Revision 1.29  2009/09/25 07:35:30  lj6061
 * 默认新建变电站时候插入电压等级和间隔
 *
 * Revision 1.28  2009/09/25 02:46:31  lj6061
 * 修正导入间隔后无法复制BUG
 *
 * Revision 1.27  2009/09/22 03:13:08  lj6061
 * 确定SSD文件中逻辑节点需要5个字段
 *
 * Revision 1.26  2009/09/22 01:12:21  lj6061
 * 删除:不使用的代码
 *
 * Revision 1.25  2009/09/21 05:59:56  lj6061
 * 删除: Xpath带有空格
 *
 * Revision 1.24  2009/09/21 05:46:20  lj6061
 * 插入节点添加prefix路径
 *
 * Revision 1.22  2009/09/18 10:11:36  lj6061
 * 删除无用的代码
 *
 *
 * Revision 1.18  2009/09/17 09:52:31  lj6061
 * 插入节点默认树选中
 *
 * Revision 1.17  2009/09/17 06:42:28  wyh
 * 粘贴母线时将插入ConnectivityNode的功能放入该类
 *
 * Revision 1.16  2009/09/17 05:47:19  hqh
 * 扩大方法访问权限
 *
 * Revision 1.15  2009/09/16 12:32:37  wyh
 * 修改：母线ConnectivityNode的名称为maxTopoNum
 *
 * Revision 1.14  2009/09/16 02:00:31  lj6061
 * 修改复制粘贴过程的深拷贝
 *
 * Revision 1.13  2009/09/15 07:41:14  wyh
 * 添加：被复制对象
 *
 * Revision 1.12  2009/09/14 06:18:39  wyh
 * 增加newName和xpath两属性便于同一间隔下多设备复制
 *
 * Revision 1.11  2009/09/14 03:22:29  lj6061
 * 添加多设备的复制粘贴
 *
 * Revision 1.10  2009/09/14 01:34:18  lj6061
 * update:导出或粘贴文件删除所有的连接点信息，保留Terminal为grounded节点
 *
 * Revision 1.9  2009/09/11 01:31:33  lj6061
 * 抽取复制粘贴节点方法
 *
 * Revision 1.8  2009/09/09 01:36:02  lj6061
 * 修改接地刀闸下LN的个数
 *
 * Revision 1.7  2009/09/07 11:22:42  lj6061
 * update:为接地刀闸添加对应的逻辑节点
 *
 * Revision 1.6  2009/09/07 11:00:34  lj6061
 * 添加对于接地和避雷针的grounded连接点
 *
 * Revision 1.5  2009/09/04 06:27:14  lj6061
 * 修正变电站节点Xpath
 *
 * Revision 1.4  2009/09/03 08:38:21  lj6061
 * 添加清理拓扑
 *
 * Revision 1.3  2009/09/03 07:54:17  cchun
 * Update:为母线间隔增加连接点信息
 *
 * Revision 1.2  2009/09/03 03:04:59  cchun
 * Update:增加母线间隔功能
 *
 * Revision 1.1  2009/08/28 01:33:24  cchun
 * Refactor:重构包路径
 *
 * Revision 1.23  2009/08/27 08:32:05  cchun
 * Update:修改由树到图联动触发时机
 *
 * Revision 1.22  2009/08/27 07:51:04  cchun
 * Update:添加由图到树的选择联动
 *
 * Revision 1.21  2009/08/27 03:09:59  cchun
 * Update:完成树、图形同步删除
 *
 * Revision 1.20  2009/08/27 02:40:36  lj6061
 * 修改设备类型文件位置
 *
 * Revision 1.19  2009/08/27 02:26:20  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.18  2009/08/26 09:29:49  cchun
 * Update:添加树节点选择联动处理逻辑
 *
 * Revision 1.17  2009/08/18 09:40:31  cchun
 * Update:合并代码
 *
 * Revision 1.15.2.3  2009/08/17 07:38:51  lj6061
 * 修改关联复制到BUG
 *
 * Revision 1.15.2.2  2009/07/31 09:41:16  cchun
 * Update:修改命名空间处理方式
 *
 * Revision 1.15.2.1  2009/07/24 07:11:15  cchun
 * Update:切换数据库Sedna
 *
 * Revision 1.15  2009/07/17 01:32:05  lj6061
 * 修改更新拓扑的方法
 *
 * Revision 1.14  2009/07/16 06:31:39  lj6061
 * 整理代码
 * 1.删除未被引用的对象和方法
 * 2 修正空指针的异常
 *
 * Revision 1.13  2009/07/14 09:13:21  lj6061
 * 添加提示信息
 *
 * Revision 1.12  2009/07/03 09:13:07  lj6061
 * 解决复制的空一场
 *
 * Revision 1.11  2009/06/24 05:44:20  lj6061
 * 整理创建节点代码
 *
 * Revision 1.8  2009/06/17 12:19:19  lj6061
 * 添加线程，修正Bug
 *
 * Revision 1.4.2.4  2009/06/17 12:08:19  lj6061
 * 添加线程，修正Bug
 *
 * Revision 1.4.2.3  2009/06/17 03:37:32  cchun
 * Thread改成Job
 *
 * Revision 1.4.2.2  2009/06/16 08:42:49  lj6061
 * 对空异常处理
 *
 * Revision 1.6  2009/06/16 08:33:02  lj6061
 * 对空异常处理
 *
 * Revision 1.5  2009/06/15 09:39:57  lj6061
 * 更改代码中的对电压处理
 *
 * Revision 1.4  2009/06/08 10:52:22  lj6061
 * 根据Schema添加全局的Lninst
 *
 * Revision 1.3  2009/06/08 02:23:36  lj6061
 * 添加线程处理
 *
 * Revision 1.2  2009/06/05 08:37:32  lj6061
 * 不插入可选描述
 *
 * Revision 1.1  2009/06/02 05:27:13  hqh
 * 新建factory包
 *
 * Revision 1.8  2009/05/31 11:15:05  lj6061
 * 修改间隔下粘贴电压等级
 *
 * Revision 1.7  2009/05/31 10:58:57  lj6061
 * 添加复制粘贴操作
 *
 * Revision 1.6  2009/05/27 08:01:03  lj6061
 * 添加重命名
 *
 * Revision 1.5  2009/05/27 04:46:28  lj6061
 * 添加一次信息图片
 *
 * Revision 1.4  2009/05/26 07:44:46  lj6061
 * 去除接入点菜单
 *
 * Revision 1.3  2009/05/26 06:17:35  lj6061
 * 默认菜单新建SSD
 *
 * Revision 1.2  2009/05/25 03:56:10  lj6061
 * 修改连接点和拓扑节点
 *
 * Revision 1.1  2009/05/23 11:29:45  lj6061
 * 添加节点菜单事件
 *
 * Revision 1.3  2009/05/22 09:19:02  lj6061
 * 修改一次节点
 *
 * Revision 1.2  2009/05/22 03:04:00  lj6061
 * 修改节点属性添加优先级
 *
 * Revision 1.1  2009/05/21 05:43:05  lj6061
 * 为树添加邮件插入事件
 *
 */
public class PrimaryNodeFactory {
	
	/**
	 * 单例对象
	 */
	private static volatile PrimaryNodeFactory instance = new PrimaryNodeFactory();
	private LnInstMap lnMap = LnInstMap.getInstance();
	private int maxTopoNum = 0;
	private int maxNum = 0;
	private Element copyEle = null;
	private INaviTreeEntry copyEntry = null;
	private Map<INaviTreeEntry, Element> copyMap = new HashMap<INaviTreeEntry, Element>();
	private String targetXPath = null;
	private String newName = null;
	private String xpath = null;
	
	private boolean flag = false;
	/**
	 * 单例模式私有构造函数
	 */
	private PrimaryNodeFactory(){
	}
	
	/**
	 * 获取单例对象
	 */
	public static PrimaryNodeFactory getInstance(){
		if(null == instance) {
			synchronized (PrimaryNodeFactory.class) {
				if(null == instance) {
					instance = new PrimaryNodeFactory();
				}
			}
		}
		return instance;
	}

	/**
	 * 创建只含有属性name、desc节点对象
	 * 
	 * @param nodeName
	 *            节点名称
	 * @param name
	 * @param desc
	 */
	private Element createElement(String nodeName,String name, String desc){
		Element subEle = DOM4JNodeHelper.createSCLNode(nodeName);
		subEle.addAttribute("name", name); //$NON-NLS-1$
		subEle.addAttribute("desc", desc); //$NON-NLS-1$
		return subEle;
	}
	
	/**
	 * 给节点添加oid
	 * @param node
	 */
	private void addOid(Element node) {
		String oid = EquipmentSequence.getOid();
		Element prvNd = DOM4JNodeHelper.selectSingleNode(node, "./Private[@type='oid']");
		if(prvNd == null) {
			prvNd = node.addElement("Private");
			prvNd.addAttribute("type", "oid");
		}
		prvNd.setText(oid);
	}
	
	/**
	 * 更新oid
	 * @param node
	 */
	private void updateOid(Element node) {
		List<Element> prvNds = DOM4JNodeHelper.selectNodes(node, ".//Private[@type='oid']");
		for(Element prvNd : prvNds) {
			prvNd.setText(EquipmentSequence.getOid());
		}
	}
	
	/**
	 * 获取节点oid属性
	 * @param node
	 * @return
	 */
	private String getOid(Element node) {
		Element prvNd = DOM4JNodeHelper.selectSingleNode(node, "./Private[@type='oid']");
		if(null != prvNd) {
			return prvNd.getText();
		}
		return null;
	}

	/**
	 * 创建含有属性name,type节点对象
	 * @param nodeName 节点名称
	 * @param name
	 * @param type
	 */
	private Element createElement(String nodeName,String name,String type, String desc){
		Element codEle = DOM4JNodeHelper.createSCLNode(nodeName);
		codEle.addAttribute("name", name); //$NON-NLS-1$
		codEle.addAttribute("type", EnumEquipType.getModelType(type)); //$NON-NLS-1$
		codEle.addAttribute("desc", desc);
		addOid(codEle);
		return codEle;
	}
	
	/**
	 * 创建绕组
	 */
	public void createTranWinNode(int tWCount,INaviTreeEntry parent, Element pwEle){
		INaviTreeEntry pTWEntry = null ;
		
		for (int i = 1; i <= tWCount; i++) {
			String name = DefaultInfo.POWER_PTW_NAME+i;
			String twPath = parent.getXPath()
					+ "/TransformerWinding[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			String twType = DefaultInfo.POWER_PTW_TYPE;
			pTWEntry = new TransformerWindingEntry(name, name, twPath, ImageConstants.TRANSFORM, DefaultInfo.SUBS_TRANSFORM);
			pTWEntry.setType(twType);
			parent.addChild(pTWEntry);
			
			// 创建Element
			Element twEle = pwEle.addElement("TransformerWinding"); //$NON-NLS-1$
			twEle.addAttribute("name", name); //$NON-NLS-1$
			twEle.addAttribute("type", twType); //$NON-NLS-1$
			twEle.addAttribute("desc", name); //$NON-NLS-1$
			
			addOid(twEle);
			// 默认一个拓扑
			createTerminal(twEle, 1, (INaviTreeEntry) parent.getParent());
		}
	}
	
	/**
	 * 创建Terminal,不需要创建树节点 树上不显示
	 * 
	 * @param parent 父节点，关联对应连接点的描述
	 */
	private void createTerminal(Element twEle, int terminal, INaviTreeEntry parent) {
		List<String> conNodeName = new LinkedList<String>();
		String subsName  = null;
		String volLsName  = "null"; //$NON-NLS-1$
		String bayName  = "null"; //$NON-NLS-1$
		while (true) {
			if (parent.getParent() == null) {
				conNodeName.add(parent.getName());
				subsName = parent.getName();
				break;
			} else {
				if (parent.getPriority() == DefaultInfo.SUBS_VOLTAGEL) {
					conNodeName.add(parent.getName());
					volLsName = parent.getName();
				} else if (parent.getPriority() == DefaultInfo.SUBS_BAY) {
					conNodeName.add(parent.getName());
					bayName = parent.getName();
				}
				parent = (INaviTreeEntry) parent.getParent();
			}
		}
		String conNode = ""; //$NON-NLS-1$
		for (int i = conNodeName.size() - 1; i >= 0; i--) {
			conNode += conNodeName.get(i);
			conNode += "/"; //$NON-NLS-1$
		}
		// 特例变压器直接插入在变电站下 设connectivityNode添加间隔部分为null
		if(volLsName.equals("null")){ //$NON-NLS-1$
			conNode+="null/"; //$NON-NLS-1$
		}
		// 特例变压器直接插入在电压等级下 设connectivityNode添加间隔部分为null
		if(bayName.equals("null")){ //$NON-NLS-1$
			conNode+="null/"; //$NON-NLS-1$
		}
		Element terEle = null;
		for (int i = 1; i <= terminal; i++) {
			//父类添加Element
			terEle = twEle.addElement("Terminal"); //$NON-NLS-1$
			terEle.addAttribute("name", DefaultInfo.TERMINAL + i); //$NON-NLS-1$
			terEle.addAttribute("connectivityNode", conNode + "null"); //$NON-NLS-1$ //$NON-NLS-2$
			terEle.addAttribute("substationName", subsName); //$NON-NLS-1$
			terEle.addAttribute("voltageLevelName",volLsName); //$NON-NLS-1$
			terEle.addAttribute("bayName", bayName); //$NON-NLS-1$
			terEle.addAttribute("cNodeName", "null"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * 创建接地类的设备Terminal,不需要创建树节点 树上不显示
	 * 此方法只针对特殊的接地类设备（避雷针，接地刀闸）
	 * 
	 * @param parent 父节点，关联对应连接点的描述
	 */
	private void createGroundedTerminal(Element twEle, INaviTreeEntry parent) {
		List<String> conNodeName = new LinkedList<String>();
		String subsName  = null;
		String volLsName  = "null"; //$NON-NLS-1$
		String bayName  = "null"; //$NON-NLS-1$
		while (true) {
			if (parent.getParent() == null) {
				conNodeName.add(parent.getName());
				subsName = parent.getName();
				break;
			} else {
				if (parent.getPriority() == DefaultInfo.SUBS_VOLTAGEL) {
					conNodeName.add(parent.getName());
					volLsName = parent.getName();
				} else if (parent.getPriority() == DefaultInfo.SUBS_BAY) {
					conNodeName.add(parent.getName());
					bayName = parent.getName();
				}
				parent = (INaviTreeEntry) parent.getParent();
			}
		}
		String conNode = ""; //$NON-NLS-1$
		for (int i = conNodeName.size() - 1; i >= 0; i--) {
			conNode += conNodeName.get(i);
			conNode += "/"; //$NON-NLS-1$
		}
		Element terEle = null;
		terEle = twEle.addElement("Terminal"); //$NON-NLS-1$
		terEle.addAttribute("name", DefaultInfo.TERMINAL + 1); //$NON-NLS-1$
		terEle.addAttribute("connectivityNode", conNode + "null"); //$NON-NLS-1$ //$NON-NLS-2$
		terEle.addAttribute("substationName", subsName); //$NON-NLS-1$
		terEle.addAttribute("voltageLevelName",volLsName); //$NON-NLS-1$
		terEle.addAttribute("bayName", bayName); //$NON-NLS-1$
		terEle.addAttribute("cNodeName", "null"); //$NON-NLS-1$ //$NON-NLS-2$
		// 添加grounded类型对应的拓扑
		terEle = twEle.addElement("Terminal"); //$NON-NLS-1$
		terEle.addAttribute("name", DefaultInfo.TERMINAL + 2); //$NON-NLS-1$
		terEle.addAttribute("connectivityNode", conNode + "grounded"); //$NON-NLS-1$ //$NON-NLS-2$
		terEle.addAttribute("substationName", subsName); //$NON-NLS-1$
		terEle.addAttribute("voltageLevelName", volLsName); //$NON-NLS-1$
		terEle.addAttribute("bayName", bayName); //$NON-NLS-1$
		terEle.addAttribute("cNodeName", "grounded"); //$NON-NLS-1$ //$NON-NLS-2$
	}
		
	/**
	 * 创建执行装置中VTR,CTR的子设备
	 */	
	private void createCRSubE(Element codEle, INaviTreeEntry codEntry, String []lnClass) {
		INaviTreeEntry subEntry = null ;
		String [] subName = {"A","B","C"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String [] descs = {"A相","B相","C相"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		for (int i = 1; i <= subName.length; i++) {
			String name = subName[i - 1];
			String desc = descs[i - 1];
			String subPath = codEntry.getXPath()+"/SubEquipment[@name='"+name+"']"; //$NON-NLS-1$ //$NON-NLS-2$
			subEntry = new SubEquipmentEntry(name, desc, subPath,ImageConstants.SUB_EQUP, DefaultInfo.SUBS_SUBEQ);
			codEntry.addChild(subEntry);
			
			// 创建Element
			Element subEle = codEle.addElement("SubEquipment"); //$NON-NLS-1$
			subEle.addAttribute("name", name); //$NON-NLS-1$
			subEle.addAttribute("phase", name); //$NON-NLS-1$
			subEle.addAttribute("desc", desc);
			addOid(subEle);
			
			createLNode(subEntry, subEle, lnClass);
		}
		
	}

	/**
	 * 创建某节点的子LN节点 
	 * 确保逻辑节点不重复 需要5个字段判断
	 * @param parent
	 * @param parentEle
	 * @param lnClasses
	 */
	public void createLNode(INaviTreeEntry parent, Element parentEle, String[] lnClasses){
		LNodeEntry lnEntry = null;
		for (int i = 1; i <= lnClasses.length; i++) {
			String lnClass = lnClasses[i - 1];
			int inst = lnMap.nextLnInst(lnClass);
			String lnInst = String.valueOf(inst) ;
			String name = DefaultInfo.IED_NAME + "/." + lnClass + lnInst + ":"; //$NON-NLS-1$ //$NON-NLS-2$
			String twPath = parent.getXPath() + "/LNode[@iedName='None'][@ldInst='']"
						+ SCL.getLNAtts("", lnClass, lnInst);
			lnEntry = new LNodeEntry(name, twPath, ImageConstants.LNODE, DefaultInfo.SUBS_LNODE);
			lnEntry.setDefaults(lnClass, lnInst);
			parent.addChild(lnEntry);
			
			parentEle.add(createLNode(lnClass, lnInst).detach());
		}
	}
	
	/**
	 * 创建独立LN节点,并设备该类型逻辑节点最大
	 */	
	public Element createLNode(String lnClass, String lnInst){
		Element lnEle = DOM4JNodeHelper.createSCLNode("LNode"); //$NON-NLS-1$
		lnEle.addAttribute("iedName", DefaultInfo.IED_NAME); //$NON-NLS-1$
		lnEle.addAttribute("ldInst", ""); //$NON-NLS-1$ //$NON-NLS-2$
		lnEle.addAttribute("lnClass", lnClass); //$NON-NLS-1$
		lnEle.addAttribute("lnType", "null"); //$NON-NLS-1$ //$NON-NLS-2$
		lnEle.addAttribute("lnInst", lnInst); //$NON-NLS-1$
		lnEle.addAttribute("prefix", ""); //$NON-NLS-1$ //$NON-NLS-2$
		lnMap.setLnInst(lnClass, Integer.parseInt(lnInst));
		return lnEle;
	}
	
	/**
	 * 插入变电站父节点
	 * 
	 * @param name 名称
	 * @param desc 描述
	 */	
	public INaviTreeEntry insertSubstation(String name, String desc) {
		lnMap.clearMap();
		String subPath = SCL.XPATH_SUBSTATION;
		String xPath = subPath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		INaviTreeEntry entry = new SubstationEntry(name, desc, xPath, ImageConstants.SUBSTATION, DefaultInfo.SUBS_ROOT);
		//创建变电站时候默认添加电压等级和间隔
		Element subEle = createElement("Substation", name, desc); //$NON-NLS-1$
		
		// 异常情况下控制只存在一个Substation
		if (XMLDBHelper.existsNode(xPath)) {
			XMLDBHelper.removeNodes(xPath);
		}
		XMLDBHelper.insertAfter("/scl:SCL/scl:Header", subEle); //$NON-NLS-1$
		// 添加增量标记
		markAddHistory(name, DevType.STA, null);
		return entry;
	}

	
	/**
	 * 插入变压器
	 * 
	 * @param tWCount 几卷边
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 * @param pwEle 元素
	 * @powerEntry 元素
	 * 
	 */	
	public INaviTreeEntry insertPowerNode(int tWCount, String name, INaviTreeEntry parent, INaviTreeEntry markerEty ,List<INaviTreeEntry> treeList) {
		// 创建树节点
		name = name + maxNum;
		String xpath = parent.getXPath()+"/PowerTransformer[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		String type = (tWCount==3) ? EnumEquipType.PTR3 : EnumEquipType.PTR2;
		INaviTreeEntry powerEntry = new PowerTransformerEntry(name, name, type, xpath, null, DefaultInfo.SUBS_POWER);
		parent.addChild(powerEntry);
				
		//创建Element
		Element pwEle = createElement("PowerTransformer", name, type, name); //$NON-NLS-1$
		
		createLNode(powerEntry, pwEle,DefaultInfo.POWER_LN);
		createTranWinNode(tWCount, powerEntry, pwEle);
		
		insertTree(markerEty, treeList, powerEntry);
		insertNode(pwEle, powerEntry, parent);
		// 添加增量标记
		PrimaryUtil.markUpdateHistory(parent, name);
		return powerEntry;
	}
	
	/**
	 * 插入电压等级节点
	 * 
	 * @param name 节点名称
	 * @param parent 父节点
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 */	
	public void insertVoltageLev(INaviTreeEntry parent, INaviTreeEntry markerEty, List<INaviTreeEntry> treeList, String name, String desc, String voltage) {
		// 创建树节点
		String xpath = parent.getXPath()+"/VoltageLevel[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		INaviTreeEntry voLevEntry = new VoltageLevelEntry(name, desc, xpath, ImageConstants.VOLTAGE, DefaultInfo.SUBS_VOLTAGEL);
		parent.addChild(voLevEntry);
				
		//创建Element
		Element volLEle = createElement("VoltageLevel", name, desc); //$NON-NLS-1$
		Element volEle = volLEle.addElement("Voltage"); //$NON-NLS-1$
		volEle.addAttribute("unit", "V"); //$NON-NLS-1$ //$NON-NLS-2$
		volEle.addAttribute("multiplier", "k"); //$NON-NLS-1$ //$NON-NLS-2$
		volEle.addText(voltage);
		
		insertVolTree(parent, markerEty, treeList, voLevEntry);
		insertNode(volLEle, voLevEntry, parent);
		// 添加增量标记
		markAddHistory(name, DevType.VOL, null);
	}
		
	/**
	 * 插入功能
	 * @param parent 父节点
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 * @param name 名称
	 */	
	public INaviTreeEntry insertFunction(INaviTreeEntry parent, INaviTreeEntry markerEty, List<INaviTreeEntry> treeList, String name, String desc, String type){
		// 创建树节点
		String xpath = parent.getXPath()+"/Function[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		INaviTreeEntry funEntry = new FunctionEntry(name, desc, type, xpath, ImageConstants.FUNCTION, DefaultInfo.SUBS_FUNCTION);
		parent.addChild(funEntry);
				
		//创建Element
		Element funEle = createElement("Function", name, desc, type); //$NON-NLS-1$
		
		insertTree(markerEty, treeList, funEntry);
		insertNode(funEle, funEntry, parent);
		PrimaryUtil.markUpdateHistory(parent, name);
		return funEntry;
	}
	
	public INaviTreeEntry insertBusbar(INaviTreeEntry parent, INaviTreeEntry markerEty, List<INaviTreeEntry> treeList, String name, String desc, String type){
		boolean isBusbar = type.equals(DefaultInfo.BUSBAR);
		// 创建树节点
		String xpath = parent.getXPath()+"/ConductingEquipment[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		BayEntry bayEntry = new BayEntry(name, desc, type, xpath, ImageConstants.BAY, DefaultInfo.SUBS_CONDUCT);
		bayEntry.setBusbar(isBusbar);
		parent.addChild(bayEntry);
				
		//创建Element
		Element bayEle = createElement("ConductingEquipment", name, type, desc); //$NON-NLS-1$
		addOid(bayEle);
//		
		insertTree(markerEty, treeList, bayEntry);
		insertNode(bayEle, bayEntry, parent);
		// 添加增量标记
		markAddHistory(name, DevType.IED, getOid(bayEle));
		return bayEntry;
	}
	
	/**
	 * 插入间隔
	 * @param parent 父节点
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 * @param name 名称
	 * @param isBusbar 该间隔是否为母线
	 */	
	public INaviTreeEntry insertBay(INaviTreeEntry parent, INaviTreeEntry markerEty, List<INaviTreeEntry> treeList, String name, String desc, String type){
		boolean isBusbar = type.equals(DefaultInfo.BUSBAR);
		// 创建树节点
		String xpath = parent.getXPath()+"/Bay[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		BayEntry bayEntry = new BayEntry(name, desc, type, xpath, ImageConstants.BAY, DefaultInfo.SUBS_BAY);
		bayEntry.setBusbar(isBusbar);
		parent.addChild(bayEntry);
				
		//创建Element
		Element bayEle = createElement("Bay", name, desc); //$NON-NLS-1$
		addOid(bayEle);
		if (!StringUtil.isEmpty(type)) {// 母线、虚拟间隔增加类别标识
			Element prvNode = DOM4JNodeHelper.createSCLNode("Private"); //$NON-NLS-1$
			prvNode.addAttribute("type", type); //$NON-NLS-1$
			bayEle.add(prvNode);
		}
		if (isBusbar) { // 如果是母线，还需为其添加<Private type="Busbar"/>,// <ConnectivityNode>
			if (!flag) {
				initmaxTopoNum();
				flag = true;
			}
			String conNodeName = "L" + maxTopoNum++;
			// <ConnectivityNode>
			Element conNode = createElement("ConnectivityNode", conNodeName, conNodeName); //$NON-NLS-1$
			String pathName = getPathName(xpath);
			pathName = pathName + "/" + conNodeName; //$NON-NLS-1$
			conNode.addAttribute("pathName", pathName); //$NON-NLS-1$
			bayEle.add(conNode);
		}
		
		insertTree(markerEty, treeList, bayEntry);
		insertNode(bayEle, bayEntry, parent);
		// 添加增量标记
		markAddHistory(name, DevType.BAY, getOid(bayEle));
		return bayEntry;
	}
	
	private String getPathName(String xpath) {
		StringTokenizer pathToken = new StringTokenizer(xpath, "/"); //$NON-NLS-1$
		StringBuilder pathName = new StringBuilder();
		while(pathToken.hasMoreElements()) {
			String node = pathToken.nextToken();
			String[] ndArr = node.split("'"); //$NON-NLS-1$
			if(ndArr.length > 2) {
				pathName.append("/" + ndArr[1]); //$NON-NLS-1$
			}
		}
		String result = pathName.toString();
		return result.substring(1);
	}

	/**
	 * 插入LN,LN优先级最高 一次树Ln通过5个字段判断
	 * @param parent 父节点
	 * 
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 */	
	public void insertLN(String lnClass, INaviTreeEntry parent, INaviTreeEntry markerEty, List<INaviTreeEntry> treeList) {
		String name = DefaultInfo.IED_NAME + "/." + lnClass + maxNum + ":"; //$NON-NLS-1$ //$NON-NLS-2$
		String prefix =""; //$NON-NLS-1$
		// 创建树节点
		String xpath = parent.getXPath() + "/LNode[@iedName='None'][@ldInst='']"
					+ SCL.getLNAtts(prefix, lnClass, maxNum+"");
		LNodeEntry lnEntry = new LNodeEntry(name, xpath, ImageConstants.LNODE, DefaultInfo.SUBS_LNODE);
		lnEntry.setDefaults(lnClass, String.valueOf(maxNum));
		parent.addChild(lnEntry);
				
		Element lnele = createLNode(lnClass, String.valueOf(maxNum));
		
		insertTree(markerEty, treeList, lnEntry);
		insertNode(lnele, lnEntry, parent);
		// 添加增量标记
		PrimaryUtil.markUpdateHistory(parent, parent.getName());
	}
	
	
	/**
	 * 插入子设备
	 * 
	 * @param name 节点名称
	 * @param parent 父节点
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 */	
	public void insertSubEqu(INaviTreeEntry parent, INaviTreeEntry markerEty ,List<INaviTreeEntry> treeList){
		String contype = parent instanceof ConductingEquipmentEntry ? ((TreeEntryImpl) parent).getType() + "_" : "";//子设备若为导电设备的某一部件，则name使用相应“设备类型代码”加后缀
		// 创建树节点
		String name = contype + DefaultInfo.SUBEQ_NAME + maxNum;
		String xpath = parent.getXPath()+"/SubEquipment[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		INaviTreeEntry subEntry = new SubEquipmentEntry(name, name, xpath, ImageConstants.SUB_EQUP, DefaultInfo.SUBS_SUBEQ);
		parent.addChild(subEntry);
				
		//创建Element
		Element subEle = createElement("SubEquipment", name, name); //$NON-NLS-1$
		addOid(subEle);
		
		insertTree(markerEty, treeList, subEntry);
		insertNode(subEle, subEntry, parent);
		// 添加增量标记
		PrimaryUtil.markUpdateHistory(parent, name);
	}
	
	/**
	 * 插入子功能
	 * @param parent 父节点
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 * @param name 节点名称
	 * @param desc 描述
	 */	
	public INaviTreeEntry insertSubFun(INaviTreeEntry parent, INaviTreeEntry markerEty, List<INaviTreeEntry> treeList, String name, String desc){
		// 创建树节点
		String xpath = parent.getXPath()+"/SubFunction[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		INaviTreeEntry subEntry = new SubFunctionEntry(name, desc, xpath, ImageConstants.SUB_FUNCTION, DefaultInfo.SUBS_SUBFUN);
		parent.addChild(subEntry);
				
		//创建Element
		Element subEle = createElement("SubFunction", name, desc); //$NON-NLS-1$
		
		insertTree(markerEty, treeList, subEntry);
		parent.setChildren(new ArrayList<ITreeEntry>());
		// 设置排序前的节点顺序
		for (INaviTreeEntry child : treeList) {
			parent.addChild(child);
		}
		insertNode(subEle, subEntry, parent);
		PrimaryUtil.markUpdateHistory(parent, name);
		return subEntry;
	}
	
	/**
	 * 插入普通设备
	 * 
	 * @param parent 父节点
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 */	
	public INaviTreeEntry insertGeneral(INaviTreeEntry parent, INaviTreeEntry markerEty, List<INaviTreeEntry> treeList, String name, String type, String [] lnName) {
		// 创建树节点
		name = name + maxNum;
		String xpath = parent.getXPath()+"/GeneralEquipment[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		INaviTreeEntry genEntry = new GeneralEquipmentEntry(name, name, type, xpath, ImageConstants.FOLDER, DefaultInfo.SUBS_GENERAL);
		parent.addChild(genEntry);
		
		//创建Element
		Element genEle = createElement("GeneralEquipment", name, type, name); //$NON-NLS-1$
		
		createLNode(genEntry, genEle, lnName);
		
		insertTree(markerEty, treeList, genEntry);
		insertNode(genEle, genEntry, parent);
		// 添加增量标记
		PrimaryUtil.markUpdateHistory(parent, name);
		return genEntry;
	}
	
	
	/**
	 * 插入导电设备
	 * @param parent 父节点
	 * @param markerEty 插入标识节点
	 * @param treeList 子节点集合
	 */	
	public INaviTreeEntry insertConduct(INaviTreeEntry parent, INaviTreeEntry markerEty, List<INaviTreeEntry> treeList, String name, String type,
			String[] lnName, int treNum) {
		// 创建树节点
		name = name + maxNum;
		String xpath = parent.getXPath()+"/ConductingEquipment[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		INaviTreeEntry codEntry = new ConductingEquipmentEntry(name, name, type, xpath, ImageConstants.FOLDER, DefaultInfo.SUBS_CONDUCT);
		parent.addChild(codEntry);
		
		//创建Element
		Element codEle = createElement("ConductingEquipment", name, type, name); //$NON-NLS-1$
		
		if (EnumEquipType.isCVTR(type)) {
			createTerminal(codEle, treNum, parent);
			createCRSubE(codEle, codEntry, lnName);
		} else {
			if (lnName != null)
				createLNode(codEntry, codEle, lnName);
			String nodeType = codEntry.getType();
			createTerminal(codEle, parent, nodeType, treNum);
		}
		
		insertTree(markerEty, treeList, codEntry);
		insertNode(codEle, codEntry, parent);
		// 添加增量标记
		PrimaryUtil.markUpdateHistory(parent, name);
		return codEntry;
	}
	
	/**
	 * 如果是接地类设备 需要自动创建连接点和一个grounded类型的拓扑
	 * @param codEle
	 * @param parent
	 * @param type
	 * @param treNum
	 */
	private void createTerminal(Element codEle,INaviTreeEntry parent, String type, int treNum){
		if (EnumEquipType.hasGrounded(type)) {
			createGroundedTerminal(codEle, parent);
		} else {
			createTerminal(codEle, treNum, parent);
		}
	}
	
	/**
	 * 插入节点信息到树和对应文件
	 * @param markerEty插入标识节点
	 * @param treeList子节点集合
	 * @param insertEle
	 * @powerEntry 元素
	 */	
	private void insertNode(final Element insertEle, final INaviTreeEntry treeEntry, final INaviTreeEntry parent) {
		PrimaryUtil.insertInDB(insertEle, treeEntry, parent);
	}
	
	/**
	 * 插入节点信息到树
	 * @param markerEty插入标识节点
	 * @param treeList子节点集合
	 * @powerEntry 元素
	 * 
	 */	
	public void insertVolTree(INaviTreeEntry parent, INaviTreeEntry lastEn, List<INaviTreeEntry> treeList, INaviTreeEntry treeEntry) {
		// 定位插入树节点
		int index = 0;
		if (lastEn == null) {
			treeList.add(index, treeEntry);
			return;
		}
		index = treeList.size();
		if (treeList.size() == 0 || treeEntry.getName().equals("")) {
			treeList.add(index, treeEntry);
			return;
		}
		int vol = getValue(treeEntry);
		List<ITreeEntry> list = new ArrayList<ITreeEntry>();
		boolean hasAdd = false;
		for (INaviTreeEntry naviTreeEntry : treeList) {
			int currVol = getValue(naviTreeEntry);
			if (!hasAdd && vol > currVol) {
				list.add(treeEntry);
				hasAdd = true;
			}
			list.add(naviTreeEntry);
		}
		if (list.size() == index)
			list.add(treeEntry);
		parent.setChildren(list);
		
		PrimaryTreeDataFactory.getInstance().setModified(true);
	}
	
	private int getValue(INaviTreeEntry o1){
		String name = ((TreeEntryImpl)o1).getName();
		return StringUtil.isEmpty(name) ? -1 : Integer.parseInt(name.split("[^\\d]")[0]);
	}
	
	/**
	 * 插入节点信息到树
	 * @param markerEty插入标识节点
	 * @param treeList子节点集合
	 * @powerEntry 元素
	 * 
	 */	
	public void insertTree(INaviTreeEntry lastEn, List<INaviTreeEntry> treeList, INaviTreeEntry treeEntry) {
		// 定位插入树节点
		int index = 0;
		if (lastEn == null||treeList.size()==0){
			treeList.add(index, treeEntry);
			return;
		}		
		index= treeList.indexOf(lastEn);
		if (treeEntry.getPriority() >= lastEn.getPriority()) {
			treeList.add(index + 1, treeEntry);
		}else{			
			treeList.add(index, treeEntry);
		}
		PrimaryTreeDataFactory.getInstance().setModified(true);
	}
	
	/**
	 * 判断该节点应该插入位置的标识节点
	 * 除类型为Lnode外的节点
	 * 
	 */
	public INaviTreeEntry getlastEntry(List<INaviTreeEntry> sortTree, int subPrior, String defName){
		maxNum = 0;
		getSortList(sortTree);
		INaviTreeEntry markerEty = null;
		for (ITreeEntry tEntry : sortTree) {
			INaviTreeEntry treeEntry = (INaviTreeEntry)tEntry;
			int prior = treeEntry.getPriority();
			if (prior == subPrior) {
				getInsertNum(treeEntry, defName);
				markerEty = treeEntry;
				continue;
			} else if (prior < subPrior) {
				markerEty = treeEntry;
				continue;
			} else {
				if (markerEty != null) {
					if (markerEty.getPriority() != subPrior)
						markerEty = treeEntry;
				} else
					markerEty = treeEntry;
				break;
			}
		}
		maxNum++;
		return markerEty;
	}

	/**
	 * 插入节点最大下标 
	 * 
	 */
	private void getInsertNum(INaviTreeEntry treeEntry, String defName) {
		String name = treeEntry.getName();
		int prior = treeEntry.getPriority();
		if (name.equals(defName))//粘贴在同父节点下，名称相同
			return;
		if (name.contains(defName)) {
			if (name.indexOf(defName) == 0) {
				String sublnst = null;
				int inst = 0;
				if (prior == 1) return;
				sublnst = name.substring(defName.length(), name.length());
				if (!sublnst.equals("") && StringUtil.verifyNum(sublnst)) //$NON-NLS-1$
						inst = Integer.parseInt(sublnst);
				if (inst > maxNum)
					maxNum = inst;
			}
		}
	}
	
	/**
	 * 插入逻辑节点标识节点
	 * 
	 */
	public INaviTreeEntry getlastLnEntry(List<INaviTreeEntry> sortTree, String lnClass){
		maxNum = lnMap.getLnInst(lnClass) + 1;
		getSortList(sortTree);
		INaviTreeEntry markerEty = null;
		for (INaviTreeEntry treeEntry : sortTree) {
			int prior = treeEntry.getPriority();
			if (prior <= DefaultInfo.SUBS_LNODE) {
				markerEty = treeEntry;
				continue;
			}else {
				if (markerEty != null) {
					if (markerEty.getPriority() != DefaultInfo.SUBS_LNODE)
						markerEty = treeEntry;
				}
				break;
			}
		}
		return markerEty;
	}
	
	/**
	 * 由于树上顺序不是按照文件顺序排列！需要对其排序
	 */
	public List<INaviTreeEntry> getSortList(List<INaviTreeEntry> treeList){
		for (int i = 0; i < treeList.size(); i++) {
			for (int j = i + 1; j < treeList.size(); j++) {
				if (treeList.get(i).getPriority() > treeList.get(j).getPriority()) {
					Collections.swap(treeList, i, j);
				}
			}	
		}
		return treeList;
	}
	
	/**
	 * 将复制的节点插入树
	 * 由于需要满足Schema条件 LNName不得重复，需要对附近的树节点和Ele中的lN便利修改
	 * @param parent
	 * @param markerEty
	 * @param treeList
	 */
	public void pasteTree(INaviTreeEntry parent, INaviTreeEntry markerEty,
			List<INaviTreeEntry> treeList, List<String> names) {
		pasteTree(parent, markerEty, treeList, true, names);
	}
	
	public void pasteTree(INaviTreeEntry parent, INaviTreeEntry markerEty,
			List<INaviTreeEntry> treeList, boolean blParentIsEmpty, List<String> names) {
		if (copyEle == null)
			return;
		ITreeEntry oldParent = copyEntry.getParent();
		if (oldParent == null)
			return;
		String oldName = copyEntry.getName();
		StringBuffer sbNewName = new StringBuffer(oldName);
		if (parent.equals(oldParent) || !blParentIsEmpty) { // 与被复制节点在同一父节点下，或父节点下无子节点
			sbNewName.append("(Copy)");
			sbNewName.append(maxNum);
		}

		String newName = getOnlyName(names, sbNewName.toString()); //$NON-NLS-1$
		this.newName = newName;
		// 修改复制节点的LN唯一
		updateCopyLNInst(copyEntry, copyEle);
		// 修改该节点对应的父节点和路径
		updateParent(copyEntry, parent, oldName, newName);
		// 对于复制的节点 清除所有的连接点信息
		PrimaryUtil.removeConNode(copyEle);
		
		String subName = null;
		String volName = null;
		String bayName = null;
		INaviTreeEntry tempEntry = parent;
		while(tempEntry != null) {
			int p = tempEntry.getPriority();
			String tempName = tempEntry.getName();
			if (p == DefaultInfo.SUBS_ROOT) {
				subName = tempName;
			} else if (p == DefaultInfo.SUBS_VOLTAGEL) {
				volName = tempName;
			} else if (p == DefaultInfo.SUBS_BAY) {
				bayName = tempName;
			}
			tempEntry = (INaviTreeEntry) tempEntry.getParent();
		}
		
		// 修改Terminal部分由于粘帖的父节点名称改变, 清除拓扑关系
		PrimaryUtil.updateTerminal(copyEle, subName, volName, bayName, true);

		copyEle.attribute("name").setValue(newName); //$NON-NLS-1$
		copyEntry.setName(newName);
		// 根据粘贴的父节点，创建Xpath
		EntryUtil.changeParentXPath(copyEntry, parent.getXPath());
		xpath = copyEntry.getXPath();

		insertTree(markerEty, treeList, copyEntry);
		parent.setChildren(new ArrayList<ITreeEntry>());
		// 设置排序前的节点顺序
		for (INaviTreeEntry child : treeList) {
			parent.addChild(child);
		}
		// 增量标记处理
		if (copyEntry instanceof SubstationEntry) {
			markAddHistory(newName, DevType.STA, null);
		} else if (copyEntry instanceof VoltageLevelEntry) {
			markAddHistory(newName, DevType.VOL, null);
		} else if (copyEntry instanceof BayEntry) {
			markAddHistory(newName, DevType.BAY, getOid(copyEle));
		} else {
			PrimaryUtil.markUpdateHistory(parent, newName);
		}
		if (null != getOid(copyEle))
			updateOid(copyEle);
		// 更新树和xmldb
		insertNode(copyEle, copyEntry, parent);
	}
	
	private String getOnlyName(List<String> names, String newName){
		if(names.contains(newName)){
			int indexOf = newName.indexOf(")") + 1;
			newName = (newName.substring(0, indexOf) + (Integer.parseInt(newName.substring(indexOf)) + 1));
			return getOnlyName(names, newName);
		} 
		return newName;
	}
	
	/**
	 * 将复制的XML节点遍历修改 并同时修改对应的树节点
	 * 
	 */
	private void updateCopyLNInst(INaviTreeEntry copyEntry,Element copyEle){
		
		String xpath = ".//LNode"; //$NON-NLS-1$
		List<Element> lnEleList = DOM4JNodeHelper.selectNodes(copyEle, xpath);
		for (Element element : lnEleList) {
			String lnClass = element.attributeValue("lnClass"); //$NON-NLS-1$
			String lnInst = element.attributeValue("lnInst"); //$NON-NLS-1$
			String prefix = element.attributeValue("prefix"); //$NON-NLS-1$
			String lnName = getLnName(prefix, lnClass, lnInst);
			String newInst = String.valueOf(lnMap.nextLnInst(lnClass));
			// 粘贴的节点必须要解除关联,设置新的名称和Xpath
			updataLNEntry(copyEntry, lnName, newInst, lnInst);
			updataLnElement(element, newInst);
		}	
				
	}
	
	/**
	 * 粘贴节点时候 节点中已经关联的节点解除关联，并设置新的LnInst值
	 * @param copyEntry
	 * @param element
	 * @param newInst
	 */
	private void updataLnElement(Element element, String newInst) {
		// 节点解除关联的值iedName = None ldInst="" lnType=null
		String iedName = element.attributeValue("iedName"); //$NON-NLS-1$
		if (!iedName.equals("None")) { //$NON-NLS-1$
			element.addAttribute("iedName", "None"); //$NON-NLS-1$ //$NON-NLS-2$
			element.addAttribute("ldInst", ""); //$NON-NLS-1$ //$NON-NLS-2$
			element.addAttribute("lnType", "null"); //$NON-NLS-1$ //$NON-NLS-2$
			element.addAttribute("prefix", DefaultInfo.UNREL_LNODE_PREFIX);
		}
		// 修改Inst的值
		element.addAttribute("lnInst", newInst); //$NON-NLS-1$
	}

	/**
	 * 将复制的节点插入树
	 * 由于需要满足Schema条件 LNName不得重复，需要对附近的树节点和Ele中的lN便利修改
	 * @param selectEntry
	 * @param parent
	 * @param markerEty
	 * @param copyEle
	 */
	public void importTreeByEle(INaviTreeEntry selectEntry, INaviTreeEntry parent,
			INaviTreeEntry markerEty, Element copyEle) {
		List<INaviTreeEntry> treeList = new ArrayList<INaviTreeEntry>();
		for (ITreeEntry entry : parent.getChildren()) {
			treeList.add((INaviTreeEntry) entry);
		}
		// 修改复制节点的LN唯一
		updateCopyLNInst(selectEntry, copyEle);
		// 更新oid
		updateOid(copyEle);
		String xPath = parent.getXPath();
		selectEntry.setXpath(xPath + selectEntry.getXPath());
		PrimaryUtil.resetXpath(selectEntry, xPath);

		insertTree(markerEty, treeList, selectEntry);
		parent.setChildren(new ArrayList<ITreeEntry>());
		// 设置排序前的节点顺序
		for (INaviTreeEntry child : treeList) {
			parent.addChild(child);
		}
		selectEntry.setParent(parent);
		insertNode(copyEle, selectEntry, parent);
	}
	
	/**
	 * 迭代更改节点对应的Xpath,和父节点名称
	 * 
	 */
	private void updateParent(INaviTreeEntry copyEntry,INaviTreeEntry parent ,String oldName, String newName){
		String oldPath = copyEntry.getXPath();
		copyEntry.setParent(parent);
		if(oldPath.contains(oldName)){
			int slashPos=oldPath.lastIndexOf("/");
			String curType=oldPath.substring(slashPos, oldPath.length());
			String last = curType.substring(0, curType.lastIndexOf(oldName));//避免父节点和重命名子节点相同，全部替换
			String newPath =parent.getXPath()+last + newName + "']"; //$NON-NLS-1$
			copyEntry.setXpath(newPath);
			PrimaryUtil.renameXpath(copyEntry, newPath, oldPath);	
		}
	}
	
	
	/**
	 * 迭代找寻对应的树节点操作
	 * 
	 */
	private void updataLNEntry(INaviTreeEntry copyEntry, String lnName, String newInst,String oldInst) {
		List<ITreeEntry> children = copyEntry.getChildren();
		for (ITreeEntry child : children) {
			INaviTreeEntry treeEntry = (INaviTreeEntry) child;
			String xpath = treeEntry.getXPath();
			if (child instanceof LNodeEntry) {
				String instAttr = "@lnInst=";
				int instPos = xpath.indexOf(instAttr);
				int endPos = xpath.indexOf("'", instPos + instAttr.length() + 1);
				String inst = xpath.substring(instPos + instAttr.length() + 1, endPos);
				if (!oldInst.equals(inst))
					continue;
				setNewLnName(treeEntry, lnName, newInst, oldInst);
				return;
			} else {
				updataLNEntry(treeEntry, lnName, newInst, oldInst);
			}
		}
	}
	
	/**
	 * 获得Ln的全名
	 * 
	 */
	private String getLnName (String prefix ,String lnClass, String lnInst){
		String lnName = null;
		if (prefix == null && lnInst != null)
			lnName = lnClass + lnInst;
		else if ((prefix != null && lnInst == null)){
			lnName = prefix + lnClass;
		}else if ((prefix == null && lnInst == null)){
			lnName = lnClass;
		}else
			lnName = prefix + lnClass + lnInst;
		return lnName;
	}
	
	/**
	 * 设置新的Ln名称，如果存在已经关联节点，需解除关联。
	 * @param child
	 * @param eleLnName
	 * @param newInst
	 * @param oldInst
	 */
	private void setNewLnName(INaviTreeEntry child, String eleLnName,String newInst, String oldInst) {
		LNodeEntry lnodeEntry = (LNodeEntry) child;
		String lnClass = lnodeEntry.getLnClass();
		lnodeEntry.setDefaults(lnClass, newInst);
		String parentXPath = XPathUtil.getParentXPath(child.getXPath());
		lnodeEntry.setXpath(parentXPath + "/LNode[@iedName='None'][@ldInst='']"
				+ SCL.getLNAtts("", lnClass, newInst));
	}

	/**
	 * 复制时读取文件中的内容
	 */
	public void copyElement( final List<INaviTreeEntry> selectEntry) {
		clearCopyMap();
		for (INaviTreeEntry treeEntry : selectEntry) {
			Element element = XMLDBHelper.selectSingleNode(treeEntry.getXPath());
			if (element != null) {
				copyMap.put(treeEntry,element);
			}
		}
	}
	
	public void copyElement(List<INaviTreeEntry> selectEntry,boolean synchronizable){
		clearCopyMap();
		if(synchronizable){
			for (INaviTreeEntry treeEntry : selectEntry) {
				Element element = XMLDBHelper.selectSingleNode(treeEntry.getXPath());
				if (element != null) {
					copyMap.put(treeEntry,element);
				}
			}
		}else{
			copyElement(selectEntry);
		}
	}
	
	public String getTargetXPath() {
		return targetXPath;
	}

	public void setTargetXPath(String targetXPath) {
		this.targetXPath = targetXPath;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public Map<INaviTreeEntry, Element> getCopyMap() {
		return copyMap;
	}
	
	public void clearCopyMap() {
		copyMap.clear();
	}
	
	public void setCopyEle(Element copyEle) {
		this.copyEle = copyEle;
	}

	public void setCopyEntry(INaviTreeEntry copyEntry) {
		this.copyEntry = copyEntry;
	}

	public String getNewName() {
		return newName;
	}

	public String getXpath() {
		return xpath;
	}

	public int getMaxTopoNum() {
		return maxTopoNum;
	}

	public void setMaxTopoNum(int maxTopoNum) {
		this.maxTopoNum = maxTopoNum;
	}
	
	/**
	 * 从数据库中获取最大的母线连接点的Lx的x值并赋给maxTopoNum
	 */
	public void initmaxTopoNum(){
		int maxTopoNum = 0;
		// 遍历
		Element sub = XMLDBHelper.selectSingleNode("/scl:SCL/scl:Substation"); //$NON-NLS-1$
		List<?> listofBay = sub.selectNodes(".//Bay"); //$NON-NLS-1$
		for (Object obj : listofBay) {
			Element e = (Element) obj;
			if (SCL.isBusbay(e)) {
				Element connectElement=e.element("ConnectivityNode");
				if(connectElement==null) continue;
				String cNodeName = connectElement.attributeValue("name"); //$NON-NLS-1$ //$NON-NLS-2$
				if(cNodeName==null) continue;
				int temp = new Integer(cNodeName.substring(1));
				maxTopoNum = temp>maxTopoNum ? temp : maxTopoNum;
			}
		}
		setMaxTopoNum(maxTopoNum+1);
	}
	
	/**
	 * 判断间隔是否为母线间隔
	 * @param bay
	 * @return
	 */
	public boolean isBusbay(Element bay){
		if(bay.elements().size()==1 && bay.elements("ConnectivityNode").size()==1) //$NON-NLS-1$
			return true;
		return false;
	}
	
	public void insertCNodeforBusbay(){
		  // 为新的母线添加ConnectivityNode元素
		initmaxTopoNum();
        int busbarCNodeNumber = getMaxTopoNum();
		String conNodeName = "L" + (busbarCNodeNumber); //$NON-NLS-1$
		setMaxTopoNum(busbarCNodeNumber+1);
		
		final Element cNode = createConnectivityNode(xpath, conNodeName);
		insertCNode(xpath, cNode);
	}

	private Element createConnectivityNode(String newXpath, String conNodeName){
		Element cNode = DOM4JNodeHelper.createSCLNode("ConnectivityNode"); //$NON-NLS-1$
		cNode.addAttribute("name", conNodeName); //$NON-NLS-1$
		String pathName = getPathName(newXpath);
		pathName = pathName + "/" + conNodeName;  //$NON-NLS-1$
		cNode.addAttribute("pathName", pathName); //$NON-NLS-1$
		return cNode;
	}
	
	private void insertCNode(final String newXpath, final Element ConnectivityNode){
		XMLDBHelper.insertAsLast(newXpath, ConnectivityNode);
	}
	
	/**
	 * 添加历史增量标记
	 * @param oldName
	 * @param newName
	 */
	private void markAddHistory(String name, DevType type, String oid){
		HistoryManager manager = HistoryManager.getInstance();
		manager.markDevChanged(type, OperType.ADD, name, oid);
	}
}
