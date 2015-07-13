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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.itgp.gwtviz.client.ui.ConfigPanel_1;
import com.itgp.gwtviz.client.ui.MainDesktop;
import com.itgp.gwtviz.client.ui.UIUtility;
import com.itgp.gwtviz.client.ui.widget.WindowBase;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.gconfig.GraphConfigUtility;
import com.itgp.gwtviz.shared.model.Item;
import com.itgp.gwtviz.shared.model.ItemUtil;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class WindowDataSubset extends WindowBase {

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
	
	VerticalLayoutContainer previewVLC;

	private TabPanel tabPanelMain;
	private ConfigPanel_1 configPanel_1;
	private GraphConfigImpl_1 graphConfigImpl;
	//private String mRouteURL;
	
	public enum Type{CLEAR}
	
	private List<Item> filteredItems;
	private String initalConfigState;

	public WindowDataSubset(GraphConfigImpl_1 graphConfigImpl,ConfigPanel_1 configPanel_1, List<Item> filteredItems) {
		super();
		//this.mRouteURL =MainDesktop.getMRouteURL();
		this.graphConfigImpl = graphConfigImpl;
		this.configPanel_1  = configPanel_1;
		this.filteredItems = filteredItems;
		this.myWindow = this;
		
		setResizable(false);
		initalConfigState =GraphConfigUtility.toJson_v1(graphConfigImpl);
		MainDesktop.consoleLog("initalConfigState"+initalConfigState);
		init();	
	}
	
	public boolean hasChanges(){
		if(initalConfigState == null){
			return false;
		}
		String initialConfigFilter = "";
		GraphConfigImpl_1 initialConfig = (GraphConfigImpl_1)GraphConfigUtility.graphConfigImplfromJson(initalConfigState);
		List<Item> initialFilter = ItemUtil.convert(initialConfig.getFilterColumns());
		
		
		initialConfig.setFilterColumns(ItemUtil.convert(columnSelector.getRightColumnValues()));
		
		
		List<Item> existingFilter = ItemUtil.convert(initialConfig.getFilterColumns());
		
		
		
		return hasChanges(initialFilter,existingFilter);

//		MainDesktop.consoleLog("initialConfigFilter:"+initialConfigFilter);
//		MainDesktop.consoleLog("currentConfigFilter:"+currentConfigFilter);
//		if(initialConfigFilter.equals(currentConfigFilter)){
//			return false;
//		}else{
//			return true;
//		}
	}
	
	private boolean hasChanges(List<Item> srcList, List<Item> destList){
		
		if(srcList.containsAll(destList)){
			if(destList.containsAll(srcList)){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

	public void init() {
		// setTitle("Select Columns and Rows");
		setMaximizable(true);
		setResizable(true);
		// setLayout(new FitLayout());
		// setWidth(350);
		// setHeight(130);
		setModal(true);
		setHeadingText("Select Columns and Rows");

		//Viewport viewPort = new Viewport();

		windowPanel = new VerticalLayoutContainer();
		// windowPanel.setHeight("100%");
		windowPanel.setShadow(true);
		// windowPanel.setLayout(new RowLayout());
		// windowPanel.setBorder(true);

		// parameterPanel = getParameterPanel();
		// background-color: #F0F0F0;

		//viewPort.add(windowPanel);

		windowPanel.add(makeTabPanel(), new VerticalLayoutData(1, 1));
		windowPanel.add(getControlButtons(),new VerticalLayoutData(1, -1, new Margins(2, 0, 2, 0)));
		windowPanel.setId("DataSubsetWindow_windowPanel");
		// windowPanel.setStyleName("gwtviz.background.gray", true);
		windowPanel.getElement().getStyle().setProperty("backgroundColor", "#F0F0F0");
		// windowPanel.setBodyStyle("background-color:#F0F0F0;");
		setId("DataSubsetWindow");
		//ContentPanel cp = new ContentPanel();
		//cp.setWidget(viewPort);
		
		add(windowPanel);

	}

	private void syncDataOnSave(){
		if(dataSelected == null || dataSelected.size()==0){
			//if nothing is set to go back, 
			//lets make sure that nothing was selected on the column tab
			dataSelected = new ArrayList<Item>(columnSelector.getRightColumnValues());
		}
	}
	public HBoxLayoutContainer getControlButtons(){
		HBoxLayoutContainer hlc = new HBoxLayoutContainer();
		hlc.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hlc.setPack(BoxLayoutPack.END);
	    
		//hlc.setHBoxLayoutAlign(HBoxLayoutAlign.BOTTOM);
		TextButton resetButton = new TextButton("Reset");
		resetButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				GraphConfigUtility.clearGCI(graphConfigImpl);
				NotifyPayload np = new NotifyPayload(myWindow,myWindow.getClass().getName(),null);
				np.setType(Type.CLEAR.name());
				configPanel_1.processNotify(np);
				myWindow.hide();
			}
		});
		
		TextButton saveButton = new TextButton("Save");
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				int TabNumber = tabPanelMain.getWidgetIndex(tabPanelMain.getActiveWidget());
				syncDataOnSave();
				
				graphConfigImpl.setFilterColumns(ItemUtil.convert(dataSelected));	
				
				NotifyPayload np = new NotifyPayload(myWindow,myWindow.getClass().getName(),null);
				configPanel_1.processNotify(np);
				initalConfigState = GraphConfigUtility.toJson_v1(graphConfigImpl);
			}
		});
		
		TextButton closeButton = new TextButton("Close");
		closeButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				myWindow.hide();
			}
		});
		
		TextButton changesButton = new TextButton("changes");
		changesButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				GraphConfigImpl_1 temp = GraphConfigUtility.makeGraphConfigImpl_1();
				temp.setFilterColumns(ItemUtil.convert(new ArrayList<Item>(columnSelector.getRightColumnValues())));
				String s =GraphConfigUtility.toJson_v1(temp);
				
				String newVersion = "Changes "+dataSelector.hasChanges()+"  Data: "+s;
				String newOld= GraphConfigUtility.toJson_v1(graphConfigImpl);
				com.google.gwt.user.client.Window.alert(newVersion);
				MainDesktop.consoleLog("NEW:"+newVersion);
				MainDesktop.consoleLog("OLD:"+newOld);
				
			}
		});
		
