window.onload = function() {
    const mainContainer = document.querySelector('#mainContainer');
    var theButton = document.getElementById('loginButton');
    var signupButton = document.getElementById('signUpButton');
    theButton.addEventListener('click', login);
    signupButton.addEventListener('click', signUp);
    const xhr = new XMLHttpRequest();
    console.log(mainContainer);
    console.log(theButton);
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
            lookForItems(data);
           // toDoListDash();
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
                    toDoListDash();
            },
        error: function (data) {
            console.log("Error has occured");
            alert("Username taken");
        }
    })
}

function toDoListDash() {

    mainContainer.innerHTML = '';

    var firstDiv = document.createElement('div');
    firstDiv.id = 'itemDiv';

    var titleInput = document.createElement('input');
    titleInput.type = 'text';
    titleInput.placeholder = 'Title of item';
    titleInput.id = 'title';

    var descInput = document.createElement('input');
    descInput.type = 'text';
    descInput.placeholder = 'Description of item';
    descInput.id = 'desc';

    var addButton = document.createElement('button');
    addButton.type = 'button';
    addButton.id = 'itemButton';
    addButton.innerHTML = 'Add';
    addButton.addEventListener('click', addItem);

    firstDiv.appendChild(titleInput);
    firstDiv.appendChild(descInput);
    firstDiv.appendChild(addButton);

    mainContainer.appendChild(firstDiv);

    var tableDiv = document.createElement('div');
    tableDiv.id = 'tableDiv';
    tableDiv.className = 'd-flex position-relative top-50 align-items-center justify-content-center align-middle';

    var itemTable = document.createElement('table');
    itemTable.id = 'itemTable';
    itemTable.className = 'table table-striped table-dark w-50';

    var itemRow = document.createElement('tr');
    itemRow.id = 'itemRow';

    var tableHeader = document.createElement('thead');

    var headerRow = document.createElement('tr');

    var selectHeader = document.createElement('th');
    selectHeader.setAttribute('scope', 'col');
    selectHeader.innerHTML = 'Select';

    var titleHeader = document.createElement('th');
    titleHeader.setAttribute('scope', 'col');
    titleHeader.innerHTML = 'Title';

    var descHeader = document.createElement('th');
    descHeader.setAttribute('scope', 'col');
    descHeader.innerHTML = 'Description';

    var userHeader = document.createElement('th');
    userHeader.setAttribute('scope', 'col');
    userHeader.innerHTML = 'User';

    var timeHeader = document.createElement('th');
    timeHeader.setAttribute('scope', 'col');
    timeHeader.innerHTML = 'Created';

    var tableBody = document.createElement('tbody');
    tableBody.id = 'tableBody';
    tableBody.className = 'table-hover';

    headerRow.appendChild(selectHeader);
    headerRow.appendChild(titleHeader);
    headerRow.appendChild(descHeader);
    headerRow.appendChild(userHeader);
    headerRow.appendChild(timeHeader);

    tableHeader.appendChild(headerRow);

    itemTable.appendChild(tableHeader);
    itemTable.appendChild(tableBody);

    tableDiv.appendChild(itemTable);

    mainContainer.appendChild(tableDiv);


}

function addItem() {
    var title = document.getElementById('title').value;
    var description = document.getElementById('desc').value;
    var creationDate = Date.now();
    console.log(creationDate);

    $.ajax({
        url: "/add-item",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            "title": title,
            "description": description,
            "creationTime": creationDate,
        }),
        success: function (data) {
            console.log(data);
            //console.log(data.title);
            //displayItems(data);
            lookForItems(data)
            //displayAllItems(data);
            },
        error: function (xhr) {
            console.log("Error has occured");
            alert("Title or description is missing");
        }
        })
}
//this currently works unless you leave the page and go back. trying to create a new function to make it work.
function displayItems(data) {
    console.log(data);
    var theTitle = data[0].title;
    console.log(theTitle);
    var theCreator = data[0].creator;

     var itemTable = document.getElementById('itemTable');

     var newRow = document.createElement('tr');
     newRow.setAttribute('scope', 'row');

     var titleCol = document.createElement('td');
     titleCol.setAttribute('id', 'title');

     titleCol.innerHTML = data[0].title;

     var descCol = document.createElement('td');
     descCol.setAttribute('id', 'desc');
     descCol.innerHTML = data[0].description;


     var userCol = document.createElement('td');
     userCol.setAttribute('id', 'user');

     userCol.innerHTML = theCreator.username;

     var tableBody = document.getElementById('tableBody');

     newRow.appendChild(titleCol);
     newRow.appendChild(descCol);
     newRow.appendChild(userCol);
     tableBody.appendChild(newRow);
}

