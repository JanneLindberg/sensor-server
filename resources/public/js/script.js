var origin = 'ws://' + location.hostname + ':' + location.port + '/ws';

var socket = new WebSocket(origin);
socket.onmessage = function(event) {
  var a = JSON.parse(event.data);
  for (index = 0; index < a.length; ++index) {
    var p = a[index].param;
    var v = a[index].value;
    var desc = a[index].description;
    document.getElementById(p).innerHTML = v;
  }
}
