/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.enums;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-16
 */
/*
 * 修改历史 $Log: EnumDAType.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:36:40  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.1  2009/04/16 07:09:48  cchun
 * 修改历史 Add:添加枚举类型
 * 修改历史
 */
public class EnumDAType {
	
	public static final String[] tPredefinedPTypeEnum = { "IP", "IP-SUBNET",
			"IP-GATEWAY", "OSI-NSAP", "OSI-TSEL", "OSI-SSEL", "OSI-PSEL",
			"OSI-AP-Title", "OSI-AP-Invoke", "OSI-AE-Qualifier",
			"OSI-AE-Invoke", "MAC-Address", "APPID", "VLAN-PRIORITY", "VLAN-ID" };

	public static final String[] tPredefinedAttributeNameEnum = { "T", "Test",
			"Check", "SIUnit" };
	public static final String[] tPredefinedCommonConductingEquipmentEnum = { "CBR",
			"DIS", "VTR", "CTR", "GEN", "CAP", "REA", "CON", "MOT", "EFN",
			"PSH", "BAT", "BSH", "CAB", "GIL", "LIN", "RRC", "SAR", "TCF",
			"TCR", "IFL" };
	public static final String tPowerTransformerEnum = "PTR";
	public static final String tTransformerWindingEnum = "PTW";
	public static final String[] tPredefinedGeneralEquipmentEnum = { "AXN", "BAT",
			"MOT" };

	public static final String[] tServiceSettingEnum = { "Dyn", "Conf", "Fix" };
	public static final String[] tPhaseEnum = { "A", "B", "C", "N", "all", "none" };

	public static final String[] tAuthenticationEnum = { "none", "password", "week",
			"strong", "certificate" };

	public static final String[] tAssociationKindEnum = { "pre-established",
			"predefined" };

	public static final String tLLN0Enum = "LLN0";

	public static final String tLPHDEnum = "LPHD";

	public static final String[] tDomainLNGroupAEnum = { "ANCR", "ARCO", "ATCC",
			"AVCO" };

	public static final String[] tDomainLNGroupCEnum = { "CILO", "CSWI", "CALH",
			"CCGR", "CPOW" };

	public static final String[] tDomainLNGroupGEnum = { "GGIO", "GSAL" };

	public static final String[] tDomainLNGroupIEnum = { "IHMI", "IARC", "ITCI",
			"ITMI" };

	public static final String[] tDomainLNGroupMEnum = { "MMXU", "MDIF", "MHAI",
			"MHAN", "MMTR", "MMXN", "MSQI", "MSTA" };

	public static final String[] tDomainLNGroupPEnum = { "PDIF", "PDIS", "PDIR",
			"PDOP", "PDUP", "PFRC", "PHAR", "PHIZ", "PIOC", "PMRI", "PMSS",
			"POPF", "PPAM", "PSCH", "PSDE", "PTEF", "PTOC", "PTOF", "PTOV",
			"PTRC", "PTTR", "PTUC", "PTUV", "PUPF", "PTUF", "PVOC", "PVPH",
			"PZSU" };

	public static final String[] tDomainLNGroupREnum = { "RSYN", "RDRE", "RADR",
			"RBDR", "RDRS", "RBRF", "RDIR", "RFLO", "RPSB", "RREC" };

	public static final String[] tDomainLNGroupSEnum = { "SARC", "SIMG", "SIML",
			"SPDC" };

	public static final String[] tDomainLNGroupTEnum = { "TCTR", "TVTR" };
	public static final String[] tDomainLNGroupXEnum = { "XCBR", "XSWI" };
	public static final String[] tDomainLNGroupYEnum = { "YPTR", "YEFN", "YLTC",
			"YPSH" };

	public static final String[] tDomainLNGroupZEnum = { "ZAXN", "ZBAT", "ZBSH",
			"ZCAB", "ZCAP", "ZCON", "ZGEN", "ZGIL", "ZLIN", "ZMOT", "ZREA",
			"ZRRC", "ZSAR", "ZTCF", "ZTCR" };

