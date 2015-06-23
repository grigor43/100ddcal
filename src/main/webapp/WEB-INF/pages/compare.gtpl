<html>
<head>
    <title>Team</title>
    <link href='https://fonts.googleapis.com/css?family=Special+Elite' rel='stylesheet' type='text/css'>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>

    <style>
        #chart {
            width: 100%;
        }

        .axis path,
        .axis line {
            fill: none;
            stroke: #000;
            shape-rendering: crispEdges;
        }

        .x.axis path {
            display: none;
        }

        .line {
            fill: none;
            stroke: steelblue;
            stroke-width: 2px;
        }
    </style>
</head>

<body>

<div id="chart"></div>

<script src="/js/thirdparty/underscore.js"></script>
<script src="/js/thirdparty/d3.min.js"></script>

<script src="/js/util.js"></script>
<script type="text/javascript">
    jQuery('.box').show();
    var margin = {top: 20, right: 80, bottom: 30, left: 50},
            width = 1140 - margin.left - margin.right,
            height = 650 - margin.top - margin.bottom;

    var parseDate = d3.time.format("%Y-%m-%d").parse;

    var x = d3.time.scale()
            .range([0, width]);

    var y = d3.scale.linear()
            .range([height, 0]);

    var color = d3.scale.category10();

    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");

    var yAxis = d3.svg.axis()
            .scale(y)
            .orient("left")
            .tickFormat(d3.format('s'));

    var line = d3.svg.line()
            .interpolate("monotone")
            .x(function(d) { return x(d.date); })
            .y(function(d) { return y(d.steps); });

    var svg = d3.select("#chart").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


    d3.tsv("/api/persons?q=${request.getParameter('q')}", function(error, data) {
        if (error) throw error;

        var names = d3.keys(data[0]).filter(function(key) { return key !== "date"; });
        color.domain(names);

        data.
                forEach(function (d) {
                    d.date = parseDate(d.date);
                });

        var people = names.
                map(function (name) {
                    return {
                        name: name,
                        values: data.map(function (d) {
                            return {date: d.date, steps: +d[name]};
                        }),
                        sortValue: data.
                                map(function (d) {
                                    return parseInt(d[name]);
                                }).
                                reduce(function (pre, curr, i, a) {
                                    return pre + curr;
                                })
                    };
                }).
                sort(function (a, b) {
                    return b.sortValue - a.sortValue
                });

        x.domain(d3.extent(data, function(d) { return d.date; }));

        y.domain([
            d3.min(people, function(c) { return 0 }),
            d3.max(people, function(c) { return d3.max(c.values, function(v) { return v.steps; }); })
        ]);

        svg.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis);

        svg.append("g")
                .attr("class", "y axis")
                .call(yAxis)
                .append("text")
                .attr("transform", "rotate(-90)")
                .attr("y", 6)
                .attr("dy", ".71em")
                .style("text-anchor", "end")
                .text("Steps");

        var person = svg.selectAll(".person")
                .data(people)
                .enter().append("g")
                .attr("class", "person");

        person.append("path")
                .attr("class", "line")
                .attr("d", function(d) { return line(d.values); })
                .style("stroke", function(d) { return color(d.name); });

        var legend = svg.selectAll(".legend")
                .data(people.map(function(d){return d.name;})) //sorted names as data
                .enter().append("g")
                .attr("class", "legend")
                .attr("transform", function (d, i) { return "translate(0," + i * 20 + ")"; });

        // draw legend colored rectangles
        legend.append("rect")
                .attr("x", width - 18)
                .attr("width", 18)
                .attr("height", 18)
                .style("fill", color);

        // draw legend text
        legend.append("text")
                .attr("x", width - 24)
                .attr("y", 9)
                .attr("dy", ".35em")
                .style("text-anchor", "end")
                .text(function (d) { return d.toTitleCase(); })

    });</script>
</body>
</html>
