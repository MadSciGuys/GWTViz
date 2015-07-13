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
package com.itgp.gwtviz.client.ui.runtime.decorator;

/**
 * Abstract class extending {@link FilterSetItem}. Base class of filters of complex data models.
 * The developer implements the {@link #accept} method to suit the specific
 * data model to which this filter is applied.
 * @author InsiTech Inc.
 * @author David Pociu
 * @since 5.4
 */
public abstract class Filter implements FilterSetItem{
	public static final int[] EMPTY_INT_ARRAY = new int[0];  
    
    /** The {@link FilterSet} that contains this <code>Filter</code>.  
     * (A given {@link FilterSetItem} can be added to only one <code>FilterSet</code>.)
     */
    protected FilterSet filterSet;
    
    /** An array of <code>int</code>s that cross references input indexes to output indexes for this <code>Filter</code>.  
     */
    protected int[] inputToOutputXref = EMPTY_INT_ARRAY;
    
    /** An array of <code>int</code>s that cross references output indexes to input indexes for this <code>Filter</code>.  
     */
    protected int[] outputToModelXref = EMPTY_INT_ARRAY;
    
    /** Used by the {@link FilterSet} that contains this <code>Filter</code> for synchronization purposes.  Do not use.
     */
    protected int   order             = -1;	// no access methods for speed reasons
    
//------------------------ start abstract methods ----------------------------------    
    /** Returns whether a particular row is accepted by this <code>Filter</code> or not.
     * <p>
     * Implemented by the developer.
     * @param modelIndex the row position using the model's (DSet) coordinate system.
     * @return <CODE>true</CODE> if this row is accepted as part of the filtered rows, 
     * <CODE>false</CODE> if this row should not be part of the data displayed by the filter
     */
    public abstract boolean accept(int modelIndex);
//------------------------ end abstract methods ----------------------------------   
    
    /** Resets this <code>Filter</code>. 
     */
    public void reset(){
        inputToOutputXref = EMPTY_INT_ARRAY;
        outputToModelXref = EMPTY_INT_ARRAY; 
    }
    
    /** Returns the output index corresponding to a given input index for this <code>Filter</code>.
     * If the input index is out of range or this <code>Filter</code> has not been refreshed, 
     * then the input index itself is returned.
     * @param inputIndex an input index
     * @return an index
     */    
    public int toOutputIndex( int inputIndex)  {
        if ( inputIndex < 0 || inputToOutputXref == null || inputIndex >= inputToOutputXref.length ){
            return inputIndex;
        } else {
            return inputToOutputXref[inputIndex];
        }
    }

    /** Returns the model index corresponding to a given output index for this <code>Filter</code>.
     * If the output index is out of range or this <code>Filter</code> has not been refreshed, 
     * then the output index itself is returned.
     * @param outputIndex an output
     * @return the corresponding model index
     */
    public int toModelIndex( int outputIndex)  {
        if ( outputIndex < 0 || outputToModelXref == null || outputIndex >= outputToModelXref.length){
            return outputIndex;
        } else {
            return outputToModelXref[outputIndex];
        }
    }
    
    
    
    //--------------------------------------------------------------------------
    /** Returns the size of {@link #outputToInputXref}, 
     * i.e. the number of input indexes that were accepted by this <code>Filter</code> on the last call to {@link #refresh}.
     * @return the size of the output
     */
    public int getOutputSize(){
        return outputToModelXref.length;
    }
    /** Returns the number of input indexes for this <code>Filter</code>.
     * @return the size of the input
     */
    public int getInputSize(){
        return (getFilterSet() == null ? 0 : getFilterSet().getInputSize(this));
    }
    
    //--------------------------------------------------------------------------
    /** Returns the {@link FilterSet} that contains this <code>Filter</code>.  
     * @return a <code>FilterSet</code> or <code>null</code> if none
     */
    public FilterSet getFilterSet() {
        return filterSet;
    }
    /** Sets the {@link FilterSet} that contains this <code>Filter</code>.  
     * @param filterSet a <code>FilterSet</code> or <code>null</code> if none 
     */
    public void setFilterSet(FilterSet filterSet) {
        this.filterSet = filterSet;
    }
    
