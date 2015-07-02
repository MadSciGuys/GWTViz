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
package com.itgp.gwtviz.shared.gconfig;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface GraphConfigImpl_1 extends GraphConfigImpl{

	enum XColumnOption {
		NORMAL, MULTI_SERIES
	}

	public GraphConfigFactory configFactory = GWT.create(GraphConfigFactory.class);

	public interface GraphConfigFactory extends AutoBeanFactory {
		AutoBean<GraphConfigImpl_1> config();
	}

	String getDataSourceName();
	long  getDataSourceID();

	String getChartType();

	String getYColumn();

	String getYColumnLabel();

	AutoBeanItems getFilterColumns();
	
	AutoBeanItems getXColumns();
	
	//*******************************************************
	//Used as a catch all for storing various data elements
	//Intended use:  use Key:Value pairs 
	// Key:  will be the label property of the AutoBeanItem
	// Value:  will be the value property of the AutoBeanItem
	// 
	// Possible key options are stored in GraphConfigUtility
	// in enum Options
	//*******************************************************
	AutoBeanItems getOptions();

	String getXColumnLabel();

	String getXAxisOption();
	
	void setDataSourceName(String DataSourceName);
	
	void setDataSourceID(long DataSourceID);

	void setChartType(String chartType);

	void setYColumn(String yColumn);

	void setYColumnLabel(String yColumnLabel);
	
	void setFilterColumns(AutoBeanItems filterColumns);
	
	void setOptions(AutoBeanItems options);

	void setXColumns(AutoBeanItems xColumns); 

	void setXColumnLabel(String xColumnLabel);

	void setXAxisOption(String xAxisOption);

}
