window.onload = function() {
    const mainContainer = document.querySelector('#mainContainer');
    var theButton = document.getElementById('loginButton');
    var signupButton = document.getElementById('signUpButton');
    theButton.addEventListener('click', login);
    signupButton.addEventListener('click', signUp);
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
        success: toDoListDash()
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

    var itemTable = document.createElement('table');
    itemTable.id = 'itemTable';
    itemTable.className = 'table table-striped table-dark';

    var itemRow = document.createElement('tr');
    itemRow.id = 'itemRow';

    var tableHeader = document.createElement('thead');

    var headerRow = document.createElement('tr');

    var titleHeader = document.createElement('th');
    titleHeader.setAttribute('scope', 'col');
    titleHeader.innerHTML = 'Title';

    var descHeader = document.createElement('th');
    descHeader.setAttribute('scope', 'col');
    descHeader.innerHTML = 'Description';

    var userHeader = document.createElement('th');
    userHeader.setAttribute('scope', 'col');
    userHeader.innerHTML = 'User';

    var tableBody = document.createElement('tbody');
    tableBody.id = 'tableBody';

    headerRow.appendChild(titleHeader);
    headerRow.appendChild(descHeader);
    headerRow.appendChild(userHeader);

    tableHeader.appendChild(headerRow);

    itemTable.appendChild(tableHeader);
    itemTable.appendChild(tableBody);

    tableDiv.appendChild(itemTable);

    mainContainer.appendChild(tableDiv);
}

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


function addItem() {
    var title = document.getElementById('title').value;
    var description = document.getElementById('desc').value;

    $.ajax({
        url: "/add-item",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            "title": title,
            "description": description
        }),
        success: function (data) {
            console.log(data);
            console.log(data.title);
            displayItems(data);
            }
        })
}

function displayItems(data) {
     console.log(data);
     //var dataArray = [];
     //dataArray = data;

    var theTitle = data[0].title;
    console.log(theTitle);

     //console.log(data.keys());


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


     var myMap = new Map();

     var theData = mapItemData(data);

     var dataArr = [];

     dataArr = data;

     var creatorMap = new Map();
     var creator = data.creator;
     console.log(creator);

     var theCreator = data[0].creator;
     console.log(theCreator);

     console.log(dataArr);

     console.log(theData);
     theData = data;
     //var dataTitle = data.title.value;
     myMap.set('title', data.title);
     myMap.set('description', data.description);
     myMap.set('creator', data.creator);

     console.log(myMap);
     var itemTable = document.getElementById('itemTable');
     //itemTable.className = 'table table-bordered table-dark';
    //itemTable.setAttribute('class', 'table table-bordered table-dark');




     var newRow = document.createElement('tr');
     newRow.setAttribute('scope', 'row');

     var titleCol = document.createElement('td');
     titleCol.setAttribute('id', 'title');
     //titleCol.innerHTML = theData.title;
     //titleCol.innerHTML = myMap.get('title');
     //titleCol.innerHTML = jsonData.title;
     titleCol.innerHTML = data[0].title;

     var descCol = document.createElement('td');
     descCol.setAttribute('id', 'desc');
     descCol.innerHTML = data[0].description;
     //document.getElementById('desc').innerHTML = myJsonData.description;
     //descCol.innerHTML = theData.description;
     //descCol.innerHTML = myMap.get('description');
     //descCol.innerHTML = jsonData.description;
     //descCol.innerHTML = myJsonData.description

     var userCol = document.createElement('td');
     userCol.setAttribute('id', 'user');
     //userCol.innerHTML = theData.creator;
     //userCol.innerHTML = myMap.get('creator');
     //userCol.innerHTML = jsonData.username;
     //userCol.innerHTML = myJsonData.username;
     userCol.innerHTML = theCreator.username;

     var tableBody = document.getElementById('tableBody');

     newRow.appendChild(titleCol);
     newRow.appendChild(descCol);
     newRow.appendChild(userCol);
     tableBody.appendChild(newRow);
     //itemTable.appendChild(newRow);

}

function mapItemData(data) {
    return {
        title: data.title,
        description: data.description,
        creator: data.creator,
    }
}

