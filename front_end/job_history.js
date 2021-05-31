var app = angular.module("FavoriteJobHistory",["ngStorage"]);

app.controller("JobHistoryController",function($http,$scope,$localStorage)
{

    $scope.name = $localStorage.name;
    // console.log($scope.name);
    var request = {
        "user_id":$scope.name
    }
    $http({method:"Post",url:"http://localhost:8080/Job_Recommendation/show",data:request})
    .then(function success(response)
    {
        console.log(response);
        $scope.job_history = Object.values(response.data);
        $scope.liked_jobs = [];
        for (i = 0; i < $scope.job_history.length; ++i)
        {
            $scope.liked_jobs.push(1);
        }

    },function error(response)
    {
        console.log(response);
    })


    //add unlike and like function
    $scope.Likes = function (index)
    {
        // sel.toggle("fa-thumbs-down");
        var jobId = $scope.job_history[index].job_id;
        var test_data = {
            user_id : $scope.name,
            job_id: jobId
        }

        console.log(test_data);

        if ($scope.liked_jobs[index] === 0)
        {
            $http({method:"Post",url:"http://localhost:8080/Job_Recommendation/save",data:test_data})
            .then(function success(response)
            {
                // $scope.new_job_likes = $scope.job_history[index].job_title + " is added to your favorite list. ";
                console.log(response);
                $scope.liked_jobs[index] = 1;
            }, function error(response)
            {
                console.log(response);
            })
        }
        else{
            $http({method:"Delete",url:"http://localhost:8080/Job_Recommendation/save",data:test_data})
            .then(function success(response)
            {
                console.log(response);
                alert("It is deleted");
                $scope.liked_jobs[index] = 0;
            }, function error(response)
            {
                console.log(response);
            })
        }
    }

    

    
})