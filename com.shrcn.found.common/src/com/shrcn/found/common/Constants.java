/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.common;

import static java.io.File.separator;

import java.io.File;

import org.eclipse.core.runtime.Platform;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-3-3
 */
public class Constants {
	
	private Constants() {}

	public static boolean DEBUG = false;

	public static boolean XQUERY = "xmldb".equals(System.getProperty("parser"));
	
	public static boolean PERMISSION = false;
	
	public static boolean IS_VIEWER = "Viewer".equals(System.getProperty("product"));
	
	public static final String STD_DATE_FORMAT		= "yyyy-MM-dd";
	public static final String STD_TIME_FORMAT		= "yyyy-MM-dd HH:mm:ss";
	public static final String CHINA_DATE_FORMAT	= "yyyy年MM月dd日HH时mm分ss秒";
	
	public static final String COLON  	= ":"; //$NON-NLS-1$
	public static final String DOLLAR 	= "$"; //$NON-NLS-1$
	public static final String DOT 		= "."; //$NON-NLS-1$
	public static final String COMMA	= ","; //$NON-NLS-1$
	public static final String SPACE	= "\t"; //$NON-NLS-1$
	public static final String POUND 	= "#"; //$NON-NLS-1$
	
	public static final String prefix 	= "scl"; 										 //$NON-NLS-1$
	public static String uri 			= "http://www.iec.ch/61850/2003/SCL"; 			 //$NON-NLS-1$
	public static final String schema 	= "http://www.w3.org/2001/XMLSchema-instance"; //$NON-NLS-1$
	
	public static final String SCD_CRC_DESC = "Substation virtual terminal conection CRC";
	public static final String IED_CRC_DESC = "IED virtual terminal conection CRC";

	public static final String NET_PREFIX	= "Gate.LD0.";
	
	/** 程序目录 */
	public static final String EASY50_HOME = System.getProperty("easy50.home");
	public static String usrDir = (EASY50_HOME == null) ? 
			(Platform.isRunning() ? new File(Platform.getInstallLocation().getURL().getFile()).getAbsolutePath() : ".") 
			: EASY50_HOME;
	
	public static final String homeDir  = System.getProperty("user.home");		//$NON-NLS-1$
	public static final String tempDir = usrDir + separator + "temp" + separator;	//$NON-NLS-1$
	public static final String cfgDir  = usrDir + separator + ".config"; //$NON-NLS-1$
	public static final String logDir  = usrDir + separator + "logs"; 		//$NON-NLS-1$
	public static final String seqDir  = usrDir + separator + "seqControl"; 		//$NON-NLS-1$
	public static final String tplDir  = usrDir + separator + "template" + separator; 		//$NON-NLS-1$
	public static final String crc32Dir  = tempDir + "CRC32"; 		//$NON-NLS-1$
	public static final String fpkDir  	 = tempDir + "fpk" + separator; 		//$NON-NLS-1$
	public static final String deviceDir  	 = "/configuration/"; 		//默认装置ftp目录
	public static boolean FINISH_FLAG     	= true;		// 是否初始化完成
	//数据库名称
	public static String CURRENT_PRJ_NAME     	= null;		// 当前配置工具名称
	public static String CURRENT_PRJ_PATH     	= null;		// 当前配置工程路径
	/**
	 * 保存ip，addr信息的文件
	 */
	public static final String DEVIPADDRINFO  	= cfgDir + separator + "devipInfo.xml";
	public static final String lic_file 		= cfgDir + separator + "product.code";
	
	
	/** Schema校验日志文件路径 */
	public static final String schemaLog 		= logDir + separator + "schema.log"; //$NON-NLS-1$
	/** 模型检查日志文件路径 */
	public static final String modelLogPath 	= Constants.logDir + separator + "modelcheck.log"; //$NON-NLS-1$
	
	/** 数据库操作相关常量 */
	public static final String dbDir 			= usrDir + separator + "data"; 	//$NON-NLS-1$
	public static final String dbTmpDir 		= dbDir + separator + "TEMP"; 	//$NON-NLS-1$
	public static final String bakDir 			= usrDir + separator + "bak"; 	//$NON-NLS-1$
	public static final String TARGET_DOC_NAME 	= "SCD"; 						//$NON-NLS-1$
	public static final String dbSuffix 		= ".dat"; 						//$NON-NLS-1$
	public static final String bakSuffix 		= ".bak"; 						//$NON-NLS-1$
	public static final int 	 BAK_MAX 		= 10;

