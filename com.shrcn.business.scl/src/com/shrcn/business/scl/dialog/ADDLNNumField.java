package com.shrcn.business.scl.dialog;

import com.shrcn.business.xml.schema.LnClass;
import com.shrcn.found.ui.model.FieldBase;


/**
 * 对应LN的添加个数，默认值为0
 */
public class ADDLNNumField extends FieldBase {
	
	public ADDLNNumField() {
		super("添加的个数", 180);
	}
	
	public String getTextValue(Object object) {
		if (object instanceof LnClass) {
			int num = ((LnClass) object).getNum();
			return String.valueOf(num);

		} else
			return "";
	}
}