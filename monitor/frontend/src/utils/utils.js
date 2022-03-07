import * as Common from "../common";

const { kakao } = window;

export const utilGetCamera = (listCamera, cameraId) => {
    let camera = listCamera.filter((element) => element.cameraId === cameraId);
    return camera[0];
};

export const utilGetInstsectionCameras = (listCamera, cameraId) => {
    let cameraData = utilGetCamera(listCamera, cameraId);
    let cameras = listCamera.filter(
        (camera) =>
            cameraData.intersection.intersectionId ===
            camera.intersection.intersectionId
    );
    return cameras;
};

export const utilIsPremakeIntersection = (intersectionId) => {
    if (
        intersectionId === "I0001" ||
        intersectionId === "I0002" ||
        intersectionId === "I0003" ||
        intersectionId === "I0004" ||
        intersectionId === "I0005"
    ) {
        return true;
    }

    return false;
};

export const utilFormatDateYYYYMMDD000000 = (date) => {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let format =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (day < 10 ? "0" + day : day) +
        " 00:00:00";

    return format;
};

export const utilFormatDateYYYYMMDDHHmm00 = (date) => {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();

    let format =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (day < 10 ? "0" + day : day) +
        " " +
        (hour < 10 ? "0" + hour : hour) +
        ":" +
        (minute < 10 ? "0" + minute : minute) +
        ":00";

    return format;
};

export const utilLeadingZeros = (n, digits) => {
    var zero = "";
    n = n.toString();

    if (n.length < digits) {
        for (var i = 0; i < digits - n.length; i++) zero += "0";
    }
    return zero + n;
};

export const utilGetSpeedColor = (speed) => {
    if (speed >= Common.trafficSpeedNormal) {
        return Common.trafficColorNormal;
    } else if (speed < Common.trafficSpeedBusy) {
        return Common.trafficColorBusy;
    } else {
        return Common.trafficColorSlowly;
    }
};

export const utilConvertParallelLines = (map, level, list) => {
    let path = [];
    if (!map) return null;

    let gap = 0;
    if (level === 1) {
        gap = 20;
    } else if (level === 2) {
        gap = 15;
    } else if (level === 3) {
        gap = 10;
    } else if (level === 4) {
        gap = 5;
    } else if (level === 5) {
        gap = 3;
    }

    var mapProjection = map.getProjection();

    let originalPath = list.map((gps) => {
        return new kakao.maps.LatLng(gps.lat, gps.lng);
    });

    originalPath.map((kakaoGps, index, array) => {
        //console.log("index", index);
        //console.log("array", array[0]);
        if (index === 0) return null;

        var p1 = mapProjection.pointFromCoords(array[index - 1]);
        var p2 = mapProjection.pointFromCoords(array[index]);
        var theta = Math.atan2(p1.x - p2.x, p1.y - p2.y) + Math.PI / 2;

        // // 방향 바로잡기
        if (theta > Math.PI) theta -= Math.PI * 2;

        // // 간격 계산
        var dx = Math.round(gap * Math.sin(theta));
        var dy = Math.round(gap * Math.cos(theta));

        // // 간격 적용된 left right point
        var p1l = new kakao.maps.Point(p1.x - dx, p1.y - dy);
        var p2l = new kakao.maps.Point(p2.x - dx, p2.y - dy);

        if (index === 1) {
            let position = mapProjection.coordsFromPoint(p1l);
            path.push({ lat: position.getLat(), lng: position.getLng() });
        }
        let position = mapProjection.coordsFromPoint(p2l);
        path.push({ lat: position.getLat(), lng: position.getLng() });

        return "";
    });

    if (level <= 2 && path.length > 1) {
        var i = path.length - 1;

        var temp1 = new kakao.maps.LatLng(path[i - 1].lat, path[i - 1].lng);
        var temp2 = new kakao.maps.LatLng(path[i].lat, path[i].lng);

        var p1 = mapProjection.pointFromCoords(temp1);
        var p2 = mapProjection.pointFromCoords(temp2);
        var theta = Math.atan2(p1.x - p2.x, p1.y - p2.y) + Math.PI / 2;

        // 방향 바로잡기
        if (theta > Math.PI) theta -= Math.PI * 2;

        // 간격 계산
        var dx = Math.round(10 * Math.sin(theta));
        var dy = Math.round(10 * Math.cos(theta));

        // 간격 적용된 left right point
        var p2l = new kakao.maps.Point(p2.x - dx, p2.y - dy);

        let position = mapProjection.coordsFromPoint(p2l);
        path.push({ lat: position.getLat(), lng: position.getLng() });
    }

    return path;
};
