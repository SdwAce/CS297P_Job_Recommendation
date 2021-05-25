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
                // $http({method:"Get",url:"http://localhost:8080/Job_Recommendation/show"})
                // .then(function success(response)
                // {
                //     console.log(response);
                //     $scope.job_history = Object.values(response.data);

                // },function error(response)
                // {
                //     console.log(response);
                // })
            }
            else(response.data)
            {
                console.log(response.data);
                // alert(response);
            }
        },function error(response)
        {
            console.log(response.data);
        })
    }


    $scope.EnterSearchPage = function()
    {
        $location.path("./search");
    }

})




