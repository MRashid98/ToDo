const params = new URLSearchParams(window.location.search); // Make a collection of the ids

for (let param of params) {
    console.log(param);
    let id = param[1];
    console.log(id);
    getSingleRecord(id);
}

function getSingleRecord(id) {
    fetch('http://localhost:9001/task/read/' + id)
        .then(
            function(response) {
                if (response.status !== 200) {
                    console.log('Looks like there was a problem. Status Code: ' +
                        response.status);
                    return;
                }

                // Examine the text in the response
                response.json().then(function(data) {
                    console.log(data);

                    document.getElementById("id").value = data.id;
                    document.getElementById("taskName").value = data.taskName;
                    document.getElementById("taskDesc").value = data.taskDesc;

                });
            }
        )
        .catch(function(err) {
            console.log('Fetch Error :-S', err);
        });
}



document.getElementById("updateBtn").addEventListener("click", function(stop) {
    stop.preventDefault();
    let formElements = document.querySelector("form.taskRecord").elements;

    let id = formElements["id"].value;
    let taskName = formElements["taskName"].value;
    let taskDesc = formElements["taskDesc"].value;

    updateTask(id, taskName, taskDesc);

})

document.getElementById("deleteBtn").addEventListener("click", function(stop) {
    stop.preventDefault();
    let formElements = document.querySelector("form.taskRecord").elements;

    let id = formElements["id"].value;
    deleteTask(id);
})

function updateTask(id, taskName, taskDesc) {
    fetch('http://localhost:9001/task/update/' + id, {
            method: 'put',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify({
                "taskName": taskName,
                "taskDesc": taskDesc
            })
        })
        .then(res => res.json())
        .then(function(data) {
            console.log('Request succeeded with JSON response', data);
        })
        .catch(function(error) {
            console.log('Request failed', error);
        });
}

function deleteTask(id) {
    fetch('http://localhost:9001/task/delete/' + id, {
            method: 'delete',
            headers: {
                "Content-type": "application/json"
            }
        })
}