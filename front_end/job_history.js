var HistoryApp = angular.module("FavoriteJobHistory",["ngStorage"]);

HistoryApp.controller("JobHistoryController",function($http,$scope,$localStorage)
{
    
    $http({method:"Get",url:"http://localhost:8080/Job_Recommendation/show"})
    .then(function success(response)
    {
        console.log(response);
        $scope.job_history = Object.values(repsonse.data);

    },function error(response)
    {
        console.log(response);
    })


    

    
})