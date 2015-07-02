/* 
 * The MIT License
 *
 * Copyright 2015 InsiTech LLC.   gwtviz@insitechinc.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.itgp.gwtviz.client.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.itgp.gwtviz.client.comm.ServerComm;
import com.itgp.gwtviz.client.ui.runtime.*;
import com.itgp.gwtviz.client.ui.runtime.event.*;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.model.*;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;
import java.util.*;

public class MainDesktop implements EntryPoint {

	public final static String version = "Visualization Module v0.60";

	public static boolean IN_DEBUG_MODE = true;

	static String mRouteURL = "";
	static public MRoute.Docs docs = DataUtility.makeDocs();
	static Drupal.Mode CurrentMode;
	static List<Item> currentDataModel;
	static String currentDataModelRawData = null;
	public static ProgressBar progressBar;

	enum RunMode {
		Design, Runtime
	};

	public MainDesktop() {
	}

	public static void setMRouteURL(String url) {
		mRouteURL = url;
	}

	public static String getMRouteURL() {
		return mRouteURL;
	}

	public static void setMRouteDocs(MRoute.Docs _docs) {
		docs = _docs;
	}

	public static MRoute.Docs getMRouteDocs() {
		return docs;
	}

	public static void setCurrentDataModel(List<Item> data, String rawData) {
		currentDataModel = data;
		currentDataModelRawData = rawData;
	}

	public static List<Item> getCurrentDataModel() {
		return currentDataModel;
	}

	public static String getCurrentDataModelRawData() {
		return currentDataModelRawData;
	}

	public static Widget makeDesignConfigurator() {

		ConfigPanel_1 configPanel_1 = new ConfigPanel_1();
		try {
			configPanel_1.initWithData(Drupal.readDataFromDevTimeEditTextField());
		} catch (Throwable e) {
			MainDesktop.consoleLog("initDevPlugin " + e.getMessage());
		}

		MainDesktop.consoleLog("initDevPlugin " + 1);
		ContentPanel cp = new ContentPanel();
		MainDesktop.consoleLog("initDevPlugin " + 2);
		cp.setHeadingText(version);
		cp.add(configPanel_1.makeVisMainMenu(), new MarginData(0, 0, 5, 0));
		configPanel_1.setParentContentPanel(cp);

		if (MainDesktop.IN_DEBUG_MODE) {
			ServerComm.getConfigString(configPanel_1, "");
		}

		MainDesktop.consoleLog("initDevPlugin " + 4);
		configPanel_1.readCurrentStateFromConfig();
		MainDesktop.consoleLog("initDevPlugin " + 5);
		
		

		return cp;
	}

	public static Widget initProgessBar() {

		progressBar = new ProgressBar();

		return progressBar.makeProgressBar();
	}

	public static Widget initRuntimePlugin() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		vPanel.getElement().setId(Drupal._GWTVIZ_MAIN_PANEL_EDIT);

		Label label = new Label(version);

		vPanel.add(label);

		return vPanel;
	}

	private static void addRuntimeGraph() {
		if (MainDesktop.IN_DEBUG_MODE) {
			ServerComm.getConfigString(null, "");
		} else {
			String configText = Drupal.readDataFromRunTimeEditTextField();
			execRuntimeGraph(configText);
		}
	}

	// ------------------------------------------------------------------------------------------------------
	public static void execRuntimeGraph(String configText) {
		RuntimeDataV1 run = new RuntimeDataV1(); // Data holder (could be different types) for the chart
		RuntimeEntry.start(configText, run);
	}

	public static void getRuntimeGraphFilteredData(GraphConfigImpl_1 config, String dataArray, FilterCompleteListener listener) {
		RuntimeEntry_Part2.getFilteredData(config, dataArray, listener);
	}

	// ------------------------------------------------------------------------------------------------------

	// public static final native BodyElement getBodyElement() /*-{
	// var win = window.open("", "win",
	// "width=940,height=400,status=1,resizeable=1,scrollbars=1"); // a window object
	// win.document.open("text/html", "replace");
	//
	// win.document
	// .write("<HTML><HEAD>"
	// +
	// "</HEAD><BODY><div class=\"mainpanel\"><div style=\"width: 100%; height: 54px;\"><div id=\"mainbody\"class=\"mainbody\" style=\"width: 100%;\"></div></div></div></BODY></HTML>");
	// win.document.close();
	// win.focus();
	// return win.document.body;
	// }-*/
	// ;

	// public Widget test() {
	// Button b = new Button("Click me");
	// b.addClickHandler(new ClickHandler() {
	// @Override
	// public void onClick(ClickEvent event) {
	//
	// // Instantiate the dialog box and show it.
	// // VerticalLayoutContainer v = (new ConfigPanel_1()).makeVisMainMenu();
	// // MyDialog myDialog = new MyDialog(v);
	//
	// int left = Window.getClientWidth() / 2;
	// int top = Window.getClientHeight() / 2;
	//
	// // myDialog.setPopupPosition(left, top);
	// // myDialog.show();
	// }
	// });
	//
	// return b;
	// }

	// private native String getTableMarkup() /*-{
	// return [
	// '<table width=100% cellpadding=0 cellspacing=0>',
	// '<tr><td class=reloadButton width=20%></td><td class=dataSourcePanel width=20%></td><td class=viewDataButton width=20%></td><td class=downloadDataButton
	// width=20%></td><td class=chartPanel width=20%></td></tr>',
	// '</table>' ].join("");
	// }-*/
	// ;

	@Override
	public void onModuleLoad() {
		// ***************************************
		// First sets the MRoute Server access
		// Then the callback calls detectModeAndMount()
		// ***************************************
		ServerComm.getMRouteAddress();
	}

	public static void detectModeAndMount() {

		/***************************************************************************************/
		/* Try to mount inside the DRUPAL page */
		/* This is the old way of mounting the plugin */
		/* New approach will involve running these plugins inside the iframe, so this mounting logic is done on the outside of this module */
		/***************************************************************************************/
		consoleLog("DMM 1");
		RootPanel devMountPointTest = RootPanel.get(Drupal._GWTVIZ_MOUNT_POINT_DEV);
		if (devMountPointTest == null) {
			consoleLog("DMM 2");

			// ***************************************
			// Nothing Mounted
			// Lets create a NEW Mount point for DEV
			// ***************************************
			Element tempMountPoint = DOM.getElementById(Drupal._DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE);
			consoleLog("DMM 3");

			if (tempMountPoint != null) {
				consoleLog("DMM 4");
				tempMountPoint.getParentElement().setId(Drupal._GWTVIZ_MOUNT_POINT_DEV);
				CurrentMode = Drupal.Mode.Drupal_Edit_New;
			}
		} else {
			consoleLog("DMM 5");
			CurrentMode = Drupal.Mode.Drupal_Edit_Existing;
		}

		devMountPointTest = RootPanel.get(Drupal._GWTVIZ_MOUNT_POINT_DEV);
		consoleLog("DMM 6");

		if (devMountPointTest != null) {
			consoleLog("DMM 7");

			// ***************************************
			// Hide element next to our mount point
			// AND Mount DEV Plugin
			// ***************************************
			consoleLog("DMM 8");
			Element otherSpace = DOM.getElementById(Drupal._DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE);
			if (otherSpace != null) {
				Drupal.hideElement(otherSpace);
				consoleLog("DMM 9");
			}

			consoleLog("DMM 10");

			// ***************************************
			// Finally lets mount our DEV plugin
			// ***************************************
			devMountPointTest.add(makeDesignConfigurator());
			devMountPointTest.add(Drupal.makeChartDiv());
			consoleLog("DMM 11");

			return;
		}

		// *******************************************************
		// If not in DEV lets try to see if we are in READ Mode;
		// *******************************************************
		consoleLog("DMM 12");

		if (devMountPointTest == null) {
			consoleLog("DMM 13");
			Element[] elements = DOMUtils.getElementsByClassName(Drupal._DRUPAL_EDIT__CONTENT_CLEARFIX, DOM.getElementById("block-system-main"));
			if (elements != null && elements.length > 0) {
				Element editContentDiv = elements[0];

				if (editContentDiv != null) {
					CurrentMode = Drupal.Mode.Drupal_Read;

					// *******************************************************
					// Find Edit MountPoint
					// *******************************************************
					String id = editContentDiv.getId();
					if (id != null || id.length() == 0) {
						editContentDiv.setId(Drupal._GWTVIZ_MAIN_PANEL_EDIT);
					}

					RootPanel editMountPointTest = RootPanel.get(Drupal._GWTVIZ_MAIN_PANEL_EDIT);
					editMountPointTest.add(Drupal.makeChartDiv());
					editMountPointTest.add(initRuntimePlugin());
					addRuntimeGraph();

					// *******************************************************
					// The second node needs to be hidden
					// *******************************************************
					Element[] elementsToHide = DOMUtils.getElementsByClassName(Drupal._DRUPAL_EDIT_FIELD_NAME_FIELD_VISUAL_SPACE,
							editMountPointTest.getElement());
					if (elementsToHide != null && elementsToHide.length > 0) {
						Drupal.hideElement(elementsToHide[0]);
					}
				}

				return;
			}

		}

		consoleLog("DMM 100 Not Mounting GWTViz");
		// ***************************************
		// NOT DRUPAL
		// ***************************************
		CurrentMode = Drupal.Mode.StandAlone_Edit;
		RunMode currentRunMode = RunMode.Design;

		String url = Window.Location.getHref();
		consoleLog("RunMode Set: "+url);
		if (url.endsWith(RunMode.Design.name())) {
			currentRunMode = RunMode.Design;		
			CurrentMode = Drupal.Mode.Drupal_Edit_Existing;
		} else if (url.endsWith(RunMode.Runtime.name())) {
			currentRunMode = RunMode.Runtime;
			CurrentMode = Drupal.Mode.Drupal_Read;
		}
		consoleLog("RunMode: "+currentRunMode.name());

		if (currentRunMode.equals(RunMode.Design)) {
			consoleLog("Trying to find:  "+Drupal._GWTVIZ_MOUNT_CONFIG_ID);
			//RootPanel rootPanel = RootPanel.get();
			RootPanel panelConfig = RootPanel.get(Drupal._GWTVIZ_MOUNT_CONFIG_ID);
			if (panelConfig != null) {
				consoleLog("Found:  "+Drupal._GWTVIZ_MOUNT_CONFIG_ID);
				panelConfig.add(makeDesignConfigurator());
				consoleLog("Added  DesignConfigurator()");
			} 
		}

		if (currentRunMode.equals(RunMode.Design) ||currentRunMode.equals(RunMode.Runtime)) {
			consoleLog("Trying to find:  "+Drupal._GWTVIZ_MOUNT_PROCESS_ID);
			RootPanel panelProcessInfo = RootPanel.get(Drupal._GWTVIZ_MOUNT_PROCESS_ID);
			if (panelProcessInfo != null) {
				consoleLog("Found:  "+Drupal._GWTVIZ_MOUNT_PROCESS_ID);
				panelProcessInfo.add(Drupal.makeProcessInfoDiv());
				consoleLog("Added  ProcessInfoDiv()");
			} 

			consoleLog("Trying to find:  "+Drupal._GWTVIZ_MOUNT_CHART_ID);
			RootPanel panelChart = RootPanel.get(Drupal._GWTVIZ_MOUNT_CHART_ID);
			if (panelProcessInfo != null) {
				consoleLog("Found:  "+Drupal._GWTVIZ_MOUNT_CHART_ID);
				panelChart.add(Drupal.makeChartDiv());
				consoleLog("Added  ChartDiv()");
				addRuntimeGraph();
			} 
		}
	}

	public static native void consoleLog(String message) /*-{
		console.log("gwtviz:" + message);
	}-*/
	;

}
