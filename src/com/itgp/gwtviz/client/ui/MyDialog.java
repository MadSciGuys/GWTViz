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
package com.itgp.gwtviz.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MyDialog extends DialogBox {

    public MyDialog(Widget w) {
       // Set the dialog box's caption.
       setText("My First Dialog");

       // Enable animation.
       setAnimationEnabled(true);

       // Enable glass background.
       setGlassEnabled(true);

       // DialogBox is a SimplePanel, so you have to set its widget 
       // property to whatever you want its contents to be.
       Button ok = new Button("OK");
       ok.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
             MyDialog.this.hide();
          }
       });

       Label label = new Label("This is a simple dialog box.");

       VerticalPanel panel = new VerticalPanel();
       panel.setBorderWidth(5);
       panel.setHeight("100");
       panel.setWidth("300");
       panel.setSpacing(10);
       panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

       panel.add(w);
       panel.add(label);
       panel.add(ok);

       setWidget(panel);
    }
 }