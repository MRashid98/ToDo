const params = new URLSearchParams(window.location.search); // Make a collection of the ids

for (let param of params) {
    console.log(param);
    let id = param[1];
    console.log(id);
    getSingleRecord(id);
}


function getSingleRecord(id) {
    fetch('http://localhost:9001/tasklist/read/' + id)
        .then(
            function(response) {
                if (response.status !== 200) {
                    console.log('Looks like there was a problem. Status Code: ' +
                        response.status);
                    return;
                }

                // Examine the text in the response
                response.json().then(function(data) {
                    document.getElementById("id").value = data.id;
                    document.getElementById("collName").value = data.collName;
                });
            }
        )
        .catch(function(err) {
            console.log('Fetch Error :-S', err);
        });
}

document.getElementById("updateBtn").addEventListener("click", function(stop) {

    let formElements = document.querySelector("form.taskRecord").elements;

    let id = formElements["id"].value;
    let collName = formElements["collName"].value;

    updateTask(id, collName);

})

function updateTask(id, taskName, taskDesc) {
    fetch('http://localhost:9001/tasklist/update/' + id, {
            method: 'put',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify({
                "collName": taskName,
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