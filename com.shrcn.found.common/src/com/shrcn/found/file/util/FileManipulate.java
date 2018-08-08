/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */

package com.shrcn.found.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ByteUtil;
import com.shrcn.found.common.util.StringUtil;

/**
 * @author 黄钦辉
 * @version 1.0, 2008-8-22
 */
public class FileManipulate {

	private FileManipulate() {
	}

	/**
	 * 根据path初始化一个指定的目录
	 * @param strDir
	 */
	public static void initDir(String strDir) {
		File fDir = new File(strDir);
		if(!fDir.exists()) {
			fDir.mkdirs();
		} else {
			if(!fDir.isDirectory()) {
				fDir.delete();
				fDir.mkdir();
			}
		}
	}
	
	public static void initDirs(String...strDirs) {
		for (String strDir : strDirs) {
			initDir(strDir);
		}
	}
	
	/**
	 * 清空目录文件
	 * @param strDir
	 */
	public static void clearDir(String strDir) {
		File fDir = new File(strDir);
		if(fDir.isDirectory()) {
			File[] files = fDir.listFiles();
			for(File f:files) {
				f.deleteOnExit();
			}
		}
	}
	
	public static void clearDirNow(String strDir) {
		File fDir = new File(strDir);
		if(fDir.isDirectory()) {
			File[] files = fDir.listFiles();
			for (File f : files) {
				f.delete();
			}
		}
	}

	/**
	 * 重新命名文件
	 * 
	 * @param oldPath
	 *            旧文件路径
	 * @param newPath
	 *            新文件路径
	 */
	public static void renameFile(String oldPath, String newPath) {
		if (oldPath.equals(newPath)) {
			return;
		}

		File file = new File(oldPath);
		File file2 = new File(newPath);

		// 重命名文件
		file.renameTo(file2);
		String childs[] = file.list();
		if (childs == null || childs.length <= 0) {
			file.delete();
			return;
		}

		for (int i = 0; i < childs.length; i++) {
			String childName = childs[i];
			// 改变文件目录
			changeDirectory(childName, oldPath, newPath, true);
		}
		deleteFiles(file.getPath());
	}

	/**
	 * 改变文件目录
	 * 
	 * @param filename
	 * @param oldpath
	 * @param newpath
	 * @param cover
	 */
	public static void changeDirectory(String filename, String oldpath,
			String newpath, boolean cover) {
		if (!oldpath.equals(newpath)) {
			File oldfile = new File(oldpath + File.separator + filename);
			File newfile = new File(newpath + File.separator + filename);

			if (oldfile.exists() && oldfile.isFile()) {
				if (newfile.exists()) {
					// 若在待转移目录下，已经存在待转移文件
					if (cover) {
						// 删除已存在的文件
						newfile.delete();
						// 覆盖
						oldfile.renameTo(newfile);
					} else {
						System.out.println(Messages.getString("FileManipulate.lable_file_exists") + filename); //$NON-NLS-1$
					}
				} else {
					oldfile.renameTo(newfile);
				}
			} else if (oldfile.exists() && oldfile.isDirectory()) {
				newfile.mkdir();

				String childs[] = oldfile.list();

				for (int i = 0; i < childs.length; i++) {
					String childName = childs[i];

					// 改变文件目录
					changeDirectory(childName, oldfile.getPath(),
							newfile.getPath(), true);
				}
			}
		}
	}

	/**
	 * 删除装置文件
	 * 
	 * @param folderPath
	 */
	public static void deleteFiles(String folderPath) {
		File folder = new File(folderPath);
		String[] childs = folder.list();
		if (childs == null || childs.length <= 0) {
			folder.delete();
			return;
		}
		for (int i = 0; i < childs.length; i++) {
			String childName = childs[i];
			String childPath = folder.getPath() + File.separator + childName;
			File filePath = new File(childPath);
			if (filePath.exists() && filePath.isFile()) {
				filePath.delete();
			} else if (filePath.exists() && filePath.isDirectory()) {
				deleteFiles(childPath);
			}
		}

		folder.delete();
	}

	/**
	 * 删除文件,如果是文件直接删除，如果是目录，删除目录内所有文件,但不删除目录
	 */
	public static void deleteFile(String fileName) {
		deleteFile(fileName, false);
	}

