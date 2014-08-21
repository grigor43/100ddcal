<html>
  <head><title>Home</title></head>
  <body>
    <div id="updateTime" style="display: none">
      Last Updated <span id="lu"></span>
    </div>
    <h3>Teams</h3>
    <div id="chart"></div>
    <h3>Individuals</h3>
    <div class="row">
      <div id="individuals">

      </div>
    </div>

    <script id="member" type="text/html">
    <div class="col-md-2">
      <div style="border: 1px solid #000000; height: 153px; padding: 0 8px;">
        <div style="height: 63px ">
          <h4>{{name}}</h4>
        </div>
        <p>{{teamName}}</p>
        <h3>{{score}}</h3>
      </div>
    </div>
    </script>

    <script src="//cdnjs.cloudflare.com/ajax/libs/moment.js/2.7.0/moment.min.js"></script>
    <script src="/js/ICanHaz.min.js"></script>
    <script src="/js/underscore.js"></script>
    <script src="/js/d3.min.js"></script>
    <script src="/js/c3.min.js"></script>
    <script src="/js/app.js"></script>
  </body>
</html>

