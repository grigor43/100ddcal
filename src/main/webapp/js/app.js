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
  $('#lu').html(data.date);
  console.log(data.data);
  var teams = _.sortBy(data.data, function (team) {
    return team.rank;
  });
  console.log(teams);
  var data1 = _.map(teams, function (team) {
    return parseInt(team.avgSteps.replace(',', ''))
  }).valueOf();
  var names = _.map(teams, function (team) {
    return team.name;
  }).valueOf();
  data1.unshift('steps');
  console.log(data1);
  //names.unshift('x');
  console.log(names);

  plotData(data1, names);
}
function reloadData() {
  var offset = 0;
  var done = false
  while (!done) {
    try {
      $.getJSON('/api/data', {offset: offset}, function (data, status, xhr) {
        if (status == 'success') {
          processData(data);
        }
      });
      done = true;
    } catch (err) {
      offset++;
    }
  }
  setTimeout(reloadData, 1000*60*5);
}
$(function () {
  reloadData();
});