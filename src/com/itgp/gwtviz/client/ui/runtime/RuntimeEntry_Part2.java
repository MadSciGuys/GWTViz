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



package com.itgp.gwtviz.client.ui.runtime;

import com.google.gwt.core.client.*;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.itgp.gwtviz.client.charts.nv.*;
import com.itgp.gwtviz.client.comm.ServerComm;
import com.itgp.gwtviz.client.ui.ProgressBarNotificationListener;
import com.itgp.gwtviz.client.ui.UIUtility.Charts;
import com.itgp.gwtviz.client.ui.runtime.ColMeta.TYPE;
import com.itgp.gwtviz.client.ui.runtime.decorator.*;
import com.itgp.gwtviz.client.ui.runtime.decorator.modelV1.*;
import com.itgp.gwtviz.client.ui.runtime.event.*;
import com.itgp.gwtviz.shared.gconfig.*;
import com.itgp.gwtviz.shared.model.*;
import java.util.*;

/**
 * <p>
 *
 * @author Warp
 */
public class RuntimeEntry_Part2 extends RuntimeEntryBase {

    protected static final int init            = 0;

    protected static final int processData     = 1;
    protected static final int processDataDone = 2;
    protected static final int applyFilters    = 3;
    protected static final int createSeries    = 4;
    protected static final int createChart     = 5;
    protected static final int displayChart    = 6;
    protected static final int finish          = 100;
    protected int              next;

    LocalRepeatingCommand      cmd;

    public static void getFilteredData(GraphConfigImpl_1 config, String dataArray, FilterCompleteListener listener) {

        RuntimeDataV1 runData = new RuntimeDataV1();    // Data holder (could be different types) for the chart
        runData.config     = config;
        runData.designTime = true;
        runData.addFilterCompleteListener(listener);
        runData.dataArray = dataArray;

        // -------------- Progress and error listeners
        DivNotificationListener         divLis = new DivNotificationListener();
        ProgressBarNotificationListener pLis   = new ProgressBarNotificationListener();
        runData.progressReporting.add(divLis);
        runData.progressReporting.add(pLis);
        runData.errorReporting.add(divLis);

        // ------------------------------------------------

        RuntimeEntry_Part2 re2 = new RuntimeEntry_Part2(runData);

        re2.initFromConfig();

        re2.designTimeLoop();

    }

    public RuntimeEntry_Part2(RuntimeDataV1 run) {
        super(run);
        this.cmd = new LocalRepeatingCommand();
    }

    public void designTimeLoop() {
        Scheduler.get().scheduleIncremental(this.cmd);    // no wait time between loop reps becasue there's no visual feedback
    }

    public void runtimeLoop() {
        Scheduler.get().scheduleFixedDelay(this.cmd, 20);    // those 20 ms are crucial to the browser having time to do DOM work. Without them sometimes updates

        // don't
    }

    protected class LocalRepeatingCommand implements RepeatingCommand {
        public boolean execute() {
            boolean loop_again = true;
            switch (next) {
               case init :

                   // First, initialize the data
                   run.setRawJSONData(run.dataArray);
                   next = processData;

                   return true;

               case processData :

                   boolean stillProcessing = run.loopSetData();
                   next = (stillProcessing ? processData : processDataDone);

                   return true;    // even if subprocess is done, don't stop - go to next step

               case processDataDone :
                   sendProgress("Applying filters. One moment please....");
                   next = applyFilters;

                   return true;

               case applyFilters :
                   step4_th2_apply_filterSet();

                   if (run.designTime) {

                       // return the filtered data
                       run.getFilterListeners().fireEvent(new FilterCompleteEvent(run));

                       next = finish;

                       return true;    // continue but go to clean-up
                   } else {
                       sendProgress("Creating chart series. One moment please....");
                       next = createSeries;

                       return true;
                   }

               case createSeries :
                   try {
                       step5_th2_create_series();
                   } catch (Exception ex) {
                       sendError(ex.getMessage());
                       next = finish;

                       return false;
                   }

                   sendProgress("Creating the chart instance. One moment please....");
                   next = createChart;

                   return true;

               case createChart :
                   boolean continueLoop = true;
                   try {
                       continueLoop = step6_th2_display_chart();
                   } catch (Exception ex) {
                       sendError(ex.getMessage());
                       next = finish;

                       return false;
                   }

                   if (continueLoop) {
                       sendProgress("Displaying chart. One moment please...");
                       next = finish;
                   }

                   return continueLoop;

               // case displayChart :
               //
               // if (run.chartToDisplay != null) {
               // displayXYBasedChart();
               // }
               //
               // next = finish;
               //
               // return true;

               case finish :
                   sendProgressDone();
                   next = 0;           // reset
                   run.progressReporting.removeAll();
                   run.errorReporting.removeAll();

                   return false;       // done

               default :
                   sendError("Unknown next label : '" + next + "'");
                   next = 0;           // reset
                   run.progressReporting.removeAll();
                   run.errorReporting.removeAll();
                   loop_again = false;

                   break;
            }

            return loop_again;
        }
    }    // LocalRepeatingCommand


