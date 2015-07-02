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

/**
 * <p>
 * 
 * @author Warp
 */
public class MatrixScatterData extends JavaScriptObject {

    protected MatrixScatterData() {
    }

    public final static native MatrixScatterData create() /*-{
		return [];
    }-*/
    ;

    public final native void add(MatrixScatterRow row) /*-{
		this.push(row);
    }-*/
    ;

    public final void set(int rowIndex, String name, Object value) {
        if (value instanceof Double) {
            setNative(rowIndex, name, ((Double) value));
        } else {
            String s = value.toString();
            setNative(rowIndex, name, s);
        }
    }

    protected final native void setNative(int rowIndex, String name, String value) /*-{
		$wnd.itgp.setScatterMatrixValue(this, rowIndex, name, value);
    }-*/;

    protected final native void setNative(int rowIndex, String name, double value) /*-{
		$wnd.itgp.setScatterMatrixValue(this, rowIndex, name, value);
    }-*/;
    // protected final native void setNative(int rowIndex, String name, Object value) /*-{
    // var row ;
    // if(typeof this[rowIndex] === 'undefined') {
    // // does not exist
    // row = {};
    // this[rowIndex] = row;
    // } else {
    // // does exist
    // row = this[rowIndex];
    // }
    //
    // row[name] = value;
    // }-*/;
}
