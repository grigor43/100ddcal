<!doctype html>
<html>
<head>
    <title>Home | H100Cal</title>
    <link rel="shortcut icon" href="/images/gaelyk-small-favicon.png" type="image/png">
    <link rel="icon" href="/images/gaelyk-small-favicon.png" type="image/png">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/css/c3.css"/>
    <script type="text/javascript" src="/js/thirdparty/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="/js/thirdparty/bootstrap.min.js"></script>
    <style type="text/css">
        .center {
            text-align: center;
        }
    </style>
    <title>Home</title>
    <link href='https://fonts.googleapis.com/css?family=Special+Elite' rel='stylesheet' type='text/css'>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>

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
            height: 22px !important;
            overflow-y: hidden;
            overflow-x: visible;
            margin: 0 -11px;
            padding: 0 11px;
        }

        .individual .team span {
            margin: -5px -11px;
            padding: 5px 11px;
            font-family: 'Special Elite', 'Courier New', Courier, monospace;
            font-size: 12px;
        }

        .mild {
            font-weight: 200;
        }

    </style>
</head>

<body>
<div class="container-fluid">
    <div id="chart"></div>

    <br>
    <br>
    <br>

    <div class="row">
        <div id="individuals">

        </div>
    </div>
    <br>
    <br>
    <br>

    <script id="member" type="text/html">
        <div class="col-md-2 col-sm-3 col-xs-6">
            <div style="" class="individual">
                <div class="team">
                    <span style="background-color: {{color}};">{{teamName}}</span>
                </div>

                <div class="name">
                    <h4>{{firstName}} <span class="mild">{{lastName}}</span></h4>
                </div>

                <h2 class="pull-right">{{score}}</h2>
            </div>
        </div>
    </script>

    <script src="/js/thirdparty/moment.js"></script>
    <script src="/js/thirdparty/ICanHaz.min.js"></script>
    <script src="/js/thirdparty/underscore.js"></script>
    <script src="/js/thirdparty/d3.min.js"></script>
    <script src="/js/thirdparty/c3.min.js"></script>
    <script src="/js/thirdparty/modernizr.min.js"></script>
    <script src="/js/thirdparty/detectizr.js"></script>

    <script src="/js/index.js"></script>
</div>
<script type="text/javascript" src="/js/thirdparty/jquery-1.11.0.js"></script>
<script type="text/javascript" src="/js/thirdparty/bootstrap.min.js"></script>

</body>
</html>
