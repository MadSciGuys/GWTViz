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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.http.client.*;
import com.google.gwt.safehtml.shared.UriUtils;
import com.itgp.gwtviz.client.comm.ServerComm;
import com.itgp.gwtviz.client.ui.*;
import com.itgp.gwtviz.shared.gconfig.*;
import com.itgp.gwtviz.shared.model.*;

public class RuntimeEntry extends RuntimeEntryBase {

    RuntimeEntry_Part2         re2;
    protected static final int start                          = 0;
    protected static final int notify_load_config             = 1;
    protected static final int notify_requesting_data         = 4;
    protected static final int finish                         = 100;

    int                        next;
    LocalRepeatingCommand      cmd;

    public static void start(String configText, RuntimeDataV1 run) {

        RuntimeEntry re     = new RuntimeEntry(run);

        // Register progress/error listener that updates the <div id="process_info"></div> element
        DivNotificationListener listener = new DivNotificationListener();
        run.progressReporting.add(listener);
        run.errorReporting.add(listener);
        ProgressBarNotificationListener pLis = new ProgressBarNotificationListener();
        run.progressReporting.add(pLis);

        re.run.configText = configText;

        re.re2              = new RuntimeEntry_Part2(run);

        re.next             = 0;

        Scheduler.get().scheduleFixedDelay(re.cmd, 20);    // those 20 ms are crucial to the browser having time to do DOM work. Without them sometimes updates don't

    }

    public RuntimeEntry(RuntimeDataV1 run) {
        super(run);
        this.cmd = new LocalRepeatingCommand();
    }

    protected class LocalRepeatingCommand implements RepeatingCommand {
        public boolean execute() {
            boolean loop_again = true;
            switch (next) {
               case start :
                   sendProgressInit();
                   sendProgress("Loading configuration. One moment please...");
                   next = notify_load_config;

                   break;

               case notify_load_config :
                   sendProgress("Loading configuration. One moment please...");

                   next = notify_requesting_data;

                   break;
               case notify_requesting_data :

                   run.config = (GraphConfigImpl_1) GraphConfigUtility.graphConfigImplfromJson(run.configText);
                   initFromConfig();
                   sendProgress("Requesting data from: " + MainDesktop.getMRouteURL());
                   next = finish;

                   break;

               case finish :
                   try {
                       requestData(); // async
                       sendProgress("Request for data in progress from: " + MainDesktop.getMRouteURL() );
                   } finally {
                       next       = -1;
                       loop_again = false;    // stop the loop
                   }
                   break;
               default :
                   sendError("Unknown next label : '" + next + "'");
                   loop_again = false;
                   break;
            }

            return loop_again;
        }
    }    // LocalRepeatingCommand


    public void requestData() {

        String         columnURL  = ItemUtil.makeColumnURL(run.getColumnDescriptors()) + "";
        final String   url        = MainDesktop.getMRouteURL() + ServerComm.getData + run.fileID + "/" + columnURL;
        String         urlEncoded = UriUtils.encode(url);
        RequestBuilder builder    = new RequestBuilder(RequestBuilder.GET, urlEncoded);
        try {
            builder.sendRequest("", new RequestCallback() {
                public void onError(Request request, Throwable e) {
                    sendError("The following error was returned while attempting to get data from " + url + "\n\n" + e.getMessage());

                    return;
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        String dataArray = response.getText();
                        run.dataArray = dataArray;
                        RuntimeEntry_Part2 re2 = new RuntimeEntry_Part2(run);
                        re2.runtimeLoop();
                    } else {
                        sendError("Error processing the data request.\n\nGot response code: " + response.getStatusCode());

                        return;
                    }
                }

            });
        } catch (RequestException e) {
            sendError("The following exception occurred while attempting to get data from " + url + "\n\n" + e.getMessage());
        }
    }

}
