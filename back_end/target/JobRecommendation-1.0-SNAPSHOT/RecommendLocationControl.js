var app = angular.module("RecommendationByLocation",["ngStorage",'ngAnimate', 'ngSanitize', 'ui.bootstrap']);

app.controller("RecommendLocationController",function($scope,$http,$localStorage,$uibModal, $log, $document)
{

    console.log($localStorage.name);
    $scope.name = $localStorage.name;
    $scope.getLocation = function ($localStorage,$sessionStorage)
    {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function success(response)
            {     

                console.log(response.coords.latitude.toString());
                console.log(response.coords.longitude.toString());
                var locationData = {
                    "lon": response.coords.longitude.toString(),
                    "lat": response.coords.latitude.toString(),
                    "user_id": $scope.name
                }
                $http({method:'Post',url:"http://localhost:8080/Job_Recommendation/searchnearby",data:locationData})
                .then(function success(response)
                {
                    var jobs = Object.values(response.data);

                    console.log(jobs);

                    $scope.jobs_by_location = [];

                    $scope.liked_jobs = []
                    for (i = 0; i< jobs.length;++i)
                    {
                        if (!jobs[i].favorite)
                        {
                            $scope.jobs_by_location.push(jobs[i]);
                            $scope.liked_jobs.push(0);
                        }
                    }
                    // console.log($scope.jobs_by_location);
                    var mapOptions = {
                        zoom: 4,
                        center: new google.maps.LatLng(locationData.lat,locationData.lon),
                        mapTypeId: google.maps.MapTypeId.TERRAIN
                    };

                    $scope.mymapdetail = new google.maps.Map(document.getElementById('jobMap'), mapOptions);
                    $scope.addMarkers($scope.jobs_by_location);

                },function error(response)
                {
                    console.log(response);
                })

            },function error(response)
            {
                console.log(response);
            });
        }
    }

    $scope.addMarkers = function (jobs)
    {
        var url = "http://maps.google.com/mapfiles/ms/icons/";
        var colors = ["purple","blue","pink","yellow","green"]
        for (i = 0; i < jobs.length; ++i)
        {
            console.log(jobs[i].lat);
            console.log(jobs[i].lon);
            new google.maps.Marker({
            map: $scope.mymapdetail,
            icon: url + colors[i%5] +"-dot.png",
            position: new google.maps.LatLng(jobs[i].lat, jobs[i].lon),
            title: "work location"
            });
        }
    }




    $scope.Likes = function (index)
    {
        // sel.toggle("fa-thumbs-down");
        var jobId = $scope.jobs_by_location[index].job_id;
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
                $scope.new_job_likes = $scope.jobs_by_location[index].job_title + " is added to your favorite list. ";
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
                console.log(response);
                alert("It is deleted");
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

    $scope.items = ['item1', 'item2', 'item3'];
  
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
            return $scope.jobs_by_location[index].job_description;
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