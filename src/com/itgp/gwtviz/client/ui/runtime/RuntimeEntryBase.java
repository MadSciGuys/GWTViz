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

import com.itgp.gwtviz.client.ui.MainDesktop;
import com.itgp.gwtviz.shared.model.ItemUtil;
import com.itgp.gwtviz.shared.model.MRoute.File;
import java.util.List;

/**
 * <p>
 * @author Warp
 */
public abstract class RuntimeEntryBase {

    public RuntimeEntryBase(RuntimeDataV1 run) {
        this.run   = run;
//        this.param = param;
    }

//    static class LocalParams {
//        String            configText;
//        String            dataArray;
//        AbstractChartXYV1 chartXY;
//        ChartV1           chartToDisplay;
//    }
//
//
//    ;

    protected RuntimeDataV1 run;
//    protected LocalParams param;

    /**
     *
     * @param fileName
     * @return -1 if file not found or the index of the file in the MRouteDocs from MainDesktop
     */
    public static long getFileID(String fileName) {
        long       fileID = -1;
        List<File> list   = MainDesktop.getMRouteDocs().getFiles();
        for (File f : list) {
            String fName = f.getMAlias();
            if (fName.equals(fileName)) {
                fileID = f.getMIndex();

                break;    // get out of the for loop
            }
        }

        return fileID;
    }

    public void initFromConfig() {
        sendProgressInit();
        run.setColumnDescriptors(ItemUtil.convert(run.config.getFilterColumns()));
        String fileName = run.config.getDataSourceName();

        run.fileID = getFileID(fileName);

        if (run.fileID == -1) {
            sendError("Could not find an index for file name '" + fileName + "'");
            sendProgressDone();
            return;
        }
    }

    public void sendProgressInit(){
        run.sendProgressInit();
    }
    public void sendProgress(String progressReport) {
        run.sendProgress(progressReport);
    }
      public void sendProgress(String progressReport, String phase) {
          run.sendProgress(progressReport, phase);
      }

    public void sendProgress(String progressReport, int max, int current, String phase) {
        run.sendProgress(progressReport, max, current, phase);
    }
    
    public void sendProgressDone() {
        run.sendProgressDone();
    }

    public void sendError(String errorReport) {
        run.sendError(errorReport);
    }
}
