String.prototype.toTitleCase = function (n) {
    var s = this;
    if (1 !== n) s = s.toLowerCase();
    return s.replace(/\b[a-z]/g, function (f) {
        return f.toUpperCase()
    });
};

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

/**
 * Gets team objects out of data
 * @param data object returned by ajax
 */
function getTeams(data) {

    var teams =  _.sortBy(data.data, function (team) {
        return parseInt(team.rank.replace(/,/g, ''));
    });

    var idx = 0;
    _.each(teams, function (team) {
        var pastel = pastels[idx++];
        team.color = pastel;
    });
    return teams;
}

/**
 * Gets member objects out of data
 * @param data object returned by ajax
 */
function getMembers(data) {

    _.each(data.data, function (team) {
        _.each(team.members, function (member) {
            member.teamName = team.name;
            member.color = team.color;
            var nameParts = member.name.toTitleCase().split(' ');
            member.firstName = nameParts[0];
            member.lastName = nameParts.slice(1, nameParts.length).join(' ');
        });
    });

    var allMembers =  _.flatten(_.collect(data.data, function (team) {
        return team.members;
    }));

    return _.sortBy(allMembers, function (member) {
        var score = member.score == '-' ? '0' : member.score;
        return -parseInt(score.replace(/,/g, ''));
    })
}
