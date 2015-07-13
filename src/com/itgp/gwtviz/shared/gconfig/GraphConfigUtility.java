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
package com.itgp.gwtviz.shared.gconfig;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.itgp.gwtviz.client.ui.UIUtility;

public class GraphConfigUtility {

	public enum Options {
		MultiSeriesBreakColumn,ForceYPercentage,YAxisSort
	}
	
	public enum YAxisSort{
		NoSort,Ascending, Descending
	}

	public GraphConfigImpl_1 getConfig1(GraphConfig gc) {

		GraphConfigImpl_1 gci1 = null;

		return gci1;
	}

	public static void saveConfigOption(String currentValue, String defaultValue, GraphConfigImpl_1 gci1, Options option) {
		if (currentValue.equals(defaultValue)) {
			// defult selected - so lets remove it
			initOptions(gci1);
			AutoBeanItem abiFound = null;
			for (AutoBeanItem abi : gci1.getOptions().getData()) {
				if (option.name().equals(abi.getLabel())) {
					abiFound = abi;
				}
			}
			if (abiFound != null) {
				gci1.getOptions().getData().remove(abiFound);
			}
		} else {
			GraphConfigUtility.setOptionString(gci1, option, currentValue);
		}

	}

	private static void initOptions(GraphConfigImpl_1 gci1) {

		if (gci1.getOptions() == null) {
			gci1.setOptions(GraphConfigUtility.makeAutoBeanItems());
		}
		if (gci1.getOptions().getData() == null) {
			List<AutoBeanItem> options = new ArrayList<AutoBeanItem>();
			gci1.getOptions().setData(options);
		}		
	}

	public static void setOptionString(GraphConfigImpl_1 gci1, Options option, String optionText) {
		boolean updated = false;
		if (gci1.getOptions() != null && gci1.getOptions().getData() != null && gci1.getOptions().getData().size() > 0) {
			for (AutoBeanItem abi : gci1.getOptions().getData()) {
				if (option.name().equals(abi.getLabel())) {
					abi.setValue(optionText);
					updated = true;
				}
			}
		}
		if (!updated) {
			initOptions(gci1);
			AutoBeanItem abiNew = makeAutoBeanItem();
			abiNew.setLabel(option.name());
			abiNew.setValue(optionText);
			gci1.getOptions().getData().add(abiNew);
		}

	}

	public static String getOptionString(GraphConfigImpl_1 gci1, Options option) {
		if (gci1.getOptions() != null && gci1.getOptions().getData() != null) {
			for (AutoBeanItem abi : gci1.getOptions().getData()) {
				if (option.name().equals(abi.getLabel())) {
					return abi.getValue();
				}
			}
		}
		return null;
	}

	public static boolean isFilterEmpty(GraphConfigImpl_1 gci_1) {
		if (gci_1.getFilterColumns() != null) {
			if (gci_1.getFilterColumns().getData() != null && gci_1.getFilterColumns().getData().size() > 0) {
				return false;
			}
		}
		return true;
	}

	public static void clearGCI(GraphConfigImpl_1 gci_1) {
		clearFilter(gci_1);
		gci_1.setChartType("");

		if (gci_1.getOptions() != null) {
			if (gci_1.getOptions().getData() != null && gci_1.getOptions().getData().size() > 0) {
				gci_1.getOptions().getData().clear();
			}
		}

		gci_1.setXColumnLabel("");
		gci_1.setYColumn("");
		gci_1.setYColumnLabel("");

		if (gci_1.getXColumns() != null) {
			if (gci_1.getXColumns().getData() != null && gci_1.getXColumns().getData().size() > 0) {
				gci_1.getXColumns().getData().clear();
			}
		}

		clearFilter(gci_1);
	}

	public static void clearFilter(GraphConfigImpl_1 gci_1) {
		if (gci_1.getFilterColumns() != null) {
			if (gci_1.getFilterColumns().getData() != null && gci_1.getFilterColumns().getData().size() > 0) {
				gci_1.getFilterColumns().getData().clear();
			}
		}
	}

	public String toJSonGgraphConfig(GraphConfig gc) {
		String json = "";

		return json;
	}

