var app = angular.module("JobRecommendation", []);
app.controller("SearchController",function($scope,$http)
{
    $scope.names = "";

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
        query = query.slice(0,-1);
        return query;
    }

    $scope.recommend = function()
    {
        console.log("http://localhost:8080/Job_Recommendation/search?" + $scope.create_query());
        $http.get("http://localhost:8080/Job_Recommendation/search?" + $scope.create_query())
        .then(function success(response)
        {
            $scope.names = Object.values(response.data)
        },
        function error(response)
        {
            console.log(response);
        });
    }
})
