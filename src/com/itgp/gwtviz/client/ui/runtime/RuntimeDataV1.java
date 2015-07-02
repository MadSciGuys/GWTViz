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
package com.itgp.gwtviz.client.ui.runtime;

import static com.itgp.gwtviz.client.ui.runtime.ColMeta.create;
import static com.itgp.gwtviz.client.ui.runtime.event.NotificationEvent.createDoneEvent;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.*;
import com.itgp.gwtviz.client.charts.nv.*;
import com.itgp.gwtviz.client.comm.ServerComm;
import com.itgp.gwtviz.client.ui.runtime.ColMeta.TYPE;
import com.itgp.gwtviz.client.ui.runtime.event.*;
import com.itgp.gwtviz.client.ui.runtime.event.progressBar.ProgressBarNotificationEvent;
import com.itgp.gwtviz.shared.gconfig.GraphConfigImpl_1;
import com.itgp.gwtviz.shared.model.*;
import java.util.*;

/**
 * <p>
 *
 * @author Warp
 */
public class RuntimeDataV1 {
    public String            configText;
    public GraphConfigImpl_1 config;
    public String            fileName;
    public long              fileID;
    public boolean           designTime = false;

    String                   dataArray;
    AbstractChartXYV1        chartXY;
    PieChartV1               pieChart;
    ScatterChartV1           scatterChart;
    MatrixScatterChartV1     matrixScatterChart;


    ChartV1                  chartToDisplay;
    
    String                   rawJSONData;
    
//    JavaScriptObject        matrixScatterData;
    

    //--------------------
    private List<Item> columnDescriptors;
    public DataModelV1 dataModel = new DataModelV1();

    //--------------------
    public FilterCompleteListenerGroup    filterListeners   = new FilterCompleteListenerGroup();
    protected ProgressReportListenerGroup progressReporting = new ProgressReportListenerGroup();
    protected ErrorReportListenerGroup    errorReporting    = new ErrorReportListenerGroup();

    //--------------------

    public boolean useNative = true;

    //--------------------
    private int nextRowIndex    = -1;    // used in recursive entry
    private int numberOfRows    = -1;    // used in recursive entry
    private int numberOfColumns = -1;    // used in recursive entry

    //TODO: Rename to RuntimeDataV1
    public RuntimeDataV1() {}

    public void setColumnDescriptors(List<Item> columnDescriptors) {
        this.columnDescriptors = columnDescriptors;
        int       n        = columnDescriptors.size();
        ColMeta[] colArray = new ColMeta[n];

        for (int i = 0; i < n; i++) {
            Item    item = columnDescriptors.get(i);
            ColMeta cm   = create(i, item.getValue());    // position and column name
            cm.positionAtDesignTime = ItemUtil.getIndexInCompleteDataSet(cm.name);
            cm.run                  = this;
            colArray[i]             = cm;
        }

        this.dataModel.setColMetaArray(colArray);

    }

    public List<Item> getColumnDescriptors() {
        return this.columnDescriptors;
    }

    //-------------------------------------------------------------------------------------------------------------------

    public void setRawJSONData(String jsonData) {
        this.rawJSONData = jsonData;
    }

    protected JSONArray     parsedJSONData;
    public static final int getData              = 0;
    public static final int parseJava            = 1;
    public static final int makeModelUsingJava   = 2;

    public static final int parseNative          = 11;
    public static final int makeModelUsingNative = 12;
    public int              next;

