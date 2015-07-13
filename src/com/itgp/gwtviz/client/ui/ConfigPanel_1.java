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
package com.itgp.gwtviz.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.itgp.gwtviz.client.comm.ServerComm;
import com.itgp.gwtviz.client.ui.UIUtility.Charts;
import com.itgp.gwtviz.client.ui.config.INotify;
import com.itgp.gwtviz.client.ui.config.ItemGrid;
import com.itgp.gwtviz.client.ui.config.NotifyPayload;
import com.itgp.gwtviz.client.ui.config.WindowColumnSelect;
import com.itgp.gwtviz.client.ui.config.WindowDataSubset;
import com.itgp.gwtviz.shared.gconfig.AutoBeanItem;
import com.itgp.gwtviz.shared.gconfig.AutoBeanItems;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.gconfig.GraphConfigUtility;
import com.itgp.gwtviz.shared.gconfig.GraphConfigUtility.YAxisSort;
import com.itgp.gwtviz.shared.model.ChartDesc;
import com.itgp.gwtviz.shared.model.Item;
import com.itgp.gwtviz.shared.model.ItemUtil;
import com.itgp.gwtviz.shared.model.MRoute;
import com.itgp.gwtviz.shared.model.MRoute.Docs;
import com.itgp.gwtviz.shared.model.MRoute.File;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ConfigPanel_1 implements INotify {

	private static final String emptyFilesComobBoxText = "Select Data Source";
	private static final String emptyChartComobBoxText = "Select Chart";
	static final int columnWidth = 220;
	ContentPanel parentContentPanel;

	SimpleComboBox<String> dataSourceComboBox;
	TextButton selectSubsetDataTB;
	TextButton viewDataTB;
	TextButton downloadDataTB;
	SimpleComboBox<String> chartComboBox;
	Element downloadElementAnchor;

	// SimpleComboBox<String> yAxisAggregateTypeComboBox;
	SimpleComboBox<String> yAxisColumnComboBox;
	TextButton yAxisColumnButton;
	private static final String yAxisColumnLabelDefaultTxt = "Click to Choose Y Column";
	private static final String xAxisMultiSeriesColumnDefaultTxt = "Click to Choose Multi-Series Column";
	TextField yAxisTitle;
	SimpleComboBox<String> yAxisSortComboBox;

	// SimpleComboBox<String> xAxisColumnComboBox;
	// SimpleComboBox<String> xAxisRotateLabelsComboBox;
	// SimpleComboBox<String> xAxisReduceLabelsComboBox;

	ItemGrid xAxisColumnGrid;
	TextButton xAxisMultiSeriesColumn;

	GraphConfigImpl_1 graphConfigImpl;

	public static Docs docFiles;
	FieldLabel labelY;

	ConfigPanel_1 configPanel_1;
	List<Item> xAxisColumns;

	HashMap<String, GraphConfigImpl_1> configCache = new HashMap<String, GraphConfigImpl_1>();

	FieldSet fieldSetMetaData;
	FieldSet fieldSetYAxis;
	FieldSet fieldSetXAxis;

	FieldLabel yAxisSortComobBoxLabel;

	public ConfigPanel_1() {
		configPanel_1 = this;
		docFiles = MainDesktop.getMRouteDocs();

		// // **************************************************
		// // First get the config from the drupal page.
		// // If it is not there lets create it
		// // **************************************************
		// String configString = Drupal.readDataFromDevTimeEditTextField();
		// if (configString == null || configString.length() == 0) {
		// graphConfigImpl = (new GraphConfigUtility()).makeGraphConfigImpl_1();
		// } else {
		// graphConfigImpl = (GraphConfigImpl_1) GraphConfigUtility.graphConfigImplfromJson(configString);
		// }

	}

	public void setParentContentPanel(ContentPanel parentContentPanel) {
		this.parentContentPanel = parentContentPanel;
	}

	public ContentPanel getParentContentPanel() {
		return parentContentPanel;
	}

	public void initWithData(String configText) {
		// **************************************************
		// First get the config from the drupal page.
		// If it is not there lets create it
		// **************************************************
		// String configString = Drupal.readDataFromDevTimeEditTextField();
		MainDesktop.consoleLog("initWithData: " + configText);
		if (configText == null || configText.length() == 0) {
			graphConfigImpl = GraphConfigUtility.makeGraphConfigImpl_1();
		} else {
			graphConfigImpl = (GraphConfigImpl_1) GraphConfigUtility.graphConfigImplfromJson(configText);
		}
	}

	public VerticalLayoutContainer makeVisMainMenu() {

		VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();
		vlcMain.setId("makeVisMainMenu VerticalLayoutContainer");

		fieldSetMetaData = new FieldSet();
		fieldSetMetaData.setId("makeVisMainMenu fieldSetMetaData");
		fieldSetMetaData.setHeadingText("Meta Data");
		fieldSetMetaData.setCollapsible(true);
		fieldSetMetaData.add(makeMetaDataPanel());

		fieldSetYAxis = new FieldSet();
		fieldSetYAxis.setHeadingText("Y-Axis");
		fieldSetYAxis.setCollapsible(true);
		fieldSetYAxis.add(makeYAxisPanel());

		fieldSetXAxis = new FieldSet();
		fieldSetXAxis.setHeadingText("X-Axis");
		fieldSetXAxis.setCollapsible(true);
		fieldSetXAxis.add(makeXAxisPanel());

		vlcMain.add(fieldSetMetaData);// ,new VerticalLayoutData(-1, -1, new Margins(1)));
		vlcMain.add(fieldSetYAxis);// ,new VerticalLayoutData(-1, -1, new Margins(1)));
		vlcMain.add(fieldSetXAxis, new VerticalLayoutData(-1, -1));// ,new VerticalLayoutData(1, 300)

		TextButton plotTB = new TextButton("Plot");
		plotTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				plot();
			}
		});
		TextButton writeTest = new TextButton("testWrite");
		writeTest.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				testWrite();
			}
		});

		// TextButton readTest = new TextButton("testREAD");
		// readTest.addSelectHandler(new SelectHandler() {
		// @Override
		// public void onSelect(SelectEvent event) {
		// String config = Drupal.readDataFromEditTextField();
		// System.out.println("READ IN :"+config);
		// graphConfigImpl = (GraphConfigImpl_1)GraphConfigUtility.graphConfigImplfromJson(config);
		// }
		// });

		vlcMain.add(plotTB, new VerticalLayoutData(150, -1, new Margins(3, 3, 3, 3)));
		if (MainDesktop.IN_DEBUG_MODE) {
			vlcMain.add(writeTest, new VerticalLayoutData(150, -1, new Margins(0, 3, 3, 3)));
		}
		// vlcMain.add(readTest, new VerticalLayoutData(150, -1, new Margins(3, 3, 20, 3)));

		vlcMain.forceLayout();

		return vlcMain;

	}

	protected VerticalLayoutContainer makeYAxisPanel() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();

		yAxisColumnButton = new TextButton("Click to Choose Y Column");
		yAxisColumnButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String defaultValue = "";
				if (!yAxisColumnButton.getText().equals(yAxisColumnLabelDefaultTxt)) {
					defaultValue = yAxisColumnButton.getText();
				}
				WindowColumnSelect csw = new WindowColumnSelect(graphConfigImpl, configPanel_1, defaultValue, WindowColumnSelect.Type.Y_AXIS_COLUMN_SELECT);
				csw.show();
				csw.centerWindow(parentContentPanel, 0.3);
				csw.toFront();
				csw.forceLayout();

			}
		});

		yAxisTitle = new TextField();

		FieldLabel yAxisColumnLabel = new FieldLabel(yAxisColumnButton, "Y Axis Column");
		yAxisColumnLabel.setLabelAlign(LabelAlign.LEFT);

		labelY = new FieldLabel(yAxisTitle, "Y Axis Label");
		labelY.setLabelAlign(LabelAlign.LEFT);

		// Make yAxis Sort ComboBox
		yAxisSortComboBox = new SimpleComboBox<String>(new StringLabelProvider<String>());
		yAxisSortComboBox.setWidth(getChildInPanelWidth());
		yAxisSortComboBox.setTriggerAction(TriggerAction.ALL);
		yAxisSortComboBox.setEmptyText(YAxisSort.NoSort.name());

		for (String s : UIUtility.getSortChoices()) {
			yAxisSortComboBox.add(s);
		}

		yAxisSortComobBoxLabel = new FieldLabel(yAxisSortComboBox, "Y Axis Sort");

		// yAxisColumnLabel.setStyleName(yAxisTitle.getStyleName());
		// hlcYAxis.add(labelY, new BoxLayoutData(new Margins(1, 2, 1, 20)));
		vlc.add(yAxisColumnLabel);
		vlc.add(labelY);
		vlc.add(yAxisSortComobBoxLabel);

		return vlc;
	}

	protected VerticalLayoutContainer makeXAxisPanel() {
		xAxisColumns = new ArrayList<Item>();
		VerticalLayoutContainer v1 = new VerticalLayoutContainer();
		// HorizontalLayoutContainer hlcXAxis = new HorizontalLayoutContainer();
		//
		// VBoxLayoutContainer verticalxAxisContainer = new VBoxLayoutContainer();

		xAxisColumnGrid = new ItemGrid(xAxisColumns, true, SelectionMode.MULTI, null, ItemGrid.GridType.XAXIS_LABEL);
		// xAxisColumnComboBox = new SimpleComboBox<String>(new StringLabelProvider<String>());
		// xAxisColumnComboBox.setWidth(getChildInPanelWidth());
		// xAxisColumnComboBox.setTriggerAction(TriggerAction.ALL);
		// xAxisColumnComboBox.setEmptyText("X Column Selection");

		FieldLabel xAxisColumnLabel = new FieldLabel(xAxisColumnGrid.asWidget(), "X Axis Columns");
		xAxisColumnLabel.setLabelAlign(LabelAlign.LEFT);

		xAxisMultiSeriesColumn = new TextButton(xAxisMultiSeriesColumnDefaultTxt);
		xAxisMultiSeriesColumn.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String defaultValue = "";
				if (!xAxisMultiSeriesColumn.getText().equals(xAxisMultiSeriesColumnDefaultTxt)) {
					defaultValue = xAxisMultiSeriesColumn.getText();
				}
				WindowColumnSelect csw = new WindowColumnSelect(graphConfigImpl, configPanel_1, defaultValue, WindowColumnSelect.Type.X_AXIS_SERIES_COLUMN);
				csw.show();
				csw.centerWindow(parentContentPanel, 0.3);
				csw.toFront();
				csw.forceLayout();
			}
		});

		FieldLabel labelX = new FieldLabel(xAxisMultiSeriesColumn, "Multi Series Column");
		labelX.setLabelAlign(LabelAlign.LEFT);

		v1.add(labelX);
		v1.add(xAxisColumnLabel, new VerticalLayoutData(1, 400, new Margins(5, 0, 0, 5)));

		// hlcXAxis.add(verticalxAxisContainer, new HorizontalLayoutData(-1, -1));
		// hlcXAxis.add(xAxisColumnLabel, new HorizontalLayoutData(1, 400, new Margins(5, 0, 0, 5)));
		// v1.add(hlcXAxis, new VerticalLayoutData(-1, 400));
		return v1;

	}

	private long getDocFileId(String text) {
		if (text != null && text.length() > 0 && !text.equals(dataSourceComboBox)) {
			return MRoute.getDocFileId(docFiles, text);
		} else {
			return -1l;
		}

	}

	protected HBoxLayoutContainer makeMetaDataPanel() {
		final HBoxLayoutContainer hlcMetaData = new HBoxLayoutContainer();
		// hlcMetaData.setEnableOverflow(false);
		// hlcMetaData.getElement().removeAttribute("overflow");
		// hlcMetaData.getElement().setAttribute("overflow", "visible");
		hlcMetaData.setId("makeMetaDataPanel__HBoxLayoutContainer");
		dataSourceComboBox = new SimpleComboBox<String>(new StringLabelProvider<String>());
		dataSourceComboBox.setWidth(getChildInPanelWidth());
		dataSourceComboBox.setTriggerAction(TriggerAction.ALL);
		dataSourceComboBox.setEmptyText(emptyFilesComobBoxText);
		for (File f : docFiles.getFiles()) {
			dataSourceComboBox.add(f.getMAlias());
		}

		dataSourceComboBox.addSelectionHandler(new SelectionHandler<String>() {
			@Override
			public void onSelection(SelectionEvent<String> event) {
				String fileName = event.getSelectedItem();
				if (fileName != null && fileName.length() > 0) {

					long id = getDocFileId(fileName);
					MainDesktop.consoleLog("dataSourceComboBox 2 :" + id);
					if (graphConfigImpl != null) {
						MainDesktop.consoleLog("dataSourceComboBox 2.1 graphConfigImpl NOT NULL");
						MainDesktop.consoleLog("dataSourceComboBox 2.2 :" + graphConfigImpl.getDataSourceID());
					} else {
						MainDesktop.consoleLog("dataSourceComboBox 2.3 graphConfigImpl IS NULL");
					}

					if (graphConfigImpl.getDataSourceID() != id && graphConfigImpl.getDataSourceID() != -1) {
						saveCurrentStateToConfig();
						configCache.put(graphConfigImpl.getDataSourceName(), graphConfigImpl);

						if (configCache.containsKey(fileName)) {
							graphConfigImpl = configCache.get(fileName);
						} else {
							graphConfigImpl = GraphConfigUtility.makeGraphConfigImpl_1();
							graphConfigImpl.setDataSourceID(id);
							graphConfigImpl.setDataSourceName(fileName);
						}
						MainDesktop.setCurrentDataModel(null, null);
						// GraphConfigUtility.clearGCI(graphConfigImpl);
						readCurrentStateFromConfig();
					} else {
						graphConfigImpl.setDataSourceID(id);
						graphConfigImpl.setDataSourceName(fileName);
					}
				}
			}
		});

		selectSubsetDataTB = new TextButton("Select Subset of Data");
		selectSubsetDataTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				dataSourceComboBox.select(dataSourceComboBox.getSelectedIndex());

				MakeFilterWindow mfw = new MakeFilterWindow(configPanel_1, graphConfigImpl);
				mfw.showFilterWindow();
			}
		});

		VerticalLayoutContainer vlcMMetaDataButtons = new VerticalLayoutContainer();

		viewDataTB = new TextButton("View Data");
		viewDataTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				MakePreviewTable mpt = new MakePreviewTable(configPanel_1, graphConfigImpl);
				mpt.showPreviewWindow();
			}
		});

		Element anchorDiv = DOM.createDiv();
		anchorDiv.setId("anchorDiv");
		makeDownloadElementAnchor();
		anchorDiv.appendChild((Element) downloadElementAnchor.cast());
		HTML anchorHtml = new HTML(anchorDiv.getString());
		anchorHtml.setVisible(false);

		downloadDataTB = new TextButton("Download Data");

		downloadDataTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {

				// MakeCSVFile uses downloaded data if it is there or downloads it.
				// Then it issues a click to the download link.

				MakeCSVFile makeCSVFile = new MakeCSVFile(configPanel_1, graphConfigImpl);
				makeCSVFile.getFileData();

				// Element e = DOM.getElementById(Drupal._DRUPAL_GWTVIZ_DOWNLOAD_LINK);
				// downloadLink(e);
				// http://stackoverflow.com/questions/25718817/gwt-download-file-from-client-side-without-server-intervention
				// http://stackoverflow.com/questions/4703014/gwt-button-acting-as-hyperlink

			}
		});

		TextButton testTB = new TextButton("Test");
		testTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				/*
				 * WindowTest wdp = new WindowTest(Window.getClientWidth(), Window.getClientHeight()); wdp.show(); wdp.center(); wdp.toFront();
				 * wdp.forceLayout();
				 */
				// com.sencha.gxt.widget.core.client.Window window = new com.sencha.gxt.widget.core.client.Window();
				// window.setMaximizable(true);
				// window.setResizable(true);
				// window.setModal(true);
				// window.forceLayout();
				//
				// window.setModal(true);
				// window.show();

				// WindowDataPreview2 wdp = new WindowDataPreview2(MainDesktop.getCurrentDataModel(), graphConfigImpl, Window.getClientWidth(), Window
				// .getClientHeight());
				// wdp.show();
				// wdp.center();
				// wdp.toFront();
				// wdp.forceLayout();
			}
		});

		vlcMMetaDataButtons.add(viewDataTB, new VerticalLayoutData(1, -1, new Margins(0, 0, 2, 0)));
		vlcMMetaDataButtons.add(downloadDataTB, new VerticalLayoutData(1, -1, new Margins(0, 0, 2, 0)));
		// vlcMMetaDataButtons.add(testTB, new VerticalLayoutData(1, -1));

		vlcMMetaDataButtons.add(anchorHtml, new VerticalLayoutData(50, 20));

		chartComboBox = new SimpleComboBox<String>(new StringLabelProvider<String>());
		chartComboBox.setWidth(getChildInPanelWidth());
		chartComboBox.setTriggerAction(TriggerAction.ALL);
		chartComboBox.setEmptyText(emptyChartComobBoxText);

		for (ChartDesc cd : UIUtility.getChartsList()) {
			chartComboBox.add(cd.getName());
		}

		chartComboBox.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(SelectionEvent<String> event) {
				// TODO Auto-generated method stub
				int z = 0;
				z++;
				Charts selectedChart = UIUtility.getChartEnum(event.getSelectedItem());
				applyChartSetting(selectedChart);
			}
		});

		hlcMetaData.add(dataSourceComboBox, new BoxLayoutData(new Margins(1, 2, 1, 2)));
		hlcMetaData.add(selectSubsetDataTB, new BoxLayoutData(new Margins(0, 2, 1, 2)));
		hlcMetaData.add(vlcMMetaDataButtons, new BoxLayoutData(new Margins(0, 2, 1, 2)));
		hlcMetaData.add(chartComboBox, new BoxLayoutData(new Margins(1, 2, 1, 2)));

		return hlcMetaData;

	}

	private void makeDownloadElementAnchor() {
		downloadElementAnchor = Document.get().createElement("a");
		downloadElementAnchor.setAttribute("download", "data.csv");
		downloadElementAnchor.setClassName("gwt-Anchor");
		downloadElementAnchor.setId(Drupal._DRUPAL_GWTVIZ_DOWNLOAD_LINK);
		// setHREFinDownloadElementAnchor("");
	}

	public void setHREFinDownloadElementAnchor(String plainTxt) {
		String encodedString = "data:text/plain;charset=utf-8," + UriUtils.encode(plainTxt);
		// downloadElementAnchor.setAttribute("href","XXXX");
		// downloadElementAnchor.setAttribute("href", encodedString);
		Element e = DOM.getElementById(Drupal._DRUPAL_GWTVIZ_DOWNLOAD_LINK);
		e.setAttribute("href", encodedString);
		e.setAttribute("download", makeDownloadFileName(graphConfigImpl.getDataSourceName()));

		String s = "1";
		s = s + "1";
	}

	private String makeDownloadFileName(String originalFileName) {
		if (originalFileName != null && originalFileName.length() > 0) {
			if (!originalFileName.toLowerCase().endsWith("csv")) {
				originalFileName = originalFileName + ".csv";
				return originalFileName;
			} else {
				return originalFileName;
			}
		} else {
			return "data.csv";
		}
	}

	public void clickDownloadLink() {
		Element e = DOM.getElementById(Drupal._DRUPAL_GWTVIZ_DOWNLOAD_LINK);
		downloadLinkClickNative(e);
	}

	public static native void setHRef(JavaScriptObject jso, String data) /*-{
		console.log("setHRef:" + jso);
		link.setAttribute('href', data);
	}-*/;

	public static native void downloadLinkClickNative(JavaScriptObject jso) /*-{
		console.log("me:" + jso);
		jso.click();
	}-*/;

	public int getPanelWidth() {
		return columnWidth;
	}

	public int getChildInPanelWidth() {
		return columnWidth - 2;
	}

	private void setYAxisFields(String columnName, String columnLabel) {

		if (columnName == null || columnName.length() == 0) {
			yAxisColumnButton.setText(yAxisColumnLabelDefaultTxt);
		} else {
			yAxisColumnButton.setText(columnName);
		}

		if (columnLabel == null || columnLabel.length() == 0) {
			yAxisTitle.setText("");
		} else {
			yAxisTitle.setText(columnLabel);
		}

		Scheduler.get().scheduleDeferred(new Command() {
			public void execute() {
				int width = yAxisTitle.getElement().getClientWidth();
				int width2 = yAxisColumnButton.getElement().getWidth(false);
				width2 = width2 + (int) (width2 * 0.12);
				// System.out.println(" Scheduler width " + width + "  : " + yAxisColumnButton.getElement().getWidth(false));
				yAxisTitle.setWidth(width2);
			}
		});

	}

	@Override
	public void processNotify(NotifyPayload notifyPayload) {
		if (notifyPayload.getWindowName().equals(WindowColumnSelect.class.getName())) {
			notifyPayload.getWindow().hide();
			AutoBeanItems aubI = (AutoBeanItems) notifyPayload.getData();

			if (WindowColumnSelect.Type.Y_AXIS_COLUMN_SELECT.name().equals(notifyPayload.getType())) {
				// if Something is there...
				if (aubI.getData() != null && aubI.getData().size() > 0) {
					String columnName = aubI.getData().get(0).getValue();
					String oldYColumnName = yAxisColumnButton.getText();
					setYAxisFields(columnName, columnName);

					xAxisColumnGrid.removeItem(columnName);
					if (xAxisMultiSeriesColumn.getText().equals(columnName)) {
						xAxisMultiSeriesColumn.setText(xAxisMultiSeriesColumnDefaultTxt);
					}
					if (!oldYColumnName.equals(yAxisColumnLabelDefaultTxt)) {

						putItemBack(xAxisColumnGrid, oldYColumnName);
					}
				} else {
					// Field has been cleared.
					String oldYColumnName = yAxisColumnButton.getText();
					setYAxisFields(null, null);
					putItemBack(xAxisColumnGrid, oldYColumnName);
				}

			} else if (WindowColumnSelect.Type.X_AXIS_SERIES_COLUMN.name().equals(notifyPayload.getType())) {
				if (aubI.getData() != null && aubI.getData().size() > 0) {
					String columnName = aubI.getData().get(0).getValue();
					String oldXColumnName = xAxisMultiSeriesColumn.getText();
					xAxisMultiSeriesColumn.setText(columnName);
					xAxisColumnGrid.removeItem(columnName);
					if (yAxisColumnButton.getText().equals(columnName)) {
						setYAxisFields(yAxisColumnLabelDefaultTxt, "");
						// yAxisColumnButton.setText(yAxisColumnLabelDefaultTxt);
						// yAxisTitle.setText("");
					}
					if (!oldXColumnName.equals(xAxisMultiSeriesColumnDefaultTxt)) {
						putItemBack(xAxisColumnGrid, oldXColumnName);
					}
				} else {
					if (!xAxisMultiSeriesColumn.getText().equals(xAxisMultiSeriesColumnDefaultTxt)) {
						putItemBack(xAxisColumnGrid, xAxisMultiSeriesColumn.getText());
						xAxisMultiSeriesColumn.setText(xAxisMultiSeriesColumnDefaultTxt);
					}
				}
			}
		} else if (notifyPayload.getWindowName().equals(WindowDataSubset.class.getName())) {
			if (WindowDataSubset.Type.CLEAR.name().equals(notifyPayload.getType())) {
				readCurrentStateFromConfig();
			} else if (graphConfigImpl.getFilterColumns() != null) {
				List<Item> filteredItems = ItemUtil.convert(graphConfigImpl.getFilterColumns());

				// First fix up labels.
				for (Item i : filteredItems) {
					i.setLabel(i.getValue());
				}

				// 2nd Lets make sure Y column is still there.
				String selectedYColumnName = yAxisColumnButton.getText();
				Item itemFound = null;
				for (Item i : filteredItems) {
					if (i.getValue().equals(selectedYColumnName)) {
						itemFound = i;
					}
				}
				if (itemFound == null) {
					// reset yAxis
					yAxisColumnButton.setText(yAxisColumnLabelDefaultTxt);
					yAxisTitle.setText("");
				} else {
					filteredItems.remove(itemFound);
				}

				// 3rd Lets make sure Y column is still there.
				String selectedXMultiSeries = xAxisMultiSeriesColumn.getText();
				itemFound = null;
				for (Item i : filteredItems) {
					if (i.getValue().equals(selectedXMultiSeries)) {
						itemFound = i;
					}
				}
				if (itemFound == null) {
					// reset xAxis
					xAxisMultiSeriesColumn.setText(xAxisMultiSeriesColumnDefaultTxt);
				} else {
					filteredItems.remove(itemFound);
				}

				List<Item> excludedList = getExcludedItems();
				List<Item> exisingItems = xAxisColumnGrid.getAllData();
				List<Item> finalList = mergeLists(exisingItems, filteredItems, excludedList);
				for (Item item : finalList) {
					ItemUtil.setDefaults(item);
				}
				xAxisColumnGrid.setData(finalList);
				xAxisColumnGrid.selectedItems();
			}
		}
	}

	private List<Item> getExcludedItems() {
		List<Item> excludedList = new ArrayList<Item>();

		if (!xAxisMultiSeriesColumn.getText().equals(xAxisMultiSeriesColumnDefaultTxt)) {
			excludedList.add(new Item(xAxisMultiSeriesColumn.getText(), 1));
		}
		if (!yAxisColumnButton.getText().equals(yAxisColumnLabelDefaultTxt)) {
			excludedList.add(new Item(yAxisColumnButton.getText(), 1));
		}

		return excludedList;
	}

	private void putItemBack(ItemGrid itemGrid, String columnNameToPutBack) {

		List<Item> gridList = itemGrid.getAllData();
		List<Item> filteredItems = ItemUtil.convert(graphConfigImpl.getFilterColumns());

		Item itemFoundToPutBack = null;
		for (Item i : filteredItems) {
			if (i.getValue().equals(columnNameToPutBack)) {
				itemFoundToPutBack = i;
				itemFoundToPutBack.setSelected(false);
			}
		}

		itemFoundToPutBack.setLabel(itemFoundToPutBack.getValue());

		Item itemFound = null;
		for (Item i : gridList) {
			if (i.equals(itemFoundToPutBack)) {
				itemFound = i;
			}
		}
		if (itemFound == null) {
			ItemUtil.setDefaults(itemFoundToPutBack);
			itemGrid.addData(itemFoundToPutBack);
		}

	}

	private List<Item> mergeLists(List<Item> existingList, List<Item> newList, List<Item> excludedList) {
		List<Item> resultList = new ArrayList<Item>(newList);
		resultList.removeAll(excludedList);

		for (Item item : resultList) {
			item.setSelected(false);
			for (int i = 0; i < existingList.size(); i++) {
				if (existingList.get(i).equals(item)) {
					item.copyInto(existingList.get(i));
				}
			}
		}

		return resultList;
	}

	public void readCurrentStateFromConfig() {
		if (graphConfigImpl != null) {
			dataSourceComboBox.setText(graphConfigImpl.getDataSourceName());
			chartComboBox.setText(graphConfigImpl.getChartType());
			setYAxisFields(graphConfigImpl.getYColumn(), graphConfigImpl.getYColumnLabel());
			xAxisColumnGrid.setData(ItemUtil.convert(graphConfigImpl.getXColumns()));
			xAxisColumnGrid.selectedItems();
			xAxisMultiSeriesColumn.setText(xAxisMultiSeriesColumnDefaultTxt);
			if (graphConfigImpl.getOptions() != null && graphConfigImpl.getOptions().getData() != null) {
				for (AutoBeanItem abi : graphConfigImpl.getOptions().getData()) {
					if (abi.getLabel().equals(GraphConfigUtility.Options.MultiSeriesBreakColumn.name())) {
						xAxisMultiSeriesColumn.setText(abi.getValue());
					} else if (abi.getLabel().equals(GraphConfigUtility.Options.YAxisSort.name())) {
						yAxisSortComboBox.setText(abi.getValue());
					}
				}
			}
			String savedChartTXT = UIUtility.getChartDisplayText(graphConfigImpl.getChartType());
			chartComboBox.setText(savedChartTXT);
		}
	}

	private String saveCurrentStateToConfig() {

		graphConfigImpl.setYColumnLabel(yAxisTitle.getText());
		graphConfigImpl.setYColumn(yAxisColumnButton.getText());
		graphConfigImpl.setXColumns(ItemUtil.convertShallow(xAxisColumnGrid.getAllData()));

		GraphConfigUtility.saveConfigOption(xAxisMultiSeriesColumn.getText(), xAxisMultiSeriesColumnDefaultTxt, graphConfigImpl,
				GraphConfigUtility.Options.MultiSeriesBreakColumn);

		GraphConfigUtility.saveConfigOption(yAxisSortComboBox.getText(), YAxisSort.NoSort.name(), graphConfigImpl, GraphConfigUtility.Options.YAxisSort);

		GraphConfigUtility.saveConfigOption("120", "", graphConfigImpl, GraphConfigUtility.Options.ForceYPercentage);

		graphConfigImpl.setChartType(UIUtility.getChartEnum(chartComboBox.getText()).name());

		return null;

	}

	private String validateConfig() {

		Charts selectedChart = UIUtility.getChartEnum(chartComboBox.getCurrentValue());
		if (selectedChart == null) {
			return "Invalid Chart Name.";
		} else {
			if (selectedChart.equals(Charts.Scatter_Plot_Chart) || selectedChart.equals(Charts.Multivariate_Scatter_Plot_Chart)) {
				if (xAxisColumnGrid.getAllData().size() == 0) {
					return "No X Axis Columns selected.";
				}

				if (chartComboBox.getText().equals(emptyChartComobBoxText)) {
					return "No X Axis Columns selected.";
				}
			} else {

				String yAxisTitleText = yAxisTitle.getText();
				if (yAxisTitleText == null || yAxisTitleText.length() == 0) {
					return "Y Axis Label is not set.";
				}

				String yAxisColumnButtonText = yAxisColumnButton.getText();
				if (yAxisColumnButtonText.equals(yAxisColumnLabelDefaultTxt)) {
					return "Y Axis Column is not set.";
				}
				if (xAxisColumnGrid.getAllData().size() == 0) {
					return "No X Axis Columns selected.";
				}

				if (chartComboBox.getText().equals(emptyChartComobBoxText)) {
					return "No X Axis Columns selected.";
				}

			}
		}

		return null;
	}

	public void applyChartSetting() {
		Charts chart = Charts.Bar_Chart;
		MainDesktop.consoleLog("QQ1 "+chart.name());
		try {
			chart = UIUtility.getChartEnum(chartComboBox.getText());
			MainDesktop.consoleLog("QQ2 "+chart.name());
		} catch (Exception e) {
			MainDesktop.consoleLog(e.getLocalizedMessage());
			MainDesktop.consoleLog("Converted " +chart.name());
		}
		MainDesktop.consoleLog("QQ3 "+chart.name());
		applyChartSetting(chart);

	}

	private void applyChartSetting(Charts chart) {
		if (chart != null) {
			if (chart.equals(Charts.Scatter_Plot_Chart) || chart.equals(Charts.Multivariate_Scatter_Plot_Chart)) {
				fieldSetYAxis.hide();
			} else {
				fieldSetYAxis.show();
			}
			if (chart.equals(Charts.Bar_Chart)) {
				// Show Y-Axis sort
				yAxisSortComobBoxLabel.show();
			} else {
				// Hide Y-Axis sort
				yAxisSortComobBoxLabel.hide();
			}
		}

	}

	private void testWrite() {
		saveCurrentStateToConfig();
		String config = GraphConfigUtility.toJson_v1(graphConfigImpl);
		ServerComm.saveConfigString("", config);
		System.out.println("WROTE OUT :" + config);
		if (MainDesktop.IN_DEBUG_MODE) {
			ServerComm.saveConfigString("", config);
		} else {
			Drupal.writeDataToDevTimeEditTextField(config);
		}
	}

	private void plot() {
		// Element gwtviz_iframe = Document.get().getElementById("gwtviz_iframe");
		// Element gwtviz_mount_chart_id = Document.get().getElementById("gwtviz_mount_chart_id");
		//
		// MainDesktop.consoleLog("gwtviz_mount_chart_id.getOffsetHeight(): " + gwtviz_mount_chart_id.getOffsetHeight());
		// if (gwtviz_iframe != null) {
		// MainDesktop.consoleLog("gwtviz_iframe.getOffsetHeight(): " + gwtviz_iframe.getOffsetHeight());
		// }

		String possibleProblem = validateConfig();
		if (possibleProblem != null) {
			ServerComm.alert(possibleProblem);
			return;
		}

		saveCurrentStateToConfig();
		String currentConfig = GraphConfigUtility.toJson_v1(graphConfigImpl);
		MainDesktop.consoleLog("Current Config : " + currentConfig);
		if (MainDesktop.IN_DEBUG_MODE) {
			ServerComm.saveConfigString("", currentConfig);
		} else {
			// Write and read config from the page.
			// This will ensure that we will save it.
			try {
				// Here should ONLY BE DEV/EDIT MODE. SHOULD NOT BE READMODE
				if (Drupal.Mode.Drupal_Edit_Existing.equals(MainDesktop.CurrentMode) || Drupal.Mode.Drupal_Edit_New.equals(MainDesktop.CurrentMode)) {
					Drupal.writeDataToDevTimeEditTextField(currentConfig);
					currentConfig = Drupal.readDataFromDevTimeEditTextField();
				} else {
					currentConfig = Drupal.readDataFromRunTimeEditTextField();
				}
			} catch (Throwable t) {
				MainDesktop.consoleLog("Problem writing to the HTML Page: " + t.getMessage());
			}

		}

		graphConfigImpl = (GraphConfigImpl_1) GraphConfigUtility.graphConfigImplfromJson(currentConfig);
		// Again makes sure there will be no problems with saved data or the user will see it.
		readCurrentStateFromConfig();
		MainDesktop.execRuntimeGraph(currentConfig);
	}

}
