var app = angular.module("FavoriteJobHistory",["ngStorage",'ngAnimate', 'ngSanitize', 'ui.bootstrap']);

app.controller("JobHistoryController",function($http,$scope,$localStorage,$uibModal, $log, $document)
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
                $scope.new_job_likes = $scope.job_history[index].job_title + " is added to your favorite list. ";
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
              $scope.unfavorite = $scope.job_history[index].job_title + "has been removed from your favorite list";
              $scope.liked_jobs[index] = 0;
            }, function error(response)
            {
                console.log(response);
            })
        }
    }

    $scope.display_history = function ()
    {
        console.log(123);
        window.location.href = "show_history.html";
    }

    $scope.display_location_recommendation = function ()
    {
        window.location.href = "search_by_location.html";
    }


    $scope.display_profile = function()
    {
        window.location.href = "profile.html";
    }

    $scope.logout =  function () {
      $http({method: 'Post', url:"http://localhost:8080/Job_Recommendation/logout"})
      .then(function success(response)
      {
          window.location.href = "login.html";
      },
      function(error)
      {
          console.log(error);
      })
    };
  
    $scope.animationsEnabled = true;
  
    $scope.open = function (index,size, parentSelector) {
      var parentElem = parentSelector ? 
        angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
        console.log(parentElem);

      var modalInstance = $uibModal.open({
        animation: $scope.animationsEnabled,
        ariaLabelledBy: 'modal-title',
        ariaDescribedBy: 'modal-body',
        templateUrl: 'Job_Description.html',
        controller: 'ModalInstanceCtrl',
        // controllerAs: '$ctrl',
        size: size,
        appendTo: parentElem,
        resolve: {
          items: function () {
            return $scope.job_history[index].job_description;
          }
        }
      });
      console.log(123);
  
      modalInstance.result.then(function (selectedItem) {
        $scope.selected = selectedItem;
      }, function () {
        $log.info('Modal dismissed at: ' + new Date());
      });
      console.log(456);
    };
    
})


app.controller('ModalInstanceCtrl', function ($uibModalInstance, items, $scope) {
    // var $ctrl = this;
    console.log(123);
    $scope.items = items;
    // $scope.selected = {
    //   item: $scope.items[0]
    // };
  
    $scope.ok = function () {
      $uibModalInstance.close($scope.selected.item);
    };
    console.log(789);
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  });