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
package com.itgp.gwtviz.client.ui.runtime.decorator;

/**
 * Generic event which all client-side events should extend (but not mandatory). Each extending event should
 * define the position in the <code>parameters</code> for each custom parameter. <p>
 * 
 * This base event does not define any parameters and initializes the <code>parameters</code> array to an 
 * empty array event if <code>null</code> is passed in.<p>
 *
 * The constructor also timestamps the event with the current system time.
 *
 * @author InsiTech Inc.
 * @author David Pociu
 * @since 5.4
 */
public class GenericEvent extends java.util.EventObject {
    public     final transient     Object[]    parameters  ;
    public     final               long        timestamp   ;
    
    /** Creates a new instance of GenericEvent */
    public GenericEvent(Object source, Object[] params ) {
        super(source);
        parameters = (params == null ? new Object[0] : params);
        timestamp = System.currentTimeMillis();
    }
    
}
