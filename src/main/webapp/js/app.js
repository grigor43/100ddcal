$(function () {
  console.log("On Load");
  $.getJSON('/api/data', null, function (data, status, xhr) {
    if (status == 'success') {
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
  });
});