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
            //createNavBar();
            splashPage(data);
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
            sessionStorage.setItem("username", data.username);
            splashPage(data);
            //toDoListDash();
            //navBarDash();
            //createNavBar();
            //createNavBar();

        },
        error: function (data) {
            console.log("Error has occured");
            alert("Username taken");
        }
    })
}

function splashPage(data) {
    mainContainer.innerHTML = '';
    //sessionStorage.setItem('username', data.username);

    var splashHeader = document.createElement('header');
    //splashHeader.setAttribute('id', 'splashHeader');
    splashHeader.innerHTML = 'Welcome ' + sessionStorage.getItem('username')+ '!';


    var para = document.createElement('p');
    para.setAttribute('id', 'para');
    para.innerHTML = 'Click create new list to create a new to do list!';

    var containerDiv = document.createElement('div');
    containerDiv.setAttribute('id', 'containerDiv');
    containerDiv.setAttribute('class', 'col-md-4');

    var addListButton = document.createElement('button');
    addListButton.setAttribute('id', 'addListButton');
    addListButton.innerHTML = 'Add a new list';
    addListButton.setAttribute('class', 'button btn-primary');
    addListButton.addEventListener('click', function (event) {
        event.preventDefault();
        const userInput = prompt("Please enter a title for your list");
        if (userInput !== null) {
            console.log(userInput);
            var listName = userInput;
            console.log(listName);
            createNewList(listName);
    }
})

    containerDiv.appendChild(addListButton);

    var userListTable = document.createElement('table');
    userListTable.setAttribute('id', 'userListTable');
    userListTable.setAttribute('class', 'table table-striped table-bordered table-hover');

    var userListTableHeader = document.createElement('thead');
    userListTableHeader.setAttribute('id', 'userListTableHeader');

    var userListTableHeaderRow = document.createElement('tr');
    userListTableHeaderRow.setAttribute('id', 'userListsHeaderRow');

    var userListTableColumn = document.createElement('td');
    userListTableColumn.setAttribute('scope', 'col');
    userListTableColumn.setAttribute('id', 'userListsHeaderColumn');
    userListTableColumn.innerHTML = 'Your Lists';

    var userListTableBody = document.createElement('tbody');
    userListTableBody.setAttribute('id', 'userListsBody');

    mainContainer.appendChild(splashHeader);
    mainContainer.appendChild(para);
    mainContainer.appendChild(containerDiv);
    userListTable.appendChild(userListTableHeader);
    userListTable.appendChild(userListTableHeaderRow);
    userListTableHeaderRow.appendChild(userListTableColumn);
    userListTable.appendChild(userListTableBody);

    mainContainer.appendChild(userListTable);

    $(document).ready(function () {
        $.get('/get-lists', function (data) {
          for (var i = 0; i < data.length; i++) {
              var tableRows = document.createElement('tr');
              tableRows.setAttribute('id', 'userListsTableRows' +i);

              var tableColumns = document.createElement('td');
              tableColumns.setAttribute('scope', 'col');
              tableColumns.setAttribute('id', 'userListsTableColumns' +i);
              tableColumns.innerHTML = data[i].listName;
              tableColumns.addEventListener('click', function (event) {
                  event.preventDefault();
                  var data = this.innerHTML;
                  getSpecificList(data);
              })

              tableRows.appendChild(tableColumns);

              userListTableBody.appendChild(tableRows);
          }
        })
    })
}

function createNewList(listName) {
    $.ajax({
        url: "/create-new-list",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            splashPage(data);
        },
        error: function (data) {
            console.log("Error has occured");
        }
    })
}

function getSpecificList(data) {
    console.log(data);
    var listName = data;
    $.ajax({
        url: "/find-specific-list",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            console.log(data);
            displayItemsPage(data);
        },
        error: function (xhr) {
            console.log("Shid is fucked");
        }
    })
}

