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
package com.itgp.gwtviz.client.ui.runtime.decorator.modelV1;

import com.itgp.gwtviz.client.ui.runtime.*;
import com.itgp.gwtviz.client.ui.runtime.decorator.Sorter;

/**
 <p>
 @author Warp
 */
public class DataModelV1Sorter extends Sorter {

    ColumnSorter sc;

    public DataModelV1Sorter(ColumnSorter sc){
        this.sc=sc;
    }
    
    @Override
    public int compare(int modelRow1, int modelRow2){
        int colNo = sc.getColumn();
        
        DataModelV1 model = getDataModel();
        Object val1 =  model.getData()[ modelRow1][colNo]; // String or Double
        Object val2 =  model.getData()[ modelRow2][colNo]; // String or Double
        
        int comparison = 0 ;
        if (val1 == null){
            if ( val2 == null){
                comparison =  0; // null == null;
            } else {
                comparison = -1; // null < val2
            } 
        } else {
            if ( val2 == null){
                comparison = 1; // val1 > null
            }  else {
                //Both String and Double implement Comparable
                comparison = ((Comparable) val1).compareTo((Comparable) val2);
            }
        }
        
        return comparison;
    }
    
    public DataModelV1 getDataModel() {
        //FilterSet is set by MultiCriteriaSorter right before invoking compare
        DataModelV1 model = (DataModelV1) this.getFilterSet().getModel();

        return model;
    }
    
}
