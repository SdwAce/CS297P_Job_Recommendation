var app = angular.module("RecommendationByLocation",["ngStorage"]);

app.controller("RecommendLocationController",function($scope,$http)
{
    
    $scope.getLocation = function ($localStorage,$sessionStorage)
    {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function success(response)
            {       
                var locationData = {
                    "lon": response.coords.longitude.toString(),
                    "lat": response.coords.latitude.toString()
                }
                console.log(response.coords.longitude);
                console.log(response.coords.latitude);
                $http({method:'Post',url:"http://localhost:8080/Job_Recommendation/searchnearby",data:locationData})
                .then(function success(response)
                {
                    console.log(response);
                    $scope.jobs_by_location = Object.values(Object.values(response.data));
                    console.log($scope.jobs_by_location);
                    
                },function error(response)
                {
                    console.log(response);
                })

            },function error(response)
            {
                console.log(response);
            });
        }
    }

    $scope.Likes = function (index)
    {
        var jobId = $scope.jobs_by_location[index].job_id;
        var test_data = {
            user_id : "diwen",
            job_id: jobId
        }
        $http({method:"Post",url:"http://localhost:8080/Job_Recommendation/save",data:test_data})
        .then(function success(response)
        {
            alert("It went through")
            console.log(response);
        }, function error(response)
        {
            console.log(response);
        })
    }

    $scope.display_history = function ()
    {
        console.log(123);
        window.location.href = "show_history.html";
    }

    $scope.display_location_recommendation = function ()
    {
        window.location.href = "search_by_location.html";
    }

})

