package com.synet.tool.rsc.excel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.shrcn.found.common.util.ObjectUtil;
import com.synet.tool.rsc.RSCProperties;

public class ImportConfig {

	private Class<?> entityClass;
	private Map<Integer, ImportField> entityPropties;
	private Map<String, ImportField> nameMap;
	
	public ImportConfig(Class<?> entityClass, Map<Integer, ImportField> entityPropties) {
		this.entityClass = entityClass;
		this.entityPropties = entityPropties;
		initNameMap();
	}
	
	private void initNameMap() {
		this.nameMap = new HashMap<String, ImportField>();
		for (ImportField field : entityPropties.values()) {
			nameMap.put(field.getFieldTitle(), field);
		}
	}
	
	public void synFieldCols(Map<Integer, String> map) {
		ImportField idField = entityPropties.get(Integer.MAX_VALUE);
		entityPropties.clear();
		for (Entry<Integer, String> entry : map.entrySet()) {
			int col = entry.getKey();
			String title = entry.getValue();
			entityPropties.put(col, nameMap.get(title));
		}
		entityPropties.put(Integer.MAX_VALUE, idField);
	}
	
	public Object createEntity() {
		ImportField idField = entityPropties.get(Integer.MAX_VALUE);
		String prefix = idField.getPrefix();
		String id = RSCProperties.getInstance().nextTbCode(prefix);
		Object entity = ObjectUtil.newInstance(entityClass);
		ObjectUtil.setProperty(entity, idField.getFieldName(), id);
		return entity;
	}
	
	public String[] getExcelFields() {
		List<Integer> fieldCols = new ArrayList<>();
		Set<Integer> keySet = entityPropties.keySet();
		fieldCols.addAll(keySet);
		Collections.sort(fieldCols, new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				return i1 - i2;
			}
		});
		int size = fieldCols.size();
		String[] titles = new String[size - 1];
		for (int i=0; i<size-1; i++) {
			titles[i] = entityPropties.get(fieldCols.get(i)).getFieldTitle();
		}
		return titles;
	}
	
	public Class<?> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public Map<Integer, ImportField> getEntityPropties() {
		return entityPropties;
	}

	public void setEntityPropties(Map<Integer, ImportField> entityPropties) {
		this.entityPropties = entityPropties;
	}
	
}