    protected void step4_th2_apply_filterSet() {

        sendProgress("Initializing filters for the data...");
        ModelV1FilterSet filterSet = new ModelV1FilterSet();
        filterSet.setModel(run.dataModel);    // put the data in so the filters can use it

        List<Item> filterList = null;

        // create and apply filterset to raw data in DataModelV1
        filterList = ItemUtil.convert(run.config.getFilterColumns());

        int               filterCount = filterList.size();
        ArrayList<Filter> filters     = new ArrayList<Filter>();

        for (int i = 0; i < filterCount; i++) {
            Item filterItem = filterList.get(i);
            if (filterItem != null) {

                List<Item> filterData = filterItem.getData();
                if (filterData != null && filterData.size() > 0) {
                    FilterColumnExactMatch colFilter = new FilterColumnExactMatch(filterItem);
                    filters.add(colFilter);
                }
            }
        }    // for filters

        Filter[] filterArray = filters.toArray(new Filter[0]);

        filterSet.setFilters(filterArray);

        // In com.itgp.gwtviz.client.ui.runtime.decorator.Sorter
        // public static final int DESCENDING = -1;
        // public static final int NOT_SORTED = 0;
        // public static final int ASCENDING = 1;

        SortedMap<Integer, ColumnSorter> sortItems      = new TreeMap<Integer, ColumnSorter>();    // will help sort things by sort order

        String yAxisSortLabel = GraphConfigUtility.getOptionString(run.config, GraphConfigUtility.Options.YAxisSort);
        
        run.sortOnYAxis = false;
        if (yAxisSortLabel != null && yAxisSortLabel.length() > 0 && (!yAxisSortLabel.equals(GraphConfigUtility.YAxisSort.NoSort.name()))) {
            run.sortOnYAxis = true;

            if (yAxisSortLabel.equals(GraphConfigUtility.YAxisSort.Descending.name())) {
                run.sortOnYAxisDirection = GraphConfigUtility.YAxisSort.Descending;
            } else {
                run.sortOnYAxisDirection = GraphConfigUtility.YAxisSort.Ascending;
            }
        }

        if (!run.sortOnYAxis) {

            // sort on XAxis

            List<Item> xItems = ItemUtil.convert(run.config.getXColumns());

            int        n      = xItems.size();
            for (int i = 0; i < n; i++) {
                Item item = xItems.get(i);
                if (item.isSelected()) {

                    String sSortOrder = item.getSortOrder();
                    if (sSortOrder != null && sSortOrder.length() > 0) {
                        int sortOrder = -1;
                        try {
                            sortOrder = Integer.parseInt(sSortOrder);
                        } catch (Throwable t) {}

                        int sortDirection = Sorter.ASCENDING;    // Ascending
                        if (sortOrder != -1) {
                            String sSortDirection = item.getSortDirection();
                            if (sSortDirection == null) {
                                sSortDirection = "A";
                            }

                            sSortDirection = sSortDirection.toUpperCase();

                            if (sSortDirection.startsWith("D")) {
                                sortDirection = Sorter.DESCENDING;
                            }
                        }

                        String       colName      = item.getValue();
                        ColMeta      meta         = run.dataModel.getColMeta(colName);

                        ColumnSorter columnSorter = new ColumnSorter(meta.position, sortDirection);
                        sortItems.put(sortOrder, columnSorter);
                    }                                            // sortOrder != null
                }
            }                                                    // for xItems;
        }

        if (sortItems.size() > 0) {
            ArrayList<ColumnSorter> sorters = new ArrayList<ColumnSorter>(sortItems.values());

            Sorter                  sorter  = (sorters.size() > 0 ? MultiCriteriaSorter.create(sorters) : null);
            if (sorter != null) {
                filterSet.setSorter(sorter);
            }
        }    // sorterItems.size > 0

        sendProgress("Applying filters. One moment please...");
        run.dataModel.setFilterSet(filterSet);
        filterSet.refresh();    // trigger filter

//      testSort();
    }

//  protected void testSort(){
//      FilterSet filterSet = run.dataModel.getFilterSet();
//      Object[][] data = run.dataModel.getData();
//      ArrayList<Double> sortedYList= new ArrayList<>();
//      ArrayList<Double> rawYList= new ArrayList<>();
//      
//          
//      int count = filterSet.getOutputSize();
//      for (int i=0; i < count; i++){
//          
//          int rowNumber = filterSet.convertIndexToModel(i);
//          
//          Double y = (Double) data[rowNumber][1];
//          sortedYList.add(y);
//          
//      }
//
//      String sortedYListAsString = sortedYList.toString();
//      System.out.println(sortedYListAsString);
//  }