    public boolean loopSetData() {
        switch (next) {
           case getData :
               sendProgress("Parsing data received. One moment please...");

               if (useNative) {
                   next = parseNative;
               } else {
                   next = parseJava;
               }

               return true;                                                         // continue

           case parseJava :
               parsedJSONData = (JSONArray) JSONParser.parseStrict(rawJSONData);    // all strings
               dataModel.setDesignDataJava(parsedJSONData);
               initColumnTypeFromJava(parsedJSONData);

               //---- now initialize makeDataModel params
               this.nextRowIndex    = 0;    // initialize the index
               this.numberOfRows    = parsedJSONData.size();
               this.numberOfColumns = this.dataModel.getColMetaArray().length;
               this.dataModel.setData(new Object[numberOfRows][numberOfColumns]);
               next = makeModelUsingJava;

               return true;                 // continue

           case makeModelUsingJava :

               // break up makeDataModelFromJava(parsedJSONData);
               boolean keepLoopGoing = makeDataModelFromJava();
               next = (keepLoopGoing ? makeModelUsingJava : getData);    // reset

               return keepLoopGoing;

           case parseNative :

               JavaScriptObject nativeJSData = nativeParse(rawJSONData);
               dataModel.setDesignDataNative(nativeJSData);
               initColumnTypeFromNative();

               //---- now initialize makeDataModel params
               this.nextRowIndex    = 0;    // initialize the index
               this.numberOfRows    = nativeLength(nativeJSData);
               this.numberOfColumns = this.dataModel.getColMetaArray().length;
               this.dataModel.setData(new Object[numberOfRows][numberOfColumns]);
               next = makeModelUsingNative;

               return true;

           case makeModelUsingNative :

               //TODO implement notifications
               boolean keepLoopGoing2 = createDataModelFromNative();
               next = (keepLoopGoing2 ? makeModelUsingNative : getData);    // reset

               return keepLoopGoing2;

           default :
               sendError("Case " + next + " not implemented.");

               return false;

        }

    }

    protected void initColumnTypeFromNative() {

        JavaScriptObject nativeJSData = dataModel.getDesignDataNative();
        sendProgress("Initializing the column information based on the data received...");
        int numberOfRows    = nativeLength(nativeJSData);
        int numberOfColumns = this.dataModel.getColMetaArray().length;

        for (int colModelIndex = 0; colModelIndex < numberOfColumns; colModelIndex++) {

            int     testDataRows = 0;
            ColMeta colMeta      = this.dataModel.getColMetaArray()[colModelIndex];
            int     colPos       = colMeta.getColPos();
            for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {

                JavaScriptObject row = getArray(nativeJSData, rowIndex);
                String           val = get(row, colPos);
                if (val != null && val.length() > 0) {
                    testDataRows += 1;

                    if ((colMeta.getType() == TYPE.STRING)) {
                        break;    // get out, no need to do more - a string cannot become a number again
                    } else {

                        // test for numeric in the first 3 rows if it's not already pegged as string
                        try {
                            double d = Double.parseDouble(val);
                            colMeta.setType(TYPE.NUMBER);
                        } catch (Exception ex) {
                            colMeta.setType(TYPE.STRING);
                        }

                    }

                    if (testDataRows >= 3) {
                        break;    // get out of the for loop - we have enough data points
                    }
                }                 // if (val != null && val.length() > 0)
            }                     // for i
        }                         // for j
    }

    protected boolean createDataModelFromNative() {

        JavaScriptObject nativeJSData = dataModel.getDesignDataNative();

        while (this.nextRowIndex < numberOfRows) {

            JavaScriptObject row = getArray(nativeJSData, this.nextRowIndex);

            for (int colModelIndex = 0; colModelIndex < numberOfColumns; colModelIndex++) {
                ColMeta colMeta = this.dataModel.getColMetaArray()[colModelIndex];
                int     colPos  = colMeta.getColPos();

                Object  x       = null;
                if (colMeta.type == TYPE.NUMBER) {
                    x = getDouble(row, colPos);
                } else {
                    x = get(row, colPos);
                }

                this.dataModel.getData()[this.nextRowIndex][colModelIndex] = x;

            }    // for j (columns)

            this.nextRowIndex++;

            if (this.nextRowIndex % 10 == 0) {

                // every 10 rows
                sendProgress("Processed " + this.nextRowIndex + " of " + this.numberOfRows + " rows.", this.numberOfRows, this.nextRowIndex , "dataLoopPhase" );
                
                return true;    //stay in the loop
            }
        }                       // while nextRowIndex

        sendProgress("All " + numberOfRows + " data rows have been processed.");

        return false;    // loop done
    }

