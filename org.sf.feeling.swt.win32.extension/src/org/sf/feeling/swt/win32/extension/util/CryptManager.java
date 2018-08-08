/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package org.sf.feeling.swt.win32.extension.util;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.sf.feeling.swt.win32.extension.io.FileSystem;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-3-6
 */
/**
 * $Log: CryptManager.java,v $
 * Revision 1.7  2014/03/05 07:19:50  cchun
 * Fix Bug:修复序列号不完整错误
 *
 * Revision 1.6  2013/12/30 09:01:14  cchun
 * Fix Bug:修复磁盘获取有可能失败的bug
 *
 * Revision 1.5  2013/12/05 10:13:11  cchun
 * Update:增加isValid()，消除激活异常
 *
 * Revision 1.4  2013/12/04 09:16:33  cchun
 * Fix Bug:修复null异常
 *
 * Revision 1.3  2013/11/06 09:38:42  zq
 * 修改硬盘号+硬盘签名作为注册码
 *
 * Revision 1.2  2013/03/08 06:20:23  cchun
 * Update:去掉序列号中MAC信息
 *
 * Revision 1.1  2013/03/07 06:45:25  cchun
 * Add:加密管理类
 *
 */
public class CryptManager {
	
	private static final String key = "yyyy-MM-dd hh:mm:ss";
	private static final String path = "C:\\";
	
	/**
	 * 检查license合法性。
	 * @param codeContent
	 * @return
	 */
	public static boolean checkLicense(String codeContent) {
		if (codeContent==null || "".equals(codeContent))
			return false;
		return getSerialNo().equals(deActivateCode(codeContent));
	}
	
	public static boolean isValid(String activeCode) {
		return activeCode.matches("[\\dA-Fa-f]+")		// "十六进制数"
				&& activeCode.length() % 16 == 0;
	}
	
	/**
	 * 检查license。
	 * @return
	 */
	public static boolean checkProduct(String lic_file) {
		File codefile = new File(lic_file);
		boolean check = false;
		if (codefile.exists() && codefile.length() > 10) {
			String codeContent = FileManager.readTextFile(lic_file);
			if (isValid(codeContent))
				check = checkLicense(codeContent);
		}
		if (!check) {
			LicenseDialog dlg = new LicenseDialog(Display.getDefault().getActiveShell(), lic_file);
			if (dlg.open() == IDialogConstants.OK_ID) {
				check = true;
			}
		}
		return check;
	}
	
	
//	public static String readTextFile(String filePath) {
//		String text = null;
//		InputStream is = null;
//		try {
//			is = new FileInputStream(filePath);
//			int size = is.available();
//			byte[] data = new byte[size];
//			is.read(data, 0, size);
//			text = new String(data);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (is != null)
//					is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return text;
//	}
	
	private static String getVolumeType(int diskType)
	{
		switch (diskType)
		{
		case FileSystem.DRIVE_TYPE_CDROM:
			return "CD-ROM";
		case FileSystem.DRIVE_TYPE_FIXED:
			return "Local Disk";
		case FileSystem.DRIVE_TYPE_RAMDISK:
			return "RAM Disk";
		case FileSystem.DRIVE_TYPE_REMOTE:
			return "Remote Drive";
		case FileSystem.DRIVE_TYPE_REMOVABLE:
			return "Removable Drive";
		default:
			return "Unknown";
		}
	}
	
	/**
	 * 获取序列号。
	 * @return
	 */
	public static String getSerialNo() {
		String volumeType = getVolumeType(FileSystem.getDriveType(path));
		String volumeSerialNo = FileSystem.getVolumeSerialNumber(path);
		String sbinfo = FileManager.getCRC32(volumeType) + FileManager.getCRC32(volumeSerialNo);
		int length = sbinfo.length();
		
		sbinfo = new StringBuilder(sbinfo).reverse().toString();
		StringBuilder sbSerial = new StringBuilder("ABCD-");
		int segs = length / 4;
		for (int i=0; i<segs; i++) {
			sbSerial.append(sbinfo.substring(i*4 + 0, i*4 + 4));
			if (i < segs-1)
				sbSerial.append("-");
		}
		if (segs * 4 < length) {
			sbSerial.append("-");
			sbSerial.append(sbinfo.substring(segs*4 + 0, length));
		}
		int mode = length % 4;
		if (mode > 0) {
			for (int i=0; i<(4-mode); i++) {
				sbSerial.append("0");
			}
		}
		return sbSerial.toString();
	}
	
	/**
	 * 根据激活码获取序列号。
	 * @param activateCode
	 * @return
	 */
	public static String deActivateCode(String activateCode) {
		String serialNo = CryptUtil.getInstance().decryptAES(activateCode, key);
		if (serialNo!=null && serialNo.length()>=key.length())
			return serialNo.substring(key.length());
		return "";
	}
	
}
