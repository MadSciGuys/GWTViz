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
package com.itgp.gwtviz.server.rm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GWTVizServerImpl {

	public static void saveConfig(String tag, String data) {
		writeFile(cacheLocation, cacheFileName, data);
	}

	public static String readConfig(String tag) {
		String content = null;
		try {
			content = readFile(cacheLocation + cacheFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("Can't read cache file " + cacheLocation + "/" + cacheFileName, e);
		}
		return content;
	}

	protected static final Logger log = LoggerFactory.getLogger(GWTVizServerImpl.class);

	public static String cacheLocation = "C:\\_subversion\\trunk\\jnj\\GWTViz\\war\\dataCache\\";
	public static String cacheFileName = "GWTVizConfig.json";

	public static void writeFile(String dir, String fileName, String content) {
		try {
			File f = new File(dir, fileName);
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			FileWriter w = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(w);
			bw.write(content);
			bw.close();
			w.close();
		} catch (IOException ioe) {
			log.error("Can't write cache file " + dir + "/" + fileName, ioe);
		}
	}

	public static String readFile(String path) throws FileNotFoundException {
		java.util.Scanner s = new java.util.Scanner(new File(path)).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	// public static ReturnData readFromCache(String table) {
	// ReturnData rd = new ReturnData();
	// String data = "";
	// try {
	// data = readFile(cacheLocation + table + ".cache");
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// // e.printStackTrace();
	// log.error("File "+cacheLocation + table + ".cache not found",e);
	// }
	// ReturnDataElem rde = rd.getElem(0);
	// rde.setData(data);
	// rde.setCurrentRows(2);
	// rde.setMaxRows(99);
	// return rd;
	// }

}
