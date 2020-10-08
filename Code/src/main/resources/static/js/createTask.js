const params = new URLSearchParams(window.location.search); // Make a collection of the ids

for (let param of params) {
    console.log(param);
    let id = param[1];
    console.log(id);
    captureCollId(id);
}


function captureCollId(id) {
    document.querySelector("form.taskRecord").addEventListener("submit", function(stop) {
        stop.preventDefault();

        let formElements = document.querySelector("form.taskRecord").elements;
        let taskName = formElements["taskName"].value;
        let taskDesc = formElements["taskDesc"].value;
        let collId = id;

        addTask(taskName, taskDesc, collId);
    })

}

function addTask(taskName, taskDesc, collId) {
    fetch('http://localhost:9001/task/create/', {
            method: 'post',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify({
                "taskName": taskName,
                "taskDesc": taskDesc,
                "coll": {
                    "id": collId
                }
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