function displayItemsPage(data) {
    console.log(data);
    mainContainer.innerHTML = '';

    console.log(data.userItems);

    var userItems = [];

    userItems = data.userItems;

    console.log(userItems);

    var firstDiv = document.createElement('div');
    firstDiv.id = 'itemDiv';
    firstDiv.setAttribute('class', 'container col-md-12');

    var titleInput = document.createElement('input');
    titleInput.type = 'text';
    titleInput.placeholder = 'Title of item';
    titleInput.id = 'title';
    titleInput.setAttribute('class', 'col-md-3');

    var descInput = document.createElement('input');
    descInput.type = 'text';
    descInput.placeholder = 'Description of item';
    descInput.id = 'desc';
    descInput.setAttribute('class', 'col-md-3');

    var dueDateInput = document.createElement('input');
    dueDateInput.type = 'date';
    dueDateInput.innerHTML = 'Due Date';
    dueDateInput.id = 'dueDate';
    dueDateInput.setAttribute('class', 'col-md-3');

    var addButton = document.createElement('button');
    addButton.type = 'button';
    addButton.id = 'itemButton';
    addButton.innerHTML = 'Add';
    addButton.setAttribute('class', 'btn btn-primary');
    addButton.addEventListener('click', addItem);

    var pageHeader = document.createElement('p');
    pageHeader.setAttribute('class', 'h1');
    pageHeader.id = 'pageHeader';
    pageHeader.innerHTML = data.listName;

    var homeButton = document.createElement('button');
    homeButton.setAttribute('id', 'homeButton');
    homeButton.setAttribute('type', 'button');
    homeButton.innerHTML = 'Home';
    homeButton.setAttribute('class', 'btn btn-secondary');
    homeButton.addEventListener('click', splashPage);


    firstDiv.appendChild(titleInput);
    firstDiv.appendChild(descInput);
    firstDiv.appendChild(dueDateInput);
    firstDiv.appendChild(addButton);
    //firstDiv.appendChild(homeButton);
    //firstDiv.appendChild(pageHeader);

    var secondDiv = document.createElement('div');
    secondDiv.id = 'secondDiv';
    secondDiv.setAttribute('class', 'container col-md-12');

    secondDiv.appendChild(homeButton);
    secondDiv.appendChild(pageHeader);

    mainContainer.appendChild(firstDiv);

    mainContainer.appendChild(secondDiv);

    //var deleteButtonDiv = document.createElement('div');
    //deleteButtonDiv.id = 'deleteButtonDiv';

    var deleteButton = document.createElement('button');
    deleteButton.type = 'button';
    deleteButton.id = 'deleteButton';
    deleteButton.innerHTML = 'Delete';
    deleteButton.setAttribute('class', 'btn btn-danger');
    deleteButton.addEventListener('click', function (event) {
        event.preventDefault();
        deleteItem();
    });

    //deleteButtonDiv.appendChild(deleteButton);

    //logoutDiv.appendChild(logoutButton);

    //mainContainer.appendChild(logoutDiv);
    //mainContainer.appendChild(deleteButtonDiv);

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

    var dueDateHeader = document.createElement('th');
    dueDateHeader.setAttribute('scope', 'col');
    dueDateHeader.innerHTML = 'Due Date';

    var deleteHeader = document.createElement('th');
    deleteHeader.setAttribute('scope', 'col');
    deleteHeader.setAttribute('id', 'deleteHeader');

    deleteHeader.appendChild(deleteButton);

    headerRow.appendChild(selectHeader);
    headerRow.appendChild(titleHeader);
    headerRow.appendChild(descHeader);
    headerRow.appendChild(userHeader);
    headerRow.appendChild(timeHeader);
    headerRow.appendChild(dueDateHeader);
    //headerRow.appendChild(deleteHeader);

    secondDiv.appendChild(deleteButton);

    theHeader.appendChild(headerRow);


    itemTable.appendChild(theHeader);

    var tableBody = document.createElement('tbody');
    tableBody.id = 'tableBody';
    tableBody.className = 'table-hover';

    //var itemTable = document.getElementById('itemTable');

    for (var i = 0; i < userItems.length; i++) {
        var row = document.createElement('tr');
        row.setAttribute('scope', 'row');

        var theCreator = userItems[i].username;

        var selectCol = document.createElement('td');
        selectCol.setAttribute('scope', 'col');
        selectCol.setAttribute('id', 'selectCol' + i);

        var selectRadio = document.createElement('input');
        selectRadio.setAttribute('scope', 'radio');
        selectRadio.setAttribute('type', 'radio');
        selectRadio.setAttribute('name', 'radio' + i);
        selectRadio.setAttribute('id', 'selectCol-' + i);
        selectRadio.setAttribute('class', 'form-check-input');
        //selectRadio.setAttribute('checked', 'false');

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

        //var date = new Date(data[i].creationTime);

        //var dueDate = new Date(data[i].dueDate);

        console.log(dueDate);

        var theCreationDate = new Date(userItems[i].creationTime);

        var titleCol = document.createElement('td');
        titleCol.setAttribute('scope', 'col');
        titleCol.setAttribute('id', 'title-' + i);
        titleCol.innerHTML = userItems[i].title;

        var descCol = document.createElement('td');
        descCol.setAttribute('scope', 'col');
        descCol.setAttribute('id', 'desc-' + i);
        descCol.innerHTML = userItems[i].description;

        var userCol = document.createElement('td');
        userCol.setAttribute('scope', 'col');
        userCol.setAttribute('id', 'user-' + i);
        //userCol.innerHTML = theCreator.username;
        userCol.innerHTML = userItems[i].username;

        var timeCol = document.createElement('td');
        timeCol.setAttribute('scope', 'col');
        timeCol.setAttribute('id', 'time-' + i);
        timeCol.innerHTML = theCreationDate.toLocaleDateString('en-US');
        //timeCol.innerHTML = userItems[i].toLocaleDateString('en-US');
        //timeCol.innerHTML = date;
        //timeCol.innerHtml = Date(data[i].creationTime);

        var dueDateCol = document.createElement('td');
        dueDateCol.setAttribute('scope', 'col');
        dueDateCol.setAttribute('id', 'dueDate-' + i);
        dueDateCol.innerHTML = userItems[i].dueDate;

        console.log(dueDateCol);

        //console.log(data[i].creationTime);

        row.appendChild(selectCol);
        row.appendChild(titleCol);
        row.appendChild(descCol);
        row.appendChild(userCol);
        row.appendChild(timeCol);
        row.appendChild(dueDateCol);
        tableBody.appendChild(row);
    }
    itemTable.appendChild(tableBody);
    tableDiv.appendChild(itemTable);
    mainContainer.appendChild(tableDiv);
}

