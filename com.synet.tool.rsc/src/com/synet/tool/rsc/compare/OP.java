package com.synet.tool.rsc.compare;

import org.eclipse.swt.graphics.Image;

import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.ImageConstants;

public enum OP {
	
	ADD("增加"), DELETE("删除"), RENAME("重命名"), UPDATE("修改"), NONE("忽略");
	
	private IconsManager iconMgr = IconsManager.getInstance();
	private String desc;

	OP(String desc) {
		this.desc = desc;
	}

	public Image getImage() {
		if (this == ADD) {
			return iconMgr.getImage(ImageConstants.STEP_ADD);
		} else if (this == DELETE) {
			return iconMgr.getImage(ImageConstants.STEP_MINUS);
		} else if (this == RENAME) {
			return iconMgr.getImage(ImageConstants.RENAME);
		} else if (this == UPDATE) {
			return iconMgr.getImage(ImageConstants.RENAME);
		}
		return null;
	}

	public String getDesc() {
		return desc;
	}
};
