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
package com.itgp.gwtviz.client.ui.runtime.decorator;

import com.itgp.gwtviz.client.ui.runtime.decorator.modelV1.ColumnSorter;
import java.util.*;

/**
 <p>
 @author Warp
 */
public class MultiCriteriaSorter extends Sorter{
    
    
    protected ArrayList<SorterCriterion> sortCriteriaList = new ArrayList<SorterCriterion>();

    
    public static MultiCriteriaSorter create( ColumnSorter...  columnSorters){
        MultiCriteriaSorter instance = new MultiCriteriaSorter();
        
        for (int i=0; i < columnSorters.length; i++){
            SorterCriterion columnSorter=columnSorters[i];
            instance.add(columnSorter);
            
        }
        
        return instance;
    }
        public static MultiCriteriaSorter create( ArrayList<ColumnSorter>  columnSorters){
        MultiCriteriaSorter instance = new MultiCriteriaSorter();
        
        for (int i=0; i < columnSorters.size(); i++){
            SorterCriterion columnSorter=columnSorters.get(i);
            instance.add(columnSorter);
            
        }
        
        return instance;
    }
    
    public void add(SorterCriterion sorter){
        if ( sorter == null){
            return;
        }
        sortCriteriaList.add(sorter); // could have duplicates in theory
    }
    
    @Override
    public int compare( int rowIndex1, int rowIndex2){
        for (SorterCriterion criterion: sortCriteriaList){
            
            Sorter sorter = criterion.getSorter();
            sorter.setFilterSet( this.getFilterSet()); // delayed assignment (this contains the model)
            
            int comparison = sorter.compare(rowIndex1, rowIndex2);
            if ( comparison != 0){
                // stop at the first difference
                if ( sorter.getSortDirection() == Sorter.DESCENDING){
                    comparison =  0-comparison; // reverse
                }
                return comparison;
            }
            
        }
        return 0;
    }
}
