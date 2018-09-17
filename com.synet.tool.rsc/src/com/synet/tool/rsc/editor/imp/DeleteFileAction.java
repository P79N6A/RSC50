package com.synet.tool.rsc.editor.imp;

import java.util.List;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.action.MenuAction;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.IM100FileInfoEntity;

public class DeleteFileAction extends MenuAction {

	private org.eclipse.swt.widgets.List titleList;
	private Class<?> itemClass;
	
	public DeleteFileAction(org.eclipse.swt.widgets.List titleList, Class<?> itemClass) {
		super("删除(&D)");
		this.titleList = titleList;
		this.itemClass = itemClass;
	}

	@Override
	public void run() {
		String[] selects = titleList.getSelection();
		if (selects != null && selects.length > 0) {
			if (!DialogHelper.showConfirm("确定要删除导入文件吗？", "是", "否")) {
				return;
			}
			BeanDaoImpl beandao = BeanDaoImpl.getInstance();
			for (String filename : selects) {
				List<IM100FileInfoEntity> files = (List<IM100FileInfoEntity>) beandao.getListByCriteria(IM100FileInfoEntity.class, "fileName", filename);
				for (IM100FileInfoEntity fileInfoEntity : files) {
					beandao.deleteAll(itemClass, "fileInfoEntity", fileInfoEntity);
					beandao.delete(fileInfoEntity);
				}
				titleList.remove(filename);
			}
			EventManager.getDefault().notify(EventConstants.REFRESH_EIDTOR, null);
		}
	}
}
