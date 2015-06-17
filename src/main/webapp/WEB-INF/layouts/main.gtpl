<!doctype html>
<html>
<head>
    <title><sitemesh:write property='title'/> | H100Cal</title>
    <link rel="shortcut icon" href="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Running_icon_-_Noun_Project_17825.svg/2000px-Running_icon_-_Noun_Project_17825.svg.png" type="image/png">
    <link rel="icon" href="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Running_icon_-_Noun_Project_17825.svg/2000px-Running_icon_-_Noun_Project_17825.svg.png" type="image/png">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/css/c3.css"/>
    <script type="text/javascript" src="/js/thirdparty/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="/js/thirdparty/bootstrap.min.js"></script>
    <style type="text/css">
        <% if (request.getParameter("l")?.toString() != '0') { %>
        body {
            background-color: #ffffff;
            background-image: url(/images/black-linen.png);
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


        <% } %>
        .comic {
            font-family: "Comic Sans MS", "Comic Sans", cursive;
            font-size: 1.5em;
        }

        .center {
            text-align: center;
        }
    </style>
    <sitemesh:write property='head'/>
</head>

<body>
    <% if (request.getParameter("l")?.toString() != '0') { %>
    <a href="https://github.com/rahulsom/100ddcal">
        <img style="position: absolute; top: 0; right: 0; border: 0; z-index: 1000;"
             src="/images/fork-me.png"
             alt="Fork me on GitHub"
             data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_right_red_aa0000.png"
             height="149" width="149"
        >
    </a>
    <% } %>

    <div class="container" >
        <div class="box effect7" style="display: none;">
            <sitemesh:write property='body'/>
        </div>
    </div>

    <script type="text/javascript" src="/js/thirdparty/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="/js/thirdparty/bootstrap.min.js"></script>


</body>
</html>
