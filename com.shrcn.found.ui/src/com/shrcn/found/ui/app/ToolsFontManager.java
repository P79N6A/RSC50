package com.shrcn.found.ui.app;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public class ToolsFontManager {

	static FontRegistry registry;
	static Font defult;
	static {
		registry = new FontRegistry();
		defult = registry.get("defult");
	}

	public static void put(String symbolicName, FontData[] fontData) {
		registry.put(symbolicName, fontData);
	}

	public static Font getDefaultFont() {
		return defult;
	}

	public static Font get(String symbolicName) {
		return registry.get(symbolicName);

	}

	public static Font get(String symbolicName, FontData[] fontData) {

		if (!registry.hasValueFor(symbolicName)) {
			registry.put(symbolicName, fontData);
			return registry.get(symbolicName);
		}
		return registry.get(symbolicName);
	}

}
