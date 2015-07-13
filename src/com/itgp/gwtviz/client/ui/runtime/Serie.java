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

import java.util.*;

/**
 * <p>
 * @author Warp
 */
public class Serie {

    protected String             seriesName;
    protected String             seriesLabel;
    protected ColMeta            xCol;
    protected ColMeta            yCol;
    protected ColMeta            breakCol;
    protected Object             breakValue;
    protected ArrayList<Integer> rowList = new ArrayList<Integer>();
    protected ColMeta            scatterSizeCol;
    protected double			 minYValue;
    protected double			 maxYValue;
    protected boolean			 minYInitialized = false;
    protected boolean			 maxYInitialized = false;

    public Serie(String seriesName, String seriesLabel, ColMeta xCol, ColMeta yCol) {
        setSeriesName(seriesName);
        setSeriesLabel(seriesLabel);
        setXCol(xCol);
        setYCol(yCol);
    }

    public Serie(String seriesName, String seriesLabel, ColMeta xCol, ColMeta yCol, ColMeta breakCol, Object breakValue) {
        this(seriesName, seriesLabel, xCol, yCol);
        setBreakCol(breakCol);
        setBreakValue(breakValue);
    }

    public String getSeriesLabel() {
        return seriesLabel;
    }

    public void setSeriesLabel(String seriesLabel) {
        this.seriesLabel = seriesLabel;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesLabelEnhanced() {
        if (getBreakValue() != null) {
            String s = getSeriesLabel() + " [" + getBreakValue() + "]";

            return s;
        }

        return getSeriesLabel();
    }

    public ArrayList<Integer> getRowList() {
        return rowList;
    }

    public void setRowList(ArrayList<Integer> rowList) {
        this.rowList = rowList;
    }

    public ColMeta getXCol() {
        return xCol;
    }

    public void setXCol(ColMeta xCol) {
        this.xCol = xCol;
    }

    public ColMeta getYCol() {
        return yCol;
    }

    public void setYCol(ColMeta yCol) {
        this.yCol = yCol;
    }

    public ColMeta getBreakCol() {
        return breakCol;
    }

    public void setBreakCol(ColMeta breakCol) {
        this.breakCol = breakCol;
    }

    public Object getBreakValue() {
        return breakValue;
    }

    public void setBreakValue(Object breakValue) {
        this.breakValue = breakValue;
    }

//  --------------------------------------------    
    public int rowCount() {
        return rowList.size();
    }

    public void add(Integer e) {
        rowList.add(e);
    }

    public void add(Integer... rowArray) {
        for (int i = 0; i < rowArray.length; i++) {
            Integer rowNum = rowArray[i];
            rowList.add(rowNum);
        }
    }

    public void addAll(Collection<Integer> c) {
        rowList.addAll(c);
    }


    public ColMeta getScatterSizeCol(){
        return scatterSizeCol;
    }

    public void setScatterSizeCol(ColMeta scatterSizeCol){
        this.scatterSizeCol=scatterSizeCol;
    }
    
    public double getMinYValue(){
        return minYValue;
    }

    public void setMinYValue(double minYValue){
        this.minYValue=minYValue;
    }

    public double getMaxYValue(){
        return maxYValue;
    }

    public void setMaxYValue(double maxYValue){
        this.maxYValue=maxYValue;
    }

    public boolean isMinYInitialized(){
        return minYInitialized;
    }

    public void setMinYInitialized(boolean minYInitialized){
        this.minYInitialized=minYInitialized;
    }

    public boolean isMaxYInitialized(){
        return maxYInitialized;
    }

    public void setMaxYInitialized(boolean maxYInitialized){
        this.maxYInitialized=maxYInitialized;
    }


    @Override
    public String toString(){
        return "Serie{" + "seriesName=" + seriesName + ", seriesLabel=" + seriesLabel + ", xCol=" + xCol + ", yCol=" + yCol + ", breakCol=" + breakCol + ", breakValue=" + breakValue + ", scatterSizeCol=" + scatterSizeCol + '}';
    }

  
    

}