	/**
	 * 删除文件,如果是文件直接删除，如果是目录，删除目录内所有文件,如果要删除目录，deleteDir设为true
	 */
	public static void deleteFile(String fileName, boolean deleteDir) {
		File f = new File(fileName);
		if (f.isDirectory()) {
			for (File list : f.listFiles()) {
				deleteFile(list.getPath(), deleteDir);
			}
			if (deleteDir) {
				f.delete();
			}
		} else if (f.isFile()) {
			if (f.exists()) {
				f.delete();
			}
		}
	}

	/**
	 * 文件文件夹拷贝
	 * 
	 * @param sourceDir
	 *            原文件夹
	 * @param targetDir
	 *            目标文件夹
	 * @throws IOException
	 */

	public static void copyDir(String sourceDir, String targetDir)
			throws IOException {
		File sourceFile = new File(sourceDir);
		if (!sourceFile.exists()) {
			throw new FileNotFoundException(sourceDir
					+ Messages.getString("FileManipulate.notFound_folder")); //$NON-NLS-1$
		}
		File targetFile = new File(targetDir);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		for (File file : sourceFile.listFiles()) {
			if (file.isFile()) {
				copyByChannel(new File(sourceDir + File.separator
						+ file.getName()), new File(targetDir + File.separator
						+ file.getName()));
			} else if (file.isDirectory()) {
				copyDir(sourceDir + File.separator + file.getName(), targetDir
						+ File.separator + file.getName());
			}
		}
	}

	public static void copyByChannel(String source, String target) {
		copyByChannel(new File(source), new File(target));
	}

