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
package com.itgp.gwtviz.client.ui.fixes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.field.DualListFieldDefaultAppearance;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;

public class MyDualListFieldAppearance extends DualListFieldDefaultAppearance {
	public interface MyDualListFieldResources extends DualListFieldResources {
		@Source("MyDualListField.css")
		@Override
		MyStyle css();
		
        @Source( "../../image/allToRight.png")
        @Override
        ImageResource allRight();
        
        @Source( "../../image/right.png")
        @Override
        ImageResource right();
        
        @Source( "../../image/allToLeft.png")
        @Override
        ImageResource allLeft();
        
        
        @Source( "../../image/left.png")
        @Override
        ImageResource left();
       
	}

	public interface MyStyle extends DualListFieldStyle {
	}

	public MyDualListFieldAppearance() {
		super(GWT.<DualListFieldResources> create(MyDualListFieldResources.class));
	
	}
	

	@Override
	public IconConfig up() {
		// TODO Auto-generated method stub
		IconConfig config = super.up();
		return config;
	}
}