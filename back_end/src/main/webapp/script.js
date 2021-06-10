var app = angular.module("JobRecommendation", ["ngStorage",'ngAnimate', 'ngSanitize', 'ui.bootstrap']);
app.controller("SearchController",function($scope,$http,$localStorage,$uibModal, $log, $document)
{

    $scope.name = $localStorage.name;

    $scope.create_query = function()
    {
        var query = "";
        if (typeof $scope.title !== "undefined")
        {
            query = query.concat("title=" + $scope.title + "&");
        }
        if (typeof $scope.location !== "undefined")
        {
            query = query.concat("location=" + $scope.location + "&");
        }
        if (typeof $scope.skill !== "undefined")
        {
            query = query.concat("skill=" + $scope.skill + "&");
        }
        if (typeof $scope.company !== "undefined")
        {
            query = query.concat("company=" + $scope.company + "&");
        }
        if (typeof $scope.name !== "undefined")
        {
            query = query.concat("user_id=" + $scope.name + "&");
        }
        query = query.slice(0,-1);
        return query;
    }


    //add a header in case we don't get any result back from the user
    $scope.recommend = function()
    {
        $http.get("http://localhost:8080/Job_Recommendation/search?" + $scope.create_query())
        .then(function success(response)
        {
            console.log(response);
            var jobs = Object.values(response.data);
            
            console.log(jobs);
            if (jobs.length === 0)
            {
                $scope.no_job = "This search returns no result. Please try again";
            }
            $scope.job_listings = [];
            $scope.liked_jobs = []
            for (i = 0; i< jobs.length;++i)
            {
                if (!jobs[i].favorite)
                {
                    $scope.job_listings.push(jobs[i]);
                    $scope.liked_jobs.push(0);
                }
            }
            console.log($scope.job_listings);
        },
        function error(response)
        {
            console.log(response);
        });
    }



    $scope.Likes = function (index)
    {
        var jobId = $scope.job_listings[index].job_id;
        var test_data = {
            user_id : $scope.name,
            job_id: jobId
        }

        if ($scope.liked_jobs[index] === 0)
        {
            $http({method:"Post",url:"http://localhost:8080/Job_Recommendation/save",data:test_data})
            .then(function success(response)
            {
                $scope.new_job_likes = $scope.job_listings[index].title + " has been added to your favorite list";
                // alert("It went through")
                $scope.liked_jobs[index] = 1;
                console.log(response);
            }, function error(response)
            {
                console.log(response);
            })
        }
        else{
            $http({method:"Delete",url:"http://localhost:8080/Job_Recommendation/save",data:test_data})
            .then(function success(response)
            {
                $scope.unfavorite = $scope.job_listings[index].title + "has been removed from your favorite list";
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

    // var $scope = this;
    // $scope.items = ['item1', 'item2', 'item3'];
  
    $scope.animationsEnabled = true;
  
    $scope.open = function (index,size, parentSelector) {
      var parentElem = parentSelector ? 
        angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;

        console.log($scope.job_listings[index].job_description);
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
            return $scope.job_listings[index].job_description;
          }
        }
      });
  
      modalInstance.result.then(function (selectedItem) {
        $scope.selected = selectedItem;
      }, function () {
        $log.info('Modal dismissed at: ' + new Date());
      });
    };

    
})

app.controller('ModalInstanceCtrl', function ($uibModalInstance, items, $scope) {
    // var $ctrl = this;
    $scope.items = items;
    // $scope.selected = {
    //   item: $scope.items[0]
    // };
  
    $scope.ok = function () {
      $uibModalInstance.close($scope.selected.item);
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  });