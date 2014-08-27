<html>
    <head>
        <title>Home</title>
        <link href='http://fonts.googleapis.com/css?family=Special+Elite' rel='stylesheet' type='text/css'>
        <style media="screen">
        .individual {
            border: 1px solid #DDD;
            height: 162px;
            padding: 8px 8px 0 8px;
            border-radius: 10px;
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
        </div>

        <script id="member" type="text/html">
        <div class="col-md-2">
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
        <script src="/js/app.js"></script>
    </body>
</html>