function displayAllItems(data) {
    //var tableBody = document.getElementById('tableBody');
    //tableBody.innerHTML = '';

    var creator = data[0].creator;
    //var creatorName = username;
    mainContainer.innerHTML = '';
    var firstDiv = document.createElement('div');
    firstDiv.id = 'itemDiv';

    var titleInput = document.createElement('input');
    titleInput.type = 'text';
    titleInput.placeholder = 'Title of item';
    titleInput.id = 'title';

    var descInput = document.createElement('input');
    descInput.type = 'text';
    descInput.placeholder = 'Description of item';
    descInput.id = 'desc';

    var addButton = document.createElement('button');
    addButton.type = 'button';
    addButton.id = 'itemButton';
    addButton.innerHTML = 'Add';
    addButton.addEventListener('click', addItem);

    firstDiv.appendChild(titleInput);
    firstDiv.appendChild(descInput);
    firstDiv.appendChild(addButton);

    mainContainer.appendChild(firstDiv);

    var logoutDiv = document.createElement('div');
    logoutDiv.setAttribute('margin-right', '0');
    logoutDiv.id = 'logoutDiv';

    var logoutButton = document.createElement('button');
    logoutButton.type = 'button';
    logoutButton.id = 'logoutButton';
    logoutButton.innerHTML = 'Logout';
    //logoutButton.addEventListener('click', userLogout(creatorName));
    logoutButton.addEventListener('click', function (event) {
        event.preventDefault();
        userLogout(creatorName);
    })

    logoutDiv.appendChild(logoutButton);

    mainContainer.appendChild(logoutDiv);

    var tableDiv = document.createElement('div');
    tableDiv.id = 'tableDiv';
    tableDiv.className = 'd-flex position-relative top-50 align-items-center justify-content-center align-middle';

    var itemTable = document.createElement('table');
    itemTable.id = 'itemTable';
    itemTable.className = 'table table-striped table-dark w-50';

    var theHeader = document.createElement('thead');

    var headerRow = document.createElement('tr');

    var selectHeader = document.createElement('th');
    selectHeader.setAttribute('scope', 'col');
    selectHeader.innerHTML = 'Select';

    var titleHeader = document.createElement('th');
    titleHeader.setAttribute('scope', 'col');
    titleHeader.innerHTML = 'Title';

    var descHeader = document.createElement('th');
    descHeader.setAttribute('scope', 'col');
    descHeader.innerHTML = 'Description';

    var userHeader = document.createElement('th');
    userHeader.setAttribute('scope', 'col');
    userHeader.innerHTML = 'User';

    var timeHeader = document.createElement('th');
    timeHeader.setAttribute('scope', 'col');
    timeHeader.innerHTML = 'Created';

    headerRow.appendChild(selectHeader);
    headerRow.appendChild(titleHeader);
    headerRow.appendChild(descHeader);
    headerRow.appendChild(userHeader);
    headerRow.appendChild(timeHeader);

    theHeader.appendChild(headerRow);


    itemTable.appendChild(theHeader);

    var tableBody = document.createElement('tbody');
    tableBody.id = 'tableBody';
    tableBody.className = 'table-hover';

    //var itemTable = document.getElementById('itemTable');

    for (var i = 0; i < data.length; i++) {
        var row = document.createElement('tr');
        row.setAttribute('scope', 'row');

        var theCreator = data[i].creator;

        var selectCol = document.createElement('td');
        selectCol.setAttribute('scope', 'col');
        selectCol.setAttribute('id', 'selectCol' +i);

        var selectRadio  = document.createElement('input');
        selectRadio.setAttribute('scope', 'radio');
        selectRadio.setAttribute('type', 'radio');
        selectRadio.setAttribute('name', 'radio');
        selectRadio.setAttribute('id', 'radio' +i);
        selectRadio.setAttribute('class', 'form-check-input');

        selectCol.appendChild(selectRadio);
        /*selectCol.setAttribute('id', 'checkbox' +i);
        selectCol.setAttribute('class', 'form-check-input');
        selectCol.setAttribute('type', 'checkbox');
        selectCol.setAttribute('value', '');
        selectCol.setAttribute('name', 'checkbox' +i);
        //selectCol.setAttribute('style', 'background: white');*/

        /*var selectLabel = document.createElement('label');
        selectLabel.setAttribute('class', 'form-check-label text-white');
        selectLabel.setAttribute('for', 'checbox' +i);*/

        //selectCol.appendChild(selectLabel);

        var date = new Date(data[i].creationTime);

        var titleCol = document.createElement('td');
        titleCol.setAttribute('scope', 'col');
        titleCol.setAttribute('id', 'title' +i);
        titleCol.innerHTML = data[i].title;
        var descCol = document.createElement('td');
        descCol.setAttribute('scope', 'col');
        descCol.setAttribute('id', 'desc' +i);
        descCol.innerHTML = data[i].description;
        var userCol = document.createElement('td');
        userCol.setAttribute('scope', 'col');
        userCol.setAttribute('id', 'user' +i);
        //userCol.innerHTML = theCreator.username;
        userCol.innerHTML = data[i].username;
        var timeCol = document.createElement('td');
        timeCol.setAttribute('scope', 'col');
        timeCol.setAttribute('id', 'time' +i);
        timeCol.innerHTML = date.toLocaleDateString('en-US');
        //timeCol.innerHTML = date;
        //timeCol.innerHtml = Date(data[i].creationTime);

        console.log(data[i].creationTime);

        row.appendChild(selectCol);
        row.appendChild(titleCol);
        row.appendChild(descCol);
        row.appendChild(userCol);
        row.appendChild(timeCol);
        tableBody.appendChild(row);
    }
    itemTable.appendChild(tableBody);
    tableDiv.appendChild(itemTable);
    mainContainer.appendChild(tableDiv);
}

