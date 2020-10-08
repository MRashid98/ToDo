document.querySelector("form.taskRecord").addEventListener("submit", function(stop) {

    let formElements = document.querySelector("form.taskRecord").elements;
    let collectionName = formElements["collectionName"].value;

    addCollection(collectionName);
})

function addCollection(collectionName) {
    fetch('http://localhost:9001/tasklist/create/', {
            method: 'post',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify({
                "collName": collectionName
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