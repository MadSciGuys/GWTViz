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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SortOrder;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.itgp.gwtviz.client.ui.MainDesktop;
import com.itgp.gwtviz.client.ui.config.ItemGrid.SortDirection;
import com.itgp.gwtviz.shared.gconfig.AutoBeanItem;
import com.itgp.gwtviz.shared.gconfig.AutoBeanItems;
import com.itgp.gwtviz.shared.gconfig.GraphConfigUtility;
import com.itgp.gwtviz.shared.model.MRoute.Columns;
import com.sencha.gxt.data.shared.ListStore;

public class ItemUtil {

	public static final String _ROW_COUNT = "Row Count";

	// final public static String jsonData =
	// "[[\"229034\",\"JNJ-56021927-n001-00359\"],[\"229035\",\"JNJ-56021927-n001-00359\"],[\"229036\",\"JNJ-56021927-n001-00359\"]]";
	// public static List<Item> stubData() {
	//
	// ArrayList<Item> newList = new ArrayList<Item>();
	// newList.add(new Item("Hi", 1));
	// newList.add(new Item("Hello", 2));
	// newList.add(new Item("dog", 3));
	// newList.add(new Item("doggy", 4));
	// newList.add(new Item("cat", 5));
	//
	// return newList;
	//
	// }
	
	public static boolean isDifferent(List<Item> srcList, List<Item> destList){
		
		if(srcList == null && destList == null){
			return false;
		}
		if((srcList == null || srcList.size()==0) && (destList!=null && destList.size()>0)){
			return true;
		}
		
		if((destList == null || destList.size()==0) && (srcList!=null && srcList.size()>0)){
			return true;
		}
		
		List<Item> newAddedElementsList = new ArrayList<Item>();
		List<Item> removedElementsList  = new ArrayList<Item>();

		for(Item item : srcList){
		    if(destList.contains(item)){
		        continue;
		    }else{
		        removedElementsList.add(item);
		    }
		}
		for(Item item : destList){
		    if(srcList.contains(item)){
		        continue;
		    }else{
		        newAddedElementsList.add(item);
		    }
		}
		if(newAddedElementsList.size()==0 && removedElementsList.size()==0){
			return false;
		}else{
			return true;
		}
	}
	

	public static String makeColumnURL(List<Item> columns) {
		String part1 = "{\"n\":\"";
		StringBuffer sb = new StringBuffer(500);
		sb.append("[");
		// int counter = 0;
		boolean firstElement = true;
		for (Item c : columns) {
			if (firstElement) {
				sb.append(part1);
				sb.append(c.getValue());
				sb.append("\"}");
				firstElement = false;
			} else {
				sb.append(",");
				sb.append(part1);
				sb.append(c.getValue());
				sb.append("\"}");
			}
			// counter++;
			// if(counter>=3){
			// break;
			// }
		}
		sb.append("]");
		return sb.toString();
	}

	public static List<Item> convert(AutoBeanItems autoBeanItems) {
		ArrayList<Item> newList = new ArrayList<Item>();
		if (autoBeanItems != null) {
			List<AutoBeanItem> colList = autoBeanItems.getData();
			if (colList != null && colList.size() > 0) {
				for (int i = 0; i < colList.size(); i++) {
					AutoBeanItem abi = colList.get(i);
					newList.add(copyToItem(abi));
				}
			}
		}
		return newList;
	}
	