function addItem() {
    var title = document.getElementById('title').value;
    var description = document.getElementById('desc').value;
    var creationDate = Date.now();
    var dueDate = document.getElementById('dueDate').value;
    var listNameElement = document.getElementById('pageHeader');
    var listName = listNameElement.innerText;
    console.log(listName);
    $.ajax({
        url: "/add-item",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            "title": title,
            "description": description,
            "creationTime": creationDate,
            "dueDate": dueDate,
            "listName": listName,
        }),
        success: function (data) {
            console.log(data);
            var theItemTable = document.getElementById("tableBody")
            theItemTable.innerHTML = '';

            var title = document.getElementById('title');
            //title.reset();
            //title.innerHTML = 'Title of item';
            title.setAttribute('placeholder', 'Title of item');
            console.log(title);

            var descrip = document.getElementById('desc');
            //descrip.reset();
            //descrip.innerHTML = 'Description of item';
            descrip.setAttribute('placeholder', 'Description of item');

            var dueDate = document.getElementById('dueDate');
            //dueDate.reset();
            //dueDate.innerHTML = 'Due date';
            dueDate.setAttribute('placeholder', 'Due date');

            var theData = [];
            theData = data.userItems;

            console.log(theData);

            var wholeItemTable = document.getElementById("itemTable");

            for (var i = 0; i <theData.length; i++) {

                var theCreationDate = new Date(theData[i].creationTime);
                console.log(theCreationDate);

               // var theDueDate = theData[i].dueDate;

                //var theDueDate = new Date(data[i].userItems[i].dueDate);
                //var theDueDate = new Date(theData[i].dueDate);

                var newRow = document.createElement("tr");
                newRow.setAttribute('scope', 'row');
                newRow.setAttribute('id', 'newTableRow-' + i);

                var selectCol = document.createElement('td');
                selectCol.setAttribute('scope', 'col');

                var selectRadio = document.createElement('input');
                selectRadio.setAttribute('scope', 'radio');
                selectRadio.setAttribute('type', 'radio');
                selectRadio.setAttribute('id', 'selectCol-' + i);
                selectRadio.setAttribute('class', 'form-check-input');

                selectCol.appendChild(selectRadio);

                var titleCol = document.createElement('td');
                titleCol.setAttribute('scope', 'col');
                titleCol.setAttribute('id', 'title-' + i);
                //titleCol.innerHTML = data[i].userItems[i].title;
                titleCol.innerHTML = theData[i].title;

                var descCol = document.createElement('td');
                descCol.setAttribute('scope', 'col');
                descCol.setAttribute('id', 'desc-' + i);
                //descCol.innerHTML = data[i].userItems[i].description;
                descCol.innerHTML = theData[i].description;

                var userCol = document.createElement('td');
                userCol.setAttribute('scope', 'col');
                userCol.setAttribute('id', 'user-' + i);
                //userCol.innerHTML = data[i].userItems[i].username;
                userCol.innerHTML = theData[i].username;

                var timeCol = document.createElement('td');
                timeCol.setAttribute('scope', 'col');
                timeCol.setAttribute('id', 'time-' + i);
                timeCol.innerHTML = theCreationDate.toLocaleDateString('en-US');

                var dueDateCol = document.createElement('td');
                dueDateCol.setAttribute('scope', 'col');
                dueDateCol.setAttribute('id', 'dueDate-' + i);
                //dueDateCol.innerHTML = data[i].dueDate;
                dueDateCol.innerHTML = theData[i].dueDate;

                //console.log(data[i].userItems[i].username);
                console.log(theData.username);

                newRow.appendChild(selectCol);
                newRow.appendChild(titleCol);
                newRow.appendChild(descCol);
                newRow.appendChild(userCol);
                newRow.appendChild(timeCol);
                newRow.appendChild(dueDateCol);

                theItemTable.appendChild(newRow);

            }
            wholeItemTable.appendChild(theItemTable);
        },
        error: function (xhr) {
            console.log("Error has occured");
            alert("Title or description is missing");
        }
    })
}

