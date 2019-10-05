function createGameLog(data) {
    let logContainer = createElement("ul", "", [{ name: "id", value: "log-container" }]);
    container.appendChild(logContainer);

    let log;
    let date;
    let message;
    data.log.forEach(function(x) {
        log = createElement("li", "log", []);
        logContainer.appendChild(log);

        message = createElement("p", "log-message", []);
        message.innerText = getCleanMessage(data.players.filter(gp => gp.id == x.gamePlayerId)[0].player, x.message, x.params);
        log.appendChild(message);

        date = createElement("span", "log-date", []);
        date.innerText = x.date;
        log.appendChild(date);
    });
}

function getCleanMessage(gamePlayer, message, params) {
    if (params != null) {
        if (params.length > 1) {
            params = params.join(', ');
        } else if (params.length == 1) {
            params = params[0];
        }

        message = message.replace("{st}", params);
        message = message.replace("{ss}", params);
    }

    message = message.replace("{gp}", gamePlayer.name);

    return message;
}