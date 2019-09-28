let gizmoObj;
let isSelected = false;
let prevParent;
let directions = ["north", "south", "west", "east"];

function createGizmoObj() {
    gizmoObj = createElement("div", "gizmo-obj", []);

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

    isSelected = true;
    prevParent = obj.parentNode;
    gizmoObj.appendChild(obj);
}

function triggerMove(e) {
    switch (e.target.getAttribute("direction")) {
        case "north":
            console.log("n");
        break;

        case "south":
            console.log("s");
        break;

        case "west":
            console.log("w");
        break;

        case "east":
            console.log("e");
        break;
    }
}

function triggerRotate(e) {
    let obj = e.target.parentNode.getElementsByClassName("ship")[0];
    obj.setAttribute("orientation", (obj.getAttribute("orientation") == "horizontal") ? "vertical" : "horizontal");
}//j.bauer@ctu.gov