let maxSalvoes;

let status;
let salvoesDisplay;

let id = "enemy";
let locations = [];

function createSalvoNav() {
    $.get("/templates/salvoes").done(function(data) {
        maxSalvoes = data;

        let salvoContainer = createElement("div", "", []);
        container.appendChild(salvoContainer);

        status = createElement("h2", "", [{ name: "id", value: "status" }]);
        salvoContainer.appendChild(status);

        salvoesDisplay = createElement("0", "", [{ name: "id", value: "salvoes" }]);
        salvoContainer.appendChild(salvoesCount);

        salvoesButton = createElement("button", "", [{ name: "id", value: "button-salvoes" }, { name: "disabled", value: true }, { name: "onclick", value: "submitSalvoes()" }]);
    });
}

function updateSalvoNav(status, isTurn) {
    let submitBtn = document.getElementById("button-salvoes");

    status.innerText = status;
    enableEvents(isTurn);

    if (isTurn) {
        salvoesDisplay.style.display = "block";

        submitBtn.setAttribute("disabled", false);
    } else {
        salvoesDisplay.style.display = "none";

        submitBtn.setAttribute("disabled", true);
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
}

function removeSalvo(e) {
    let cellId = e.target.getAttribute(id).substring(id.length);

    locations.remove(locations.indexOf(cellId));
    e.target.setAttribute("hasSalvo", false);
}

function submitSalvoes() {
    let salvoes = { cells: locations };
    locations = [];

    $.post({ url: "/api/games/players/" + gamePlayerId + "/salvoes", data: JSON.stringify(salvoes), dataType: "text", contentType: "application/json" }).done(x => window.location.reload());
}