//		TextButton printButton = new TextButton("Print");
//		printButton.addSelectHandler(new SelectHandler() {
//			@Override
//			public void onSelect(SelectEvent event) {
//				System.out.println(GraphConfigUtility.toJson_v1(graphConfigImpl));			
//			}
//		});
		//hlc.getLayoutData();
		//BoxLayoutData b;
		//hlc.setHBoxLayoutAlign(HBoxLayoutAlign.);
		hlc.add(resetButton,new BoxLayoutData());
		hlc.add(saveButton);
		hlc.add(closeButton);
		//hlc.add(changesButton);
		//hlc.add(printButton);
		
				
		
		return hlc;

	}
	private TabPanel makeTabPanel() {
		tabPanelMain = new TabPanel();
		// tabPanelMain.setPixelSize(600, 250);
		// tabPanelMain.setHeight("100%");
		tabPanelMain.setAnimScroll(true);
		tabPanelMain.setTabScroll(true);
		tabPanelMain.setCloseContextMenu(true);
		tabPanelMain.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				TabPanel panel = (TabPanel) event.getSource();
				Widget w = event.getSelectedItem();
				int tabID = panel.getWidgetIndex(w);
				if (tabID == 1) {
					Item item = columnGrid.getSelectedItem();
					loadRowData();
					columnGrid.selectedItem(item);
				}else if(tabID ==2){
					makePreviewTable();
				}
			}
		});

		addMyTab(makeColumnsTab(), "Columns");
		addMyTab(makeRowsTab(), "Rows");
		addMyTab(makePreviewTab(), "Filter Summary");

		tabPanelMain.setActiveWidget(tabPanelMain.getWidget(0));
		return tabPanelMain;
	}

	private void addMyTab(IsWidget w, String tabName) {
		tabPanelMain.add(w, new TabItemConfig(tabName, false));
		// tabPanelMain.setActiveWidget(tabPanelMain.getWidget(tabPanelMain.getWidgetCount() - 1));
	}

	public Widget makeColumnsTab() {
		ContentPanel cp = new ContentPanel();
		cp.getHeader().hide();

//		MRoute mroute = new MRoute();
//		AutoBean<Columns> columnsBean = AutoBeanCodex.decode(mroute.columnsFactory, Columns.class, MRoute.getJsonColumns());
//		Columns columns = columnsBean.as();
//		columnData = ItemUtil.convert(columns);

		columnSelector = new DualFieldSelector(columnData, "Filter Columns...",graphConfigImpl,DualFieldSelector.SortType.NUMERIC,DualFieldSelector.SortType.NONE,null);
		cp.setWidget(columnSelector);
		
		columnSelector.setData(filteredItems);
		
		//ServerComm.getAllMRouteColumns(mRouteURL, graphConfigImpl.getDataSourceID(), columnSelector);

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(columnSelector.asWidget(), new VerticalLayoutData(1, 1, new Margins(5)));
		vlc.forceLayout();
		return vlc;
	}

	public Widget makeRowsTab() {
		// ContentPanel cp = new ContentPanel();
		// cp.getHeader().hide();

		// MRoute mroute = new MRoute();
		// AutoBean<Data> dataBean = AutoBeanCodex.decode(mroute.dataFactory, Data.class, MRoute.getJsonData());
		// Data data = dataBean.as();
		dataSelected = new ArrayList<Item>();
		dataSelector = new DualFieldSelector(dataSelected, "Filter Data...",graphConfigImpl,DualFieldSelector.SortType.ALPHA,DualFieldSelector.SortType.ALPHA,null);
		columnGrid = new ItemGrid(dataSelected, true, SelectionMode.SINGLE, dataSelector,ItemGrid.GridType.HIDE_CHECKBOX);
		dataSelector.setINotify(columnGrid);
		
		
//		TextButton applyButton = new TextButton("Apply");
//		applyButton.addSelectHandler(new SelectHandler() {
//			@Override
//			public void onSelect(SelectEvent event) {
//				columnGrid.saveCurrentSelection();			
//			}
//		});
//		

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(dataSelector.asWidget(), new VerticalLayoutData(1, .5, new Margins(5)));
		vlc.add(columnGrid.asWidget(), new VerticalLayoutData(1, .5, new Margins(5)));
		//vlc.add(applyButton, new VerticalLayoutData(1, -1, new Margins(5,0,5,16)));
		vlc.forceLayout();
		// cp.setWidget(vlc);

		return vlc;
	}

	private void loadRowData() {
		dataSelected.clear();
		dataSelected.addAll(columnSelector.getRightColumnValues());
		//dataSelector.setLeftColumnData(dataSelected);//,true);

		columnGrid.setData(dataSelected);

	}

	private Widget makePreviewTab() {
		previewVLC = new VerticalLayoutContainer();
		previewVLC.getScrollSupport().setScrollMode(ScrollMode.ALWAYS);
		
		HBoxLayoutContainer hlc = new HBoxLayoutContainer();
		hlc.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		//hlc.setPack(BoxLayoutPack.END);
	    
		
		
		TextButton downloadPreviewButton = new TextButton("Download Data");
		downloadPreviewButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				makeAndDownloadPreviewCSV();
			}
		});
		hlc.add(downloadPreviewButton);
		
		previewVLC.add(hlc,new VerticalLayoutData(1, -1,new Margins(0, 0, 0, 0)));

		return previewVLC;
	}
	
	private void makePreviewTable(){
		if(previewVLC.getWidgetCount()>1){
			previewVLC.remove(1);
		}
		String table  = UIUtility.makeHTMLTable(columnSelector.getRightColumnValues());
		HTML html = new HTML(table);
		previewVLC.add(html);
		previewVLC.forceLayout();
		
	}
	
	private void makeAndDownloadPreviewCSV(){
		String scv  = UIUtility.makeCSV(columnSelector.getRightColumnValues());
		configPanel_1.setHREFinDownloadElementAnchor(scv);
		configPanel_1.clickDownloadLink();
	}
	
}
