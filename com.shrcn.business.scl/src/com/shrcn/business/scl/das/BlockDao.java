package com.shrcn.business.scl.das;

import java.util.List;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.xmldb.XMLDBHelper;

public class BlockDao extends IEDEditDAO implements GetBlockInfo{

	public BlockDao(String iedName, String ldInst) {
		super(iedName, ldInst);
	}
    

	//得到控制块的信息列表
	@Override
	public List<Element> getEleData() {
		return null;
	}

	//控制块刷新
	@Override
	public void reLoad() {
	}

	/**
	 * 更新控制块信息
	 * row 行号
	 * name 属性名称
	 * value 更新的值
	 * isMust value是否可以为空
	 */
	@Override
	public void update(int row, String name, String oldValue, String value, boolean isMust) {
	}

	/**
	 * 得到控制块参引
	 * @param fc
	 * @param cbName
	 * @return
	 */
	public String getCbRef(String fc, String cbName) {
		return iedName + ldInst + "/LLN0$" + fc + "$" + cbName;
	}

	public static void updateConfRev(String xpath) {
		String confRev = XMLDBHelper.getAttributeValue(xpath + "/@confRev");
		if (!StringUtil.isEmpty(confRev) && StringUtil.isNumeric(confRev)) {
			confRev = (Integer.parseInt(confRev) + 1) + "";
		} else {
			confRev = "1";
		}
		XMLDBHelper.saveAttribute(xpath, "confRev", confRev);
	}
}