function lookForItems(data) {
    var uName = data.username;
    $.ajax({
        url: "/get-all-items",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({username: uName}),
        success: function (data) {
            displayAllItems(data);
        },
        error: function (xhr) {
            console.log("Error has occured");
            alert("Something shit the bed");
        }
    })
}

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





























/*
function mapItemData(data) {
    return {
        title: data.title,
        description: data.description,
        creator: data.creator,
    }
}
*/
//var dataArray = [];
//dataArray = data;
//itemTable.appendChild(newRow);
//userCol.innerHTML = theData.creator;
//userCol.innerHTML = myMap.get('creator');
//userCol.innerHTML = jsonData.username;
//userCol.innerHTML = myJsonData.username;
//document.getElementById('desc').innerHTML = myJsonData.description;
//descCol.innerHTML = theData.description;
//descCol.innerHTML = myMap.get('description');
//descCol.innerHTML = jsonData.description;
//descCol.innerHTML = myJsonData.description
//titleCol.innerHTML = theData.title;
//titleCol.innerHTML = myMap.get('title');
//titleCol.innerHTML = jsonData.title;
//itemTable.className = 'table table-bordered table-dark';
//itemTable.setAttribute('class', 'table table-bordered table-dark');
//var dataTitle = data.title.value;
//console.log(data.keys());
/* var myMap = new Map();
var theData = mapItemData(data);
var dataArr = [];
dataArr = data;
var creatorMap = new Map();
console.log(creator);
var theCreator = data[0].creator;
console.log(theCreator);
console.log(dataArr);
console.log(theData);
theData = data;
myMap.set('title', data.title);
myMap.set('description', data.description);
myMap.set('creator', data.creator);
console.log(myMap); */
//console.log(data.items);
//console.log(data.values());
//var workData = data.results;
//console.log(workData);
//console.log(data.title);
//console.log(dataArray);
//console.log(dataArray.title);
//jsonData = JSON.stringify(data)
//console.log(jsonData)
//var theTitle = data.title;
//console.log(theTitle);
//myJsonData = JSON.parse(jsonData);
//console.log(myJsonData);
/*document.createElement("div").setAttribute("id", "itemDiv");
    document.createElement("input").setAttribute('type', 'text','id','title')
    document.createElement("input").setAttribute('type', 'text', 'id', 'description')
    document.createElement("button").setAttribute('id', 'itemButton', 'textContent', 'Add Item')

    document.getElementById('itemDiv').appendChild(document.getElementById("title"));
    document.getElementById('itemDiv').appendChild(document.getElementById("description"));
    document.getElementById('itemDiv').appendChild(document.getElementById("itemButton"));

    //document.querySelector('#itemDiv').appendChild(document.querySelector('#title'))
    //document.querySelector('#itemDiv').appendChild(document.querySelector('#description'))
    //document.querySelector('#itemDiv').appendChild(document.querySelector('#itemButton'))

    mainContainer.appendChild(document.querySelector('itemDiv'))

    document.createElement("div").setAttribute('id', 'tableDiv')
    document.createElement('table').setAttribute('id', 'theTable')
    document.getElementById("tableDiv").appendChild(document).getElementById("theTable")

    mainContainer.appendChild(document.getElementById("tableDiv"))

    document.querySelector('#itemButton').addEventListener('click', addItem);
}*/