	public static void removeBasedOnValue(Item itemToBeRemoved, List<Item> items) {
		for (int i = items.size()-1; i >=0;i--) {
			Item item = items.get(i);
			if(item.getValue().equals(itemToBeRemoved.getValue())){
				items.remove(i);
			}
		}
	}
	public static String topLevelToString(ListStore<Item> items) {
		StringBuffer sb = new StringBuffer();
		
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				sb.append(item.getValue());
			}
		}
		return sb.toString();
	}

	public static AutoBeanItems convert(List<Item> items) {
		AutoBeanItems abis = GraphConfigUtility.makeAutoBeanItems();
		abis.setData(new ArrayList<AutoBeanItem>());
		for (Item i : items) {
			AutoBeanItem abi = convert(i, false);
			abis.getData().add(abi);
		}
		return abis;
	}

	public static AutoBeanItems convertShallow(List<Item> items) {
		AutoBeanItems abis = GraphConfigUtility.makeAutoBeanItems();
		abis.setData(new ArrayList<AutoBeanItem>());
		for (Item i : items) {
			AutoBeanItem abi = convert(i, true);
			abis.getData().add(abi);
		}
		return abis;
	}

	public static AutoBeanItem convert(Item item, boolean shallow) {
		AutoBeanItem newABItem = GraphConfigUtility.makeAutoBeanItem();
		newABItem.setValue(item.getValue());
		newABItem.setId(item.getId());
		newABItem.setLabel(item.getLabel());
		newABItem.setSortDirection(item.getSortDirection());
		newABItem.setSortOrder(item.getSortOrder());
		newABItem.setSelected(item.isSelected());

		List<AutoBeanItem> newABUIData = new ArrayList<AutoBeanItem>();
		if (!shallow) {
			if (item.getData() != null) {
				for (Item i : item.getData()) {
					// Item newItemData = new Item(i.value, i.id);
					AutoBeanItem newABItemData = GraphConfigUtility.makeAutoBeanItem();
					newABItemData.setValue(i.getValue());
					newABItemData.setId(i.getId());
					newABItemData.setLabel(i.getLabel());
					newABItemData.setSortDirection(i.getSortDirection());
					newABItemData.setSortOrder(i.getSortOrder());
					newABItemData.setSelected(i.isSelected());
					newABUIData.add(newABItemData);
				}
			}
		}
		newABItem.setData(newABUIData);
		return newABItem;
	}

	public static Item copyToItem(AutoBeanItem autoBeanItem) {
		Item newItem = new Item(autoBeanItem.getValue(), autoBeanItem.getId());
		newItem.setLabel(autoBeanItem.getLabel());
		newItem.setSortDirection(autoBeanItem.getSortDirection());
		newItem.setSortOrder(autoBeanItem.getSortOrder());
		newItem.setSelected(autoBeanItem.isSelected());

		List<Item> newIData = new ArrayList<Item>();
		if (autoBeanItem.getData() != null) {
			for (AutoBeanItem i : autoBeanItem.getData()) {
				Item newDataItem = new Item(i.getValue(), i.getId());
				newDataItem.setLabel(i.getLabel());
				newDataItem.setSortDirection(i.getSortDirection());
				newDataItem.setSortOrder(i.getSortOrder());
				newDataItem.setSelected(i.isSelected());
				newIData.add(newDataItem);
			}
		}
		newItem.setData(newIData);
		return newItem;
	}

	public static List<Item> convert(Columns columns) {
		ArrayList<Item> newList = new ArrayList<Item>();
		if (columns != null) {
			List<String> colList = columns.getColumns();
			if (colList != null && colList.size() > 0) {
				for (int i = 0; i < colList.size(); i++) {
					newList.add(new Item(colList.get(i), i));
				}
			}
		}

		return newList;
	}

	public static List<Item> getDataFromColumn(String jsonArrayString, Item item, boolean distinct) {
		JSONValue val = parseInput(jsonArrayString);
		int icolumnNumber = (int) item.getId();
		return getDataColumn(val, icolumnNumber, item.getData(), distinct);
	}

	public static List<Item> getDataFromColumn(String jsonArrayString, long columnNumber, boolean distinct) {
		JSONValue val = parseInput(jsonArrayString);
		int icolumnNumber = (int) columnNumber;
		return getDataColumn(val, icolumnNumber, distinct);
	}

	protected static JSONValue parseInput(String jsonData) {
		JSONValue val = JSONParser.parseStrict(jsonData);
		return val;
	}

	public static void elimateDuplicateData(List<Item> columns) {

		for (Item item : columns) {
			Set<Item> hs = new HashSet<Item>();
			hs.addAll(item.getData());
			item.getData().clear();
			item.getData().addAll(hs);
		}
	}

	public static Item elimateDuplicateDataForItem(Item src) {
		Item uniqueDataItem = new Item(src.getValue(), src.getId());
		Set<Item> hs = new HashSet<Item>();
		hs.addAll(src.getData());
		uniqueDataItem.getData().clear();
		uniqueDataItem.getData().addAll(hs);
		return uniqueDataItem;
	}

	public static Item findItem(List<Item> items, String value) {
		for (Item i : items) {
			if (i.getValue().equals(value)) {
				return i;
			}
		}
		return null;
	}

	public static ArrayList<Item> copy(List<Item> src) {
		ArrayList<Item> dest = new ArrayList<Item>();
		for (Item i : src) {
			dest.add(i.copy());
		}
		return dest;
	}

	public static void addDataToColumns(String jsonArrayString, List<Item> columns) {
		System.out.println(jsonArrayString);
		JSONValue jsonData = parseInput(jsonArrayString);
		JSONArray jsonArray = (JSONArray) jsonData;
		int rows = jsonArray.size();

		for (int i = 0; i < rows; i++) {
			JSONArray jItem = (JSONArray) jsonArray.get(i); // this could trigger an exception if data is not right
			int itemSize = jItem.size();

			// lets hope the array sizes align
			for (int cIndex = 0; cIndex < itemSize; cIndex++) {
				Item item = columns.get(cIndex);
				JSONString colVal = jItem.get(cIndex).isString();
				String sColVal = colVal.stringValue();
				item.getData().add(new Item(sColVal, 0));
			}

			// if (columnNumber < itemSize) {
			// JSONString colVal = jItem.get(columnNumber).isString();
			// String sColVal = colVal.stringValue();
			// Item item = new Item(sColVal, columnNumber);
			// if (distinct) {
			// if (!list.contains(item)) {
			// list.add(item);
			// }
			// } else {
			// list.add(item);
			// }
			// }
		}

		// return list;
	}
	
	public static int getIndexInCompleteDataSet(String columnName){
		if(MainDesktop.getCurrentDataModel()== null || MainDesktop.getCurrentDataModel().size()==0){
			return -1;
		}
		
		for(int i = 0; i<MainDesktop.getCurrentDataModel().size();i++){
			if(MainDesktop.getCurrentDataModel().get(i).getValue().equals(columnName)){
				return i;
			}
		}
		return -1;
	}

	protected static List<Item> getDataColumn(JSONValue jsonData, int columnNumber, boolean distinct) {
		if (jsonData == null) {
			return new ArrayList<Item>();
		}

		if (!(jsonData instanceof JSONArray)) {
			return new ArrayList<Item>();
		}

		JSONArray jsonArray = (JSONArray) jsonData;
		int n = jsonArray.size();
		ArrayList<Item> list = new ArrayList<Item>();
		for (int i = 0; i < n; i++) {
			JSONArray jItem = (JSONArray) jsonArray.get(i); // this could trigger an exception if data is not right

			int itemSize = jItem.size();

			if (columnNumber < itemSize) {
				JSONString colVal = jItem.get(columnNumber).isString();
				String sColVal = colVal.stringValue();
				Item item = new Item(sColVal, columnNumber);
				if (distinct) {
					if (!list.contains(item)) {
						list.add(item);
					}
				} else {
					list.add(item);
				}
			}
		}
		return list;
	}

	protected static List<Item> getDataColumn(JSONValue jsonData, int columnNumber, List<Item> excludeItems, boolean distinct) {
		if (jsonData == null) {
			return new ArrayList<Item>();
		}

		if (!(jsonData instanceof JSONArray)) {
			return new ArrayList<Item>();
		}

		JSONArray jsonArray = (JSONArray) jsonData;
		int n = jsonArray.size();
		ArrayList<Item> list = new ArrayList<Item>();
		for (int i = 0; i < n; i++) {
			JSONArray jItem = (JSONArray) jsonArray.get(i); // this could trigger an exception if data is not right

			int itemSize = jItem.size();

			if (columnNumber < itemSize) {
				JSONString colVal = jItem.get(columnNumber).isString();
				String sColVal = colVal.stringValue();
				Item item = new Item(sColVal, columnNumber);
				if (excludeItems == null) {
					if (distinct) {
						if (!list.contains(item)) {
							list.add(item);
						}
					} else {
						list.add(item);
					}
				} else {
					if (!excludeItems.contains(item)) {
						if (distinct) {
							if (!list.contains(item)) {
								list.add(item);
							}
						} else {
							list.add(item);
						}
					}
				}
			}
		}

		return list;
	}
	
	public static void setDefaults(Item item){
		if(item.getSortDirection()==null ||item.getSortDirection().length()==0){
			item.setSortDirection(SortDirection.Ascending.name());
		}
	}
}
