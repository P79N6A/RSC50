package com.shrcn.found.ui.app;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.FastViewBar;
import org.eclipse.ui.internal.FastViewManager;
import org.eclipse.ui.internal.LayoutPart;
import org.eclipse.ui.internal.Perspective;
import org.eclipse.ui.internal.PerspectiveHelper;
import org.eclipse.ui.internal.ViewSashContainer;
import org.eclipse.ui.internal.ViewStack;
import org.eclipse.ui.internal.WindowTrimProxy;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.internal.layout.IWindowTrim;
import org.eclipse.ui.internal.layout.TrimArea;
import org.eclipse.ui.internal.layout.TrimLayout;
import org.eclipse.ui.internal.progress.ProgressRegion;
import org.sf.feeling.swt.win32.extension.widgets.ShellWrapper;

import com.shrcn.found.common.event.Context;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.event.IEventHandler;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.ui.UIConstants;

public abstract class AbstractWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor implements IEventHandler {

	private WorkbenchWindow window;
	private TrimLayout defaultLayout;
	private CBanner topBar;
	private WindowTrimProxy topBarTrim;
	private FastViewBar fastViewBar;
	private ProgressRegion progressRegion;
	private SCTPerspectiveSwitcher perspectiveSwitcher;
	
	private TopBanner topBanner;
	private Composite clientArea;
	
