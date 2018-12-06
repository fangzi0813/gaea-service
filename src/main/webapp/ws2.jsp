<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
  <meta charset="UTF-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>${plugin} | Log</title>
  <link rel="stylesheet" type="text/css" href="css/normalize.css" />
  <link rel="stylesheet" type="text/css" href="css/demo.css" />
  <link rel="stylesheet" type="text/css" href="css/icons.css" />
  <link rel="stylesheet" type="text/css" href="css/style3.css" />
  <script src="js/modernizr.custom.js"></script>
</head>
<body>
<div class="container">
  <header class="codrops-header">
    <div style="position: absolute; top: 19px; right: 0px;">
      <select onchange="selectLog(this)">
        <option value="-1">请选择下载</option>
        #foreach($log in $logs)
        <option value="$log">$log</option>
        #end
      </select>
    </div>
    <div style="position: absolute; left: 4px; top: 14px;">Filter&nbsp;/&nbsp;<input type="text" id="filterText" />&nbsp;<a href="#" onclick="filterClear()">Clear</a></div>
    <div style="position: absolute; left: 4px; top: 40px;">
      <input type="checkbox" checked="checked" id="trace" /><label for="trace">[TRACE]</label>&nbsp;
      <input type="checkbox" checked="checked" id="debug" /><label for="debug">[DEBUG]</label>&nbsp;
      <input type="checkbox" checked="checked" id="info" /><label for="info">[INFO] </label>&nbsp;
      <input type="checkbox" checked="checked" id="warn" /><label for="warn" style="color: orange">[WARN] </label>&nbsp;
      <input type="checkbox" checked="checked" id="error" /><label for="error" style="color: orangered">[ERROR]</label>&nbsp;
    </div>
    <div style="font-size: 34px;">${plugin}&nbsp;/&nbsp;${version}&nbsp;/&nbsp;<span id="state" style="font-size: 20px;"></span></div>
    <p class="codrops-links">
      <a class="codrops-icon codrops-icon-prev" href="status"><span>Config</span></a>
      <a class="codrops-icon codrops-icon-prev" href="moniter"><span>Moniter</span></a>
      <a class="codrops-icon codrops-icon-drop" href="2json"><span>2JSON</span></a>
      <a class="codrops-icon codrops-icon-drop" href="swagger" target="_blank"><span>Swagger</span></a>
      <a class="codrops-icon codrops-icon-drop" href="sync"><span>Sync</span></a>
    </p>
    <div id="output"></div>
  </header>
</div><!-- /container -->
</body>
<script src="/js/sockjs.min.js"></script>
<script src="/js/stomp.min.js"></script>
<script src="/js/log.js"></script>
<script type="text/javascript">
    //   var url = "ws://${host}:${port}/log";
    window.addEventListener("load", init, false);
</script>
</html>