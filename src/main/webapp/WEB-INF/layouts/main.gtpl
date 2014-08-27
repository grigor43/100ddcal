<!doctype html>
<html>
    <head>
        <title><sitemesh:write property='title'/> | H100Cal</title>
        <link rel="shortcut icon" href="/images/gaelyk-small-favicon.png" type="image/png">
        <link rel="icon" href="/images/gaelyk-small-favicon.png" type="image/png">
        <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" href="/css/c3.css"/>
        <script type="text/javascript" src="/js/jquery-1.11.0.js">
        </script>
        <script type="text/javascript" src="/js/bootstrap.min.js"></script>
        <style type="text/css">
        .center {
            text-align: center;
        }

        body {
            background-color: #ffffff;
            background-image: url(/images/black-linen.png);
            /* This is mostly intended for prototyping; please download the pattern and re-host for production environments. Thank you! */
        }

        .box {
            background: #FFF;
            margin: 40px -20px 40px -20px;
            padding: 20px;
        }

        /*==================================================
         * Effect 7
         * ===============================================*/
        .effect7 {
            position: relative;
            -webkit-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3), 0 0 40px rgba(0, 0, 0, 0.1) inset;
            -moz-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3), 0 0 40px rgba(0, 0, 0, 0.1) inset;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3), 0 0 40px rgba(0, 0, 0, 0.1) inset;
        }

        .effect7:before, .effect7:after {
            content: "";
            position: absolute;
            z-index: -1;
            -webkit-box-shadow: 0 0 20px rgba(0, 0, 0, 0.8);
            -moz-box-shadow: 0 0 20px rgba(0, 0, 0, 0.8);
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.8);
            top: 0;
            bottom: 0;
            left: 10px;
            right: 10px;
            -moz-border-radius: 100px / 10px;
            border-radius: 100px / 10px;
        }

        .effect7:after {
            right: 10px;
            left: auto;
            -webkit-transform: skew(8deg) rotate(3deg);
            -moz-transform: skew(8deg) rotate(3deg);
            -ms-transform: skew(8deg) rotate(3deg);
            -o-transform: skew(8deg) rotate(3deg);
            transform: skew(8deg) rotate(3deg);
        }

        </style>
        <sitemesh:write property='head'/>
    </head>

    <body>
        <a href="https://github.com/rahulsom/100ddcal">
            <img style="position: absolute; top: 0; right: 0; border: 0; z-index: 1000;"
                 src="https://camo.githubusercontent.com/365986a132ccd6a44c23a9169022c0b5c890c387/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f7265645f6161303030302e706e67"
                 alt="Fork me on GitHub"
                 data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_right_red_aa0000.png">
        </a>

        <div class="container" >
            <div class="box effect7" style="display: none;">
                <sitemesh:write property='body'/>
            </div>

        </div>
    </body>
</html>
