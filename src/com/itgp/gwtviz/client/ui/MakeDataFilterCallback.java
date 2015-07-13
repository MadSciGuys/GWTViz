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

import com.itgp.gwtviz.client.ui.config.*;
import com.itgp.gwtviz.client.ui.runtime.*;
import com.itgp.gwtviz.client.ui.runtime.event.*;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;

public class MakeDataFilterCallback {
	GraphConfigImpl_1 graphConfigImpl;
	INotify iNotify;

	enum Type {
		CVS, PREVIEW
	};

	public MakeDataFilterCallback(GraphConfigImpl_1 graphConfigImpl, INotify iNotify) {
		this.graphConfigImpl = graphConfigImpl;
		this.iNotify = iNotify;
	}

	public void applyCSVFilter() {
		String dataArray = MainDesktop.getCurrentDataModelRawData();
		if (dataArray == null || dataArray.length() == 0) {
			return;
		}

		ProcessCVSFile cvsFilter = new ProcessCVSFile();

		RuntimeEntry_Part2.getFilteredData(graphConfigImpl, dataArray, cvsFilter);
	}

	public void applyPreviewFilter() {
		String dataArray = MainDesktop.getCurrentDataModelRawData();
		if (dataArray == null || dataArray.length() == 0) {
			return;
		}

		ProcessPreviewFile previewFilter = new ProcessPreviewFile();

		RuntimeEntry_Part2.getFilteredData(graphConfigImpl, dataArray, previewFilter);
	}

	class ProcessCVSFile implements FilterCompleteListener {

		public ProcessCVSFile() {
		}

		// listener gets called in a different thread from the starting one because there is an Async call to the server in between
		// that is also the reason why I can't simply return the filtered data model directly and need a listener.

		@Override
		public void filterComplete(FilterCompleteEvent evt) {
			StringBuffer sb = new StringBuffer();
			String _CR_NL = "\r\n";

			RuntimeDataV1 runtimeData = evt.getRuntimeData();
			DataModelV1 dataModel = runtimeData.dataModel;
			int viewRowCount = dataModel.getRowCount();

			// Make CSV Headings
			int colCount = dataModel.getColumnCount(); // same in view and model
			String[] columnLabelArray = new String[colCount];
			for (int i = 0; i < colCount; i++) {
				String columnLabel = dataModel.getColumnLabel(i);
				columnLabelArray[i] = columnLabel;
				sb.append(columnLabel);
				sb.append(",");
			}
			if (colCount > 1) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append(_CR_NL);

			// Process all rows
			for (int viewRowIndex = 0; viewRowIndex < viewRowCount; viewRowIndex++) {
				// ArrayList row = new ArrayList();
				// Process one row
				for (int columnIndex = 0; columnIndex < colCount; columnIndex++) {
					Object viewCellValue = dataModel.getValueAt(viewRowIndex, columnIndex); // This is either String or Double
					// row.add(viewCellValue);
					sb.append(viewCellValue);
					sb.append(",");
				}
				if (sb.substring(sb.length() - 1).equals(",")) {
					sb.deleteCharAt(sb.length() - 1);
				}
				sb.append(_CR_NL);
				// consoleLog("View row# " + viewRowIndex + ": " + row.toString());
			}

			NotifyPayload np = new NotifyPayload(null, ProcessCVSFile.class.getName(), sb.toString());
			np.setType(Type.CVS.name());
			iNotify.processNotify(np);

			// String[][] dataFromAllColumns = dataModel.getDesignDataAllColumns();
			// String[] row0 = dataFromAllColumns[0];
			// String row0col0 = row0[0];

			// consoleLog("\n\n\n");
			// // Model (unfiltered now)
			// int modelRowCount = dataModel.getModelRowCount();
			// for (int modelRowIndex = 0; modelRowIndex < modelRowCount; modelRowIndex++) {
			// ArrayList row = new ArrayList();
			// for (int columnIndex = 0; columnIndex < colCount; columnIndex++) {
			// Object modelCellValue = dataModel.getModelValueAt(modelRowIndex, columnIndex);
			// row.add(modelCellValue);
			// }
			//
			// consoleLog("Model row# " + modelRowIndex + ": " + row.toString());
			// }

		}
	}

	class ProcessPreviewFile implements FilterCompleteListener {

		public ProcessPreviewFile() {
		}

		@Override
		public void filterComplete(FilterCompleteEvent evt) {

			// http://www.lonniebest.com/formatcss/
			// http://tablestyler.com/#
			String headS = "<thead><tr>";
			String headE = "</tr></thead>";
			String trS = "<tr>";
			String trSAlt = "<tr class=\"alt\">";
			String trE = "</tr>";
			String tdS = "<td>";
			String tdE = "</td>";
			String thS = "<th>";
			String thE = "</th>";
			String t1 = "<table style=\"width:100%\">";
			String divS = "<div class=\"gwtvizgridpreview\">";
			String divE = "</div>";

			StringBuffer sb = new StringBuffer();

			RuntimeDataV1 runtimeData = evt.getRuntimeData();
			DataModelV1 dataModel = runtimeData.dataModel;
			int viewRowCount = dataModel.getRowCount();

			// Make CSV Headings
			int colCount = dataModel.getColumnCount(); // same in view and model
			String[] columnLabelArray = new String[colCount];

			sb.append(divS);
			sb.append(t1);
			sb.append(headS);

			for (int i = 0; i < colCount; i++) {
				String columnLabel = dataModel.getColumnLabel(i);
				columnLabelArray[i] = columnLabel;
				sb.append(thS);
				sb.append("  " + columnLabel + "  ");
				sb.append(thE);
			}
			sb.append(headE);

			// Process all rows
			for (int viewRowIndex = 0; viewRowIndex < viewRowCount; viewRowIndex++) {
				if (viewRowIndex % 2 == 0) {
					sb.append(trS);
				} else {
					sb.append(trSAlt);
				}

				// Process one row
				for (int columnIndex = 0; columnIndex < colCount; columnIndex++) {
					Object viewCellValue = dataModel.getValueAt(viewRowIndex, columnIndex); // This is either String or Double
					sb.append(tdS);
					sb.append(viewCellValue);
					sb.append(tdE);
				}
				sb.append(trE);
			}

			sb.append("</table");
			sb.append(divE);

			NotifyPayload np = new NotifyPayload(null, ProcessPreviewFile.class.getName(), sb.toString());
			np.setType(Type.PREVIEW.name());
			iNotify.processNotify(np);

			// String[][] dataFromAllColumns = dataModel.getDesignDataAllColumns();
			// String[] row0 = dataFromAllColumns[0];
			// String row0col0 = row0[0];

			// consoleLog("\n\n\n");
			// // Model (unfiltered now)
			// int modelRowCount = dataModel.getModelRowCount();
			// for (int modelRowIndex = 0; modelRowIndex < modelRowCount; modelRowIndex++) {
			// ArrayList row = new ArrayList();
			// for (int columnIndex = 0; columnIndex < colCount; columnIndex++) {
			// Object modelCellValue = dataModel.getModelValueAt(modelRowIndex, columnIndex);
			// row.add(modelCellValue);
			// }
			//
			// consoleLog("Model row# " + modelRowIndex + ": " + row.toString());
			// }

		}
	}
}