    protected void step5_th2_create_series() throws Exception {

        sendProgress("Creating data series for the chart...");
        List<Item> xItems = ItemUtil.convert(run.config.getXColumns());
        int        n      = xItems.size();
        if (n == 0) {
            throw new IllegalStateException("There are no x columns defined in chart config");
        }

        ArrayList<ColMeta> tempColArray = new ArrayList<ColMeta>();
        for (int i = 0; i < n; i++) {
            Item item = xItems.get(i);
            if (item.isSelected()) {

                String colName  = item.getValue();
                String colLabel = item.getLabel();
                if (colLabel == null) {
                    colLabel = colName;
                }

                ColMeta meta = run.dataModel.getColMeta(colName);
                meta.label = colLabel;
                tempColArray.add(meta);
            }
        }

        ColMeta[] xCols       = tempColArray.toArray(new ColMeta[0]);

        String    yColumnName = run.config.getYColumn();
        if (yColumnName == null) {
            throw new IllegalStateException("yColumnName is null in chart config");
        }

        ColMeta yCol = null;
        if (yColumnName.equals(ItemUtil._ROW_COUNT)) {

            // Calculated column at position -1
            yCol = ColMeta.create(-1, ItemUtil._ROW_COUNT);
            yCol.setType(TYPE.NUMBER);
        } else {

            yCol = run.dataModel.getColMeta(yColumnName);

            if (yCol == null) {
                throw new IllegalStateException("y column '" + yColumnName + "'does not exist!");
            }
        }

        String colLabel = run.config.getYColumnLabel();
        if (colLabel == null || colLabel.length() == 0) {
            colLabel = yCol.name;
        }

        yCol.label = colLabel;

        int        viewRowCount    = run.dataModel.getRowCount();    // get the Row count after filtering
        Object[][] data            = run.dataModel.getData();

        String     breakColumnName = GraphConfigUtility.getOptionString(run.config, GraphConfigUtility.Options.MultiSeriesBreakColumn);
        ColMeta    breakCol        = null;
        boolean    applyBreak      = (breakColumnName != null);
        if (applyBreak) {
            breakCol = run.dataModel.getColMeta(breakColumnName);
            int breakColNum = breakCol.position;

            // create series broken by breakCol

            for (int i = 0; i < xCols.length; i++) {
                ColMeta                      xCol     = xCols[i];

                LinkedHashMap<String, Serie> breakMap = new LinkedHashMap<String, Serie>();

                for (int j = 0; j < viewRowCount; j++) {
                    int    modelIndex  = run.dataModel.convertToModelIndex(j);    // convert each row to model index

                    Object breakObject = data[modelIndex][breakColNum];
                    String breakString = breakObject.toString();                  // convert to String for equals purposes (Double doesn't work well and same double value should

                    // have the same string value since conversion is uniform)

                    // find the serie by breakString first (or create it)
                    Serie serie = breakMap.get(breakString);
                    if (serie == null) {

                        // create new serie
                        serie = new Serie(xCol.name, xCol.getLabel(), xCol, yCol, breakCol, breakObject);
                        breakMap.put(breakString, serie);
                    }

                    // add the data point to the correct serie
                    serie.add(modelIndex);
                }    // for j

                // now add all the nicely broken-up series to the model
                Collection<Serie> mapSeriesCollection = breakMap.values();
                for (Serie s : mapSeriesCollection) {
                    run.dataModel.series.add(s);
                }

            }    // for i (next serie)

        } else {

            // Simple add the series to the
            for (int i = 0; i < xCols.length; i++) {
                ColMeta xCol = xCols[i];

                // create and add the series at the same time
                Serie serie = new Serie(xCol.name, xCol.getLabel(), xCol, yCol);
                run.dataModel.series.add(serie);

                // now fill the serie
                for (int j = 0; j < viewRowCount; j++) {
                    int modelIndex = run.dataModel.convertToModelIndex(j);    // convert each row to model index
                    serie.add(modelIndex);
                }                                                             // for j

            }                                                                 // for i (next serie)
        }                                                                     // if applyBreak

    }

