package com.synet.tool.rsc.incr;

import com.shrcn.found.common.util.ObjectUtil;
import com.synet.tool.rsc.compare.Difference;

public class ConflictHandlerFactory {

	/**
	 * 获取diff冲突处理器
	 * @param type
	 * @param diff
	 * @return
	 */
	public static IConflictHandler getHandlerByType(Difference diff) {
		String type = diff.getType();
		EnumConflict conflict = null;
		for (EnumConflict cft : EnumConflict.values()) {
			if (cft.getType().equals(type)) {
				conflict = cft;
				break;
			}
		}
		if (conflict != null) {
			return createConflict(conflict, diff);
		}
		return null;
	}

	/**
	 * 创建指定类型的冲突处理器
	 * @param conflict
	 * @param diff
	 * @return
	 */
	public static IConflictHandler createConflict(EnumConflict conflict,
			Difference diff) {
		Class<?> clazz = conflict.getClazz();
		return (IConflictHandler) ObjectUtil.newInstance(clazz, 
				new Class<?>[] {Difference.class}, new Object[] {diff});
	}
	
}
