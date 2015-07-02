/* 11global d3 */

/**
 * 
 */
// ----- Graph div ID ----
var ITGP_SVG_ID = 'chart';
// ------ Graph Types -----
var ITGP_CHART_TYPE_BAR = 'bar';
var ITGP_CHART_TYPE_DISCRETE_BAR = 'discreteBar';
var ITGP_CHART_TYPE_LINE = 'line';
var ITGP_CHART_TYPE_PIE = 'pie';
var ITGP_CHART_TYPE_DONUT = 'donut';
var ITGP_CHART_TYPE_SCATTER = 'scatter';
var ITGP_CHART_TYPE_MATRIXSCATTER = 'matrixScatter';

// -------------------------

var itgp = new function() {

	this.lastGraph = null;

	this.fromString = function(stringParam) {
		var jParam = JSON.parse(stringParam);
		return jParam;
	}

	this.toString = function(object) {
		var stringData = JSON.stringify(object);
		return stringData;
	}

	this.getLineChart = function(jParam) {
//		var chart = nv.models.lineChart().margin({
//			left : 100
//		}) // Adjust chart margins to give the x-axis some breathing room.
		
		var chart = nv.models.lineWithFocusChart().margin({
			left : 100
		}) // Adjust chart margins to give the x-axis some breathing room.

		try {
		chart.useInteractiveGuideline(true); // We want nice looking tooltips and a guideline!
		} catch(err){}
		
		chart.showLegend(true) // Show the legend, allowing users to turn
		// on/off line
		// series.
		try {
		chart.showYAxis(true) // Show the y-axis
		chart.showXAxis(true) // Show the x-axis
		} catch(err){}

		setupXYAxis(chart, jParam);

		// var d3zoom = d3.behavior.zoom();
		// d3zoom.on("zoom", this.redraw());

		return chart;
	}

	this.getMultiBarChart = function getMultiBarChart(jParam) {

		var chart = nv.models.multiBarChart();

		chart.reduceXTicks(true) // If 'false', every single x-axis tick
		// label will be rendered.
		// chart.rotateLabels(90) // Angle to rotate x-axis labels.
		chart.showControls(true) // Allow user to switch between
		// 'Grouped' and 'Stacked' mode.
		chart.groupSpacing(0.1); // Distance between each group of bars.

		if (jParam.stacked != null) {
			chart.stacked(jParam.stacked);
		} else {
			chart.stacked(false);
		}
		setupXYAxis(chart, jParam); // private function call inside this domain
		return chart;
	}

	this.getDiscreteBarChart = function getDiscreteBarChart(jParam) {

		var chart = nv.models.discreteBarChart();

		chart.x(function(d) {
			return d.x
		}) // Specify the data
		// accessors.
		.y(function(d) {
			return d.y
		}).staggerLabels(true) // Too many bars and not enough room? Try
		// staggering labels.
		.tooltips(false) // Don't show tooltips
		.showValues(true) // ...instead, show the bar value right on
		// top of each bar.

		return chart;
	}

	/**
	 * private function
	 */
	var setupXYAxis = function(chart, jParam) {

		if (jParam.xAxisLabel != null) {
			chart.xAxis.axisLabel(jParam.xAxisLabel);
		}
		if (jParam.xAxisFormat != null) {
			chart.xAxis.tickFormat(d3.format(jParam.xAxisFormat));
		}

		if (jParam.yAxisLabel != null) {
			chart.yAxis.axisLabel(jParam.yAxisLabel);
		}
		if (jParam.yAxisFormat != null) {
			chart.yAxis.tickFormat(d3.format(jParam.yAxisFormat));
		}
            
		try {
                if ( jParam.forcedRangeX != null){
                    rx = jParam.forcedRangeX; // format is {min:1, max:100}
                    minX = rx.min;
                    maxX = rx.max;
                    chart.forceX([minX, maxX]);
                }
		}catch(err){}
                
		try {
                if ( jParam.forcedRangeY != null){
                    ry = jParam.forcedRangeY; // format is {min:1, max:100}
                    minY = ry.min;
                    maxY = ry.max;
                    chart.forceY([minY, maxY]);
                }
		} catch(err){}
                
                
	}

	this.getPieChart = function getPieChart(jParam) {

		var chart = nv.models.pieChart().x(function(d) {
			return d.label
		}).y(function(d) {
			return d.value
		}).showLabels(true);
		return chart;
	}

	this.getDonutPieChart = function getDonutPieChart(jParam) {

		var chart = nv.models.pieChart().x(function(d) {
			return d.label
		}).y(function(d) {
			return d.value
		}).showLabels(true) // Display pie labels
		.labelThreshold(.05) // Configure the minimum slice size for labels
		// to show up
		.labelType("percent") // Configure what type of data to show in the
		// label. Can be "key", "value" or "percent"
		.donut(true) // Turn on Donut mode. Makes pie chart look tasty!

		if (jParam.donutRatio != null) {
			chart.donutRatio(jParam.donutRatio);
		} else {
			chart.donutRatio(0.35); // Configure the size of the donut hole.
		}

		return chart;

	}

	this.getScatterChart = function getScatterChart(jParam) {
		var chart = nv.models.scatterChart();
		chart.showDistX(true); // showDist, when true, will display those
		// little distribution lines on the axis.
		chart.showDistY(true);
		chart.color(d3.scale.category10().range());
		// Configure how the tooltip looks.
		chart.tooltipContent(function(key) {
			return '<h3>' + key + '</h3>';
		});
		return chart;
	}

	/**
	 * 
	 * @param jParam
	 *            object that passes in all the data needed for the Graph. The
	 *            chart data is passed in the "chartData" property and matches
	 *            the data expected by the multiBarChart The id of the div that
	 *            contains the SVG tag is passed in the "svgID" property The
	 *            type of chart we want to see is determined by the "chartType"
	 *            property. "bar" for simple multiBarChart, "line" for lineChart
	 */
	this.showXYBasedChart = function(jParam) {

		if (jParam == null) {
			return;
		}

		// The next 2 lines are a horrendous HACK in order to get the data from
		// GWT to work well with nvd3.
		// For some reason JavaScriptObjects initialized in GWT don't play well
		// with nvd3 so we turn them
		// to string and parse them right back in order to get a 'proper'
		// (whatever that is) object
		var s1 = itgp.toString(jParam);
		var jParam2 = itgp.fromString(s1);

		try {

			itgp.chartUsingJSParam(jParam2);

		} catch (e) {
			alert("The following error occurred while executing the itgp.chart function:\n\n"
					+ e);
		}
	}

	/**
	 * Do not call directly - Call chart()
	 * 
	 * @param jParam
	 */
	this.chartUsingJSParam = function(jParam) {

		var svgID = jParam.svgID

		if (svgID == null) {
			svgID = ITGP_SVG_ID;
		}

		var svgParent = d3.select("#" + svgID);
		svgParent.selectAll("*").remove();
		var svg = svgParent.append('svg');

		jParam.svg = svg;
		// var moveLegend = function(){
		// // move the legend to the the bottom of the chart
		// // assumes #gwtviz_chart_id height of about 700 in css and 200px
		// bottom padding
		// var elem= d3.select(".nv-legendWrap");
		// elem.attr("transform","translate(0,650)");
		// elem.attr("visibility", "visible");
		// }
		//		
		// var hideLegend = function(){
		// // hide the legend while the chart is redrawn
		// d3.select(".nv-legendWrap").attr("visibility", "hidden");
		// }

		var chartType = jParam.chartType;
		graph = function() {

			var graph = null;
			if (chartType == ITGP_CHART_TYPE_BAR) {
				graph = itgp.getMultiBarChart(jParam);
				svg.datum(jParam.xySeries)

			} else if (chartType == ITGP_CHART_TYPE_LINE) {
				graph = itgp.getLineChart(jParam);
				svg.datum(jParam.xySeries)

			} else if (chartType == ITGP_CHART_TYPE_PIE) {
				graph = itgp.getPieChart(jParam);
				svg.datum(jParam.labelValueSeries)

			} else if (chartType == ITGP_CHART_TYPE_DONUT) {

				graph = itgp.getDonutPieChart(jParam);
				svg.datum(jParam.labelValueSeries);

			} else if (chartType == ITGP_CHART_TYPE_DISCRETE_BAR) {

				graph = itgp.getDiscreteBarChart(jParam);
				svg.datum(jParam.xySeries);
			} else if (chartType == ITGP_CHART_TYPE_SCATTER) {

				graph = itgp.getScatterChart(jParam);
				svg.datum(jParam.scatterSeries);

			} else {
				alert('Chart type "' + chartType + '" not implemented!');
				return;
			}

			svg.transition().duration(350);
			svg.call(graph);

			// graph.dispatch.on('renderEnd', moveLegend);
			// graph.dispatch.on('stateChange', hideLegend);
			// moveLegend(); // do this the first time

			// The zoom must occur after the initialization of the graph with
			// call
			if (chartType == ITGP_CHART_TYPE_LINE) {
				itgp.enableZoom(graph, jParam);
			} else if (chartType == ITGP_CHART_TYPE_SCATTER) {
				itgp.enableZoom(graph, jParam);
			}

			return graph;
		}();

		nv.utils.windowResize(undefined);

		lastGraph = graph;

		nv.utils.windowResize(function() {
			if (lastGraph != null) {
				lastGraph.update();
			}
		});

		nv.addGraph(graph);
	}

	/**
	 * 
	 * @param errMsg
	 *            "origin" holds the origin of the error, "err" holds the actual
	 *            error
	 */
	this.handleError = function(errMsg) {
		var errMsgTag = document.getElementById("errorMessage")
		if (errMsgTag != null) {
			errMsgTag = errMsgTag.childNodes[0].nodeValue;
		}

		if (errMsgTag == null) {
			alert("Error occurred: " + JSON.stringify(errMsg))
		} else {
			errMsgTag.innerHTML = JSON.stringify(errMsg);
		}
	};

	this.getColor10 = function() {
		var colorArray = d3.scale.category10().range();
		colorArray[7] = colorArray[1]; // move the orange to later where gray
		// is now
		colorArray[0] = "blue"; // overwrite first color
		colorArray[1] = "gray"; // overwrite second color
		// colorArray = [ "blue", "gray" ];
		// colorArray = [ "gray"];

		return colorArray;
	};

	/**
	 * 
	 * @param {type}
	 *            originalData
	 * @param {type}
	 *            colInfo [true, false, true, false]
	 * @returns {undefined}
	 */
	this.correctDataTypes = function(originalData, colInfo) {

		origJS = itgp.fromString(originalData);
		colTypes = itgp.fromString(colInfo);
		numCols = colTypes.length;

		retVal = "[";

		numRows = origJS.length;
		for (i = 0; i < numRows; i++) {
			var row = origJS[i];

			if (i > 0) {
				retVal += ", ";
			}
			var rowString = "[";
			for (j = 0; j < numCols; j++) {
				var cell = row[j];
				numeric = colTypes[j];
				var cellVal = null;
				if (numeric == true) {
					if (cell) {
						cellVal = cell;
					} else {
						cellVal = 0; // don't leave a blank when it's numeric
										// - default to 0
					}
				} else {
					cellVal = '"' + cell + '"';
				}
				if (j > 0) {
					rowString += ", ";
				}
				rowString = rowString.concat(cellVal);

			}
			rowString += "]";

			retVal = retVal.concat(rowString);
		}
		retVal += "]"
		return retVal;

	};

	this.enableZoom = function(chart, jParam) {
		// jParam comes from GWT and will contain the id of the chart DIV
		var svgID = null;
		if (jParam !== null) {
			svgID = jParam.svgID;
		}
		if (svgID == null) {
			svgID = ITGP_SVG_ID;
		}

		try {

			function addZoom(options) {

				// parameters
				var chart = options.chart;
				var yAxis = options.yAxis;
				var xAxis = options.xAxis;
				var xDomain = options.xDomain || xAxis.scale().domain;
				var yDomain = options.yDomain || yAxis.scale().domain;
				var redraw = options.redraw;
				var extentMin = options.extentMin;
				var extentMax = options.extentMax;
				var svgID = options.svgID;
				var svg = options.svg;

				// scales
				var xScale = xAxis.scale();
				var yScale = yAxis.scale();

				// min/max boundaries
				var x_boundary = xScale.domain().slice();
				var y_boundary = yScale.domain().slice();

				// create d3 zoom handler
				var d3zoom = d3.behavior.zoom();

				// ensure nice axis
				xScale.nice();
				yScale.nice();

				// zoom event handler
				function zoomed() {
					yDomain(yScale.domain());
					xDomain(xScale.domain());
					redraw();
				}
				;

				// zoom event handler
				function unzoomed() {
					xDomain(x_boundary);
					yDomain(y_boundary);
					redraw();
					d3zoom.scale(1);
					d3zoom.translate([ 0, 0 ]);
				}
				;

				// initialize wrapper
				d3zoom.x(xScale).y(yScale)
						.scaleExtent([ extentMin, extentMax ]).on('zoom',
								zoomed);

				// add handler
				svg.call(d3zoom).on('dblclick.zoom', unzoomed);
				// d3.select(svgID).call(d3zoom).on('dblclick.zoom', unzoomed);
			}
			;

			// add zoom
			addZoom({
				chart : chart,
				xAxis : chart.xAxis,
				yAxis : chart.yAxis,
				yDomain : chart.yDomain,
				xDomain : chart.xDomain,
				redraw : function() {
					chart.update()
				},
				extentMin : -1,
				extentMax : 100,
				svgID : svgID,
				svg : jParam.svg

			});
		} catch (err) {
			console.log(err);
		}

	}

	// /**
	// *
	// * @returns String test data for charts
	// */
	// this.testData = function(param) {
	//
	// var colorArray = getColor10();
	// var test_data = stream_layers(2, 5, 10).map(function(data, i) {
	// return {
	// key : 'Data Range ' + i,
	// values : data,
	// color : colorArray[i]
	// };
	// });
	//
	// var cdata = {}
	//
	// // copy all the param properties in cdata
	// for ( var k in param)
	// cdata[k] = param[k];
	//
	// cdata.xySeries = test_data
	// cdata.svgID = "chart";
	//
	// cdata.xAxisLabel = "Horizontal Label"
	// cdata.yAxisLabel = "Vertical Label"
	// cdata.xAxisFormat = ',r'
	// cdata.yAxisFormat = '.02f'
	//
	// return cdata;
	//
	// }

	// Heavily influenced by Mike Bostock's Scatter Matrix example
	// http://mbostock.github.io/d3/talk/20111116/iris-splom.html
	// Adapted by InsiTech from
	// http://benjiec.github.io/scatter-matrix/demo/demo.html#
	//

	this.mockScatterMatrix = function() {
		var data = [ {
			"species" : "setosa",
			"sepal length" : "5.1",
			"sepal width" : "3.5",
			"petal length" : "1.4",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.9",
			"sepal width" : "3",
			"petal length" : "1.4",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.7",
			"sepal width" : "3.2",
			"petal length" : "1.3",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.6",
			"sepal width" : "3.1",
			"petal length" : "1.5",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5",
			"sepal width" : "3.6",
			"petal length" : "1.4",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.4",
			"sepal width" : "3.9",
			"petal length" : "1.7",
			"petal width" : "0.4"
		}, {
			"species" : "setosa",
			"sepal length" : "4.6",
			"sepal width" : "3.4",
			"petal length" : "1.4",
			"petal width" : "0.3"
		}, {
			"species" : "setosa",
			"sepal length" : "5",
			"sepal width" : "3.4",
			"petal length" : "1.5",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.4",
			"sepal width" : "2.9",
			"petal length" : "1.4",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.9",
			"sepal width" : "3.1",
			"petal length" : "1.5",
			"petal width" : "0.1"
		}, {
			"species" : "setosa",
			"sepal length" : "5.4",
			"sepal width" : "3.7",
			"petal length" : "1.5",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.8",
			"sepal width" : "3.4",
			"petal length" : "1.6",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.8",
			"sepal width" : "3",
			"petal length" : "1.4",
			"petal width" : "0.1"
		}, {
			"species" : "setosa",
			"sepal length" : "4.3",
			"sepal width" : "3",
			"petal length" : "1.1",
			"petal width" : "0.1"
		}, {
			"species" : "setosa",
			"sepal length" : "5.8",
			"sepal width" : "4",
			"petal length" : "1.2",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.7",
			"sepal width" : "4.4",
			"petal length" : "1.5",
			"petal width" : "0.4"
		}, {
			"species" : "setosa",
			"sepal length" : "5.4",
			"sepal width" : "3.9",
			"petal length" : "1.3",
			"petal width" : "0.4"
		}, {
			"species" : "setosa",
			"sepal length" : "5.1",
			"sepal width" : "3.5",
			"petal length" : "1.4",
			"petal width" : "0.3"
		}, {
			"species" : "setosa",
			"sepal length" : "5.7",
			"sepal width" : "3.8",
			"petal length" : "1.7",
			"petal width" : "0.3"
		}, {
			"species" : "setosa",
			"sepal length" : "5.1",
			"sepal width" : "3.8",
			"petal length" : "1.5",
			"petal width" : "0.3"
		}, {
			"species" : "setosa",
			"sepal length" : "5.4",
			"sepal width" : "3.4",
			"petal length" : "1.7",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.1",
			"sepal width" : "3.7",
			"petal length" : "1.5",
			"petal width" : "0.4"
		}, {
			"species" : "setosa",
			"sepal length" : "4.6",
			"sepal width" : "3.6",
			"petal length" : "1",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.1",
			"sepal width" : "3.3",
			"petal length" : "1.7",
			"petal width" : "0.5"
		}, {
			"species" : "setosa",
			"sepal length" : "4.8",
			"sepal width" : "3.4",
			"petal length" : "1.9",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5",
			"sepal width" : "3",
			"petal length" : "1.6",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5",
			"sepal width" : "3.4",
			"petal length" : "1.6",
			"petal width" : "0.4"
		}, {
			"species" : "setosa",
			"sepal length" : "5.2",
			"sepal width" : "3.5",
			"petal length" : "1.5",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.2",
			"sepal width" : "3.4",
			"petal length" : "1.4",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.7",
			"sepal width" : "3.2",
			"petal length" : "1.6",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.8",
			"sepal width" : "3.1",
			"petal length" : "1.6",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.4",
			"sepal width" : "3.4",
			"petal length" : "1.5",
			"petal width" : "0.4"
		}, {
			"species" : "setosa",
			"sepal length" : "5.2",
			"sepal width" : "4.1",
			"petal length" : "1.5",
			"petal width" : "0.1"
		}, {
			"species" : "setosa",
			"sepal length" : "5.5",
			"sepal width" : "4.2",
			"petal length" : "1.4",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.9",
			"sepal width" : "3.1",
			"petal length" : "1.5",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5",
			"sepal width" : "3.2",
			"petal length" : "1.2",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.5",
			"sepal width" : "3.5",
			"petal length" : "1.3",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.9",
			"sepal width" : "3.6",
			"petal length" : "1.4",
			"petal width" : "0.1"
		}, {
			"species" : "setosa",
			"sepal length" : "4.4",
			"sepal width" : "3",
			"petal length" : "1.3",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.1",
			"sepal width" : "3.4",
			"petal length" : "1.5",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5",
			"sepal width" : "3.5",
			"petal length" : "1.3",
			"petal width" : "0.3"
		}, {
			"species" : "setosa",
			"sepal length" : "4.5",
			"sepal width" : "2.3",
			"petal length" : "1.3",
			"petal width" : "0.3"
		}, {
			"species" : "setosa",
			"sepal length" : "4.4",
			"sepal width" : "3.2",
			"petal length" : "1.3",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5",
			"sepal width" : "3.5",
			"petal length" : "1.6",
			"petal width" : "0.6"
		}, {
			"species" : "setosa",
			"sepal length" : "5.1",
			"sepal width" : "3.8",
			"petal length" : "1.9",
			"petal width" : "0.4"
		}, {
			"species" : "setosa",
			"sepal length" : "4.8",
			"sepal width" : "3",
			"petal length" : "1.4",
			"petal width" : "0.3"
		}, {
			"species" : "setosa",
			"sepal length" : "5.1",
			"sepal width" : "3.8",
			"petal length" : "1.6",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "4.6",
			"sepal width" : "3.2",
			"petal length" : "1.4",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5.3",
			"sepal width" : "3.7",
			"petal length" : "1.5",
			"petal width" : "0.2"
		}, {
			"species" : "setosa",
			"sepal length" : "5",
			"sepal width" : "3.3",
			"petal length" : "1.4",
			"petal width" : "0.2"
		}, {
			"species" : "versicolor",
			"sepal length" : "7",
			"sepal width" : "3.2",
			"petal length" : "4.7",
			"petal width" : "1.4"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.4",
			"sepal width" : "3.2",
			"petal length" : "4.5",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.9",
			"sepal width" : "3.1",
			"petal length" : "4.9",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.5",
			"sepal width" : "2.3",
			"petal length" : "4",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.5",
			"sepal width" : "2.8",
			"petal length" : "4.6",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.7",
			"sepal width" : "2.8",
			"petal length" : "4.5",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.3",
			"sepal width" : "3.3",
			"petal length" : "4.7",
			"petal width" : "1.6"
		}, {
			"species" : "versicolor",
			"sepal length" : "4.9",
			"sepal width" : "2.4",
			"petal length" : "3.3",
			"petal width" : "1"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.6",
			"sepal width" : "2.9",
			"petal length" : "4.6",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.2",
			"sepal width" : "2.7",
			"petal length" : "3.9",
			"petal width" : "1.4"
		}, {
			"species" : "versicolor",
			"sepal length" : "5",
			"sepal width" : "2",
			"petal length" : "3.5",
			"petal width" : "1"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.9",
			"sepal width" : "3",
			"petal length" : "4.2",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "6",
			"sepal width" : "2.2",
			"petal length" : "4",
			"petal width" : "1"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.1",
			"sepal width" : "2.9",
			"petal length" : "4.7",
			"petal width" : "1.4"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.6",
			"sepal width" : "2.9",
			"petal length" : "3.6",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.7",
			"sepal width" : "3.1",
			"petal length" : "4.4",
			"petal width" : "1.4"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.6",
			"sepal width" : "3",
			"petal length" : "4.5",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.8",
			"sepal width" : "2.7",
			"petal length" : "4.1",
			"petal width" : "1"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.2",
			"sepal width" : "2.2",
			"petal length" : "4.5",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.6",
			"sepal width" : "2.5",
			"petal length" : "3.9",
			"petal width" : "1.1"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.9",
			"sepal width" : "3.2",
			"petal length" : "4.8",
			"petal width" : "1.8"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.1",
			"sepal width" : "2.8",
			"petal length" : "4",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.3",
			"sepal width" : "2.5",
			"petal length" : "4.9",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.1",
			"sepal width" : "2.8",
			"petal length" : "4.7",
			"petal width" : "1.2"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.4",
			"sepal width" : "2.9",
			"petal length" : "4.3",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.6",
			"sepal width" : "3",
			"petal length" : "4.4",
			"petal width" : "1.4"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.8",
			"sepal width" : "2.8",
			"petal length" : "4.8",
			"petal width" : "1.4"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.7",
			"sepal width" : "3",
			"petal length" : "5",
			"petal width" : "1.7"
		}, {
			"species" : "versicolor",
			"sepal length" : "6",
			"sepal width" : "2.9",
			"petal length" : "4.5",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.7",
			"sepal width" : "2.6",
			"petal length" : "3.5",
			"petal width" : "1"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.5",
			"sepal width" : "2.4",
			"petal length" : "3.8",
			"petal width" : "1.1"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.5",
			"sepal width" : "2.4",
			"petal length" : "3.7",
			"petal width" : "1"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.8",
			"sepal width" : "2.7",
			"petal length" : "3.9",
			"petal width" : "1.2"
		}, {
			"species" : "versicolor",
			"sepal length" : "6",
			"sepal width" : "2.7",
			"petal length" : "5.1",
			"petal width" : "1.6"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.4",
			"sepal width" : "3",
			"petal length" : "4.5",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "6",
			"sepal width" : "3.4",
			"petal length" : "4.5",
			"petal width" : "1.6"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.7",
			"sepal width" : "3.1",
			"petal length" : "4.7",
			"petal width" : "1.5"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.3",
			"sepal width" : "2.3",
			"petal length" : "4.4",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.6",
			"sepal width" : "3",
			"petal length" : "4.1",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.5",
			"sepal width" : "2.5",
			"petal length" : "4",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.5",
			"sepal width" : "2.6",
			"petal length" : "4.4",
			"petal width" : "1.2"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.1",
			"sepal width" : "3",
			"petal length" : "4.6",
			"petal width" : "1.4"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.8",
			"sepal width" : "2.6",
			"petal length" : "4",
			"petal width" : "1.2"
		}, {
			"species" : "versicolor",
			"sepal length" : "5",
			"sepal width" : "2.3",
			"petal length" : "3.3",
			"petal width" : "1"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.6",
			"sepal width" : "2.7",
			"petal length" : "4.2",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.7",
			"sepal width" : "3",
			"petal length" : "4.2",
			"petal width" : "1.2"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.7",
			"sepal width" : "2.9",
			"petal length" : "4.2",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "6.2",
			"sepal width" : "2.9",
			"petal length" : "4.3",
			"petal width" : "1.3"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.1",
			"sepal width" : "2.5",
			"petal length" : "3",
			"petal width" : "1.1"
		}, {
			"species" : "versicolor",
			"sepal length" : "5.7",
			"sepal width" : "2.8",
			"petal length" : "4.1",
			"petal width" : "1.3"
		}, {
			"species" : "virginica",
			"sepal length" : "6.3",
			"sepal width" : "3.3",
			"petal length" : "6",
			"petal width" : "2.5"
		}, {
			"species" : "virginica",
			"sepal length" : "5.8",
			"sepal width" : "2.7",
			"petal length" : "5.1",
			"petal width" : "1.9"
		}, {
			"species" : "virginica",
			"sepal length" : "7.1",
			"sepal width" : "3",
			"petal length" : "5.9",
			"petal width" : "2.1"
		}, {
			"species" : "virginica",
			"sepal length" : "6.3",
			"sepal width" : "2.9",
			"petal length" : "5.6",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "6.5",
			"sepal width" : "3",
			"petal length" : "5.8",
			"petal width" : "2.2"
		}, {
			"species" : "virginica",
			"sepal length" : "7.6",
			"sepal width" : "3",
			"petal length" : "6.6",
			"petal width" : "2.1"
		}, {
			"species" : "virginica",
			"sepal length" : "4.9",
			"sepal width" : "2.5",
			"petal length" : "4.5",
			"petal width" : "1.7"
		}, {
			"species" : "virginica",
			"sepal length" : "7.3",
			"sepal width" : "2.9",
			"petal length" : "6.3",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "6.7",
			"sepal width" : "2.5",
			"petal length" : "5.8",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "7.2",
			"sepal width" : "3.6",
			"petal length" : "6.1",
			"petal width" : "2.5"
		}, {
			"species" : "virginica",
			"sepal length" : "6.5",
			"sepal width" : "3.2",
			"petal length" : "5.1",
			"petal width" : "2"
		}, {
			"species" : "virginica",
			"sepal length" : "6.4",
			"sepal width" : "2.7",
			"petal length" : "5.3",
			"petal width" : "1.9"
		}, {
			"species" : "virginica",
			"sepal length" : "6.8",
			"sepal width" : "3",
			"petal length" : "5.5",
			"petal width" : "2.1"
		}, {
			"species" : "virginica",
			"sepal length" : "5.7",
			"sepal width" : "2.5",
			"petal length" : "5",
			"petal width" : "2"
		}, {
			"species" : "virginica",
			"sepal length" : "5.8",
			"sepal width" : "2.8",
			"petal length" : "5.1",
			"petal width" : "2.4"
		}, {
			"species" : "virginica",
			"sepal length" : "6.4",
			"sepal width" : "3.2",
			"petal length" : "5.3",
			"petal width" : "2.3"
		}, {
			"species" : "virginica",
			"sepal length" : "6.5",
			"sepal width" : "3",
			"petal length" : "5.5",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "7.7",
			"sepal width" : "3.8",
			"petal length" : "6.7",
			"petal width" : "2.2"
		}, {
			"species" : "virginica",
			"sepal length" : "7.7",
			"sepal width" : "2.6",
			"petal length" : "6.9",
			"petal width" : "2.3"
		}, {
			"species" : "virginica",
			"sepal length" : "6",
			"sepal width" : "2.2",
			"petal length" : "5",
			"petal width" : "1.5"
		}, {
			"species" : "virginica",
			"sepal length" : "6.9",
			"sepal width" : "3.2",
			"petal length" : "5.7",
			"petal width" : "2.3"
		}, {
			"species" : "virginica",
			"sepal length" : "5.6",
			"sepal width" : "2.8",
			"petal length" : "4.9",
			"petal width" : "2"
		}, {
			"species" : "virginica",
			"sepal length" : "7.7",
			"sepal width" : "2.8",
			"petal length" : "6.7",
			"petal width" : "2"
		}, {
			"species" : "virginica",
			"sepal length" : "6.3",
			"sepal width" : "2.7",
			"petal length" : "4.9",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "6.7",
			"sepal width" : "3.3",
			"petal length" : "5.7",
			"petal width" : "2.1"
		}, {
			"species" : "virginica",
			"sepal length" : "7.2",
			"sepal width" : "3.2",
			"petal length" : "6",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "6.2",
			"sepal width" : "2.8",
			"petal length" : "4.8",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "6.1",
			"sepal width" : "3",
			"petal length" : "4.9",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "6.4",
			"sepal width" : "2.8",
			"petal length" : "5.6",
			"petal width" : "2.1"
		}, {
			"species" : "virginica",
			"sepal length" : "7.2",
			"sepal width" : "3",
			"petal length" : "5.8",
			"petal width" : "1.6"
		}, {
			"species" : "virginica",
			"sepal length" : "7.4",
			"sepal width" : "2.8",
			"petal length" : "6.1",
			"petal width" : "1.9"
		}, {
			"species" : "virginica",
			"sepal length" : "7.9",
			"sepal width" : "3.8",
			"petal length" : "6.4",
			"petal width" : "2"
		}, {
			"species" : "virginica",
			"sepal length" : "6.4",
			"sepal width" : "2.8",
			"petal length" : "5.6",
			"petal width" : "2.2"
		}, {
			"species" : "virginica",
			"sepal length" : "6.3",
			"sepal width" : "2.8",
			"petal length" : "5.1",
			"petal width" : "1.5"
		}, {
			"species" : "virginica",
			"sepal length" : "6.1",
			"sepal width" : "2.6",
			"petal length" : "5.6",
			"petal width" : "1.4"
		}, {
			"species" : "virginica",
			"sepal length" : "7.7",
			"sepal width" : "3",
			"petal length" : "6.1",
			"petal width" : "2.3"
		}, {
			"species" : "virginica",
			"sepal length" : "6.3",
			"sepal width" : "3.4",
			"petal length" : "5.6",
			"petal width" : "2.4"
		}, {
			"species" : "virginica",
			"sepal length" : "6.4",
			"sepal width" : "3.1",
			"petal length" : "5.5",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "6",
			"sepal width" : "3",
			"petal length" : "4.8",
			"petal width" : "1.8"
		}, {
			"species" : "virginica",
			"sepal length" : "6.9",
			"sepal width" : "3.1",
			"petal length" : "5.4",
			"petal width" : "2.1"
		}, {
			"species" : "virginica",
			"sepal length" : "6.7",
			"sepal width" : "3.1",
			"petal length" : "5.6",
			"petal width" : "2.4"
		}, {
			"species" : "virginica",
			"sepal length" : "6.9",
			"sepal width" : "3.1",
			"petal length" : "5.1",
			"petal width" : "2.3"
		}, {
			"species" : "virginica",
			"sepal length" : "5.8",
			"sepal width" : "2.7",
			"petal length" : "5.1",
			"petal width" : "1.9"
		}, {
			"species" : "virginica",
			"sepal length" : "6.8",
			"sepal width" : "3.2",
			"petal length" : "5.9",
			"petal width" : "2.3"
		}, {
			"species" : "virginica",
			"sepal length" : "6.7",
			"sepal width" : "3.3",
			"petal length" : "5.7",
			"petal width" : "2.5"
		}, {
			"species" : "virginica",
			"sepal length" : "6.7",
			"sepal width" : "3",
			"petal length" : "5.2",
			"petal width" : "2.3"
		}, {
			"species" : "virginica",
			"sepal length" : "6.3",
			"sepal width" : "2.5",
			"petal length" : "5",
			"petal width" : "1.9"
		}, {
			"species" : "virginica",
			"sepal length" : "6.5",
			"sepal width" : "3",
			"petal length" : "5.2",
			"petal width" : "2"
		}, {
			"species" : "virginica",
			"sepal length" : "6.2",
			"sepal width" : "3.4",
			"petal length" : "5.4",
			"petal width" : "2.3"
		}, {
			"species" : "virginica",
			"sepal length" : "5.9",
			"sepal width" : "3",
			"petal length" : "5.1",
			"petal width" : "1.8"
		} ];
		jParam = {
			"matrixScatterData" : data
		};
		itgp.showScatterMatrixChart(jParam);
	};

	this.showScatterMatrixChart = function(jParam) {
		var sm = new itgp.ScatterMatrix(jParam.data);

		var svgID = jParam.svgID
		if (svgID == null) {
			svgID = ITGP_SVG_ID;
		}

		sm.render(svgID);
	};

	this.ScatterMatrix = function(data) {
		this.__url = undefined;
		this.__data = data;
		this.__cell_size = 140;
	};

	this.ScatterMatrix.prototype.cellSize = function(n) {
		this.__cell_size = n;
		return this;
	};

	this.ScatterMatrix.prototype.onData = function(cb) {
		if (this.__data) {
			cb();
			return;
		}
		var self = this;
		d3.csv(self.__url, function(data) {
			self.__data = data;
			cb();
		});
	};

	this.ScatterMatrix.prototype.render = function(svgID) {
		var self = this;

		var outerContainer = d3.select("#" + svgID);
		outerContainer.selectAll("*").remove();
		outerContainer.append('div').attr("id", "matrixChartContainer");

		outerContainer = d3.select("#matrixChartContainer");
		var container = outerContainer.append('div').attr('class',
				'scatter-matrix-container');
		var control = container.append('div').attr('class',
				'scatter-matrix-control');
		var svg = container.append('div').attr('class', 'scatter-matrix-svg')
				.html('<em>Loading data...</em>');

		this.onData(function() {
			var data = self.__data;

			// Fetch data and get all string variables
			var string_variables = [ undefined ];
			var numeric_variables = [];
			var numeric_variable_values = {};

			for (k in data[0]) {
				if (isNaN(+data[0][k])) {
					string_variables.push(k);
				} else {
					numeric_variables.push(k);
					numeric_variable_values[k] = [];
				}
			}

			data.forEach(function(d) {
				for ( var j in numeric_variables) {
					var k = numeric_variables[j];
					var value = d[k];
					if (numeric_variable_values[k].indexOf(value) < 0) {
						numeric_variable_values[k].push(value);
					}
				}
			});

			var size_control = control.append('div').attr('class',
					'scatter-matrix-size-control');
			var color_control = control.append('div').attr('class',
					'scatter-matrix-color-control');
			var filter_control = control.append('div').attr('class',
					'scatter-matrix-filter-control');
			var variable_control = control.append('div').attr('class',
					'scatter-matrix-variable-control');
			var drill_control = control.append('div').attr('class',
					'scatter-matrix-drill-control');

			// shared control states
			var to_include = [];
			var color_variable = undefined;
			var selected_colors = undefined;
			for ( var j in numeric_variables) {
				var v = numeric_variables[j];
				to_include.push(v);
			}
			var drill_variables = [];

			function set_filter(variable) {
				filter_control.selectAll('*').remove();
				if (variable) {
					// Get unique values for this variable
					var values = [];
					data.forEach(function(d) {
						var v = d[variable];
						if (values.indexOf(v) < 0) {
							values.push(v);
						}
					});

					selected_colors = [];
					for ( var j in values) {
						var v = values[j];
						selected_colors.push(v);
					}

					var filter_li = filter_control.append('p').text(
							'Filter by ' + variable + ': ').append('ul')
							.selectAll('li').data(values).enter().append('li');

					filter_li.append('input').attr('type', 'checkbox').attr(
							'checked', 'checked').on(
							'click',
							function(d, i) {
								var new_selected_colors = [];
								for ( var j in selected_colors) {
									var v = selected_colors[j];
									if (v !== d || this.checked) {
										new_selected_colors.push(v);
									}
								}
								if (this.checked) {
									new_selected_colors.push(d);
								}
								selected_colors = new_selected_colors;
								self.__draw(self.__cell_size, svg,
										color_variable, selected_colors,
										to_include, drill_variables);
							});
					filter_li.append('label').html(function(d) {
						return d;
					});
				}
			}

			size_a = size_control.append('p').text('Change cell size: ');
			size_a.append('a').attr('href', '#').html('-').on(
					'click',
					function() {
						self.__cell_size *= 0.75;
						self.__draw(self.__cell_size, svg, color_variable,
								selected_colors, to_include, drill_variables);
					});
			size_a.append('span').html('&nbsp;');
			size_a.append('a').attr('href', '#').html('+').on(
					'click',
					function() {
						self.__cell_size *= 1.25;
						self.__draw(self.__cell_size, svg, color_variable,
								selected_colors, to_include, drill_variables);
					});

			color_control.append('p').text('Select a variable to color:')
			color_control.append('ul').selectAll('li').data(string_variables)
					.enter().append('li').append('a').attr('href', '#').text(
							function(d) {
								return d ? d : 'None';
							}).on(
							'click',
							function(d, i) {
								color_variable = d;
								selected_colors = undefined;
								self.__draw(self.__cell_size, svg,
										color_variable, selected_colors,
										to_include, drill_variables);
								set_filter(d);
							});

			var variable_li = variable_control.append('p').text(
					'Include variables: ').append('ul').selectAll('li').data(
					numeric_variables).enter().append('li');

			variable_li.append('input').attr('type', 'checkbox').attr(
					'checked', 'checked').on(
					'click',
					function(d, i) {
						var new_to_include = [];
						for ( var j in to_include) {
							var v = to_include[j];
							if (v !== d || this.checked) {
								new_to_include.push(v);
							}
						}
						if (this.checked) {
							new_to_include.push(d);
						}
						to_include = new_to_include;
						self.__draw(self.__cell_size, svg, color_variable,
								selected_colors, to_include, drill_variables);
					});
			variable_li.append('label').html(function(d) {
				return d;
			});

			drill_li = drill_control.append('p').text('Drill and Expand: ')
					.append('ul').selectAll('li').data(numeric_variables)
					.enter().append('li');

			drill_li.append('input').attr('type', 'checkbox').on(
					'click',
					function(d, i) {
						var new_drill_variables = [];
						for ( var j in drill_variables) {
							var v = drill_variables[j];
							if (v !== d || this.checked) {
								new_drill_variables.push(v);
							}
						}
						if (this.checked) {
							new_drill_variables.push(d);
						}
						drill_variables = new_drill_variables;
						self.__draw(self.__cell_size, svg, color_variable,
								selected_colors, to_include, drill_variables);
					});
			drill_li.append('label').html(function(d) {
				return d + ' (' + numeric_variable_values[d].length + ')';
			});

			self.__draw(self.__cell_size, svg, color_variable, selected_colors,
					to_include, drill_variables);
		});
	};

	this.ScatterMatrix.prototype.__draw = function(cell_size, container_el,
			color_variable, selected_colors, to_include, drill_variables) {
		var self = this;
		this
				.onData(function() {
					var data = self.__data;

					if (color_variable && selected_colors) {
						data = [];
						self.__data
								.forEach(function(d) {
									if (selected_colors
											.indexOf(d[color_variable]) >= 0) {
										data.push(d);
									}
								});
					}

					container_el.selectAll('*').remove();

					// If no data, don't do anything
					if (data.length == 0) {
						return;
					}

					// Parse headers from first row of data
					var numeric_variables = [];
					for (k in data[0]) {
						if (!isNaN(+data[0][k]) && to_include.indexOf(k) >= 0) {
							numeric_variables.push(k);
						}
					}
					numeric_variables.sort();

					// Get values of the string variable
					var colors = [];
					if (color_variable) {
						// Using self.__data, instead of data, so our css
						// classes are consistent when
						// we filter by value.
						self.__data.forEach(function(d) {
							var s = d[color_variable];
							if (colors.indexOf(s) < 0) {
								colors.push(s);
							}
						});
					}

					function color_class(d) {
						var c = d;
						if (color_variable && d[color_variable]) {
							c = d[color_variable];
						}
						return colors.length > 0 ? 'color-' + colors.indexOf(c)
								: 'color-2';
					}

					// Size parameters
					var size = cell_size, padding = 10, axis_width = 20, axis_height = 15, legend_width = 200, label_height = 15;

					// Get x and y scales for each numeric variable
					var x = {}, y = {};
					numeric_variables
							.forEach(function(trait) {
								// Coerce values to numbers.
								data.forEach(function(d) {
									d[trait] = +d[trait];
								});

								var value = function(d) {
									return d[trait];
								}, domain = [ d3.min(data, value),
										d3.max(data, value) ], range_x = [
										padding / 2, size - padding / 2 ], range_y = [
										padding / 2, size - padding / 2 ];

								x[trait] = d3.scale.linear().domain(domain)
										.range(range_x);
								y[trait] = d3.scale.linear().domain(domain)
										.range(range_y.reverse());
							});

					// When drilling, user select one or more variables. The
					// first drilled
					// variable becomes the x-axis variable for all columns, and
					// each column
					// contains only data points that match specific values for
					// each of the
					// drilled variables other than the first.

					var drill_values = [];
					var drill_degrees = []
					drill_variables.forEach(function(variable) {
						// Skip first one, since that's just the x axis
						if (drill_values.length == 0) {
							drill_values.push([]);
							drill_degrees.push(1);
						} else {
							var values = [];
							data.forEach(function(d) {
								var v = d[variable];
								if (v !== undefined && values.indexOf(v) < 0) {
									values.push(v);
								}
							});
							values.sort();
							drill_values.push(values);
							drill_degrees.push(values.length);
						}
					});
					var total_columns = 1;
					drill_degrees.forEach(function(d) {
						total_columns *= d;
					});

					// Pick out stuff to draw on horizontal and vertical
					// dimensions

					if (drill_variables.length > 0) {
						// First drill is now the x-axis variable for all
						// columns
						x_variables = [];
						for (var i = 0; i < total_columns; i++) {
							x_variables.push(drill_variables[0]);
						}
					} else {
						x_variables = numeric_variables.slice(0);
					}

					if (drill_variables.length > 0) {
						// Don't draw any of the "drilled" variables in vertical
						// dimension
						y_variables = [];
						numeric_variables.forEach(function(variable) {
							if (drill_variables.indexOf(variable) < 0) {
								y_variables.push(variable);
							}
						});
					} else {
						y_variables = numeric_variables.slice(0);
					}

					var filter_descriptions = 0;
					if (drill_variables.length > 1) {
						filter_descriptions = drill_variables.length - 1;
					}

					// Axes
					var x_axis = d3.svg.axis();
					var y_axis = d3.svg.axis();
					var intf = d3.format('d');
					var fltf = d3.format('.f');
					var scif = d3.format('e');

					x_axis.ticks(5).tickSize(size * y_variables.length)
							.tickFormat(
									function(d) {
										if (Math.abs(+d) > 10000
												|| (Math.abs(d) < 0.001 && Math
														.abs(d) != 0)) {
											return scif(d);
										}
										if (parseInt(d) == +d) {
											return intf(d);
										}
										return fltf(d);
									});

					y_axis.ticks(5).tickSize(size * x_variables.length)
							.tickFormat(
									function(d) {
										if (Math.abs(+d) > 10000
												|| (Math.abs(d) < 0.001 && Math
														.abs(d) != 0)) {
											return scif(d);
										}
										if (parseInt(d) == +d) {
											return intf(d);
										}
										return fltf(d);
									});

					// Brush - for highlighting regions of data
					var brush = d3.svg.brush().on("brushstart", brushstart).on(
							"brush", brush).on("brushend", brushend);

					// Root panel
//					var svg = container_el.append("svg:svg").attr(
//							"width",
//							label_height + size * x_variables.length
//									+ axis_width + padding + legend_width)
//							.attr(
//									"height","100%"									).append(
//									"svg:g").attr("transform",
//									"translate(" + label_height + ",0)");
					
					var svg = container_el.append("svg:svg").attr(
							"width",
							label_height + size * x_variables.length
									+ axis_width + padding + legend_width)
							.attr(
									"height",
									size * y_variables.length + axis_height
											+ label_height + label_height
											* filter_descriptions).append(
									"svg:g").attr("transform",
									"translate(" + label_height + ",0)");					

					// Push legend to the side
					var legend = svg
							.selectAll("g.legend")
							.data(colors)
							.enter()
							.append("svg:g")
							.attr("class", "legend")
							.attr(
									"transform",
									function(d, i) {
										return "translate("
												+ (label_height + size
														* x_variables.length + padding)
												+ "," + (i * 20 + 10) + ")";
									});

					legend.append("svg:circle").attr("class", function(d, i) {
						return color_class(d);
					}).attr("r", 3);

					legend.append("svg:text").attr("x", 12).attr("dy", ".31em")
							.text(function(d) {
								return d;
							});

					// Draw X-axis
					svg.selectAll("g.x.axis").data(x_variables).enter().append(
							"svg:g").attr("class", "x axis").attr("transform",
							function(d, i) {
								return "translate(" + i * size + ",0)";
							}).each(
							function(d) {
								d3.select(this).call(
										x_axis.scale(x[d]).orient("bottom"));
							});

					// Draw Y-axis
					svg.selectAll("g.y.axis").data(y_variables).enter().append(
							"svg:g").attr("class", "y axis").attr("transform",
							function(d, i) {
								return "translate(0," + i * size + ")";
							}).each(
							function(d) {
								d3.select(this).call(
										y_axis.scale(y[d]).orient("right"));
							});

					// Draw scatter plot
					var cell = svg.selectAll("g.cell").data(
							cross(x_variables, y_variables)).enter().append(
							"svg:g").attr("class", "cell").attr(
							"transform",
							function(d) {
								return "translate(" + d.i * size + "," + d.j
										* size + ")";
							}).each(plot);

					// Add titles for y variables
					cell.filter(function(d) {
						return d.i == 0;
					}).append("svg:text").attr("x", padding - size).attr("y",
							-label_height).attr("dy", ".71em").attr(
							"transform", function(d) {
								return "rotate(-90)";
							}).text(function(d) {
						return d.y;
					});

					function plot(p) {
						// console.log(p);

						var data_to_draw = data;

						// If drilling, compute what values of the drill
						// variables correspond to
						// this column.
						//
						var filter = {};
						if (drill_variables.length > 1) {
							var column = p.i;

							var cap = 1;
							for (var i = drill_variables.length - 1; i > 0; i--) {
								var var_name = drill_variables[i];
								var var_value = undefined;

								if (i == drill_variables.length - 1) {
									// for the last drill variable, we index by
									// %
									var_value = drill_values[i][column
											% drill_degrees[i]];
								} else {
									// otherwise divide by capacity of
									// subsequent variables to get value array
									// index
									var_value = drill_values[i][parseInt(column
											/ cap)];
								}

								filter[var_name] = var_value;
								cap *= drill_degrees[i];
							}

							data_to_draw = [];
							data.forEach(function(d) {
								var pass = true;
								for (k in filter) {
									if (d[k] != filter[k]) {
										pass = false;
										break;
									}
								}
								if (pass === true) {
									data_to_draw.push(d);
								}
							});
						}

						var cell = d3.select(this);

						// Frame
						cell.append("svg:rect").attr("class", "frame").attr(
								"x", padding / 2).attr("y", padding / 2).attr(
								"width", size - padding).attr("height",
								size - padding);

						// Scatter plot dots
						cell.selectAll("circle").data(data_to_draw).enter()
								.append("svg:circle").attr("class",
										function(d) {
											return color_class(d);
										}).attr("cx", function(d) {
									return x[p.x](d[p.x]);
								}).attr("cy", function(d) {
									return y[p.y](d[p.y]);
								}).attr("r", 5);

						// Add titles for x variables and drill variable values
						if (p.j == y_variables.length - 1) {
							cell.append("svg:text").attr("x", padding).attr(
									"y", size + axis_height)
									.attr("dy", ".71em").text(function(d) {
										return d.x;
									});

							if (drill_variables.length > 1) {
								var i = 0;
								for (k in filter) {
									i += 1;
									cell.append("svg:text").attr("x", padding)
											.attr(
													"y",
													size + axis_height
															+ label_height * i)
											.attr("dy", ".71em").text(
													function(d) {
														return filter[k] + ': '
																+ k;
													});
								}
							}
						}

						// Brush
						cell.call(brush.x(x[p.x]).y(y[p.y]));
					}

					// Clear the previously-active brush, if any
					function brushstart(p) {
						if (brush.data !== p) {
							cell.call(brush.clear());
							brush.x(x[p.x]).y(y[p.y]).data = p;
						}
					}

					// Highlight selected circles
					function brush(p) {
						var e = brush.extent();
						svg
								.selectAll(".cell circle")
								.attr(
										"class",
										function(d) {
											return e[0][0] <= d[p.x]
													&& d[p.x] <= e[1][0]
													&& e[0][1] <= d[p.y]
													&& d[p.y] <= e[1][1] ? color_class(d)
													: null;
										});
					}

					// If brush is empty, select all circles
					function brushend() {
						if (brush.empty())
							svg.selectAll(".cell circle").attr("class",
									function(d) {
										return color_class(d);
									});
					}

					function cross(a, b) {
						var c = [], n = a.length, m = b.length, i, j;
						for (i = -1; ++i < n;)
							for (j = -1; ++j < m;)
								c.push({
									x : a[i],
									i : i,
									y : b[j],
									j : j
								});
						return c;
					}
				});

	};

	this.setScatterMatrixValue = function(array, rowIndex, name, value) {
		var row;
		if (typeof array[rowIndex] === 'undefined') {
			// does not exist
			row = {};
			array[rowIndex] = row;
		} else {
			// does exist
			row = array[rowIndex];
		}

		row[name] = value;
	}

};
