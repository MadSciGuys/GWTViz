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

import java.util.*;


/** An abstract class that assists a model object (typically a {@link com.itgp.mvc.DModelComplex})
 * by filtering and sorting the rows of data aggregated by that model from a {@link com.itgp.mvc.DSet}.
 * <p>
 * Contains an array of {@link Filter}s and one {@link Sorter}, and most of the implementation 
 * needed to manage these items.
 * <p> 
 * Extending classes implement {@link #getModelSize()}, and possibly provide other methods 
 * that are more component specific (e.g., methods dealing with data in row and column formation,
 * for a table-specific implementation).
 * <p>
 * <img src="doc-files/FilterSet-usage.gif">
 * <p>
 * As depicted above, in a typical scenario a {@link com.itgp.mvc.DViewComplex} 
 * uses a <code>DModelComplex</code> to aggregate data from within a <code>DSet</code>. 
 * (The <code>DPanel</code> acts as the controller between the view and the model.) 
 * A <code>DModelComplex</code> in turn uses a <code>FilterSet</code>
 * to filter and sort the aggregated data.  The <code>FilterSet</code> contains an array of 
 * an array of <code>Filter</code>s and one <code>Sorter</code> to accomplish this.
 * <p>
 * <img src="doc-files/FilterSet-impl.gif">
 * <p>
 * Classes implementing this interface include {@link com.itgp.mvc.decorator.TableFilterSet}. 
 * @author David Pociu - InsiTech Inc.
 * @since 5.4
 */
public abstract class FilterSet {
    public static final Filter[] EMPTY_FILTER_ARRAY  = new Filter[]{}; //immutable empty filter array
    
    /** The value of the {@link #state} property that signifies a pre-change cascade state.
     */
    public static final byte     STATE_PRE_CHANGE_CASCADE           = 0;
    
    /** The value of the {@link #state} property that signifies a change-cycle-in-progress state.
     */
    public static final byte     STATE_CHANGE_CYCLE_IN_PROGRESS     = 1;
    
    /** The value of the {@link #state} property that signifies a post-change cascade state.
     */
    public static final byte     STATE_POST_CHANGE_CASCADE          = 2;
    
    //--------------------------------------------------------------------------
    /** The array of {@link Filter}s in this <code>FilterSet</code>.
     */
    protected Filter[]                filters             = EMPTY_FILTER_ARRAY;
    
    /** The {@link Sorter} in this <code>FilterSet</code>.
     */
    protected Sorter                  sorter              = null;
    /** The data model filtered and sorted by this <code>FilterSet</code>.
     */
    protected transient Object        model               = null;
    /** The <code>listenerList</code> containing the listeners
     * that are notified when the state of this <code>FilterSet</code> changes.
     */
    protected transient Set            listenerList        = new LinkedHashSet();
    
    /** Signifies the state of this <code>FilterSet</code>.
     * Defaults to {@link #STATE_PRE_CHANGE_CASCADE}
     * @see #getState()
     * @see #STATE_CHANGE_CYCLE_IN_PROGRESS
     * @see #STATE_POST_CHANGE_CASCADE
     */
    protected byte                    state               = STATE_PRE_CHANGE_CASCADE;
    
    //--------------------------------------------------------------------------
    /** Constructs a new <code>FilterSet</code>.
     */
    public FilterSet() {
    }
    
    
//----------------------------------- start abstract methods -------------------------------
    /** Returns the row count for the data model handled by this <code>FilterSet</code>.
     * The row count is the number of rows before any filters are applied.
     * @return an (<code>int</code>) row count
     */
    public abstract int getModelSize();
//----------------------------------- end abstract methods ---------------------------------
    
    /** Resets all {@link Filter}s and the {@link Sorter} in this <code>FilterSet</code>.
     */
    public void reset(){
        for( int i=0; i< filters.length; i++){
            if (filters[i] != null)
                filters[i].reset();
        }
        if ( sorter != null){
            sorter.reset();
        }
    }
    
    //--------------------------------------------------------------------------
    
    /** Returns the original position in the model
     * corresponding to a given position in the view created (via filtering and sorting) by this <code>FilterSet</code>.
     * @param viewIndex the view position
     */
    public int convertIndexToModel( int viewIndex){
        if ( viewIndex < 0){
            return -1;
        }
        int index = viewIndex;
        if ( getSorter() != null){
            index = getSorter().toPreSortIndex(index);
        }
        
        Filter last = last();
        return ( last == null ? index : last.toModelIndex(index)); 
    }
    
