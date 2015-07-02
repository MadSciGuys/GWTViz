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
package com.itgp.gwtviz.shared.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public class MRoute {
	
	private static Map<String,String> mRouteCache = new HashMap<String,String>();
	public DocsFactory docFactory = GWT.create(DocsFactory.class);
	public ColumnsFactory columnsFactory = GWT.create(ColumnsFactory.class);
	public DataFactory dataFactory = GWT.create(DataFactory.class);
	public RowFactory rowFactory = GWT.create(RowFactory.class);
	

	public interface DocsFactory extends AutoBeanFactory {
		AutoBean<MRoute.Docs> docs();
	}
	public interface ColumnsFactory extends AutoBeanFactory {
		AutoBean<MRoute.Columns> columns();
	}	
	public interface DataFactory extends AutoBeanFactory {
		AutoBean<MRoute.Data> data();
	}	
	public interface RowFactory extends AutoBeanFactory {
		AutoBean<MRoute.Row> rows();
	}	

	public interface Docs {
		List<File> getFiles();

		void setFile(List<File> files);

	}

	public interface File {
		String getMAlias();

		long getMIndex();

		void setMAlias(String MAlias);

		void setMIndex(long MIndex);
	}

	public interface Columns {
		List<String>  getColumns();
		void setColumns(List<String> column);
	}
	
	public interface Data {
		List<Row>  getData();
		void setData(List<Row> data);
	}
	
	public interface Row {
		List<String>  getRow();
		void setRow(List<String> row);
	}
	

	public interface GraphConfig{
		long getDocID();
		void setDocID(long docid);
		String[] getColumnNames();
		void  setColumnNames(String[] columnNames);
		long getChartType();
		void setChartType(long chartType);
		
		
	}

//	public void test() {
//
//		GQuery.getJSON("test.json", null, new Function() {
//			public void f() {
//				// Docs s = GQ.create(Docs.class).load(arguments(0));
//				// Window.alert("Response received: " + s.getFile() + " " + s.getFile()[0] + " ");
//			}
//		});
//
//	}

//	public void test1() {
//		GQuery.getJSONP("http://10.0.0.137:8080/a", GQuery.$$("callbackParameter: ?, otherParameter: 'abate ver'"), new Function() {
//			public void f() {
//				Window.alert("success " + getDataProperties().toJsonString());
//			}
//		});
//	}

//	public void test2() {
//		// Docs s = GQ.create(Docs.class);
//		// s.parse(json);
//		// Window.alert(s.getFile()[0].getMAlias()+" : "+s.getFile()[0].getMIndex());
//		
//	}
	
	public static long getDocFileId(Docs docs, String mAlias) {
		long id = -1;
		if (docs != null) {
			for (File file : docs.getFiles()) {
				if(file.getMAlias().equals(mAlias)){
					return file.getMIndex();
				}
			}
		}
		return id;
	}

	
	
	public static String getJsonFiles(){
		return getJsonFiles(jsonFiles);
	}
	public static String getJsonColumns(){
		return getJsonColumns(jsonColumns);
	}
	public static String getJsonData(){
		return getJsonData(jsonData);
	}	

	
	public static String getJsonFiles(String files){
		return "{\"files\":"+files+"}";
	}
	public static String getJsonColumns(String columns){
		return "{\"columns\":"+columns+"}";
	}
	public static String getJsonData(String data){
		return "{\"data\":"+data+"}";
	}	
	
	final public static String jsonFiles = "[{\"MAlias\":\"No Server. Using Sample Data.\",\"MIndex\":\"0\"},{\"MAlias\":\"Solids_DataBlock_00359.csv\",\"MIndex\":\"1\"},{\"MAlias\":\"Solids_DataBlock.csv\",\"MIndex\":\"2\"}]";

	final public static String jsonColumns = "[\"Granular barcode\",\"Granular Lot\"]";
	
	final public static String jsonData = "[[\"229031\",\"JNJ-56021927-n001-00356\"],[\"229032\",\"JNJ-56021927-n001-00357\"],[\"229033\",\"JNJ-56021927-n001-00358\"],[\"229034\",\"JNJ-56021927-n001-00359\"],[\"229035\",\"JNJ-56021927-n001-00359\"],[\"229036\",\"JNJ-56021927-n001-00359\"]]";
	
	public Docs decodeFiles(String rawDataToDecode){
		String filesJson= MRoute.getJsonFiles(rawDataToDecode);
		AutoBean<Docs> docsBean = AutoBeanCodex.decode(docFactory, Docs.class, filesJson);
		Docs docs = docsBean.as();
		return docs;
	}
	
	
	public List<Item> decodeColumns(String fileID,String rawDataToDecode){
		String columnsJson = MRoute.getJsonColumns(rawDataToDecode);
		AutoBean<Columns> columnsBean = AutoBeanCodex.decode(columnsFactory, Columns.class, columnsJson);
		Columns columns = columnsBean.as();
		List<Item> columnData = ItemUtil.convert(columns);
		if(columnData!=null&& columnData.size()>0){
			if(!mRouteCache.containsKey("file"+fileID)){
				setMRouteCacheColumns(fileID,rawDataToDecode);
			}
		}
		return columnData;
	}
	
	public  List<Item> getCachedColumns(String fileID){
		 String data = mRouteCache.get("file"+fileID);
		 if(data!=null && data.length()>0){
			 return decodeColumns(fileID,data);
		 }else{
			 return null;
		 }
	}
	
	
	public void setMRouteCacheColumns(String fileID,String data){
		//mRouteCache.put("file"+fileID,data);
	}	
	public String getMRouteCacheColumns(String fileID){
		return mRouteCache.get("file"+fileID);
	}
}
