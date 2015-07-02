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


public class DonutPieChartV1 extends PieChartV1 {

	protected DonutPieChartV1() {
	}

	public static final native DonutPieChartV1 create() /*-{
		return {
			chartType : $wnd.ITGP_CHART_TYPE_DONUT,
			donutRatio: 0.35
		}; //  ITGP_CHART_TYPE_PIE is defined in itgp.js as a constant
	}-*/;
	
	
	public static final DonutPieChartV1 create(double value) {
		DonutPieChartV1 val = DonutPieChartV1.create();
		val.setDonutRatio(value);
		return val;
	}
	
	public final native double getDonutRatio() /*-{
	   return this.donutRatio;
	}-*/;



	public final native void setDonutRatio(double value) /*-{
	   this.donutRatio = value;
	}-*/;

}
