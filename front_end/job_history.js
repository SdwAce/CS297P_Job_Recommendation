var app = angular.module("FavoriteJobHistory",["ngStorage"]);

app.controller("JobHistoryController",function($http,$scope,$localStorage)
{
    $scope.name = localStorage.name;
    $http({method:"Get",url:"http://localhost:8080/Job_Recommendation/show"})
    .then(function success(response)
    {
        console.log(response);
        $scope.job_history = Object.values(response.data);

    },function error(response)
    {
        console.log(response);
    })


    

    
})