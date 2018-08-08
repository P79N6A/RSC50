package com.shrcn.found.ui.editor.xml;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public  class XMLEditorInput extends PlatformObject  implements IStorageEditorInput {

	private XMLStringInput storage;
	public XMLEditorInput(XMLStringInput storage){
		super();
		this.storage = storage;
	}
	
    @Override  
    public IStorage getStorage() throws CoreException {  
	     return storage;   
    }  
	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return storage.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return storage.getName();
	}
	
	public int getPre(){
		return storage.getPre();
	}

}
