var claraApp = angular.module('claraApp', [ 'ngRoute', 'angular-table',
		'angular-tabs' ]);

var indirizzoUpload = "https://clara.io/api/scenes/";
var appendiceUpload = "/import?async=0";
var indirizzoEmbed = "https://clara.io/embed/";
var appendiceEmbed = "?renderer=webgl&timeline=false&autoplay=false&tools=false&header=false";

claraApp.run(function($rootScope, $location, $http) {

});

// Controller principale e inject Angular's $scope
claraApp.controller('mainController', function($scope, $http, $location) {
	
	$scope.idScena = 'non ancora caricato';
	
	$scope.add = function() {
	    var url = "http://test.services.ltc-logistics.it/clara/rest/scena";
		
		var f = document.getElementById('file').files[0],
	        r = new FileReader();
		
		var aggiornaID = function(data) {
			$scope.idScena = data._id;
			sessionStorage.setItem('id', JSON.stringify($scope.idScena));
			setTimeout(function(){
				var htmlElementEmbed = document.getElementById('embed');
				htmlElementEmbed.src = indirizzoEmbed + $scope.idScena + appendiceEmbed;
				console.log('cambiato!');
			}, 5000);
		};

	    r.onloadend = function(e) {
	      var data = e.target.result;
	      console.log('file caricato: ');
	      console.log(data);
	      var obj = { contenuto : data };
	      //send your binary data via $http or $resource or do anything else with it
	      $http.post(url, obj, getHTTPHeaderConfig()).then(function(response) {
	    	  console.log(response);
	    	  aggiornaID(response.data);
	      }, function(response) {
	    	  console.log(response);
	      });
	    };

	    r.readAsBinaryString(f);
	};
	
	$scope.calcolaVolume = function getVolume(geometry) {
	    //Check elemento
		if (!geometry.isBufferGeometry) {
			console.log("'geometry' must be an indexed or non-indexed buffer geometry");
	        return 0;
	    }
	    var isIndexed = geometry.index !== null;
	    let position = geometry.attributes.position;
	    let sum = 0;
	    let p1 = new THREE.Vector3(), p2 = new THREE.Vector3(), p3 = new THREE.Vector3();
	    if (!isIndexed) {
	    	let faces = position.count / 3;
	        for (let i = 0; i < faces; i++) {
	        	p1.fromBufferAttribute(position, i * 3 + 0);
	        	p2.fromBufferAttribute(position, i * 3 + 1);
	        	p3.fromBufferAttribute(position, i * 3 + 2);
	        	sum += signedVolumeOfTriangle(p1, p2, p3);
	        }
	    } else {
	        let index = geometry.index;
	        let faces = index.count / 3;
	        for (let i = 0; i < faces; i++) {
	        	p1.fromBufferAttribute(position, index.array[i * 3 + 0]);
	        	p2.fromBufferAttribute(position, index.array[i * 3 + 1]);
	        	p3.fromBufferAttribute(position, index.array[i * 3 + 2]);
	        	sum += signedVolumeOfTriangle(p1, p2, p3);
	        }
	    }
	    return sum;
	};
	
	function signedVolumeOfTriangle(p1, p2, p3) {
	    return p1.dot(p2.cross(p3)) / 6.0;
	};

});