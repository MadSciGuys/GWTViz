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
 *
 * Base class for easy implementation of listeners and events inside different classes. <p>
 *
 * This class takes care of registering listeners and calling events with the developer only having to implement one method: {@link #eventMethodImplementation(java.lang.Object, java.lang.Object) } <p>
 *
 * The implementation also contains static methods to send an event to multiple {@link ListenerGroup}s
 *
 * @author [Warp]
 */
public abstract class ListenerGroup<LISTENER, EVENT> {

    protected LinkedHashSet<LISTENER> listeners = new LinkedHashSet<LISTENER>();

    public void add(LISTENER listener) {
        listeners.add(listener);
    }

    /**
     *
     * @param evt
     * @return true if the listener was found and removed, false if the listener
     * was not in the set
     */
    public boolean remove(LISTENER listener) {
        return listeners.remove(listener);
    }

    
    public void removeAll(){
        listeners.clear();
    }
    /**
     *
     * @return local listener list
     */
    public Set<LISTENER> listeners() {
        return listeners;
    }

    public boolean isEmpty() {
        return listeners.isEmpty();
    }

    public void fireEvent(EVENT evt) {
        if (evt != null && listeners.size() > 0) {
            for (Iterator<LISTENER> it = listeners.iterator(); it.hasNext(); ) {
                LISTENER listener = it.next();
                this.eventMethodImplementation(listener, evt);
            }
        }
    }

    /**
     * Implement the method to be called upon firing the event
     *
     * @param listener
     * @param evt the event to be passed to the listener
     */
    protected abstract void eventMethodImplementation(LISTENER listener, EVENT evt);

    public static <EVENT> void fireEvent(EVENT evt, ListenerGroup<? ,EVENT> listeners) {
        if (listeners != null && evt != null) {
            listeners.fireEvent(evt);
        }
    }

    public static <EVENT> void fireEvent(EVENT evt, ListenerGroup<? ,EVENT>[] listenersArray) {
        if (listenersArray != null && evt != null) {
            int n = listenersArray.length;
            for (int i = 0; i < n; i++) {
                ListenerGroup listeners = listenersArray[i];
                if (listeners != null) {
                    listeners.fireEvent(evt);
                }
            }
        }
    }
}
