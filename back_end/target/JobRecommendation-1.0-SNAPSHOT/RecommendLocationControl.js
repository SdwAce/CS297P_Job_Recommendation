var app = angular.module("RecommendationByLocation",["ngStorage",'ngAnimate', 'ngSanitize', 'ui.bootstrap']);

app.controller("RecommendLocationController",function($scope,$http,$localStorage,$uibModal, $log, $document)
{


    $scope.name = $localStorage.name;
    $scope.getLocation = function ($localStorage,$sessionStorage)
    {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function success(response)
            {     
                var locationData = {
                    "lon": response.coords.longitude.toString(),
                    "lat": response.coords.latitude.toString(),
                    "user_id": $scope.name
                }


                $http({method:'Post',url:"http://localhost:8080/Job_Recommendation/searchnearby",data:locationData})
                .then(function success(response)
                {
                    var jobs = Object.values(response.data);

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

                    var mapOptions = {
                        zoom: 4,
                        center: new google.maps.LatLng(locationData.lat,locationData.lon),
                        mapTypeId: google.maps.MapTypeId.TERRAIN
                    };

                    

                    $scope.mymapdetail = new google.maps.Map(document.getElementById('jobMap'), mapOptions);
                    var url = "http://maps.google.com/mapfiles/ms/icons/";
                    var marker = new google.maps.Marker({
                        map: $scope.mymapdetail,
                        icon: url + "yellow-dot.png",
                        position: new google.maps.LatLng(parseFloat(locationData.lat), parseFloat(locationData.lon)),
                        title: "Home"
                        });
            
                        marker.description = new google.maps.InfoWindow({
                            content: "Current Location"
                        });
            
                        google.maps.event.addListener(marker, 'click', function(){
                            this.description.setPosition(this.getPosition());
                            this.description.open($scope.mymapdetail); //map to display on
                          });
                    
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
        for (i = 0; i < jobs.length; ++i)
        {
            var marker = new google.maps.Marker({
            map: $scope.mymapdetail,
            icon: url + "green-dot.png",
            position: new google.maps.LatLng(jobs[i].lat, jobs[i].lon),
            title: "work location"
            });

            marker.description = new google.maps.InfoWindow({
                content: "Job Title: " + jobs[i].job_title + "\nCompany: " + jobs[i].company
            });

            google.maps.event.addListener(marker, 'click', function(){
                this.description.setPosition(this.getPosition());
                this.description.open($scope.mymapdetail); //map to display on
              });
        }
    }




    $scope.Likes = function (index)
    {
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
    };

})


app.controller('ModalInstanceCtrl', function ($uibModalInstance, items, $scope) {

    $scope.items = items;

  
    $scope.ok = function () {
      $uibModalInstance.close($scope.selected.item);
    };
    console.log(789);
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  });