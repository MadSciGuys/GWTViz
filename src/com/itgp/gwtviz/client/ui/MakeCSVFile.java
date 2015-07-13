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
package com.itgp.gwtviz.client.ui;

import java.util.List;

import com.itgp.gwtviz.client.comm.ServerComm;
import com.itgp.gwtviz.client.ui.config.IMRouteCallable;
import com.itgp.gwtviz.client.ui.config.INotify;
import com.itgp.gwtviz.client.ui.config.NotifyPayload;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.model.Item;

public class MakeCSVFile implements IMRouteCallable,INotify{

	ConfigPanel_1 configPanel_1;
	MainDesktop mainDesktop;
	GraphConfigImpl_1 graphConfigImpl;
	
	public MakeCSVFile(ConfigPanel_1 configPanel_1,GraphConfigImpl_1 graphConfigImpl){
		this.configPanel_1= configPanel_1;
		this.graphConfigImpl = graphConfigImpl;
	}
	
	
	public void getFileData(){
		if(MainDesktop.getCurrentDataModel()==null || MainDesktop.getCurrentDataModel().size()==0){
			String mRouteURL = MainDesktop.getMRouteURL();
			ServerComm.getAllMRouteColumns(mRouteURL, graphConfigImpl.getDataSourceID(), this);
		}else{ 
			setAndClickDownloadLink();
		}
	}
	
	@Override
	public void setData(List<Item> items) {
		setAndClickDownloadLink();
	}

	private void setAndClickDownloadLink(){
		MakeDataFilterCallback dataFilterCallback = new MakeDataFilterCallback(graphConfigImpl,this);
		dataFilterCallback.applyCSVFilter();
	}

	@Override
	public void processNotify(NotifyPayload notifyPayload) {
		configPanel_1.setHREFinDownloadElementAnchor((String)notifyPayload.getData());
		configPanel_1.clickDownloadLink();
	}
}
