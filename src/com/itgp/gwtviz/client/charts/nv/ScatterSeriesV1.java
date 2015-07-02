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

import com.google.gwt.core.client.*;

public class ScatterSeriesV1 extends JavaScriptObject {

    protected ScatterSeriesV1() {}

    public final static native ScatterSeriesV1 create()    /*-{
           return {};
        }-*/
    ;

    public final static ScatterSeriesV1 create(String key) {
        ScatterSeriesV1 value = ScatterSeriesV1.create();
        value.setKey(key);

        return value;
    }

    // -------------------------------------------------------------

    public final native String getKey()    /*-{
               return this.key;
       }-*/
    ;

    public final native void setKey(String value)    /*-{
               this.key = value;
       }-*/
    ;

    public final native JsArray<ScatterCoordsV1> getValues()    /*-{
               if (this.values == null) {
                       this.values = [];
               }
               return this.values;
       }-*/
    ;

    public final native void setValues(JsArray<ScatterCoordsV1> value)    /*-{
               this.values = value;
       }-*/
    ;

    public final void addPoint(Object x, double y, double size, Shape shape) {
        JsArray<ScatterCoordsV1> value  = getValues();
        ScatterCoordsV1          coords = null;
        if (x instanceof Double) {
            coords = ScatterCoordsV1.create((Double) x, y, size, shape);
        } else {
            coords = ScatterCoordsV1.create(x.toString(), y, size, shape);
        }
        value.set(value.length(), coords);
    }

    ;

    /**
     * Defaults shape to circle
     * @param x
     * @param y
     * @param size
     */
    public final void addPoint(Object x, double y, double size) {
       addPoint(x, y , size, Shape.CIRCLE);
    }

    ;

    /**
     * Defaults Shape to circle and the size to 1
     * @param x
     * @param y
     */
    public final void addPoint(double x, double y) {
       addPoint(x, y , 1, Shape.CIRCLE);
    }

    ;

}