function deleteItem() {
    var itemTable = document.getElementById("tableBody")
    var rows = itemTable.getElementsByTagName("tr");
    console.log(rows.length);
    for (var i = 0; i < rows.length; i++) {
        var radio = document.getElementById("selectCol-" +i);
        if (radio.checked) {
            var title = document.getElementById('title-' + i).innerHTML;
            var desc = document.getElementById('desc-' + i).innerHTML;
            var userName = document.getElementById('user-' + i).innerHTML;
            //Date creationDate = new Date().toLocaleDateString('en-US');
            var creationTime = document.getElementById('time-' + i).innerHTML;
            var theCreationTime = Date.parse(creationTime);
            var dueDate = document.getElementById('dueDate-' + i).innerHTML;
            console.log(title)
            console.log(desc);
            console.log(userName);
            $.ajax({
                url: "/delete-item",
                contentType: "application/json",
                type: "POST",
                data: JSON.stringify({
                    "title": title,
                    "description": desc,
                    "username": userName,
                    "creationTime": theCreationTime,
                    "dueDate": dueDate,
                }),
                success: function (data) {
                    console.log(data);
                    //itemTable.remove();
                    //document.getElementsByTagName('radio').checked = false;
                    displayItemsPage(data)
                },
                error: function (xhr) {
                    console.log("Error has occured");
                }
            });
        }
    }
}