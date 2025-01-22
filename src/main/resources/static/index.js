//import * as XLSX from "xlsx";
//import {saveAs} from './file-saver';
//import * as Exceljs from "/exceljs";
//let XLSX = require('xlsx');
//import XLSX from "xlsx";
//var XLSX = require("xlsx");
//import XLSX from "xlsx";
window.onload = function() {
    const mainContainer = document.querySelector('#mainContainer');
    const mainDivContainer = document.querySelector('#mainDivContainer');
    var theButton = document.getElementById('loginButton');
    var signupButton = document.getElementById('signUpButton');
    theButton.addEventListener('click', login);
    signupButton.addEventListener('click', signupForm);
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

    var rect = new Rectangle(1, 2);
    console.log("Printing rectanlge dimensions: "+rect.height+", "+rect.width);
    $.ajax({
        url: "/login",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({username: uName,
            password: pw}),
        success: function (data) {
            splashPage(data);
        },
        error: function (xhr) {
            console.log("Error has occured");
            alert("Username or password incorrect");

        }
    })
}

function splashPage(data) {
    mainContainer.innerHTML = '';

    var splashHeader = document.createElement('header');
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
        //url: "/create-new-list",
        url:"/add-list",
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
    var listName = data;
    $.ajax({
        url: "/find-specific-list",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
        },
        error: function (xhr) {
            console.log("Shid is fucked");
        }
    })
}