	/**
	 * 利用管道方式复制文件
	 * 
	 * @param source
	 *            源文件
	 * @param target
	 *            目标文件
	 */
	public static void copyByChannel(File source, File target) {
		FileChannel chIn = null;
		FileChannel chOut = null;
		try {
			chIn = new FileInputStream(source).getChannel();
			chOut = new FileOutputStream(target).getChannel();
			chIn.transferTo(0, chIn.size(), chOut);
		} catch (FileNotFoundException e) {
			SCTLogger.error("未找到文件异常：" + e.getMessage());
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} finally {
			try {
				if (null != chIn)
					chIn.close();
				if (null != chOut)
					chOut.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
	}

	/**
	 * 复制文件夹
	 */
	public static void copyDirectiory(String sourceDir, String targetDir) {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyByChannel(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}
	
	/**
	 * 判断文件是否存在
	 * 
	 * @param f
	 * @return
	 */
	public static boolean exist(String f) {
		return new File(f).exists();
	}

	
	/**
	 * 添加文本内容到指定文件
	 * 
	 * @param content
	 * @param path
	 */
	public static void appentTextToFile(String content, String path) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(path), true);
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
	}

	/**
	 * 合并文件。
	 * 
	 * @param source
	 * @param target
	 */
	public static void mergeFiles(String source, String target) {
		String content = FileManager.readTextFile(source);
		appentTextToFile(content, target);
	}

	/**
	 * 将输入流输出到指定输出流
	 * 
	 * @param is
	 * @param os
	 */
	public static void copyTo(InputStream is, OutputStream os, boolean close) {
		try {
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data, 0, size);
			os.write(data);
			os.flush();
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} finally {
			try {
				if (close && null != is)
					is.close();
				if (null != os)
					os.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
	}

	public static void copyTo(InputStream is, OutputStream os) {
		copyTo(is, os, true);
	}

	/**
	 * 保存资源
	 * 
	 * @param srcDir
	 *            源目录
	 * @param targetDir
	 *            目标目录
	 * @param rsrcName
	 *            资源名
	 */
	public static void copyResource(Class<?> baseClass, String srcDir,
			String targetDir, String rsrcName) {
		URL url = getResourceURL(baseClass, srcDir, rsrcName);
		if (null == url)
			return;
		InputStream in = null;
		OutputStream out = null;
		try {
			in = url.openStream();
			out = new FileOutputStream(targetDir + File.separator + rsrcName);
			copyTo(in, out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in)
					in.close();
				if (null != out)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	public static String getFileCRCCode(File file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		return getStreamCRCCode(fileInputStream);
	}

	/**
	 * 得到CRC码
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String getStreamCRCCode(InputStream input) throws IOException {
		CRC32 crc32 = new CRC32();
		for (CheckedInputStream checkedinputstream = new CheckedInputStream(
				input, crc32); checkedinputstream.read() != -1;)
			;
		if (input != null) {
			input.close();
			input = null;
		}
		return Long.toHexString(crc32.getValue()).toUpperCase();
	}

	public static String getFileCRCCode(String file) throws IOException {
		return getFileCRCCode(new File(file));
	}

	public static String getFileCRCCode(String[] files) throws IOException {
		CRC32 crc32 = new CRC32();
		FileInputStream fileInputStream = null;
		for (int i = 0; i < files.length; i++) {
			File file = new File(files[i]);
			fileInputStream = new FileInputStream(file);
			for (CheckedInputStream checkedinputstream = new CheckedInputStream(
					fileInputStream, crc32); checkedinputstream.read() != -1;)
				;

		}
		fileInputStream.close();
		return Long.toString(crc32.getValue());
	}

	/**
	 * 得到清除空格后的CRC校验码
	 * 
	 * @param str
	 * @return
	 */
	public static String getCRC32Clean(String str) {
		StringUtil.replaceBlank(str);
		return getCRC32(str);
	}
	
	private static void init() {
		if (crctable==null)
			crctable = new int[256];
		int crc, i, j;
		int poly = 0xEDB88320;
		for (i = 0; i < 256; i++) {
			crc = i;
			for (j = 8; j > 0; j--) {
				if ((crc & 1) > 0)
					crc = (crc >>> 1) ^ poly;
				else
					crc >>>= 1;
			}
			crctable[(int) i] = crc;
		}
	}
	
	/**
	 * 得到CRC校验码
	 * 
	 * @param str
	 * @return
	 */
	public static String getCRC32(String str) {
		if (crctable == null) {
			init();
		}
		byte[] bytes = null;
		try {
			bytes = str.getBytes(Constants.CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int crc = 0xffffffff;
		for (byte b : bytes) {
			crc = (crc >>> 8) ^ crctable[((b ^ crc) & 0xff)];
		}
		crc = crc ^ 0xffffffff;
		String crcCode = Integer.toHexString(crc).toUpperCase();
		
		int less = 8 - crcCode.length();
		if (less > 0) {
			for (int i=0; i<less; i++) {
				crcCode = "0" + crcCode;
			}
		}
		return crcCode;
	}

	/**
	 * 获取资源URL
	 * 
	 * @param dir
	 * @param file
	 * @return
	 */
	public static URL getResourceURL(Class<?> baseClass, String dir, String file) {
		URL url = null;
		if (dir.endsWith("/")) {
			file = dir + file;
			url = baseClass.getResource(file);
		} else {
			file = dir + "/" + file;
			try {
				url = new File(file).toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 * 
	 * @param dir
	 *            directory
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * 根据指定路径删除目录
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(String path) {
		return deleteDir(new File(path));
	}


	/**
	 * 获取指定文件CRC码
	 * 
	 * @param path
	 * @return
	 */
	public static String getCRC32Code(String path) {
		File file = new File(path);
		FileInputStream fileinputstream = null;
		CRC32 crc32 = null;
		try {
			fileinputstream = new FileInputStream(file);
			crc32 = new CRC32();
			for (CheckedInputStream checkedinputstream = new CheckedInputStream(
					fileinputstream, crc32); checkedinputstream.read() != -1;) {
			}
		} catch (FileNotFoundException e) {
			SCTLogger.error("未找到文件" + path + "。", e);
		} catch (IOException e) {
			SCTLogger.error("IO异常" + path + "。", e);
		} finally {
			try {
				if (fileinputstream != null)
					fileinputstream.close();
			} catch (IOException e) {
				SCTLogger.error("IO异常" + path + "。", e);
			}
		}
		
		String crcCode = Long.toHexString(crc32.getValue()).toUpperCase();
		
		int less = 8 - crcCode.length();
		if (less > 0) {
			for (int i=0; i<less; i++) {
				crcCode = "0" + crcCode;
			}
		}
		return crcCode;
	}

	/**
	 * 获取指定文件MD5码
	 * 
	 * @param path
	 * @return
	 */
	public static String getMD5Code(String path) {
		File file = new File(path);
		FileInputStream fileinputstream = null;
		String fileMD5 = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			fileinputstream = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fileinputstream.read(buffer)) > 0) {
				digest.update(buffer, 0, numRead);
			}
			byte[] md5sum = digest.digest();
			fileMD5 = ByteUtil.bytes2HexString(md5sum);
		} catch (FileNotFoundException e) {
			SCTLogger.error("未找到文件" + path + "。", e);
		} catch (NoSuchAlgorithmException e) {
			SCTLogger.error("初始化失败，MessageDigest不支持MD5", e);
		} catch (IOException e) {
			SCTLogger.error("IO异常" + path + "。", e);
		} finally {
			try {
				if (fileinputstream != null)
					fileinputstream.close();
			} catch (IOException e) {
				SCTLogger.error("IO异常" + path + "。", e);
			}
		}
		return fileMD5;
	}
	
	/**
	 * 获取指定字符串MD5码
	 * @param str
	 * @return
	 */
	public static String getMD5CodeForStr(String str) {
		String md5 = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			byte[] md5sum = digest.digest();
			md5 = ByteUtil.bytes2HexString(md5sum);
		} catch (NoSuchAlgorithmException e) {
			SCTLogger.error("初始化失败，MessageDigest不支持MD5", e);
		}
		return md5;
	}

	/**
	 * 未指定文件添加后缀
	 * 
	 * @param path
	 * @param suffix
	 * @return
	 */
	public static String addSuffix(String fPath, String suffix) {
		File f = new File(fPath);
		if (!f.exists())
			return null;
		String oldName = f.getName();
		String extend = oldName.substring(oldName.lastIndexOf('.'));
		String newName = oldName.substring(0, oldName.lastIndexOf('.')) + "_"
				+ suffix + extend;
		return f.getParent() + File.separator + newName;
	}

	/**
	 * 查找指定目录下特定后缀的文件。
	 * 
	 * @param dirpath
	 * @param endfix
	 * @return
	 */
	public static List<File> findFiles(String dirpath, String endfix) {
		List<File> files = new ArrayList<File>();
		File dir = new File(dirpath);
		if (!dir.exists())
			return files;
		findFiles(dir, endfix, files);
		return files;
	}

	private static void findFiles(File file, String endfix, List<File> files) {
		if (file.isFile()) {
			if (file.getName().endsWith(endfix))
				files.add(file);
		} else {
			File[] subfiles = file.listFiles();
			for (File subfile : subfiles) {
				findFiles(subfile, endfix, files);
			}
		}
	}
	
	/**
	 * 得到指定目录下所有文件路径
	 * @param dirpath
	 * @return
	 */
	public static String[] getSubFileNames(String dirpath) {
		List<String> names = new ArrayList<String>();
		List<File> files = getSubFiles(dirpath);
		for (File file : files) {
			names.add(file.getAbsolutePath());
		}
		return names.toArray(new String[names.size()]);
	}
	
	/**
	 * 得到指定目录下所有文件
	 * @param dirpath
	 * @return
	 */
	public static List<File> getSubFiles(String dirpath) {
		List<File> files = new ArrayList<File>();
		File dir = new File(dirpath);
		if (!dir.exists())
			return files;
		getSubFiles(dir, files);
		return files;
	}

	private static void getSubFiles(File file, List<File> files) {
		if (file.isFile()) {
			files.add(file);
		} else {
			File[] subfiles = file.listFiles();
			for (File subfile : subfiles) {
				getSubFiles(subfile, files);
			}
		}
	}

	/**
	 * 得到文件名（不带后缀）。
	 * 
	 * @param openfile
	 * @return
	 */
	public static String getName(String openfile) {
		File openFile = new File(openfile);
		String openFName = openFile.getName();
		int p = openFName.lastIndexOf(".");
		return (p > 0) ? openFName.substring(0, p) : openFName;
	}

	/**
	 * 根据路径得到文件名。
	 * 
	 * @param pathName
	 * @return
	 */
	public static String getFileName(String pathName) {
		if (!StringUtil.isEmpty(pathName)) {
			int index = pathName.lastIndexOf(File.separator);
			if (index > -1)
				pathName = pathName.substring(index + 1);
			return pathName;
		}
		return null;
	}

	/**
	 * 获取系统默认文件路径。
	 * @param clazz
	 * @param classpath
	 * @return
	 */
	public static String getConfigFile(Class<?> clazz, String classpath) {
		String filePath = Constants.cfgDir + 
			classpath.substring(classpath.lastIndexOf('/'));
		File file = new File(filePath);
		if (!file.exists()) {
			InputStream is = clazz.getClassLoader().getResourceAsStream(classpath);
			try {
				copyTo(is, new FileOutputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}
	
	public static int[] crctable = null;
}