    //--------------------------------------------------------------------------
    /** Returns the position in the view created (via filtering and sorting) by this <code>FilterSet</code>
     * corresponding to a given <b>input</b> index in a given {@link Filter} in this <code>FilterSet</code>.
     * <p>
     * If <code>filter</code> is not in this <code>FilterSet</code>, or if it is <code>null</code>,
     * then the value of <code>filterInputIndex</code> itself is returned.
     * @param filter the <code>Filter</code>
     * @param filterInputIndex the input index in the <code>Filter</code>
     */
    public int convertIndexToView( Filter filter, int filterInputIndex) {
        int index = filterInputIndex;
        
        if ( contains(filter)){
            while( index >= 0 && filter != null){
                index = filter.toOutputIndex(index);
                filter = next(filter);
            }
        }
        
        // now do the sorter transformation if needed
        if ( index >=0 && getSorter() != null){
            index = getSorter().toSortedIndex(index);
        }
        
        return index;
    }
    
    /** Returns the position in the view created (via filtering and sorting) by this <code>FilterSet</code>
     * corresponding to a given  position in the model.
     * @param filter the <code>Filter</code>
     * @param filterInputIndex the input index in the <code>Filter</code>
     */
    public int convertIndexToView( int modelIndex) {
        Filter first = first();
        return convertIndexToView(first, modelIndex);
    }
    //--------------------------------------------------------------------------
    /** Returns the first {@link Filter} (i.e. the one closest to the actual model) in this <code>FilterSet</code>.
     * If no <code>Filter</code>s exist in this <code>FilterSet</code>, returns <CODE>null</CODE>.
     * @return a <code>Filter</code> or <CODE>null</CODE>
     */
    public Filter first(){
        return  (filters.length >0 ? filters[0] : null);
    }
    
    /** Returns the last {@link Filter} (i.e. the one closest to the view) in this <code>FilterSet</code>.
     * If no <code>Filter</code>s exist in this <code>FilterSet</code>, returns <CODE>null</CODE>.
     * @return a <code>Filter</code> or <CODE>null</CODE>
     */
    public Filter last() {
        return  ((filters.length > 0) ? filters[filters.length - 1] : null);
        
    }
    
    /** Returns the next {@link Filter} after a given <code>Filter</code> in this <code>FilterSet</code>.
     * If the given <code>Filter</code> does not exist in this <code>FilterSet</code>,
     * or if no <code>Filter</code> follows it, returns <CODE>null</CODE>.
     * @param filter a <code>Filter</code>
     * @return a <code>Filter</code> of <code>null</code>
     */
    public Filter next(Filter filter) {
        return (contains(filter) ? ( last() == filter ? null : filters[filter.order + 1]
                ) : null);
    }
    
    /** Returns the previous {@link Filter} before a given <code>Filter</code> in this <code>FilterSet</code>.
     * If the given <code>Filter</code> does not exist in this <code>FilterSet</code>,
     * or if no <code>Filter</code> precedes it, returns <CODE>null</CODE>.
     * @param filter a <code>Filter</code>
     * @return a <code>Filter</code> of <code>null</code>
     */
    public Filter previous(Filter filter) {
        return (contains(filter) ? ( first() == filter ? null : filters[filter.order - 1]
                ) : null);
    }
    //--------------------------------------------------------------------------
    //Very fast method to ensure that thie FilterSet contains the filter passed in as a parameter.
    /** Returns <code>true</code> if a given {@link Filter} exists in this <code>FilterSet</code>,
     * <code>false</code> otherwise.
     * @param filter a <code>Filter</code>
     * @return a <code>boolean</code>
     */
    public boolean contains(Filter filter) {
        return ((filter != null) && (filters.length > 0) && filter.order >= 0) &&  (filters[filter.order] == filter);
    }
    
    /** Returns the count of rows in the model that are input for a given {@link Filter} in this <code>FilterSet</code>.
     * <p>
     * (Same as the output size of the previous <code>Filter</code> in this <code>FilterSet</code>
     * if a previous <code>Filter</code> exists.  Otherwise, returns the size of the model.)
     * <p>
     * Returns zero the given <code>Filter</code> does not exist in this <code>FilterSet</code>.
     * @param filter a <code>Filter</code>
     * @return the input size
     */
    public int getInputSize( Filter filter){
        int value = 0;
        if ( contains(filter)){
            Filter previous = previous(filter);
            value = (previous == null? getModelSize() : previous.getOutputSize());
            value = (value < 0 ? 0 : value) ;
        }
        return value;
    }
    
    
    /** Returns the count of rows in the model that are output for this <code>FilterSet</code>.
     * <p>
     * (Same as the size of the model if no {@link Filter}s exist in this <code>FilterSet</code>.)
     * @param filter a <code>Filter</code>
     * @return the input size
     */
    public int getOutputSize(){
        Filter last = last();
        return (last == null ?  getModelSize() : last.getOutputSize());
    }
    
