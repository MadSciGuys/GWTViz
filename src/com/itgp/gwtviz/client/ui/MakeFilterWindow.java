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

import java.util.List;

import com.google.gwt.user.client.Window;
import com.itgp.gwtviz.client.comm.ServerComm;
import com.itgp.gwtviz.client.ui.config.IMRouteCallable;
import com.itgp.gwtviz.client.ui.config.WindowDataSubset;
import com.itgp.gwtviz.client.ui.widget.Sizer;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.model.Item;
import com.itgp.gwtviz.shared.model.ItemUtil;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;

public class MakeFilterWindow implements IMRouteCallable {

	ConfigPanel_1 configPanel_1;
	MainDesktop mainDesktop;
	GraphConfigImpl_1 graphConfigImpl;

	public MakeFilterWindow(ConfigPanel_1 configPanel_1, GraphConfigImpl_1 graphConfigImpl) {
		this.configPanel_1 = configPanel_1;
		this.graphConfigImpl = graphConfigImpl;
	}

	public void showFilterWindow() {
		if (MainDesktop.getCurrentDataModel() == null || MainDesktop.getCurrentDataModel().size() == 0) {
			String mRouteURL = MainDesktop.getMRouteURL();
			ServerComm.getAllMRouteColumns(mRouteURL, graphConfigImpl.getDataSourceID(), this);
		} else {
			List<Item> filteredItems = ItemUtil.copy(MainDesktop.getCurrentDataModel());
			ItemUtil.elimateDuplicateData(filteredItems);
			showFilterWindowImpl(filteredItems);
		}
	}

	@Override
	public void setData(List<Item> filteredItems) {
		showFilterWindowImpl(filteredItems);
	}

	private void showFilterWindowImpl(List<Item> filteredItems) {
		final WindowDataSubset dsw = new WindowDataSubset(graphConfigImpl, configPanel_1, filteredItems);
		dsw.setModal(true);
		dsw.show();
		dsw.toFront();
		dsw.centerWindow(configPanel_1.getParentContentPanel(),0.1);
		dsw.forceLayout();
		
		
			dsw.addBeforeHideHandler(new BeforeHideEvent.BeforeHideHandler() {
			private boolean shouldCancel = true;

			@Override
			public void onBeforeHide(final BeforeHideEvent evt) {
					if (dsw.hasChanges()) {
					evt.setCancelled(shouldCancel);
					if (evt.isCancelled()) {
						final ConfirmMessageBox box = new ConfirmMessageBox("Please confirm",
								"<p>Unsaved changes detected.<p><p> Do you still want to close this window?");
						final DialogHideHandler hideHandler = new DialogHideHandler() {
							@Override
							public void onDialogHide(DialogHideEvent event) {
								if (event.getHideButton() == PredefinedButton.YES) {
									shouldCancel = false;
									evt.setCancelled(true);
									dsw.hide();
									shouldCancel = false;
									evt.setCancelled(true);
									box.hide();
								}
							}
						};
						box.addDialogHideHandler(hideHandler);
						box.show();
						Sizer sizer = new Sizer();
						sizer.setDialogSize(configPanel_1.getParentContentPanel().getElement().getSize(), box);
						
//						int h = Drupal.getTopWindowHeight();
//						int w = Drupal.getTopWindowWidth();
//						int thisWidth = box.getMinWidth();
//						int thisHeight = box.getMinHeight();
//						box.setPagePosition((w-thisWidth)/2, (h-thisHeight)/2);
					}
				}
			}
		});

		// dsw.addListener(Events.Hide, new Listener<ComponentEvent>() {
		// @Override
		// public void handleEvent(ComponentEvent be) {
		// // Do stuff
		// }
		// });
	}
}
