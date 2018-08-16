/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.io.scd;

import java.io.File;

public interface ITagSG {

	String sep = File.separator;
	
	String REF_SEP 				= "$";
	
	String EL_ROOT				= "SG";
	String ATTR_VERSION			= "version";
	String ATTR_CRC				= "crc";
	
	String EL_VOI_CRC			= "VIO_CRC";
	String ATTR_ICD_CRC			= "icdVOICRC";
	String ATTR_SCD_CRC			= "scdVOICRC";

	String ATTR_NET_MASK		= "netMask";
	String ATTR_NET_B			= "netBMask";
	String ATTR_NET_SG			= "netSgMask";
	String ATTR_NET_SG_B		= "netSgBMask";
	
	String EL_GSE_OUT			= "GOOSE_OUT";
	String EL_CB						= "cb";
	String ATTR_CB_REF			= "cbRef";
	String ATTR_CB_ID			= "cbId";
	String ATTR_DS_REF			= "dsRef";
	String ATTR_CONF_REV		= "confRev";
	String ATTR_ENTRY_NUM		= "entryNum";
	String ATTR_MAX_SPACE		= "T0";
	String ATTR_MIN_SPACE		= "T1";
	
	String EL_ADDR				= "addr";
	String ATTR_APPID			= "appID";
	String ATTR_MAC				= "mac";
	String ATTR_VLANID			= "vlanID";
	String ATTR_PRIORITY		= "priority";
	
	String EL_DS						= "ds";
	String EL_FCDA				= "fcda";
	String ATTR_REF				= "ref";
	String ATTR_IDX				= "idx";
	String ATTR_TYPE				= "typ";
	String ATTR_TM_IDX			= "tmIdx";
	String ATTR_DB				= "db";
	String ATTR_ZERO_DB		= "zeroDb";
	String EL_ITEM					= "item";
	String ATTR_ADDR			= "addr";
	String ATTR_ADDR_DBP		= "addrDbp";
	String ATTR_STR_TAG			= "strTag";
	
	String EL_GSE_IN					= "GOOSE_IN";
	String ATTR_RFL_ADDR			= "rflAddr";
	String ATTR_RFL_ADDR_DBP	= "rflAddrDbp";
	
	String EL_SV_OUT				= "SV_OUT";
	String ATTR_ASDU_NUM		= "asduNum";
	String ATTR_SMP_TYPE			= "smpTyp";
	String ATTR_SMP_RATE		= "smpRate";
	String ATTR_FT3_RATE			= "ft3Rate";
	String ATTR_FT3_LDNAM		= "ft3LDNam";
	String ATTR_POLE					= "pole";
	
	String EL_SV_IN				= "SV_IN";
	String ATTR_SCALE			= "scale";
	String ATTR						= "attr";
	String ATTR_DCHL_IDX		= "dchlIdx";
	String EL_ADC					= "adc";
	String EL_CHL					= "chl";
	String ATTR_IN_ADDR			= "inAddr";
	String ATTR_OUT_ADDR		= "outAddr";
	String EL_LNK					= "lnk";
	String EL_VOLSW				= "volSW";
	String EL_BUS					= "bus";
	String ATTR_NAME			= "name";
	String ATTR_IN_IDX			= "in_idx";
	String ATTR_IN_CHL			= "in_chl";
	String ATTR_OUT_CHL			= "out_chl";
	String EL_UPARA					= "upara";
	String ATTR_CB1_IDX			= "cb1_idx";
	String ATTR_BUS1_ID			= "bus1_id"; 
	String ATTR_BUS1_A			= "bus1_a"; 
	String ATTR_BUS1_B			= "bus1_b";
	String ATTR_BUS1_C			= "bus1_c";
	String ATTR_CB2_IDX			= "cb2_idx";
	String ATTR_BUS2_ID			= "bus2_id";
	String ATTR_BUS2_A			= "bus2_a";
	String ATTR_BUS2_B			= "bus2_b";
	String ATTR_BUS2_C			= "bus2_c";
	String EL_USWITCH			= "uswitch";
	
	String EL_IEEE1588			= "IEEE1588";
	String ATTR_MOD			= "mod";
	
	String EL_GMRP				= "GMRP";
	String EL_MULTICAST		= "multicast";
	
	String FC_BRCB 		= "BR";
	String FC_URCB 		= "RP";
}
