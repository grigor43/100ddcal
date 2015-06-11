<html>
    <head>
        <title>Home</title>
        <link href='https://fonts.googleapis.com/css?family=Special+Elite' rel='stylesheet' type='text/css'>
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

        <style media="screen">
        .individual {
            border: 1px solid #DDD;
            height: 162px;
            padding: 8px 8px 0 8px;
            border-radius: 10px;
            margin-bottom: 18px;
        }
        .individual .name {
          height: 54px;
        }
        .individual .team {
          height: 22px !important;overflow-y: hidden;overflow-x: visible; margin: 0 -11px; padding: 0 11px;
        }

        .individual .team span {
          margin: -5px -11px;
          padding: 5px 11px;
          font-family: 'Special Elite', 'Courier New', Courier, monospace;
          font-size: 12px;
        }
        </style>
    </head>

    <body>
        <h3>Teams</h3>

        <div id="chart"></div>

        <h3>Individuals</h3>

        <div class="row">
            <div id="individuals">

            </div>
        </div>
        <br>
        <br>
        <br>

        <div id="updateTime" style="display: none">
            Last Updated <a href="" id="updatelink"><span id="lu"></span></a>
            <span class="pull-right">
                <a href="#" data-toggle="modal" data-target="#myModal">Help update the data</a>
            </span>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Help update the data</h4>
              </div>
              <div class="modal-body">
                <p>Run this with the right values for your vitality username and password. If you skip those, it will
                        prompt you. This has been tested with groovy 2.4, but should work with a wider range of
                        versions. </p>
                <pre>HUM_USER=????? HUM_PASS=????? groovy https://raw.githubusercontent.com/rahulsom/100ddcal/master/H100DD.groovy</pre>
                <p>&nbsp;</p>
                <p>On a Humana network, it can take a very long time. Consider inserting this between groovy and the
                        url</p>
                <pre>-Dgroovy.grape.report.downloads=true</pre>
                <p>&nbsp;</p>
                <p>If you want to improve visualization or data collection, please send a Pull Request</p>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>

        <script id="member" type="text/html">
        <div class="col-md-2 col-sm-3 col-xs-4">
            <div style="" class="individual">
                <div class="team">
                    <span style="background-color: {{color}};">{{teamName}}</span>
                </div>

                <div class="name">
                    <h4>{{name}}</h4>
                </div>

                <h2 class="pull-right">{{score}}</h2>
            </div>
        </div>
        </script>

        <script src="/js/moment.js"></script>
        <script src="/js/ICanHaz.min.js"></script>
        <script src="/js/underscore.js"></script>
        <script src="/js/d3.min.js"></script>
        <script src="/js/c3.min.js"></script>
        <script src="/js/modernizr.min.js"></script>
        <script src="/js/detectizr.js"></script>

        <script src="/js/app.js"></script>
    </body>
</html>
