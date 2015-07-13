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
package com.itgp.gwtviz.client.ui.runtime.decorator.modelV1;

import com.itgp.gwtviz.client.ui.runtime.*;
import com.itgp.gwtviz.client.ui.runtime.ColMeta.TYPE;
import com.itgp.gwtviz.client.ui.runtime.decorator.Filter;
import com.itgp.gwtviz.shared.model.Item;
import java.util.*;

/*
 */

/**
 * <p>
 *
 * @author Warp
 */
public class FilterColumnExactMatch extends Filter {

    public static double equalityThreshold = 0.000001;
    protected Item       filterItem;
    protected boolean    initialized = false;

    // Fields below this are updated during initialize()
    protected String            colName;
    protected int               colNumber;
    protected HashSet<String>   filterMatchStrings;
    protected ArrayList<Double> filterMatchNumeric;

    public FilterColumnExactMatch(Item filterItem) {

        this.filterItem = filterItem;
    }

    protected void initialize() {
        try {

            DataModelV1 dataModel = (DataModelV1) getFilterSet().getModel();

            this.colName   = this.filterItem.getValue();
            this.colNumber = dataModel.getColumnPosition(colName);
            ColMeta    col                = dataModel.getColMetaArray()[colNumber];
            TYPE       colType            = col.type;

            List<Item> colFilterDataArray = this.filterItem.getData();
            for (Item dataItem : colFilterDataArray) {

                String filterValue = dataItem.getValue();
                if (colType == TYPE.NUMBER) {

                    if (this.filterMatchNumeric == null) {
                        this.filterMatchNumeric = new ArrayList<Double>();
                    }

                    Double dVal = 0.0;
                    try {
                        dVal = Double.parseDouble(filterValue);
                    } catch (Throwable t) {}

                    this.filterMatchNumeric.add(dVal);
                } else {

                    if (this.filterMatchStrings == null) {
                        this.filterMatchStrings = new HashSet<String>();
                    }

                    this.filterMatchStrings.add(filterValue);
                }

            }

        } finally {
            this.initialized = true;
        }
    }

    @Override
    public boolean accept(int modelIndex) {
        if (!initialized) {
            initialize();
        }

        if (this.colNumber < 0) {
            return false;
        }

        DataModelV1 dataModel = (DataModelV1) getFilterSet().getModel();
        try {
            ColMeta col = dataModel.getColMetaArray()[colNumber];
            if (col.type == TYPE.NUMBER) {
                Double dValue = (Double) dataModel.getData()[modelIndex][colNumber];
                double value  = dValue.doubleValue();
                for (Double dVal : filterMatchNumeric) {
                    double filterValue = dVal.doubleValue();

                    // best we can do is see of the difference in absolute terms is smaller than the arbitrary threshold
                    if (Math.abs(value - filterValue) < equalityThreshold) {
                        return true;
                    }

                }

                return false;
            } else {
                String value = (String) dataModel.getData()[modelIndex][colNumber];

                return this.filterMatchStrings.contains(value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // public static boolean eq(Object obj1, Object obj2) {
    // if (obj1 == null && obj2 == null) {
    // return true;
    // } else {
    // if (obj1 == null && obj2 != null) {
    // return false;
    // } else if (obj1 != null && obj2 == null) {
    // return false;
    // } else {
    // return obj1.equals(obj2);
    // }
    // }
    // }
}
