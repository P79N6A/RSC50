package com.shrcn.found.file.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 计算处理文件的CRC32码
 * @author xiaohuang
 * @since 2012.2.2 初次构建
 */
public class CRC32File {
	
	private String filepath;
	private int crcValue;
	private boolean crcTailed;
	private boolean fileLoaded;
	
	/**
	 * 构造系统文件CRC检验类，如果发生文件错误将抛出异常
	 * @param filename 要处理的文件路径
	 * @throws IOException 发生文件错误将抛出异常
	 */
	public CRC32File(String filename) throws IOException {
		
		filepath = filename;
		crcValue = 0;
		crcTailed = false;
		fileLoaded = false;

		loadFile();
	}
	
	/**
	 * 加载文件，如果文件还没有加载过，可用于延迟加载文件
	 * @throws IOException
	 */
	private void loadFile() throws IOException {
		if(fileLoaded) {
			return;
		}

		fileLoaded = true;
		InputStream inputStream = null;
		try {
			File file = new File(filepath);
			long size = file.length() - 4;
			
			inputStream = new BufferedInputStream(new FileInputStream(file));
			CRC32 crc32 = new CRC32();

			if(size > 0) {
				long bytes = 0;
				for(int val = inputStream.read(); val >=0; val = inputStream.read()) {
					crc32.update(val);
					if(++bytes >= size) {
						break;
					}
				}
				
				int crcVal = crc32.getValue();
				byte[] tail = new byte[8];
				int ret = inputStream.read(tail);
				if(ret != 4) {
					throw new IOException("File read error, invaild file size " + (size+4) + "(" + (size+ret) + ") --- by Hxc");
				}
				int tailVal = 0;
				tailVal |= (tail[0] << 24);
				tailVal |= (tail[1] << 16);
				tailVal |= (tail[2] << 8);
				tailVal |= tail[3];
				if(crcVal == tailVal) {
					crcTailed = true;
				}
				else {
					crc32.update(tail[0]);
					crc32.update(tail[1]);
					crc32.update(tail[2]);
					crc32.update(tail[3]);
				}
				crcValue = crc32.getValue();
			}
			else {
				for(int val = inputStream.read(); val >=0; val = inputStream.read()) {
					crc32.update(val);
				}
			}
			crcValue = crc32.getValue();			
		} 
		finally {
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取文件实际的CRC值，将检测到文件是否附加了CRC校验
	 */
	public int getCrcValue() {
		return crcValue;
	}
	/**
	 * 检测文件是否在尾部附加了CRC校验
	 * @return
	 */
	public boolean isCrcTailed() {
		return crcTailed;
	}
	/**
	 * 在文件尾部附加CRC数据，如果文件已经附加了校验，将不会进行操作
	 * @throws IOException 操作失败抛出异常
	 */
	public void appendTailCrc() throws IOException {
		if(!crcTailed) appendTailCrc(filepath, crcValue);
	}
	/**
	 * 检查文件末尾CRC16是否正确，如果不正确将在文件末尾添加4个字节CRC16结果码
	 * @param filename 文件路径
	 * @return CRC验证通过,不用再添加CRC返回false；成功添加CRC返回true
	 * @throws IOException 操作失败将抛出异常
	 */
	static public boolean appendTailCrc(String filename) throws IOException {
		CRC32File file = new CRC32File(filename);
		if(file.isCrcTailed()) {
			return false;
		} else {
			file.appendTailCrc();
			return true;
		}
	}
	/**
	 * 检查文件末尾的4字节CRC16码是否正确
	 * @param filename 文件路径
	 * @return CRC验证通过返回true；否则返回true
	 * @throws IOException 操作失败将抛出异常
	 */
	static public boolean checkTailCrc(String filename) throws IOException {
		return new CRC32File(filename).isCrcTailed();
	}
	/**
	 * 计算文件的CRC16，如果文件末尾已经附加4字节CRC，将直接返回
	 * @param filename 文件路径
	 * @return 返回CRC16码
	 * @throws IOException 文件操作失败将抛出异常
	 */
	static public int calcTailCrc(String filename) throws IOException {
		return new CRC32File(filename).getCrcValue();
	}
	/**
	 * 计算文件的CRC16，将对文件整个内容进行计算
	 * @param filename 文件路径
	 * @return 返回CRC16码
	 * @throws IOException 文件操作失败将抛出异常
	 */
	static public int calcFileCrc(String filename) throws IOException {
		return calcFileCrc(filename, 0);
	}
	
	/**
	 * 计算文件的CRC16，将对文件整个内容进行计算，接收CRC初值，一般用于多文件计算CRC
	 * @param filename 文件路径
	 * @param crc CRC的初值
	 * @return 返回CRC16码
	 * @throws IOException 文件操作失败将抛出异常
	 */
	static public int calcFileCrc(String filename, int crc) throws IOException {
		int crcVal = 0;
		InputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(new FileInputStream(filename));
			CRC32 crc32 = new CRC32(crc);

			byte[] buffer = new byte[1024];
			for (int len = inputStream.read(buffer); len > 0; len = inputStream.read(buffer)) {
				crc32.update(buffer, 0, len);
			}

			crcVal = crc32.getValue();
		} 
		finally {
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return crcVal;
	}
	/** 
	 * 向指定文件末尾添加4字节的CRC值，不会进行任何计算与校验
	 * @param filename 文件路径
	 * @param crc 要增加的CRC值
	 * @throws IOException 操作失败返回异常
	 */
	static public void appendTailCrc(String filename, int crc) throws IOException {
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(filename, "rw");
			long len = file.length();
			file.setLength(len+4);
			file.seek(len);
			file.write((crc >>> 24) & 0xff);
			file.write((crc >>> 16) & 0xff);
			file.write((crc >>> 8) & 0xff);
			file.write(crc & 0xff);
		} finally {
			try {
				if(file != null) {
					file.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
