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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.itgp.gwtviz.client.ui.runtime.event.*;
import com.itgp.gwtviz.client.ui.runtime.event.NotificationEvent.EVENT_TYPE;

/**
 * <p>
 *
 * @author Warp
 */
public class DivNotificationListener implements ProgressReportListener, ErrorReportListener {

    public static String progressID = "process_info";

    static enum LAST_STYLE { None, Status, Error }

    ;

    Element    status     = DOM.getElementById(progressID);

    LAST_STYLE statusLast = LAST_STYLE.None;

    public DivNotificationListener() {
        super();

    }

    @Override
    public void progressReported(NotificationEvent evt) {
        EVENT_TYPE type = evt.getType();
        switch (type) {
           case init :
               displayMessage("");

               break;

           case progress :
               displayMessage(evt.getMessage());

               break;

           case done :
               displayMessage("");
        }
    }

    @Override
    public void errorReported(NotificationEvent evt) {
        displayError(evt.getMessage());
    }

    protected void toErrorStyle() {

        status.setClassName("processError");
        statusLast = LAST_STYLE.Error;
    }

    protected void toStatusStyle() {

        status.setClassName("processStatus");
        statusLast = LAST_STYLE.Status;

    }

    protected void displayError(final String error) {

        if (status == null)
            return;

        if (statusLast != LAST_STYLE.Error) {
            this.toErrorStyle();
        }

        status.setInnerText(error);

    }

    protected void displayMessage(final String msg) {
        if (status == null)
            return;

        if (statusLast != LAST_STYLE.Status) {
            this.toStatusStyle();
        }

        status.setInnerText(msg);
    }

}
