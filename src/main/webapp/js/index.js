
function plotData(data1, names, teams) {
    var chart = c3.generate({
        data: {
            columns: [ data1 ],
            type: 'bar',
            labels: true,
            color: function (color, d) {
                if (teams[d.index]) {
                    return teams[d.index].color;
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

    var teams = getTeams(data);
    var teamAverages = _.map(teams, function (team) {
        return parseInt(team.avgSteps.replace(/,/g, ''))
    });
    var teamNames = _.map(teams, function (team) {
        return team.name;
    });
    teamAverages.unshift('steps');

    var sortedMembers = getMembers(data).slice(0, 12);

    $('.box').show();
    plotData(teamAverages, teamNames, teams);
    $('#updatelink').attr('href', link);
    $('#updateTime').show();
    $('#individuals').html('');
    _.each(sortedMembers, function (member) {
        $('#individuals').append(ich.member(member));
    });

}
function reloadData() {
    reloadDataImpl(0);
}

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
            console.error("No way I'm making this work");
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
