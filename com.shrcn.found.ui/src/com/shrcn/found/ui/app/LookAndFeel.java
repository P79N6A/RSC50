package com.shrcn.found.ui.app;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.shrcn.found.ui.Activator;

public class LookAndFeel {
	private static LookAndFeel instance;
	public static final String TYPE_BLUE = "blue";
	public static final String TYPE_PURPLE = "purple";
	public static String CURRENT_TYPE = "blue";

	private LookAndFeel() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings
				.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {
			if (rcp_settings.get(IPreferencePageConstants.SKIN_TYPE) != null
					&& !"".equals(rcp_settings
							.get(IPreferencePageConstants.SKIN_TYPE)))
				CURRENT_TYPE = rcp_settings
						.get(IPreferencePageConstants.SKIN_TYPE);
		}
	}

	synchronized public static LookAndFeel getDefault() {
		if (instance == null) {
			instance = new LookAndFeel();
		}
		return instance;
	}

	public Image getToolBarImage() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			return Activator.getImageDescriptor("icons/toolbar_blue.jpg")
					.createImage();
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			return Activator.getImageDescriptor("icons/toolbar_purple.jpg")
					.createImage();
		} else {
			return Activator.getImageDescriptor("icons/toolbar_blue.jpg")
					.createImage();
		}
	}

	public Image getMenuImage() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			return Activator.getImageDescriptor("icons/menu_blue.jpg")
					.createImage();
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			return Activator.getImageDescriptor("icons/menu_purple.jpg")
					.createImage();
		} else {
			return Activator.getImageDescriptor("icons/menu_blue.jpg")
					.createImage();
		}
	}

	public Image getContentBgImage() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			return Activator.getImageDescriptor("icons/content_blue.jpg")
					.createImage();
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			return Activator.getImageDescriptor("icons/content_purple.jpg")
					.createImage();
		} else {
			return Activator.getImageDescriptor("icons/content_blue.jpg")
					.createImage();
		}
	}

	public Color getShellColor() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			RGB rgb = new RGB(151, 205, 255);
			return ToolsColorManager.get(rgb.toString(), rgb);
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			RGB rgb = new RGB(239, 213, 253);
			return ToolsColorManager.get(rgb.toString(), rgb);
		} else {
			RGB rgb = new RGB(151, 205, 255);
			return ToolsColorManager.get(rgb.toString(), rgb);
		}
	}

	public Color getTabFolderColor() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			RGB rgb = new RGB(210, 234, 255);
			return ToolsColorManager.get(rgb.toString(), rgb);
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			RGB rgb = new RGB(226, 206, 249);
			return ToolsColorManager.get(rgb.toString(), rgb);
		} else {
			RGB rgb = new RGB(210, 234, 255);
			return ToolsColorManager.get(rgb.toString(), rgb);
		}
	}

	public Font getControlFont() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings
				.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {
			String fontname = rcp_settings
					.get(IPreferencePageConstants.SYSTEM_CONTROL_FONT_NAME);
			if (fontname != null && !"".equals(fontname)) {
				int fontheight = rcp_settings
						.getInt(IPreferencePageConstants.SYSTEM_CONTROL_FONT_HEIGHT);
				int fontstyle = rcp_settings
						.getInt(IPreferencePageConstants.SYSTEM_CONTROL_FONT_STYLE);
				FontData fontdata = new FontData(fontname, fontheight,
						fontstyle);
				return ToolsFontManager.get(fontdata.getName() + ","
						+ fontdata.getStyle() + "," + fontdata.getHeight(),
						new FontData[] { fontdata });
			}
		}

		return null;

	}

	public Color getControlBGColor() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings
				.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {
			String controlcolor = rcp_settings
					.get(IPreferencePageConstants.CONTROL_BG_CORLOR);
			return getColor(controlcolor);
		}
		RGB rgb = new RGB(255, 255, 255);
		return ToolsColorManager.get(rgb.toString(), rgb);

	}

	public Color getLabelBGColor() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings
				.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {

			String labelcolor = rcp_settings
					.get(IPreferencePageConstants.LABEL_BG_CORLOR);
			return getColor(labelcolor);
		}
		return null;

	}

	private Color getColor(String scolor) {
		if (scolor.equals("")) {
			return null;
		}
		String[] colors = scolor.split(",");
		int red = Integer.parseInt(colors[0]);
		int green = Integer.parseInt(colors[1]);
		int blue = Integer.parseInt(colors[2]);
		RGB rgb = new RGB(red, green, blue);
		return ToolsColorManager.get(rgb.toString(), rgb);
	}

	public Color getFontColor() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings
				.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {

			String fontcolor = rcp_settings
					.get(IPreferencePageConstants.SYSTEM_CONTROL_FONT_CORLOR);
			return getColor(fontcolor);
		}

		return null;

	}

}
