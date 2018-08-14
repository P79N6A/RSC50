/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.common;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-8-24
 */
/**
 * $Log: EnumEquipType.java,v $
 * Revision 1.1  2013/03/29 09:36:56  cchun
 * Add:创建
 *
 * Revision 1.15  2011/07/20 05:52:41  cchun
 * Update:修改getModelType()避免null异常
 *
 * Revision 1.14  2011/07/11 08:51:08  cchun
 * Update:增加getModelType(),hasGrounded(),isCVTR()
 *
 * Revision 1.13  2010/12/06 05:06:40  cchun
 * Update:添加isGround()
 *
 * Revision 1.12  2010/10/08 03:24:13  cchun
 * Update:增加接地图元
 *
 * Revision 1.11  2010/09/17 06:03:00  cchun
 * Update:添加isTransformer()
 *
 * Revision 1.10  2010/08/10 08:02:09  cchun
 * Update:增加AXN
 *
 * Revision 1.9  2010/06/25 09:04:26  cchun
 * Update:修改格式
 *
 * Revision 1.8  2010/02/01 03:11:26  hqh
 * 增加默认拓扑变量
 *
 * Revision 1.7  2009/10/21 11:22:47  lj6061
 * 提取插入电压等级和子设备
 *
 * Revision 1.6  2009/10/21 03:11:07  cchun
 * Update:将"功能图元"代码中的字符串常量改用静态变量代替
 *
 * Revision 1.5  2009/10/12 02:16:07  cchun
 * Update:增加功能、子功能id
 *
 * Revision 1.4  2009/09/10 11:32:57  lj6061
 * 修改选中属性窗口显示节点类型
 *
 * Revision 1.3  2009/09/03 03:04:59  cchun
 * Update:增加母线间隔功能
 *
 * Revision 1.2  2009/08/28 07:41:52  cchun
 * Update:整理变量顺序，使之和菜单顺序一致
 *
 * Revision 1.1  2009/08/27 02:35:39  lj6061
 * 修改设备类型文件位置
 *
 * Revision 1.1  2009/08/26 09:32:37  cchun
 * Add:设备类型常量
 *
 */
public class EnumEquipType {

	private EnumEquipType() {}
	
	public static final String ANCHOR_NULL 	= "null";
	public static final String BAY 			= "BAY";
	public static final String VIRTUALBAY 	= "VirtualBAY";
	public static final String FUNLIST 		= "FunctionList";
	public static final String FUNCTION 	= "Function";
	public static final String SUBFUNCTION 	= "SubFunction";
	public static final String VOLAGELEVEL 	= "VoltageLevel";
	public static final String SUBEQUIPMENT = "SubEquipment";

	public static final String GROUNDED = "GROUNDED";

	public static final String PTR  = "PTR";
	public static final String PTR2 = "PTR2";
	public static final String PTR3 = "PTR3";
	
	public static final String DIS 	= "DIS";
	public static final String DDIS = "DDIS";
	public static final String CBR 	= "CBR";
	public static final String VTR 	= "VTR";
	public static final String CTR 	= "CTR";
	
	public static final String CAP 	= "CAP";
	public static final String REA 	= "REA";
	public static final String EFN 	= "EFN";
	public static final String SAR 	= "SAR";
	public static final String PSH 	= "PSH";

	public static final String BAT 	= "BAT";
	public static final String RRC 	= "RRC";
	public static final String TCF 	= "TCF";
	public static final String TCR 	= "TCR";

	public static final String BSH 	= "BSH";
	public static final String CAB 	= "CAB";
	public static final String GIL 	= "GIL";
	public static final String LIN 	= "LIN";
	public static final String IFL 	= "IFL";

	public static final String GEN 	= "GEN";
	public static final String MOT 	= "MOT";
	public static final String AXN 	= "AXN";
	public static final String BCAP = "BCAP";
	
	
	
	/**
	 * 根据图形类型得到模型类型
	 * @param type
	 * @return
	 */
	public static String getModelType(String type) {
		if (PTR3.equals(type)|| PTR2.equals(type))
			return PTR;
		if (DDIS.equals(type))
			return DIS;
		return type;
	}
	
	/**
	 * 判断当前类型是否为变压器
	 * @param type
	 * @return
	 */
	public static boolean isTransformer(String type) {
		return PTR.equals(getModelType(type));
	}
	
	/**
	 * 判断当前类型是否为电流/电压互感器
	 * @param type
	 * @return
	 */
	public static boolean isCVTR(String type) {
		return CTR.equals(type) || VTR.equals(type);
	}
	
	/**
	 * 判断当前设备是否有一端接地
	 * @param type
	 * @return
	 */
	public static boolean hasGrounded(String type) {
		return DDIS.equals(type) || SAR.equals(type);
	}
	
	/**
	 * 判断当前类型是否为接地
	 * @param type
	 * @return
	 */
	public static boolean isGround(String type) {
		return GROUNDED.equals(type);
	}
}
