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
                $http({method:'Post',url:"http://localhost:8080/Job_Recommendation/searchnearby",data:locationData})
                .then(function success(response)
                {
                    $scope.jobs_by_location = Object.values(Object.values(response.data));
                    console.log($scope.jobs_by_location);
                    var mapOptions = {
                        zoom: 4,
                        center: new google.maps.LatLng(locationData.lat,locationData.lon),
                        mapTypeId: google.maps.MapTypeId.TERRAIN
                    };

                    $scope.mymapdetail = new google.maps.Map(document.getElementById('jobMap'), mapOptions);
                    $scope.addMarkers($scope.jobs_by_location);

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

    $scope.addMarkers = function (jobs)
    {
        for (i = 0; i < jobs.length; ++i)
        {
            console.log(jobs[i].lat);
            console.log(jobs[i].lon);
            new google.maps.Marker({
            map: $scope.mymapdetail,
            icon: pinSymbol("#FFF"),
            position: new google.maps.LatLng(jobs[i].lat, jobs[i].lon),
            title: "work location"
            });
        }
    }




    $scope.Likes = function (index)
    {
        // sel.toggle("fa-thumbs-down");
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