	public String makeTestJsonGraphConfig() {
		GraphConfig gc = makeGraphConfig();
		gc.setVersion(1);

		GraphConfigImpl_1 gci_1 = makeGraphConfigImpl_1();

		gci_1.setDataSourceName("file1");
		gci_1.setChartType(UIUtility.Charts.Line_Chart.name());

		gci_1.setYColumn("MY_Y_COLUMN");
		gci_1.setYColumnLabel("MY_Y_COLUMN LABEL");

		AutoBeanItems xColumns = makeAutoBeanItems();
		List<AutoBeanItem> columns = new ArrayList<AutoBeanItem>();

		AutoBeanItem i1 = makeAutoBeanItem();
		AutoBeanItem i2 = makeAutoBeanItem();

		i1.setValue("MY xColumn 1");
		i2.setValue("MY xColumn 2");

		i1.setId(0l);
		i2.setId(0l);
		columns.add(i1);
		columns.add(i2);

		xColumns.setData(columns);

		gci_1.setXColumns(xColumns);
		gci_1.setXColumnLabel("MY_X_COLUMN LABEL");
		// gci_1.setXAxisOption(GraphConfigImpl_1.XColumnOption.NORMAL.name());

		AutoBean<GraphConfigImpl_1> beanGraphConfigImpl_1 = AutoBeanUtils.getAutoBean(gci_1);
		String jsonGraphConfigImpl_1 = AutoBeanCodex.encode(beanGraphConfigImpl_1).getPayload();

		gc.setConfig(jsonGraphConfigImpl_1);

		AutoBean<GraphConfig> beanGraphConfig = AutoBeanUtils.getAutoBean(gc);
		String jsonGC = AutoBeanCodex.encode(beanGraphConfig).getPayload();
		return jsonGC;
	}

	public static GraphConfig makeGraphConfig() {
		AutoBean<GraphConfig> config = GraphConfig.configFactory.config();
		return config.as();
	}

	public static GraphConfigImpl_1 makeGraphConfigImpl_1() {
		AutoBean<GraphConfigImpl_1> config = GraphConfigImpl_1.configFactory.config();
		GraphConfigImpl_1 configImpl = config.as(); 
		configImpl.setDataSourceID(-1);
		return configImpl;
	}

	public static AutoBeanItem makeAutoBeanItem() {
		AutoBean<AutoBeanItem> item = AutoBeanItem.itemFactory.make();
		return item.as();
	}

	public static AutoBeanItems makeAutoBeanItems() {
		AutoBean<AutoBeanItems> items = AutoBeanItems.itemsFactory.make();
		return items.as();
	}

	private static String toJson(GraphConfigImpl_1 gci_1) {
		AutoBean<GraphConfigImpl_1> beanGraphConfigImpl_1 = AutoBeanUtils.getAutoBean(gci_1);
		String jsonGC = AutoBeanCodex.encode(beanGraphConfigImpl_1).getPayload();
		return jsonGC;
	}

	public static String toJson_v1(GraphConfigImpl_1 gci_1) {
		String configImpl_V1 = toJson(gci_1);
		GraphConfig gc = makeGraphConfig();
		gc.setVersion(1);
		gc.setConfig(configImpl_V1);

		AutoBean<GraphConfig> beanGraphConfig = AutoBeanUtils.getAutoBean(gc);
		String jsonGC = AutoBeanCodex.encode(beanGraphConfig).getPayload();
		return jsonGC;
	}

	public static GraphConfig graphConfigfromJson(String jsonString) {
		// if(graphConfigImpl = (new GraphConfigUtility()).makeGraphConfigImpl_1();)
		GraphConfig gc = null;
		try {
			AutoBean<GraphConfig> bean = AutoBeanCodex.decode(GraphConfig.configFactory, GraphConfig.class, jsonString);
			gc = bean.as();
		} catch (Throwable t) {
			gc = GraphConfigUtility.makeGraphConfig();
		}

		return gc;
	}

	public static GraphConfigImpl graphConfigImplfromJson(String jsonString) {
		GraphConfig gc = graphConfigfromJson(jsonString);
		String versionedJsonConfig = gc.getConfig();
		int version = gc.getVersion();
		if (version == 1) {
			AutoBean<GraphConfigImpl_1> bean = AutoBeanCodex.decode(GraphConfigImpl_1.configFactory, GraphConfigImpl_1.class, versionedJsonConfig);
			GraphConfigImpl_1 gci_1 = bean.as();
			return gci_1;
		} else {
			return GraphConfigUtility.makeGraphConfigImpl_1();
		}
	}

}
