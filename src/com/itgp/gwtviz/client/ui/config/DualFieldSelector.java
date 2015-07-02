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

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.itgp.gwtviz.client.ui.fixes.Itgp2DualListField;
import com.itgp.gwtviz.client.ui.fixes.ItgpListStore;
import com.itgp.gwtviz.client.ui.fixes.MyDualListFieldAppearance;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.gconfig.GraphConfigUtility;
import com.itgp.gwtviz.shared.model.Item;
import com.itgp.gwtviz.shared.model.ItemUtil;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

public class DualFieldSelector implements IsWidget, IMRouteCallable {
	String rightItemState = "";
	String leftItemsState = "";
	Itgp2DualListField<Item, String> field;
	ItgpListStore<Item> rightItems;
	ItgpListStore<Item> leftItems;
	List<Item> allItems;
	VerticalLayoutContainer vlc;
	TextField searchFieldFrom;
	TextField searchFieldTo;
	GraphConfigImpl_1 graphConfigImpl;
	private String emptySearchTxt = "Search...";
	INotify iNotify;
	SortType sortTypeLeft;
	SortType sortTypeRight;

	enum SortType {
		ALPHA, NUMERIC, NONE
	};

	public DualFieldSelector(List<Item> allItems, String emptySearchText, GraphConfigImpl_1 graphConfigImpl, SortType sortTypeLeft, SortType sortTypeRight,
			INotify iNotify) {
		this.allItems = allItems;
		this.emptySearchTxt = emptySearchText;
		this.graphConfigImpl = graphConfigImpl;
		this.sortTypeLeft = sortTypeLeft;
		this.sortTypeRight = sortTypeRight;
		this.iNotify = iNotify;
	}

	public void setINotify(INotify iNotify) {
		this.iNotify = iNotify;
	}

	interface ItemProperties extends PropertyAccess<Item> {
		ModelKeyProvider<Item> value();

		@Path("value")
		ValueProvider<Item, String> valueProp();
	}

	private void resetWidget() {
		searchFieldFrom.setEmptyText(emptySearchTxt);
		searchFieldTo.setEmptyText(emptySearchTxt);
	}

