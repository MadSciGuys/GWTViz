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
package com.itgp.gwtviz.client.comm;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Window;
import com.itgp.gwtviz.client.ServerService;
import com.itgp.gwtviz.client.ServerServiceAsync;
import com.itgp.gwtviz.client.ui.ConfigPanel_1;
import com.itgp.gwtviz.client.ui.MainDesktop;
import com.itgp.gwtviz.client.ui.config.IMRouteCallable;
import com.itgp.gwtviz.shared.model.Item;
import com.itgp.gwtviz.shared.model.ItemUtil;
import com.itgp.gwtviz.shared.model.MRoute;
import com.itgp.gwtviz.shared.model.MRoute.Docs;

public class ServerComm {

	static private final ServerServiceAsync serverService = GWT.create(ServerService.class);

	public static void alert(Throwable e) {
		Window.alert(e.getMessage());
	}

	public static void alert(String e) {
		Window.alert(e);
	}

	public static final String getAllColumns = "/h/";
	public static final String getAllFiles = "/a";
	public static final String getData = "/c/";

	public static void getAllMRouteColumns(final String mRouteURL, final long fileID, final IMRouteCallable mRouteCallableWidget) {
		final MRoute mRoute = new MRoute();
		String url = mRouteURL + getAllColumns + fileID;
		List<Item> columnData = mRoute.getCachedColumns("" + fileID);
		if (columnData != null) {
			mRouteCallableWidget.setData(columnData);
			return;
		}
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest("", new RequestCallback() {
				public void onError(Request request, Throwable e) {
					alert(e);
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						System.out.println("COLUMNS>>>> " + response.getText());
						List<Item> columnData2 = mRoute.decodeColumns("" + fileID, response.getText());
						getMRouteData(mRouteURL, fileID, columnData2, mRouteCallableWidget);
					} else {
						alert("Received HTTP status code other than 200 : " + response.getStatusText());
					}
				}
			});
		} catch (RequestException e) {
			alert(e);
		}
	}

	public static void getMRouteData(String mRouteURL, final long fileID, final List<Item> columnData, final IMRouteCallable mRouteCallableWidget) {
		String columnURL = ItemUtil.makeColumnURL(columnData) + "";
		String url = mRouteURL + getData + fileID + "/" + columnURL;
		String urlEncoded = UriUtils.encode(url);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, urlEncoded);
		try {
			builder.sendRequest("", new RequestCallback() {
				public void onError(Request request, Throwable e) {
					alert(e);
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						String dataArray = response.getText();
						ItemUtil.addDataToColumns(dataArray, columnData);
						MainDesktop.setCurrentDataModel(ItemUtil.copy(columnData), dataArray); // Make a copy for further reference
						ItemUtil.elimateDuplicateData(columnData);
						// List<Item> columnData2 = mRoute.decodeColumns(""+fileID,response.getText());
						mRouteCallableWidget.setData(columnData);
					} else {
						alert("Received HTTP status code other than 200 : " + response.getStatusText());
					}
				}
			});
		} catch (RequestException e) {
			alert(e);
		}
	}

	public static void getAllMRouteFiles(String mRouteURL) {
		final MRoute mRoute = new MRoute();
		final String url = mRouteURL + getAllFiles;

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest("", new RequestCallback() {
				public void onError(Request request, Throwable e) {
					alert(e);
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						MainDesktop.consoleLog("getAllMRouteFiles 1");
						Docs docs = mRoute.decodeFiles(response.getText());
						MainDesktop.consoleLog("getAllMRouteFiles 2");
						MainDesktop.setMRouteDocs(docs);
						MainDesktop.consoleLog("getAllMRouteFiles 3");
						MainDesktop.detectModeAndMount();
						MainDesktop.consoleLog("getAllMRouteFiles 4");

					} else {
						MainDesktop.consoleLog("getAllMRouteFiles 5");
						alert("Received HTTP status code other than 200 : " + response.getStatusText() + "\n" + url);
						MRoute mRoute = new MRoute();
						Docs docs = mRoute.decodeFiles(MRoute.jsonFiles);
						MainDesktop.setMRouteDocs(docs);
						MainDesktop.detectModeAndMount();
					}
				}
			});
		} catch (RequestException e) {
			alert(e);
		}
	}

	public static void getMRouteAddress() {
		//String mRouteURL = GWT.getModuleBaseURL() + "../../visuals/mroute.url";
		String mRouteURL = GWT.getModuleBaseURL() + "../visuals/mroute.url"; 
		MainDesktop.consoleLog("MRoute URL Path "+mRouteURL);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, mRouteURL);
		try {
			builder.sendRequest("", new RequestCallback() {
				public void onError(Request request, Throwable e) {
					String error = "MRoute location not found.  Use 'Managa Data Block' to enter MRoute URL"+e.getMessage();
					MainDesktop.consoleLog(error);
					alert(error);
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						String[] data = response.getText().split(";");
						// 0 : MROUTE URL
						// 1 : DEBUG -> 1:0

						if (data!= null && data.length > 1) {
							MainDesktop.setMRouteURL(data[0]);

							if (data[1].equals("1")) {
								MainDesktop.IN_DEBUG_MODE = true;
							} else {
								MainDesktop.IN_DEBUG_MODE = false;
							}
						}else{
							MainDesktop.IN_DEBUG_MODE = false;
						}
						

						ServerComm.getAllMRouteFiles(MainDesktop.getMRouteURL());
					} else {
						String error = "MRoute location not found.  Use 'Managa Data Block' to enter MRoute URL"+response.getStatusText();
						MainDesktop.consoleLog(error);
						alert(error);
						
					}
				}
			});
		} catch (RequestException e) {
			alert(e);
		}

	}

	// public void getURL() {
	// String postUrl = "http://localhost:8000";
	// String requestData = "q=ABC&callback=callback125";
	// RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, postUrl);
	// try {
	// builder.sendRequest(requestData.toString(), new RequestCallback() {
	// public void onError(Request request, Throwable e) {
	//
	// String URL = GWT.getModuleBaseURL();
	// Window.alert(e.getMessage() + "   " + URL);
	// }
	//
	// public void onResponseReceived(Request request, Response response) {
	// String URL = GWT.getModuleBaseURL();
	// if (200 == response.getStatusCode()) {
	// Window.alert(response.getText() + "   " + URL);
	// } else {
	// Window.alert("Received HTTP status code other than 200 : " + response.getStatusText() + "   " + URL);
	// }
	// }
	// });
	// } catch (RequestException e) {
	// // Couldn't connect to server
	// Window.alert(e.getMessage());
	// }
	// }

	public static void getConfigString(final ConfigPanel_1 configPanel_1, String tag) {
		// serverService.readConfiguration(tag, new AsyncCallback<String>() {
		// public void onFailure(Throwable caught) {
		//
		// }
		//
		// @Override
		// public void onSuccess(String configText) {
		// if (configText != null && configText.length() > 0) {
		// try {
		// if (configPanel_1 == null) {
		// MainDesktop.execRuntimeGraph(configText);
		// } else {
		// configPanel_1.initWithData(configText);
		// configPanel_1.readCurrentStateFromConfig();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// });
	};

	public static void saveConfigString(String tag, String data) {
		// serverService.saveConfiguration(tag, data, new AsyncCallback<String>() {
		// public void onFailure(Throwable caught) {
		//
		// }
		//
		// @Override
		// public void onSuccess(String result) {
		// if (result == null) {
		// try {
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return;
		// }
		// }
		// });
	};

}
