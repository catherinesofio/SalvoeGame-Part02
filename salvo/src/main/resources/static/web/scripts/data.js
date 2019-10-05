var root;
var container;
var username = "";
var gamePlayerId;
var playerId = "player";
var oponentId = "oponent";
var cellsX = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
var cellsY = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
var cells = cellsY.length;

$(document).ready(function() {
    root = document.documentElement;
    container = document.getElementById("container");
});