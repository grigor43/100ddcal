String.prototype.toTitleCase = function (n) {
    var s = this;
    if (1 !== n) s = s.toLowerCase();
    return s.replace(/\b[a-z]/g, function (f) {
        return f.toUpperCase()
    });
};

var pastels = [
    '#ffb347' /*Gold*/,
    '#cfcfc4' /*Silver*/,
    "#03c03c" /*Green*/,
    '#f49ac2' /*Pink2*/,
    '#779ecb' /*Blue*/,
    '#ff6961' /*Red*/,
    '#B39eb5' /*Purple*/,
    '#dea5a4' /*Pink*/,
    '#b19cd9' /*Violet*/,
    '#aec6cf' /*GrayBlue*/,
    '#fdfd96' /*Yellow*/,
    '#836953' /*Brown*/
];

var teamColors = [];

function plotData(data1, names) {
    var chart = c3.generate({
        data: {
            columns: [ data1 ],
            type: 'bar',
            labels: true,
            color: function (color, d) {
                console.log("Color: " + color);
                console.log(d);
                if (teamColors.length > d.index) {
                    return teamColors[d.index].color;
                } else {
                    return color;
                }
            },
            colors: pastels
        },
        legend: {
            show: false
        },
        axis: {
            x: {
                type: 'category',
                categories: names
            },
            rotated: true
        },
        zoom: {
            enabled: false
        },
        tooltip: {
            show: false
        }

    });

    chart.flush();
}
function processData(data, link) {
    console.log(data.data);
    var teams = _.sortBy(data.data, function (team) {
        return parseInt(team.rank.replace(/,/g, ''));
    }) /*.slice(0, 0 + 5)*/;
    console.log(teams);
    var idx = 0;
    _.each(teams, function (team) {
        teamColors.push({team: team, color: pastels[idx++]});
    });
    var data1 = _.map(teams, function (team) {
        return parseInt(team.avgSteps.replace(/,/g, ''))
    });
    var names = _.map(teams, function (team) {
        return team.name;
    });
    data1.unshift('steps');
    console.log(data1);
    console.log(names);

    _.each(data.data, function (team) {
        _.each(team.members, function (member) {
            member.teamName = team.name;
        });
    });

    var allMembers = _.flatten(_.collect(data.data, function (team) {
        return team.members;
    }));

    var sortedMembers = _.sortBy(allMembers, function (member) {
        var score = member.score == '-' ? '0' : member.score;
        return -parseInt(score.replace(/,/g, ''));
    }).slice(0, 12);
    console.log(sortedMembers);

    $('.box').show();
    plotData(data1, names);
    $('#updatelink').attr('href', link);
    $('#updateTime').show();
    $('#individuals').html('');
    _.each(sortedMembers, function (member) {
        member.color = pastels[(_.indexOf(names, member.teamName, false)) % 12];
        member.name = member.name.toTitleCase();
        $('#individuals').append(ich.member(member));
    });

}
function reloadData() {
    reloadDataImpl(0);
}

String.prototype.hashCode = function () {
    var hash = 0, i, chr, len;
    if (this.length == 0) return hash;
    for (i = 0, len = this.length; i < len; i++) {
        chr = this.charCodeAt(i);
        hash = ((hash << 5) - hash) + chr;
        hash |= 0; // Convert to 32bit integer
    }
    return hash;
};

var lastUpdateId = null;

function reloadDataImpl(offset) {
    $.getJSON('/api/data', {offset: offset}, function (data, status) {
        if (status == 'success') {
            try {
                if (lastUpdateId != data.id) {
                    processData(data, '/api/data?offset=' + offset);
                    lastUpdateId = data.id;
                }
                var theMoment = moment.utc(data.date);
                $('#lu').html(theMoment.fromNow());
            } catch (err) {
                reloadDataImpl(offset + 1)
            }
        } else {
            console.log("No way I'm making this work");
        }
    });
}
$(function () {
    var detected = Modernizr.Detectizr.detect();
    if (detected.is('ie') || detected.is('trident')) {

        var newContent = $('<center>').
            append($('<p>').
                attr('class', 'comic').
                html('This is what you get for using IE. Please use a real browser')).
            append($('<p>').
                html('&nbsp;')).
            append($('<iframe>').
                attr('width', 640).
                attr('height', 480).
                attr('src','http://www.youtube.com/embed/A8yjNbcKkNY?autoplay=1&iv_load_policy=3'));

        $('.box').
            html(newContent).
            show()
        ;

    } else {
        reloadData();
        setInterval(reloadData, 1000 * 30);
    }
});
