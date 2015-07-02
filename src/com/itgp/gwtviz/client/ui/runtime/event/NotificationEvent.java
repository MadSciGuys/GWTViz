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
package com.itgp.gwtviz.client.ui.runtime.event;

import static com.itgp.gwtviz.client.ui.runtime.event.NotificationEvent.EVENT_TYPE.*;

/**
 * <p>
 * @author Warp
 */
public class NotificationEvent {

    public static enum EVENT_TYPE { init, progress, done }

    protected String     message;
    protected String     hiddenText;    // anything that should not display but the event should still carry
    protected EVENT_TYPE type = progress;
    protected String     phase;

    public NotificationEvent(String message) {
        this.message = message;
    }

    public NotificationEvent(String message, String phase) {
        this.message    = message;
        this.phase = phase;
    }

//  ----------------------------------------------------------------------------
    public static NotificationEvent create(String message) {
        return new NotificationEvent(message);
    }

    public static NotificationEvent create(String message, String hiddenText) {
        return new NotificationEvent(message, hiddenText);
    }

    public static NotificationEvent createDoneEvent() {
        NotificationEvent evt = new NotificationEvent("");
        evt.setType(done);

        return evt;
    }

    public static NotificationEvent createInitEvent() {
        NotificationEvent evt = new NotificationEvent("");
        evt.setType(init);

        return evt;
    }

//  ----------------------------------------------------------------------------
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return null or any addditional information that does not go in a message
     */
    public String getHiddenText() {
        return hiddenText;
    }

    public void setHiddenText(String hiddenText) {
        this.hiddenText = hiddenText;
    }

    public EVENT_TYPE getType() {
        return type;
    }

    public void setType(EVENT_TYPE type) {
        this.type = type;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {
        return "NotificationEvent{" + "message=" + message + ", hiddenText=" + hiddenText + ", type=" + type + ", phase=" + phase + '}';
    }

}
