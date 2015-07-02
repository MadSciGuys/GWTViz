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

/**
 * @author Dev
 *
 */
public abstract class AbstractChartXYV1 extends ChartV1 {

    /**
     *
     */
    protected AbstractChartXYV1() {

        // don't do anything here. Use create
    }

    // ----------------------------------------------------

    public final native String getXAxisLabel()    /*-{
               return this.xAxisLabel;
       }-*/
    ;

    public final native void setXAxisLabel(String xAxisLabelparam)    /*-{
               this.xAxisLabel = xAxisLabelparam;
       }-*/
    ;

    public final native String getYAxisLabel()    /*-{
               return this.yAxisLabel;
       }-*/
    ;

    public final native void setYAxisLabel(String yAxisLabelparam)    /*-{
               this.yAxisLabel = yAxisLabelparam;
       }-*/
    ;

    public final native String getXAxisFormat()    /*-{
               return this.xAxisFormat;
       }-*/
    ;

    public final native void setXAxisFormat(String xAxisFormatparam)    /*-{
               this.xAxisFormat = xAxisFormatparam;
       }-*/
    ;

    public final native String getYAxisFormat()    /*-{
               return this.yAxisFormat;
       }-*/
    ;

    public final native void setYAxisFormat(String yAxisFormatparam)    /*-{
               this.yAxisFormat = yAxisFormatparam;
       }-*/
    ;

    public final native JsArray<XYSeriesV1> getXYSeries()    /*-{
               if ( this.xySeries == null){
                       this.xySeries = [];
               }
          return this.xySeries;
       }-*/
    ;

    public final native void setXYSeries(JsArray<XYSeriesV1> value)    /*-{
          this.xySeries = value;
       }-*/
    ;

    public final void addXYSeries(XYSeriesV1 value) {
        if (value == null) {
            return;
        }

        JsArray<XYSeriesV1> series = getXYSeries();

        series.set(series.length(), value);
    }

    public final XYSeriesV1 createAndAddXYSeries() {
        XYSeriesV1 val = XYSeriesV1.create();
        addXYSeries(val);

        return val;

    }

    public final XYSeriesV1 createAndAddXYSeries(String key) {
        XYSeriesV1 val = XYSeriesV1.create(key);
        addXYSeries(val);

        return val;

    }

    public final XYSeriesV1 createAndAddXYSeries(String key, String color) {
        XYSeriesV1 val = XYSeriesV1.create(key, color);
        addXYSeries(val);

        return val;

    }

    //---------------------------
    
    public final ForcedRange getOrCreateForcedRangeY(){
        ForcedRange yr = getForcedRangeY();
        if ( yr == null){
            yr = ForcedRange.create();
            setForcedRangeY(yr);
        }
        return yr;
    }
    
    public final native ForcedRange getForcedRangeY()    /*-{
          return this.forcedRangeY;
       }-*/
    ;

    public final native void setForcedRangeY(ForcedRange value)    /*-{
          this.forcedRangeY = value;
       }-*/
    ;

    
    
    public final ForcedRange getOrCreateForcedRangeX(){
        ForcedRange xr = getForcedRangeX();
        if ( xr == null){
            xr = ForcedRange.create();
            setForcedRangeX(xr);
        }
        return xr;
    }
    public final native ForcedRange getForcedRangeX()    /*-{
          return this.forcedRangeX;
       }-*/
    ;

    public final native void setForcedRangeX(ForcedRange value)    /*-{
          this.forcedRangeX = value;
       }-*/
    ;

}
