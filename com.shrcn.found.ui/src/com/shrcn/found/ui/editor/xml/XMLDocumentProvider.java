package com.shrcn.found.ui.editor.xml;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.editors.text.StorageDocumentProvider;

public class XMLDocumentProvider extends StorageDocumentProvider  {

	@Override
	protected IAnnotationModel createAnnotationModel(Object element)
			throws CoreException {
	     return null;
	}

	@Override
	protected void setupDocument(Object element, IDocument document) {
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(
					new XMLPartitionScanner(), new String[] {
							XMLPartitionScanner.XML_TAG,
							XMLPartitionScanner.XML_COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
	}

}