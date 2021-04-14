function recommend() {
    fetch("http://localhost:8080/Job_Recommendation/search")
    .then(function(response)
    {
        return response.json();
    })
    .then(function(dataset)
    {
        display(dataset);
    })
    .catch(function (err)
    {
        console.log(err);
    });
}

function display(data)
{
    var result = document.getElementById("search_result");
    var div = document.createElement("div");
    div.innerHTML = JSON.stringify(data);
    result.appendChild(div);
}