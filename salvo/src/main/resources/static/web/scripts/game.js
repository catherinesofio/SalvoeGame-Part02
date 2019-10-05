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
        let table = createTable(playerId, username, cellsX, cellsY, []);
        container.appendChild(table);

        createDummyShips();
    }
}

function loadPlayerTables(data, hasStarted) {
    let gp = data.gamePlayers.filter(x => x.id == gamePlayerId)[0];
    let gp2 = (data.gamePlayers.length > 1) ? data.gamePlayers.filter(x => x.id != gamePlayerId)[0] : null;

    let tableGP = createTable(playerId, username, cellsX, cellsY, [{ name: "empty", value: true }]);
    container.appendChild(tableGP);
    gp.ships.forEach(function (ship) {
        setElementsAttributes(playerId + "-", ship.locations, [{ name: "empty", value: false }, { name: "type", value: ship.type }, { name: "hasSalvo", value: false }]);
    });
    if (gp2 != null) {
        gp2.salvoes.forEach(function (salvo) {
            element = setElementAttributes(playerId + "-" + salvo.cell, [{ name: "hasSalvo", value: true }, { name: "turn", value: salvo.turn }]);
            element.innerText = salvo.turn;
        });
    }

    let tableGP2 = createTable(oponentId, (gp2 != null) ? gp2.player.name : "Waiting for player...", cellsX, cellsY, [{ name: "hasSalvo", value: false }]);
    container.appendChild(tableGP2);
    gp.salvoes.forEach(function (salvo) {
        element = setElementAttributes(oponentId + "-" + salvo.cell, [{ name: "hasSalvo", value: true }, {name: "turn", value: salvo.turn}]);
        element.innerText = salvo.turn;
    });

    createSalvoNav(gp.state);
}

function loadPlayerResults(data) {
    let resultsContainer = document.createElement("div");
    resultsContainer.setAttribute("id", "results");
    container.appendChild(resultsContainer);

    let title = document.createElement("h1");
    title.innerText = "GAME " + data.id;
    resultsContainer.appendChild(title);

    resultsContainer.appendChild(createPlayerResults(data.gamePlayers.filter(x => x.id == gamePlayerId)[0], playerId));
    resultsContainer.appendChild(createPlayerResults(data.gamePlayers.filter(x => x.id != gamePlayerId)[0], oponentId));
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