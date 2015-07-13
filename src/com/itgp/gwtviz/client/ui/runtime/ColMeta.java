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

/**
 * <p>
 * @author Warp
 */
public class ColMeta {

    public static enum TYPE { UNKNOWN, STRING, NUMBER }

    ;
    public String name;
    public int    position;
    public TYPE   type = TYPE.UNKNOWN;
    public String label;
    public int positionAtDesignTime = -1;
    public RuntimeDataV1 run;

    private ColMeta() {}

    public static ColMeta create(int position, String name) {
        ColMeta x = new ColMeta();
        x.position = position;
        x.name     = name;

        return x;
    }

    public void setType(TYPE t) {
        if (t == TYPE.UNKNOWN) {
            return;    // set UNKNOWN directly at creation time, not later (or set it directly on the field)
        }

        if (this.type == TYPE.UNKNOWN) {
            this.type = t;

            return;
        }

        if (this.type == TYPE.STRING) {

            // do nothing because once a type is string for one row it cannot be ever number, and it makes no sense to set it to String again.
        }

        if (this.type == TYPE.NUMBER) {
            if (t == TYPE.STRING) {
                this.type = t;    // was numeric but one of the values is string so bye-bye
            } else if (t == TYPE.NUMBER) {
                return;
            }

        }

    }

    public TYPE getType() {
        return this.type;
    }

    public String getLabel(){
        if ( this.label != null){
            return this.label;
        }
        return this.name;
    }
    
    public int getColPos(){
        if ( run.designTime ){
            // position in the complete data model
            return this.positionAtDesignTime;
        } else {
            // runtime
            // position in the datamodel that only contains the filter Columns
            return this.position;
        }
                
    }
    
    @Override
    public String toString() {
        return "ColMeta{" + "name=" + name + ", position=" + position + ", type=" + type + '}';
    }

}