    protected boolean step6_th2_display_chart() throws Exception {
        boolean continueLoop = true;
        sendProgress("Displaying the chart... One moment please..");
        ArrayList<Serie> serieList  = run.dataModel.series;
        int              serieCount = serieList.size();

        if (serieCount == 0) {
            throw new IllegalStateException("There are no series to display!");
        }

        String chartType = run.config.getChartType();
        if (chartType == null) {
            chartType = Charts.Line_Chart.name();    // from ChartV1
        }

        if (chartType.equals(Charts.Bar_Chart.name()) || chartType.equals(Charts.Bar_Chart_With_Grouped_Columns.name()) || chartType.equals(Charts.Bar_Chart_With_Stacked_Columns.name())) {

            if (run.sortOnYAxis) {
                YSortedBarChartV1 chart = YSortedBarChartV1.create();
                run.chartXY = chart;
                makeYSortedChart();
            } else {

                // normal bar chart
                BarChartV1 chart = BarChartV1.create();
                run.chartXY = chart;
                makeXYChart();    // stacking charts work using this
            }

            decorateXYLabels();
            run.chartToDisplay = run.chartXY;
            showXYBasedChart();

        } else if (chartType.equals(Charts.Line_Chart.name())) {
            LineChartV1 chart = LineChartV1.create();
            run.chartXY = chart;

            // makeXYChart(true); //stacking charts work using this
            makeLineChart();

            decorateXYLabels();
            run.chartToDisplay = chart;
            showXYBasedChart();
        } else if (chartType.equals(Charts.Scatter_Plot_Chart.name())) {
            ScatterChartV1 chart = ScatterChartV1.create();
            run.scatterChart   = chart;
            run.chartToDisplay = chart;

            makeScatterChart();
            showXYBasedChart();

        } else if (chartType.equals(Charts.Multivariate_Scatter_Plot_Chart.name())) {

            // special case
            run.matrixScatterChart = MatrixScatterChartV1.create();

            makeMultiVariateScatterChart();
            showScatterMatrixChart(run.matrixScatterChart);
        } else if (chartType.equals(Charts.Pie_Chart.name())) {
            PieChartV1 chart = DonutPieChartV1.create();
            run.pieChart = chart;
            makePieChart();
            run.chartToDisplay = run.pieChart;
            showXYBasedChart();
        } else {

            // TODO convert to event
            ServerComm.alert("Unknown chart type '" + chartType + "' !!");
            continueLoop = false;

            return continueLoop;
        }

        return continueLoop;
    }

