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
package com.itgp.gwtviz.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ProgressBar {
	
	Element progressElement;
	Element progressBarElement;
	Element progressSpanElement;

	public ProgressBar(){
	}
	
	
	public Widget makeProgressBar() {
		progressElement = DOM.createDiv();
		progressElement.setClassName("progress");
		progressElement.setId(Drupal._DRUPAL_GWTVIZ_RUNTIME_STATUS);
		
		progressBarElement = DOM.createDiv();
		progressBarElement.setClassName("progress-bar");
		progressBarElement.setAttribute("role","progressbar");
		progressBarElement.setAttribute("aria-valuenow","0.0");
		progressBarElement.setAttribute("aria-valuemin","0.0");
		progressBarElement.setAttribute("aria-valuemax","0.0");
		progressBarElement.setAttribute("style","width:0%");	
		
		progressSpanElement = DOM.createSpan();
		progressSpanElement.setInnerHTML("");
		
		progressBarElement.appendChild(progressSpanElement);
		progressElement.appendChild(progressBarElement);
		
		HTML progressHtml = new HTML(progressElement.getString());
		progressHtml.setVisible(false);
		
		return progressHtml;
	}
	
	public void init(){
		setProgress("",0,0,0);
	}
	
	public void setProgress(String msg, int start,int current, int max){
		if((msg== null || msg.length() ==0 ) && start==0 && max ==0 && current ==0){
			_setProgress("","width:0%",0,0,0);
		}else{
			
			_setProgress("","width:0%",0,0,0);
		}
	}
	
	public void setFinished(String msg, int start,int current, int max){
		
	}
	
	public void setError(String msg, int start,int current, int max){
		
	}
	
	private void _setProgress(String msg,String width, int start, int current, int max){
		
		double dCurrentPercent= (current*1.0)/(max*1.0);
		String _internalMSG = msg;
		if(msg==null || msg.length()==0){
			String _internalMsg = current+" of "+max+" Records Processed.";
		}
		
		progressBarElement.setAttribute("aria-valuenow",""+start);
		progressBarElement.setAttribute("aria-valuemin",""+dCurrentPercent);
		progressBarElement.setAttribute("aria-valuemax",""+max);
		progressBarElement.setAttribute("style","width:"+dCurrentPercent+"%");	
		progressSpanElement.setInnerHTML(_internalMSG);
	}
	
}
