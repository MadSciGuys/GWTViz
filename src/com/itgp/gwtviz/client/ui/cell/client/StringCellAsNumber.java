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
package com.itgp.gwtviz.client.ui.cell.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;

public class StringCellAsNumber extends AbstractCell<String> {

	/**
	 * The {@link NumberFormat} used to render the number.
	 */
	private final NumberFormat format;

	/**
	 * The {@link SafeHtmlRenderer} used to render the formatted number as HTML.
	 */
	private final SafeHtmlRenderer<String> renderer;

	/**
	 * Construct a new {@link NumberCell} using decimal format and a {@link SimpleSafeHtmlRenderer}.
	 */
	public StringCellAsNumber() {
		this(NumberFormat.getDecimalFormat(), SimpleSafeHtmlRenderer.getInstance());
	}

	/**
	 * Construct a new {@link NumberCell} using the given {@link NumberFormat} and a {@link SimpleSafeHtmlRenderer}.
	 *
	 * @param format
	 *            the {@link NumberFormat} used to render the number
	 */
	public StringCellAsNumber(NumberFormat format) {
		this(format, SimpleSafeHtmlRenderer.getInstance());
	}

	/**
	 * Construct a new {@link NumberCell} using decimal format and the given {@link SafeHtmlRenderer}.
	 *
	 * @param renderer
	 *            the {@link SafeHtmlRenderer} used to render the formatted number as HTML
	 */
	public StringCellAsNumber(SafeHtmlRenderer<String> renderer) {
		this(NumberFormat.getDecimalFormat(), renderer);
	}

	/**
	 * Construct a new {@link NumberCell} using the given {@link NumberFormat} and a {@link SafeHtmlRenderer}.
	 *
	 * @param format
	 *            the {@link NumberFormat} used to render the number
	 * @param renderer
	 *            the {@link SafeHtmlRenderer} used to render the formatted number as HTML
	 */
	public StringCellAsNumber(NumberFormat format, SafeHtmlRenderer<String> renderer) {
		if (format == null) {
			throw new IllegalArgumentException("format == null");
		}
		if (renderer == null) {
			throw new IllegalArgumentException("renderer == null");
		}
		this.format = format;
		this.renderer = renderer;
	}

	// @Override
	// public void render(Context context, Number value, SafeHtmlBuilder sb) {
	// if (value != null) {
	// sb.append(renderer.render(format.format(value)));
	// }
	// }

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
		if (value != null) {
			double d = 0;
			try {
				value = value.replaceAll("[^\\d.]", "");
				d = Double.parseDouble(value);
			} catch (NumberFormatException e) {
				return;
			}

			sb.append(renderer.render(format.format(d)));
		}
	}
}
