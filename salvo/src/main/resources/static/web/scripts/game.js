let turn;
let timeout = 5000;
let interval;

$(document).ready(function() {
    gamePlayerId = getParamObj(window.location.search).gp;
    checkGameState();
})

function checkGameState() {
    $.ajax("/api/game_view/"+gamePlayerId).done(function (data) {
        let gp = data.gamePlayers.filter(x => x.id == gamePlayerId)[0];
        let hideUpdates = false;
        turn = data.turn;

        switch (data.state) {
            case "WAITING":
                loadWaitingTable(data, gp.state == "WAITING_PLAYER");
                break;

            case "PLAYING":
                hideUpdates = data.gamePlayers.filter(x => x.state == "PLAYING_WAITING").length == 1;
                loadPlayerTables(data, true, hideUpdates);
                break;

            case "FINISHED":
                loadPlayerResults(data);
                break;
        };

        createGameLog({ players: data.gamePlayers.map(function(player) { return { id: player.id, player: player.player }; }), log: data.logs }, hideUpdates);
    }).fail(() => window.location = "/web/games.html" );
}

function loadWaitingTable(data, hasPlacedShips) {
    if (hasPlacedShips) {
        loadPlayerTables(data, false);

        let opponentState = (data.gamePlayers.length == 2) ? data.gamePlayers.filter(x => x.id != gamePlayerId)[0].state : "";

        interval = scheduleInterval(() => { updateWaitingView(opponentState); }, timeout);
    } else {
        let table = createTable(playerId, username, cellsX, cellsY, []);
        container.appendChild(table);

        createDummyShips();

        interval = scheduleInterval(updateShipsView, timeout);
    }
}

function updateShipsView() {
    $.ajax("/api/game_view/"+gamePlayerId).done(function(data) {
        let logCount = logContainer.childElementCount;

        if (logCount < data.logs.length) {
            data.logs.splice(0, logCount);

            addLogs({ players: data.gamePlayers.map(function (player) { return { id: player.id, player: player.player }; }), log: data.logs });

            if (data.gamePlayers.filter(x => x.id != gamePlayerId).state == "WAITING_PLAYER") {
                clearInterval(interval);

                return;
            }
        }

        interval = scheduleInterval(updateShipsView, timeout);
    });
}

function updateWaitingView(prevState) {
    $.ajax("/api/game_view/"+gamePlayerId).done(function(data) {
        let logCount = logContainer.childElementCount;

        if (logCount < data.logs.length) {
            data.logs.splice(0, logCount);

            addLogs({ players: data.gamePlayers.map(function (player) { return { id: player.id, player: player.player }; }), log: data.logs });
        }

        if (data.state == "PLAYING") {
            let gamePlayer = data.gamePlayers.filter(x => x.id == gamePlayerId)[0];
            let opponent = data.gamePlayers.filter(x => x.id != gamePlayerId)[0];

            document.getElementById(opponentId).getElementsByTagName("caption")[0].innerText = opponent.player.name + " - Ships: " + opponent.ships.activeShips;
            createSalvoNav(gamePlayer.state);

            clearInterval(interval);
            interval = scheduleInterval(updateGameView, timeout);

            return;
        } else if (data.gamePlayers.length == 2) {
            let opponent = data.gamePlayers.filter(x => x.id != gamePlayerId)[0];
            let state = opponent.state;

            if (state != prevState) {
                let caption = (state == "WAITING_PREPARING") ? opponent.player.name : opponent.player.name + " - Ships: " + gp2.ships.activeShips;
                document.getElementById(opponentId).getElementsByTagName("caption")[0].innerText = caption;
            }

            interval = scheduleInterval(() => { updateWaitingView(state); }, timeout);

            return;
        }

        interval = scheduleInterval(() => { updateWaitingView(""); }, timeout);
    });
}

