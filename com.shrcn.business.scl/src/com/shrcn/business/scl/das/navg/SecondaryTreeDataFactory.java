/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das.navg;

import java.util.ArrayList;
import java.util.List;

import com.shrcn.business.scl.das.CommunicationDAO;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.model.IED;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.CommunicationEntry;
import com.shrcn.business.scl.model.navgtree.DeviceEntry;
import com.shrcn.business.scl.model.navgtree.HistoryEntry;
import com.shrcn.business.scl.model.navgtree.IEDEntry;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.SubNetEntry;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.dbxapi.io.OpenByStringHandler;
import com.shrcn.found.dbxapi.io.ScdSaxParser;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.ImageConstants;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-3-3
 */
/*
 * 修改历史 $Log: SecondaryTreeDataFactory.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:38:32  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.2  2011/11/03 02:33:43  cchun
 * 修改历史 Update:去掉未使用的方法
 * 修改历史
 * 修改历史 Revision 1.1  2011/09/09 07:40:09  cchun
 * 修改历史 Refactor:转移包位置
 * 修改历史
 * 修改历史 Revision 1.12  2011/07/11 09:23:49  cchun
 * 修改历史 Update:去掉不必要的常量定义
 * 修改历史
 * 修改历史 Revision 1.11  2011/06/22 02:53:05  cchun
 * 修改历史 Update:装置提示增加制造厂商信息
 * 修改历史
 * 修改历史 Revision 1.10  2011/02/22 06:06:25  cchun
 * 修改历史 Update:去掉无用方法
 * 修改历史
 * 修改历史 Revision 1.9  2011/01/27 01:03:01  cchun
 * 修改历史 Add:聂国勇增加，增加数据模板的详细显示功能
 * 修改历史
 * 修改历史 Revision 1.8  2011/01/24 08:36:56  cchun
 * 修改历史 Update:将数据模板和IED视图分离
 * 修改历史
 * 修改历史 Revision 1.7  2011/01/10 09:23:49  cchun
 * 修改历史 Refactor:将通信、IED查询处理分开
 * 修改历史
 * 修改历史 Revision 1.6  2010/12/29 06:30:43  cchun
 * 修改历史 Update:增加过滤方法
 * 修改历史
 * 修改历史 Revision 1.5  2010/12/24 03:48:49  cchun
 * 修改历史 Update:信号关联检查改成数据模板
 * 修改历史
 * 修改历史 Revision 1.4  2010/09/07 10:02:01  cchun
 * 修改历史 Update:更换过时接口
 * 修改历史
 * 修改历史 Revision 1.3  2010/09/03 03:37:57  cchun
 * 修改历史 Update:IED树结点按type归类排列
 * 修改历史
 * 修改历史 Revision 1.2  2010/08/10 03:48:01  cchun
 * 修改历史 Refactor:修改方法名
 * 修改历史
 * 修改历史 Revision 1.1  2010/03/02 07:49:59  cchun
 * 修改历史 Add:添加重构代码
 * 修改历史
 * 修改历史 Revision 1.5  2010/02/08 10:41:39  cchun
 * 修改历史 Refactor:完成第一阶段重构
 * 修改历史
 * 修改历史 Revision 1.4  2010/01/28 07:31:32  cchun
 * 修改历史 Refactor:为方便代码维护，将IEDGraphEditor移动到com.shrcn.sct.draw插件中
 * 修改历史
 * 修改历史 Revision 1.3  2010/01/21 08:48:49  gj
 * 修改历史 Update:完成UI插件的国际化字符串资源提取
 * 修改历史
 * 修改历史 Revision 1.2  2009/08/27 02:26:20  cchun
 * 修改历史 Refactor:重构导航树模型包路径
 * 修改历史
 * 修改历史 Revision 1.1  2009/08/12 01:46:32  cchun
 * 修改历史 Refactor:重构类名TreeDataFactory->SecondaryTreeDataFactory
 * 修改历史
 * 修改历史 Revision 1.7  2009/07/02 01:50:38  hqh
 * 修改历史 单例 set null
 * 修改历史
 * 修改历史 Revision 1.6  2009/06/22 03:49:44  cchun
 * 修改历史 Update:修改editor名称为关联检查
 * 修改历史
 * 修改历史 Revision 1.5  2009/06/17 11:26:54  hqh
 * 修改历史 添加连线工具
 * 修改历史
 * 修改历史 Revision 1.4  2009/06/16 12:31:23  lj6061
 * 修改历史 初始化隐藏信息窗
 * 修改历史
 * 修改历史 Revision 1.2  2009/06/02 09:13:14  gj
 * 修改历史 删除绘图功能
 * 修改历史
 * 修改历史 Revision 1.1  2009/06/02 05:27:14  hqh
 * 修改历史 新建factory包
 * 修改历史
 * 修改历史 Revision 1.26  2009/05/26 05:36:15  hqh
 * 修改历史 修改数据工厂
 * 修改历史
 * 修改历史 Revision 1.25  2009/05/23 11:31:01  lj6061
 * 修改历史 添加2次图片
 * 修改历史
 * 修改历史 Revision 1.24  2009/05/23 10:20:32  cchun
 * 修改历史 Update:重构IEDDAO
 * 修改历史
 * 修改历史 Revision 1.23  2009/05/22 03:04:00  lj6061
 * 修改历史 修改节点属性添加优先级
 * 修改历史
 * 修改历史 Revision 1.22  2009/05/20 01:01:33  pht
 * 修改历史 修改菜单放到树上，与树的描述联动。
 * 修改历史
 * 修改历史 Revision 1.21  2009/05/19 11:10:00  pht
 * 修改历史 IED名称改为：name+":"+desc
 * 修改历史
 * 修改历史 Revision 1.20  2009/05/18 06:18:57  cchun
 * 修改历史 Refactor:重构常量定义
 * 修改历史
 * 修改历史 Revision 1.19  2009/05/15 11:50:20  hqh
 * 修改历史 修改对话框
 * 修改历史
 * 修改历史 Revision 1.18  2009/05/15 07:01:29  lj6061
 * 修改历史 删除无用代码
 * 修改历史
 * 修改历史 Revision 1.17  2009/05/13 07:45:25  lj6061
 * 修改历史 注释测试代码
 * 修改历史
 * 修改历史 Revision 1.16  2009/05/13 07:41:03  hqh
 * 修改历史 对实体类操作
 * 修改历史 修改历史 Revision 1.15 2009/05/11 04:59:28
 * hqh 修改历史 修改导入树方法 修改历史 修改历史 Revision 1.14 2009/05/08 04:52:45 hqh 修改历史 修改表模型
 * 修改历史 修改历史 Revision 1.13 2009/05/07 13:02:28 hqh 修改历史 修改节点路径名称 修改历史 修改历史
 * Revision 1.12 2009/05/06 02:24:12 hqh 修改历史 添加子菜单 修改历史 修改历史 Revision 1.11
 * 2009/05/04 12:27:31 hqh 修改历史 添加新建菜单 修改历史 修改历史 Revision 1.10 2009/05/04
 * 01:07:11 hqh 修改历史 修改导入方法 修改历史 Revision 1.9 2009/04/27 07:55:21 lj6061
 * 添加导入ICD文件操作
 * 
 * Revision 1.8 2009/04/23 08:00:15 hqh 代码重构,调整
 * 
 * Revision 1.7 2009/04/22 05:37:45 cchun Update:修改历史信息节点图标
 * 
 * Revision 1.6 2009/04/17 07:16:53 cchun Refactor:清理无效代码
 * 
 * Revision 1.5 2009/04/17 06:07:02 cchun Refactor:重构导航视图
 * 
 * Revision 1.2 2009/03/31 06:17:27 cchun 重构包名称
 * 
 * Revision 1.1 2009/03/31 05:56:30 cchun 添加ui包代码
 * 
 */