    public AbstractWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
        EventManager.getDefault().registEventHandler(this);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShellStyle(SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE);
        configurer.setInitialSize(new Point(600, 400));
        configurer.setShowCoolBar(false);
    }
    
    @Override
    public void postWindowOpen() {
    	addEditorAreaTrim();
    	addViewsAreaTrim();
    	defaultLayout.forceLayout();
    }
    
    @Override
    public void postWindowClose() {
    	EventManager.getDefault().removeEventHandler(this);
    	super.postWindowClose();
    }
    
	@Override
	public void execute(Context context) {
		String event = context.getEventName();
		if (EventConstants.SYS_REFRESH_TOP_BAN.equals(event)) {
	    	topBanner.refresh(false);
		} else if (EventConstants.SYS_RELOAD_TOP_BAN.equals(event)) {
	    	topBanner.refresh(true);
		}
	}
    
	@Override
    public void createWindowContents(final Shell shell) {
    	
    	ShellWrapper wrapper = new ShellWrapper( shell );
    	wrapper.installTheme(UIConstants.CurrentTheme);
    	clientArea = wrapper.getClientArea();
    	
		window = (WorkbenchWindow) getWindowConfigurer().getWindow();

    	defaultLayout = new TrimLayout();
    	injectWbwElement("defaultLayout", defaultLayout);
//    	int s = 0;
//    	defaultLayout.setSpacing(s, s, s, s);
//		defaultLayout.setMargins(s, s);
    	setAreaSpacings();
		clientArea.setLayout(defaultLayout);
		
		topBar = new CBanner(clientArea, SWT.NONE);
		injectWbwElement("topBar", topBar);
		topBarTrim = new WindowTrimProxy(topBar,
				"org.eclipse.ui.internal.WorkbenchWindow.topBar",
				WorkbenchMessages.TrimCommon_Main_TrimName, SWT.NONE, true);
		injectWbwElement("topBarTrim", topBarTrim);
		getWindowConfigurer().getPresentationFactory().createStatusLineControl(
				window.getStatusLineManager(), clientArea);

		fastViewBar = new FastViewBar(window);
		injectWbwElement("fastViewBar", fastViewBar);
		fastViewBar.createControl(clientArea);
		
		if (getWindowConfigurer().getShowPerspectiveBar()) {
			addPerspectiveBar(perspectiveBarStyle());
		}

		// Create the client composite area (where page content goes).
		ObjectUtil.invoke(window, "createPageComposite", new Class<?>[] {Composite.class}, clientArea);
		
		clientArea.setBackground(UIConstants.Win_BG);
		// topComposite
		topBanner = new TopBanner(clientArea, getWindowConfigurer());
        WindowTrimProxy topCompositeTrim = new WindowTrimProxy(topBanner,"org.eclipse.swt.widgets.Composite",
        		"标题", SWT.NONE, true);
        defaultLayout.addTrim(SWT.TOP, topCompositeTrim);
        topBanner.setVisible(true);
        // progress
        createProgressIndicator(clientArea);
		
        ObjectUtil.invoke(window, "setLayoutDataForContents");
    }
	
	private int perspectiveBarStyle() {
		return SWT.FLAT | SWT.WRAP | SWT.RIGHT | SWT.HORIZONTAL;
	}
	
	private void addPerspectiveBar(int style) {
		Assert.isTrue(perspectiveSwitcher == null);
		perspectiveSwitcher = new SCTPerspectiveSwitcher(window, null, style);
		ObjectUtil.setFieldValue(perspectiveSwitcher, "parent", clientArea);
		ObjectUtil.setFieldValue(perspectiveSwitcher, "currentLocation", 3);
		ObjectUtil.invoke(perspectiveSwitcher, "createControlForLeft", new Class<?>[0], new Object[0]);
		defaultLayout.addTrim(SWT.LEFT, perspectiveSwitcher);
	}
    
    private void injectWbwElement(String name, Object value) {
    	ObjectUtil.setFieldValue(window, name, value);
    }
    
    private void createProgressIndicator(Composite shell) {
		if (getWindowConfigurer().getShowProgressIndicator()) {
			progressRegion = new ProgressRegion();
			progressRegion.createContents(shell, window);
			injectWbwElement("progressRegion", progressRegion);
		}
	}
    
    private void addEditorAreaTrim() {
		TrimLayout tbm = defaultLayout;
		WorkbenchWindow wbw = (WorkbenchWindow) getWindowConfigurer().getWindow();
		LayoutPart editorArea = ((WorkbenchPage)wbw.getActivePage()).getEditorPresentation().getLayoutPart();
		
		int suggestedSide = SWT.BOTTOM;
		int cachedSide = ((TrimLayout)tbm).getPreferredArea(IPageLayout.ID_EDITOR_AREA);
		if (cachedSide != -1)
			suggestedSide = cachedSide;
		
		IWindowTrim beforeMe = ((TrimLayout)tbm).getPreferredLocation(IPageLayout.ID_EDITOR_AREA);
		
		// Gain access to the trim manager
		SCTEditorAreaTrimToolBar editorAreaTrim = new SCTEditorAreaTrimToolBar(wbw, editorArea, clientArea);
		editorAreaTrim.dock(suggestedSide);
		tbm.addTrim(suggestedSide, editorAreaTrim, beforeMe);
		
		tbm.setTrimVisible(editorAreaTrim, false);
    }
    
    private void addViewsAreaTrim() {
    	WorkbenchWindow wbw = (WorkbenchWindow) getWindowConfigurer().getWindow();
    	WorkbenchPage workbenchPage = (WorkbenchPage)wbw.getActivePage();
    	Perspective perspective = (Perspective) workbenchPage.getActivePerspective();
    	PerspectiveHelper perspectiveHelper = perspective.getPresentation();
    	ViewSashContainer vsc = perspectiveHelper.getLayout();
    	LayoutPart[] parts = vsc.getChildren();
    	for (LayoutPart part : parts) {
    		if (part instanceof ViewStack) {
    			addViewTrim((ViewStack) part, perspective, wbw);
    		}
    	}
    }
    
    private void addViewTrim(ViewStack vs, Perspective perspective, WorkbenchWindow wbw) {
    	// If we're part of a 'maximize' operation then use the cached
		// bounds...
		String id = vs.getID();
		TrimLayout tbm = defaultLayout;
		Rectangle stackBounds = perspective.getPresentation().getCachedBoundsFor(id);

		// OK, no cache means that we use the current stack position
		if (stackBounds == null)
			stackBounds = vs.getBounds();
		
		int paneOrientation = (stackBounds.width > stackBounds.height) ? SWT.HORIZONTAL
				: SWT.VERTICAL;
    	
    		int suggestedSide = SWT.BOTTOM;
			int cachedSide = tbm.getPreferredArea(id);
			if (cachedSide != -1)
				suggestedSide = cachedSide;
			
			IWindowTrim beforeMe = tbm.getPreferredLocation(id);
			
			SCTViewStackTrimToolBar trim = new SCTViewStackTrimToolBar(id, suggestedSide,
					paneOrientation, wbw, clientArea);
			tbm.addTrim(suggestedSide, trim, beforeMe);
			tbm.setTrimVisible(trim, false);
			updateTrim(trim.getId(), perspective);
    }
    
    private void updateTrim(String id, Perspective persp) {
    	FastViewManager fvm = persp.getFastViewManager();
    	ObjectUtil.invoke(fvm, "updateTrim", new Class[]{String.class}, id);
	}
    
    /**
     * 修改窗口各部件间距为零
     */
    private void setAreaSpacings() {
		Class<?> cl = TrimArea.class;
		try {
			Field[] fs = new Field[] {
					cl.getDeclaredField("TILE_SPACING"),
					cl.getDeclaredField("LINE_SPACING")
					};
			for (Field f : fs) {
				f.setAccessible(true);
				f.set(null, 0);
			}
		} catch (SecurityException e) {
			SCTLogger.error("", e);
		} catch (NoSuchFieldException e) {
			SCTLogger.error("", e);
		} catch (IllegalArgumentException e) {
			SCTLogger.error("", e);
		} catch (IllegalAccessException e) {
			SCTLogger.error("", e);
		}
	}
}
