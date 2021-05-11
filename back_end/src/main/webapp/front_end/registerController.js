var registerApp = angular.module("registerPage",[]);

registerApp.controller("RegisterController",function($scope,$http)
{

    $scope.fetch = function ()
    {
        $scope.data = {
            "user_id": $scope.username,
            "password": $scope.password,
            "firstName": $scope.first_name,
            "lastName": $scope.last_name
        }

        console.log($scope.data);
        $http({method: 'Post',url:'http://localhost:8080/Job_Recommendation/register',data:$scope.data})
        .then(function(response)
        {
            console.log(response);
            alert(Object.values(response.data));

        }),function error(response)
        {
            console.log(response);
        }
    }
})