public class SecondaryTreeDataFactory {

	private OpenByStringHandler handler = null;
	private List<String> subNets = null;
	private List<IED> ieds = null;
	
	private static SecondaryTreeDataFactory instance = new SecondaryTreeDataFactory();
	
	private SecondaryTreeDataFactory() {
	}
	
	public static SecondaryTreeDataFactory getInstance() {
		return instance;
	}
	
	/**
	 * 创建二次设备菜单项
	 * @return
	 */
	public List<INaviTreeEntry> createSecondaryData() {
		List<INaviTreeEntry> treeData = new ArrayList<INaviTreeEntry>();
		INaviTreeEntry history = new HistoryEntry("历史",
				SCL.XPATH_HISTORY, ImageConstants.HISTORY,
				"历史", UIConstants.HISTORY_EDITOR_ID);
		CommunicationEntry communication = new CommunicationEntry(
				"通信", SCL.XPATH_SUBNETWORK,
				ImageConstants.PARENT, "通信", null);
		DeviceEntry iedRoot = new DeviceEntry("装置",
				SCL.XPATH_IED, ImageConstants.PARENT, "装置", null);
		treeData.add(history);
		treeData.add(communication);
		treeData.add(iedRoot);
		
		this.subNets = null;
		this.ieds = null;
		// 解析SCD
		if (Constants.oldFile != null && !Constants.FINISH_FLAG) {
			ScdSaxParser parser = ScdSaxParser.getInstance();
			subNets = parser.getSubNets();
			ieds = new ArrayList<>();
			for (String[] temp : parser.getIeds()) {
				ieds.add(new IED(temp[0], temp[1], temp[2], temp[3], temp[4]));
			}
		} else {
			// 加载通信子网
			subNets = CommunicationDAO.getSubNets();
			// 加载装置列表
			ieds = IEDDAO.getAllNameDescsByTypeOrder();
		}
		if (subNets != null)
			fillCommTreeItems(communication, subNets);
		if (ieds != null)
			fillIEDTreeItems(iedRoot, ieds);
		
		return treeData;
	}
	
