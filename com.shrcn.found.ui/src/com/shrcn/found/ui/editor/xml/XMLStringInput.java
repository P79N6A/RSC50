
package com.shrcn.found.ui.editor.xml;

import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.PlatformObject;

public class XMLStringInput extends PlatformObject  implements IStorage   {

	private InputStream inputStream;
	private String name;
	private int pre;
	
	public XMLStringInput(InputStream inputStream , String name, int pre){
		super();
		this.inputStream = inputStream;
		this.name = name;
		this.pre = pre;
	}
	@Override
	public InputStream getContents() throws CoreException {
		return inputStream;
	}

	@Override
	public IPath getFullPath() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}
	
	public int getPre(){
		return pre;
	}
	

}
