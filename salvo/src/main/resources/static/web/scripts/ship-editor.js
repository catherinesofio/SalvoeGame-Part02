let obj;
let size;
let orientation;
let prevOrientation;
let positions;

let gizmoObj;
let handlers = [];
let buttons = [];

let prevParent;
let initPos;

let cellX = cells / 2;
let cellY = parseInt(cells / 4);

let isSelected = false;
let directions = ["north", "south", "west", "east"];

let occupiedCells = [];

function createDummyShips() {
    $.ajax("/api/templates/ships").done(function(data) {
        let ships = document.createElement("div");
        ships.setAttribute("id", "ship-container");
        container.appendChild(ships);

        data.forEach(function (x) {
            let ship = createElement("div", "ship", [ { name: "type", value: x.type }, { name: "size", value: x.size }, { name: "orientation", value: "horizontal" }, { name: "positions", value: "" }, { name: "onclick", value: "triggerSelect(event)" } ]);

            ships.appendChild(ship);
        });

        let btn = createElement("button", "", [{ name: "id", value: "submit-ships" }, { name: "disabled", value: true }, { name: "onclick", value: "submitShips()" }]);
        btn.innerText = "SUBMIT SHIPS";
        ships.appendChild(btn);
    });
}

function createGizmoObj() {
    gizmoObj = createElement("div", "gizmo-obj", []);
    initPos = document.getElementById(playerId + "-" + cellsY[cellY] + cellX);

    container.appendChild(gizmoObj);

    for(let i = 0; i < 4; i++) {
        let handler = createElement("div", "gizmo-position", [{ name: "direction", value: directions[i] }, { name: "isBlocked", value: false }, { name: "onclick", value: "triggerMove(event)" }]);
        handlers.push(handler);

        gizmoObj.appendChild(handler);
    }

    gizmoObj.appendChild(createElement("div", "gizmo-rotation", [{ name: "onclick", value: "triggerRotate()" }]));

    let btn = createElement("div", "gizmo-button", [{ name: "id", value: "gizmo-accept" }, { name: "isEnabled", value: true }, { name: "onclick", value: "triggerPlace()" }]);
    gizmoObj.appendChild(btn);
    buttons.push(btn);

    btn = createElement("div", "gizmo-button", [{ name: "id", value: "gizmo-cancel" }, { name: "isEnabled", value: true }, { name: "onclick", value: "triggerUnselect()" }]);
    gizmoObj.appendChild(btn);
    buttons.push(btn);
}

function resetGizmoObj() {
    size = parseInt(obj.getAttribute("size"));
    prevOrientation = orientation = obj.getAttribute("orientation");
    positions = obj.getAttribute("positions").split("-");

    obj.setAttribute("onclick", "");

    prevParent = obj.parentNode;
    gizmoObj.appendChild(obj);

    if (positions != "") {
        prevParent.appendChild(gizmoObj);

        let parentId = prevParent.getAttribute("id");
        cellX = parseInt(parentId.substring(playerId.length + 2));
        cellY = cellsY.indexOf(parentId.charAt(playerId.length + 1));

        checkGizmoHandlers(orientation, size);
    } else {
        initPos.appendChild(gizmoObj);
        cellX = cells / 2;
        cellY = parseInt(cells / 4);

        resetGizmoHandlers();
    }

    isSelected = true;
    gizmoObj.style.display = "block";

    resizeGizmoObj();
}

function resetGizmoHandlers() {
    handlers.forEach(x => x.setAttribute("isBlocked", false));
}

function resizeGizmoObj() {
    if (orientation == "horizontal") {
        root.style.setProperty("--gizmo-width", size);
        root.style.setProperty("--gizmo-height", 1);
    } else {
        root.style.setProperty("--gizmo-width", 1);
        root.style.setProperty("--gizmo-height", size);
    }
}

function repositionGizmoObj(offsetX, offsetY) {
    cellX += offsetX;
    cellY += offsetY;

    let parent = document.getElementById(playerId + "-" + cellsY[cellY] + "" + cellX);
    parent.appendChild(gizmoObj);
}

function unselectGizmoObj(isPlaced) {
    if (!isPlaced) {
        obj.setAttribute("orientation", prevOrientation);
        prevParent.appendChild(obj);
    } else {
        gizmoObj.parentNode.appendChild(obj);
        obj.style.position = "absolute";
    }

    obj.setAttribute("onclick", "triggerSelect(event)");

    isSelected = false;
    gizmoObj.style.display = "none";
}