function loadPlayerTables(data, hasStarted, hideUpdates) {
    let gp = data.gamePlayers.filter(x => x.id == gamePlayerId)[0];
    let gp2 = (data.gamePlayers.length > 1) ? data.gamePlayers.filter(x => x.id != gamePlayerId)[0] : null;

    let newData;
    if (hideUpdates) {
        if (gp.state == "PLAYING_WAITING") {
            newData = hideRefreshedUpdates(gp, gp2);
            gp = newData.player;
            gp2 = newData.opponent;
        } else {
            newData = hideRefreshedUpdates(gp2, gp);
            gp2 = newData.player;
            gp = newData.opponent;
        }
    }

    let tableGP = createTable(playerId, username + " - Ships: " + gp.ships.activeShips, cellsX, cellsY, [{ name: "empty", value: true }]);
    container.appendChild(tableGP);
    gp.ships.all.forEach(function (ship) {
        setElementsAttributes(playerId + "-", ship.locations, [{ name: "empty", value: false }, { name: "type", value: ship.type }, { name: "hasSalvo", value: false }, { name: "sunk", value: ship.isDown }]);
    });
    if (gp2 != null) {
        gp2.salvoes.forEach(function (salvo) {
            element = setElementAttributes(playerId + "-" + salvo.cell, [{ name: "hasSalvo", value: true }, { name: "turn", value: salvo.turn }, { name: "success", value: salvo.success }]);
            element.innerText = salvo.turn;
        });
    }

    let caption = (gp2 == null) ? "Waiting for player..." : (gp2.state.includes("PLAYING_")) ? gp2.player.name + " - Ships: " + gp2.ships.activeShips : gp2.player.name;
    let tableGP2 = createTable(opponentId, caption, cellsX, cellsY, [{ name: "hasSalvo", value: false }]);
    container.appendChild(tableGP2);
    if (gp.ships.sunkShips != null) {
        gp.ships.sunkShips.forEach(function (ship) {
            setElementsAttributes(opponentId + "-", ship.locations, [{ name: "empty", value: false }, { name: "type", value: ship.type }, { name: "hasSalvo", value: false }, { name: "sunk", value: ship.isDown }]);
        });
    }
    gp.salvoes.forEach(function (salvo) {
        element = setElementAttributes(opponentId + "-" + salvo.cell, [{ name: "hasSalvo", value: true }, {name: "turn", value: salvo.turn }, { name: "success", value: salvo.success }]);
        element.innerText = salvo.turn;
    });

    if (hideUpdates && newData.salvoes != null) {
        newData.salvoes.forEach(function(salvo) {
            setElementAttributes(opponentId + "-" + salvo.cell, [{ name: "hasSalvo", value: true }]);
        });
    }

    if (hasStarted) {
        createSalvoNav(gp.state);

        interval = scheduleInterval(updateGameView, timeout);
    }
}

function hideRefreshedUpdates(player, opponent) {
    let salvoes = null;

    if (player.salvoes != null) {
        salvoes = player.salvoes.filter(x => x.turn == turn);
        player.salvoes = player.salvoes.filter(x => x.turn != turn);
    }

    if (player.salvoes != null) {
        player.salvoes = player.salvoes.filter(x => x.turn != turn);
    }

    if (opponent.ships.sunkShips != null) {
        opponent.ships.sunkShips = opponent.ships.sunkShips.filter(x => x.sunkInTurn != turn);
    }

    return { player: player, salvoes: salvoes, opponent: opponent };
}

function updateGameView() {
    $.ajax("/api/game_view/"+gamePlayerId+"/turns/"+turn).done(function(data) {
        let gamePlayers = data.gamePlayers;

        if (turn != data.turn) {
            turn = data.turn;

            gamePlayers.forEach(gp => {
                if (gp.id == gamePlayerId) {
                    updatePlayerTable(playerId, opponentId, gp);
                } else {
                    updatePlayerTable(opponentId, playerId, gp);
                }
            });

            data.logs.splice(0, logContainer.childElementCount);
            addLogs({ players: gamePlayers.map(function (player) { return { id: player.id, player: player.player }; }), log: data.logs });
        }

        if (data.state != "FINISHED") {
            interval = scheduleInterval(updateGameView, timeout);
        } else {
            alert(gamePlayers.filter(x => x.id == gamePlayerId)[0].state);
            window.location.reload();
        }
    });
}

function updatePlayerTable(player, opponent, data) {
    updateSalvoNav(data.state);

    data.salvoes.forEach(function(salvo) {
        element = setElementAttributes(opponent + "-" + salvo.cell, [{ name: "hasSalvo", value: true }, { name: "turn", value: salvo.turn }, { name: "success", value: salvo.success }]);
        element.innerText = salvo.turn;
    });

    if (data.ships.sunkShips != null) {
        data.ships.sunkShips.forEach(function (ship) {
            setElementsAttributes(player + "-", ship.locations, [{ name: "empty", value: false }, { name: "type", value: ship.type }, { name: "sunk", value: true }]);
        });
    }

    document.getElementById(player).getElementsByTagName("caption")[0].innerText = data.player.name + " - Ships: " + data.ships.activeShips;
}

function loadPlayerResults(data) {
    let resultsContainer = document.createElement("div");
    resultsContainer.setAttribute("id", "results");
    container.appendChild(resultsContainer);

    let title = document.createElement("h1");
    title.innerText = "GAME " + data.id;
    resultsContainer.appendChild(title);

    resultsContainer.appendChild(createPlayerResults(data.gamePlayers.filter(x => x.id == gamePlayerId)[0], playerId));
    resultsContainer.appendChild(createPlayerResults(data.gamePlayers.filter(x => x.id != gamePlayerId)[0], opponentId));
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