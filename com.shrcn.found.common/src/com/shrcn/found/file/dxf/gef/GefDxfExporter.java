/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf.gef;

import java.util.List;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineDecoration;

import com.shrcn.found.file.dxf.AbstractExporter;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-6-29
 */
/**
 * $Log: GefDxfExporter.java,v $
 * Revision 1.2  2012/07/31 09:30:21  cchun
 * Update:增加边框处理
 *
 * Revision 1.1  2012/07/02 02:17:22  cchun
 * Add:重构后的dxf输出class
 *
 */
public class GefDxfExporter extends AbstractExporter {

	protected IFigure root;
	protected IGefDxfWriter writer;
	
	public GefDxfExporter(IFigure root) {
		this.root = root;
	}

	@Override
	protected void doWrite() {
		writer = new GefDxfWriter(w);
		writer.writeHeader("DXF created with EASY50");
		writer.writeTables();
		writer.writeBlocks();
		// Geometric entities begin
		writer.writeEntitiesBegin();
		// Geometric entities go here
		writeFigure(root);
		// Geometric entities end
		writer.writeEntitiesEnd();
		writer.writeDictionary();
		writer.writeEnd();
		w.flush();
	}
	
	/**
	 * 输出图形子图形
	 * @param fig
	 */
	private void writeFigureChildren(IFigure fig) {
		List<?> children = fig.getChildren();
		for (Object child : children) {
			writeFigure((IFigure) child);
		}
	}
	
	/**
	 * 输出图形
	 * @param fig
	 */
	protected void writeFigure(IFigure fig) {
		if (fig instanceof IGefDxfFigure) {
			((IGefDxfFigure)fig).writeDXF(writer);
		} else if (fig instanceof Label) {
			writer.writeLabel((Label) fig);
		} else if (fig instanceof Connection) {
			writer.writeConnection((Connection) fig);
		} else if (fig instanceof PolylineDecoration) {
			writer.writeLineDecoration((PolylineDecoration)fig);
		}
		Border border = fig.getBorder();
		if (border instanceof IGefDxfFigure) {
			((IGefDxfFigure)border).writeDXF(writer);
		}
		
		if (fig.getChildren().size() > 0)
			writeFigureChildren(fig);
	}
	
}
