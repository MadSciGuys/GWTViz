/* 
 * The MIT License
 *
 * Copyright 2015 InsiTech LLC.   gwtvis@insitechinc.com
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
package com.itgp.gwtviz.client.ui.widget;

import com.itgp.gwtviz.client.ui.Drupal;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.core.client.util.Size;

public class Sizer {

	private int width;
	private int height;
	private int x;
	private int y;

	public Sizer(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	public Sizer() {

	}

	public void setWindowSize(Size parentSize, double percentShrinkage,Window windowToSize) {
		setWindowSize(parentSize.getWidth(), parentSize.getHeight(),percentShrinkage,windowToSize);
	}
	
	
	public void setDialogSize(Size parentSize, Dialog dialog){
		this.width = dialog.getMinWidth();
		this.height = dialog.getMinHeight();
		this.x = (parentSize.getWidth() - this.width) / 2;
		this.y = (parentSize.getHeight() - this.height) / 2;
		dialog.setPagePosition(x, y);	
	}

	public void setWindowSize(int _width, int _height, double percentShrinkage,Window windowToSize) {
		this.width = (int) (_width * (1-percentShrinkage));
		this.height = (int) (_height * (1-percentShrinkage));
		this.x = (_width - this.width) / 2;
		this.y = (_height - this.height) / 2;
				
		windowToSize.setPixelSize(getWidth(), getHeight());
		windowToSize.setPagePosition(x, y);	
	}

	public String toString() {
		return "width: " + width + ", height: " + height + ", x: " + x + ", y: " + y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
