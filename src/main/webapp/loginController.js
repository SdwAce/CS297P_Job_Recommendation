var LoginApp = angular.module("loginPage",["ngRoute","ngStorage"]);

LoginApp.controller("loginController",function($scope,$http,$location,$localStorage,$sessionStorage)
{
    $scope.check_user = function(){

        $scope.data = {
            "user_id": $scope.username,
            "password": $scope.password
        }

        $http({method: 'Post', url:"http://localhost:8080/Job_Recommendation/login",data:$scope.data})
            .then(function success(response)
            {
                if (response.data)
                {
                    $localStorage.name = $scope.username;
                    window.location.href = "search.html";
                }
                else(response.data)
                {
                    console.log(response.data);
                }
            },function error(response)
            {
                console.log(response.data);
                $scope.user_not_exist = response.data["status"];
            })
    }


    $scope.EnterSearchPage = function()
    {
        $location.path("./search");
    }

})