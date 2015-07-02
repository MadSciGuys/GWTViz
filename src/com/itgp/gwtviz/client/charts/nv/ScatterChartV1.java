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
package com.itgp.gwtviz.client.charts.nv;

import com.google.gwt.core.client.JsArray;

public class ScatterChartV1 extends ChartV1 {

	protected ScatterChartV1() {
		// TODO Auto-generated constructor stub
	}

	public static final native ScatterChartV1 create()/*-{
		return {
			chartType : $wnd.ITGP_CHART_TYPE_SCATTER ,
		}; //  ITGP_CHART_TYPE_SCATTER is defined in itgp.js as a constant
	}-*/;
	
	public final native JsArray<ScatterSeriesV1> getScatterSeries() /*-{
	   if ( this.scatterSeries == null){
	   		this.scatterSeries = [];
	   }
	   return this.scatterSeries;
	}-*/;



	public final native void setScatterSeries(JsArray<ScatterSeriesV1> value) /*-{
	   this.scatterSeries = value;
	}-*/;
	
	
	public final void addSeries(ScatterSeriesV1 value){
		if (value == null){
			return;
		}
		JsArray<ScatterSeriesV1>  series = getScatterSeries();
		
		series.set(series.length(), value);
	}
	
	
	public final ScatterSeriesV1 createAndAddSeries(String key){
		ScatterSeriesV1 val = ScatterSeriesV1.create(key);
		addSeries(val);
		return val;
		
	}
	
}