	public static final String[] tDomainLNEnum = { "ANCR", "ARCO", "ATCC", "AVCO",
			"CILO", "CSWI", "CALH", "CCGR", "CPOW", "GGIO", "GSAL", "IHMI",
			"IARC", "ITCI", "ITMI", "MMXU", "MDIF", "MHAI", "MHAN", "MMTR",
			"MMXN", "MSQI", "MSTA", "PDIF", "PDIS", "PDIR", "PDOP", "PDUP",
			"PFRC", "PHAR", "PHIZ", "PIOC", "PMRI", "PMSS", "POPF", "PPAM",
			"PSCH", "PSDE", "PTEF", "PTOC", "PTOF", "PTOV", "PTRC", "PTTR",
			"PTUC", "PTUV", "PUPF", "PTUF", "PVOC", "PVPH", "PZSU", "RSYN",
			"RDRE", "RADR", "RBDR", "RDRS", "RBRF", "RDIR", "RFLO", "RPSB",
			"RREC", "SARC", "SIMG", "SIML", "SPDC", "TCTR", "TVTR", "XCBR",
			"XSWI", "YPTR", "YEFN", "YLTC", "YPSH", "ZAXN", "ZBAT", "ZBSH",
			"ZCAB", "ZCAP", "ZCON", "ZGEN", "ZGIL", "ZLIN", "ZMOT", "ZREA",
			"ZRRC", "ZSAR", "ZTCF", "ZTCR" };

	public static final String[] tPredefinedLNClassEnum = { "LPHD", "LLN0", "ANCR",
			"ARCO", "ATCC", "AVCO", "CILO", "CSWI", "CALH", "CCGR", "CPOW",
			"GGIO", "GSAL", "IHMI", "IARC", "ITCI", "ITMI", "MMXU", "MDIF",
			"MHAI", "MHAN", "MMTR", "MMXN", "MSQI", "MSTA", "PDIF", "PDIS",
			"PDIR", "PDOP", "PDUP", "PFRC", "PHAR", "PHIZ", "PIOC", "PMRI",
			"PMSS", "POPF", "PPAM", "PSCH", "PSDE", "PTEF", "PTOC", "PTOF",
			"PTOV", "PTRC", "PTTR", "PTUC", "PTUV", "PUPF", "PTUF", "PVOC",
			"PVPH", "PZSU", "RSYN", "RDRE", "RADR", "RBDR", "RDRS", "RBRF",
			"RDIR", "RFLO", "RPSB", "RREC", "SARC", "SIMG", "SIML", "SPDC",
			"TCTR", "TVTR", "XCBR", "XSWI", "YPTR", "YEFN", "YLTC", "YPSH",
			"ZAXN", "ZBAT", "ZBSH", "ZCAB", "ZCAP", "ZCON", "ZGEN", "ZGIL",
			"ZLIN", "ZMOT", "ZREA", "ZRRC", "ZSAR", "ZTCF", "ZTCR" };

	public static final String[] tLNClassEnum = tPredefinedLNClassEnum;

	public static final String[] tPredefinedCDCEnum = tLNClassEnum;

	public static final String[] tCDCEnum = tPredefinedCDCEnum;

	public static final String[] tTrgOptEnum = { "dchg", "qchg", "dupd", "none" };
	public static final String[] tTrgOptControlEnum = { "dchg", "qchg", "dupd",
			"period", "none" };
	public static final String[] tFCEnum = { "ST", "MX", "CO", "SP", "SG", "SE",
			"SV", "CF", "DC", "EX" };

	public static final String[] tPredefinedBasicTypeEnum = { "BOOLEAN", "INT8",
			"INT16", "INT24", "INT32", "INT128", "INT8U", "INT16U", "INT24U",
			"INT32U", "FLOAT32", "FLOAT64", "Enum", "Dbpos", "Tcmd", "Quality",
			"Timestamp", "VisString32", "VisString64", "VisString255",
			"Octet64", "Struct", "EntryTime", "Unicode255", "Check" };

	public static final String[] tBasicTypeEnum = tPredefinedBasicTypeEnum;

	public static final String[] tValKindEnum = { "Spec", "Conf", "RO", "Set" };

	public static final String[] tGSEControlTypeEnum = { "GSSE", "GOOSE" };

	public static final String[] tSIUnitEnum = { "none", "m", "kg", "s", "A", "K",
			"mol", "cd", "deg", "rad", "sr", "Gy", "q", "℃", "Sv", "F", "C",
			"S", "H", "V", "ohm", "J", "N", "Hz", "lx", "Lm", "Wb", "T", "W",
			"Pa", "m^2", "m^3", "m/s", "m/s^2", "m^3/s", "m/m^3", "M",
			"kg/m^3", "m^2/s", "W/m K", "J/K", "ppm", "s^-1", "rad/s", "VA",
			"VAr", "theta", "cos_theta", "Vs", "V^2", "As", "A^2", "A^2 s",
			"VAh", "Wh", "VArh", "V/Hz", "b/s" };
	public static final String[] tUnitMultiplierEnum = { "", "m", "k", "M", "mu",
			"y", ",z", ",a", ",f", ",p", "n", ",c", ",d", ",da", "h", "G", "T",
			"P", "E", "Z", "Y" };
}