    //--------------------------------------------------------------------------
    /** Refreshes all {@link Filter}s and the {@link Sorter} in this FilterSet instance.
     * <p>
     * Same as calling {@link #refresh(int startInputIndex)}
     * with a <code>startInputIndex</code> of <code>0</code>.
     */
    public void refresh(){
        refresh(0);
    }
    
    /**
     * Refresh all {@link Filter}s and the {@link Sorter} in this <code>FilterSet</code>,
     * but start the refresh of the first <code>Filter</code> at the position given by
     * <code>startInputIndex</code>.
     * <p>
     * If no <code>Filter</code>s exist in this <code>FilterSet</code>,
     * then the <code>startInputIndex</code> parameter is ignored
     * (<code>Sorter</code>s always sort all input rows).
     */
    public void refresh(int startInputIndex){
        if (getModel() == null){
            return;
        }
        startInputIndex = (startInputIndex <0 ? 0 : startInputIndex); // must start at 0
        Filter first = first();
        if (first != null){
            first.refresh(startInputIndex);
        } else if (sorter != null) {
            filterChanged(null); // fire events
        }
    }
    
//------------------------------ start listener/event code --------------------------------
    
    /** Adds a given <code>ChangeListener</code> to the list of listeners
     * notified when this <code>FilterSet</code> changes.
     * @param listener the <code>ChangeListener</code>
     */
    public void addChangeListener(FilterSetListener listener) {
        if ( listener != null){
            listenerList.add(listener);
        }
    }
    
    /** Removes a given <code>ChangeListener</code> from the list of listeners
     * notified when this <code>FilterSet</code> changes.
     * @param listener the <code>ChangeListener</code>
     */
    public void removeChangeListener(FilterSetListener listener) {
        if (listener !=null){
            listenerList.remove(listener);
        }
    }
    
    /**
     * Returns an array of all the <CODE>FilterSet</CODE> instance listeners
     * registered on this <code>FilterPipeline</code>.
     *
     * @return all of this <CODE>FilterSet</CODE> instance's <code>ChangeListener</code>s,
     *         or an empty array if no <CODE>FilterSet</CODE> instance listeners
     *         are currently registered
     *
     * @see #addChangeListener
     * @see #removeChangeListener
     */
    public FilterSetListener[] getChangeListeners() {
    	FilterSetListener[] farray = (FilterSetListener[]) listenerList.toArray(new FilterSetListener[0]);
    	return farray;
//        return (FilterSetListener[]) Utility.convertArrayType(listenerList.toArray(), FilterSetListener.class);
    }
    
    /**
     * Notifies all registered listeners that the contents of this <CODE>FilterSet</CODE> instance has changed. The event instance
     * is lazily created.
     *
     * This method usually gets called in 2 cases: before any refreshes are started (to allow the listeners to take actions like saving the
     * existing selections) and
     */
    public void fireContentsChanged() {
        if (listenerList.size() > 0){
            GenericEvent evt = new GenericEvent(this, new Object[]{});
            Iterator iterator = listenerList.iterator();
            while( iterator.hasNext()){
                FilterSetListener listener = (FilterSetListener) iterator.next();
                listener.filterSetChanged(evt);
            }
        }
    }
    
    /** If the state of this <CODE>FilterSet</CODE> is {@link STATE_PRE_CHANGE_CASCADE},
     * sets the state to {@link STATE_CHANGE_CYCLE_IN_PROGRESS} and fires a contents-changed event.
     * Otherwise, has no effect.
     */
    public void refreshStarted(){
        if (state == STATE_PRE_CHANGE_CASCADE){
            fireContentsChanged(); // inform listeners that a filter sort cascade is about to begin
            state = STATE_CHANGE_CYCLE_IN_PROGRESS;
        }
    }
    