    //--------------------------------------------------------------------------
    /** Perform filtering for all input indexes.
     * <p>
     * Same as calling {@link #refresh(int startInputIndex)} 
     * with a <code>startInputIndex</code> of <code>0</code>.
     */
    public void refresh(){
        refresh(0); // refresh starting at 0
    }
    
    /**
     * Perform filtering starting at a given input index. This method
     * is used in situations where the model behind the {@link FilterSet} is continuously updated
     * (streaming for example) and filtering should only be applied to the newly added rows.
     * <p>
     * <b>Important Note:</b> Never call this method if the input rows might have shifted for any reason - this method
     * will work only if the existing rows stayed intact and new rows were appended since last time a refresh was called.
     * In all other circumstances please call the regular {@link #refresh()} method.
     * @param startInputIndex the input index at which to start filtering
     */
    public void refresh(int startInputIndex){
        //This implementation is optimized for performance
        
        
        if (filterSet == null){
            if (inputToOutputXref.length > 0){
                inputToOutputXref = EMPTY_INT_ARRAY;
            }
            if (outputToModelXref.length > 0){
                outputToModelXref = EMPTY_INT_ARRAY;
            }
        } else {
            
            if (filterSet.contains(this)) {
                filterSet.refreshStarted(); // inform the filterset that a new refresh started (up to the filterset to percolate this up to its listeners)
            }
            
            if ( startInputIndex < 0 )
                startInputIndex = 0;
            
            int origInputSize = inputToOutputXref.length;
            int inputSize = getInputSize();
            
            if ( startInputIndex > 0){
                startInputIndex = Math.min( origInputSize, startInputIndex); // starting index cannot be greater that the last position we ever filtered.
                if ( startInputIndex > 0)
                    startInputIndex = Math.min( startInputIndex, inputSize); // cannot start past the end of where the current input ends
            }
            
            int outputSize = 0; // start by eliminating everything in the old output
            // see if there's any output worth saving because we'return only filtering a partial number of rows.
            if ( startInputIndex > 0){
                // cut down the outputToInputXref array down to the size corresponding to the last row we won't run through a filter
                // find the last output index and cut down the output to size
                int lastOutputIndex = toOutputIndex(startInputIndex-1);
                if (lastOutputIndex >=0){
                    // looks like we're keeping at least one output row.
                    outputSize = lastOutputIndex +1;
                }
            }
            
            
            int difference = inputSize - startInputIndex;
            
            int[] tempInputToOutputXref = new int[ inputSize];
            if ( startInputIndex > 0){
                System.arraycopy(inputToOutputXref, 0, tempInputToOutputXref, 0, startInputIndex);
            }
            inputToOutputXref = tempInputToOutputXref;
            
            
            int[] diffOutputToInputXref = new int[difference];
            int counter = 0;
            for ( int i=startInputIndex; i< inputSize; i++){
                int modelIndex = i; //assume it is the last filter to input index= model index
                Filter filterPrev = filterSet.previous(this);
                if (filterPrev != null){
                    modelIndex = filterPrev.toModelIndex(modelIndex); // get the model index from the previous filter
                } 
                if (accept(modelIndex)){
                    inputToOutputXref[i] = counter;
                    diffOutputToInputXref[counter] = modelIndex;
                    counter++;
                } else {
                    inputToOutputXref[i] = -1;
                }
            } // for
            
            
            int[] tempOutputToInputXref = new int[ outputSize + counter];
            if ( outputSize > 0){
                // copy old indices first
                System.arraycopy( outputToModelXref, 0, tempOutputToInputXref, 0 , outputSize);
            }
            if ( counter > 0){
                // copy any additional indices in
                System.arraycopy(diffOutputToInputXref, 0 , tempOutputToInputXref, outputSize, counter);
            }
            outputToModelXref = tempOutputToInputXref;
            fireFilterChanged();
        } // filterSet != null
    } // refresh
    
    /** Notifies the {@link FilterSet} that contains this <code>Filter</code>
     * that the current filter state has changed.  Automatically fired by 
     * the default implementation of {@link #refresh(int startInputIndex).
     */
    public void fireFilterChanged(){
        filterSet.filterChanged(this);
    }
}
