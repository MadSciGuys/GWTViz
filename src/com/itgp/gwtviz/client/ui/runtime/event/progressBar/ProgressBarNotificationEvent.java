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
package com.itgp.gwtviz.client.ui.runtime.event.progressBar;

import com.itgp.gwtviz.client.ui.runtime.event.NotificationEvent;

/**
 <p>
 @author Warp
 */
public class ProgressBarNotificationEvent extends NotificationEvent{

    private int max;
    private int current;
    
    public ProgressBarNotificationEvent(String message, int max, int current){
        super(message);
        this.max=max;
        this.current=current;
    }

    public ProgressBarNotificationEvent(String message, int max, int current, String phase){
        super(message);
        this.phase=phase;
        this.max=max;
        this.current=current;
    }


    public int getMax(){
        return max;
    }

    public void setMax(int max){
        this.max=max;
    }

    public int getCurrent(){
        return current;
    }

    public void setCurrent(int current){
        this.current=current;
    }

    
    @Override
    public String toString(){
        return "ProgressBarNotificationEvent{" + super.toString() + ", max=" + max + ", current=" + current + '}';
    }
    
    
    
    
    
    
}
