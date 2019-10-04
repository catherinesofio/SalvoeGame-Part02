$(document).ready(function() {
    gamePlayerId = getParamObj(window.location.search).gp;
    checkGameState();
})

function checkGameState() {
    $.ajax("/api/game_view/"+gamePlayerId).done(function (data) {
        let gp = data.gamePlayers.filter(x => x.id == gamePlayerId)[0];

        switch (data.state) {
            case "WAITING":
                loadWaitingTable(data, gp.state == "WAITING_PLAYER");
                break;

            case "PLAYING":
                loadPlayerTables(data, true);
                break;

            case "FINISHED":
                loadPlayerResults(data);
                break;
        };
    }).fail(() => window.location = "/web/games.html" );
}

function loadWaitingTable(data, hasPlacedShips) {
    if (hasPlacedShips) {
        loadPlayerTables(data, false);
    } else {
        let table = createTable("player", username, cellsX, cellsY, []);
        container.appendChild(table);

        $.ajax("/api/templates/ships").done(data => createDummyShips(data));
    }
}

function loadPlayerTables(data, hasStarted) {
    let gp = data.gamePlayers.filter(x => x.id == gamePlayerId)[0];
    let gp2 = (data.gamePlayers.length > 1) ? data.gamePlayers.filter(x => x.id != gamePlayerId)[0] : null;

    let tableGP = createTable("player", username, cellsX, cellsY, [{ name: "empty", value: true }]);
    container.appendChild(tableGP);
    gp.ships.forEach(function (ship) {
        setElementsAttributes("player-", ship.locations, [{ name: "empty", value: false }, { name: "type", value: ship.type }, { name: "hasSalvo", value: false }]);
    });
    gp.salvoes.forEach(function (salvo) {
        setElementsAttributes("player-", salvo.locations, [{ name: "hasSalvo", value: true }, {name: "turn", value: salvo.turn}]);
    });

    let tableGP2 = createTable("enemy", (gp2 != null) ? gp2.player.name : "Waiting for player...", cellsX, cellsY, [{ name: "hasSalvo", value: false }]);
    container.appendChild(tableGP2);
    if (gp2 != null) {
        gp2.salvoes.forEach(function (salvo) {
            setElementsAttributes("enemy-", salvo.locations, [{ name: "hasSalvo", value: true }, { name: "turn", value: salvo.turn }]);
        });
    }
}

function loadPlayerResults(data) {
    let resultsContainer = document.createElement("div");
    resultsContainer.setAttribute("id", "results");
    container.appendChild(resultsContainer);

    let title = document.createElement("h1");
    title.innerText = "GAME " + data.id;
    resultsContainer.appendChild(title);

    resultsContainer.appendChild(createPlayerResults(data.gamePlayers.filter(x => x.id == gamePlayerId)[0], "player"));
    resultsContainer.appendChild(createPlayerResults(data.gamePlayers.filter(x => x.id != gamePlayerId)[0], "enemy"));
}

function createPlayerResults(player, id) {
    let gp = document.createElement("div");
    gp.setAttribute("id", id);

    let title = document.createElement("h2");
    title.innerHTML = player.player.name;
    gp.appendChild(title);

    let log = document.createElement("p");
    log.innerHTML = player.state;
    gp.appendChild(log);

    return gp;
}