function checkGizmoHandlers() {
    let offsetX = 0;
    let offsetY = 0;

    switch(orientation) {
        case "horizontal":
            let dx = cellX + size - 1;
            offsetX -= size - 1;
            offsetY -= 1;

            if (cells < dx) {
                repositionGizmoObj((dx - cells) * - 1, 0);
                handlers[3].setAttribute("isBlocked", true);
            }
            break;

        case "vertical":
            let dy = cellY + size;
            offsetY -= size;

            if (cells < dy) {
                repositionGizmoObj(0, (dy - cells) * - 1);
                handlers[1].setAttribute("isBlocked", true);
            }
            break;
    }

    switch(cellX) {
        case 1:
            handlers[2].setAttribute("isBlocked", true);
            break;

        case (cells + offsetX):
            handlers[3].setAttribute("isBlocked", true);
            break;

        default:
            handlers[2].setAttribute("isBlocked", false);
            handlers[3].setAttribute("isBlocked", false);
            break;
    }

    switch(cellY) {
        case 0:
            handlers[0].setAttribute("isBlocked", true);
            break;

        case (cells + offsetY):
            handlers[1].setAttribute("isBlocked", true);
            break;

        default:
            handlers[0].setAttribute("isBlocked", false);
            handlers[1].setAttribute("isBlocked", false);
            break;
    }
}

function checkObjPosition() {
    let offsetX = 0;
    let offsetY = 0;
    if (orientation == "horizontal") {
        offsetX = 1;
    } else {
        offsetY = 1;
    }

    let newPositions = [];
    for (let i = 0; i < size; i++) {
        newPositions.push(cellsY[(cellY + (offsetY * i))] + "" + (cellX + (offsetX * i)));
    }

    for (let i = 0; i < size; i++) {
        if (occupiedCells.indexOf(newPositions[i]) > - 1 && !positions.includes(newPositions[i])) {
            buttons[0].style.display = "none";
            return false;
        } else {
            buttons[0].style.display = "block";
        }
    }
}

function triggerSelect(e) {
    if (gizmoObj == null) {
        createGizmoObj();
    } else if (isSelected) {
        unselectGizmoObj(false);
    }

    obj = e.target;
    resetGizmoObj();
    checkObjPosition();
}

function triggerUnselect() {
    unselectGizmoObj(false);
}

function triggerMove(e) {
    let parent = gizmoObj.parentNode;

    switch (e.target.getAttribute("direction")) {
        case "north":
            cellY -= 1;
            parent = parent.parentNode.previousSibling.querySelector("#" + playerId + "-" + cellsY[cellY] + cellX);
            parent.appendChild(gizmoObj);
            break;

        case "south":
            cellY += 1;
            parent = parent.parentNode.nextSibling.querySelector("#" + playerId + "-" + cellsY[cellY] + cellX);
            parent.appendChild(gizmoObj);
            break;

        case "west":
            cellX -= 1;
            parent.previousSibling.appendChild(gizmoObj);
            break;

        case "east":
            cellX += 1;
            parent.nextSibling.appendChild(gizmoObj);
            break;
    }

    checkGizmoHandlers();
    resizeGizmoObj();
    checkObjPosition();
}

function triggerRotate() {
    orientation = (orientation == "horizontal") ? "vertical" : "horizontal";
    obj.setAttribute("orientation", orientation);

    checkGizmoHandlers();
    resizeGizmoObj();
    checkObjPosition();
}

function triggerPlace() {
    let offsetX = 0;
    let offsetY = 0;

    if (orientation == "horizontal") {
        offsetX = 1;
    } else {
        offsetY = 1;
    }

    for (var i = positions.length - 1; i >= 0; i--) {
        if (occupiedCells.includes(positions[i]))
        {
            occupiedCells.splice(occupiedCells.indexOf(positions[i]), 1);
        }
    }

    positions = "";
    for (let i = 0; i < size; i++) {
        let pos = cellsY[(cellY + (offsetY * i))] + "" + (cellX + (offsetX * i));
        positions = positions + pos + "-";
        occupiedCells.push(pos);
    }
    positions = positions.substring(0, positions.length - 1);
    obj.setAttribute("positions", positions);

    unselectGizmoObj(true);

    if (document.getElementById("ship-container").childElementCount == 1) {
        document.getElementById("submit-ships").disabled = false;
    }
}

function submitShips() {
    let data = document.getElementsByClassName("ship");

    let ships = [];
    for (let i = data.length - 1; i >= 0; i--){
        ships.push({ type: data[i].getAttribute("type"), locations: data[i].getAttribute("positions").split("-") });
    }

    $.post({ url: "/api/games/players/" + gamePlayerId + "/ships", data: JSON.stringify(ships), dataType: "text", contentType: "application/json" }).done(x => window.location.reload());
}