    protected void obsolete_createDataModelFromNative() {

        ///!!! DO NOT USE YET- VERY UNSTABLE  AT THIS TIME8777
        JavaScriptObject nativeJSData    = dataModel.getDesignDataNative();
        int              numberOfRows    = nativeLength(nativeJSData);
        int              numberOfColumns = this.dataModel.getColMetaArray().length;
        this.dataModel.setData(new Object[numberOfRows][numberOfColumns]);

        for (int colModelIndex = 0; colModelIndex < numberOfColumns; colModelIndex++) {
            ColMeta colMeta = this.dataModel.getColMetaArray()[colModelIndex];
            int     colPos  = colMeta.getColPos();

            for (int i = 0; i < numberOfRows; i++) {
                if (i % 10 == 0) {

                    // progress every 10 records
                    sendProgress(
                        "Converting data to proper format. Working on column " + (colModelIndex + 1) + " of " + numberOfColumns + ". " + i + " of " + numberOfRows + " rows processed so far...");
                }

                JavaScriptObject row = getArray(nativeJSData, i);

                Object           x   = null;
                if (colMeta.type == TYPE.NUMBER) {
                    x = getDouble(row, colPos);
                } else {
                    x = get(row, colPos);
                }

                this.dataModel.getData()[i][colModelIndex] = x;
            }    // for i ( rows)
        }        // for j (columns)

        sendProgress("Data conversion complete.");
    }

    public static native String get(JavaScriptObject jsArray, int index)    /*-{
               var v = jsArray[index];
               return v;
       }-*/
    ;

    public static native double getDouble(JavaScriptObject jsArray, int index)                                                                           /*-{
                                                                                      var v = jsArray[index];

                                                                                      var retVal = $wnd.parseFloat(v);
                                                                                      if ( $wnd.isNaN( retVal) ){
                                                                                              retVal = 0;
                                                                                      }
                                                                                      return retVal;
                                                                              }-*/
    ;

    public static native JavaScriptObject getArray(JavaScriptObject jsArray, int index)    /*-{
               var v = jsArray[index];
               return v;
       }-*/
    ;

    public static native JavaScriptObject nativeParse(String input)    /*-{
               var v = $wnd.itgp.fromString(input);
               return v;
       }-*/
    ;

    public static native int nativeLength(JavaScriptObject jsArray)    /*-{
               var v = jsArray.length;
               return v;
       }-*/
    ;

    //-------------------------------------------------------------------------------------------------------------------

    protected void initColumnTypeFromJava(JSONArray parsedJSONData) {

        int numberOfRows    = parsedJSONData.size();
        int numberOfColumns = this.dataModel.getColMetaArray().length;

        for (int colModelIndex = 0; colModelIndex < numberOfColumns; colModelIndex++) {

            int     testDataRows = 0;
            ColMeta colMeta      = this.dataModel.getColMetaArray()[colModelIndex];
            int     colPos       = colMeta.getColPos();
            for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
                JSONValue  cell         = ((JSONArray) parsedJSONData.get(rowIndex)).get(colPos);

                JSONString cellAsString = cell.isString();
                if (cellAsString == null) {
                    ServerComm.alert("row " + rowIndex + " col " + colPos + " : [" + cell.toString() + "] is not a String! This circumstance contradicts the design parameters given!");

                    return;
                }

                String val = cellAsString.stringValue();

                if (val != null && val.length() > 0) {
                    testDataRows += 1;

                    if ((colMeta.getType() == TYPE.STRING)) {
                        break;    // get out, no need to do more - a string cannot become a number again
                    } else {

                        // test for numeric in the first 3 rows if it's not already pegged as string
                        try {
                            double d = Double.parseDouble(val);
                            colMeta.setType(TYPE.NUMBER);
                        } catch (Exception ex) {
                            colMeta.setType(TYPE.STRING);
                        }

                    }

                    if (testDataRows >= 3) {
                        break;    // get out of the for loop - we have enough data points
                    }
                }                 // if (val != null && val.length() > 0) 
            }                     // for i
        }                         // for j
    }

    protected boolean makeDataModelFromJava() {

        JSONArray parsedJSONData = dataModel.getDesignDataJava();
        while (this.nextRowIndex < numberOfRows) {
            JSONArray row = (JSONArray) parsedJSONData.get(this.nextRowIndex);

            for (int colModelIndex = 0; colModelIndex < this.numberOfColumns; colModelIndex++) {
                ColMeta    colMeta    = this.dataModel.getColMetaArray()[colModelIndex];
                int        colPos     = colMeta.getColPos();

                JSONString cell       = (JSONString) row.get(colPos);    // everything is a string
                String     value      = cell.stringValue();
                Object     modelValue = null;                            // can only be String or Double
                if (colMeta.type == TYPE.NUMBER) {
                    try {
                        modelValue = Double.parseDouble(value);          // Double instance
                    } catch (Exception ex) {
                        modelValue = Double.valueOf(0);                  // 0 but as Double
                    }
                } else {
                    modelValue = value;                                  // direct string
                }

                this.dataModel.getData()[this.nextRowIndex][colModelIndex] = modelValue;
            }                                                            // for j (columns)

            this.nextRowIndex++;

            if (this.nextRowIndex % 10 == 0) {

                // every 10 rows
                sendProgress("Processed " + this.nextRowIndex + " of " + this.numberOfRows + " rows.");

                return true;
            }

        }    // while (nextRowIndex)

        sendProgress("All " + numberOfRows + " data rows have been processed.");

        return false;
    }

