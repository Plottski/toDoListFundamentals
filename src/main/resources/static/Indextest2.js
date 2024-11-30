window.onload = function() {
    const mainContainer = document.querySelector('#mainContainer');
    const mainDivContainer = document.querySelector('#mainDivContainer');
    var theButton = document.getElementById('loginButton');
    var signupButton = document.getElementById('signUpButton');
    theButton.addEventListener('click', login);
    signupButton.addEventListener('click', signUp);
    const xhr = new XMLHttpRequest();
    console.log(mainContainer);
    console.log(theButton);
    //const navBar = createNavBar();
}

function login() {
    console.log("things are happening");
    var uName = document.querySelector('#username').value;
    var pw = document.querySelector('#password').value;
    console.log(pw);
    console.log(uName);

    $.ajax({
        url: "/login",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({username: uName,
            password: pw}),
        success: function (data) {
            console.log(data);
            //lookForItems(data);
            // toDoListDash();
            createNavBar();
        },
        error: function (xhr) {
            console.log("Error has occured");
            alert("Username or password incorrect");
        }
    })
}

function signUp() {
    console.log("signup button functioning");
    var uName = document.querySelector('#username').value;
    var pw = document.querySelector('#password').value;
    console.log(pw);
    console.log(uName);

    $.ajax ({
        url: "/signup",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({"username": uName,
            "password": pw}),
        success: function (data) {
            //toDoListDash();
            //navBarDash();
            //createNavBar();
            createNavBar();

        },
        error: function (data) {
            console.log("Error has occured");
            alert("Username taken");
        }
    })
}

//this function creates the navbar dashboard screen where I want everything to be able to be accessed from this function.

function createNavBar() {
    //mainContainer.innerHTML = '';
    mainDivContainer.innerHTML = '';

    var navbar = document.createElement('nav');
    navbar.setAttribute('id', 'navbar');
    navbar.setAttribute('class', 'navbar navbar-expand-lg navbar-light bg-light');

    var logOutLink = document.createElement('a');
    logOutLink.setAttribute('href', '/logout');
    logOutLink.setAttribute('class', 'navbar-brand')
    logOutLink.innerHTML = 'Logout';

    navbar.appendChild(logOutLink);

    var navBarButton = document.createElement('button');
    navBarButton.setAttribute('id', 'navBarButton');
    navBarButton.setAttribute('class', 'navbar-toggler');
    navBarButton.setAttribute('type', 'button');
    navBarButton.setAttribute('data-toggle', 'collapse');
    navBarButton.setAttribute('data-target', '#navbarSupportedContent');
    navBarButton.setAttribute('aria-controls', 'navbarSupportedContent');
    navBarButton.setAttribute('aria-expanded', 'false');
    navBarButton.setAttribute('aria-label', 'Toggle navigation');

    navbar.appendChild(navBarButton);

    var navSpan = document.createElement('span');
    navSpan.setAttribute('class', 'navbar-toggler-icon');

    navbar.appendChild(navSpan);

    var navDiv = document.createElement('div');
    navDiv.setAttribute('class', 'collapse navbar-collapse');
    navDiv.setAttribute('id', 'navbarSupportedContent');


    var navList = document.createElement('ul');
    navList.setAttribute('class', 'navbar-nav mr-auto');

    var homeLI = document.createElement('li');
    homeLI.setAttribute('class', 'nav-item-active');

    var homeLink = document.createElement('a');
    homeLink.setAttribute('class', 'nav-link');
    homeLink.addEventListener('click', function (event) {
        event.preventDefault();
        navBarDash();
    })
    homeLink.innerHTML = 'Home';

    homeLI.appendChild(homeLink);
    navList.appendChild(homeLI);


    var liForAbout = document.createElement('li');
    liForAbout.setAttribute('class', 'nav-item');

    var linkAbout = document.createElement('a');
    linkAbout.setAttribute('class', 'nav-link');
    linkAbout.addEventListener('click', function (event) {
    })
    linkAbout.innerHTML = 'About';

    liForAbout.appendChild(linkAbout);

    navList.appendChild(liForAbout);

    var dropDownList = document.createElement('li');
    dropDownList.setAttribute('class', 'nav-item');
    dropDownList.addEventListener('click', function (event) {
        event.preventDefault();
        getAllUserLists();
    })
    //dropDownList.setAttribute('class', 'nav-item dropdown');

    var dropDownListLink = document.createElement('a');
    dropDownListLink.setAttribute('class', 'nav-link');
    dropDownListLink.setAttribute('click', function (event) {
        event.preventDefault();
    });
    dropDownListLink.innerHTML = 'Your Lists';

    dropDownList.appendChild(dropDownListLink);

    navList.appendChild(dropDownList);

    //navList.appendChild(newListLink);

    navDiv.appendChild(navList);

    navbar.appendChild(navDiv);

    //mainContainer.appendChild(navbar);
    mainDivContainer.appendChild(navbar);
   // return null;
}

