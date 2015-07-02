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

public class XYSeriesV1 extends JavaScriptObject {

	protected XYSeriesV1() {
	}

	public final static native XYSeriesV1 create() /*-{
		return {}; // same as new Object()
	}-*/;

	public final static XYSeriesV1 create(String key) {
		XYSeriesV1 value = XYSeriesV1.create();
		value.setKey(key);
		return value;
	}

	public final static XYSeriesV1 create(String key, String color) {
		XYSeriesV1 value = XYSeriesV1.create(key);
		value.setColor(color);
		return value;
	}

	// -------------------------------------------------------------

	public final native String getKey() /*-{
		return this.key;
	}-*/;

	public final native void setKey(String value) /*-{
		this.key = value;
	}-*/;

	public final native JsArray<XYCoordsV1> getValues() /*-{
		if (this.values == null) {
			this.values = [];
		}
		return this.values;
	}-*/;

	public final native void setValues(JsArray<XYCoordsV1> value) /*-{
		this.values = value;
	}-*/;

  	public final void addXY(Object x, double y) {
            if ( x instanceof Double){
            	addXYDoubleX((Double) x, y);
            } else {
            	addXYStringX((String) x, y);
            }
	};  
        
	protected final void addXYDoubleX(double x, double y) {
		JsArray<XYCoordsV1> value = getValues();
		value.set(value.length(), XYCoordsV1.create(x, y));

	};


	protected final void addXYStringX(String x, double y) {
		JsArray<XYCoordsV1> value = getValues();
		value.set(value.length(), XYCoordsV1.create(x, y));

	};
    
    public final void addNull(Object x){
        if ( x instanceof Double){
            addNullDoubleX((Double) x);
        } else {
            addNullStringX((String) x);
        }
        
    }
	
    protected final void addNullDoubleX(double x) {
        JsArray<XYCoordsV1> value = getValues();
        value.set(value.length(), XYCoordsV1.create(x));

    };	
	
    protected final void addNullStringX(String x) {
        JsArray<XYCoordsV1> value = getValues();
        value.set(value.length(), XYCoordsV1.create(x));

    };
	
	public final native String getColor() /*-{
		return this.color;
	}-*/;

	public final native void setColor(String value) /*-{
		this.color = value;
	}-*/;

}
