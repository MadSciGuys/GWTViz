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
package com.itgp.gwtviz.client.ui.runtime;

import static com.itgp.gwtviz.client.ui.runtime.RuntimeDataV1.*;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.*;
import com.itgp.gwtviz.client.ui.runtime.decorator.FilterSet;
import java.util.*;

/**
 * <p>
 * @author Warp
 */
public class DataModelV1 {

    protected ColMeta[]                colMetaArray;    // initialized in setColumnDescription
    protected HashMap<String, Integer> colNameMap = null;

    protected Object[][]               data;
    protected FilterSet                filterSet;
    protected ArrayList<Serie>         series           = new ArrayList<Serie>();

    protected JavaScriptObject         designDataNative = null;
    protected JSONArray                designDataJava   = null;

    public int getModelRowCount() {
        if (getData() == null) {
            return 0;
        }

        return getData().length;
    }

    public int getRowCount() {

        // view row count that is
        int rowCount = 0;
        if (getFilterSet() != null) {
            rowCount = getFilterSet().getOutputSize();
        } else {
            rowCount = getModelRowCount();
        }

        return rowCount;

    }

    public int getColumnCount() {
        if (getColMetaArray() == null) {
            return 0;
        }

        return getColMetaArray().length;
    }

    public String getColumnName(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            return null;
        }

        return getColMetaArray()[columnIndex].name;
    }

    public String getColumnLabel(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            return null;
        }

        return getColMetaArray()[columnIndex].getLabel();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        // get a value using view coordinates
        int rowCount = getRowCount();
        if (rowIndex < 0 || rowIndex >= rowCount) {
            return null;
        }

        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            return null;
        }

        int modelIndex = rowIndex;
        if (getFilterSet() != null) {
            modelIndex = getFilterSet().convertIndexToModel(rowIndex);
        }

        Object value = getModelValueAt(modelIndex, columnIndex);

        return value;
    }

    public int getModelIndex(int viewIndex) {
        int modelIndex = viewIndex;
        if (getFilterSet() != null) {
            modelIndex = getFilterSet().convertIndexToModel(viewIndex);
        }

        return modelIndex;
    }

    public int getViewIndex(int modelIndex) {
        int viewIndex = modelIndex;
        if (getFilterSet() != null) {
            viewIndex = getFilterSet().convertIndexToView(modelIndex);
        }

        return viewIndex;
    }

    public Object getModelValueAt(int modelIndex, int columnIndex) {
        if (modelIndex < 0 || modelIndex > getModelRowCount()) {
            return null;
        }

        Object value = getData()[modelIndex][columnIndex];

        return value;

    }

    /**
     * Gets the position of a column based on its name
     * @param columnName
     * @return  -1 if not found
     */
    public int getColumnPosition(String columnName) {
        if (this.colNameMap == null) {
            return -1;
        }

        Integer val = this.colNameMap.get(columnName);
        if (val == null) {
            return -1;
        } else {
            return val.intValue();
        }
    }

    public ColMeta getColMeta(String columnName) {
        int colPos = getColumnPosition(columnName);
        if (colPos == -1) {
            return null;
        }

        return this.colMetaArray[colPos];
    }

    /**
     * @return the colMetaArray
     */
    public ColMeta[] getColMetaArray() {
        return colMetaArray;
    }

    /**
     * @param colMetaArray the colMetaArray to set
     */
    public void setColMetaArray(ColMeta[] colMetaArray) {
        this.colMetaArray = colMetaArray;

        if (colMetaArray == null) {
            this.colNameMap = null;

            return;
        }

        this.colNameMap = new HashMap<String, Integer>();

        for (int i = 0; i < colMetaArray.length; i++) {
            ColMeta colMeta = colMetaArray[i];

            if (colMeta != null) {
                colNameMap.put(colMeta.name, colMeta.position);
            }
        }    // for
    }

    /**
     * @return the data
     */
    public Object[][] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object[][] data) {
        this.data = data;
    }

    /**
     * @return the filterSet
     */
    public FilterSet getFilterSet() {
        return filterSet;
    }

    /**
     * @param filterSet the filterSet to set
     */
    public void setFilterSet(FilterSet filterSet) {
        this.filterSet = filterSet;
    }

    public int convertToModelIndex(int viewIndex) {
        return filterSet.convertIndexToModel(viewIndex);
    }

    public int convertToViewView(int modelIndex) {
        return filterSet.convertIndexToView(modelIndex);
    }

    public JavaScriptObject getDesignDataNative() {
        return designDataNative;
    }

    public void setDesignDataNative(JavaScriptObject designDataNative) {
        this.designDataNative = designDataNative;
    }

    public JSONArray getDesignDataJava() {
        return designDataJava;
    }

    public void setDesignDataJava(JSONArray designDataJava) {
        this.designDataJava = designDataJava;
    }

    public String[][] getDesignDataAllColumns() {

        String[][] data1      = null;

        boolean    nativeData = (getDesignDataNative() != null);
        if (!nativeData) {
            if (getDesignDataJava() == null) {
                data1 = new String[0][];
            }

        }

        if (nativeData) {
            JavaScriptObject nativeJSData = getDesignDataNative();
            int              numberOfRows = nativeLength(nativeJSData);
            if (numberOfRows == 0) {
                return new String[0][];
            }

            JavaScriptObject row             = getArray(nativeJSData, 0);    // get the first row
            int              numberOfColumns = nativeLength(row);

            data1 = new String[numberOfRows][numberOfColumns];

            for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
                row = getArray(nativeJSData, rowIndex);

                for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
                    String cellValue = get(row, columnIndex);
                    data1[rowIndex][columnIndex] = cellValue;
                }                                                            // for colIndex
            }                                                                // for rowIndex
        } else {
            // not native
            JSONArray nativeJSData = getDesignDataJava();
            int       numberOfRows = nativeJSData.size();
            if (numberOfRows == 0) {
                return new String[0][];
            }

            JSONArray row             = (JSONArray) nativeJSData.get(0);         // get the first row
            int       numberOfColumns = row.size();

            data1 = new String[numberOfRows][numberOfColumns];

            for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
                row = (JSONArray) nativeJSData.get(rowIndex);

                for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {

                    JSONString cell      = (JSONString) row.get(columnIndex);    // everything is a string
                    String     cellValue = cell.stringValue();
                    data1[rowIndex][columnIndex] = cellValue;
                }                                                                // for colIndex
            }                                                                    // for rowIndex            
        }

        return data1;
    }
}
