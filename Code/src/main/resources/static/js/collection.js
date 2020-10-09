fetch('http://localhost:9001/tasklist/readall/')
    .then(
        function(response) {
            if (response.status !== 200) {
                console.log('Looks like there was a problem. Status Code: ' +
                    response.status);
                return;
            }

            // Examine the text in the response
            response.json().then(function(collectionData) {

                    let table = document.querySelector("table"); // Creates an element called Table

                    for (let collection of collectionData) {
                        let row = table.insertRow();
                        let text = document.createTextNode(collection.collName);
                        let th = document.createElement("th"); // Create table header element
                        th.appendChild(text); // Connect the text to the header
                        row.appendChild(th); // Will fill up the header to the first row

                        let addCell = row.insertCell();
                        let addBtn = document.createElement("a");
                        addBtn.innerHTML = "Add New Task";
                        addBtn.className = "btn btn-success";
                        addBtn.href = "newTask.html?id=" + collection.id;
                        addCell.appendChild(addBtn);

                        let editCell = row.insertCell();
                        let updateBtn = document.createElement("a");
                        updateBtn.innerHTML = "Update Collection";
                        updateBtn.className = "btn btn-primary";
                        updateBtn.href = "updateColl.html?id=" + collection.id;
                        editCell.appendChild(updateBtn);

                        let delBtn = document.createElement("a");
                        delBtn.innerHTML = "Delete Collection";
                        delBtn.className = "btn btn-danger";
                        delBtn.onclick = function() {
                            deleteCollection(collection.id);
                            location.reload();
                        }
                        editCell.appendChild(delBtn);

                        for (let tasks of collection.tasks) {
                            console.log(tasks);
                            let taskRow = table.insertRow();
                            taskRow.className = "table table-light";
                            let taskCellName = taskRow.insertCell();
                            let taskCellDesc = taskRow.insertCell();


                            let taskTitle = document.createTextNode(tasks.taskName);
                            taskCellName.appendChild(taskTitle);

                            let taskInfo = document.createTextNode(tasks.taskDesc);
                            taskCellDesc.appendChild(taskInfo);

                            let viewCell = taskRow.insertCell();
                            let viewBtn = document.createElement("a");
                            viewBtn.innerHTML = "view";
                            viewBtn.className = "btn btn-primary";
                            viewBtn.href = "viewTask.html?id=" + tasks.id;
                            viewCell.appendChild(viewBtn);
                        }
                    }

                })
                .catch(function(err) {
                    console.log('Fetch Error :-S', err);
                });
        });

function deleteCollection(id) {
    fetch('http://localhost:9001/tasklist/delete/' + id, {
        method: 'delete',
        headers: {
            "Content-type": "application/json"
        }
    })
}