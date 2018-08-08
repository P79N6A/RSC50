/*
 * @(#) FileItem.java
 *
 * Copyright (c) 2007 - 2013 上海思源弘瑞电力自动化有限公司.
 * All rights reserved. 基于 Eclipse E4 a next generation 
 * platform (e.g., the CSS styling, dependency injection, Modeled UI) 
 * Rich Client Application 开发的U21继电保护装置平台工具软件. 
 * 此系列工具软件包括装置调试下载工具、装置配置工具. 103通信调试工具、
 * 自动化装置测试工具。
 */
package com.shrcn.found.file.fpk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.shrcn.found.common.util.ByteUtil;
import com.shrcn.found.file.util.FileManager;

/**
 * 文件基本属性
 * 
 * @author 吴小兵
 * @version 1.0 2012-12-6
 */
public class FileItem {
	public FileItem() {

	}

	/** 状态 */
	private String status = "";
	private String process = "";
	/** 板卡号 */
	private int boardId = 0;
	/** 本地文件名 */
	private String fileName = "";
	/** 远程文件名，带路径 */
	private String remoteFileName = "";
	/** 文件地址 */
	private String fileAdress = "";
	/** 文件是否选择 */
	private boolean checked = false;
	/** 文件大小 */
	private long length = 0;
	/** 文件选择行号 */
	private int row = 0;
	/** fpk包文件名 */
	private String fpkFileName = "";
	private byte[] data;
	private String type = "exe";
	private int crc = 0;
	private long createTime = 0;
	private long modifyTime = 0;
	/** 本地文件名,带路径 */
	private String fullFileName = "";
	/** 上传文件保存本地路径 */
	private String localPath = "";
	// 记录out文件附加时间所在位置,fpk打包用
	private long timeSeek = 0;

	// 下面为录波文件用
	private boolean bascii = false;
	private int orgAnaNum;
	private int orgDigNum;
	private int nrrAnaNum;
	private int nrrDigNum;
	// 文件是否加密
	private boolean decrFlag = true;
	//间隔名（配网工具使用，用于录波文件名前）
	private String bayName;

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileAdress() {
		return fileAdress;
	}

	public void setFileAdress(String fileAdress) {
		this.fileAdress = fileAdress;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String value) {
		this.status = value;
	}

