let maxSalvoes;

let status;
let salvoesDisplay;

let id = "enemy";
let locations = [];

function createSalvoNav(data) {
    $.get("/templates/salvoes").done(function(data) {
        maxSalvoes = data;

        let salvoContainer = createElement("div", "", []);
        container.appendChild(salvoContainer);

        status = createElement("h2", "", [{ name: "id", value: "status" }]);
        salvoContainer.appendChild(status);

        salvoesDisplay = createElement("0", "", [{ name: "id", value: "salvoes" }]);
        salvoContainer.appendChild(salvoesCount);

        salvoesButton = createElement("button", "", [{ name: "id", value: "button-salvoes" }, { name: "disabled", value: true }, { name: "onclick", value: "submitSalvoes()" }]);
    }).done(x => updateSalvoNav(data));
}

function updateSalvoNav(state) {
    let isTurn = (state == "PLAYING_TURN");

    status.innerText = state;
    enableEvents(isTurn);

    if (isTurn) {
        salvoesDisplay.style.display = "block";
    } else {
        salvoesDisplay.style.display = "none";
    }
}

function enableEvents(enable) {
    let cells = document.getElementById(id).querySelectorAll("td[hasSalvo=false]");

    if (enable) {
        cells.forEach(x => x.setAttribute("onclick", "placeSalvo(event)"));
    } else {
        cells.forEach(x => x.setAttribute("onclick", ""));
    }
}

function placeSalvo(e) {
    let cellId = e.target.getAttribute(id).substring(id.length);

    locations.push(cellId);
    e.target.setAttribute("hasSalvo", true);
    e.target.setAttribute("onclick", "removeSalvo(event)");

    if (cells.length == maxSalvoes) {
        document.getElementById("button-salvoes").setAttribute("disabled", false);
    }
}

function removeSalvo(e) {
    let cellId = e.target.getAttribute(id).substring(id.length);

    locations.remove(locations.indexOf(cellId));
    e.target.setAttribute("hasSalvo", false);
    e.target.setAttribute("onclick", "placeSalvo(event)");

    if (locations.length == maxSalvoes - 1) {
        document.getElementById("button-salvoes").setAttribute("disabled", true);
    }
}

function submitSalvoes() {
    document.getElementById("button-salvoes").setAttribute("disabled", true);

    let salvoes = [];
    for (let i = locations.length - 1; i >= 0; i--) {
        document.getElementById(id + "-" + locations[i]).setAttribute("onclick", "");
        salvoes.push({ cell: locations[i] });
    }
    locations = [];

    $.post({ url: "/api/games/players/" + gamePlayerId + "/salvoes", data: JSON.stringify(salvoes), dataType: "text", contentType: "application/json" }).done(x => console.log("enviados perro"));
}