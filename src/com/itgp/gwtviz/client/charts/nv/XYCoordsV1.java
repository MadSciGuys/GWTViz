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

import com.google.gwt.core.client.JavaScriptObject;

public class XYCoordsV1 extends JavaScriptObject {

	protected XYCoordsV1() {
	}

	public static final XYCoordsV1 create (double x, double y) {
		XYCoordsV1 value =  create();
		value.setX(x);
		value.setY(y);
		return value;
	}
	
	public static final XYCoordsV1 create (String x, double y) {
		XYCoordsV1 value =  create();
		value.setX(x);
		value.setY(y);
		return value;
	}
	
	
    public static final XYCoordsV1 create (double x) {
        XYCoordsV1 value =  create();
        value.setX(x);
        value.setNullY();
        return value;
    }
    
    public static final XYCoordsV1 create (String x) {
        XYCoordsV1 value =  create();
        value.setX(x);
        value.setNullY();
        return value;
    }	
	
	
	
	protected static native XYCoordsV1 create () /*-{
	   return {};
	}-*/;
	
	public final native double getX() /*-{
	   return this.x;
	}-*/;



	public final native void setX(String value) /*-{
	   this.x = value;
	}-*/;
	
	public final native void setX(double value) /*-{
	   this.x = value;
	}-*/;

	public final native double getY() /*-{
	   return this.y;
	}-*/;



	public final native void setY(double value) /*-{
	   this.y = value;
	}-*/;	
	

    public final native void setNullY() /*-{
       this.y = null;
    }-*/;
	
}
