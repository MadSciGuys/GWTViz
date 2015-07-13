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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.itgp.gwtviz.client.ui.MainDesktop;
import com.itgp.gwtviz.client.ui.widget.WindowBase;
import com.itgp.gwtviz.shared.gconfig.AutoBeanItems;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.model.Item;
import com.itgp.gwtviz.shared.model.ItemUtil;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class WindowColumnSelect extends WindowBase {

	VerticalLayoutContainer windowPanel;
	ContentPanel parameterPanel;
	int gridPanelCounter = 0;

	TextField userNameTF;
	PasswordField passwordTF;
	Label signOnStatus;

	MainDesktop topWindow;
	Window myWindow;
	
	DualFieldSelector columnSelector;
	DualFieldSelector dataSelector;
	List<Item> columnData;
	List<Item> dataSelected;
	ItemGrid columnGrid;

	GraphConfigImpl_1 graphConfigImpl;
	INotify notifable;
	String defaultValue;
	
	public enum Type{Y_AXIS_COLUMN_SELECT,X_AXIS_SERIES_COLUMN}
	
	private Type type;

	public WindowColumnSelect(GraphConfigImpl_1 graphConfigImpl, INotify notifable, String defaultValue, Type type) {
		super();
		this.myWindow = this;
		this.graphConfigImpl = graphConfigImpl;
		this.notifable = notifable;
		this.defaultValue = defaultValue;
		this.type = type;

		setResizable(false);
		init();
	}

	public void init() {
		setMaximizable(true);
		setResizable(true);
		setModal(true);
		setHeadingText("Select Columns and Rows");
		//Viewport viewPort = new Viewport();
		windowPanel = new VerticalLayoutContainer();

		windowPanel.setShadow(true);
		windowPanel.add(makeRowsTab(), new VerticalLayoutData(1, 1));
		windowPanel.getElement().getStyle().setProperty("backgroundColor", "#F0F0F0");

		//viewPort.add(windowPanel);
		add(windowPanel);
	}

	public Widget makeRowsTab() {

		List<Item> filteredColumns = ItemUtil.convert(graphConfigImpl.getFilterColumns());
		if(Type.Y_AXIS_COLUMN_SELECT.equals(type)){
			filteredColumns.add(new Item(ItemUtil._ROW_COUNT,-1));
			columnGrid = new ItemGrid(filteredColumns, true, SelectionMode.SINGLE, null,ItemGrid.GridType.HIDE_CHECKBOX);
		}else if(Type.X_AXIS_SERIES_COLUMN.equals(type)){
			columnGrid = new ItemGrid(filteredColumns, true, SelectionMode.SINGLE, null,ItemGrid.GridType.REGULAR);
		}
		
		
		TextButton applyButton = new TextButton("Save and Close");
		applyButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<Item> selectedItems = columnGrid.getSelectedItems();
				AutoBeanItems aubI = ItemUtil.convert(selectedItems);
				NotifyPayload np = new NotifyPayload(myWindow,myWindow.getClass().getName(),aubI );
				np.setType(type.name());
				notifable.processNotify(np); 
			}
		});

		TextButton clearButton = new TextButton("Clear");
		clearButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				columnGrid.deselectAll();
			}
		});

		HBoxLayoutContainer hlcButtons = new HBoxLayoutContainer();
		hlcButtons.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hlcButtons.setPack(BoxLayoutPack.END);

		hlcButtons.add(applyButton);
		hlcButtons.add(clearButton);

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(columnGrid.asWidget(), new VerticalLayoutData(1, 1, new Margins(5)));
		vlc.add(hlcButtons, new VerticalLayoutData(1, -1, new Margins(1,0,18,5)));// , new VerticalLayoutData(1, -1, new Margins(5,0,5,16)));
		// vlc.add(dataSelector.asWidget(), new VerticalLayoutData(1, .5, new Margins(5)));
		vlc.forceLayout();
		
		//finally select value
		columnGrid.selectedItem(defaultValue);
		
		return vlc;
	}
}
