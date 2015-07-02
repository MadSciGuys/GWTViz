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

import com.google.web.bindery.autobean.shared.AutoBean;
import com.itgp.gwtviz.shared.model.MRoute;
import com.itgp.gwtviz.shared.model.MRoute.Data;
import com.itgp.gwtviz.shared.model.MRoute.Docs;
import com.itgp.gwtviz.shared.model.MRoute.Row;

public class DataUtility {
	
	public Data makeData() {
		MRoute mroute = new MRoute();
		AutoBean<Data> data = mroute.dataFactory.data();
		return data.as();
	}

	public Row makeRow() {
		MRoute mroute = new MRoute();
		AutoBean<Row> row = mroute.rowFactory.rows();
		return row.as();
	}
	
	
	public static Docs makeDocs() {
		MRoute mroute = new MRoute();
		AutoBean<Docs> docs = mroute.docFactory.docs();
		return docs.as();
	}

}
