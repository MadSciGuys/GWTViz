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

import java.util.Comparator;

/**
 * Compares Doubles and Strings in particular. If none of these it does a toString comparison as a last resort.
 * <p>
 * @author David Pociu / May 2015 
 */
public class SeriesComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null) {
            return -1;
        }

        if (o2 == null) {
            return +1;
        }

        if ((o1 instanceof String) && (o2 instanceof String)) {
            return ((String) o1).compareTo((String) o2);
        }

        if ((o1 instanceof Double) && o2 instanceof Double) {
            return ((Double) o1).compareTo((Double) o2);
        }

        if (o1 instanceof Double && (!(o2 instanceof Double))) {
            return -1;    // Double < String
        }

        if (o2 instanceof Double && (!(o1 instanceof Double))) {
            return 1;    // String > Double
        }

        return ((o1.toString()).compareTo(o2.toString()));
    }

}