	/** 编码 */
	public static final String CHARSET_UTF8 	= "UTF-8"; //$NON-NLS-1$
	public static final String CHARSET_GB2312 	= "GB2312"; //$NON-NLS-1$
	public static final String CHARSET_GBK		= "GBK"; //$NON-NLS-1$
	/** 国网标准配置文件 */
	public static final String cfgfile_name = "configured";
	public static final String CID = ".cid";
	public static final String CCD = ".ccd";
	/** U21平台研发产物中的icd文件名. */
	public static final String FPK_DEVICE_CID	= "device.cid"; //$NON-NLS-1$
	/** 导入文件过滤名 */
	public static final String FPK_FILENAME 	= "*.fpk"; //$NON-NLS-1$
	public static final String ICD_FILENAME 	= "*.icd"; //$NON-NLS-1$
	public static final String SCD_FILENAME 	= "*.scd"; //$NON-NLS-1$
	public static final String SSD_FILENAME 	= "*.ssd"; //$NON-NLS-1$
	public static final String IID_FILENAME		= "*.iid"; //$NON-NLS-1$
	public static final String SCHEMA_FILENAME 	= "*.xsd"; //$NON-NLS-1$
	public static final String RTU_FILENAME 	= "*.rtu"; //$NON-NLS-1$
	public static final String EXCEL_FILENAME 	= "*.xls"; //$NON-NLS-1$
	public static final String XML_FILENAME 	= "*.xml"; //$NON-NLS-1$
	public static final String RTU_ZIP_FILENAME = "*.data"; //$NON-NLS-1$
	public static final String INCRE_FILENAME 	= ".inc";  //$NON-NLS-1$
	public static final String RCD_FILENAME 	= "*.rcd"; //$NON-NLS-1$
	public static final String[] SCL_FILES 		= new String[] {ICD_FILENAME + "; " + SCD_FILENAME + "; " + SSD_FILENAME
	 										+ "; " + IID_FILENAME, "*.xml; *.xsd", "*.*"};
	public static final String[] ICD_FILES		= new String[] {ICD_FILENAME + "; *.ICD;" + FPK_FILENAME + "; *.FPK"};
	public static final String[] IED_REPLACE_FILES		= new String[] {ICD_FILENAME + "; *.ICD;" + IID_FILENAME + "; *.IID;" + FPK_FILENAME + "; *.FPK"};
	public static final String SCD_FILES		= SCD_FILENAME + "; *.SCD";
	public static final String EXCEL_FILES		= EXCEL_FILENAME + "; *.XLS";
	
	/** 导入文件类型提示信息 */
	public static final String SCD_FILE_IMPORT = "Substation Configuration Description(*.scd)"; //$NON-NLS-1$
	public static final String ICD_FILE_IMPORT = "IED Capability Description(*.icd)"; 			//$NON-NLS-1$
	public static final String SSD_FILE_IMPORT = "System Specification Description(*.ssd)"; 	//$NON-NLS-1$
	public static final String IID_FILE_IMPORT = "Instantiated IED Description(*.iid)"; 		//$NON-NLS-1$

	// DEMO版使用的临时常量-----------------------------------------------
	public static String oldFile 				= null;
	public static String DEFAULT_PRJECT_NAME  	= null;		// 工程名
	public static String DEFAULT_SCD_DOC_NAME 	= "SCD";	// 保存.scd文件的doc名称 //$NON-NLS-1$
	public static String DEFAULT_PRJ_NAME  	    = null;		// 工程名（仅用于工程名称，与SCD文件名称无关）
	public static String CURRENT_RTU_NAME     	= null;		// 当前RTU名称
	public static String verName 				= "版本";
	public static boolean HisVer 				= false;//是否打开的是历史版本
	public static String verFile 				= "ver.xml";//版本管理文件
	public static String verPath 				= usrDir + separator +"versions";//版本管理文件目录
	public static String mainFile 				= null;//打开历史版本时，记录其主文件名，只在版本管理程序赋值
	public static final String[] SCD_ATTS 		= new String[] {"prefix", "lnClass", "lnInst", "doName", "daName", "lnType", "ldInst", "fc"};
	/** 默认库中doc名 */
	public static  String DEFAULT_SSD_DOC_NAME = "SSD"; //$NON-NLS-1$

