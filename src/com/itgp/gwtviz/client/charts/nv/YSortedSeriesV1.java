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

import com.google.gwt.core.client.*;

public class YSortedSeriesV1 extends JavaScriptObject {

	protected YSortedSeriesV1() {
	}

	public final static native YSortedSeriesV1 create() /*-{
		return {}; // same as new Object()
	}-*/;

	public final static YSortedSeriesV1 create(String key) {
		YSortedSeriesV1 value = YSortedSeriesV1.create();
		value.setKey(key);
		return value;
	}

	public final static YSortedSeriesV1 create(String key, String color) {
		YSortedSeriesV1 value = YSortedSeriesV1.create(key);
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

	public final native JsArray<YSortedCoordsV1> getValues() /*-{
		if (this.values == null) {
			this.values = [];
		}
		return this.values;
	}-*/;

	public final native void setValues(JsArray<YSortedCoordsV1> value) /*-{
		this.values = value;
	}-*/;

  	public final void addCoords(Object x, double y, String label) {
            if ( x instanceof Double){
            	addCoordsDoubleX((Double) x, y,label);
            } else {
            	addCoordsStringX((String) x, y, label);
            }
	};  
        
	protected final void addCoordsDoubleX(double x, double y, String label) {
		JsArray<YSortedCoordsV1> value = getValues();
		value.set(value.length(), YSortedCoordsV1.create(x, y, label));

	};


	protected final void addCoordsStringX(String x, double y, String label) {
		JsArray<YSortedCoordsV1> value = getValues();
		value.set(value.length(), YSortedCoordsV1.create(x, y, label));

	};
    

	
	public final native String getColor() /*-{
		return this.color;
	}-*/;

	public final native void setColor(String value) /*-{
		this.color = value;
	}-*/;

}
