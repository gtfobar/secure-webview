url = "https://github.com/OWASP/java-html-sanitizer/tree/master#getting-started";
count = 0;

var script = document.createElement('script');

script.src = '//code.jquery.com/jquery-3.6.0.min.js';
document.getElementsByTagName('head')[0].appendChild(script);

function xhrGET(elem_id) {
    fetch(`${url}?q=${count}`, { mode: 'no-cors'})
      .then(r => r.text())
      .then(r => document.getElementById(elem_id).innerText = r);
	count += 1;
}

function xhrPOST(elem_id) {
    data = {"q": count.toString()};
    $.post(`${url}?q=${count}`, data);
    count += 1;
}

function xhrDELETE(elem_id) {
    $.ajax({
            url: `${url}?q=${count}`,
            type: 'DELETE',
        });
        count += 1;
}

function wsOpen() {
    var sock = new WebSocket(
        url.replace("http", "ws")
    );

    sock.onmessage = logMessage;
}

function logMessage(e) {
    var data = JSON.parse(e.data);
    console.log(data);
}