/* 
 * The MIT License
 *
 * Copyright 2015 InsiTech LLC.
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

public class YSortedBarChartV1 extends AbstractChartXYV1 {

    protected YSortedBarChartV1() {}

    public static final native YSortedBarChartV1 create()   /*-{
               return {
                       chartType : $wnd.ITGP_CHART_TYPE_YSORTED_BAR,
                       reduceXTicks : true,
                       rotateLabels : 0,
                       showControls: true,
                       groupSpacing: 0.1,
                       stacked: false,
                       showControls: false
               }; //  ITGP_CHART_TYPE_YSORTED_BAR is defined in itgp.js as a constant
       }-*/
    ;

    
    
    public final YSortedSeriesV1 createAndAddYSortedSeries(String key) {
        YSortedSeriesV1 val = YSortedSeriesV1.create(key);
        addYSortedSeries(val);

        return val;

    }

    public final void addYSortedSeries(YSortedSeriesV1 value) {
        if (value == null) {
            return;
        }

        JsArray<YSortedSeriesV1> series = getYSortedsSeries();

        series.set(series.length(), value);
    }
    
        public final native JsArray<YSortedSeriesV1> getYSortedsSeries()    /*-{
               if ( this.xySeries == null){
                       this.xySeries = [];
               }
          return this.xySeries;
       }-*/
    ;
}
