package com.shrcn.business.scl.das;

import java.util.List;

import org.dom4j.Element;

public interface GetBlockInfo {
	//重新加载数据
	public void reLoad();
	//得到节点信息，为数据库中的节点
    public List<Element> getEleData();
    
    public void update(int row, String name ,String oldValue, String value, boolean isMust);
   
}
