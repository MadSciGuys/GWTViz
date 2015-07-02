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

public class PieChartV1 extends ChartV1 {

	protected PieChartV1() {
	}

	public static native PieChartV1 create() /*-{
		return {
			chartType : $wnd.ITGP_CHART_TYPE_PIE 
		}; //  ITGP_CHART_TYPE_PIE is defined in itgp.js as a constant
	}-*/;
	
	
	
	public final native JsArray<LabelValueCoordsV1> getLabelValueSeries() /*-{
		if ( this.labelValueSeries == null){
			this.labelValueSeries = [];
		}
	   return this.labelValueSeries;
	}-*/;



	public final native void setLabelValueSeries(JsArray<LabelValueCoordsV1> value) /*-{
	   this.labelValueSeries = value;
	}-*/;

	
	protected final void addLabelValueCoords(LabelValueCoordsV1 value){
		if (value == null){
			return;
		}
		JsArray<LabelValueCoordsV1>  series = getLabelValueSeries();
		
		series.set(series.length(), value);
	}
	
	protected  final LabelValueCoordsV1 createAndAddLabelValueSeries(){
		LabelValueCoordsV1 val = LabelValueCoordsV1.create();
		addLabelValueCoords(val);
		return val;
		
	}
	
	
	public final PieChartV1 add(String label, double value){
		this.addLabelValueCoords(LabelValueCoordsV1.create(label, value));
		return this;
		
	}
}
