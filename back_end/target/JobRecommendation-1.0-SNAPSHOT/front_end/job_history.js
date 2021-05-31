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

    },function error(response)
    {
        console.log(response);
    })


    


    

    
})