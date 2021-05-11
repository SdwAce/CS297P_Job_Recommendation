var LoginApp = angular.module("loginPage",["ngRoute"]);

LoginApp.config(function ($routeProvider)
{
    $routeProvider.
    when("/",{
        templateUrl: "login1.html"
    })
    .when("/search",{
        templateUrl: "search.html",
        controller: "SearchController"
    })
    .otherwise({
        redirectTo : "/"
    });
});

LoginApp.controller("loginController",function($scope,$http,$location)
{
    $scope.check_user = function(){
        console.log($cope.username);
        console.log($scope.password);
        console.log("http://localhost:8080/Job_Recommendation/login",{data:{username:$scope.username,password:$scope.password}});
        $http.post("http://localhost:8080/Job_Recommendation/login",{data:{username:$scope.username,password:$scope.password}})
        .then(function success(response)
        {
            if (response.data)
            {
                console.log(response.data);
                // $scope.EnterSearchPage($location);
            }
            else(response.data)
            {
                alert(response);
            }
        },function error(response)
        {
            console.log(response);
        })
    }


    $scope.EnterSearchPage = function()
    {
        $location.path("/search");
    }

})




