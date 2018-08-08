/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.compare.BufferedContent;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IModificationDate;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-24
 */
/**
 * $Log: StringCompareItem.java,v $
 * Revision 1.1  2013/03/29 09:37:56  cchun
 * Add:创建
 *
 * Revision 1.2  2011/09/20 08:42:07  cchun
 * Update:清理注释
 *
 * Revision 1.1  2011/07/27 07:18:37  cchun
 * Add:比较对话框类
 *
 */
public class StringCompareItem extends BufferedContent implements ITypedElement, IModificationDate, IEditableContent {   
    private String content;   
    private long time;   
  
    public StringCompareItem(String content) {   
        this.content = content;   
        this.time = System.currentTimeMillis();   
    }   
  
    /**  
     * @see org.eclipse.compare.BufferedContent#createStream()  
     */  
    protected InputStream createStream() throws CoreException {   
    	if (StringUtil.isEmpty(content))
    		return new ByteArrayInputStream(new byte[0]);
    	else
    		return new ByteArrayInputStream(content.getBytes());   
    }   
  
    /**  
     * @see org.eclipse.compare.IModificationDate#getModificationDate()  
     */  
    public long getModificationDate() {   
        return time;   
    }   
  
    /**  
     * @see org.eclipse.compare.ITypedElement#getImage()  
     */  
    public Image getImage() {   
        return CompareUI.DESC_CTOOL_NEXT.createImage();   
    }   
  
    /**  
     * @see org.eclipse.compare.ITypedElement#getName()  
     */  
    public String getName() {   
        return "字符串比较";   
    }   
  
    /**  
     * @see org.eclipse.compare.ITypedElement#getType()  
     */  
    public String getType() {   
        return ITypedElement.TEXT_TYPE;   
    }
  
    /**  
     * @see org.eclipse.compare.IEditableContent#isEditable()  
     */  
    public boolean isEditable() {   
        return false;   
    }   
  
    /**  
     * @see org.eclipse.compare.IEditableContent#replace(org.eclipse.compare.ITypedElement, org.eclipse.compare.ITypedElement)  
     */  
    public ITypedElement replace(ITypedElement dest, ITypedElement src) {   
        return null;   
    }   
}  
