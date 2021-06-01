var app =  angular.module("ProfileSetter",["ngStorage"]);

app.controller("ProfileController",function($scope,$localStorage,$http,$document)
{
    $scope.name = $localStorage.name;
    console.log($scope.name);
    var request = {
        "user_id": $scope.name
    }
    $http({method:"Post",url:"http://localhost:8080/Job_Recommendation/check",data:request})
    .then(function success (response)
    {
        console.log(response);

        if (response.data === null)
        {
            document.getElementById("username").value = $scope.name;
            console.log("there is no response");
            return;
        }
        document.getElementById("username").value = response.data["user_id"];
        document.getElementById("email").value = response.data["email"];
        document.getElementById("major").value = response.data["major"];

        document.getElementById(response.data["level"]).selected = true;
        if (response.data["find_Job"])
        {
            document.getElementById("job_found").selected = true;
        }
        else{
            document.getElementById("job_not_found").selected = true;
        }
        

    }, function error(response)
    {
        console.log(response);
    })



    $scope.submit = function ()
    {
        var request = {
            "user_id": document.getElementById("username").value,
            "email": document.getElementById("email").value,
            "major": document.getElementById("major").value,
            "level": document.getElementById("grade").value,
            "find_Job": document.getElementById("job_hired").value
        }

        $http({method:"Post",url:"http://localhost:8080/Job_Recommendation/profile",data:request})
        .then(function success (response)
        {
            console.log(response);
            
            
            

        }, function error(response)
        {
            console.log(response);
        })
    }

})