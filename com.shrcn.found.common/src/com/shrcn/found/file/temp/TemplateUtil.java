package com.shrcn.found.file.temp;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;

public class TemplateUtil {
	
	public static final String LIST_NAME = "list";
	public static final String SPLIT = "_";
	
	/**
	 * 将指定的配置信息用模板的方式导出到指定文件夹中。
	 * 
	 * @param list
	 * @param targetDir
	 * @param group
	 * @param fileName
	 * @param clear
	 * @param tempPath
	 */
	public static void evaluateList(Collection<?> list, String targetDir, String group, String fileName, boolean clear, String tempPath) {
		VelocityContext context = new VelocityContext();
		context.put(LIST_NAME, list);
		evaluateContext(context, targetDir, group, fileName, clear, tempPath);
		if (list != null && clear)
			list.clear();
	}
	
	/**
	 * 按指定上下文用模板的方式导出到指定文件夹中。
	 * 
	 * @param context
	 * @param targetDir
	 * @param group
	 * @param fileName
	 * @param clear
	 * @param tempPath
	 */
	public static void evaluateContext(VelocityContext context, String targetDir, String group, String fileName, boolean clear, String tempPath) {
		group = group.contains(SPLIT) ? group.split(SPLIT)[0] : group;
		String template = getTemplateText(tempPath + group + "/" + fileName + ".eq");
		if (null == template)
			return;
		File outfile = new File(targetDir + "/" + fileName + ".xml");
		OutputStreamWriter w = null;
		try {
			if (!outfile.exists())
				outfile.createNewFile();
			w = new OutputStreamWriter(new FileOutputStream(outfile), Constants.CHARSET_UTF8);
			Velocity.evaluate(context, w, TemplateUtil.class.getName(), template);
		} catch (ParseErrorException pee) {
			SCTLogger.error("", pee);
		} catch (MethodInvocationException mee) {
			SCTLogger.error("", mee);
		} catch (IOException e) {
			SCTLogger.error("", e);
		} finally {
			try {
				if (w != null) {
					w.flush();
					w.close();
				}
			} catch (IOException e) {
				SCTLogger.error("", e);
			}
		}
	}
	
	/**
	 * 转换字符串模板
	 * @param context
	 * @param template
	 * @return
	 */
	public static String evaluateContext(VelocityContext context, String template) {
		CharArrayWriter w = null;
		String result = "";
		try {
			w = new CharArrayWriter();
			Velocity.evaluate(context, w, TemplateUtil.class.getName(), template);
			result = w.toString();
		} catch (ParseErrorException pee) {
			SCTLogger.error("", pee);
		} catch (MethodInvocationException mee) {
			SCTLogger.error("", mee);
		} catch (IOException e) {
			SCTLogger.error("", e);
		} finally {
			if (w != null) {
				w.flush();
				w.close();
			}
		}
		return result;
	}
	
	/**
	 * 得到模板内容。
	 * @param name
	 * @return
	 */
	private static String getTemplateText(String name) {
		return TempFileManager.readTextFile(TemplateUtil.class, name);
	}
}
