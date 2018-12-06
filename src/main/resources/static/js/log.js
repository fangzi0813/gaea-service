var output;
var state;
var websocket;
var pause = false;
function init() {
    output = document.getElementById("output");
    state = document.getElementById("state");
    output.style.height = (document.documentElement.clientHeight - 84) + "px";
    window.onresize = function () {
        output.style.height = (document.documentElement.clientHeight - 84) + "px";
    }
    output.onscroll = function () {
        pause = (output.scrollHeight >= (output.scrollTop + output.clientHeight + 5 /*+ 20*/));
    }
    doWebSocket();
}

function doWebSocket() {
    var socket = new SockJS('/logEndpoint'); //1
    stompClient = Stomp.over(socket);//2
    stompClient.connect({}, function(frame) {//3
        console.log('开始进行连接Connected: ' + frame);
        stompClient.subscribe('/topic/logListener', function(respnose){ //4
            writeToScreen(respnose.body);
        });
    });
    // websocket = new WebSocket(url);
    //     websocket.onopen = function (evt) {
    //         state.style.color = "#FFF";
    //         state.innerHTML = "CONNECTED";
    //     };
    //
    //     websocket.onclose = function (evt) {
    //         state.style.color = "red";
    //     state.innerHTML = "DISCONNECTED";
    //     setTimeout(function () {
    //         window.location.reload();
    //     }, 3000);
    // };
    //
    // websocket.onmessage = function (evt) {
    //     writeToScreen(evt.data);
    // };
    //
    // websocket.onerror = function (evt) {
    //     writeToScreen(evt.data);
    // };
}

function send(message) {
    writeToScreen("SENT: " + message);
    websocket.send(message);
}

function isError(msg) {
    var re = /ERROR/;
    return re.test(msg);
}
function isWarn(msg) {
    var re = /WARN/;
    return re.test(msg);
}
function isTrace(msg) {
    var re = /TRACE/;
    return re.test(msg);
}
function isInfo(msg) {
    var re = /INFO/;
    return re.test(msg);
}
function isDebug(msg) {
    var re = /DEBUG/;
    return re.test(msg);
}

function filterClear() {
    document.getElementById('filterText').value = '';
    clear();
}
function clear() {
    output.innerHTML = "";
}
function writeToScreen(message) {
    var append = function (pre, message, color) {
        var filter = document.getElementById("filterText").value;
        if (filter === undefined || filter === '') {
            pre.innerHTML = message;
            output.appendChild(pre);
            if (color)
                pre.style.color = color;
        } else {
            if (message.indexOf(filter) > 0) {
                pre.innerHTML = message;
                output.appendChild(pre);
                if (color)
                    pre.style.color = color;
            }
        }
    }
    //clear
    if (output.scrollHeight > 1000000) {
        clear();
    }
    var tmp = message.substr(0, 50);
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.style.margin = "0px";
    pre.style.padding = "0px";
    if (isError(tmp)) {
        if (document.getElementById("error").checked)
            append(pre, message, "orangered");
    } else if (isWarn(tmp)) {
        if (document.getElementById("warn").checked)
            append(pre, message, "orange");
    } else if (isTrace(tmp)) {
        if (document.getElementById("trace").checked)
            append(pre, message);
    } else if (isInfo(tmp)) {
        if (document.getElementById("info").checked)
            append(pre, message);
    } else if (isDebug(tmp)) {
        if (document.getElementById("debug").checked)
            append(pre, message);
    } else {
        append(pre, message);
    }
    if (!pause)
        output.scrollTop = output.scrollHeight;

}
function down() {
    output.scrollTop = output.scrollHeight;
}
function selectLog(logName) {
    if (logName.value === "-1") return;
    window.location = "log?name=" + encodeURI(logName.value);
}
