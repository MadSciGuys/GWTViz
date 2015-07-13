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
package com.itgp.gwtviz.client.ui.loader;

import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class Utils {
	
	
//	
//	  com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
//	      @Override
//	      public void onResize(ResizeEvent event) {
//	    	  adjustColumnWidths(grid);
//	      }
//	    });


	  public static void adjustColumnWidths(Grid grid) {
		  int DEFAULT_COLUMN_WIDTH= 50;

		    int requiredWidth = 0;
		    boolean isRefreshRequired = false;
		    int cellCount = grid.getColumnModel().getColumnCount();
		    for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
		      ColumnConfig<Object, Object> columnConfig = grid.getColumnModel().getColumn(cellIndex);
		      if (!columnConfig.isHidden()) {
		        int columnWidth = columnConfig.getWidth();
		        if (cellIndex > 0 && columnWidth < DEFAULT_COLUMN_WIDTH) {
		          columnWidth = DEFAULT_COLUMN_WIDTH;
		          columnConfig.setWidth(columnWidth);
		          isRefreshRequired = true;
		        }
		        requiredWidth += columnWidth;
		      }
		    }
		    int offsetWidth = grid.getOffsetWidth();
		    boolean isAutoFillRequired = requiredWidth < offsetWidth;
		    if (isAutoFillRequired != grid.getView().isAutoFill()) {
		      isRefreshRequired = true;
		      grid.getView().setAutoFill(isAutoFillRequired);
		    }
		    if (isRefreshRequired) {
		    	grid.getView().refresh(true);
		    }
		  }	
	  
	  
		/*http://greatlogic.com/2014/06/02/autofit-column-width-in-a-gxt-grid/*/
//		private void resizeColumnToFit(Grid grid,final int columnIndex) {
//			  final ColumnConfig<Pet, ?> columnConfig = _grid.getColumnModel().getColumn(columnIndex);
//			  final TextMetrics textMetrics = TextMetrics.get();
//			  textMetrics.bind(_grid.getView().getHeader().getAppearance().styles().head());
//			  int maxWidth = textMetrics.getWidth(columnConfig.getHeader().asString()) + 6
//			  if (_petStore.size() > 0) {
//			    textMetrics.bind(_grid.getView().getCell(1, 1));
//			    for (final Pet pet : _petStore.getAll()) {
//			      final Object value = columnConfig.getValueProvider().getValue(pet);
//			      if (value != null) {
//			        String valueAsString;
//			        if (columnConfig.getCell() instanceof DateCell) {
//			          valueAsString = _dateTimeFormat.format((Date)value);
//			        }
//			        else {
//			          valueAsString = value.toString();
//			        }
//			        final int width = textMetrics.getWidth(valueAsString) + 10;
//			        maxWidth = width > maxWidth ? width : maxWidth;
//			      }
//			    }
//			    for (final Store<Pet>.Record record : grid.getColumnModel().getModifiedRecords()) {
//			      final String valueAsString = record.getValue(columnConfig.getValueProvider()).toString();
//			      final int width = textMetrics.getWidth(valueAsString) + 10;
//			      maxWidth = width > maxWidth ? width : maxWidth;
//			    }
//			  }
//			  columnConfig.setWidth(maxWidth);
//			  if (_checkBoxSet.contains(columnConfig)) {
//			    centerCheckBox(columnConfig);
//			  }
//			}


}
