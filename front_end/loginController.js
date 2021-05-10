var LoginApp = angular.module("loginPage",["ngRoute"]);

LoginApp.config(function ($routeProvider)
{
    $routeProvider.
    when("/",{
        templateUrl: "login.html"
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
        $http.post("http://localhost:8080/Job_Recommendation/login",{data:{username:$scope.username,password:$scope.password}})
        .then(function success(response)
        {
            if (response.data)
            {
                $scope.EnterSearchPage($location);
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