    protected void decorateXYLabels() {
        String xLabel = run.config.getXColumnLabel();
        if (xLabel != null) {
            if (run.sortOnYAxis) {
                run.chartXY.setXAxisLabel("Sequential numbers (Originally: " + xLabel + " )");
            } else {
                run.chartXY.setXAxisLabel(xLabel);
            }
        }

        String yLabel = run.config.getYColumnLabel();
        if (yLabel != null) {
            run.chartXY.setYAxisLabel(yLabel);
        }

        // Here s should be 150 or 100 etc....
        String s = GraphConfigUtility.getOptionString(run.config, GraphConfigUtility.Options.ForceYPercentage);

        // if ( s == null){
        // s = "120";
        // }

        if (s != null && s.length() > 0) {
            int yPercentage = -1;
            try {
                yPercentage = Integer.parseInt(s);
            } catch (Exception e) {
                yPercentage = -1;
            }

            if (yPercentage != -1) {

                double extraPercent = yPercentage - 100;
                if (extraPercent > 0) {
                    extraPercent = extraPercent / 100;
                    extraPercent = extraPercent / 2;    // extra on each side of the interval

                    double           min         = 0;
                    double           max         = 0;
                    boolean          initialized = false;
                    ArrayList<Serie> serieList   = run.dataModel.series;
                    int              serieCount  = serieList.size();
                    for (int i = 0; i < serieCount; i++) {
                        Serie  serie    = serieList.get(i);

                        double localMin = serie.getMinYValue();
                        double localMax = serie.getMaxYValue();
                        if (!initialized) {

                            min         = localMin;
                            max         = localMax;
                            initialized = true;
                        } else {
                            if (localMin < min) {
                                min = localMin;
                            }

                            if (localMax > max) {
                                max = localMax;
                            }
                        }
                    }                                   // for

                    double rangeSize = max - min;
                    if (rangeSize > 0.00001) {

                        // do something only if range is > 0

                        double      extraRange = rangeSize * extraPercent;

                        double      realMin    = (double) Math.round(min - extraRange);
                        double      realMax    = (double) Math.round(max + extraRange);
                        ForcedRange rY         = run.chartXY.getOrCreateForcedRangeY();
                        rY.setMin(realMin);
                        rY.setMax(realMax);

                    }    // if ( extra > 0){
                }

                // ForcedRange rY = run.chartXY.getOrCreateForcedRangeY();
                // rY.setMin(-10);
                // rY.setMax(2500);
                //
                // ForcedRange rX = run.chartXY.getOrCreateForcedRangeX();
                // rX.setMin(-1);
                // rX.setMax(20);
            }            // if ( yPercentage != -1)
        }                // if ( s != null && s.length() > 0)
    }    // decorateXYLabels()

    protected void gatherAllSeriesXValues(ArrayList<HashMap<String, Integer>> seriesData, Set<Object> xValues) throws Exception {
        ArrayList<Serie> serieList  = run.dataModel.series;
        int              serieCount = serieList.size();
        Object[][]       data       = run.dataModel.getData();

        // First get all the xs from all the series into xValues sorted
        // Also put each series itself in a HashMap of x/y
        for (int i = 0; i < serieCount; i++) {
            Serie serie = serieList.get(i);
            if (serie.yCol.type != ColMeta.TYPE.NUMBER) {
                String chartType = run.config.getChartType();

                throw new IllegalArgumentException("The Y axis is not a numeric value for a '" + chartType + "' chart type!");
            }

            int                      xColNo     = serie.getXCol().position;
            ArrayList<Integer>       rowList    = serie.getRowList();
            int                      rowCount   = rowList.size();

            HashMap<String, Integer> seriesTree = new HashMap<String, Integer>();
            for (int j = 0; j < rowCount; j++) {

                int    rowIndex = rowList.get(j);

                Object xVal     = data[rowIndex][xColNo];
                String xKey     = (xVal == null ? "" : xVal.toString());

                if (!seriesTree.containsKey(xKey)) {
                    seriesTree.put(xKey, rowIndex);    // first xValue of it's kind wins for every series
                }

                xValues.add(xVal);
            }

            seriesData.add(seriesTree);
        }
    }    // gatherAllSeriesData

    protected void makeXYChart() throws Exception {
        makeXYChart(false);

    }

