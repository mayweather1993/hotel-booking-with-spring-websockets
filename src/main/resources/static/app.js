function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}

function connect() {
    window.socket = new WebSocket("ws://localhost:8080/booking/listen");
    setConnected(true);
    socket.onmessage = function (event) {
        showUris(event.data);
    }
}

function disconnect() {
    window.socket.close();
    setConnected(false);
    console.log("Disconnected");
}

function sendSub() {
    window.socket.send("sub," + $("#sub").val());
}

function drawRow(rowData) {
    var t = $('#conversation').DataTable();
    t.row.add([
        rowData.uri,
        rowData.method,
        rowData.responseCode,
        rowData.responseTime,
        rowData.responseLength,
        rowData.responseBody
    ]).draw(false);
}

function showUris(message) {
    var json = $.parseJSON(message);
    drawRow(json);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendSub();
    });
});

$(document).ready(function () {
    $('#conversation').DataTable();
});