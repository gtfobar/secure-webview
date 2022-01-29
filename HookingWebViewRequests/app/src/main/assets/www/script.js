host = "na1wr039p8096mzftfp82tq6dxjn7c.burpcollaborator.net";
count = 0;

function xhr(method, host) {
    let xhr = new XMLHttpRequest();
    xhr.onload = function () { console.log(xhr.responseText); };
    xhr.open(method, "http://" + host + "?" + count);
    xhr.send();
    count += 1;
}

function wsOpen(host) {
    var ssock = new WebSocket("ws://" + host) + "?" + count;
    count += 1;
}

window.onload = function() {
    document.getElementById("xhr-get").onclick = xhr.bind(null, "GET", host);
    document.getElementById("xhr-post").onclick = xhr.bind(null, "POST", host);
    document.getElementById("xhr-delete").onclick = xhr.bind(null, "DELETE", host);
    document.getElementById("ws-open").onclick = wsOpen.bind(null, host);
}