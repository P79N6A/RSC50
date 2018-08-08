/*
 * @(#) FpkFile.java
 *
 * Copyright (c) 2007 - 2013 上海思源弘瑞电力自动化有限公司.
 * All rights reserved. 基于 Eclipse E4 a next generation 
 * platform (e.g., the CSS styling, dependency injection, Modeled UI) 
 * Rich Client Application 开发的U21继电保护装置平台工具软件. 
 * 此系列工具软件包括装置调试下载工具、装置配置工具. 103通信调试工具、
 * 自动化装置测试工具。
 */
package com.shrcn.found.file.fpk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.shrcn.found.common.util.ByteUtil;
import com.shrcn.found.file.util.FileManipulate;

/**
 * 
 * 
 * @author 吴小兵
 * @version 1.0 2012-12-26
 */
public class FpkFile {

	private String totalFullName = "";// fpk文件名
	private long totalFileSize = 0;// fpk文件长度
	private int totalFileCount = 0;// fpk子文件数量
	private int totalCrcCode = 0;// fpk文件crc
	private long totalTime = 0;
	private boolean bSaveDiffFolder = true;// 保存到不同的子目录
	private boolean bChangeFileTime = true;// 更新子文件修改时间
	private boolean appendTimeAndCrc = true;// 附加时间及crc到文件
	private boolean isNew = true;//文件是否是新格式，默认为true
	private int timeoption = 1; // 附加时间选项，1表示系统时间，2文件修改时间，3自定义时间timevalue
	public boolean splitTempFlag = false;
	private long timevalue = 0;
	private List<FileItem> subFileList = new ArrayList<FileItem>();
	private String path = "";
	private String ver = "";
	private String insideVer="";//内部版本号

	// 保存fpk文件，totcrc为所有子文件的crc之和，如果之和超出int上限，
	// 则转换成16进制字符串，取最后8为字符，转换成int
	public String saveFpkFile(String fpkFullname) throws IOException {
		if (subFileList.size() < 1) {
			return "请添加要打包的子文件";
		}
		if (ver.equals("") || insideVer.equals("")){
			return "请输入版本号和内部版本号";
		}
		isNew = true;
		String err = "";
		totalFullName = fpkFullname;
		calTotalInfo();
		
		byte[] tWbIdentity = null;
		tWbIdentity = ByteUtil.intToBytes(CN_JOINT_FILE_IDENTITY_NEW);
		byte[] tWbFileSize = ByteUtil.intToBytes((int) totalFileSize);
		byte[] tWbFileCount = ByteUtil.intToBytes(totalFileCount);

		byte[] tWbPkgTime = ByteUtil.intToBytes((int) (totalTime));
		byte[] tWbTotalCrc = ByteUtil.intToBytes(totalCrcCode);
		byte[] buf = ArrayUtils.addAll(tWbIdentity, tWbFileSize);
		buf = ArrayUtils.addAll(buf, tWbFileCount);
		buf = ArrayUtils.addAll(buf, tWbPkgTime);
		buf = ArrayUtils.addAll(buf, tWbTotalCrc);

		getFileWrite(fpkFullname);

		RandomAccessFile randomFile = null;
		randomFile = new RandomAccessFile(fpkFullname, "rw");
		// 文件长度，字节数
		long fileLength = randomFile.length();
		// 将写文件指针移到文件尾。
		randomFile.seek(fileLength);
		randomFile.write(buf);
		randomFile.seek(randomFile.length());
		// writeFile(fpkFullname, buf);
		int i = 0;
		for (FileItem fileitem : subFileList) {
			err = writeSubFile(fileitem, randomFile, i);
			if (!err.equals("")) {
				break;
			}
			i++;
		}
		if (randomFile != null)
			randomFile.close();
		return err;

	}

