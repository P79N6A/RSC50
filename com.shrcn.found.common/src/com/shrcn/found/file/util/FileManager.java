/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.file.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.internal.Utilities;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-5-20
 */
public class FileManager {

	/**
	 * 读文件
	 * 
	 * @param fileName
	 */
	public static String readFile(String fileName) {
		String s = null;
		FileReader fr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			// 读每一行
			while ((s = br.readLine()) != null) {
				sb.append(s + "\r\n"); //$NON-NLS-1$
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 写文件
	 * 
	 * @param content文件内容
	 * @param fileName文件名
	 */
	public static void writeFile(String content, String fileName) {
		FileWriter writer = null;
		PrintWriter pw = null;
		try {
			writer = new FileWriter(fileName);
			pw = new PrintWriter(writer);
			pw.println(content);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pw != null)
					pw.close();
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取文本输入流内容
	 * @param is
	 * @return
	 */
	public static String readTextFile(InputStream is) {
		String text = null;
		try {
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data, 0, size);
			text = new String(data);
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
		return text;
	}
	
	/**
	 * 读取指定的路径文件
	 * @param filePath
	 * @return
	 */
	public static String readTextFile(String filePath) {
		try {
			InputStream is = new FileInputStream(filePath);
			return readTextFile(is);
		} catch (FileNotFoundException e) {
			SCTLogger.error("文件未找到异常：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据字符集读取文件
	 * @param filePath
	 * @param charset
	 * @return
	 */
	public static String readFileByCharset(String filePath, String charset) {
		String text = null;
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
			text = Utilities.readString(is, charset);
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
		return text;
	}
	
	/**
	 * 按指定字符集保存字符串。
	 * 
	 * @param content
	 * @param path
	 * @param charset
	 */
	public static void saveTextFile(String path, String content, String charset) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(path), charset);
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
	 * 保存文本内容到指定文件
	 * @param content
	 * @param path
	 */
	public static boolean saveTextFile(String content, String path) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(path));
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
			return false;
		} finally {
			try {
				writer.close();
				return true;
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
		return false;
	}

	public static void saveInputStream(InputStream is, String target) {
		saveInputStream(is, new File(target));
	}
	
	/**
	 * 将输入流保存至指定文件
	 * @param is 输入流
	 * @param target 文件
	 */
	public static void saveInputStream(InputStream is, File target) {
		ReadableByteChannel chIn = Channels.newChannel(is);
		WritableByteChannel chOut = null;
		try {
			chOut = new FileOutputStream(target).getChannel( );
			ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		    while(chIn.read(buffer) != -1) {
		        buffer.flip( );
		        while (buffer.hasRemaining( )) {
		        	chOut.write(buffer);
		        }
		        buffer.clear( );
		    }
		} catch (FileNotFoundException e) {
			SCTLogger.error("未找到文件异常：" + e.getMessage());
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} finally {
			try {
				if(null != chIn)
					chIn.close();
				if(null != chOut)
					chOut.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
	}

	/**
	 * 转化编码为UTF8
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n, Constants.CHARSET_UTF8));
		}
		return out.toString();
	}
	
	/**
	 * 将ByteBuffer内容写到文件中.
	 * 
	 * @param buff
	 * @param path
	 */
	public static void writeToFile(ByteBuffer buff, String path) {
		File file = new File(path);
		FileOutputStream os = null;
		BufferedOutputStream bs = null;
		try {
			os = new FileOutputStream(file);
			bs = new BufferedOutputStream(os, 1024);
			int pos = buff.position();
			for(int i = 0;i<pos;i++) {
				bs.write(buff.get(i));
			}
			os.flush();
		} catch (FileNotFoundException e) {
			SCTLogger.error("未找到文件异常：" + e.getMessage());
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} finally {
			try {
				if (bs != null)
					bs.close();
				if (null != os)
					os.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
	}

	/**
	 * 读文件（单线程操作）
	 * 
	 * @param filename
	 * @return
	 */
	public static synchronized String readFileText(String filename) {
		File file = new File(filename);
		String temp = "";
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				if (tempString.equals(""))
					continue;
				temp = temp + tempString + "\r\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return temp;
	}
	
	/**
	 * 读取文本文件内容到字符串列表
	 * @param filename
	 * @return
	 */
	public static List<String> readLines(String filename) {
		File file = new File(filename);
		List<String> sx = new ArrayList<>();
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				if (tempString.equals(""))
					continue;
				sx.add(tempString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sx;
	}

	/**
	 * 写文件（单线程操作）
	 * 
	 * @param fileName
	 * @param content
	 * @return
	 */
	public static synchronized boolean writeFileString(String fileName,
			String content) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return true;
	}

	/**
	 * 按行读取文本文件，存入列表.
	 * 
	 * @param filename
	 * @return
	 */
	public static List<String> getFileList(String filename) {
		File file = new File(filename);
		if (!file.exists())
			return null;
		try {
			return getFileList(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 按行读取文本文件，存入列表.
	 * @param is
	 * @return
	 */
	public static List<String> getFileList(InputStream is) {
		if (is == null)
			return null;
		List<String> temp = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				if (tempString.equals(""))
					continue;
				temp.add(tempString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e1) {
			}
		}
		return temp;
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
	
	/**
	 * 读取文件内容数据
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileData(String fileName) throws IOException {
		byte[] b = null;

		FileInputStream fis = null;
		File f = null;
		try {
			f = new File(fileName);
			fis = new FileInputStream(f);
			b = new byte[(int) f.length()];

			int offset = 0;
			int numRead = 0;
			int numOfBytes = 1048576;// I choose this value randomally,
			// any other (not too big) value also can be here.
			if (b.length - offset < numOfBytes) {
				numOfBytes = b.length - offset;
			}
			while (offset < b.length
					&& (numRead = fis.read(b, offset, numOfBytes)) >= 0) {
				offset += numRead;
				if (b.length - offset < numOfBytes) {
					numOfBytes = b.length - offset;
				}
			}
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		return b;
	}
}
