/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package org.sf.feeling.swt.win32.extension.util;

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
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
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
			e.printStackTrace();
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
			text = readString(is, charset);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}
	
	/*
	 * Returns null if an error occurred.
	 */
	public static String readString(InputStream is, String encoding) throws IOException {
		return readString(is, encoding, -1);
	}

	public static String readString(InputStream is, String encoding, int length) throws IOException {
		if (is == null)
			return null;
		BufferedReader reader= null;
		try {
			StringBuffer buffer= new StringBuffer();
			char[] part= new char[2048];
			int read= 0;
			reader= new BufferedReader(new InputStreamReader(is, encoding));
			while ((read= reader.read(part)) != -1) {
				buffer.append(part, 0, read);
			}
			return buffer.toString();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
					// silently ignored
				}
			}
		}
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
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
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
			e.printStackTrace();
			return false;
		} finally {
			try {
				writer.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != chIn)
					chIn.close();
				if(null != chOut)
					chOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bs != null)
					bs.close();
				if (null != os)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
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
	 * 写文件（单线程操作）
	 * 
	 * @param fileName
	 * @param content
	 * @return
	 */
	public static synchronized boolean writeFileSting(String fileName,
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
	 * 得到CRC校验码
	 * 
	 * @param str
	 * @return
	 */
	public static String getCRC32(String str) {
		int[] table = { 0x00000000, 0x77073096, 0xee0e612c, 0x990951ba,
				0x076dc419, 0x706af48f, 0xe963a535, 0x9e6495a3, 0x0edb8832,
				0x79dcb8a4, 0xe0d5e91e, 0x97d2d988, 0x09b64c2b, 0x7eb17cbd,
				0xe7b82d07, 0x90bf1d91, 0x1db71064, 0x6ab020f2, 0xf3b97148,
				0x84be41de, 0x1adad47d, 0x6ddde4eb, 0xf4d4b551, 0x83d385c7,
				0x136c9856, 0x646ba8c0, 0xfd62f97a, 0x8a65c9ec, 0x14015c4f,
				0x63066cd9, 0xfa0f3d63, 0x8d080df5, 0x3b6e20c8, 0x4c69105e,
				0xd56041e4, 0xa2677172, 0x3c03e4d1, 0x4b04d447, 0xd20d85fd,
				0xa50ab56b, 0x35b5a8fa, 0x42b2986c, 0xdbbbc9d6, 0xacbcf940,
				0x32d86ce3, 0x45df5c75, 0xdcd60dcf, 0xabd13d59, 0x26d930ac,
				0x51de003a, 0xc8d75180, 0xbfd06116, 0x21b4f4b5, 0x56b3c423,
				0xcfba9599, 0xb8bda50f, 0x2802b89e, 0x5f058808, 0xc60cd9b2,
				0xb10be924, 0x2f6f7c87, 0x58684c11, 0xc1611dab, 0xb6662d3d,
				0x76dc4190, 0x01db7106, 0x98d220bc, 0xefd5102a, 0x71b18589,
				0x06b6b51f, 0x9fbfe4a5, 0xe8b8d433, 0x7807c9a2, 0x0f00f934,
				0x9609a88e, 0xe10e9818, 0x7f6a0dbb, 0x086d3d2d, 0x91646c97,
				0xe6635c01, 0x6b6b51f4, 0x1c6c6162, 0x856530d8, 0xf262004e,
				0x6c0695ed, 0x1b01a57b, 0x8208f4c1, 0xf50fc457, 0x65b0d9c6,
				0x12b7e950, 0x8bbeb8ea, 0xfcb9887c, 0x62dd1ddf, 0x15da2d49,
				0x8cd37cf3, 0xfbd44c65, 0x4db26158, 0x3ab551ce, 0xa3bc0074,
				0xd4bb30e2, 0x4adfa541, 0x3dd895d7, 0xa4d1c46d, 0xd3d6f4fb,
				0x4369e96a, 0x346ed9fc, 0xad678846, 0xda60b8d0, 0x44042d73,
				0x33031de5, 0xaa0a4c5f, 0xdd0d7cc9, 0x5005713c, 0x270241aa,
				0xbe0b1010, 0xc90c2086, 0x5768b525, 0x206f85b3, 0xb966d409,
				0xce61e49f, 0x5edef90e, 0x29d9c998, 0xb0d09822, 0xc7d7a8b4,
				0x59b33d17, 0x2eb40d81, 0xb7bd5c3b, 0xc0ba6cad, 0xedb88320,
				0x9abfb3b6, 0x03b6e20c, 0x74b1d29a, 0xead54739, 0x9dd277af,
				0x04db2615, 0x73dc1683, 0xe3630b12, 0x94643b84, 0x0d6d6a3e,
				0x7a6a5aa8, 0xe40ecf0b, 0x9309ff9d, 0x0a00ae27, 0x7d079eb1,
				0xf00f9344, 0x8708a3d2, 0x1e01f268, 0x6906c2fe, 0xf762575d,
				0x806567cb, 0x196c3671, 0x6e6b06e7, 0xfed41b76, 0x89d32be0,
				0x10da7a5a, 0x67dd4acc, 0xf9b9df6f, 0x8ebeeff9, 0x17b7be43,
				0x60b08ed5, 0xd6d6a3e8, 0xa1d1937e, 0x38d8c2c4, 0x4fdff252,
				0xd1bb67f1, 0xa6bc5767, 0x3fb506dd, 0x48b2364b, 0xd80d2bda,
				0xaf0a1b4c, 0x36034af6, 0x41047a60, 0xdf60efc3, 0xa867df55,
				0x316e8eef, 0x4669be79, 0xcb61b38c, 0xbc66831a, 0x256fd2a0,
				0x5268e236, 0xcc0c7795, 0xbb0b4703, 0x220216b9, 0x5505262f,
				0xc5ba3bbe, 0xb2bd0b28, 0x2bb45a92, 0x5cb36a04, 0xc2d7ffa7,
				0xb5d0cf31, 0x2cd99e8b, 0x5bdeae1d, 0x9b64c2b0, 0xec63f226,
				0x756aa39c, 0x026d930a, 0x9c0906a9, 0xeb0e363f, 0x72076785,
				0x05005713, 0x95bf4a82, 0xe2b87a14, 0x7bb12bae, 0x0cb61b38,
				0x92d28e9b, 0xe5d5be0d, 0x7cdcefb7, 0x0bdbdf21, 0x86d3d2d4,
				0xf1d4e242, 0x68ddb3f8, 0x1fda836e, 0x81be16cd, 0xf6b9265b,
				0x6fb077e1, 0x18b74777, 0x88085ae6, 0xff0f6a70, 0x66063bca,
				0x11010b5c, 0x8f659eff, 0xf862ae69, 0x616bffd3, 0x166ccf45,
				0xa00ae278, 0xd70dd2ee, 0x4e048354, 0x3903b3c2, 0xa7672661,
				0xd06016f7, 0x4969474d, 0x3e6e77db, 0xaed16a4a, 0xd9d65adc,
				0x40df0b66, 0x37d83bf0, 0xa9bcae53, 0xdebb9ec5, 0x47b2cf7f,
				0x30b5ffe9, 0xbdbdf21c, 0xcabac28a, 0x53b39330, 0x24b4a3a6,
				0xbad03605, 0xcdd70693, 0x54de5729, 0x23d967bf, 0xb3667a2e,
				0xc4614ab8, 0x5d681b02, 0x2a6f2b94, 0xb40bbe37, 0xc30c8ea1,
				0x5a05df1b, 0x2d02ef8d, };
		byte[] bytes = str.getBytes();
		int crc = 0xffffffff;
		for (byte b : bytes) {
			crc = (crc >>> 8 ^ table[(crc ^ b) & 0xff]);
		}
		crc = crc ^ 0xffffffff;
		return Integer.toHexString(crc).toUpperCase();
	}
}
