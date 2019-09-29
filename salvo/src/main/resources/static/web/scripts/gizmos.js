let gizmoObj;
let isSelected = false;
let prevParent;
let directions = ["north", "south", "west", "east"];
let positionRef;
let cellsY = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
let cells = cellsY.length;
let id = "player-";
let root;

$(document).ready(x => root = document.documentElement);

function createGizmoObj() {
    gizmoObj = createElement("div", "gizmo-obj", []);
    positionRef = document.getElementById(id + cellsY[(cells / 2) - 1] + cells / 2);

    let container = document.getElementById("container");
    container.appendChild(gizmoObj);

    for(let i = 0; i < 4; i++) {
        gizmoObj.appendChild(createElement("div", "gizmo-position", [{ name: "direction", value: directions[i] }, { name: "isBlocked", value: false }, { name: "onclick", value: "triggerMove(event)" }]));
    }

    gizmoObj.appendChild(createElement("div", "gizmo-rotation", [{ name: "onclick", value: "triggerRotate(event)" }]));
}

function triggerSelect(e) {
    let obj = e.target;

    if (gizmoObj == null) {
        createGizmoObj(e.target);
    } else if (isSelected) {
        prevParent.appendChild(gizmoObj.getElementsByClassName("ship")[0]);
    }

    setGizmoObj(obj);
}

function setGizmoObj(obj) {
    let size = obj.getAttribute("size");
    isSelected = true;
    prevParent = obj.parentNode;
    gizmoObj.appendChild(obj);
    positionRef.appendChild(gizmoObj);
    resetPositionGizmos();
    resizeGizmoObj(obj.getAttribute("orientation"), size);
}
function triggerMove(e) {
    let parent = gizmoObj.parentNode;
    let parentId = parent.getAttribute("id");
    let cellX = parseInt(parentId.charAt(parentId.length - 1));
    let cellY = cellsY.indexOf(parentId.charAt(parentId.length - 2));

    switch (e.target.getAttribute("direction")) {
        case "north":
            cellY -= 1;
            parent = parent.parentNode.previousSibling.querySelector("#" + id + cellsY[cellY] + cellX);
            parent.appendChild(gizmoObj);

            if (cellY == 0) {
                e.target.setAttribute("isBlocked", true);
            } else if (cellY < cells - 1) {
                gizmoObj.querySelector('div[direction="south"]').setAttribute("isBlocked", false);
            }
            break;

        case "south":
            cellY += 1;
            parent = parent.parentNode.nextSibling.querySelector("#" + id + cellsY[cellY] + cellX);
            parent.appendChild(gizmoObj);

            if (cellY == cells - 1) {
                e.target.setAttribute("isBlocked", true);
            } else if (cellY > 0) {
                gizmoObj.querySelector('div[direction="north"]').setAttribute("isBlocked", false);
            }
            break;

        case "west":
            cellX -= 1;
            parent.previousSibling.appendChild(gizmoObj);

            if (cellX == 1) {
                e.target.setAttribute("isBlocked", true);
            } else if (cellX < cells) {
                gizmoObj.querySelector('div[direction="east"]').setAttribute("isBlocked", false);
            }
            break;

        case "east":
            cellX += 1;
            parent.nextSibling.appendChild(gizmoObj);

            if (cellX == cells) {
                e.target.setAttribute("isBlocked", true);
            } else if (cellX > 1) {
                gizmoObj.querySelector('div[direction="west"]').setAttribute("isBlocked", false);
            }
            break;
    }
}

function triggerRotate(e) {
    let obj = e.target.parentNode.getElementsByClassName("ship")[0];

    let orientation = (obj.getAttribute("orientation") == "horizontal") ? "vertical" : "horizontal";
    obj.setAttribute("orientation", orientation);

    resizeGizmoObj(orientation, obj.getAttribute("size"));
}

function resetPositionGizmos() {
    gizmoObj.querySelectorAll(".gizmo-position").forEach(x => x.setAttribute("isBlocked", false));
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

//j.bauer@ctu.gov