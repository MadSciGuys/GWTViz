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

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Drupal {

	public static final String _DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE = "edit-field-visual-space";
	public static final String _DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE_ADD_MORE_WRAPPER = "field-visual-space-add-more-wrapper";

	public static final String _DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE_UND_0_VALUE = "edit-field-visual-space-und-0-value";
	public static final String _DRUPAL_DEV__EDIT_TEXT_FULL_FORM_CONTROL_FORM_TEXTAREA = "text-full form-control form-textarea";

	public static final String _DRUPAL_EDIT__NODE_VIZCONTENTTYPE = "node node-vizcontenttype node-promoted clearfix";
	public static final String _DRUPAL_EDIT__CONTENT_CLEARFIX = "content clearfix"; // this is the class that needs to be hidden

	public static final String _DRUPAL_EDIT_FIELD_NAME_FIELD_VISUAL_SPACE = "field field-name-field-visual-space field-type-text-long field-label-above"; // this

	public static final String _DRUPAL_GWTVIZ_CHART = "chart ";
	public static final String _DRUPAL_GWTVIZ_CHART_ID = "gwtviz_chart_id";
	public static final String _DRUPAL_GWTVIZ_PROCESS_ID = "gwtviz_process_id";
	// the

	public static final String _DRUPAL_GWTVIZ_DOWNLOAD_LINK = "gwtviz_downloadLink";
	public static final String _DRUPAL_GWTVIZ_RUNTIME_STATUS = "gwtviz_runtime_status";

	// with  EDIT  data
	public static final String _DRUPAL_EDIT_FIELD_ITEM_EVEN = "field-item even"; // this class div contains the graph data

	public final static String _GWTVIZ_MOUNT_POINT_DEV = "gwtviz-mount-point-dev";
	public final static String _GWTVIZ_MAIN_PANEL_DEV = "gwtviz-main-panel-dev";

	public final static String _GWTVIZ_MAIN_PANEL_EDIT = "gwtviz-main-panel-edit";

	/******************************/
	/* Main HTML page mount points */
	/******************************/
	public final static String _GWTVIZ_MOUNT_CONFIG_ID = "gwtviz_mount_config_id";
	public final static String _GWTVIZ_MOUNT_PROCESS_ID = "gwtviz_mount_process_id";
	public final static String _GWTVIZ_MOUNT_CHART_ID = "gwtviz_mount_chart_id";

	public enum Mode {
		Drupal_Edit_New, Drupal_Edit_Existing, Drupal_Read, StandAlone_Edit, StandAlone_Read
	};

	public static void hideElement(Element element) {
		if (!Display.NONE.toString().toLowerCase().equals(element.getStyle().getDisplay().toLowerCase())) {
			element.getStyle().setDisplay(Display.NONE);
		}
	}

	public static HTML makeProcessInfoDiv() {
		Element processDiv = DOM.createDiv();
		processDiv.setId("process_info");
		processDiv.appendChild(DOM.createElement("svg"));

		HTML processHtml = new HTML(processDiv.getString());
		processHtml.getElement().setId(_DRUPAL_GWTVIZ_PROCESS_ID);
		return processHtml;

	}

	public static HTML makeChartDiv() {

		Element chartDiv = DOM.createDiv();
		chartDiv.setId("chart");
		chartDiv.appendChild(DOM.createElement("svg"));

		HTML chartHtml = new HTML(chartDiv.getString());
		chartHtml.getElement().setId(_DRUPAL_GWTVIZ_CHART_ID);
		return chartHtml;
	}

	public static String readDataFromRunTimeEditTextField() {
		if (1 == 1) {
			return readDataFromRunTime_Parent_EditTextField();
		}
		RootPanel editMountPointTest = RootPanel.get(Drupal._GWTVIZ_MAIN_PANEL_EDIT);
		Element[] elements = DOMUtils.getElementsByClassName(_DRUPAL_EDIT_FIELD_NAME_FIELD_VISUAL_SPACE, editMountPointTest.getElement());
		if (elements != null && elements.length > 0) {
			Element[] field = DOMUtils.getElementsByClassName(_DRUPAL_EDIT_FIELD_ITEM_EVEN, elements[0]);
			if (field != null && field.length > 0) {
				String value = field[0].getInnerText();
				return value;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public static String readDataFromRunTime_Parent_EditTextField() {
		return getDataFromRunTime_Parent_EditTextField(_DRUPAL_EDIT_FIELD_NAME_FIELD_VISUAL_SPACE, _DRUPAL_EDIT_FIELD_ITEM_EVEN);
	}

	private static final native String getDataFromRunTime_Parent_EditTextField(String className1, String className2) /*-{
		var parentDocument = window.top.document;
		var elems = parentDocument.getElementsByClassName(className1);
		var field = elems[0].getElementsByClassName(className2);
		var text = field[0].innerHTML;
		return text;
		//		if (field != null) {
		//			var child = elem.firstChild;
		//			while (child) {
		//				// 1 == Element node
		//				if (child.nodeType == 1) {
		//					text += this.@com.google.gwt.dom.client.DOMImpl::getInnerText(Lcom/google/gwt/dom/client/Element;)(child);
		//				} else if (child.nodeValue) {
		//					text += child.nodeValue;
		//				}
		//				child = child.nextSibling;
		//			}
		//		}
		//		return text;

	}-*/;

	/*****************************************************************************/
	/*** Write to DESIGN/DEV TIME page ***/
	/*****************************************************************************/
	public static void writeDataToDevTimeEditTextField(String data) {
		if (1 == 1) {
			writeDataToDev_Parent_EditTextField(data);
		}
		RootPanel writePoint = RootPanel.get(Drupal._DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE_ADD_MORE_WRAPPER);
		if (writePoint != null) {
			Element[] elements = DOMUtils.getElementsByClassName(_DRUPAL_DEV__EDIT_TEXT_FULL_FORM_CONTROL_FORM_TEXTAREA, writePoint.getElement());
			if (elements != null && elements.length > 0) {
				elements[0].setInnerText(data);
			}
		}
	}

	public static void writeDataToDev_Parent_EditTextField(String data) {
		setDataToDev_Parent_EditTextField(data, _DRUPAL_DEV__EDIT_TEXT_FULL_FORM_CONTROL_FORM_TEXTAREA, _DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE_ADD_MORE_WRAPPER);
	}

	private static final native void setDataToDev_Parent_EditTextField(String data, String className, String id) /*-{
		var parentDocument = window.top.document;
		var elem = parentDocument.getElementById(id).getElementsByClassName(
				className);
		elem[0].innerHTML = data;
		//		while (elem.firstChild) {
		//			elem.removeChild(elem.firstChild);
		//		}
		//		// Add a new text node.
		//		if (text != null) {
		//			elem.appendChild(elem.ownerDocument.createTextNode(data));
		//		}
	}-*/;

	/*****************************************************************************/
	/*** READ from DESIGN/DEV TIME page ***/
	/*****************************************************************************/
	public static String readDataFromDevTimeEditTextField() {
		// RootPanel writePoint = RootPanel.get(Drupal._DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE_ADD_MORE_WRAPPER);
		if (1 == 1) {
			return readDataFromDev_Parent_EditTextField();
		}

		Element writePoint = Document.get().getElementById(Drupal._DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE_ADD_MORE_WRAPPER);
		if (writePoint != null) {
			Element[] elements = DOMUtils.getElementsByClassName(_DRUPAL_DEV__EDIT_TEXT_FULL_FORM_CONTROL_FORM_TEXTAREA, writePoint);
			if (elements != null && elements.length > 0) {
				return elements[0].getInnerText();
			}
		}
		return null;
	}

	public static String readDataFromDev_Parent_EditTextField() {
		return getDataFromDev_Parent_EditTextField(Drupal._DRUPAL_DEV__EDIT_TEXT_FULL_FORM_CONTROL_FORM_TEXTAREA,
				Drupal._DRUPAL_DEV__EDIT_FIELD_VISUAL_SPACE_ADD_MORE_WRAPPER);
	}

	private static final native String getDataFromDev_Parent_EditTextField(String className, String id) /*-{
		var parentDocument = window.top.document;
		var current = document;
		var elem = parentDocument.getElementById(id).getElementsByClassName(
				className);
		var text = elem[0].innerHTML;
		return text;
		//		var text = '', child = elem.firstChild;
		//		while (child) {
		//			// 1 == Element node
		//			if (child.nodeType == 1) {
		//				text += this.@com.google.gwt.dom.client.DOMImpl::getInnerText(Lcom/google/gwt/dom/client/Element;)(child);
		//			} else if (child.nodeValue) {
		//				text += child.nodeValue;
		//			}
		//			child = child.nextSibling;
		//		}		
	}-*/;

	public static final native BodyElement getTopWindowBody() /*-{
		return window.top.document.body;
	}-*/;
	

	public static final native int getTopWindowWidth() /*-{
		var w = window.top, d = window.top.document, e = d.documentElement, g = d
				.getElementsByTagName('body')[0], x = w.innerWidth
				|| e.clientWidth || g.clientWidth, y = w.innerHeight
				|| e.clientHeight || g.clientHeight;
		return x;
	}-*/;

	public static final native int getCurrentWindowWidth() /*-{
		var w = window;
		var d = window.document;
		var e = window.document.documentElement;
		var g = window.document.getElementsByTagName('body')[0];
		var x = w.innerWidth || e.clientWidth || g.clientWidth;
		return x;
	}-*/;

	public static final native int getTopWindowHeight() /*-{
		var w = window.top, d = window.top.document, e = d.documentElement, g = d
				.getElementsByTagName('body')[0], x = w.innerWidth
				|| e.clientWidth || g.clientWidth, y = w.innerHeight
				|| e.clientHeight || g.clientHeight;
		return y;
	}-*/;
}