    protected void makeXYChart(boolean useNullNotZero) throws Exception {

        ArrayList<Serie>                    serieList  = run.dataModel.series;
        int                                 serieCount = serieList.size();
        Object[][]                          data       = run.dataModel.getData();

        ArrayList<HashMap<String, Integer>> seriesData = new ArrayList<HashMap<String, Integer>>(serieCount);
        TreeSet<Object>                     xValues    = new TreeSet<Object>(new SeriesComparator());    // sorts the Xs and keeps them unique

        gatherAllSeriesXValues(seriesData, xValues);

        // seriesData and xValues completely instantiated

        for (int i = 0; i < serieCount; i++) {
            HashMap<String, Integer> seriesTree = seriesData.get(i);
            Serie                    serie      = serieList.get(i);
            int                      yColNo     = serie.getYCol().position;
            ArrayList<Integer>       rowList    = serie.getRowList();
            int                      rowCount   = rowList.size();
            XYSeriesV1               xy         = run.chartXY.createAndAddXYSeries(serie.getSeriesLabelEnhanced());

            for (Object xValue : xValues) {
                String xKey = xValue.toString();

                // Calculate y value
                Double yVal = (useNullNotZero ? null : 0.0);
                if (seriesTree.containsKey(xKey)) {

                    int rowIndex = seriesTree.get(xKey);    // gets the row Index in the data model based on the xValue

                    if (yColNo >= 0) {
                        yVal = (Double) data[rowIndex][yColNo];
                    } else {

                        // calculated
                        if (serie.getYCol().name.equals(ItemUtil._ROW_COUNT)) {
                            yVal = new Double(rowCount);
                        }
                    }
                }

                // min/max here
                if (yVal != null) {
                    double current = yVal.doubleValue();
                    if (serie.isMinYInitialized()) {
                        if (current < serie.getMinYValue()) {
                            serie.setMinYValue(current);
                        }
                    } else {
                        serie.setMinYValue(current);
                        serie.setMinYInitialized(true);
                    }

                    if (serie.isMaxYInitialized()) {
                        if (current > serie.getMaxYValue()) {
                            serie.setMaxYValue(current);
                        }
                    } else {
                        serie.setMaxYValue(current);
                        serie.setMaxYInitialized(true);
                    }
                }

                // add all the Xs in the same order for all series. Y will be either 0 or the actual Y
                if (yVal == null) {
                    xy.addNull(xValue);
                } else {
                    xy.addXY(xValue, yVal);
                }

            }

        }

        //displayXY();
    }

    protected void makeYSortedChart() throws Exception {
        makeYSortedChart(false);

    }

    protected void makeYSortedChart(boolean useNullNotZero) throws Exception {

        ArrayList<Serie>                    serieList  = run.dataModel.series;
        int                                 serieCount = serieList.size();
        Object[][]                          data       = run.dataModel.getData();

        ArrayList<HashMap<String, Integer>> seriesData = new ArrayList<HashMap<String, Integer>>(serieCount);
        LinkedHashSet<Object>               xValues    = new LinkedHashSet<Object>();    // sorts the Xs and keeps them unique

        gatherAllSeriesXValues(seriesData, xValues);

        // seriesData and xValues completely instantiated

        for (int i = 0; i < serieCount; i++) {
            HashMap<String, Integer> seriesTree = seriesData.get(i);
            Serie                    serie      = serieList.get(i);
            int                      yColNo     = serie.getYCol().position;
            ArrayList<Integer>       rowList    = serie.getRowList();
            int                      rowCount   = rowList.size();
            YSortedSeriesV1          xy         = ((YSortedBarChartV1) run.chartXY).createAndAddYSortedSeries(serie.getSeriesLabelEnhanced());

            YSortedModelV1           model      = new YSortedModelV1();

            double                   counter    = 0;
            for (Object xValue : xValues) {
                String xKey = xValue.toString();

                if (seriesTree.containsKey(xKey)) {
                    // no phantom zeros or nulls here
                    // Calculate y value
                    Double yVal     = (useNullNotZero ? null : 0.0);

                    int    rowIndex = seriesTree.get(xKey);                  // gets the row Index in the data model based on the xValue

                    if (yColNo >= 0) {
                        yVal = (Double) data[rowIndex][yColNo];
                    } else {

                        // calculated
                        if (serie.getYCol().name.equals(ItemUtil._ROW_COUNT)) {
                            yVal = new Double(rowCount);
                        }
                    }

                    // min/max here
                    if (yVal != null) {
                        double current = yVal.doubleValue();
                        if (serie.isMinYInitialized()) {
                            if (current < serie.getMinYValue()) {
                                serie.setMinYValue(current);
                            }
                        } else {
                            serie.setMinYValue(current);
                            serie.setMinYInitialized(true);
                        }

                        if (serie.isMaxYInitialized()) {
                            if (current > serie.getMaxYValue()) {
                                serie.setMaxYValue(current);
                            }
                        } else {
                            serie.setMaxYValue(current);
                            serie.setMaxYInitialized(true);
                        }
                    }

                    String label = xKey;

                    // add all the Xs in the same order for all series. Y will be either 0 or the actual Y
                    if (yVal == null) {

                        //xy.addNull(xValue);
                    } else {
                        counter++;
                        model.add(counter, yVal, label);
                    }

                } //  if (seriesTree.containsKey(xKey))
            }                                                                //for xValues

            YSortedModelFilterSet filterSet = new YSortedModelFilterSet(run.sortOnYAxisDirection);
            filterSet.setModel(model);
            filterSet.refresh();                                             // do the sort

            int n = filterSet.getOutputSize();
            for (int j = 0; j < n; j++) {
                double   counter1 = j + 1;
                int      index    = filterSet.convertIndexToModel(j);        // transform to model index
                Object[] row      = model.get(index);
                xy.addCoords(counter1, (Double) row[1], (String) row[2]);    //xy.addCoords(counter, yVal, label);

            }

        }                                                                    //for serieCount

//      displayXY();
    }

//  protected void displayXY() {
//
//      JsArray<XYSeriesV1> array    = run.chartXY.getXYSeries();
//      String              stringXY = JsonUtils.stringify(array);
//      System.out.println(stringXY);
//
//  }

