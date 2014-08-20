function plotData(data1, names) {
  var chart = c3.generate({
    data: {
      columns: [
        data1,
      ],
      type: 'bar'
    },
    axis: {
      x: {
        type: 'category',
        categories: names
      },
      rotated: true
    }

  });
}
function processData(data) {
  console.log(data.data);
  var teams = _.sortBy(data.data, function (team) {
    return parseInt(team.rank.replace(',', ''));
  });
  console.log(teams);
  var data1 = _.map(teams, function (team) {
    return parseInt(team.avgSteps.replace(',', ''))
  });
  var names = _.map(teams, function (team) {
    return team.name;
  });
  data1.unshift('steps');
  console.log(data1);
  console.log(names);

  plotData(data1, names);
  var theMoment = moment.utc(data.date);

  $('#lu').html(theMoment.fromNow());

}
function reloadData() {
  reloadDataImpl(0)
  setTimeout(reloadData, 1000 * 60 * 5);
}

function reloadDataImpl(offset) {
  $.getJSON('/api/data', {offset: offset}, function (data, status, xhr) {
    if (status == 'success') {
      try {
        processData(data);
      } catch (err) {
        reloadDataImpl(offset + 1)
      }
    } else {
      console.log ("No way I'm making this work");
    }
  });
}
$(function () {
  reloadData();
});