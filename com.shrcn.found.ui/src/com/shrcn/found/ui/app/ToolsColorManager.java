package com.shrcn.found.ui.app;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ToolsColorManager {

	public static ColorRegistry registry;

	static {
		registry = new ColorRegistry();

	}

	public static void put(String symbolicName, RGB colorData) {
		registry.put(symbolicName, colorData);
	}

	public static Color get(String symbolicName) {
		return registry.get(symbolicName);

	}

	public static Color get(String symbolicName, RGB colorData) {

		if (registry.get(symbolicName) == null) {
			registry.put(symbolicName, colorData);
			return registry.get(symbolicName);
		}
		return registry.get(symbolicName);
	}
}