    protected void makeLineChart() throws Exception {

        ArrayList<Serie> serieList  = run.dataModel.series;
        int              serieCount = serieList.size();
        Object[][]       data       = run.dataModel.getData();

        for (int i = 0; i < serieCount; i++) {
            Serie serie = serieList.get(i);

            if (serie.yCol.type != ColMeta.TYPE.NUMBER) {
                String chartType = run.config.getChartType();

                throw new IllegalArgumentException("The Y axis is not a numeric value for a '" + chartType + "' chart type!");
            }

            int                xColNo   = serie.getXCol().position;
            int                yColNo   = serie.getYCol().position;

            XYSeriesV1         xy       = run.chartXY.createAndAddXYSeries(serie.getSeriesLabelEnhanced());
            ArrayList<Integer> rowList  = serie.getRowList();
            int                rowCount = rowList.size();
            for (int j = 0; j < rowCount; j++) {

                int    rowIndex = rowList.get(j);
                Object xVal     = data[rowIndex][xColNo];

                // Double yVal = (Double) data[rowIndex][yColNo];
                Double yVal = 0.0;
                if (yColNo >= 0) {
                    yVal = (Double) data[rowIndex][yColNo];
                } else {

                    // calculated
                    if (serie.getYCol().name.equals(ItemUtil._ROW_COUNT)) {
                        yVal = new Double(rowCount);
                    } else {
                        throw new IllegalArgumentException("Unknown column name: '" + serie.getYCol().name + "'");
                    }
                }

                // min/max here
                if (yVal != null) {
                    double current = yVal.doubleValue();
                    if (serie.isMinYInitialized()) {
                        if (current < serie.getMinYValue()) {
                            serie.setMinYValue(current);
                        }
                    } else {
                        serie.setMinYValue(current);
                        serie.setMinYInitialized(true);
                    }

                    if (serie.isMaxYInitialized()) {
                        if (current > serie.getMaxYValue()) {
                            serie.setMaxYValue(current);
                        }
                    } else {
                        serie.setMaxYValue(current);
                        serie.setMaxYInitialized(true);
                    }
                }

                xy.addXY(xVal, yVal);
            }    // next j (next row number)

        }        // next i (next serie)
    }

