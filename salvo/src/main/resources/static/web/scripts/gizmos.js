let root;

let id = "player-";
let cellsY = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
let cells = cellsY.length;

let obj;
let gizmoObj;
let handlers = [];

let prevParent;
let initPos;

let cellX = cells / 2;
let cellY = parseInt(cells / 4);

let isSelected = false;
let directions = ["north", "south", "west", "east"];

$(document).ready(x => root = document.documentElement);

function createGizmoObj() {
    gizmoObj = createElement("div", "gizmo-obj", []);
    initPos = document.getElementById(id + cellsY[cellY] + cellX);

    let container = document.getElementById("container");
    container.appendChild(gizmoObj);

    for(let i = 0; i < 4; i++) {
        let handler = createElement("div", "gizmo-position", [{ name: "direction", value: directions[i] }, { name: "isBlocked", value: false }, { name: "onclick", value: "triggerMove(event)" }]);
        handlers.push(handler);

        gizmoObj.appendChild(handler);
    }

    gizmoObj.appendChild(createElement("div", "gizmo-rotation", [{ name: "onclick", value: "triggerRotate(event)" }]));
}

function resetGizmoObj() {
    obj.setAttribute("onclick", "");

    prevParent = obj.parentNode;
    gizmoObj.appendChild(obj);
    initPos.appendChild(gizmoObj);
    cellX = cells / 2;
    cellY = parseInt(cells / 4);

    isSelected = true;

    resetGizmoHandlers();
    resizeGizmoObj(obj.getAttribute("orientation"), obj.getAttribute("size"));
}

function resetGizmoHandlers() {
    handlers.forEach(x => x.setAttribute("isBlocked", false));
}

function resizeGizmoObj(orientation, size) {
    if (orientation == "horizontal") {
        root.style.setProperty("--gizmo-width", size);
        root.style.setProperty("--gizmo-height", 1);
    } else {
        root.style.setProperty("--gizmo-width", 1);
        root.style.setProperty("--gizmo-height", size);
    }
}

function repositionGizmoObj(offsetX, offsetY, orientation, size) {
    cellX += offsetX;
    cellY += offsetY;

    let parent = document.getElementById(id + cellsY[cellY] + "" + cellX);
    parent.appendChild(gizmoObj);
}

function checkGizmoHandlers(orientation, size) {
    let offsetX = 0;
    let offsetY = 0;

    switch(orientation) {
        case "horizontal":
            let dx = cellX + size - 1;
            offsetX -= size - 1;
            offsetY -= 1;

            if (cells < dx) {
                repositionGizmoObj((dx - cells) * - 1, 0, orientation, size);
                handlers[3].setAttribute("isBlocked", true);
            }
            break;

        case "vertical":
            let dy = cellY + size;
            offsetY -= size;

            if (cells < dy) {
                repositionGizmoObj(0, (dy - cells) * - 1, orientation, size);
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

function triggerSelect(e) {
    if (gizmoObj == null) {
        createGizmoObj();
    } else if (isSelected) {
        obj.setAttribute("onclick", "triggerSelect(event)");
        prevParent.appendChild(obj);
    }

    obj = e.target;
    resetGizmoObj();
}

function triggerMove(e) {
    let parent = gizmoObj.parentNode;
    let orientation = obj.getAttribute("orientation");
    let size = parseInt(obj.getAttribute("size"));

    switch (e.target.getAttribute("direction")) {
        case "north":
            cellY -= 1;
            parent = parent.parentNode.previousSibling.querySelector("#" + id + cellsY[cellY] + cellX);
            parent.appendChild(gizmoObj);
            break;

        case "south":
            cellY += 1;
            parent = parent.parentNode.nextSibling.querySelector("#" + id + cellsY[cellY] + cellX);
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

    checkGizmoHandlers(orientation, size);
    resizeGizmoObj(orientation, size);
}

function triggerRotate(e) {
    let orientation = (obj.getAttribute("orientation") == "horizontal") ? "vertical" : "horizontal";
    obj.setAttribute("orientation", orientation);

    let size = parseInt(obj.getAttribute("size"));

    checkGizmoHandlers(orientation, size);
    resizeGizmoObj(orientation, size);
}
//j.bauer@ctu.gov