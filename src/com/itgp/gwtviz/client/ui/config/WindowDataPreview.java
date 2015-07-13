/* 
 * The MIT License
 *
 * Copyright 2015 InsiTech LLC.   gwtvis@insitechinc.com
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
package com.itgp.gwtviz.client.ui.config;

import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.itgp.gwtviz.client.ui.UIUtility;
import com.itgp.gwtviz.client.ui.widget.WindowBase;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.model.Item;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class WindowDataPreview extends WindowBase {
	
	Window myWindow;
	List<Item> displayData;
	String tableHTMLData;
	HorizontalLayoutContainer previewVLC;
	VerticalLayoutContainer windowPanel;
	GraphConfigImpl_1 graphConfigImpl;

	public WindowDataPreview(List<Item> displayData,GraphConfigImpl_1 graphConfigImpl) {
		super();
		this.myWindow = this;
		this.displayData =displayData;
		this.graphConfigImpl= graphConfigImpl;
		
		setResizable(false);
		init();
	}
	
	public WindowDataPreview(String tableHTMLData,GraphConfigImpl_1 graphConfigImpl) {
		super();
		myWindow = this;
		this.tableHTMLData = tableHTMLData;
		this.displayData =null;
		this.graphConfigImpl= graphConfigImpl;
	
		setResizable(false);
		init();
	}

	public void init() {
		setMaximizable(true);
		setResizable(true);
		setModal(true);
		
		setHeadingText("Data Preview of "+graphConfigImpl.getDataSourceName());
		//Viewport viewPort = new Viewport();
		windowPanel = new VerticalLayoutContainer();
		windowPanel.setId("windowPanel 1");


		windowPanel.setShadow(true);
		windowPanel.add(makePreviewTab(), new VerticalLayoutData(1, 1));
		windowPanel.add(makeButtons(),new VerticalLayoutData(1, -1));
		windowPanel.getElement().getStyle().setProperty("backgroundColor", "#F0F0F0");

		//viewPort.add(windowPanel);
		add(windowPanel);
	}
	
	public Widget makeButtons() {
	
		TextButton closeButton = new TextButton("Close");
		closeButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				myWindow.hide(); 
			}
		});

		HBoxLayoutContainer hlcButtons = new HBoxLayoutContainer();
		hlcButtons.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hlcButtons.setPack(BoxLayoutPack.END);

		hlcButtons.add(closeButton);
		
		return hlcButtons;
	}

	
	private Widget makePreviewTab() {
		previewVLC = new HorizontalLayoutContainer();
		previewVLC.setId("previewVLC 1");
		previewVLC.getScrollSupport().setScrollMode(ScrollMode.ALWAYS);
		makePreviewTable();
		return previewVLC;
	}
	
	private void makePreviewTable(){
		if(previewVLC.getWidgetCount()>0){
			previewVLC.remove(0);
		}
		
		String table  = "";
		if(displayData!=null){
			table= UIUtility.makeHTMLTable(displayData);
		}else{
			table = tableHTMLData;
		}
		
		HTML html = new HTML(table);
		//html.getElement().setId(Drupal._GWTVIZ_ITGP_PREVIEW_TABLE);
		previewVLC.add(html);
		previewVLC.forceLayout();
	}
}
