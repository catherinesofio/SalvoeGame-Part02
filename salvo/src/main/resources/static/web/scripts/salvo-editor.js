let maxSalvoes;

let status;
let salvoesDisplay;

let salvoesCount = 0;
let locations = [];

function createSalvoNav(data) {
    $.ajax("/api/templates/salvoes").done(function(data) {
        maxSalvoes = data;

        let salvoContainer = createElement("div", "", []);
        salvoContainer.setAttribute("id", "salvo-container");
        container.insertBefore(salvoContainer, container.firstChild);

        status = createElement("h2", "", [{ name: "id", value: "status" }]);
        salvoContainer.appendChild(status);

        salvoesDisplay = createElement("p", "", [{ name: "id", value: "salvoes" }]);
        salvoContainer.appendChild(salvoesDisplay);

        salvoesButton = createElement("button", "", [{ name: "id", value: "submit-salvoes" }, { name: "disabled", value: true }, { name: "onclick", value: "submitSalvoes()" }]);
        salvoesButton.innerText = "SUBMIT SALVOES";
        salvoContainer.appendChild(salvoesButton);
    }).done(x => updateSalvoNav(data));
}

function updateSalvoNav(state) {
    let isTurn = (state == "PLAYING_TURN");

    status.innerText = state;
    enableEvents(isTurn);

    if (isTurn) {
        salvoesCount = maxSalvoes;
        salvoesDisplay.innerText = salvoesCount;

        salvoesDisplay.style.display = "block";
    } else {
        salvoesDisplay.style.display = "none";
    }
}

function enableEvents(enable) {
    let cells = document.getElementById(opponentId).querySelectorAll("td[hasSalvo=false]");

    if (enable) {
        cells.forEach(x => x.setAttribute("onclick", "placeSalvo(event)"));
    } else {
        cells.forEach(x => x.setAttribute("onclick", ""));
    }
}

function placeSalvo(e) {
    if (salvoesCount > 0) {
        let cellId = e.target.getAttribute("id").substring(opponentId.length + 1);

        locations.push(cellId);
        e.target.setAttribute("hasSalvo", true);
        e.target.setAttribute("onclick", "removeSalvo(event)");

        salvoesCount -= 1;
        salvoesDisplay.innerText = salvoesCount;

        if (salvoesCount == 0) {
            document.getElementById("submit-salvoes").removeAttribute("disabled");
        }
    }
}

function removeSalvo(e) {
    let cellId = e.target.getAttribute("id").substring(opponentId.length + 1);

    locations.splice(locations.indexOf(cellId), 1);
    e.target.setAttribute("hasSalvo", false);
    e.target.setAttribute("onclick", "placeSalvo(event)");

    salvoesCount += 1;
    salvoesDisplay.innerText = salvoesCount;

    if (salvoesCount > 0) {
        document.getElementById("submit-salvoes").setAttribute("disabled", true);
    }
}

function submitSalvoes() {
    document.getElementById("submit-salvoes").setAttribute("disabled", true);

    let salvoes = [];
    for (let i = locations.length - 1; i >= 0; i--) {
        document.getElementById(opponentId + "-" + locations[i]).setAttribute("onclick", "");

        salvoes.push({ cell: locations[i] });
    }
    locations = [];

    $.post({ url: "/api/games/players/" + gamePlayerId + "/salvoes", data: JSON.stringify(salvoes), dataType: "text", contentType: "application/json" }).done(() => {
        clearInterval(interval);
        updateGameView();
    });
}