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
package com.itgp.gwtviz.client.ui;

import com.itgp.gwtviz.client.ui.runtime.event.*;
import com.itgp.gwtviz.client.ui.runtime.event.NotificationEvent.EVENT_TYPE;
import com.itgp.gwtviz.client.ui.runtime.event.progressBar.ProgressBarNotificationEvent;

public class ProgressBarNotificationListener implements ProgressReportListener {

    ProgressBar progressBar;
    
    @Override
    public void progressReported(NotificationEvent evt){        EVENT_TYPE type = evt.getType();
        switch (type) {
           case init :
               initializeProgressBar();

               break;

           case progress :
               if ( evt instanceof ProgressBarNotificationEvent){
                   progressBarNotification((ProgressBarNotificationEvent) evt );
               } else {
                   genericNotification(evt);
               }
               break;

           case done :
               disposeOfProgressBar();
               break;
        }
    }
    
    
    
    public void initializeProgressBar(){
        // progressBar = initialize
        
    }
    
    public void genericNotification(NotificationEvent evt){
        String phase = evt.getPhase();
        String message = evt.getMessage();
        
    }
    
    public void progressBarNotification(ProgressBarNotificationEvent evt){
        int current = evt.getCurrent();
        int max = evt.getMax();
        String phase = evt.getPhase();
        String message = evt.getMessage();
        
        
    }
    
    public void disposeOfProgressBar(){
        
    }
    
}