	private void makeTextFieldSearchableLeft(final TextField tf, final Store<Item> store) {
		tf.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				String value = tf.getText();
				if (value == null || value.length() == 0) {
					store.setEnableFilters(false);
				} else {
					store.setEnableFilters(false);
					store.setEnableFilters(true);
				}
			}
		});
		resetWidget();

	}

	// private void filterData(List<Item> _allItems) {
	// leftItems.clear();
	// leftItems.addAll(_allItems);
	// field.getToView().refresh();
	// }

	@Override
	public Widget asWidget() {
		if (vlc == null) {
			ItemProperties props = GWT.create(ItemProperties.class);

			leftItems = new ItgpListStore<Item>(props.value());
			if (allItems != null) {
				leftItems.addAll(allItems);
			}

			rightItems = new ItgpListStore<Item>(props.value());

			searchFieldFrom = new TextField();
			searchFieldTo = new TextField();

			// searchField.setAllowBlank(false);

			// field = new Itgp2DualListField<Item, String>(leftItems, rightItems, props.valueProp(), new TextCell(), new MyDualListFieldAppearance());
			field = new Itgp2DualListField<Item, String>(leftItems, rightItems, props.valueProp(), new TextCell(), new MyDualListFieldAppearance());
			field.addValidator(new EmptyValidator<List<Item>>());
			field.setEnableDnd(true);
			field.setMode(Itgp2DualListField.Mode.INSERT);

			// field.getFromStore().addFilter(new StoreFilter<Item>() {
			// @Override
			// public boolean select(Store<Item> store, Item parent, Item item) {
			// if (item.getValue().equals("WEEEE-TEST")) {
			// return true;
			// } else {
			// return false;
			// }
			// }
			// });
			field.getToStore().addStoreHandlers(new StoreHandlers<Item>() {

				@Override
				public void onAdd(StoreAddEvent<Item> event) {
					NotifyPayload np = new NotifyPayload(null, DualFieldSelector.class.getName(), null);
					np.setType(ItemGrid.Operation.SAVE.name());
					// Window.alert("Adding "+event.getItems().get(0).getValue());

					// com.google.gwt.dom.client.Node n;//:appendChild
					if (iNotify != null) {

						iNotify.processNotify(np);
						field.getFromStore().applySort(false);
					}
				}

				@Override
				public void onRemove(StoreRemoveEvent<Item> event) {
					NotifyPayload np = new NotifyPayload(null, DualFieldSelector.class.getName(), null);
					np.setType(ItemGrid.Operation.SAVE.name());
					field.getFromStore().setEnableFilters(false);
					field.getFromStore().setEnableFilters(true);
					if (iNotify != null) {
						iNotify.processNotify(np);
						// field.getFromStore().applySort(false);
					}
				}

				@Override
				public void onFilter(StoreFilterEvent<Item> event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onClear(StoreClearEvent<Item> event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onUpdate(StoreUpdateEvent<Item> event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onDataChange(StoreDataChangeEvent<Item> event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onRecordChange(StoreRecordChangeEvent<Item> event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSort(StoreSortEvent<Item> event) {
					// TODO Auto-generated method stub

				}

			});

			//Apply left/From Sorts
			if (SortType.ALPHA.name().equals(sortTypeLeft.name())) {
				// Use value to sort
				field.getFromStore().addSortInfo(new StoreSortInfo<Item>(new Comparator<Item>() {
					@Override
					public int compare(Item o1, Item o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				}, SortDir.ASC));
			} else if (SortType.NUMERIC.name().equals(sortTypeLeft.name())) {
				// Use ID to sort
				field.getFromStore().addSortInfo(new StoreSortInfo<Item>(new Comparator<Item>() {
					@Override
					public int compare(Item o1, Item o2) {
						return Long.compare(o1.getId(), o2.getId());
					}
				}, SortDir.ASC));

			}

			//Apply right/To Sorts
			if (SortType.ALPHA.name().equals(sortTypeRight.name())) {
				// Use value to sort
				field.getToStore().addSortInfo(new StoreSortInfo<Item>(new Comparator<Item>() {
					@Override
					public int compare(Item o1, Item o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				}, SortDir.ASC));
			} else if (SortType.NUMERIC.name().equals(sortTypeRight.name())) {
				// Use ID to sort
				field.getToStore().addSortInfo(new StoreSortInfo<Item>(new Comparator<Item>() {
					@Override
					public int compare(Item o1, Item o2) {
						return Long.compare(o1.getId(), o2.getId());
					}
				}, SortDir.ASC));
			}

			// Enable filters for both from and to lists
			field.getFromStore().addFilter(new StoreFilterItem(searchFieldFrom));
			field.getFromStore().setEnableFilters(true);

			field.getToStore().addFilter(new StoreFilterItem(searchFieldTo));
			field.getToStore().setEnableFilters(true);

			makeTextFieldSearchableLeft(searchFieldFrom, field.getFromStore());
			makeTextFieldSearchableLeft(searchFieldTo, field.getToStore());

			HorizontalLayoutContainer hLayouytFilters = new HorizontalLayoutContainer();
			hLayouytFilters.add(searchFieldFrom, new HorizontalLayoutData(0.5, -1, new Margins(0, 25, 0, 0)));
			hLayouytFilters.add(searchFieldTo, new HorizontalLayoutData(0.5, -1, new Margins(0, 0, 0, 30)));

			HorizontalLayoutContainer hLayouytLabels = new HorizontalLayoutContainer();
			hLayouytLabels.add(new FieldLabel(null, "Excluded Items"), new HorizontalLayoutData(0.5, -1, new Margins(0, 25, 0, 0)));
			hLayouytLabels.add(new FieldLabel(null, "Included Items"), new HorizontalLayoutData(0.5, -1, new Margins(0, 0, 0, 30)));

			vlc = new VerticalLayoutContainer();
			// vlc.add(searchFieldFrom, new VerticalLayoutData(1, -1, new Margins(3)));
			vlc.add(hLayouytLabels, new VerticalLayoutData(-1, 25));
			vlc.add(hLayouytFilters, new VerticalLayoutData(-1, 25));
			vlc.add(field, new VerticalLayoutData(1, 1));

		}
		return vlc;
	}

	@Override
	// This sets the data from the server
	public void setData(List<Item> items) {
		allItems = items;
		if (GraphConfigUtility.isFilterEmpty(graphConfigImpl)) {
			setLeftColumnData(items);// , true);
		} else {
			List<Item> filteredItems = ItemUtil.convert(graphConfigImpl.getFilterColumns());
			for (Item filteredItem : filteredItems) {
				ItemUtil.removeBasedOnValue(filteredItem, items);
			}
			setLeftColumnData(items);// , true);
			setRightColumnValues(filteredItems);
		}
		leftItemsState = ItemUtil.topLevelToString(leftItems);
		rightItemState = ItemUtil.topLevelToString(rightItems);
	}

	public boolean hasChanges() {
		String _leftItemsState = ItemUtil.topLevelToString(leftItems);
		String _rightItemState = ItemUtil.topLevelToString(rightItems);

		if (leftItemsState.equals(_leftItemsState) && rightItemState.equals(_rightItemState)) {
			return false;
		} else {
			return true;
		}
	}

	public List<Item> getRightColumnValues() {
		return field.getValue();
	}

	public List<Item> getAllRightColumnValues() {
		return field.getAllRightValues();
	}

	public List<Item> getLeftColumnValues() {
		return field.getFromStore().getAll();
	}

	/**********************************************************
	 * 
	 * @param leftItems
	 *            -- should always be a complete set of values for the left side
	 * @param rightItems
	 *            -- should be the values that we want to see on the right side
	 **********************************************************/
	public void setData(List<Item> leftItems, List<Item> rightItems) {
		allItems = leftItems;
		if (leftItems != null && leftItems.size() > 0 && rightItems != null && rightItems.size() > 0) {
			leftItems.removeAll(rightItems);
		}
		setLeftColumnData(leftItems);
		setRightColumnValues(rightItems);

	}

	public void setRightColumnValues(List<Item> _rightItems) {
		this.rightItems.clear();
		this.rightItems.addAll(_rightItems);
		field.setValue(_rightItems);
		field.getToView().refresh();
	}

	public void setLeftColumnData(List<Item> _allItems) {
		field.getFromStore().clear();
		if (leftItems != null) {
			leftItems.clear();
			leftItems.addAll(_allItems);
		}
		// field.getFromStore().clear();
		field.getFromView().refresh();
		resetWidget();
	}

	// boolean addAllItems = false;
	// if (allItems == null) {
	// addAllItems = true;
	// }
	// allItems = _allItems;
	//
	// if (init) {
	// //field.clear();
	// field.getFromStore().clear();
	// rightItems.clear();
	// if (addAllItems) {
	// leftItems.addAll(allItems);
	// }
	// } else {
	// leftItems.addAll(allItems);
	// }
	// field.getToView().refresh();
	// resetWidget();
	// }

	// public void setRightColumnValues(List<Item> rightItems) {
	// rightItems.clear();
	// rightItems.addAll(rightItems);
	// field.getToView().refresh();
	// field.setValue(rightItems);
	// field.getToView().refresh();
	// }
	//
	// public void setLeftColumnData(List<Item> _allItems, boolean init) {
	// boolean addAllItems = false;
	// if (allItems == null) {
	// addAllItems = true;
	// }
	// allItems = _allItems;
	// if (leftItems != null) {
	// leftItems.clear();
	// }
	// if (init) {
	// //field.clear();
	// field.getFromStore().clear();
	// rightItems.clear();
	// if (addAllItems) {
	// leftItems.addAll(allItems);
	// }
	// } else {
	// leftItems.addAll(allItems);
	// }
	// field.getToView().refresh();
	// resetWidget();
	// }

}