    protected void makeScatterChart() throws Exception {

        ArrayList<Serie>                    serieList  = run.dataModel.series;
        int                                 serieCount = serieList.size();
        Object[][]                          data       = run.dataModel.getData();

        ArrayList<HashMap<String, Integer>> seriesData = new ArrayList<HashMap<String, Integer>>(serieCount);
        TreeSet<Object>                     xValues    = new TreeSet<Object>(new SeriesComparator());    // sorts the Xs and keeps them unique

        gatherAllSeriesXValues(seriesData, xValues);

        // seriesData and xValues completely instantiated

        for (int i = 0; i < serieCount; i++) {
            HashMap<String, Integer> seriesTree       = seriesData.get(i);
            Serie                    serie            = serieList.get(i);
            int                      yColNo           = serie.getYCol().position;
            ColMeta                  scatterSizeCol   = serie.getScatterSizeCol();
            int                      scatterSizeColNo = (scatterSizeCol == null ? -1 : scatterSizeCol.position);
            ArrayList<Integer>       rowList          = serie.getRowList();
            int                      rowCount         = rowList.size();
            ScatterSeriesV1          coords           = run.scatterChart.createAndAddSeries(serie.getSeriesLabelEnhanced());

            for (Object xValue : xValues) {
                String xKey = xValue.toString();

                // Calculate y value
                Double yVal = 0.0;
                if (seriesTree.containsKey(xKey)) {

                    int rowIndex = seriesTree.get(xKey);    // gets the row Index in the data model based on the xValue

                    if (yColNo >= 0) {
                        yVal = (Double) data[rowIndex][yColNo];
                    } else {

                        // calculated
                        if (serie.getYCol().name.equals(ItemUtil._ROW_COUNT)) {
                            yVal = new Double(rowCount);
                        }
                    }
                }

                Double size = 1.0;                          // default to size = 1 if no size column specified

                if (seriesTree.containsKey(xKey)) {
                    int rowIndex = seriesTree.get(xKey);    // gets the row Index in the data model based on the xValue
                    if (scatterSizeColNo >= 0) {
                        size = (Double) data[rowIndex][scatterSizeColNo];
                    }
                }

                coords.addPoint(xValue, yVal, size, Shape.CIRCLE);

            }

        }
    }

    protected void makeMultiVariateScatterChart() {

        ArrayList<Serie>  serieList  = run.dataModel.series;
        int               serieCount = serieList.size();
        Object[][]        data       = run.dataModel.getData();

        MatrixScatterData matrixData = MatrixScatterData.create();

        // JSONObject nice = new JSONObject(matrixData);

        for (int i = 0; i < serieCount; i++) {
            Serie              serie    = serieList.get(i);

            int                xColNo   = serie.getXCol().position;
            ArrayList<Integer> rowList  = serie.getRowList();
            int                rowCount = rowList.size();
            String             dataName = serie.getSeriesLabelEnhanced();

            for (int j = 0; j < rowCount; j++) {

                int    rowIndex = rowList.get(j);
                Object xVal     = data[rowIndex][xColNo];

                // String xKey = (xVal == null ? "" : xVal.toString());

                matrixData.set(j, dataName, xVal);

                // String s = nice.toString();
                // s = s + "";
            }

        }

        run.matrixScatterChart.setData(matrixData);
    }

    protected void makePieChart() throws Exception {

        ArrayList<Serie> serieList = run.dataModel.series;
        Serie            serie     = serieList.get(0);
        if (serie.yCol.type != ColMeta.TYPE.NUMBER) {
            String chartType = run.config.getChartType();

            throw new IllegalArgumentException("The Y axis is not a numeric value for a '" + chartType + "' chart type!");
        }

        Object[][]         data     = run.dataModel.getData();
        int                xColNo   = serie.getXCol().position;
        int                yColNo   = serie.getYCol().position;
        ArrayList<Integer> rowList  = serie.getRowList();
        int                rowCount = rowList.size();
        for (int j = 0; j < rowCount; j++) {

            int    rowIndex = rowList.get(j);
            Object xVal     = data[rowIndex][xColNo];
            xVal = (xVal == null ? "" : xVal);
            Double yVal = 0.0;
            if (yColNo >= 0) {
                yVal = (Double) data[rowIndex][yColNo];
            } else {

                // calculated
                if (serie.getYCol().name.equals(ItemUtil._ROW_COUNT)) {
                    yVal = new Double(rowCount);
                }
            }

            run.pieChart.add(xVal.toString(), yVal);
        }    // next j (next row number)
    }

    // ------------------------------------------------------------------
    public final void showXYBasedChart() {
        displayChartNative(run.chartToDisplay);
    }

    /**
     * Call the actual JavaScript library to display the chartXY
     *
     * @param value
     */
    public final native void displayChartNative(JavaScriptObject value)    /*-{
               $wnd.itgp.showXYBasedChart(value)
       }-*/
    ;

    public final native void showScatterMatrixChart(JavaScriptObject value)    /*-{
               $wnd.itgp.showScatterMatrixChart(value)
       }-*/
    ;

}