function sortByTitleAscending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-ascending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var titleHeaderToChange = document.getElementById("tableTitleHeader");
            titleHeaderToChange.removeEventListener('click', sortByTitleAscending);
            titleHeaderToChange.addEventListener('click', sortByTitleDescending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByTitleDescending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-descending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var titleHeaderToChange = document.getElementById("tableTitleHeader");
            titleHeaderToChange.removeEventListener("click", sortByTitleDescending);
            titleHeaderToChange.addEventListener("click", sortByTitleAscending);

        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByDescriptionAscending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-descriptions-ascending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var descriptionHeaderToChange = document.getElementById("tableDescHeader");
            descriptionHeaderToChange.removeEventListener('click', sortByDescriptionAscending);
            descriptionHeaderToChange.addEventListener('click', sortByDescriptionDescending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByDescriptionDescending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-descriptions-descending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var descriptionHeaderToChange = document.getElementById("tableDescHeader");
            descriptionHeaderToChange.removeEventListener('click', sortByDescriptionDescending);
            descriptionHeaderToChange.addEventListener('click', sortByDescriptionAscending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByUserAscending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-users-descending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var userHeaderToChange = document.getElementById("tableUserHeader");
            userHeaderToChange.removeEventListener('click', sortByUserAscending);
            userHeaderToChange.addEventListener('click', sortByUserDescending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByUserDescending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-users-descending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var userHeaderToChange = document.getElementById("tableUserHeader");
            userHeaderToChange.removeEventListener('click', sortByUserDescending);
            userHeaderToChange.addEventListener('click', sortByUserAscending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByCreationDateAscending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-creationdate-ascending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var timeHeaderToChange = document.getElementById("tableTimeHeader");
            timeHeaderToChange.removeEventListener('click', sortByCreationDateAscending);
            timeHeaderToChange.addEventListener('click', sortByCreationDateDescending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByCreationDateDescending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-creationdate-descending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var timeHeaderToChange = document.getElementById("tableTimeHeader");
            timeHeaderToChange.removeEventListener('click', sortByCreationDateDescending);
            timeHeaderToChange.addEventListener('click', sortByCreationDateAscending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByDueDateAscending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-duedate-ascending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var dueDateHeaderToChange = document.getElementById("tableDueDateHeader");
            dueDateHeaderToChange.removeEventListener('click', sortByDueDateAscending);
            dueDateHeaderToChange.addEventListener('click', sortByDueDateDescending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByDueDateDescending(event) {
    event.preventDefault();
    var listName = document.getElementById("pageHeader").innerHTML;
    console.log(listName);
    $.ajax({
        url: "/sort-items-duedate-descending",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
            var dueDateHeaderToChange = document.getElementById("tableDueDateHeader");
            dueDateHeaderToChange.removeEventListener('click', sortByDueDateDescending);
            dueDateHeaderToChange.addEventListener('click', sortByDueDateAscending);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function addCollaborator() {
    var listName = document.getElementById('pageHeader').innerHTML;
    var username = document.getElementById('collaboratorInput').value;
    console.log(username);
    var itemListWithUserWrapper = new ItemListWithUserWrapper(listName, username);
    console.log(JSON.stringify({itemListWithUserWrapper}));

    $.ajax({
        url: "/add-collaborator",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(itemListWithUserWrapper),
        dataType: 'json',
        success: function (data) {
            displayItemsPage(data);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function displayItemsPage(data) {
    mainContainer.innerHTML = '';

    var userItems = [];

    console.log(data);
    userItems = data.items;

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

    var secondDiv = document.createElement('div');
    secondDiv.id = 'secondDiv';
    secondDiv.setAttribute('class', 'container col-md-12');

    secondDiv.appendChild(homeButton);
    secondDiv.appendChild(pageHeader);

    mainContainer.appendChild(firstDiv);

    mainContainer.appendChild(secondDiv);

    var deleteButton = document.createElement('button');
    deleteButton.type = 'button';
    deleteButton.id = 'deleteButton';
    deleteButton.innerHTML = 'Delete';
    deleteButton.setAttribute('class', 'btn btn-danger');
    deleteButton.addEventListener('click', function (event) {
        event.preventDefault();
        deleteItem();
    });

    var exportButton = document.createElement('button');
    exportButton.type = 'button';
    exportButton.id = 'exportButton';
    exportButton.innerHTML = 'Export to Excel';
    exportButton.setAttribute('class', 'btn btn-Secondary');
    exportButton.addEventListener('click', function (event) {
        event.preventDefault();
        var listName = document.getElementById('pageHeader').innerHTML;
        $.ajax({
            url: "/find-specific-list",
            method: "POST",
            contentType: "application/json",
            data: listName,
            success: function (data) {
                exportToExcel(data);
            },
            error: function (xhr) {
                console.log("Shid is fucked");
            }
        })
    })

    var collaboratorInput = document.createElement('input');
    collaboratorInput.type = 'text';
    collaboratorInput.id = 'collaboratorInput';
    collaboratorInput.setAttribute('class', 'col-md-3');
    collaboratorInput.placeholder = 'Add Collaborator';

    var addCollaboratorButton = document.createElement('button');
    addCollaboratorButton.type = 'button';
    addCollaboratorButton.id = 'addCollaboratorButton';
    addCollaboratorButton.innerHTML = 'Add Collaborator';
    addCollaboratorButton.setAttribute('class', 'btn btn-secondary');
    addCollaboratorButton.addEventListener('click', function (event) {
        event.preventDefault();
        addCollaborator();
    });

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
    titleHeader.setAttribute('id', 'tableTitleHeader');
    titleHeader.addEventListener('click', sortByTitleAscending);
    titleHeader.innerHTML = 'Title';
    titleHeader.style.cursor = 'pointer';

    var descHeader = document.createElement('th');
    descHeader.setAttribute('scope', 'col');
    descHeader.setAttribute('id', 'tableDescHeader');
    descHeader.addEventListener('click', sortByDescriptionAscending)
    descHeader.innerHTML = 'Description';
    descHeader.style.cursor = 'pointer';

    var userHeader = document.createElement('th');
    userHeader.setAttribute('scope', 'col');
    userHeader.setAttribute('id', 'tableUserHeader');
    userHeader.addEventListener('click', sortByUserAscending);
    userHeader.innerHTML = 'User';
    userHeader.style.cursor = 'pointer';

    var timeHeader = document.createElement('th');
    timeHeader.setAttribute('scope', 'col');
    timeHeader.setAttribute('id', 'tableTimeHeader');
    timeHeader.addEventListener('click', sortByCreationDateAscending);
    timeHeader.innerHTML = 'Created';
    timeHeader.style.cursor = 'pointer';

    var dueDateHeader = document.createElement('th');
    dueDateHeader.setAttribute('scope', 'col');
    dueDateHeader.setAttribute('id', 'tableDueDateHeader');
    dueDateHeader.addEventListener('click', sortByDueDateAscending)
    dueDateHeader.innerHTML = 'Due Date';
    dueDateHeader.style.cursor = 'pointer';

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

    secondDiv.appendChild(deleteButton);
    secondDiv.appendChild(exportButton);
    secondDiv.appendChild(collaboratorInput);
    secondDiv.appendChild(addCollaboratorButton);

    theHeader.appendChild(headerRow);


    itemTable.appendChild(theHeader);

    var tableBody = document.createElement('tbody');
    tableBody.id = 'tableBody';
    tableBody.className = 'table-hover';

    for (var i = 0; i < userItems.length; i++) {
        var row = document.createElement('tr');
        row.setAttribute('scope', 'row');

        var theCreator = userItems[i].username;

        var itemIdCol = document.createElement('td');
        itemIdCol.setAttribute('scope', 'col');
        itemIdCol.setAttribute('id', i);
        itemIdCol.innerHTML = userItems[i].id;
        itemIdCol.hidden = true;
        itemIdCol.style.display = 'none';

        var selectCol = document.createElement('td');
        selectCol.setAttribute('scope', 'col');
        selectCol.setAttribute('id', 'selectCol' + i);

        var selectRadio = document.createElement('input');
        selectRadio.setAttribute('scope', 'radio');
        selectRadio.setAttribute('type', 'radio');
        selectRadio.setAttribute('name', 'radio' + i);
        selectRadio.setAttribute('id', 'selectCol-' + i);
        selectRadio.setAttribute('class', 'form-check-input');

        selectCol.appendChild(selectRadio);


        var theCreationDate = new Date(userItems[i].creationTime);

        var titleCol = document.createElement('td');
        titleCol.setAttribute('scope', 'col');
        titleCol.setAttribute('id', 'title-' + i);
        titleCol.innerHTML = userItems[i].title;
        titleCol.style.cursor = 'pointer';
        titleCol.addEventListener('click', filterByTitle);

        var descCol = document.createElement('td');
        descCol.setAttribute('scope', 'col');
        descCol.setAttribute('id', 'desc-' + i);
        descCol.innerHTML = userItems[i].description;
        descCol.style.cursor = 'pointer';
        descCol.addEventListener('click', filterByDescription);

        var userCol = document.createElement('td');
        userCol.setAttribute('scope', 'col');
        userCol.setAttribute('id', 'user-' + i);
        userCol.innerHTML = userItems[i].creatorName;
        userCol.style.cursor = 'pointer';
        userCol.addEventListener('click', filterByUsername);

        var timeCol = document.createElement('td');
        timeCol.setAttribute('scope', 'col');
        timeCol.setAttribute('id', 'time-' + i);
        timeCol.innerHTML = theCreationDate.toLocaleDateString('en-US');
        timeCol.style.cursor = 'pointer';
        timeCol.addEventListener('click', filterByCreationTime);

        var dueDateCol = document.createElement('td');
        dueDateCol.setAttribute('scope', 'col');
        dueDateCol.setAttribute('id', 'dueDate-' + i);
        dueDateCol.innerHTML = userItems[i].dueDate;
        dueDateCol.style.cursor = 'pointer';
        dueDateCol.addEventListener('click', filterByDueDate);

        row.appendChild(itemIdCol);
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
            title.setAttribute('placeholder', 'Title of item');
            console.log(title);

            var descrip = document.getElementById('desc');
            descrip.setAttribute('placeholder', 'Description of item');

            var dueDate = document.getElementById('dueDate');
            dueDate.setAttribute('placeholder', 'Due date');

            var theData = [];
            theData = data.items;


            console.log(theData);

            var wholeItemTable = document.getElementById("itemTable");

            for (var i = 0; i <theData.length; i++) {

                var theCreationDate = new Date(theData[i].creationTime);

                var newRow = document.createElement("tr");
                newRow.setAttribute('scope', 'row');
                newRow.setAttribute('id', 'newTableRow-' + i);

                var itemIdCol = document.createElement('td');
                itemIdCol.setAttribute('scope', 'col');
                itemIdCol.setAttribute('id', i);
                itemIdCol.innerHTML = theData[i].id;
                itemIdCol.hidden = true;
                itemIdCol.style.display = 'none';

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
                titleCol.style.cursor = 'pointer';
                titleCol.innerHTML = theData[i].title;
                //titleCol.appendChild(filterByTitle);
                titleCol.addEventListener('click', filterByTitle);

                var descCol = document.createElement('td');
                descCol.setAttribute('scope', 'col');
                descCol.setAttribute('id', 'desc-' + i);
                //descCol.innerHTML = data[i].userItems[i].description;
                descCol.innerHTML = theData[i].description;
                descCol.style.cursor = 'pointer';
                descCol.addEventListener('click', filterByDescription);

                var userCol = document.createElement('td');
                userCol.setAttribute('scope', 'col');
                userCol.setAttribute('id', 'user-' + i);
                //userCol.innerHTML = data[i].userItems[i].username;
                userCol.innerHTML = theData[i].creatorName;
                //userCol.innerHTML = theData[i].user.username;
                userCol.style.cursor = 'pointer';
                userCol.addEventListener('click', filterByUsername);

                var timeCol = document.createElement('td');
                timeCol.setAttribute('scope', 'col');
                timeCol.setAttribute('id', 'time-' + i);
                timeCol.innerHTML = theCreationDate.toLocaleDateString('en-US');
                timeCol.style.cursor = 'pointer';
                timeCol.addEventListener('click', filterByCreationTime);

                var dueDateCol = document.createElement('td');
                dueDateCol.setAttribute('scope', 'col');
                dueDateCol.setAttribute('id', 'dueDate-' + i);
                //dueDateCol.innerHTML = data[i].dueDate;
                dueDateCol.innerHTML = theData[i].dueDate;
                dueDateCol.style.cursor = 'pointer';
                dueDateCol.addEventListener('click', filterByDueDate);

                //console.log(data[i].userItems[i].username);
                console.log(theData.creatorName);

                newRow.appendChild(itemIdCol);
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
            var theID = document.getElementById(i).innerHTML;
            console.log(title)
            console.log(desc);
            console.log(userName);
            console.log(theID);
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
                    "id": theID,
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

function signupForm(event) {
    event.preventDefault();

    var cardDiv = document.createElement("div");
    cardDiv.id = "cardDiv";

    var usernameDiv = document.createElement("div");
    usernameDiv.setAttribute("class", "username");

    var labelForUsername = document.createElement("label");
    labelForUsername.setAttribute("class", "label");
    labelForUsername.setAttribute("for", "signupUsername");
    labelForUsername.innerHTML = "Please enter a username";

    var usernameInput = document.createElement("input");
    usernameInput.type = "text";
    usernameInput.placeholder = 'Username'
    usernameInput.id = 'signupUsername';

    labelForUsername.appendChild(usernameInput);
    usernameDiv.appendChild(labelForUsername);
    cardDiv.appendChild(usernameDiv);

    var passwordDiv = document.createElement("div");
    passwordDiv.setAttribute("class", "password");

    var labelForPassword = document.createElement("label");
    labelForPassword.setAttribute("class", "label");
    labelForPassword.setAttribute("for", "signupPassword");
    labelForPassword.innerHTML = "Please enter your password";

    var passwordInput = document.createElement("input");
    passwordInput.type = "password";
    passwordInput.placeholder = 'Password'
    passwordInput.id = 'signupPassword';
    //passwordInput.setAttribute('class', 'password');

    labelForPassword.appendChild(passwordInput);
    passwordDiv.appendChild(labelForPassword);
    cardDiv.appendChild(passwordDiv);
    //usernameDiv.appendChild(passwordDiv);

    var emailDiv = document.createElement("div");
    emailDiv.setAttribute("class", "email");

    var emailLabel = document.createElement("label");
    emailLabel.setAttribute("class", "label");
    emailLabel.setAttribute("for", "signupEmail");
    emailLabel.innerHTML = "Please enter your email";

    var emailInput = document.createElement("input");
    emailInput.type = "email";
    emailInput.placeholder = 'Email'
    emailInput.id = 'signupEmail';
    emailInput.setAttribute('class', 'email');


    emailLabel.appendChild(emailInput);
    //emailInput.appendChild(passwordInput);
    emailDiv.appendChild(emailLabel);
    //usernameDiv.appendChild(emailDiv);
    cardDiv.appendChild(emailDiv);

    var newUserSignupButton = document.createElement("button");
    newUserSignupButton.setAttribute("class", "btn btn-primary");
    newUserSignupButton.id = "signupFormButton";
    newUserSignupButton.innerHTML = "Sign up";
    newUserSignupButton.addEventListener("click", function () {
        event.preventDefault();
        newUserSignup();
    });

    cardDiv.appendChild(newUserSignupButton);
    var container = document.getElementById("mainContainer");
    container.appendChild(cardDiv);
}

function newUserSignup() {
    var usernameFromSignupForm = document.getElementById('signupUsername').value;
    var passwordFromSignupForm = document.getElementById('signupPassword').value;
    var emailFromSignupForm = document.getElementById('signupEmail').value;

    console.log(usernameFromSignupForm);
    console.log(passwordFromSignupForm);
    console.log(emailFromSignupForm);

    $.ajax({
        url: "/signup",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({username: usernameFromSignupForm,
            password: passwordFromSignupForm,
            email: emailFromSignupForm}),
        success: function (data) {
            console.log(data);
            sessionStorage.setItem("username", data.username);
            splashPage(data);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function sortByTitle(event) {
    event.preventDefault();
    var tableBody = document.getElementById("tableBody");
    var rows = tableBody.getElementsByTagName("tr");

    const titles = [];
    const notSortedTitles = [];
    var rowsToAppend = [];
    console.log(rows.length);
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        titles.push(document.getElementById('title-' + i).innerHTML);
        notSortedTitles.push(document.getElementById('title-' + i).innerHTML);
        rowsToAppend.push(row);
    }

    const sortedTitles = titles.sort();
    console.log(notSortedTitles);
    console.log(sortedTitles);

    tableBody.remove(document.getElementsByTagName('tr'));

    //iterate through all rows until rows = 0

        for (let j = 0, k = 0; k < rowsToAppend.length;) {
            //var rowTitle = document.getElementById('title-' + k).innerHTML;
            //if the title in the row matches the sorted row append that row to the table and remove that row from the array of rows?
            if (sortedTitles[j] !== notSortedTitles[k]) {
                j++;
                k = 0;
            }
                console.log(sortedTitles[j]);
                console.log(notSortedTitles[k]);
                sortedTitles.splice(j, 1);
                notSortedTitles.splice(k, 1);
                tableBody.appendChild(rowsToAppend[k]);
                rowsToAppend.splice(k, 1);
                //tableBody.insertBefore(rows[k], rows[k]);
                //tableBody.appendChild(rowsToAppend[k]);

                //tableBody.appendChild(rowsToAppend[j]);
                //tableBody.appendChild(rows[j]);
                //rows.removeChild(rows[j]);

                //Reset iterator so it starts from the beginning again
                j = 0;
                //console.log(rowsToAppend);
                k++;
            }
    var theTable = document.getElementById("itemTable");
    theTable.appendChild(tableBody);
    console.log(rowsToAppend);

    var titleSortHeader = document.getElementById('tableTitleHeader');
    titleSortHeader.removeEventListener('click', sortByTitle);

    titleSortHeader.addEventListener('click', sortDescendingByTitle);
    titleSortHeader.style.cursor = 'pointer';
}

function sortDescendingByTitle(event) {
    event.preventDefault();

    var tableBody = document.getElementById("tableBody");
    var rows = tableBody.getElementsByTagName("tr");

    const titles = [];
    const notSortedTitles = [];
    var rowsToAppend = [];
    console.log(rows.length);

    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        titles.push(document.getElementById('title-' + i).innerHTML);
        notSortedTitles.push(document.getElementById('title-' + i).innerHTML);
        rowsToAppend.push(row);
    }

    const sortedTitles = titles.sort();
    sortedTitles.reverse();
    console.log(notSortedTitles);
    console.log(sortedTitles);

    tableBody.remove(document.getElementsByTagName('tr'));

    for (let j = 0, k = 0; k < rowsToAppend.length;) {
        //var rowTitle = document.getElementById('title-' + k).innerHTML;
        //if the title in the row matches the sorted row append that row to the table and remove that row from the array of rows?
        if (sortedTitles[j] !== notSortedTitles[k]) {
            j++;
            k = 0;
        }
        console.log(sortedTitles[j]);
        console.log(notSortedTitles[k]);
        sortedTitles.splice(j, 1);
        notSortedTitles.splice(k, 1);
        tableBody.appendChild(rowsToAppend[k]);
        rowsToAppend.splice(k, 1);
        //tableBody.insertBefore(rows[k], rows[k]);
        //tableBody.appendChild(rowsToAppend[k]);

        //tableBody.appendChild(rowsToAppend[j]);
        //tableBody.appendChild(rows[j]);
        //rows.removeChild(rows[j]);

        //Reset iterator so it starts from the beginning again
        j = 0;
        //console.log(rowsToAppend);
        k++;
    }
    var theTable = document.getElementById("itemTable");
    theTable.appendChild(tableBody);
    console.log(rowsToAppend);

    var titleSortHeader = document.getElementById('tableTitleHeader');
    titleSortHeader.removeEventListener('click', sortDescendingByTitle);

    titleSortHeader.addEventListener('click', sortByTitle);
    titleSortHeader.style.cursor = 'pointer';
}

function filterByTitle(event) {
    var title = event.target.innerHTML;
    var listName = document.getElementById("pageHeader").innerHTML;
    $.ajax({
        url: '/filterByTitle',
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            title: title,
            listName: listName,
        }),
        success: function (data) {
            displayItemsPage(data);
            var theTitleColumn = document.getElementById('title-0');
            theTitleColumn.removeEventListener('click', sortByTitle);
            theTitleColumn.addEventListener('click', getSpecificListInTableView);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function filterByDescription(event) {
    var description = event.target.innerHTML;
    var listName = document.getElementById("pageHeader").innerHTML;
    $.ajax({
        url: '/filterByDescription',
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            description: description,
            listName: listName,
        }),
        success: function (data) {
            displayItemsPage(data);
            var theDescriptionColumn = document.getElementById('desc-0');
            theDescriptionColumn.removeEventListener('click', filterByDescription);
            theDescriptionColumn.addEventListener('click', getSpecificListInTableView);
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function filterByUsername(event) {
    var username = event.target.innerHTML;
    var listName = document.getElementById("pageHeader").innerHTML;
    $.ajax({
        url: '/filterByUsername',
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            username: username,
            listName: listName,
        }),
        success: function (data) {
            displayItemsPage(data);
            if (data.length > 1) {
                for (var j = 0; j < data.length; j++) {
                    var theUserColumn = document.getElementById('user-' + j);
                    console.log(theUserColumn);
                    theUserColumn.removeEventListener('click', filterByUsername);
                    theUserColumn.addEventListener('click', getSpecificListInTableView);
                }
            }
            else {
                var theUserColumn = document.getElementById('user-0');
                console.log(theUserColumn);
                theUserColumn.removeEventListener('click', filterByDescription);
                theUserColumn.addEventListener('click', getSpecificListInTableView);
            }
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function filterByDueDate(event) {
    var dueDate = event.target.innerHTML;
    var listName = document.getElementById("pageHeader").innerHTML;
    $.ajax({
        url: '/filterByDueDate',
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            dueDate: dueDate,
            listName: listName,
        }),
        success: function (data) {
            displayItemsPage(data);
            if (data.length > 1) {
                for (var j = 0; j < data.length; j++) {
                    var theDateColumn = document.getElementById('dueDate-' + j);
                    theDateColumn.removeEventListener('click', filterByDueDate);
                    theDateColumn.addEventListener('click', getSpecificListInTableView);
                }
            }
            else {
                var theDateColumn = document.getElementById('dueDate-0');
                theDateColumn.removeEventListener('click', filterByDueDate);
                theDateColumn.addEventListener('click', getSpecificListInTableView);
            }
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function filterByCreationTime(event) {
    var creationTime = event.target.innerHTML;
    var listName = document.getElementById("pageHeader").innerHTML;
    $.ajax({
        url: '/filterByCreationTime',
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            creationTime: creationTime,
            listName: listName,
        }),
        success: function (data) {
            displayItemsPage(data);
            if (data.length > 1) {
                for (var j = 0; j < data.length; j++) {
                    var theCreationColumn = document.getElementById('time-' + j);
                    console.log(theCreationColumn);
                    theCreationColumn.removeEventListener('click', filterByCreationTime);
                    theCreationColumn.addEventListener('click', getSpecificListInTableView);
                }
            }
            else {
                var theCreationColumn = document.getElementById('time-0');
                console.log(theCreationColumn);
                theCreationColumn.removeEventListener('click', filterByCreationTime);
                theCreationColumn.addEventListener('click', getSpecificListInTableView);
            }
        },
        error: function (xhr) {
            console.log("Error has occured");
        }
    })
}

function getSpecificListInTableView(event) {
    event.preventDefault()
    var listName = document.getElementById('pageHeader').innerHTML;
    $.ajax({
        url: "/find-specific-list",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {
            displayItemsPage(data);
        },
        error: function (xhr) {
            console.log("Shid is fucked");
        }
    })
}

function exportToExcel() {
    var listName = document.getElementById('pageHeader').innerHTML;
    $.ajax({
        url: "/exportToExcel",
        method: "POST",
        contentType: "application/json",
        data: listName,
        success: function (data) {

        },
        error: function (xhr) {
            console.log("Shid is fucked");
        }
    })
}

/*async function exportToExcel() {
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('userExport', {});

    var rows = document.getElementsByTagName('tr');
    console.log(rows);
    for (var i = 0; i < rows.length; i++) {
        var columnToDelete = document.getElementById('radio-' + i);
        rows[i].removeChild(columnToDelete);
        worksheet.addRow(rows[i]);
    }
    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new blob([buffer], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'userExport.xlsx';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);

} */


/*function exportToExcel(data, filename = 'listexport.xlsx') {
    //let XLSX = require('xlsx');
    const workbook = XLSX.utils.book_new();

    let worksheet;
    if (Array.isArray(data) && Array.isArray(data[0])) {
        worksheet = XLSX.utils.aoa_to_sheet(data);
    }else if (Array.isArray(data) && typeof data[0] === 'object') {
        worksheet = XLSX.utils.json_to_sheet(data);
    }else {
        console.error("This shid didn't work amigo!");
        return;
    }
    XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet1");

    XLSX.writeFile(workbook, filename);
}*/

        //tableBody.remove(document.getElementsByTagName('tr'));
        //tableBody.append(rowsToAppend);

function sortByDescription(event) {
    event.preventDefault();
}

function sortByUser(event) {
    event.preventDefault();
}

function sortByCreationTime(event) {
    event.preventDefault();
}

function sortByDueDate(event) {
    event.preventDefault();
}