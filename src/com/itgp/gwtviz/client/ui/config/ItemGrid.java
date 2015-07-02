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
package com.itgp.gwtviz.client.ui.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.itgp.gwtviz.client.ui.MainDesktop;
import com.itgp.gwtviz.client.ui.fixes.IgnoreRowClickCheckBoxSelectionModel;
import com.itgp.gwtviz.shared.model.Item;
import com.itgp.gwtviz.shared.model.ItemProperties;
import com.itgp.gwtviz.shared.model.ItemUtil;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;

public class ItemGrid implements IsWidget, INotify {

	private static final ItemProperties props = GWT.create(ItemProperties.class);

	private ContentPanel panel;
	private ListStore<Item> store;
	private Grid<Item> grid;
	private List<Item> itemList;
	boolean enableDnD;
	SelectionMode selectionMode;
	DualFieldSelector dataSelector;
	Item currentlySelectedItem;
	public enum GridType {
		REGULAR, XAXIS_LABEL, HIDE_CHECKBOX
	};

	public enum Operation {
		SAVE
	};

	GridType gridType;

	public enum SortDirection {
		Ascending, Descending
	};

	public ItemGrid(List<Item> itemList, boolean enableDnD, SelectionMode selectionMode, DualFieldSelector dataSelector, GridType gridType) {
		this.itemList = itemList;
		this.enableDnD = enableDnD;
		this.selectionMode = selectionMode;
		this.dataSelector = dataSelector;
		this.gridType = gridType;
	}

	public void setData(List<Item> items) {
		if (store.size() > 0) {
			store.rejectChanges();
			store.clear();
		}
		if (items != null && items.size() > 0) {
			store.addAll(items);
			store.commitChanges();
			grid.getView().refresh(true);
			grid.getView().setForceFit(true);
			grid.getView().layout();
		}
	}

	public void addData(Item item) {
		// List<Item> newList = new ArrayList<Item>(store.getAll());
		// newList.add(item);
		// setData(newList);
		store.add(item);
	}

	public List<Item> getAllData() {
		grid.getStore().commitChanges();
		return grid.getStore().getAll();
	}

	public List<Item> getSelectedItems() {
		if (grid != null) {
			List<Item> items = grid.getSelectionModel().getSelectedItems();
			return items;
		}
		return null;
	}

	public void deselectAll() {
		if (grid != null) {
			grid.getSelectionModel().deselectAll();
		}
	}

	public Item getSelectedItem() {
		if (grid != null) {
			Item items = grid.getSelectionModel().getSelectedItem();
			return items;
		}
		return null;
	}

	public void selectedItem(String value) {
		if (grid != null) {
			List<Item> items = grid.getStore().getAll();
			for (Item i : items) {
				if (i.getValue().equals(value)) {
					grid.getSelectionModel().select(i, false);
				}
			}

		}
	}
	public void selectedItem(Item item) {
		if (grid != null) {
			List<Item> items = grid.getStore().getAll();
			for (Item i : items) {
				if (i.equals(item)) {
					grid.getSelectionModel().select(i, false);
				}
			}

		}
	}

	public void selectedItems() {
		grid.getSelectionModel().deselectAll();
		if (grid != null) {
			List<Item> items = grid.getStore().getAll();
			for (Item i : items) {
				if (i.isSelected()) {
					grid.getSelectionModel().select(i, true);
				}
			}

		}
	}

	public void removeItem(String value) {
		if (grid != null) {
			List<Item> items = grid.getStore().getAll();
			for (Item i : items) {
				if (i.getValue().equals(value)) {
					grid.getStore().remove(i);
					break;
				}
			}
		}
	}

