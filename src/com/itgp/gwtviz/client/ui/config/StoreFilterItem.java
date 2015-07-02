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
package com.itgp.gwtviz.client.ui.config;

import com.itgp.gwtviz.shared.model.Item;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.form.TextField;

public class StoreFilterItem implements StoreFilter<Item> {

	TextField textField;
	
	public StoreFilterItem(TextField textField){
		this.textField= textField;
	}

	private String matchItem(String filter, String item, boolean startWildCard) {
		if (startWildCard) {
			if ((item.toLowerCase()).contains(filter.toLowerCase())) {
				return item;
			}
		} else {
			if ((item.toLowerCase()).startsWith(filter.toLowerCase())) {
				return item;
			}
		}
		return null;
	}
	@Override
	public boolean select(Store<Item> store, Item parent, Item item) {
		if(textField.getText().isEmpty()){
			return true;
		}
		String filter =textField.getText();
		boolean startWildCard = false;
		if (filter.startsWith("*")) {
			startWildCard = true;
			filter = filter.substring(1, filter.length());
		}
		if(matchItem(filter, item.getValue(), startWildCard)==null){
			return false;
		}else{
			return true;
		}
	}
	
	



}
