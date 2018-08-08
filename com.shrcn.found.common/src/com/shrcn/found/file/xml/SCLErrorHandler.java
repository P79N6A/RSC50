/*
 * MyErrorHandler.java
 */

package com.shrcn.found.file.xml;

import org.eclipse.core.runtime.IProgressMonitor;


/**
 * This ErrorHandler prints any Warning , Error or Fatal Error.
 *
 * @author  Neeraj Bajaj, Sun Microsystems.
 */
public class SCLErrorHandler implements org.xml.sax.ErrorHandler{
	
	private static final String AVOID = "<unique>";  //$NON-NLS-1$
	private ReportBean report = null;
	IProgressMonitor monitor;
    /** Creates a new instance of MyErrorHandler */
    public SCLErrorHandler(ReportBean report) {
    	this.report = report;
    }
    

    public void error(org.xml.sax.SAXParseException sAXParseException) throws org.xml.sax.SAXException {
    	int lineNum = sAXParseException.getLineNumber();
    	int colNum = sAXParseException.getColumnNumber();
    	String error = sAXParseException.getLocalizedMessage();
    	if(error.indexOf(AVOID) == -1) {
    		if(monitor != null)
    			monitor.worked(1);
	    	report.addMessage(Messages.getString("SCLErrorHandler.syntax_error_row") + lineNum + Messages.getString("SCLErrorHandler.column") + colNum + "\t " + error + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//	        System.out.println("ERROR: " + error + "\n");
    	}
    }

    public void fatalError(org.xml.sax.SAXParseException sAXParseException) throws org.xml.sax.SAXException {
    	
    	int lineNum = sAXParseException.getLineNumber();
    	int colNum = sAXParseException.getColumnNumber();
    	String error = sAXParseException.getLocalizedMessage();
    	if(error.indexOf(AVOID) == -1) {
    		if(monitor != null)
    			monitor.worked(1);
    		report.addMessage(Messages.getString("SCLErrorHandler.syntax_error_row") + lineNum + Messages.getString("SCLErrorHandler.column") + colNum + "\t " + error + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//    		System.out.println("FATAL ERROR: " + error + "\n");
    	}
    	
    }

    public void warning(org.xml.sax.SAXParseException sAXParseException) throws org.xml.sax.SAXException {
    	int lineNum = sAXParseException.getLineNumber();
    	int colNum = sAXParseException.getColumnNumber();
    	String error = sAXParseException.getLocalizedMessage();
    	if(error.indexOf(AVOID) == -1) {
    		if(monitor != null)
    			monitor.worked(1);
	    	report.addMessage(Messages.getString("SCLErrorHandler.syntax_error_row") + lineNum + Messages.getString("SCLErrorHandler.column") + colNum + "\t " + error + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//	        System.out.println("WARNING: " + error + "\n");
    	}
    }


	public IProgressMonitor getMonitor() {
		return monitor;
	}


	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
    
}