	public String getRemoteFileName() {
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

	public long getLength() {
		if (length == 0 && !fullFileName.equals("")) {
			File file = new File(this.fullFileName);
			if (!file.exists())
				return 0;
			return file.length();
		}
		return length;
	}

	public void setLength(long l) {
		this.length = l;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getFileName(String fullfilename) {
		File file = new File(fullfilename);
		if (!file.exists() || file.isDirectory())
			return "";
		FileInputStream fis = null;
		try {
			String ext = fullfilename.substring(fullfilename.length() - 3,
					fullfilename.length()).toUpperCase();
			if (ext.equals("HEX")) {
				fis = new FileInputStream(file);
				if (fis.read() == 0x02) {

					this.setType("hex");
				} else {
					this.setType("exe");
				}
			} else {
				this.setType("exe");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		if (this.length < 1)
			this.length = file.length();
		return file.getName();

	}

	public long getLength(String fullfilename) {
		File file = new File(fullfilename);
		if (!file.exists())
			return 0;
		return file.length();
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFpkFileName() {
		return fpkFileName;
	}

	public void setFpkFileName(String fpkFileName) {
		this.fpkFileName = fpkFileName;
	}

	public int getCrc() {
		return crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	public long getCreateTime() {
		return this.createTime;
	}

	public long getCreateTime(String fullname) {
		createTime = 0;
		String strTime = "";
		try {
			if (fullname != null && !fullname.equals("")) {
				fullname = fullname.replace("/", "\\");
				File f = new File(fullname);
				this.fileName = f.getName();
				this.length = f.length();

			}
			Process p = Runtime.getRuntime().exec(
					"cmd /C dir \"" + fullname + "\" /tc");
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith(this.fileName)) {
					strTime = line.substring(0, 17);
					break;
				}
			}
		} catch (IOException e) {
			return 0;
		}
		if (!strTime.equals("")) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			try {
				Date date = formatter.parse(strTime);
				cal.setTime(date);
				this.createTime = cal.getTimeInMillis();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public long getModifyTime(String fullname) {
		File f = new File(fullname);
		long time = f.lastModified();
		this.modifyTime = time;
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getFullFileName() {
		if (fullFileName.equals("")) {
			return fileName;
		}
		return fullFileName;
	}

	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
		File file = new File(fullFileName);
		if (file.exists() && file.isFile()) {
			this.fileName = file.getName();
		}
	}

	/**
	 * 读取文件创建时间
	 */
	public String getStringCreateTime() {
		String filePath = fullFileName;
		String strTime = "";
		try {
			if (this.fullFileName != null && !this.fullFileName.equals("")) {
				File f = new File(fullFileName);
				this.fileName = f.getName();
				this.length = f.length();
			}
			Process p = Runtime.getRuntime().exec(
					"cmd /C dir " + filePath + "/tc");
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith(this.fileName)) {
					strTime = line.substring(0, 17);
					break;
				}
			}
		} catch (IOException e) {
			return "";
		}
		if (!strTime.equals("")) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			try {
				Date date = formatter.parse(strTime);
				cal.setTime(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// System.out.println("创建时间    " + strTime);
		return strTime;

		// 输出：创建时间 2009-08-17 10:21
	}

	/**
	 * 读取文件修改时间的方法1
	 */

	public String getStringModifiedTime() {
		File f = new File(fullFileName);
		Calendar cal = Calendar.getInstance();
		long time = f.lastModified();
		this.modifyTime = time;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cal.setTimeInMillis(time);
		// System.out.println("修改时间[2] " + formatter.format(cal.getTime()));
		return formatter.format(cal.getTime());
		// 输出：修改时间[1] 2009-8-17 10:32:38
	}

	public long getTimeSeek() {
		return timeSeek;
	}

	public void setTimeSeek(long timeSeek) {
		this.timeSeek = timeSeek;
	}

	/**
	 * 复制filename1对应的文件，并创建新文件filename2，文件名都要带路径
	 * 
	 * @param filename1
	 * @param filename2
	 * @return
	 */
	public static synchronized boolean copy(String filename1, String filename2) {
		File srcfile = new File(filename1);
		if (!srcfile.exists() || srcfile.isDirectory()) {
			return false;
		}
		// 判断文件是否存在，如果存在则删除
		File destFile = new File(filename2);
		if (destFile.exists()) {
			destFile.delete();

		} else {
			// 创建上层目录
			if (!destFile.getParentFile().exists()) {
				if (!destFile.getParentFile().mkdirs()) {
					return false;
				}
			}
		}

		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(srcfile);
			output = new FileOutputStream(destFile);
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	public static boolean delete(String filename) {
		File srcfile = new File(filename);
		if (!srcfile.exists()) {
			return true;
		}
		if (srcfile.isDirectory()) {
			if (srcfile.listFiles() == null || srcfile.listFiles().length == 0) {
				if (srcfile.delete()) {
					return true;
				}
			}
		}
		if (srcfile.delete()) {
			return true;
		}
		return false;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public synchronized boolean writeFile() {
		if (this.data.length < 1)
			return false;
		if (this.fullFileName.equals(""))
			return false;
		File writeFile = null;
		writeFile = new File(fullFileName);
		if (writeFile.exists()) {
			writeFile.delete();

		} else {
			// 创建上层目录
			if (!writeFile.getParentFile().exists()) {
				if (!writeFile.getParentFile().mkdirs()) {
					return false;
				}
			}
		}
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(fullFileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.write(data);
			// randomFile.seek(randomFile.length());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public static synchronized boolean writeFile(String fileName, byte[] data) {
		if (data.length < 1)
			return true;
		if (fileName.equals(""))
			return false;
		File writeFile = null;
		writeFile = new File(fileName);
		if (!writeFile.exists()) {
			// 创建上层目录
			if (!writeFile.getParentFile().exists()) {
				if (!writeFile.getParentFile().mkdirs()) {
					return false;
				}
			}
		}
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.write(data);
			randomFile.seek(randomFile.length());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}
	
	public static List<FileItem> getDtuWaveFileList(String filename,
			FileItem fileitem, String localPath) {
		List<FileItem> temp = new ArrayList<FileItem>();
		FileItem hdr = new FileItem();
		hdr.setFullFileName(localPath + "/" + filename + ".HDR");
		hdr.setRemoteFileName(fileitem.getRemoteFileName());
		delete(hdr.getFullFileName());
		temp.add(hdr);
		FileItem cfg = new FileItem();
		cfg.setFullFileName(localPath + "/" + filename + ".CFG");
		cfg.setRemoteFileName(fileitem.getRemoteFileName());
		delete(cfg.getFullFileName());
		temp.add(cfg);
		FileItem dat = new FileItem();
		dat.setFullFileName(localPath + "/" + filename + ".DAT");
		dat.setRemoteFileName(fileitem.getRemoteFileName());
		delete(dat.getFullFileName());
		temp.add(dat);
		return temp;
	}

	public static List<FileItem> getWaveFileList(String filename,
			FileItem fileitem, String localPath) {
		List<FileItem> temp = new ArrayList<FileItem>();
		FileItem hdr = new FileItem();
		hdr.setFullFileName(localPath + "/" + filename + ".HDR");
		hdr.setRemoteFileName(fileitem.getRemoteFileName());
		delete(hdr.getFullFileName());
		temp.add(hdr);
		FileItem cfg = new FileItem();
		cfg.setFullFileName(localPath + "/" + filename + ".CFG");
		cfg.setRemoteFileName(fileitem.getRemoteFileName());
		delete(cfg.getFullFileName());
		temp.add(cfg);
		FileItem dat = new FileItem();
		dat.setFullFileName(localPath + "/" + filename + ".DAT");
		dat.setRemoteFileName(fileitem.getRemoteFileName());
		delete(dat.getFullFileName());
		temp.add(dat);
		FileItem dbgcfg = new FileItem();
		dbgcfg.setFullFileName(localPath + "/" + filename + "_DBG.CFG");
		dbgcfg.setRemoteFileName(fileitem.getRemoteFileName());
		delete(dbgcfg.getFullFileName());
		temp.add(dbgcfg);
		FileItem dbgdat = new FileItem();
		dbgdat.setFullFileName(localPath + "/" + filename + "_DBG.DAT");
		dbgdat.setRemoteFileName(fileitem.getRemoteFileName());
		delete(dbgdat.getFullFileName());
		temp.add(dbgdat);
		return temp;
	}
	
	/**
	 * 处理录波文件，去除没有数据的文件
	 * @return
	 */
	private void doFileItems(List<FileItem> temp) {
//		List<FileItem> result = new ArrayList<>();
		int size = temp.size();
		for (int i = size - 1; i >= 0; i--) {
			FileItem fileItem = temp.get(i);
			String filePath = fileItem.getFullFileName().toLowerCase();
			File file = new File(filePath);
			if (!file.exists()) {
				temp.remove(i);
			}
//			byte[] data = temp.get(i).getData();
//			if (data == null ||  data.length <= 0) {
//				temp.remove(i);
//			}
		}
	}

	public boolean mergeWaveFile(List<FileItem> temp) {
		System.out.println("mergeWaveFile");
		if (temp.size() < 3) {
			System.out.println("temp.size<3");
			return true;
		}
		doFileItems(temp);
		String cfgfile = "";
		String datfile = "";
		String dbgcfg = "";
		String dbgdat = "";
		String hdrfile = "";
		for (FileItem fileItem : temp) {
			String file = fileItem.getFullFileName().toLowerCase();
			if (file.endsWith("_dbg.dat")) {
				dbgdat = fileItem.getFullFileName();
				delete(dbgdat.replace("/temp/", "/wave/"));
				continue;
			} else if (file.endsWith("_dbg.cfg")) {
				dbgcfg = fileItem.getFullFileName();
				delete(dbgcfg.replace("/temp/", "/wave/"));
				continue;
			} else if (file.endsWith(".cfg")) {
				cfgfile = fileItem.getFullFileName();
				delete(cfgfile.replace("/temp/", "/wave/"));
				continue;
			} else if (file.endsWith(".dat")) {
				datfile = fileItem.getFullFileName();
				delete(datfile.replace("/temp/", "/wave/"));
				continue;
			} else if (file.endsWith(".hdr")) {
				hdrfile = fileItem.getFullFileName();
				delete(hdrfile.replace("/temp/", "/wave/"));
				continue;
			}
		}
		System.out.println("hdrfile:" + hdrfile);
		if (copy(hdrfile, hdrfile.replace("/temp/", "/wave/"))) {
			delete(hdrfile);
		} else {
			System.out.println("复制文件出错");
		}
		//当文件数为3时，为FTU、DTU装置，不做其他处理
		if (temp.size() == 3) {
			if (copy(datfile, datfile.replace("/temp/", "/wave/"))) {
				delete(datfile);
			} else {
				System.out.println("复制文件出错");
			}
			if (copy(cfgfile, cfgfile.replace("/temp/", "/wave/"))) {
				delete(cfgfile);
			} else {
				System.out.println("复制文件出错");
			}
			return true;
		}
		boolean b = mergeCFGFile(cfgfile, dbgcfg);
		if (b) {
			if (bascii) {
				changeAsciiToBinary(datfile);
			}
			if (mergeDatFile(datfile, dbgdat)) {
				delete(dbgcfg);
				delete(dbgdat);
			} else {
				return false;
			}

		} else {

			return false;
		}
		return true;
	}

	private synchronized boolean mergeDatFile(String datafile, String dbgdat) {
		boolean bright = true;
		int AnaStartPos = 8;
		int orgDigSize = ((orgDigNum + 15) / 16) * 2;
		int orgLineSize = AnaStartPos + orgAnaNum * 2 + orgDigSize;
		int orgAnaEnd = AnaStartPos + orgAnaNum * 2;

		int nrrDigSize = ((nrrDigNum + 15) / 16) * 2;
		int nrrLineSize = AnaStartPos + nrrAnaNum * 2 + nrrDigSize;
		// int nrrAnaStart = AnaStartPos;
		int nrrAnaEnd = AnaStartPos + nrrAnaNum * 2;

		int totalDigSize = ((orgDigNum + nrrDigNum + 15) / 16) * 2;
		int totalLineSize = AnaStartPos + (orgAnaNum + nrrAnaNum) * 2
				+ totalDigSize;
		int totalDigNum = orgDigNum + nrrDigNum;

		byte[] porg = new byte[orgLineSize];
		byte[] pnrr = new byte[nrrLineSize];
		byte[] ptotal = new byte[totalLineSize];
		byte[] ptotalDig = new byte[totalDigSize * 8];
		String fname1 = datafile;
		String fname2 = dbgdat;
		String fname3 = datafile.replace("/temp/", "/wave/");
		delete(fname3);
		if (bright) {
			FileInputStream fp1 = null;
			FileInputStream fp2 = null;
			try {
				fp1 = new FileInputStream(fname1);
				fp2 = new FileInputStream(fname2);
				int orgReadLine = fp1.read(porg);
				int nrrReadLine = fp2.read(pnrr);
				if (orgReadLine != orgLineSize) {
					// String str = "org error!";
					System.out.println("org error");
					return false;
				}
				if (nrrReadLine != nrrLineSize) {
					// String str1 = "nrr error!";
					System.out.println("nrr error");
					return false;
				}
				while (orgReadLine > 0 && nrrReadLine > 0) {
					for (int i = 0; i < orgAnaEnd; i++) {
						ptotal[i] = porg[i];
					}
					for (int i = 0; i < nrrAnaNum * 2; i++) {
						ptotal[i + orgAnaEnd] = pnrr[AnaStartPos + i];
					}

					int totalDigStart = orgAnaEnd + nrrAnaEnd - AnaStartPos;
					int total = 0;
					for (int i = 0; i < orgDigSize / 2; i++) {
						byte[] tmp = new byte[2];
						tmp[0] = (porg[orgAnaEnd + 2 * i]);
						tmp[1] = (porg[orgAnaEnd + 2 * i + 1]);
						int tmp3 = ByteUtil.bytesToShort(tmp, 0);
						for (int k = 0; k < 16; k++) {
							ptotalDig[total++] = (byte) ((tmp3 >>> k) & 0x01);
							if (total == orgDigNum) {
								break;
							}
						}
					}
					for (int i = 0; i < nrrDigSize / 2; i++) {
						byte[] tmp = new byte[2];
						tmp[0] = (pnrr[nrrAnaEnd + 2 * i]);
						tmp[1] = (pnrr[nrrAnaEnd + 2 * i + 1]);
						int tmp3 = ByteUtil.bytesToShort(tmp, 0);
						for (int k = 0; k < 16; k++) {
							ptotalDig[total++] = (byte) ((tmp3 >>> k) & 0x01);
							if (total == totalDigNum) {
								break;
							}
						}
					}

					for (int i = 0; i < totalDigSize / 2; i++) {
						int tmp = 0;
						for (int k = 0; k < 16; k++) {
							tmp |= ((ptotalDig[i * 16 + k]) << k);
						}
						ptotal[totalDigStart + 2 * i] = (byte) (tmp & 0xFF);
						ptotal[totalDigStart + 2 * i + 1] = (byte) ((tmp >> 8) & 0xFF);

					}
					writeFile(fname3, ptotal);
					ptotal = new byte[totalLineSize];
					orgReadLine = fp1.read(porg);
					nrrReadLine = fp2.read(pnrr);
				}
				// if (!writeFile(fname3, ptotal)) {
				// // delete(fname3);
				// // return false;
				// }
				fp1.close();
				fp2.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					delete(fname1);
					delete(fname2);
					if (fp1 != null)
						fp1.close();
					if (fp2 != null)
						fp2.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

		return bright;
	}

	private synchronized boolean changeAsciiToBinary(String datafile) {
		int orgDigSize = ((orgDigNum + 15) / 16) * 2;
		int zeroNum = orgDigSize * 8 - orgDigNum;

		int orgLineSize = 8 + orgAnaNum * 2 + orgDigSize;
		int orgAnaEnd = 8 + orgAnaNum * 2;
		byte[] porg = new byte[orgLineSize];
		int pos = orgAnaEnd; // 合并后数字量开始位置

		List<String> lst = readStringList(datafile,"GBK");
		File fdatfile = new File(datafile);
		fdatfile.delete();
		int dotnum = orgDigNum + orgAnaNum + 1;
		for (int i = 0; i < lst.size(); i++) {
			String temp = lst.get(i);
			String[] lineLst = temp.split(",");
			if (lineLst.length >= dotnum) {
				for (int k = 0; k < zeroNum; k++) {
					lineLst = (String[]) ArrayUtils.add(lineLst, "0");
					// lineLst..append("0"); //补齐0
				}
				String sidx = lineLst[0].trim();
				String sval = lineLst[1].trim();
				int idx = Integer.parseInt(sidx);
				int tmval = Integer.parseInt(sval);
				porg[0] = (byte) (idx & 0xFF);
				porg[1] = (byte) ((idx >>> 8) & 0xFF);
				porg[2] = (byte) ((idx >>> 16) & 0xFF);
				porg[3] = (byte) ((idx >>> 24) & 0xFF);
				porg[4] = (byte) (tmval & 0xFF);
				porg[5] = (byte) ((tmval >>> 8) & 0xFF);
				porg[6] = (byte) ((tmval >>> 16) & 0xFF);
				porg[7] = (byte) ((tmval >>> 24) & 0xFF);
				for (int k = 0; k < orgAnaNum; k++) {
					String sana = lineLst[2 + k].trim();
					int ana = Integer.parseInt(sana);
					int saveval = ana;
					if (ana < 0) {
						saveval = (0xFFFF + ana + 1);
					} else {
						saveval = ana;
					}
					// memcpy(&porg[8+2*k], &saveval, sizeof(ushort));
					porg[8 + 2 * k] = (byte) (saveval & 0xFF);
					porg[8 + 2 * k + 1] = (byte) ((saveval >>> 8) & 0xFF);
				}
				// 合并成能被16整除
				// int size = 0;
				for (int k = 0; k < orgDigSize / 2; k++) {
					int dig = 0;
					for (int j = 0; j < 16; j++) {
						String sv = lineLst[2 + orgAnaNum + k * 16 + j];
						byte cv = (byte) Integer.parseInt(sv);
						dig |= (cv << j);
					}
					porg[pos + 2 * k] = (byte) (dig & 0XFF);
					porg[pos + 2 * k + 1] = (byte) ((dig >>> 8) & 0XFF);
					// size = pos + 2 * k + 1;
				}
				// int c=size;
			}
			writeFile(datafile, porg);

		}
		return true;
	}

	private synchronized boolean mergeCFGFile(String cfgfile, String dbgcfg) {
		if (decrFlag) {
			if (!decCfgFile(dbgcfg)) {
				System.out.println("解密dbgcfg文件失败");
				return false;
			}
			dbgcfg = dbgcfg.replace("/temp/", "/wave/");
		} else {
			String targetdbg = dbgcfg.replace("/temp/", "/wave/");
			copy(dbgcfg, targetdbg);
			delete(dbgcfg);
			dbgcfg = targetdbg;
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		String megText = "";
		List<String> orgList = readStringList(cfgfile,"GBK");
		List<String> nrrList = readStringList(dbgcfg,"GBK");
		if (orgList == null || orgList.isEmpty() || nrrList == null
				|| nrrList.isEmpty()) {
			System.out.println("orgList或nrrList为空");
			return false;
		}
		if (nrrList.size() < 2) {
			System.out.println("nrrList小于2");
			return false;
		}
		megText += orgList.get(0) + "\r\n"; // UAPC,变压器成套保护装置,1999
		String sorgNum = orgList.get(1);
		String[] orgtmp = sorgNum.split(",");
		if (orgtmp.length < 3) {
			System.out.println("orgtmp小于3");
			return false;
		}
		String sorgAna = orgtmp[1];
		String sorgDig = orgtmp[2];
		String sorgAna1 = sorgAna.substring(0, sorgAna.length() - 1);
		String sorgDig1 = sorgDig.substring(0, sorgDig.length() - 1);
		orgAnaNum = Integer.parseInt(sorgAna1);
		orgDigNum = Integer.parseInt(sorgDig1);
		int orgTotal = orgAnaNum + orgDigNum;

		String snrrNum = nrrList.get(1);
		String[] nrrtmp = snrrNum.split(",");
		if (nrrtmp.length < 3) {
			System.out.println("nrrtmp小于3");
			return false;
		}
		String snrrAna = nrrtmp[1];
		String snrrDig = nrrtmp[2];
		String snrrAna1 = snrrAna.substring(0, snrrAna.length() - 1);
		String snrrDig1 = snrrDig.substring(0, snrrDig.length() - 1);
		nrrAnaNum = Integer.parseInt(snrrAna1);
		nrrDigNum = Integer.parseInt(snrrDig1);
		if (nrrAnaNum == 0 && nrrDigNum == 0) {
			// System.out.println("nrrAnaNum并且nrrDigNum为0");
			String target = cfgfile.replace("/temp/", "/wave/");
			copy(cfgfile, target);
			delete(cfgfile);
			delete(dbgcfg);
		} else {
			int nrrTotal = nrrAnaNum + nrrDigNum;

			String sTotal = String.valueOf(orgTotal + nrrTotal);
			String sAna = String.valueOf(orgAnaNum + nrrAnaNum);
			String sDig = String.valueOf(orgDigNum + nrrDigNum);
			String stotalNum = sTotal + "," + sAna + "A," + sDig + "D\r\n";
			// QString("%1,%2A,%3D\r\n").arg(sTotal, sAna, sDig);
			megText += stotalNum;
			int org1 = orgList.size();
			int nrr1 = nrrList.size();
			for (int i = 0; i < orgAnaNum; i++) {
				if (org1 <= (2 + i)) {
					// printf("ana");
					break;
				}
				megText += orgList.get(2 + i) + "\r\n";
			}
			for (int i = 0; i < nrrAnaNum; i++) {
				if (nrr1 <= (2 + i)) {
					// printf("dbgana");
					break;
				}
				String sline = nrrList.get(2 + i);
				int pos = sline.indexOf(",");
				String sright = sline.substring(pos, sline.length());
				String snewLine = String.valueOf(i + orgAnaNum + 1) + sright;
				megText += snewLine + "\r\n";
			}
			for (int i = 0; i < orgDigNum; i++) {
				if (org1 <= (2 + orgAnaNum + i)) {
					// printf("dig");
					break;
				}
				megText += orgList.get(2 + orgAnaNum + i) + "\r\n";
			}
			for (int i = 0; i < nrrDigNum; i++) {
				if (nrr1 <= (2 + nrrAnaNum + i)) {
					// printf("dbgdig");
					break;
				}
				String sline = nrrList.get(2 + nrrAnaNum + i);
				int pos = sline.indexOf(",");
				String sright = sline.substring(pos, sline.length());
				String snewLine = String.valueOf(i + orgDigNum + 1) + sright;
				megText += snewLine + "\r\n";
			}
			int lnum = (int) orgList.size();
			for (int i = 2 + orgAnaNum + orgDigNum; i < lnum; i++) {
				if (org1 <= i) {
					// printf("last");
					break;
				}
				String str = orgList.get(i);
				if (str == "ASCII") {
					// qDebug("find ascii.");
					str = "BINARY";
					bascii = true;
				}
				megText += str + "\r\n";
			}
			String target = cfgfile.replace("/temp/", "/wave/");
			delete(target);
			delete(cfgfile);
			delete(dbgcfg);
			if (!writeFileString(target, megText)) {
				return false;
			}
		}

		return true;
	}

	public static synchronized boolean writeFileString(String fileName,
			String content) {
		OutputStreamWriter pw = null;// 定义一个流
		try {
			pw = new OutputStreamWriter(new FileOutputStream(fileName), "GBK");// 确认流的输出文件和编码格式，此过程创建了“test.txt”实例
			pw.write(content);// 将要写入文件的内容，可以多次write
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (pw != null)
				try {
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return true;
	}

	public static List<String> readStringList(String filename, String encoding) {
		List<String> temp = new ArrayList<String>();
		File file = new File(filename);
		String line = null;
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), encoding));
			while ((line = reader.readLine()) != null) {
				temp.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if (reader!=null ) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}


	public synchronized boolean decCfgFile(String dbgcfg) {
		String target = dbgcfg.replace("/temp/", "/wave/");
		File f1 = new File(dbgcfg);
		String temp = target;
		File f2 = new File(temp);
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(f1);
			output = new FileOutputStream(f2);
			byte[] buffer = new byte[1];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				buffer[0] = (byte) (buffer[0] - 3);
				output.write(buffer, 0, n);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		f1.delete();
		return true;
	}

	public static String getFileChart(String filePath) throws IOException {
		String code = "GBK";
		String lower = filePath.toLowerCase();
		if (lower.endsWith(".xml") || lower.endsWith(".cid")
				|| lower.endsWith(".icd")) {
			code = "UTF-8";
		}
		return code;
	}

	public static synchronized String readFileText(String filename) {
		String temp = "";
		try {
			temp = FileManager.readFileByCharset(filename,
					getFileChart(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public boolean isDecrFlag() {
		return decrFlag;
	}

	public void setDecrFlag(boolean decrFlag) {
		this.decrFlag = decrFlag;
	}

	public String getBayName() {
		return bayName;
	}

	public void setBayName(String bayName) {
		this.bayName = bayName;
	}
	
}
