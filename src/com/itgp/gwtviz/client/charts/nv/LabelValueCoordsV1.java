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

public class LabelValueCoordsV1 extends JavaScriptObject {

	protected LabelValueCoordsV1() {
	}

	public static final LabelValueCoordsV1 create (String label, double value) {
		LabelValueCoordsV1 retVal =  create();
		retVal.setLabel(label);
		retVal.setValue(value);
		return retVal;
	}
	
	protected static final native LabelValueCoordsV1 create () /*-{
	   return {};
	}-*/;
	
	public final native String getLabel() /*-{
	   return this.label;
	}-*/;



	public final native void setLabel(String value) /*-{
	   this.label = value;
	}-*/;


	public final native double getValue() /*-{
	   return this.value;
	}-*/;



	public final native void setValue(double value) /*-{
	   this.value = value;
	}-*/;
	
	
}
