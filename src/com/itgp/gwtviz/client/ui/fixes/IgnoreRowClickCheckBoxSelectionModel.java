/* 
 *
 * Copyright 2015 InsiTech LLC.   gwtviz@insitechinc.com
 *
 */
package com.itgp.gwtviz.client.ui.fixes;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;

import java.util.Collections;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowMouseDownEvent;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;



/**
 * This class extends GXT's CheckBoxSelectionModel<M> class and modifies two
 * behaviors from the parent class:
 *
 * 1.  It ignores row clicks with the mouse.  This forces the user to click the
 * actual checkbox in order to select the row.  This was done because it creates
 * sort of a bad behavior by automatically selecting the checkbox when the user
 * clicks anywhere on in the row.
 *
 * 2.  For mouse-down events on the row, it ignores the click unless the click
 * happens to be in the actual checkbox cell for that row.  I.e., it does not
 * propagate the event if the mouse-down event happens outside the checkbox
 * cell.  This ultimately provides the same functionality as #1 above.
 *
 * @param <M> the model object (Java POJO that adhears to the Java Bean
 * contract) represented by a row.
 */
public class IgnoreRowClickCheckBoxSelectionModel<M> extends CheckBoxSelectionModel<M> {
	  private int indexOnSelectNoShift;

	  /**
	   * True to deselect a selected item on click (defaults to true).
	   */
	  protected boolean deselectOnSimpleClick = true;

	  private boolean focusCellCalled;

  public IgnoreRowClickCheckBoxSelectionModel(IdentityValueProvider<M> identity) {
    super(identity);
  }


  @Override
  protected void onRowClick(RowClickEvent event) { }


  @Override
  protected void onRowMouseDown(RowMouseDownEvent event) {


    boolean left = event.getEvent().getButton() == NativeEvent.BUTTON_LEFT;
    Element target = event.getEvent().getEventTarget().cast();
    String s = target.getClassName();

    if (left && event.getColumnIndex()== 0) {
      M model = listStore.get(event.getRowIndex());
      if (model != null) {
        if (isSelected(model)) {
          deselect(model);
        }
        else {
          select(model, true);
        }
      }
    }else{
    	  onRowMouseDown1(event);
    }
  }
  
  protected void setChecked(boolean checked) {
	    if (isShowSelectAll() && grid.isViewReady()) {
	      int idx = grid.getColumnModel().getColumns().indexOf(getColumn());
	      if (idx == -1) {
	        return;
	      }
	      //If it is hidden, what difference does it make?
	      if(getColumn().isHidden()){
	    	  return;
	      }
	      XElement hd = grid.getView().getHeader().getHead(idx).getElement();
	      if (hd != null) {
	    	  getAppearance().onHeaderChecked(hd.getParentElement().<XElement> cast(), checked);
	      }
	    }
	  }
  
  
  protected void onRowMouseDown1(RowMouseDownEvent event) {
	    if (Element.is(event.getEvent().getEventTarget())
	        && !grid.getView().isSelectableTarget(Element.as(event.getEvent().getEventTarget()))) {
	      return;
	    }
	    
	    if (isLocked()) {
	      return;
	    }
	    
	    int rowIndex = event.getRowIndex();
	    int colIndex = event.getColumnIndex();
	    if (rowIndex == -1) {
	      return;
	    }

	    focusCellCalled = false;
	    mouseDown = true;

	    XEvent e = event.getEvent().<XEvent> cast();

	    // it is important the focusCell be called once, and only once in onRowMouseDown and onRowMouseClick
	    // everything but multi select with the control key pressed is handled in mouse down

	    if (event.getEvent().getButton() == Event.BUTTON_RIGHT) {
	      if (selectionMode != SelectionMode.SINGLE && isSelected(listStore.get(rowIndex))) {
	        return;
	      }
	      grid.getView().focusCell(rowIndex, colIndex, false);
	      select(rowIndex, false);
	      focusCellCalled = true;
	    } else {
	      M sel = listStore.get(rowIndex);
	      if (sel == null) {
	        return;
	      }
	      
	      boolean isSelected = isSelected(sel);
	      boolean isMeta = e.getCtrlOrMetaKey();
	      boolean isShift = event.getEvent().getShiftKey();

	      switch (selectionMode) {
	        case SIMPLE:
	          grid.getView().focusCell(rowIndex, colIndex, false);
	          focusCellCalled = true;
	          if (!isSelected) {
	            select(sel, true);
	          } else if (isSelected && deselectOnSimpleClick) {
	            deselect(sel);
	          }
	          break;
	          
	        case SINGLE:
	          grid.getView().focusCell(rowIndex, colIndex, false);
	          focusCellCalled = true;
	          if (isSelected && isMeta) {
	            deselect(sel);
	          } else if (!isSelected) {
	            select(sel, false);
	          }
	          break;
	          
	        case MULTI:
	          if (isMeta) {
	            break;
	          }
	          
	          if (isShift && lastSelected != null) {
	            int last = listStore.indexOf(lastSelected);
	            grid.getView().focusCell(last, colIndex, false);
	            
	            int start;
	            int end;
	            // This deals with flipping directions
	            if (indexOnSelectNoShift < rowIndex) {
	              start = indexOnSelectNoShift;
	              end = rowIndex;
	            } else {
	              start = rowIndex;
	              end = indexOnSelectNoShift;
	            }
	            
	            focusCellCalled = true;
	            select(start, end, false);
	          } else if (!isSelected) {
	            // reset the starting location of multi select
	            indexOnSelectNoShift = rowIndex;
	            
	            grid.getView().focusCell(rowIndex, colIndex, false);
	            focusCellCalled = true;
	            if(colIndex==0){
	            	doSelect(Collections.singletonList(sel), false, false);
	            }
	          } 
	          break;
	      }
	    }

	    mouseDown = false;
	  }
}