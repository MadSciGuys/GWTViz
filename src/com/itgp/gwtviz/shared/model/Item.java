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
package com.itgp.gwtviz.shared.model;

import java.util.ArrayList;
import java.util.List;

public class Item  {

	private List<Item> data;
	private String value;
	private long id;
	private String label;
	private String sortOrder;
	private String sortDirection;
	private boolean selected;

	public Item(String value, long id) {
		super();
		this.value = value;
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setData(List<Item> dataList) {
		data = dataList;
	}

	public List<Item> getData() {
		if (this.data == null) {
			this.data = new ArrayList<Item>();
		}
		return data;
	}

	public void addData(Item data) {
		if (this.data == null) {
			this.data = new ArrayList<Item>();
		}
		this.data.add(data);
	}

	public int getDatacount() {
		return getData().size();
	}

	public Item copy() {
		Item newItem = new Item(this.value, this.id);
		List<Item> newData = new ArrayList<Item>();
		if (getData() != null && getData().size() > 0) {
			for (Item i : getData()) {
				Item newItemData = new Item(i.value, i.id);
				newData.add(newItemData);
			}
		}
		newItem.setData(newData);
		return newItem;
	}

	public Item copyShallow() {
		Item newItem = new Item(this.value, this.id);
		newItem.setLabel(this.getLabel());
		newItem.setSelected(this.isSelected());
		newItem.setSortDirection(this.getSortDirection());
		newItem.setSortOrder(this.getSortOrder());
		return newItem;
	}

	public void copyInto(Item src) {
		// Item newItem = new Item(this.value, this.id);
		this.setValue(src.getValue());
		this.setId(src.getId());
		this.setLabel(src.getLabel());
		this.setSelected(src.isSelected());
		this.setSortDirection(src.getSortDirection());
		this.setSortOrder(src.getSortOrder());
	}

	@Override
	public String toString() {
		return "Item [value=" + value + ", id=" + id + "]";
	}

	public String dataToString() {
		StringBuffer sb = new StringBuffer();

		for (Item i : getData()) {
			sb.append("\t");
			sb.append(i.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		
		return !ItemUtil.isDifferent(getData(), ((Item)obj).getData());
	}

//	@Override
//	public int compareTo(Item o) {
//		if (o == null) {
//			return 1;
//		}
//		int comp1 = getValue().compareTo(o.getValue());
//		if(ItemUtil.isDifferent(getData(), o.getData())){
//			if(comp1==0){
//				comp1 = 1; 
//			}
//		}
//		
//		return comp1;
//		
//	}

}
