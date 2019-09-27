$(document).ready(function() {
    let param = getParamObj(window.location.search);
    $.ajax("/api/game_view/"+param.gp).done(function (data) {
        let gp = data.gamePlayers.filter(x => x.id == param.gp)[0];
        switch (data.state) {
            case "WAITING":
                loadWaitingTable(data, gp.id, gp.state == "WAITING_PLAYER");
            break;

            case "PLAYING":
                loadTables(data, gp.id);
            break;

            case "FINISHED":
                loadResults(data, gp.id);
            break;
        };
    }).fail(() => window.location = "/web/games.html" );
})

function getParamObj(search) {
  var obj = {};
  var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

  search.replace(reg, function(match, param, val) {
    obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
  });

  return obj;
}

function loadWaitingTable(data, id, hasPlacedShips) {
    if (hasPlacedShips) {
        loadTables(data, id)
    } else {
        $.ajax("/api/templates/ships").done(data => loadShips(data));
    }
}

function loadShips(data) {
    let container = document.createElement("ul");
    container.setAttribute("id", "ships");

    console.log(data);
}

function loadResults(data, id) {
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

function loadTables(data, id) {
    createPlayerTable(data.gamePlayers.filter(x => x.id == id)[0]);

    /*if (data.gamePlayers.length > 1) {
        createEnemyTable(data.gamePlayers.filter(x => x.id != id)[0]);
    } else {
        createEnemyTable({ "player": { "id": -1, "email": "Waiting for player...@" } , "ships": [], "salvoes": [] });
    }*/
}

function createPlayerTable(data) {
    let table = createTable("player", data, true);
}

function createEnemyTable(data) {
    let table = createTable("enemy", data, false);
}

function createTable(id, data, showShips) {
    let count = 10;
    let cols = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    let rows = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
    let cells = data.ships.map(x => x.locations).flat();
    let salvoesLocations = data.salvoes.map(x => x.locations).flat();
    let salvoesTurns = { };

    for (var i = data.salvoes.length - 1; i >= 0; i--) {
        let salvo = data.salvoes[i];
        for (var j = salvo.locations.length - 1; j >= 0; j--) {
            salvoesTurns[salvo.locations[j]] = salvo.turn;
        }
    }

    let table = document.createElement("table");
    table.setAttribute("id", id);
    $(document.body)[0].appendChild(table);

    let caption = document.createElement("caption");
    caption.innerHTML = data.player.email.split("@")[0];
    table.appendChild(caption);

    let thead = document.createElement("thead");
    table.appendChild(thead);

    let tbody = document.createElement("tbody");
    table.appendChild(tbody);

    let th = document.createElement("th");
    th.setAttribute("scope", "col");
    thead.appendChild(th);

    for (let i = 0; i < count; i++) {
        th = document.createElement("th");
        th.setAttribute("scope", "col");
        th.innerHTML = cols[i];
        thead.appendChild(th);

        let tr = document.createElement("tr");
        tbody.appendChild(tr);

        th = document.createElement("th");
        th.setAttribute("scope", "row");
        th.innerHTML = rows[i];
        tr.appendChild(th);

        let empty = true;
        let cell = "";
        for (let j = 0; j < count; j++) {
            cell = rows[i] + cols[j];

            empty = (!cells.includes(cell));
            salvo = (salvoesLocations.includes(cell));

            let td = document.createElement("td");
            if (showShips) {
                td.setAttribute("empty", empty);
            }

            if (empty && salvo) {
                td.setAttribute("success", false);
                td.innerHTML = salvoesTurns[rows[i] + cols[j]];
            } else if (!empty && salvo) {
                td.setAttribute("success", true);
                td.innerHTML = salvoesTurns[cell];
            }

            tr.appendChild(td);
        }
    }
}