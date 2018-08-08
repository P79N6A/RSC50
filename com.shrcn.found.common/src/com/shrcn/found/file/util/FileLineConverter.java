package com.shrcn.found.file.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.HashSet;

/**
 * 
 * @author zengyeye202359
 * @since 2017.2.9
 * 
 */
public class FileLineConverter {
	
	public static final String dosline 	= "\r\n";
	public static final String unixline = "\n";
	public static final String charset 	= "UTF-8";
	public static final String[] txtFiles = new String[] {"application/xml", "text/html"};
	public static final String[] ntxtFiles = new String[] {
		".jar", ".ico", ".db", ".png", ".jpg", 
		 ".otf", ".eot", ".ttf", ".woff", ".woff2",
		".psd", ".swf", ".exe"};
//	public static final String[] txtTypes = new String[] {".java", ".js", ".css", ".jsp", ".json", ".txt", ".html", ".md"};
	public static HashSet<String> cpTypes = new HashSet<>();
	
	public static void main(String args[]) throws IOException {
//		String inputDir = "E:/SHR_TOOLS/Source/Charger/gizwits-submit/20170123/sy-m2mservice-system"; // 源文件夹
//		String outputDir = "E:/SHR_TOOLS/Source/Charger/gizwits-submit/20170123/sy-m2mservice-system-dos/"; // 目标文件夹
//		String inputDir = "E:/SHR_TOOLS/Source/Charger/gizwits-submit/20170123/chargepile/chargepile";
//		String outputDir = "E:/SHR_TOOLS/Source/Charger/gizwits-submit/20170123/chargepile-dos/";
		String inputDir = "E:/SHR_TOOLS/Charger/gizwits-submit/20170427/sy-biz-20170427";
		unix2Dos(inputDir + "/chargepile", inputDir + "/chargepile-dos/");
//		String inputDir = "E:/SHR_TOOLS/Charger/gizwits-submit/20170418";
//		unix2Dos(inputDir + "/sy-m2m-20170418", inputDir + "/sy-m2m-20170418-dos/");
		for (String type : cpTypes) {
			System.out.println("复制类型：" + type);
		}
	}
	
	public static void unix2Dos(String inputDir, String outputDir) {
		convertDirectory(inputDir, outputDir, dosline);
	}
	
	public static void dos2Unix(String inputDir, String outputDir) {
		convertDirectory(inputDir, outputDir, unixline);
	}

	private static void convertDirectory(String inputDir, String outputDir, String sline) {
		File desfile = new File(outputDir);
		if (!desfile.exists()) {
			desfile.mkdirs();
		}
		File[] file = (new File(inputDir)).listFiles(); // 获取源文件夹当前下的文件或目录
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {// 复制目录
				String sourceDir = inputDir + File.separator + file[i].getName();
				String targetDir = outputDir + File.separator + file[i].getName();
				copyDirectiory(sourceDir, targetDir, sline);
			} else { // 复制文件
				copyFile(file[i], new File(outputDir + file[i].getName()), sline);
			}
		}
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile, String sline) {
		boolean isText = isTextFile(sourceFile);
//		if (isText) {
//			System.out.println("已转换：" + sourceFile.getAbsolutePath());
//		} else {
//			System.out.println("已复制：" + sourceFile.getAbsolutePath());
//		}
		copyNormalFile(sourceFile, targetFile, isText);
	}
	
	private static boolean isTextFile(File sourceFile) {
		String type = getFileType(sourceFile);
		if (type != null) {
			if (isIn(type, txtFiles))
				return true;
		} else {
			String fname = sourceFile.getName();
			if (!isIn(fname, ntxtFiles)) {
				return true;
			}
			type = "." + fname.substring(fname.lastIndexOf(".")+1);
		}
		cpTypes.add(type);
		return false;
	}

	private static boolean isIn(String type, String[] txtFiles) {
		for (String txt : txtFiles) {
			if (type.endsWith(txt)) {
				return true;
			}
		}
		return false;
	}

	private static String getFileType(File sourceFile) {
		InputStream is = null;
		String type = null;
		try {
			is = new BufferedInputStream(new FileInputStream(sourceFile));
			type = URLConnection.guessContentTypeFromStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return type;
	}

//	/**
//	 * 复制java文件,并处理换行符
//	 * 
//	 * @param srcFile
//	 * @param destFile
//	 */
//	private static void copyJavaFile(File srcFile, File destFile, String sline) {
//		BufferedReader br = null;
//		PrintWriter pw = null;
//		try {
//			br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), charset));
//			pw = new PrintWriter(destFile, charset);
//			String line = br.readLine();
//			while (null != line) {
//				String tline = new String(line);
//				line = br.readLine();
//				if (null != line) {
//					pw.print(tline + sline);
//				} else {
//					if (!"".equals(tline)) {
//						pw.print(tline);
//					}
////					System.out.println("最后一行");
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (br != null) {
//					br.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (pw != null) {
//					pw.close();
//				}
//			}
//		}
//
//	}

	/**
	 * 复制普通文件
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyNormalFile(File sourceFile, File targetFile, boolean isTxt) {
		InputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			if (isTxt) {
				ByteBuffer b = ByteBuffer.allocate(1024 * 1024 * 10);
				byte c = (byte) inBuff.read();
				while (c != -1) {
					if (c != '\r') {
						if (c == '\n') {
							b.put((byte) '\r');
						}
						b.put(c);
					}
					c = (byte) inBuff.read();
				}
				int len = b.position();
				b.rewind();
				byte[] bs = new byte[len];
				b.get(bs);
				outBuff.write(bs, 0, len);
			} else {
				FileManipulate.copyByChannel(sourceFile, targetFile);
			}
			outBuff.flush();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 复制文件夹
	public static void copyDirectiory(String sourceDir, String targetDir, String sline) {
		new File(targetDir).mkdirs();
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				File sourceFile = file[i];
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				copyFile(sourceFile, targetFile, sline);// 递归调用
			}
			if (file[i].isDirectory()) {
				String dir1 = sourceDir + "/" + file[i].getName();
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2, sline);
			}
		}
	}
}
