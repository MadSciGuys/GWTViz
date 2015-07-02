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

import java.util.Arrays;

/**
 * Abstract class extending {@link FilterSetItem}.  Base class of sorters of complex data models.
 * The developer implements the {@link #compare} method to suit the specific
 * data model to which this sorter is applied.
 * <p>
 * Internally, this class uses <CODE>Array.sort(Object[] array)</CODE>. This sort is
 * guaranteed to be stable: equal elements will not be reordered as a result of the sort.
 * @author InsiTech Inc.
 * @author David Pociu
 * @since 5.4
 */
public abstract class Sorter implements FilterSetItem {
    
    /** The value of the {@link #sortDirection} property that signifies sorting in a descending direction.
     */
    public static final int DESCENDING = -1;
    
    /** The value of the {@link #sortDirection} property that signifies no sorting.
     */
    public static final int NOT_SORTED = 0;
    
    /** The value of the {@link #sortDirection} property that signifies sorting in an ascending sort.
     */
    public static final int ASCENDING = 1;
    
    //--------------------------------------------------------------------------
    // everything is protected so it is available to classes that may extend Sorter
    /** An array of <code>int</code>s that maps sorted indexes to pre-sorted indexes for this <code>Sorter</code>.
     */
    protected Row[] sortedToPreSorted = null ;
    
    /** An array of <code>int</code>s that maps pre-sorted indexes to sorted indexes for this <code>Sorter</code>.
     */
    protected int[] preSortedToSorted = null ;
    
    /** The {@link FilterSet} that contains this <code>Sorter</code>.
     * (A given {@link FilterSetItem} can be added to only one <code>FilterSet</code>.)
     */
    protected FilterSet filterSet;
    
    /** Signifies the order of sorting for this <code>Sorter</code>.
     * Defaults to {@link #NOT_SORTED}
     * @see #getSortDirection()
     * @see #setSortDirection(int)
     * @see #DESCENDING
     * @see #ASCENDING
     */
    protected int sortDirection = NOT_SORTED;
    
    /** Constructs a new <code>Sorter</code>.
     */
    public Sorter() {
    }
//--------------------------- start abstract methods -----------------------------------
    /** Returns a negative integer if the object at <code>modelRow1</code> is less than the object at <code>modelRow2</code>,
     * zero if the object at modelRow1 is equal to the object at <code>modelRow2</code>,
     * or a positive integer if the object at <code>modelRow1</code> is greater than the object at <code>modelRow2</code>.
     * <p>
     * Implemented by the developer, who decides what "less than", "equal to", and "greater than" mean.
     * The system of coordinates is that of the component model (without any filter interference).
     * @param modelRow1 the model index for the first row
     * @param modelRow2 the model index for the second row
     * @return an <code>int</code>
     */
    public abstract int compare( int modelRow1, int modelRow2);
    
//--------------------------- end abstract methods ---------------------------------------
    
    /** Resets this <code>Sorter</code>.
     */
    public void reset(){
        sortedToPreSorted = null ;
        preSortedToSorted = null ;
    }
    
    /** Re-sorts all rows in the model
     */
    public void refresh(){
        FilterSet filterSet = getFilterSet();
        if ( filterSet == null )
            return;
        
        filterSet.refreshStarted(); // inform the filterset that a new refresh started (up to the filterset to percolate this up to it's listeners)
        
        Filter last = filterSet.last();
        
        int size = (last == null? filterSet.getModelSize() : last.getOutputSize());
        
        preSortedToSorted = new int[size];
        
        sortedToPreSorted = new Row[size];
        
        for( int i=0; i<size; i++){
            sortedToPreSorted[i] = new Row(i);
        }
        Arrays.sort(sortedToPreSorted); // stable sort
        
        for (int i=0; i<size; i++){
            preSortedToSorted[ toPreSortIndex(i)] = i;
        }
        
    }
    
