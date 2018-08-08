package com.shrcn.found.common.jvm.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class HackMain {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
		InputStream is = new FileInputStream("c:/TestClass.class");
		byte[] b = new byte[is.available()];
		is.read(b);
		is.close();
		
		System.out.println(JavaClassExecuter.execute(b));
	}

}