	/** 选项配置文件名 */
	public static final String PROPERTIES_FILE 		 = cfgDir + separator + "sct.properties"; //$NON-NLS-1$
	
	/**=============== 一次接线图模板常量信息 ================*/
	/** 程序图片目录 */
	public static final String IMAGES_DIR 			 = cfgDir + separator + "images";
	/** 树形图标目录 */
	public static final String ICONS_DIR 			 = cfgDir + separator + "icons";
	/** 模板目录 */
	public static final String TEMPLATES_DIR 		 = cfgDir + separator + "templates";
	/** 图形文件目录 */
	public static final String GRAPH_DIR 		     = cfgDir + separator + "graph";
	/** 选项板配置文件名 */
	public static final String PALETTE_CONFIG_FILE 	 = cfgDir + separator + "Palette.properties";
	/** 模板文件后缀 */
	public static final String SUFFIX_TEMPLATES 	= ".eqp";
	/** 图形文件后缀 */
	public static final String SUFFIX_GRAPH 		= ".graph";
	/** 图标文件后缀 */
	public static final String SUFFIX_PNG		    = ".png";

	public static final String SUFFIX_GIF		    = ".gif";
	/** 典型间隔配置目录 */
	public static final String EXPORT_BAY_DIR 		= cfgDir + separator+ "bay"; 	//$NON-NLS-1$
	/** 典型间隔模型文件后缀 */
	public static final String SUFFIX_BAY 			= ".bay";					//$NON-NLS-1$
	/** FPK文件常量 */
	public static final String FPK_VERSION			= "00000000";
	public static final String FPK					= ".fpk";
	public static final String[] FPK_EXT 			= { "*.fpk" };
	public static final String[] FPK_EXTNAME 		= { "fpk文件" };
	
	// 开位状态
	public static final String STATUS_OPEN = "00";
	// 合位状态
	public static final String STATUS_CLOSE = "01";
	
	/**
	 * 获取主接线图形文件路径
	 * @return
	 */
	public static String getDefaultGraphPath() {
		File grpDir = new File(Constants.GRAPH_DIR);
    	if(!grpDir.exists()){
    		grpDir.mkdirs();
    	}
		return Constants.GRAPH_DIR + File.separator + 
			Constants.DEFAULT_PRJECT_NAME + 
			Constants.SUFFIX_GRAPH;
	}
	
	/**
	 * 获取模板文件路径
	 * @param name
	 * @return
	 */
	public static String getTemplatePath(String name) {
		return TEMPLATES_DIR + separator + name + SUFFIX_TEMPLATES;
	}
	
	/**
	 * 获取图形文件路径
	 * @param name
	 * @return
	 */
	public static String getGraphPath(String name) {
		return GRAPH_DIR + separator + name + SUFFIX_GRAPH;
	}
	
	/**
	 * 获取工具按钮图标文件路径
	 * @param name
	 * @return
	 */
	public static String getToolIconPath(String name) {
		String iconName = "create" + name + SUFFIX_PNG;
		return IMAGES_DIR + separator + iconName;
	}
	
	/**
	 * 获取树控件图标文件路径
	 * @param name
	 * @return
	 */
	public static String getTreeIconPath(String name) {
		return ICONS_DIR + separator + name + SUFFIX_GIF;
	}

	public static String getCloseTid(String tid) {
		return tid.substring(0, 2) + Constants.STATUS_CLOSE;
	}
	
	public static String getVerFile(String fileName){
    	File file = new File(fileName);
    	String path = getVerPath(fileName);
    	return path+separator+file.getName()+"_"+Constants.verFile;
	}
	public static String getVerPath(String fileName){
		createDir(verPath);
		File file = new File(fileName);
		createDir(verPath+separator+file.getName());
		return verPath+separator+file.getName();
	}
	private static void createDir(String path) {
    	File file = new File(path);
        
        if (!file.exists()) {
            file.mkdirs();
        }
    }
	
}