    /** Returns the sorted index corresponding to a given pre-sorted index.<p>
     * Returns the pre-sorted index itself under the following circumstances:
     * <UL>
     *  <LI> preSortIndex is out of bounds
     *  <LI> sorting has not yet been initialized
     *</UL>
     * @param preSortIndex a pre-sorted index.
     * @return an <code>int</code>
     */
    public int toSortedIndex(int preSortIndex){
        if ( preSortIndex < 0 || preSortedToSorted == null || preSortIndex >= preSortedToSorted.length){
            return preSortIndex;
        } else {
            return preSortedToSorted[preSortIndex];
        }
    }
    
    /**
     * Returns the pre-sorted index corresponding to a given sorted index.
     * Returns the sorted index itself under the following circumstances:
     * <UL>
     *  <LI> sortedIndex is out of bounds
     *  <LI> sorting has not yet been initialized
     *</UL>
     * @param sortedIndex a sorted index.
     * @return an <code>int</code>
     */
    public int toPreSortIndex(int sortedIndex){
        if ( sortedIndex < 0 || sortedToPreSorted == null || sortedIndex >= sortedToPreSorted.length){
            return sortedIndex;
        } else {
            return sortedToPreSorted[sortedIndex].filterIndex;
        }
    }
    
    /**  Inner class implementing <code>Comparable</code>.
     */
    protected class Row implements Comparable {
        
        /** The index of this <code>Row</code>.
         */
        protected int filterIndex;
        
        /** Constructs a new <code>Row</code> with a given index.
         * @param index the index
         */
        public Row(int index) {
            this.filterIndex = index;
        }
        
        /** Compares the index of this <code>Row</code> to the index of another.
         * Takes into account the sort direction of the outer <code>Sorter</code>.
         * @param otherRow the other row
         * @return an <code>int</code>
         */
        public int compareTo(Object otherRow) {
            int modelIndex1 = filterIndex;
            int modelIndex2 = ((Row) otherRow).filterIndex;
            Filter last = getFilterSet().last();
            if ( last != null){
                modelIndex1 = last.toModelIndex(modelIndex1); // convert to original model coordinates
                modelIndex2 = last.toModelIndex(modelIndex2); // convert to original model coordinates
            }
            int comparison = 0;
            if ( modelIndex1 < 0 && modelIndex2 < 0){
                comparison = 0; // non-existent indices are completely equal (since we know nothing about them they are equally unknown ;) )
            } else if ( modelIndex1 < 0){
                comparison = -1; // non-existent index is less than existent one
            } else if ( modelIndex2 < 0 ){
                comparison = 1; // existent index is greater than non-existent one
            } else {
                comparison = compare( modelIndex1, modelIndex2); // go to the outer class implementation of compare and use it in the sort
            }
            
            if ( comparison != 0 && getSortDirection() == Sorter.DESCENDING){
                comparison =  0-comparison; // reverse
            }
            return comparison;
        }
    }
    
    
//-------------------------- start get/set pairs -------------------------
    
    /** Returns the {@link FilterSet} that contains this <code>Filter</code>.
     * @return a <code>FilterSet</code> or <code>null</code> if none
     */
    public FilterSet getFilterSet() {
        return filterSet;
    }
    /** Sets the {@link FilterSet} that contains this <code>Filter</code>.
     * Automatically invoked when the filter is added to the FilterSet.
     * @param filterSet a <code>FilterSet</code> or <code>null</code> if none
     */
    public void setFilterSet(FilterSet filterSet) {
        this.filterSet = filterSet;
    }
    
    
    //--------------------------------------------------------------------------
    
    /** Returns the value of the property that signifies the order of sorting for this <code>Sorter</code>.
     * Defaults to {@link #NOT_SORTED}
     * @return an <code>int</code>
     * @see #DESCENDING
     * @see #ASCENDING
     */
    public int getSortDirection() {
        return sortDirection;
    }
    /** Sets the value of the property that signifies the order of sorting for this <code>Sorter</code>.
     * Defaults to {@link #NOT_SORTED}
     * @param sortDirection an <code>int</code>
     * @see #DESCENDING
     * @see #ASCENDING
     */
    public void setSortDirection(int sortDirection) {
        this.sortDirection = sortDirection;
    }
    
//-------------------------- end get/set pairs -------------------------
    
}
