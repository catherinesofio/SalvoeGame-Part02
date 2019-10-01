$(document).ready(function() {
    let param = getParamObj(window.location.search);
    $.ajax("/api/game_view/"+param.gp).done(function (data) {
        let gp = data.gamePlayers.filter(x => x.id == param.gp)[0];
        switch (data.state) {
            case "WAITING":
                loadWaitingTable(data, gp, gp.state == "WAITING_PLAYER");
            break;

            case "PLAYING":
                loadPlayerTables(data, gp.id);
            break;

            case "FINISHED":
                loadPlayerResults(data, gp.id);
            break;
        };
    }).fail(() => window.location = "/web/games.html" );
})

function loadWaitingTable(data, gp, hasPlacedShips) {
    if (hasPlacedShips) {
        loadTables(data, gp.id)
    } else {
        let table = createTable("player", gp.player.name, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"], [{ name: "empty", value: true }]);
        container.appendChild(table);

        $.ajax("/api/templates/ships").done(data => createDummyShips(data));
    }
}

function loadPlayerTables(data, id) {

}

function loadPlayerResults(data, id) {
    let container = document.createElement("div");
    container.setAttribute("id", "results");
    $(document.body)[0].appendChild(container);

    let title = document.createElement("h1");
    title.innerText = "GAME " + data.id;
    container.appendChild(title);

    createPlayerResults(data.gamePlayers.filter(x => x.id == id)[0], "player");
    createPlayerResults(data.gamePlayers.filter(x => x.id != id)[0], "enemy");
}

function createPlayerResults(player, id) {
    let container = document.getElementById("results");

    let gp = document.createElement("div");
    gp.setAttribute("id", id);
    container.appendChild(gp);

    let title = document.createElement("h2");
    title.innerHTML = player.name;
    container.appendChild(title);

    let log = document.createElement("p");
    log.innerHTML = player.status;
    container.appendChild(log);
}