	@Override
	public Widget asWidget() {
		if (panel == null) {

			IdentityValueProvider<Item> identity = new IdentityValueProvider<Item>();
			final IgnoreRowClickCheckBoxSelectionModel<Item> selectionModel = new IgnoreRowClickCheckBoxSelectionModel<Item>(identity) {
				@Override
				protected void onRefresh(RefreshEvent event) {
					// this code selects all rows when paging if the header checkbox is selected
					if (isSelectAllChecked()) {
						selectAll();
					}
					super.onRefresh(event);
				}

				@Override
				public void deselect(Item item) {
					item.setSelected(false);
					super.deselect(item);
				}

				@Override
				public void select(Item item, boolean keepExisting) {
					item.setSelected(true);
					super.select(item, keepExisting);
				}

			};
			selectionModel.setSelectionMode(selectionMode);

			String columnName = "Column";
			if (GridType.XAXIS_LABEL.equals(gridType)) {
				columnName = "X Axis Column";
			}

			ColumnConfig<Item, String> valueCol = new ColumnConfig<Item, String>(props.value(), 245, columnName);
			ColumnConfig<Item, String> labelCol = new ColumnConfig<Item, String>(props.valueLabel(), 195, "X Axis Label");
			ColumnConfig<Item, Integer> dataCol = new ColumnConfig<Item, Integer>(props.datacount(), 130, "Data Elements Selected");
			ColumnConfig<Item, String> sortOrderCol = new ColumnConfig<Item, String>(props.sortOrder(), 60, "Sort Order");
			ColumnConfig<Item, String> sortDirCol = new ColumnConfig<Item, String>(props.sortDirection(), 125, "Sort Direction");

			valueCol.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			labelCol.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			dataCol.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			sortOrderCol.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			selectionModel.getColumn().setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

			sortOrderCol.setHidden(true);

			sortOrderCol.setFixed(true);
			sortDirCol.setFixed(true);

			sortOrderCol.setResizable(false);
			sortDirCol.setResizable(false);

			ListStore<String> direction = new ListStore<String>(new ModelKeyProvider<String>() {
				@Override
				public String getKey(String item) {
					return item;
				}
			});
			direction.add(SortDirection.Ascending.name());
			direction.add(SortDirection.Descending.name());

			ComboBoxCell<String> sortCombo = new ComboBoxCell<String>(direction, new LabelProvider<String>() {
				@Override
				public String getLabel(String item) {
					return item;
				}
			});

			JSNIsetEditable(sortCombo, false);
			sortCombo.setTriggerAction(TriggerAction.ALL);
			sortCombo.setForceSelection(true);
			sortCombo.setWidth(110);
			sortCombo.setHeight(10);
			sortCombo.addSelectionHandler(new SelectionHandler<String>() {

				@Override
				public void onSelection(SelectionEvent<String> event) {
					String data = event.getSelectedItem();
					data = data + "";
					grid.getStore().commitChanges();

				}
			});

			sortDirCol.setCell(sortCombo);

			List<ColumnConfig<Item, ?>> columns = new ArrayList<ColumnConfig<Item, ?>>();

			if (GridType.REGULAR.equals(gridType)) {
				columns.add(selectionModel.getColumn());
				columns.add(valueCol);
				columns.add(dataCol);

			} else if (GridType.HIDE_CHECKBOX.equals(gridType)) {
				columns.add(selectionModel.getColumn());
				selectionModel.getColumn().setHidden(true); // DO NOT REMOVE THE selectionModel FROM COLUMNS. You'll GET NULL Pointer on refresh of GRID	
				columns.add(valueCol);
				columns.add(dataCol);
				
			} else if (GridType.XAXIS_LABEL.equals(gridType)) {
				columns.add(selectionModel.getColumn());
				columns.add(valueCol);
				columns.add(labelCol);
				columns.add(sortOrderCol);
				columns.add(sortDirCol);
			} else {
				columns.add(selectionModel.getColumn());
				columns.add(valueCol);
				columns.add(dataCol);
			}

			ColumnModel<Item> cm = new ColumnModel<Item>(columns);

			store = new ListStore<Item>(props.key());
			store.setAutoCommit(true);
			store.addAll(itemList);

			// ToolTipConfig config = new ToolTipConfig("Example Info",
			// "This examples includes resizable panel, reorderable columns and grid state. Text selection is allowed.");
			// config.setMaxWidth(225);
			// ToolButton info = new ToolButton(ToolButton.QUESTION);
			// info.setToolTipConfig(config);

			grid = new Grid<Item>(store, cm);
			grid.getElement().setMargins(1);
			grid.setSelectionModel(selectionModel);
			// grid.setAllowTextSelection(true);
			// grid.getView().setAutoExpandColumn(valueCol);
			if (GridType.XAXIS_LABEL.equals(gridType)) {
				grid.getView().setAutoExpandColumn(labelCol);

				final GridEditing<Item> editing = new GridInlineEditing<Item>(grid);
				editing.addEditor(labelCol, (new TextField()));
				editing.addEditor(sortOrderCol, new TextField());
				// editing.addCompleteEditHandler(getCompleteEditHandler(labelCol));
				// editing.addCompleteEditHandler(getCompleteEditHandler(sortDirCol));
				// editing.addCompleteEditHandler(getCompleteEditHandler(sortOrderCol));

			}
			grid.getView().setStripeRows(true);
			grid.getView().setColumnLines(true);
			grid.setBorders(false);
			grid.setColumnReordering(true);

			grid.addRowClickHandler(new RowClickHandler() {

				@Override
				public void onRowClick(RowClickEvent event) {
					if (dataSelector != null) {
						Item clickedItem = ((Item) event.getSource().getSelectionModel().getSelectedItem());
						if (clickedItem != null) {
							// List<Item> uniqueDataList = selecteItem.getData();// ItemUtil.getDataFromColumn(MRoute.jsonData, selecteItem.getId(), true);
							if (clickedItem.isSelected()) {
								// clickedItem.setSelected(true);
								Item originalItem = ItemUtil.findItem(MainDesktop.getCurrentDataModel(), clickedItem.getValue());
								Item uniqueDataItem = ItemUtil.elimateDuplicateDataForItem(originalItem);
								currentlySelectedItem = clickedItem;
								dataSelector.setData(uniqueDataItem.getData(), clickedItem.getData());
								grid.getStore().commitChanges();
							}
						}
					} else {
						// Item clickedItem = ((Item) event.getSource().getSelectionModel().getSelectedItem());
						// if(clickedItem!=null){
						// if(grid.getSelectionModel().getSelectedItems().contains(clickedItem)){
						// clickedItem.setSelected(true);
						// }else{
						// clickedItem.setSelected(false);
						// }
						//
						// }
					}
				}
			});

			if (enableDnD) {
				GridDragSource source = new GridDragSource<Item>(grid);

				GridDropTarget<Item> target = new GridDropTarget<Item>(grid);
				target.setAllowSelfAsSource(true);
				target.setFeedback(Feedback.BOTH);

				target.addDropHandler(new DndDropHandler() {

					@Override
					public void onDrop(DndDropEvent event) {
						// TODO Auto-generated method stub
						// grid.getSelectionModel().setSelection(selectedItemsForDrag);
					}
				});

				source.addDragStartHandler(new DndDragStartHandler() {
					@Override
					public void onDragStart(DndDragStartEvent event) {
						// selectedItemsForDrag = getSelectedItems();
					}
				});
			}

			panel = new ContentPanel();
			panel.setHeadingText("Columns");
			panel.getHeader().hide();
			panel.setWidget(grid);
			// panel.setBodyBorder(true);
			panel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
			// panel.addStyleName("margin-10");
		}

		return panel;
	}