	/**
	 * 按名称过滤
	 * @param data
	 * @param name IED、数据类型名称
	 * @return
	 */
	public static List<INaviTreeEntry> getFiltTreeData(List<INaviTreeEntry> data, String name) {
		String[] names = name.split(",");
		List<INaviTreeEntry> newdata = new ArrayList<INaviTreeEntry>();
		for (INaviTreeEntry group : data) {
			if (group instanceof HistoryEntry ||
					group instanceof CommunicationEntry) {
				newdata.add(group);
			} else {
				ITreeEntry group1 = group.copy();
				newdata.add((INaviTreeEntry)group1);
				for (ITreeEntry item : group.getChildren()) {
					if (ArrayUtil.contains(item.getName(), names)) {
						group1.addChild(item);
					}
				}
			}
		}
		return newdata;
	}
	
	/**
	 * 加载通信配置
	 * @param nodeEntry
	 */
	private void fillCommTreeItems(CommunicationEntry nodeEntry, List<String> names) {
		String nodeXPath = nodeEntry.getXPath();
		String edtID = getEditorByXPath(nodeXPath);
		if (names == null)
			return;
		for (String name : names) {
			INaviTreeEntry subEty = new SubNetEntry(name, nodeXPath
					+ "[@name='" + name + "']", ImageConstants.SUBNETWORK, name, edtID);
			nodeEntry.addChild(subEty);
		}
	}
	
	/**
	 * 加载装置
	 * @param nodeEntry
	 */
	private void fillIEDTreeItems(DeviceEntry nodeEntry, List<IED> ieds) {
		String nodeXPath = nodeEntry.getXPath();
		String edtID = getEditorByXPath(nodeXPath);
		for (IED ied : ieds) {
			INaviTreeEntry subEty = new IEDEntry(ied, edtID);
			nodeEntry.addChild(subEty);
		}
	}
	
	/**
	 * 根据节点xpath获取editor id
	 * @param xpath
	 * @return
	 */
	public String getEditorByXPath(String xpath) {
		if (xpath.equals(SCL.XPATH_SUBNETWORK))
			return UIConstants.COMMUNICATION_EDITOR_ID;
		else
			return UIConstants.IED_CONFIGURE_EDITOR_ID;
	}
}