//
//  protected void obsolete_makeDataModelFromJava(JSONArray parsedJSONData) {
//      int numberOfRows    = parsedJSONData.size();
//      int numberOfColumns = this.dataModel.getColMetaArray().length;
//      this.dataModel.setRawJSONData(new Object[numberOfRows][numberOfColumns]);
//
//      for (int colModelIndex = 0; colModelIndex < numberOfColumns; colModelIndex++) {
//          ColMeta colMeta = this.dataModel.getColMetaArray()[colModelIndex];
//          int     colPos  = colMeta.getColPos();
//
//          for (int i = 0; i < numberOfRows; i++) {
//              JSONArray  row        = (JSONArray) parsedJSONData.get(i);
//
//              JSONString cell       = (JSONString) row.get(colPos);    // everything is a string
//              String     value      = cell.stringValue();
//              Object     modelValue = null;                            // can only be String or Double
//              if (colMeta.type == TYPE.NUMBER) {
//                  try {
//                      modelValue = Double.parseDouble(value);          // Double instance
//                  } catch (Exception ex) {
//                      modelValue = Double.valueOf(0);                  // 0 but as Double
//                  }
//              } else {
//                  modelValue = value;                                  // direct string
//              }
//
//              this.dataModel.getData()[i][colModelIndex] = modelValue;
//          }                                                            // for i ( rows)
//      }                                                                // for j (columns)
//  }

    //----------------------------------------------------------------

    
    public void sendProgressInit(){
        NotificationEvent evt = NotificationEvent.createInitEvent();
        progressReporting.fireEvent(evt);
    }
    
    public void sendProgress(String progressReport) {
        if (progressReport != null) {
            NotificationEvent evt = new NotificationEvent(progressReport);
            progressReporting.fireEvent(evt);
        }
    }
      public void sendProgress(String progressReport, String phase) {
        if (progressReport != null) {
            NotificationEvent evt = new NotificationEvent(progressReport, phase);
            progressReporting.fireEvent(evt);
        }
    }  
    public void sendProgress(String progressReport, int max, int current, String phase) {
        if (progressReport != null) {
            ProgressBarNotificationEvent evt = new ProgressBarNotificationEvent(progressReport, max, current,phase);
            progressReporting.fireEvent(evt);
        }
    }  
    
    public void sendProgressDone() {
        progressReporting.fireEvent(createDoneEvent());
    }

    public void sendError(String errorReport) {
        if (errorReport != null) {
            NotificationEvent evt = new NotificationEvent(errorReport);
            errorReporting.fireEvent(evt);
        }
    }

    public FilterCompleteListenerGroup getFilterListeners() {
        return filterListeners;
    }

    public void addFilterCompleteListener(FilterCompleteListener listener) {
        filterListeners.add(listener);
    }

}
