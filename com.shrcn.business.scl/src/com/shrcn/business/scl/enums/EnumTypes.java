/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.enums;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-8
 */
/*
 * 修改历史
 * $Log: EnumTypes.java,v $
 * Revision 1.1  2013/03/29 09:36:37  cchun
 * Add:创建
 *
 * Revision 1.1  2009/05/18 05:57:50  cchun
 * Add:常用枚举类型
 *
 */
public interface EnumTypes {
	public static final int ENUM_TYPE_LN                 = 1;
	public static final int ENUM_TYPE_PREDEFINEDP        = 2;
	public static final int ENUM_TYPE_PREDEFINEDATTI     = 3;
	public static final int ENUM_TYPE_PREDEFINEDCCE      = 4;
	public static final int ENUM_TYPE_POWERTRANSFORMER   = 5;
	public static final int ENUM_TYPE_TRANSFORMERWINDING = 6;
	public static final int ENUM_TYPE_PREDEFINEDGE       = 7;
	public static final int ENUM_TYPE_SERVICESETTING     = 8;
	public static final int ENUM_TYPE_PHASE              = 9;
	public static final int ENUM_TYPE_AUTHENTICATION     = 10;
	public static final int ENUM_TYPE_ASSOCKIND          = 11;
	public static final int ENUM_TYPE_PREDEFINEDCDC      = 12;
	public static final int ENUM_TYPE_TRGOPT             = 13;
	public static final int ENUM_TYPE_TRGOPTCONTROL      = 14;
	public static final int ENUM_TYPE_FC                 = 15;
	public static final int ENUM_TYPE_PREDEFINEDBTYPE    = 16;
	public static final int ENUM_TYPE_VALKIND            = 17;
	public static final int ENUM_TYPE_GSECONTROLTYPE     = 18;
	public static final int ENUM_TYPE_SIUNIT             = 19;
	public static final int ENUM_TYPE_UNITMULTIPLIER     = 20;
	public static final int ENUM_TYPE_BOOLTYPE           = 21;
	public static final int ENUM_TYPE_HEADERNS           = 22;
	
	public static final String szNameStru[] = {
        "FuncName",
		"IEDName",
	};
	public static final int HEADER_NS = szNameStru.length; //2
	
	public static final String szLNEnum[] = {
        "ANCR",
		"ARCO",
		"ATCC",
		"AVCO",
		"CALH",
		"CCGR",
		"CILO",
		"CPOW",
        "CSWI",
		"GAPC",
		"GGIO",
		"GSAL",
		"IARC",
		"IHMI",
		"ITCI",
		"ITMI",
		"LPHD",
		"LLN0",
		"MDIF",
		"MHAI",
		"MHAN",
		"MMTR",
		"MMXN",
		"MMXU",
		"MSQI",
		"MSTA",
		"PDIF",
		"PDIS",
		"PDIR",
		"PDOP",
		"PDUP",
		"PFRC",
		"PHAR",
		"PHIZ",
		"PIOC",
		"PMRI",
		"PMSS",
		"POPF",
		"PPAM",
		"PSCH",
		"PSDE",
		"PTEF",
		"PTOC",
		"PTOF",
		"PTOV",
		"PTRC",
		"PTTR",
		"PTUC",
		"PTUV",
		"PUPF",
		"PTUF",
		"PVOC",
		"PVPH",
		"PZSU",
		"RDRE",
		"RADR",
		"RBDR",
		"RDRS",
		"RBRF",
		"RDIR",
		"RFLO",
		"RPSB",
		"RREC",
		"RSYN",
		"SARC",
		"SIMG",
		"SIML",
		"SPDC",
		"TCTR",
		"TVTR",
		"XCBR",
		"XSWI",
		"YEFN",
		"YLTC",
		"YPSH",
		"YPTR",
		"ZAXN",
		"ZBAT",
		"ZBSH",
		"ZCAB",
		"ZCAP",
		"ZCON",
		"ZGEN",
		"ZGIL",
		"ZLIN",
		"ZMOT",
		"ZREA",
		"ZRRC",
		"ZSAR",
		"ZTCF",
		"ZTCR",
	};
	public static final int LN_ENUM = szLNEnum.length; //91
	
	public static final String szPredefinedPTypeEnum[] = {
		"IP",
		"IP-SUBNET",
		"IP-GATEWAY",
		"OSI-NSAP",
		"OSI-TSEL",
		"OSI-SSEL",
		"OSI-PSEL",
		"OSI-AP-Title",
		"OSI-AP-Invoke",
		"OSI-AE-Qualifier",
		"OSI-AE-Invoke",
		"MAC-Address",
		"APPID",
		"VLAN-PRIORITY",
		"VLAN-ID",
	};
	public static final int PREDEFINEDPTYPE_ENUM =szPredefinedPTypeEnum.length; //15
}