	private native void JSNIsetEditable(TriggerFieldCell<String> combobox, boolean editable) /*-{
		combobox.@com.sencha.gxt.cell.core.client.form.TriggerFieldCell::editable = editable;
	}-*/;

	private CompleteEditHandler<Item> getCompleteEditHandler(final ColumnConfig<Item, String> label) {
		CompleteEditHandler completeEditHandler = new CompleteEditHandler<Item>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Item> event) {

				grid.getStore().commitChanges();
			}
		};

		return completeEditHandler;
	}
	
	public Item getCurrentlySelectedItem(){
		return currentlySelectedItem;
	}

	public void saveCurrentSelection() {
		Item selecteItem = grid.getSelectionModel().getSelectedItem();
		if (selecteItem != null) {
			List<Item> dataValues = dataSelector.getAllRightColumnValues();

			selecteItem.getData().clear();
			if (dataValues != null) {
				for (Item i : dataValues) {
					selecteItem.getData().add(i);

				}
			}
			store.update(selecteItem);
		}

	}

	@Override
	public void processNotify(NotifyPayload notifyPayload) {
		if (notifyPayload.getWindowName().equals(DualFieldSelector.class.getName())) {
			if (Operation.SAVE.name().equals((String) notifyPayload.getType())) {
				saveCurrentSelection();
			}
		}
	}
}
