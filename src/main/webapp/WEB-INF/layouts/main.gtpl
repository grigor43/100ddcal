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
    </style>
    <sitemesh:write property='head'/>
  </head>

  <body>
    <a href="https://github.com/rahulsom/100ddcal">
      <img style="position: absolute; top: 0; right: 0; border: 0;"
           src="https://camo.githubusercontent.com/365986a132ccd6a44c23a9169022c0b5c890c387/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f7265645f6161303030302e706e67"
           alt="Fork me on GitHub"
           data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_right_red_aa0000.png">
    </a>

    <div class="container">
      <sitemesh:write property='body'/>
    </div>
  </body>
</html>

