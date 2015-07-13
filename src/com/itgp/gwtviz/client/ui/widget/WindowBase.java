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
import com.itgp.gwtviz.client.ui.MainDesktop;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;

public class WindowBase extends Window {

	protected WindowBase windowBase;

	public WindowBase() {
		super();
		windowBase = this;
	}

	public void centerWindow(ContentPanel parentContentPanel,double marginPercentLess) {

		
//		int h = Drupal.getTopWindowHeight();
//		int w = Drupal.getTopWindowWidth();
//		
//		//w = com.google.gwt.user.client.Window.getClientWidth();
//
//		int thisWidth = w - ((int) (w * marginPercentLess)); 
//		int thisHeight = h - ((int) (h * marginPercentLess));
//
//		MainDesktop.consoleLog("h="+h+"  w="+w+"   thisWidth="+thisWidth+"   thisHeight="+thisHeight);
//		setPixelSize(thisWidth, thisHeight);
//		setPagePosition((w-thisWidth)/2, (h-thisHeight)/2);
		Sizer sizer = sizeThisWindow(parentContentPanel,marginPercentLess);
		
		MainDesktop.consoleLog("sizer: "+sizer.toString());
//		setPixelSize(sizer.getWidth(), sizer.getHeight());
//		setPagePosition((w-thisWidth)/2, (h-thisHeight)/2);	
		
		
	}

//	public void centerBase() {
//		Point p = getElement().getAlignToXY(Drupal.getTopWindowBody(), new AnchorAlignment(Anchor.CENTER, Anchor.CENTER), 0, 0);
//		setPagePosition(p.getX(), p.getY());
//	}

	
	private int getWindowWidth(ContentPanel contentPanel){
		
		int absoluteLeft =  contentPanel.getAbsoluteLeft();
		int width =  contentPanel.getElement().getWidth(false);
		return width;
	}
	/*
	 * 
		int thisWidth = w - ((int) (w * marginPercentLess)); 
		int thisHeight = h - ((int) (h * marginPercentLess));

		MainDesktop.consoleLog("h="+h+"  w="+w+"   thisWidth="+thisWidth+"   thisHeight="+thisHeight);
		setPixelSize(thisWidth, thisHeight);
		setPagePosition((w-thisWidth)/2, (h-thisHeight)/2);
		
	 */
	
	private Sizer sizeThisWindow(ContentPanel contentPanel, double percentShrinkage){
		Size parentSize = contentPanel.getElement().getSize();
		Sizer sizer = new Sizer();
		sizer.setWindowSize(parentSize,0.1,this);
		MainDesktop.consoleLog(sizer.toString());
		return sizer;
	}
}
