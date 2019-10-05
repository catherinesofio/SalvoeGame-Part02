function getParamObj(search) {
  var obj = {};
  var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

  search.replace(reg, function(match, param, val) {
    obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
  });

  return obj;
}

function createElement(element, objClass, attributes) {
    let e = document.createElement(element);
    if (objClass != "") {
        e.classList.add(objClass);
    }
    attributes.forEach(x => e.setAttribute(x.name, x.value));

    return e;
}

function createTable(id, caption, headersX, headersY, attributes) {
    let cellsX = headersX.length;
    let cellsY = headersY.length;

    let table = document.createElement("table");
    table.setAttribute("id", id);

    let tcaption = document.createElement("caption");
    tcaption.innerText = caption;
    table.appendChild(tcaption);

    let thead = document.createElement("thead");
    table.appendChild(thead);

    let th = document.createElement("th");
    th.innerText = "";
    th.setAttribute("scope", "col");
    thead.appendChild(th);

    let tbody = document.createElement("tbody");
    table.appendChild(tbody);

    for (let i = 0; i < cellsY; i++) {
        let headerY = headersY[i];

        th = document.createElement("th");
        th.innerText = (headersX[i]);
        th.setAttribute("scope", "col");
        thead.appendChild(th);

        let tr = document.createElement("tr");
        tbody.appendChild(tr);

        th = document.createElement("th");
        th.innerText = (headerY);
        th.setAttribute("scope", "row");
        tr.appendChild(th);

        for (let j = 0; j < cellsX; j++) {
            let td = document.createElement("td");
            td.setAttribute("id", id + "-" + headerY + headersX[j]);
            attributes.forEach(x => td.setAttribute(x.name, x.value));
            tr.appendChild(td);
        }
    }

    return table;
}

function setElementAttributes(id, attributes) {
    let element = document.getElementById(id);

    attributes.forEach(function (attr) {
        element.setAttribute(attr.name, attr.value);
    });

    return element;
}

function setElementsAttributes(idsPrefix, ids, attributes) {
    for (let i = ids.length - 1; i >= 0; i--) {
        let element = document.getElementById(idsPrefix + ids[i]);
        attributes.forEach(function (attr) {
            element.setAttribute(attr.name, attr.value);
        });
    }
}