    /**
     * Called when the specified filter has changed.
     * Cascades <b><code>filterChanged</code></b> notifications to the next
     * filter in the <CODE>FilterSet</CODE> instance after the specified filter. If the specified filter
     * is the last filter in the <CODE>FilterSet</CODE> instance, this method broadcasts a
     * <b><code>filterChanged</code></b> notification to all
     * <b><code>ChangeListener</code></b> objects registered with this <CODE>FilterSet</CODE> instance.
     *
     * @param filter a filter in this <CODE>FilterSet</CODE> instance that has changed in any way
     */
    public void filterChanged(Filter filter) {
        Filter  next = contains(filter) ? next(filter) : null;
        if (next == null) {
            // done with filters - try the sorter and after that trigger change notification.
            if (sorter != null) {
                sorter.refresh();   // cascade to interposed sorter
            }
            state = STATE_POST_CHANGE_CASCADE; // this cascade change is done - notify listeners.
            fireContentsChanged();
            state = STATE_PRE_CHANGE_CASCADE; // done with this cascade, listeners notified - set back to original state
        } else {
            //next filter
            next.refresh(); // Cascade to next filter
        }
    }
    
//------------------------------ end listener/event code --------------------------------
    
//-------------------------- start get/set pairs -------------------------
    /** Returns this <code>FilterSet</code>'s possibly empty array of {@link Filter}s.
     * Does not return <code>null</code>.
     * <p>
     * The <code>Filter</code> at position 0 in the array is the <code>Filter</code> closest to the model (i.e. it is refreshed first).
     * @return an array of zero or more <code>Filter</code>s
     */
    public Filter[] getFilters() {
        return filters;
    }
    /** Sets this <code>FilterSet</code>'s array of {@link Filter}s.
     * Also sets the <code>filterSet</code> property of each given <code>Filter</code> to this <code>FilterSet</code>.
     * Finally, performs a {@link #refresh()}.
     * <p>
     * Setting to <code>null</code> has the same effect as setting to an empty array.
     * <p>
     * The <code>Filter</code> at position 0 in the array is the <code>Filter</code> closest to the model (i.e. it is refreshed first).
     * @param filters an array of zero or more <code>Filter</code>s
     */
    public void setFilters(Filter[] filters) {
        if ( filters == null){
            this.filters = EMPTY_FILTER_ARRAY;
        } else {
            this.filters = filters;
            int size = filters.length;
            for(int i=0; i<size; i++){
                Filter filter = filters[i];
                if ( filter!= null){
                    filter.setFilterSet(this); // assign filter set
                    filter.order = i;           // package private
                }
            }
        }
        refresh();
    }
    //--------------------------------------------------------------------------
    /** Returns this <code>FilterSet</code>'s {@link Sorter}, or <code>null</code> if none.
     * @return a <code>Sorter</code> or <code>null</code>
     */
    public Sorter getSorter() {
        return sorter;
    }
    /** Sets this <code>FilterSet</code>'s {@link Sorter}.
     * Also sets the given <code>Sorter</code>'s <code>filterSet</code> property to this <code>FilterSet</code>.
     * @param sorter a <code>Sorter</code> or <code>null</code>
     */
    public void setSorter(Sorter sorter) {
        this.sorter = sorter;
        if ( sorter != null){
            sorter.setFilterSet(this);
        }
    }
    
    /** Returns the data model filtered and sorted by this <code>FilterSet</code>, or <code>null</code> if none.
     * @return a model or <code>null</code>
     */
    public Object getModel() {
        return model;
    }
    
    /** Sets the model filtered and sorted by this <code>FilterSet</code>.
     * <p>
     * Note: It is recommended that this method be called by the model
     * to which this <code>FilterSet</code> is added to during the
     * <code>setFilterSet</code> method in that model.
     * @param model a model
     */
    public void setModel(Object model) {
        this.model = model;
        reset(); // model changed -> reset all filters/sort
        if ( model != null){
            refresh();
        }
    }
    //--------------------------------------------------------------------------
    /** Returns a value signifying the state of this <code>FilterSet</code>.
     * Returns a value signifting the state of this <code>FilterSet</code>.
     * Usually used by listeners to determine if this is the beginning or the end of a change cycle.
     * Values are {@link #STATE_PRE_CHANGE_CASCADE} or {@link #STATE_POST_CHANGE_CASCADE} for listeners' use.<p>
     * Could also be in {@link #STATE_CHANGE_CYCLE_IN_PROGRESS} state in the middle of a refresh cycle,
     * but it will never be in this state when listeners are notified.
     * @return a byte
     */
    public byte getState() {
        return state;
    }
    
//-------------------------- end get/set pairs -------------------------
    

}
