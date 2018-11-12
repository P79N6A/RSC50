package com.synet.tool.rsc.excel;

public class ImportField {

	private int colIndex;
	private String fieldTitle;
	private String fieldName;
	private String prefix;
	
	public ImportField(int colIndex, String fieldTitle, String fieldName) {
		this(colIndex, fieldTitle, fieldName, null);
	}
	
	public ImportField(int colIndex, String fieldTitle, String fieldName, String prefix) {
		this.colIndex = colIndex;
		this.fieldTitle = fieldTitle;
		this.fieldName = fieldName;
		this.prefix = prefix;
	}
	
	public int getColIndex() {
		return colIndex;
	}
	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}
	public String getFieldTitle() {
		return fieldTitle;
	}
	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
