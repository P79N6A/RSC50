package com.synet.tool.rsc;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.util.TaskManager;
import com.synet.tool.rsc.excel.ImportConfigFactory;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.RuleType;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.synet.tool.rsc"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		EventManager.getDefault().loadEventHandlers(getClass(), RSCConstants.EVENT_PATH);
		
		DictManager dictmgr = DictManager.getInstance();
		dictmgr.init(getClass(), RSCConstants.DICT_PATH, true);
		RuleType.initDicts();
		
		new TaskManager().start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
