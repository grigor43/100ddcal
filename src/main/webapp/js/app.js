String.prototype.toTitleCase = function (n) {
    var s = this;
    if (1 !== n) s = s.toLowerCase();
    return s.replace(/\b[a-z]/g, function (f) {
        return f.toUpperCase()
    });
};

function plotData(data1, names) {
    var chart = c3.generate({
        data: {
            columns: [
                data1,
            ],
            type: 'bar',
            labels: true
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
}
function processData(data, link) {
    console.log(data.data);
    var teams = _.sortBy(data.data, function (team) {
        return parseInt(team.rank.replace(/,/g, ''));
    }) /*.slice(0, 0 + 5)*/;
    console.log(teams);
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
    }).slice(0, 6);
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
    setTimeout(reloadData, 1000 * 30);
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

var pastels = [
    '#B39eb5', '#dea5a4', '#b19cd9',
    '#ff6961', '#ffb347', '#fdfd96',
    '#aec6cf', '#f49ac2', '#cfcfc4',
    "#03c03c", '#836953', '#779ecb'
];

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
$(reloadData);
