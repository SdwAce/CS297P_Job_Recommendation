var app = angular.module("JobRecommendation", ["ngStorage"]);
app.controller("SearchController",function($scope,$http,$localStorage,$sessionStorage)
{

    $scope.name = $localStorage.name;

    $scope.create_query = function()
    {
        var query = "";
        if ($scope.title !== "")
        {
            query = query.concat("title=" + $scope.title + "&");
        }
        if ($scope.location !== "")
        {
            query = query.concat("location=" + $scope.location + "&");
        }
        if ($scope.skill !== "")
        {
            query = query.concat("skill=" + $scope.skill + "&");
        }
        if ($scope.company !== "")
        {
            query = query.concat("company=" + $scope.company + "&");
        }
        query = query.slice(0,-1);
        return query;
    }

    $scope.recommend = function()
    {
        $http.get("http://localhost:8080/Job_Recommendation/search?" + $scope.create_query())
        .then(function success(response)
        {
            console.log(response);
            $scope.job_listings = Object.values(response.data)
        },
        function error(response)
        {
            console.log(response);
        });
    }

    $scope.Likes = function (index)
    {
        var jobId = $scope.job_listings[index].job_id;
        var test_data = {
            user_id : $scope.name,
            job_id: jobId
        }
        $http({method:"Post",url:"http://localhost:8080/Job_Recommendation/save",data:test_data})
        .then(function success(response)
        {
            $scope.new_job_likes = $scope.job_listings[index].title + " has been added to your favorite list";
            // alert("It went through")
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

    $scope.logout =  function () {
        $http({method: 'Post', url:"http://localhost:8080/Job_Recommendation/logout"})
        .then(function success(response)
        {
            window.location.href = "login.html";
        },
        function(error)
        {
            console.log(error);
        })
    };
})
