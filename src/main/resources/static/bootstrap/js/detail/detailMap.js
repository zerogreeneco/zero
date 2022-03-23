window.onload = function(){
    var mapAddress = document.getElementById('mapAddress').value;
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

    // 지도를 생성합니다
    var map = new kakao.maps.Map(mapContainer, mapOption);
	// 마우스 휠과 모바일 터치를 이용한 지도 확대, 축소를 막는다
	map.setZoomable(false);
    // 주소-좌표 변환 객체를 생성합니다
    var geocoder = new kakao.maps.services.Geocoder();
        var imageSrc = "/zerogreen/bootstrap/images/map/here.png", // 마커이미지의 주소입니다
            imageSize = new kakao.maps.Size(15, 15), // 마커이미지의 크기입니다
            imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
``
        // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption); // 마커가 표시될 위치입니다
        // 주소로 좌표를 검색합니다
        geocoder.addressSearch(mapAddress, function(result, status) {

        // 정상적으로 검색이 완료됐으면
         if (status === kakao.maps.services.Status.OK) {
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new kakao.maps.Marker({
                image: markerImage,
                position: coords,

            });
            // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            marker.setMap(map);
            map.setCenter(coords);
        }
    });

}