//Function to ping the server to get all user lists after the your lists link is clicked in the navbar. Should recreate the nav
// and also add a table to the DOM with the users lists by name. I want to make each name a clickable link and then go to the
//add items dashboard.

function getAllUserLists() {
    //mainContainer.innerHTML = '';
    //mainContainer.appendChild(createNavBar());
    //var mainDiv = document.getElementById('mainDivContainer');
    mainDivContainer.innerHTML = '';

    $(document).ready(function () {
        $.get('/get-lists', function (data) {
            console.log(data);
            createNavBar();

            var createNewListButton = document.createElement('button');
            createNewListButton.setAttribute('id', 'createListButton');
            createNewListButton.setAttribute('class', 'btn btn-primary');
            createNewListButton.setAttribute('type', 'button');
            createNewListButton.addEventListener('click', function (event) {
                event.preventDefault();
                const userInput = prompt("Please enter a title for your list");
                if (userInput !== null) {
                    console.log(userInput);
                    var listName = userInput;
                    console.log(listName);
                    createNewList(listName);
                }
                createNewListButton.innerHTML = 'Create List';
                consol.log(createNewListButton);

                var bodyDiv = document.createElement('div');
                bodyDiv.setAttribute('class', 'col-md-12');

                bodyDiv.appendChild(createNewListButton);



                //mainContainer.appendChild(bodyDiv);
                mainDivContainer.appendChild(bodyDiv);

                //var navBarDiv = document.getElementById('navbarSupportedContent');

                //navBarDiv.appendChild(createNewListButton);

                var listTable = document.createElement('table');
                listTable.setAttribute('id', 'userLists');
                listTable.setAttribute('class', 'table table-striped table-bordered table-hover');

                var tableHeader = document.createElement('thead');
                tableHeader.setAttribute('id', 'userListsHeader');

                var headerRow = document.createElement('tr');
                headerRow.setAttribute('id', 'userListsHeaderRow');

                var titleColumn = document.createElement('td');
                titleColumn.setAttribute('scope', 'col');
                titleColumn.setAttribute('id', 'userListsTitleColumn');
                titleColumn.innerHTML = 'Your Lists';

                headerRow.appendChild(titleColumn);

                tableHeader.appendChild(headerRow);

                listTable.appendChild(tableHeader);

                var tableBody = document.createElement('tbody');

                for (var i = 0; i < data.length; i++) {
                    var row = document.createElement('tr');
                    row.setAttribute('scope', 'row');

                    var column = document.createElement('td');
                    column.setAttribute('scope', 'col');
                    column.setAttribute('id', 'userListsTitleColumn' + i);

                    var link = document.createElement('a');
                    link.innerHTML = data[i].listName;
                    link.setAttribute('click', function (event) {
                        event.preventDefault();
                        $.get('/get-specific-list', function (data) {
                            mainContainer.innerHTML = '';
                            createNavBar();
                        })
                    })

                    column.appendChild(link);

                    row.appendChild(column);

                    tableBody.appendChild(row);

                    listTable.appendChild(tableBody);

                    mainContainer.appendChild(listTable);
                }
            })
        })
    })
}

//Logout function currently broken I need to fix this.
function userLogout(creatorName) {
    //var uName = data.username;
    $.ajax({
        url: "/logout",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({username: creatorName}),
        success: function (data) {
            resetHTML();
        }
    })
}

//Function to reset the DOM to the user login splash page
function resetHTML() {
    var userInput = document.createElement('input');
    userInput.setAttribute('type', 'text');
    userInput.setAttribute('placeholder', 'Username');
    userInput.setAttribute('id', 'username');

    var userPasswordInput = document.createElement('input');
    userPasswordInput.setAttribute('type', 'password');
    userPasswordInput.setAttribute('placeholder', 'Password');
    userPasswordInput.setAttribute('id', 'password');

    var loginButton = document.createElement('button');
    loginButton.setAttribute('type', 'button');
    loginButton.setAttribute('id', 'loginButton');
    loginButton.addEventListener('click', login)
    loginButton.innerHTML = 'Login';

    var signUpButton = document.createElement('button');
    signUpButton.setAttribute('type', 'button');
    signUpButton.setAttribute('id', 'signUpButton');
    signUpButton.addEventListener('click', signUp);
    signUpButton.innerHTML = 'Sign Up';

    mainContainer.innerHTML = '';

    mainContainer.appendChild(userInput);
    mainContainer.appendChild(userPasswordInput);
    mainContainer.appendChild(loginButton);
    mainContainer.appendChild(signUpButton);
}