let navContent = [ "<ul><li><button onclick='openPopup(0)'>sign in</button></li><li><button onclick='openPopup(1)'>sign up</button></li></ul>",
                   "<ul><li><span id='username'></span></li><li><button onclick='triggerSignOut(event)'>sign out</button></li></ul>"];

let popupContent = [ "<form id='form-signIn'><legend>sign in</legend><fieldset><label>e-mail</label><input type='text' name='email' /></fieldset><fieldset><label>password</label><input type='password' name='password' /></fieldset><button type='reset' onclick='triggerSignIn(event)'>sign in</button></form>",
                     "<form id='form-signUp'><legend>sign up</legend><fieldset><label>name</label><input type='text' name='name' /></fieldset><fieldset><label>e-mail</label><input type='text' name='email' /></fieldset><fieldset><label>password</label><input type='submit' name='password' /></fieldset><button type='button' onclick='triggerSignUp(event)'>sign up</button></form>"];

let username = "";

$(document).ready(function() {
    $.ajax("/api/user").done((data) => addTemplates(data));
});

function setUsername() {
    $.ajax("/api/user").done((data) => { username = data.name; updateNav(1, username); });
}

function addTemplates(data) {
    let nav = document.createElement("nav");
    document.body.insertBefore(nav, document.body.firstChild);
    let navContent = document.createElement("div");
    navContent.setAttribute("id", "nav-content");
    nav.appendChild(navContent);

    let popup = document.createElement("div");
    popup.setAttribute("id", "popup");
    document.body.insertBefore(popup, nav.nextSibling);
    popup.style.display = "none";
    let popupContent = document.createElement("div");
    popupContent.setAttribute("id", "popup-content");
    popup.appendChild(popupContent);

    (data.name == null) ? updateNav(0, "") : updateNav(1, data.name);
}

function updateNav(state, user) {
	let nav = document.getElementById("nav-content");
    nav.innerHTML = navContent[state];

    username = user;
    if (user != "") {
        let username = document.getElementById("username");
        username.innerHTML = user;
    }
}

function triggerSignIn(e) {
    let form = e.target.form;

    $.post("/api/login", { email: form["email"].value, password: form["password"].value }).done(() => { closePopup(); setUsername(); window.location.reload(); });
}

function triggerSignUp(e) {
    let form = e.target.form;

    $.post("/api/register", { name: form["name"].value, email: form["email"].value, password: form["password"].value }).done(() => triggerSignIn(e));
}

function triggerSignOut(e) {
    $.post("/api/logout").done(() => { updateNav(0, ""); window.location = "/web/games.html"; });
}

function openPopup(state) {
    document.getElementById("popup").style.display = "block";
    document.getElementById("popup-content").innerHTML = popupContent[state];
}

function closePopup() {
    document.getElementById("popup").style.display = "none";
}