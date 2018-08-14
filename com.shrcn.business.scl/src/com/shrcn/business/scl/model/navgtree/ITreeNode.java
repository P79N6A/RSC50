/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model.navgtree;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-4
 */
/**
 * $Log: ITreeNode.java,v $
 * Revision 1.1  2013/03/29 09:35:22  cchun
 * Add:创建
 *
 * Revision 1.2  2011/03/11 01:20:23  cchun
 * Update:添加id属性
 *
 * Revision 1.1  2010/03/09 08:43:04  cchun
 * Add:通用树节点对象
 *
 */
public interface ITreeNode extends Serializable {
	
	/**
	 * 得到树结点的名称
	 * @return
	 */
	public String getId();
	
	/**
	 * 设置树结点的名称
	 * @return
	 */
	public void setId(String id);
	
	/**
	 * 得到树结点的名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 设置树结点的名称
	 * @return
	 */
	public void setName(String name);
	
	/**
	 * 得到图片地址
	 * @return
	 */
	public String getIconName();
	
	/**
	 * 得到树结点的提示信息
	 * @return
	 */
	public String getToolTip();
	
	/**
	 * 取得子节点
	 * @return
	 */
	public List<ITreeNode> getChildren();
	
	/**
	 * 设置子节点
	 * @return
	 */
	public void setChildren(List<ITreeNode> children);
	
	/**
	 * 添加子节点
	 * @param child
	 */
	public void addChild(ITreeNode child);
	
	/**
	 * 删除子节点
	 * @param child
	 */
	public void removeChild(ITreeNode child);

	/**
	 * 取得父节点
	 * @return
	 */
	public ITreeNode getParent();
	
	/**
	 * 设置父节点
	 * @param parent 父节点
	 */
	public void setParent(ITreeNode parent);
	
	public boolean isRoot();

	public boolean isLeaf();
}