	// 写子文件内容
	private String writeSubFile(FileItem fileitem, RandomAccessFile randomFile,
			int i) throws IOException {
		String err = "";
		String strFileName = fileitem.getFileName();
		if (strFileName.length() > CN_JOINT_FILE_NAME_LENGTH) {
			err = "子文件名长度超限";
			return err;
		}
		byte[] buf = new byte[0];
		// 写入子文件index
		byte[] index = ByteUtil.intToBytes(i);
		buf = ArrayUtils.addAll(buf, index);

		// 写入子文件名
		// dsWrite.writeRawData( chFileName,CN_JOINT_FILE_NAME_LENGTH );
		byte[] wjm = new byte[256];
		wjm = ArrayUtils.addAll(strFileName.getBytes(), wjm);
		wjm = ArrayUtils.subarray(wjm, 0, 256);
		buf = ArrayUtils.addAll(buf, wjm);
		// 写入子文件创建时间
		byte[] zwjcjsj = ByteUtil.intToBytes((int) (fileitem.getCreateTime() / 1000));
		buf = ArrayUtils.addAll(buf, zwjcjsj);
		// 写入子文件修改时间
		byte[] zwjxgsj = ByteUtil.intToBytes((int) (fileitem.getModifyTime() / 1000));
		buf = ArrayUtils.addAll(buf, zwjxgsj);
		// 写入子文件实际长度
		long sjcd = fileitem.getLength();
		if (appendTimeAndCrc && existOut(fileitem)) {
			if (isNew){
				sjcd = sjcd + CN_JOINT_FILE_TIMESTAMP_LENGTH
						+ CN_JOINT_FILE_SUBCRC_LENGTH + CN_JOINT_FILE_VER_LENGTH
						+ CN_JOINT_FILE_VER_LENGTH + 1;
			} else {
				sjcd = sjcd + CN_JOINT_FILE_TIMESTAMP_LENGTH
					+ CN_JOINT_FILE_SUBCRC_LENGTH + CN_JOINT_FILE_VER_LENGTH;
			}
		}
		byte[] zwjsjcd = ByteUtil.intToBytes((int) sjcd);
		buf = ArrayUtils.addAll(buf, zwjsjcd);
		// 写入子文件原始长度
		byte[] zwjyscd = ByteUtil.intToBytes((int) fileitem.getLength());
		buf = ArrayUtils.addAll(buf, zwjyscd);
		// 写入子文件下载板号
		byte[] zwjxzbh = ByteUtil.intToBytes(fileitem.getBoardId());
		buf = ArrayUtils.addAll(buf, zwjxzbh);
		// 写入子文件CRC32校验码(此校验码不包括附加的时间及CRC共8个字节的数据)
		byte[] zwjcrc = ByteUtil.intToBytes(fileitem.getCrc());
		buf = ArrayUtils.addAll(buf, zwjcrc);
		randomFile.seek(randomFile.length());
		randomFile.write(buf);
		FileInputStream fis = null;
		// 写入文件内容
		byte[] chBuffer = new byte[CN_FILE_OPER_BUFFER_SIZE];
		try {
			fis = new FileInputStream(fileitem.getFullFileName());
			int len = fis.read(chBuffer);
			while (len > 0) {
				if (len == CN_FILE_OPER_BUFFER_SIZE) {
					// writeFile(fpkFullname, chBuffer);
					randomFile.seek(randomFile.length());
					randomFile.write(chBuffer);
				} else {
					chBuffer = ArrayUtils.subarray(chBuffer, 0, len);
					randomFile.seek(randomFile.length());
					randomFile.write(chBuffer);
					break;
					// writeFile(fpkFullname, chBuffer);
				}
				len = fis.read(chBuffer);
			}
			if (existOut(fileitem) && appendTimeAndCrc) {
				writeAppendCrc(randomFile, fileitem);
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
			err = "子文件未找到";
			return err;
		} catch (IOException e) {

			e.printStackTrace();

			err = "文件读写错误";
			return err;
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return err;
	}

	// 附加crc
	private void writeAppendCrc(RandomAccessFile randomFile, FileItem fileitem)
			throws IOException {
		// 字节反转写入
		byte[] buf = new byte[4];
		buf = ByteUtil.intToBytesBE((int) (totalTime));
		// writeFile(fpkFullname, buf);
		// System.out.println(randomFile.length());
		fileitem.setTimeSeek(randomFile.length());
		randomFile.seek(randomFile.length());
		randomFile.write(buf);
		buf = new byte[4];
		buf = ByteUtil.intToBytesBE(totalCrcCode);
		// writeFile(fpkFullname, buf);
		randomFile.seek(randomFile.length());
		randomFile.write(buf);
		buf = new byte[CN_JOINT_FILE_VER_LENGTH];
		buf = this.ver.getBytes();
		buf = ArrayUtils.subarray(buf, 0, 8);
		randomFile.seek(randomFile.length());
		randomFile.write(buf);
		if (isNew){
			buf = new byte[CN_JOINT_FILE_VER_LENGTH];
			buf = this.insideVer.getBytes();
			buf = ArrayUtils.subarray(buf, 0, 8);
			randomFile.seek(randomFile.length());
			randomFile.write(buf);
			buf = new byte[1];
			buf[0] = 0x0F;
			randomFile.seek(randomFile.length());
			randomFile.write(buf);
		}
	}

	private void getFileWrite(String fpkFullname) {
		File writeFile = null;
		writeFile = new File(fpkFullname);
		if (writeFile.exists()) {
			writeFile.delete();

		} else {
			// 创建上层目录
			if (!writeFile.getParentFile().exists()) {
				if (!writeFile.getParentFile().mkdirs()) {
					return;
				}
			}
		}
	}

	// 计算汇总信息
	private void calTotalInfo() {
		long totalcrc = 0;
		totalFileCount = subFileList.size();
		totalFileSize = 0;

		long tempcrc = 0;
		for (FileItem fileitem : subFileList) {
			if (existOut(fileitem) && appendTimeAndCrc) {
				if (isNew){
					totalFileSize = totalFileSize + fileitem.getLength()
							+ CN_JOINT_FILE_SUBFILE_HEADER_LENGTH
							+ CN_JOINT_FILE_TIMESTAMP_LENGTH
							+ CN_JOINT_FILE_SUBCRC_LENGTH
							+ CN_JOINT_FILE_VER_LENGTH
							+ CN_JOINT_FILE_VER_LENGTH +1;
				} else {
					totalFileSize = totalFileSize + fileitem.getLength()
							+ CN_JOINT_FILE_SUBFILE_HEADER_LENGTH
							+ CN_JOINT_FILE_TIMESTAMP_LENGTH
							+ CN_JOINT_FILE_SUBCRC_LENGTH
							+ CN_JOINT_FILE_VER_LENGTH;
				}
				

			} else {
				totalFileSize = totalFileSize + fileitem.getLength()
						+ CN_JOINT_FILE_SUBFILE_HEADER_LENGTH;

			}
			if (fileitem.isChecked())
				totalcrc = totalcrc + fileitem.getCrc();
			tempcrc = tempcrc + fileitem.getCrc();
		}
		if (totalcrc == 0) {
			totalcrc = tempcrc;
		}
		totalFileSize = totalFileSize + CN_JOINT_FILE_HEADER_LENGTH;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		totalTime = cal.getTimeInMillis() / 1000;

		String hexcrc = Long.toHexString(totalcrc);
		if (hexcrc.length() > 8) {
			hexcrc = hexcrc.substring(hexcrc.length() - 8, hexcrc.length());
		}
		totalCrcCode = (int) Long.parseLong(hexcrc, 16);

	}

	/***
	 * 获取fpk文件信息
	 * 
	 * @param fpkFullname
	 * @return
	 */
	public String getFpkFileInfo(String fpkFullname) {
		File file = new File(fpkFullname);
		if (!file.exists()) {
			return "文件不存在";
		}
		long n64FileSize = file.length();
		if (n64FileSize < CN_JOINT_FILE_HEADER_LENGTH) {
			return "文件过短，不是合格的打包文件";
		}
		this.splitTempFlag = false;
		FileInputStream fis = null;
		this.totalFullName = fpkFullname;
		int len = 0;
		try {
			fis = new FileInputStream(fpkFullname);
			// 读取标识符
			byte[] bsf = new byte[CN_JOINT_FILE_IDENTITY_LENGTH];
			len = fis.read(bsf);
			if (len != CN_JOINT_FILE_IDENTITY_LENGTH
					|| (ByteUtil.byteToInt(bsf) != CN_JOINT_FILE_IDENTITY && ByteUtil
							.byteToInt(bsf) != CN_JOINT_FILE_IDENTITY_NEW)) {
				return "文件标识错误，不是合格的打包文件";
			} else {
				if (ByteUtil.byteToInt(bsf) == CN_JOINT_FILE_IDENTITY){
					isNew = false;
				}
			}
//			VarItem var = new VarItem();
//			var.setType(VarItem.UINT32_TYPE);
			// 读取文件总长度
			byte[] zcd = new byte[CN_JOINT_FILE_TOTALSIZE_LENGTH];
			len = fis.read(zcd);
			if (len != CN_JOINT_FILE_TOTALSIZE_LENGTH) {
				return "文件长度错误";
			}
//			var.setByteValue(zcd);
//			totalFileSize = var.getiValue();
			totalFileSize = ByteUtil.bytesToUInt(zcd, 0);
			if (totalFileSize != n64FileSize) {
				return "文件长度错误";
			}

			// 读取子文件数量
			byte[] zwjsl = new byte[CN_JOINT_FILE_FILECOUNT_LENGTH];
			len = fis.read(zwjsl);
			if (len != CN_JOINT_FILE_FILECOUNT_LENGTH) {
				return "读取子文件数量失败";
			}
//			var.setByteValue(zwjsl);
//			totalFileCount = (int) var.getiValue();
			totalFileCount = (int) ByteUtil.bytesToUInt(zwjsl, 0);

			// 读取文件打包时间
			byte[] wjdbsj = new byte[CN_JOINT_FILE_PKGTIME_LENGTH];
			len = fis.read(wjdbsj);
			if (len != CN_JOINT_FILE_PKGTIME_LENGTH) {
				return "读取文件打包时间失败";
			}
//			var.setByteValue(wjdbsj);
//			this.totalTime = var.getiValue() * 1000;
			totalTime = ByteUtil.bytesToUInt(wjdbsj, 0) * 1000;
			// 读取总CRC校验码
			byte[] zcrcdm = new byte[CN_JOINT_FILE_TOTALCRC_LENGTH];
			len = fis.read(zcrcdm);
			if (len != CN_JOINT_FILE_TOTALCRC_LENGTH) {
				return "读取文件总CRC校验码失败";
			}
//			var.setByteValue(zcrcdm);
//			this.totalCrcCode = (int) var.getiValue();
			totalCrcCode = (int) ByteUtil.bytesToUInt(zcrcdm, 0);

			// 读取子文件
			for (int i = 0; i < totalFileCount; i++) {
				readSubFile(fis, file,true);
			}

			// 读文件结束

		} catch (FileNotFoundException e) {

			e.printStackTrace();
			return "文件不存在";
		} catch (IOException e) {

			e.printStackTrace();
			return "文件读写失败";
		} finally {
			try {
				if (fis!=null) fis.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		// 清除临时文件夹
		FileManipulate.deleteDir(getExtrDirPath(file,true));
		return "";
	}


	private void readSubFile(FileInputStream fis, File file,boolean flag) throws IOException {
		FileItem subfile = new FileItem();
		// 读取子文件index
		byte[] zwjindex = new byte[CN_JOINT_FILE_SUBINDEX_LENGTH];
		fis.read(zwjindex);

		// 读取文件名
		byte[] wjm = new byte[CN_JOINT_FILE_NAME_LENGTH];
		fis.read(wjm);
		String subFilename = getSubFileName(wjm);
		subfile.setFileName(subFilename);
		// 读取文件创建时间
		byte[] wjcjsj = new byte[CN_JOINT_FILE_CREATETIME_LENGTH];
		fis.read(wjcjsj);
//		VarItem var = new VarItem();
//		var.setByteValue(wjcjsj);
//		long fileCreateTime = var.getiValue() * 1000;
		long fileCreateTime = ByteUtil.bytesToUInt(wjcjsj, 0) * 1000;
		subfile.setCreateTime(fileCreateTime);
		// 读取文件修改时间
		byte[] wjxgsj = new byte[CN_JOINT_FILE_MODIFIEDTIME_LENGTH];
		fis.read(wjxgsj);
//		var.setByteValue(wjxgsj);
//		long fileModifyTime = var.getiValue() * 1000;
		long fileModifyTime = ByteUtil.bytesToUInt(wjxgsj, 0) * 1000;
		subfile.setModifyTime(fileModifyTime);
		// 读取文件实际大小
		byte[] wjsjdx = new byte[CN_JOINT_FILE_FILESIZE_LENGTH];
		fis.read(wjsjdx);
//		var.setByteValue(wjsjdx);
//		long subSize = var.getiValue();
		long subSize = ByteUtil.bytesToUInt(wjsjdx, 0);
		subfile.setLength(subSize);
		// 读取文件原始大小
		byte[] wjysdx = new byte[CN_JOINT_FILE_ORIGINSIZE_LENGTH];
		fis.read(wjysdx);
//		var.setByteValue(wjysdx);
//		long subOriginSize = var.getiValue();
		long subOriginSize = ByteUtil.bytesToUInt(wjysdx, 0);

		// 读取板件号
		byte[] bjh = new byte[CN_JOINT_FILE_BOARDNO_LENGTH];
		fis.read(bjh);
//		var.setByteValue(bjh);
//		int boardid = (int) var.getiValue();
		int boardid = (int) ByteUtil.bytesToUInt(bjh, 0);
		subfile.setBoardId(boardid);
		// 读取文件校验码
		byte[] wjjym = new byte[CN_JOINT_FILE_CRC32_LENGTH];
		fis.read(wjjym);
//		var.setByteValue(wjjym);
//		long subfilecrc = var.getiValue();
		long subfilecrc = ByteUtil.bytesToUInt(wjjym, 0);
		subfile.setCrc((int) subfilecrc);
		// 生成子文件保存路径
		String strSubFileDir = getSubFileDir(file, boardid,flag);
		this.path = strSubFileDir;
		subFilename = strSubFileDir + subFilename;
		// 生成保存文件夹
		// 判断文件是否存在，如果存在则删除
		File writeFile = new File(subFilename);
		if (writeFile.exists()) {
			writeFile.delete();
		} else {
			// 创建上层目录
			if (!writeFile.getParentFile().exists()) {
				if (!writeFile.getParentFile().mkdirs()) {
					return;
				}
			}
		}
		// 读取内容

		if (this.splitTempFlag) {
			readSubContent(subSize, fis, subFilename);
			if (subFilename.endsWith(".out")) {
				appendCRC(fis);
			}
		} else {
			readSubContent(subSize, fis, subFilename);
		}
		// 校验文件CRC
		if (!checkCrc(subFilename, (int) subfilecrc, subOriginSize)) {
			// System.out.println("EN_SPLIT_CRC_ERROR");
			return;
		}
		subfile.setLength(subfile.getLength(subFilename));
		subfile.setFullFileName(subFilename);
		subFileList.add(subfile);
	}

	private String getSubFileDir(File file, int boardid, boolean flag) {
		String strSubFileDir = getExtrDirPath(file,flag);
		
		// 若设置子文件保存在不同文件夹中,则加上板件号
		if (bSaveDiffFolder)
			strSubFileDir = strSubFileDir + String.valueOf(boardid) + File.separator;
		return strSubFileDir;
	}
	
	private static String getExtrDirPath(File file, boolean flag) {
		String filename = file.getName();
		String path = file.getParent().replace("\\", File.separator);
		String strSubFileDir;
		if (path.endsWith(File.separator)) {
			strSubFileDir = path;
		} else {
			strSubFileDir = path + File.separator;
		}
		if (flag){
			return strSubFileDir + "temp_" + filename.substring(0, filename.lastIndexOf("."))+File.separator;
		} else {
			return strSubFileDir + "extr_" + filename.substring(0, filename.lastIndexOf("."))+File.separator;
		}
	}
	
	public static String getExtrDirPath(String filepath) {
		return getExtrDirPath(new File(filepath),false);
	}

	private void appendCRC(FileInputStream fis) throws IOException {
		byte[] timebyte = new byte[CN_JOINT_FILE_TIMESTAMP_LENGTH];
		fis.read(timebyte);
		byte[] temp = new byte[4];
		// 反转字节
		for (int j = 0; j < 4; j++) {
			temp[j] = timebyte[3 - j];
		}
//		VarItem var = new VarItem();
//		var.setByteValue(temp);
//		long val = var.getiValue();
		long val = ByteUtil.bytesToUInt(temp, 0);
		this.timevalue = val * 1000;
		byte[] crcbyte = new byte[CN_JOINT_FILE_SUBCRC_LENGTH];
		fis.read(crcbyte);
		for (int j = 0; j < 4; j++) {
			temp[j] = crcbyte[3 - j];
		}
//		var.setByteValue(temp);
//		this.totalCrcCode = (int) var.getiValue();
		this.totalCrcCode = (int) ByteUtil.bytesToUInt(temp, 0);
		byte[] verbyte = new byte[CN_JOINT_FILE_VER_LENGTH];
		fis.read(verbyte);
		String vertemp = new String(verbyte);
		this.ver = vertemp;
		if (isNew){
			verbyte = new byte[CN_JOINT_FILE_VER_LENGTH];
			fis.read(verbyte);
			vertemp = new String(verbyte);
			this.insideVer = vertemp;
			byte[] flag = new byte[1];
			fis.read(flag);
		}
	}

	private void readSubContent(long subOriginSize, FileInputStream fis,
			String subFilename) throws IOException {

		// 读取文件内容
		long n64UnReadFileSize = subOriginSize;
		if (this.splitTempFlag) {
			if (subFilename.endsWith(".out")) {
				if (isNew){
					n64UnReadFileSize = n64UnReadFileSize
							- CN_JOINT_FILE_TIMESTAMP_LENGTH
							- CN_JOINT_FILE_SUBCRC_LENGTH
							- CN_JOINT_FILE_VER_LENGTH
							- CN_JOINT_FILE_VER_LENGTH -1;
				} else {
				n64UnReadFileSize = n64UnReadFileSize
						- CN_JOINT_FILE_TIMESTAMP_LENGTH
						- CN_JOINT_FILE_SUBCRC_LENGTH
						- CN_JOINT_FILE_VER_LENGTH;
				}
			}
		}
		int len = 0;
		// 读取文件内容
		while (n64UnReadFileSize > 0) {
			if (n64UnReadFileSize > CN_FILE_OPER_BUFFER_SIZE) {
				byte[] readdata = new byte[CN_FILE_OPER_BUFFER_SIZE];
				len = fis.read(readdata);
				readdata = ArrayUtils.subarray(readdata, 0, len);
				// 写子文件
				this.writeFile(subFilename, readdata);
				n64UnReadFileSize = n64UnReadFileSize
						- CN_FILE_OPER_BUFFER_SIZE;
			} else {
				byte[] readdata = new byte[(int) n64UnReadFileSize];
				len = fis.read(readdata);
				readdata = ArrayUtils.subarray(readdata, 0, len);
				// 写子文件
				this.writeFile(subFilename, readdata);
				n64UnReadFileSize = 0;
			}

		}
	}

	public String extractFpkFile(String fpkFullname) {
		String msg = openFpkFile(fpkFullname);
		return msg;
	}

	public String openFpkFile(String fpkFullname) {
		File file = new File(fpkFullname);
		if (!file.exists()) {
			return "文件不存在";
		}
		long n64FileSize = file.length();
		if (n64FileSize < CN_JOINT_FILE_HEADER_LENGTH) {
			return "文件过短，不是合格的打包文件";
		}
		FileInputStream fis = null;
		int len = 0;
		try {
			fis = new FileInputStream(fpkFullname);
			// 读取标识符
			byte[] bsf = new byte[CN_JOINT_FILE_IDENTITY_LENGTH];
			len = fis.read(bsf);
			if (len != CN_JOINT_FILE_IDENTITY_LENGTH
					|| (ByteUtil.byteToInt(bsf) != CN_JOINT_FILE_IDENTITY && ByteUtil
							.byteToInt(bsf) != CN_JOINT_FILE_IDENTITY_NEW)) {
				return "文件标识不正确";
			} else {
				if (ByteUtil.byteToInt(bsf) == CN_JOINT_FILE_IDENTITY){
					isNew = false;
				}
			}
			// 读取文件总长度
			byte[] zcd = new byte[CN_JOINT_FILE_TOTALSIZE_LENGTH];
			len = fis.read(zcd);
			if (len != CN_JOINT_FILE_TOTALSIZE_LENGTH) {
				return "EN_SPLIT_LENGTH_ERROR";
			}
			totalFileSize = ByteUtil.bytesToUInt(zcd, 0);
			if (totalFileSize != n64FileSize) {
				return "文件长度不正确";
			}

			// 读取子文件数量
			byte[] zwjsl = new byte[CN_JOINT_FILE_FILECOUNT_LENGTH];
			len = fis.read(zwjsl);
			if (len != CN_JOINT_FILE_FILECOUNT_LENGTH) {
				return "读取子文件数量失败";
			}
//			var.setByteValue(zwjsl);
//			totalFileCount = (int) var.getiValue();
			totalFileCount = (int) ByteUtil.bytesToUInt(zwjsl, 0);

			// 读取文件打包时间
			byte[] wjdbsj = new byte[CN_JOINT_FILE_PKGTIME_LENGTH];
			len = fis.read(wjdbsj);
			if (len != CN_JOINT_FILE_PKGTIME_LENGTH) {
				return "读取文件打包时间失败";
			}
//			var.setByteValue(wjdbsj);
//			this.totalTime = var.getiValue() * 1000;
			this.totalTime = ByteUtil.bytesToUInt(wjdbsj, 0) * 1000;

			// 读取总CRC校验码
			byte[] zcrcdm = new byte[CN_JOINT_FILE_TOTALCRC_LENGTH];
			len = fis.read(zcrcdm);
			if (len != CN_JOINT_FILE_TOTALCRC_LENGTH) {
				return "读取文件总CRC校验码失败";
			}

			// 读取子文件
			for (int i = 0; i < totalFileCount; i++) {
				this.readSubFile(fis, file,false);
			}
			// 读文件结束

		} catch (FileNotFoundException e) {

			e.printStackTrace();
			return "文件不存在";
		} catch (IOException e) {

			e.printStackTrace();
			return "文件读写失败";
		} finally {
			try {
				if (fis!=null) fis.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return "";
	}


	private String getSubFileName(byte[] wjm) {
		byte[] temp = new byte[0];
		for (int i = 0; i < wjm.length; i++) {
			if (wjm[i] == 0x00) {
				temp = ArrayUtils.subarray(wjm, 0, i);
				break;
			}
		}
		if (temp.length > 0)
			return new String(temp);
		else
			return "";
	}

	private boolean checkCrc(String fileFullName, int subfilecrc,
			long subOriginSize) {
		int crcCal = 0;
		crcCal = fileCrc32(fileFullName, subOriginSize);
		// test
		// crcCal = subCrcCode;
		if (subfilecrc == crcCal)
			return true;
		else
			return true;
	}

	// 计算文件CRC校验值
	public static int fileCrc32(String fileFullName, long subOriginSize) {
		int dwCrc32 = 0xFFFFFFFF;
		FileInputStream fis = null;
		long n64UnreadLength = 0;
		File file = new File(fileFullName);
		n64UnreadLength = file.length();
		if (n64UnreadLength > subOriginSize)
			n64UnreadLength = subOriginSize;
		int len = 0;

		try {
			fis = new FileInputStream(fileFullName);

			while (n64UnreadLength > 0) {
				if ((n64UnreadLength - CN_MAX_CRC_BUFFER_SIZE) > 0) {
					byte[] buffer = new byte[CN_MAX_CRC_BUFFER_SIZE];
					len = fis.read(buffer);
					n64UnreadLength = n64UnreadLength - len;

					for (int nLoop = 0; nLoop < len; nLoop++)
						dwCrc32 = calcCrc32(buffer[nLoop], dwCrc32);
				} else {
					byte[] buffer = new byte[(int) n64UnreadLength];
					len = fis.read(buffer);
					n64UnreadLength = n64UnreadLength - len;

					for (int nLoop = 0; nLoop < len; nLoop++)
						dwCrc32 = calcCrc32(buffer[nLoop], dwCrc32);
				}
			}
			dwCrc32 = ~dwCrc32;
			return dwCrc32;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			return 0;
		} catch (IOException e) {

			e.printStackTrace();
			return 0;
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static int calcCrc32(byte b, int dwCrc32) {
		int index = ((b) ^ (dwCrc32)) & 0x000000FF;
		return ((dwCrc32) >>> 8) ^ CRC[index];
	}

	private boolean writeFile(String filename, byte[] result) {
		RandomAccessFile randomFile = null;
		try {
			// System.out.println(new String(result, "utf-8"));

			// 打开一个随机访问文件流，按读写方式
			randomFile = new RandomAccessFile(filename, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.write(result);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (randomFile != null)
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
		}
		return true;
	}

	public String getTotalFullName() {
		return totalFullName;
	}

	public void setTotalFullName(String totalFullName) {
		this.totalFullName = totalFullName;
	}

	public long getTotalFileSize() {
		return totalFileSize;
	}

	public void setTotalFileSize(long totalFileSize) {
		this.totalFileSize = totalFileSize;
	}

	public int getTotalFileCount() {
		return totalFileCount;
	}

	public void setTotalFileCount(int totalFileCount) {
		this.totalFileCount = totalFileCount;
	}

	public List<FileItem> getSubFileList() {
		return subFileList;
	}

	public void setSubFileList(List<FileItem> subFileList) {
		this.subFileList = subFileList;
	}

	public boolean isbChangeFileTime() {
		return bChangeFileTime;
	}

	public void setbChangeFileTime(boolean bChangeFileTime) {
		this.bChangeFileTime = bChangeFileTime;
	}

	public boolean isAppendTimeAndCrc() {
		return appendTimeAndCrc;
	}

	public void setAppendTimeAndCrc(boolean appendTimeAndCrc) {
		this.appendTimeAndCrc = appendTimeAndCrc;
	}

	public int getTimeoption() {
		return timeoption;
	}

	public void setTimeoption(int timeoption) {
		this.timeoption = timeoption;
	}

	public long getTimevalue() {
		return timevalue;
	}

	public void setTimevalue(long timevalue) {
		this.timevalue = timevalue;
	}

	public int getTotalCrcCode() {
		return totalCrcCode;
	}

	public void setTotalCrcCode(int totalCrcCode) {
		this.totalCrcCode = totalCrcCode;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public String getPath() {

		return this.path;
	}

	// 是否out文件
	private boolean existOut(FileItem fileitem) {
		if (fileitem.getFullFileName().toLowerCase().endsWith(".out")) {
			return true;
		}
		return false;
	}

	public boolean writeTime(String fpkfile, int time, int crc) {
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(fpkfile, "rw");
			for (FileItem fileitem : subFileList) {
				if (fileitem.getFileName().toLowerCase().endsWith(".out")) {
					long seek = fileitem.getTimeSeek();
					// 字节反转写入
					// System.out.println(seek);
					byte[] buf = new byte[4];
					buf = ByteUtil.intToBytesBE(time);
					randomFile.seek(seek);
					randomFile.write(buf);
					buf = new byte[4];
					buf = ByteUtil.intToBytesBE(crc);
					randomFile.seek(seek + 4);
					randomFile.write(buf);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (randomFile != null)
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
		}
		return true;
	}

	public void setVer(String ver) {
		if (ver==null) ver="1.00";
		byte[] verByte = ver.getBytes();
		if (verByte.length==8){
			
		} else {
			byte[] temp = new byte[8];
			for (int i = 0; i < temp.length; i++) {
				if (i>=verByte.length){
					temp[i]=' ';//不够补空格
				} else {
					temp[i] = verByte[i];
				}
			}
			ver = new String(temp);
		}
		this.ver = ver;
	}

	public static int[] getCrc() {
		return CRC;
	}

	public String getVer() {
		return ver;
	}
	
	public String getInsideVer() {
		return insideVer;
	}

	public void setInsideVer(String insideVer) {
		if (insideVer==null || insideVer.equals("")){
			this.insideVer="";
			return;
		}
		if (insideVer.length()!=8){
			Date date = new Date();
			long ldate = date.getTime() ;
			long randonNum = (long) (Math.random() * ldate);
			String randStr = String.valueOf(randonNum) +"00000";
			randStr = randStr.substring(0, 5);
			this.insideVer = randStr+"000";
		} else {
			this.insideVer = insideVer;
		}
	}

	/**fpk文件用*/
	public final static int CN_JOINT_FILE_IDENTITY_LENGTH = 4;
	public final static int CN_JOINT_FILE_IDENTITY_NEW = 0x0ABCDEFA;
	public final static int CN_JOINT_FILE_IDENTITY = 0x0ACBEDAF;
	public final static int CN_JOINT_FILE_TOTALSIZE_LENGTH = 4;
	public final static int CN_JOINT_FILE_FILECOUNT_LENGTH = 4;
	public final static int CN_JOINT_FILE_PKGTIME_LENGTH = 4;
	public final static int CN_JOINT_FILE_TOTALCRC_LENGTH = 4;
	public final static int CN_JOINT_FILE_SUBINDEX_LENGTH = 4;
	public final static int CN_JOINT_FILE_CREATETIME_LENGTH = 4;
	public final static int CN_JOINT_FILE_MODIFIEDTIME_LENGTH = 4;
	public final static int CN_JOINT_FILE_FILESIZE_LENGTH = 4;
	public final static int CN_JOINT_FILE_ORIGINSIZE_LENGTH = 4;
	public final static int CN_JOINT_FILE_BOARDNO_LENGTH = 4;
	public final static int CN_JOINT_FILE_CRC32_LENGTH = 4;
	public final static int CN_FILE_OPER_BUFFER_SIZE = 4096000;
	public final static int CN_JOINT_FILE_HEADER_LENGTH = 20;
	public final static int CN_JOINT_FILE_NAME_LENGTH = 256;
	public final static int CN_MAX_CRC_BUFFER_SIZE = 4096000;
	public final static int CN_CRCERRORCODE_NOERROR = 0;
	public static final int CN_JOINT_FILE_SUBFILE_HEADER_LENGTH = 284;
	public static final int CN_JOINT_FILE_TIMESTAMP_LENGTH = 4;
	public static final int CN_JOINT_FILE_SUBCRC_LENGTH = 4;
	public static final int CN_JOINT_FILE_VER_LENGTH = 8;
	public final static int[] CRC = { 0x00000000, 0x77073096, 0xEE0E612C,
		0x990951BA, 0x076DC419, 0x706AF48F, 0xE963A535, 0x9E6495A3,
		0x0EDB8832, 0x79DCB8A4, 0xE0D5E91E, 0x97D2D988, 0x09B64C2B,
		0x7EB17CBD, 0xE7B82D07, 0x90BF1D91, 0x1DB71064, 0x6AB020F2,
		0xF3B97148, 0x84BE41DE, 0x1ADAD47D, 0x6DDDE4EB, 0xF4D4B551,
		0x83D385C7, 0x136C9856, 0x646BA8C0, 0xFD62F97A, 0x8A65C9EC,
		0x14015C4F, 0x63066CD9, 0xFA0F3D63, 0x8D080DF5, 0x3B6E20C8,
		0x4C69105E, 0xD56041E4, 0xA2677172, 0x3C03E4D1, 0x4B04D447,
		0xD20D85FD, 0xA50AB56B, 0x35B5A8FA, 0x42B2986C, 0xDBBBC9D6,
		0xACBCF940, 0x32D86CE3, 0x45DF5C75, 0xDCD60DCF, 0xABD13D59,
		0x26D930AC, 0x51DE003A, 0xC8D75180, 0xBFD06116, 0x21B4F4B5,
		0x56B3C423, 0xCFBA9599, 0xB8BDA50F, 0x2802B89E, 0x5F058808,
		0xC60CD9B2, 0xB10BE924, 0x2F6F7C87, 0x58684C11, 0xC1611DAB,
		0xB6662D3D,

		0x76DC4190, 0x01DB7106, 0x98D220BC, 0xEFD5102A, 0x71B18589,
		0x06B6B51F, 0x9FBFE4A5, 0xE8B8D433, 0x7807C9A2, 0x0F00F934,
		0x9609A88E, 0xE10E9818, 0x7F6A0DBB, 0x086D3D2D, 0x91646C97,
		0xE6635C01, 0x6B6B51F4, 0x1C6C6162, 0x856530D8, 0xF262004E,
		0x6C0695ED, 0x1B01A57B, 0x8208F4C1, 0xF50FC457, 0x65B0D9C6,
		0x12B7E950, 0x8BBEB8EA, 0xFCB9887C, 0x62DD1DDF, 0x15DA2D49,
		0x8CD37CF3, 0xFBD44C65, 0x4DB26158, 0x3AB551CE, 0xA3BC0074,
		0xD4BB30E2, 0x4ADFA541, 0x3DD895D7, 0xA4D1C46D, 0xD3D6F4FB,
		0x4369E96A, 0x346ED9FC, 0xAD678846, 0xDA60B8D0, 0x44042D73,
		0x33031DE5, 0xAA0A4C5F, 0xDD0D7CC9, 0x5005713C, 0x270241AA,
		0xBE0B1010, 0xC90C2086, 0x5768B525, 0x206F85B3, 0xB966D409,
		0xCE61E49F, 0x5EDEF90E, 0x29D9C998, 0xB0D09822, 0xC7D7A8B4,
		0x59B33D17, 0x2EB40D81, 0xB7BD5C3B, 0xC0BA6CAD,

		0xEDB88320, 0x9ABFB3B6, 0x03B6E20C, 0x74B1D29A, 0xEAD54739,
		0x9DD277AF, 0x04DB2615, 0x73DC1683, 0xE3630B12, 0x94643B84,
		0x0D6D6A3E, 0x7A6A5AA8, 0xE40ECF0B, 0x9309FF9D, 0x0A00AE27,
		0x7D079EB1, 0xF00F9344, 0x8708A3D2, 0x1E01F268, 0x6906C2FE,
		0xF762575D, 0x806567CB, 0x196C3671, 0x6E6B06E7, 0xFED41B76,
		0x89D32BE0, 0x10DA7A5A, 0x67DD4ACC, 0xF9B9DF6F, 0x8EBEEFF9,
		0x17B7BE43, 0x60B08ED5, 0xD6D6A3E8, 0xA1D1937E, 0x38D8C2C4,
		0x4FDFF252, 0xD1BB67F1, 0xA6BC5767, 0x3FB506DD, 0x48B2364B,
		0xD80D2BDA, 0xAF0A1B4C, 0x36034AF6, 0x41047A60, 0xDF60EFC3,
		0xA867DF55, 0x316E8EEF, 0x4669BE79, 0xCB61B38C, 0xBC66831A,
		0x256FD2A0, 0x5268E236, 0xCC0C7795, 0xBB0B4703, 0x220216B9,
		0x5505262F, 0xC5BA3BBE, 0xB2BD0B28, 0x2BB45A92, 0x5CB36A04,
		0xC2D7FFA7, 0xB5D0CF31, 0x2CD99E8B, 0x5BDEAE1D,

		0x9B64C2B0, 0xEC63F226, 0x756AA39C, 0x026D930A, 0x9C0906A9,
		0xEB0E363F, 0x72076785, 0x05005713, 0x95BF4A82, 0xE2B87A14,
		0x7BB12BAE, 0x0CB61B38, 0x92D28E9B, 0xE5D5BE0D, 0x7CDCEFB7,
		0x0BDBDF21, 0x86D3D2D4, 0xF1D4E242, 0x68DDB3F8, 0x1FDA836E,
		0x81BE16CD, 0xF6B9265B, 0x6FB077E1, 0x18B74777, 0x88085AE6,
		0xFF0F6A70, 0x66063BCA, 0x11010B5C, 0x8F659EFF, 0xF862AE69,
		0x616BFFD3, 0x166CCF45, 0xA00AE278, 0xD70DD2EE, 0x4E048354,
		0x3903B3C2, 0xA7672661, 0xD06016F7, 0x4969474D, 0x3E6E77DB,
		0xAED16A4A, 0xD9D65ADC, 0x40DF0B66, 0x37D83BF0, 0xA9BCAE53,
		0xDEBB9EC5, 0x47B2CF7F, 0x30B5FFE9, 0xBDBDF21C, 0xCABAC28A,
		0x53B39330, 0x24B4A3A6, 0xBAD03605, 0xCDD70693, 0x54DE5729,
		0x23D967BF, 0xB3667A2E, 0xC4614AB8, 0x5D681B02, 0x2A6F2B94,
		0xB40BBE37, 0xC30C8EA1, 0x5A05DF1B, 0x2D02EF8D, };
	
}
