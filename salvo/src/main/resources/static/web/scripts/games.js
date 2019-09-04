$(document).ready(function () {
    $.ajax("/api/players").done((data) => fillLeaderboardTable(data));
    $.ajax("/api/games").done((data) => fillMatchesTable(data));
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

    let users = sortedData.map(x => x.email.split("@")[0]);
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

function fillMatchesTable(data) {
    let sortedData = data.sort(function(a, b) {
        let dateA = new Date(a.created);
        let dateB = new Date(b.created);

        return (dateB < dateA) ? 1 : -1;
    });

    let games = sortedData.map(x => x.id);
    let players1 = sortedData.map(x => x.gamePlayers[0].player.email);
    let players2 = sortedData.map(function(x) {
        return (x.gamePlayers.length > 1) ? x.gamePlayers[1].player.email : "N/A";
    });

    let rows = [games, players1, players2];

    fillTable("matches-content", rows);
}