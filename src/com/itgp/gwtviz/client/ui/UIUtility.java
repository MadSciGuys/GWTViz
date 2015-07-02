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

import com.itgp.gwtviz.shared.model.*;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import java.util.*;

public class UIUtility {

	public static enum Charts {
		Bar_Chart, Pie_Chart, Bar_Chart_With_Stacked_Columns, Bar_Chart_With_Grouped_Columns, Line_Chart, Scatter_Plot_Chart, Multivariate_Scatter_Plot_Chart,  Heat_Map_Chart
	};

	private static Container mainContainer;

	public static void setMainDesktop(Container _mainContainer) {
		mainContainer = _mainContainer;
	}

	public static void protectMainDesktop(boolean protect) {
		if (mainContainer != null) {
			if (protect) {
				mainContainer.mask();
			} else {
				mainContainer.unmask();
			}
		}
	}

	public static ArrayList<ChartDesc> getChartsList() {
		ArrayList<ChartDesc> list = new ArrayList<ChartDesc>();
		list.add(convertChartEnum(Charts.Bar_Chart));
		list.add(convertChartEnum(Charts.Pie_Chart));
		list.add(convertChartEnum(Charts.Bar_Chart_With_Stacked_Columns));
		list.add(convertChartEnum(Charts.Bar_Chart_With_Grouped_Columns));
		list.add(convertChartEnum(Charts.Line_Chart));
		list.add(convertChartEnum(Charts.Scatter_Plot_Chart));
		list.add(convertChartEnum(Charts.Multivariate_Scatter_Plot_Chart));
		// list.add(convertChartEnum(Charts.Heat_Map_Chart));
		return list;
	}

	public static ArrayList<String> getDegreeRotation() {

		ArrayList<String> degrees = new ArrayList<String>();
		// Character degreeChar = 176;
		final String DEGREE = "\u00b0";
		degrees.add("0" + DEGREE);
		degrees.add("90" + DEGREE);
		degrees.add("270" + DEGREE);
		degrees.add("45" + DEGREE);
		degrees.add("225" + DEGREE);
		degrees.add("60" + DEGREE);
		degrees.add("240" + DEGREE);

		// '0': '0&deg;(Normal)',
		// '90': '90&deg;',
		// '-90': '270 &deg;',
		// '45': '45&deg;',
		// '-45': '225&deg;',
		// '60': '60&deg;',
		// '-60': '240&deg;'
		return degrees;
	}

	private static ChartDesc convertChartEnum(Charts charts) {
		ChartDesc cd = new ChartDesc(charts.name(), stripUnderscores(charts.name()));
		return cd;
	}

	public static Charts getChartEnum(String text) {
		String enumName = text.replace(" ", "_");

		Charts validEnum = Charts.Bar_Chart;
		try {
			validEnum = Charts.valueOf(enumName);
		} catch (Exception e) {
			return null;

		}
		return validEnum;
	}
	
	public static String getChartDisplayText(String enumChartName) {
		String dropDownName = convertChartEnum(Charts.Bar_Chart).getName();
		Charts validEnum = Charts.Bar_Chart;
		try {
			validEnum = Charts.valueOf(enumChartName);
			dropDownName = convertChartEnum(validEnum).getName();
		} catch (Exception e) {

		}
		
		return dropDownName;
	}
	

	private static String stripUnderscores(String s) {
		return s.replace("_", " ");
	}

	public static void reloadComboBox(SimpleComboBox comboBox, List<String> values) {
		comboBox.clear();
		for (String value : values) {
			comboBox.add(value);
		}
	}

	public static String makeHTMLTable(List<Item> items) {
		// http://www.lonniebest.com/formatcss/
		// http://tablestyler.com/#
		String headS = "<thead><tr>";
		String headE = "</tr></thead>";
		String trS = "<tr>";
		String trSAlt = "<tr class=\"alt\">";
		String trE = "</tr>";
		String tdS = "<td>";
		String tdE = "</td>";
		String thS = "<th>";
		String thE = "</th>";
		String t1 = "<table style=\"width:100%\">";
		String divS = "<div class=\"gwtvizgridpreview\">";
		String divE = "</div>";

		ArrayList<Integer> counters = new ArrayList<Integer>();

		StringBuffer sb = new StringBuffer();
		sb.append(divS);
		sb.append(t1);
		sb.append(headS);
		for (int i = 0; i < items.size(); i++) {
			counters.add(1);
			sb.append(thS);
			sb.append(items.get(i).getValue());
			sb.append(thE);
		}
		sb.append(headE);
		boolean done = false;

		int j = 0;
		while (!done) {
			if (j % 2 == 0) {
				sb.append(trS);
			} else {
				sb.append(trSAlt);
			}
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getData().size() > j) {
					sb.append(tdS);
					sb.append(items.get(i).getData().get(j).getValue());
					sb.append(tdE);
				} else {
					counters.set(i, 0);
					sb.append(tdS);
					sb.append(tdE);
				}
			}
			sb.append(trE);
			j++;
			done = areWeDone(counters);
		}
		sb.append("</table");
		sb.append(divE);
		return sb.toString();
	}
	
	
	public static String makeCSV(List<Item> items) {
		String _CR_NL = "\r\n";

		ArrayList<Integer> counters = new ArrayList<Integer>();

		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < items.size(); i++) {
			counters.add(1);
			sb.append(items.get(i).getValue());
			sb.append(",");
		}
		if(items.size()>1){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append(_CR_NL);
		boolean done = false;

		int j = 0;
		while (!done) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getData().size() > j) {
					sb.append(items.get(i).getData().get(j).getValue());
					sb.append(",");
				} else {
					counters.set(i, 0);
					//sb.append(",");
				}
			}
			j++;
			if(sb.substring(sb.length()-1).equals(",") ){
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append(_CR_NL);
			done = areWeDone(counters);
		}

		return sb.toString();
	}

	private static boolean areWeDone(ArrayList<Integer> counters) {
		boolean areWeDone = false;
		int counter = 0;
		for (Integer _int : counters) {
			counter = counter + _int;
		}
		if (counter == 0) {
			areWeDone = true;
		}
		return areWeDone;
	}

}
