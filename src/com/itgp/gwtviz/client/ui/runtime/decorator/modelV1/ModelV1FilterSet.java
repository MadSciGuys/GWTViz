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

import com.itgp.gwtviz.client.ui.runtime.DataModelV1;
import com.itgp.gwtviz.client.ui.runtime.decorator.Filter;
import com.itgp.gwtviz.client.ui.runtime.decorator.FilterSet;
import com.itgp.gwtviz.client.ui.runtime.decorator.Sorter;

/** A class that assists a {@link javax.swing.table.TableModel}
 * by filtering and sorting the rows of data aggregated by that model from a {@link com.itgp.mvc.DSet}.
 * Extends {@link com.itgp.gwtviz.client.ui.runtime.decorator.FilterSet}.
 * <p>
 * @author David Pociu - InsiTech
 */
public class ModelV1FilterSet extends FilterSet {
    
    /** Constructs a new <code>TableFilterSet<code>.
     */
    public ModelV1FilterSet() {
        super();
    }
    /** Constructs a new <code>TableFilterSet</code> with a given array of {@link Filter}s.
     * @param filters the array of <code>Filter</code>s
     */    
    public ModelV1FilterSet(Filter[] filters){
        super();
        setFilters(filters);
    }
    /** Constructs a new <code>TableFilterSet</code> with a given {@link Sorter}.
     * @param sorter the <code>Sorter</code>
     */    
    public ModelV1FilterSet(Sorter sorter){
        super();
        setSorter(sorter);
    }    
    /** Constructs a new <code>TableFilterSet</code> with a given array of {@link Filter}s and a given {@link Sorter}.
     * @param filters the array of <code>Filter</code>s
     * @param sorter the <code>Sorter</code>
     */   
    public ModelV1FilterSet(Filter[] filters, Sorter sorter){
        super();
        setSorter(sorter);
        setFilters(filters);
    }    
    
//----------------------------------- start abstract methods -------------------------------
    /** Returns the row count for the model handled by this <code>TableFilterSet</code>.
     * The row count is the number of rows before any filters are applied.
     * <p>
     * Implements the abstract method {@link FilterSet#getModelSize()}.  
     * @return an (<code>int</code>) row count
     */    
    public int getModelSize(){
    	
    	DataModelV1 model =  getModelV1();
    	if ( model == null){
    		return 0;
    	}
        Object[][] data = model.getData();
        if ( data == null ){
            return 0;
        } else {
            return data.length; // number of rows
        }
    }
//----------------------------------- end abstract methods ---------------------------------
    
//-------------------------------- start overwrite FilterSet methods ------------------------
    /** Sets the model filtered and sorted by this <code>TableFilterSet</code>.  
     * Throws an <code>IllegalArgumentException</code> if the model is not a
     * <code>DataModelV1</code>.
     * <p>
     * Overrides the method {@link FilterSet#setModel(Object model)}.  
     * <p>
     * Note: It is recommended that this method be called by the model 
     * to which this <code>FilterSet</code> is added to during the
     * <code>setFilterSet</code> method in that model.
     * @param model a model
     */      
    public void setModel(Object model){
        if ( model == null || (model instanceof DataModelV1)){
            super.setModel(model);
        } else {
            throw new IllegalArgumentException();
        }
    }
//-------------------------------- end overwrite FilterSet methods ------------------------
    
//-------------------------------- start local methods ------------------------------------
    
    /** Returns the <code>DataModelV1</code> filtered and sorted by this <code>FilterSet</code>, or <code>null</code> if none.   
     * @return a model or <code>null</code>
     */    
    public DataModelV1 getModelV1(){
        return (DataModelV1) getModel();
    }
//-------------------------------- end local methods ------------------------------------
    
    
}
