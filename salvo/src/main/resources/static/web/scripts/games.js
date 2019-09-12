$(document).ready(function () {
    $.ajax("/api/players").done((data) => fillLeaderboardTable(data));
    $.ajax("/api/games").done((data) => {
        if (data.player != null) {
            let matches = document.createElement("table");
            matches.innerHTML = "<thead><tr><th>game</th><th>player 1</th><th>player 2</th></tr></thead><tbody id='matches-content'></tbody>";
            document.body.appendChild(matches);

            let newMatch = document.createElement("button");
            newMatch.onclick = createGame;
            newMatch.innerHTML = "create match";
            document.body.appendChild(newMatch);

            fillMatchesTable(data.games, data.player);
        }
    });
});

function fillTable(id, rows) {
    let tbody = $("#" + id)[0];
    let tr;

    let c = rows.length;
    let r = rows[0].length;
    for (var i = 0; i < r; i++) {
        tr = document.createElement("tr");
        tbody.appendChild(tr);

        for (var j = 0; j < c; j++) {
            let td = document.createElement("td")
            td.innerHTML = rows[j][i];
            tr.appendChild(td);
        }
    }
}

function fillLeaderboardTable(data) {
    let sortedData = data.sort(function(a, b) {
        if (a.scores.length == 0) {
            return 1;
        } else if (b.scores.length == 0) {
            return -1;
        }

        let sumA = a.scores.reduce(function(sum, x) { return sum + x; });
        let sumB = b.scores.reduce(function(sum, x) { return sum + x; });

        return (sumB > sumA) ? 1 : -1;
    });

    let users = sortedData.map(x => x.name);
    let total = sortedData.map(function(x) {
        if (x.scores.length == 0) {
            return 0;
        }

        return x.scores.reduce(function(sum, x) { return sum + x; });
    });

    let scores = sortedData.map(x => x.scores);
    let won = scores.map(x => x.filter(y => y == 1).length);
    let lost = scores.map(x => x.filter(y => y == 0).length);
    let tied = scores.map(x => x.filter(y => y == 0.5).length);

    let rows = [users, total, won, lost, tied];

    fillTable("leaderboard-content", rows);
}

function fillMatchesTable(data, player) {
    let sortedData = data.sort(function(a, b) {
        let dateA = new Date(a.created);
        let dateB = new Date(b.created);

        return (dateB < dateA) ? 1 : -1;
    });

    let games = sortedData.map(x => x.id);
    let players1 = sortedData.map(x => x.gamePlayers[0].player.name);
    let players2 = sortedData.map(function(x) {
        return (x.gamePlayers.length > 1) ? x.gamePlayers[1].player.name : (x.gamePlayers[0].player.id != player.id) ? "<button id='" + x.id + "' onclick='joinGame(event)'>join</button>" : "N/A";
    });

    let rows;
    if (player != null) {
        let count = sortedData.length;
        let matches = sortedData.map(x => {
            for(let i = x.gamePlayers.length - 1; i >= 0; i--){
                if (x.gamePlayers[i].player.id == player.id) {
                    return "<button id='" + x.gamePlayers[i].id + "' onclick='loadGame(event)'>PLAY</button>"
                }

                if (i == 0) {
                    return "";
                }
            }
         });
        
        rows = [games, players1, players2, matches];
    } else {
        rows = [games, players1, players2];
    }

    fillTable("matches-content", rows);
}

function joinGame(e) {
    e.preventDefault();

    $.post("/api/game/" + e.target.getAttribute("id") + "/players").done(id => { window.location = "/web/game.html?gp=" + id; }).fail(() => alert("my bad dude"));
}

function loadGame(e) {
    e.preventDefault();

    window.location = "/web/game.html?gp=" + e.target.getAttribute("id");
}

function createGame() {
    $.post("/api/games").done(gp => { window.location = "/web/game.html?gp=" + gp; }).fail(() => alert("my bad dude"));
}