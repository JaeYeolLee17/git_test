import * as Common from "../commons/common";
import axios from "axios";
import * as String from "../commons/string";

const { kakao } = window;

export const utilGetCamera = (listCamera, cameraId) => {
    let camera = listCamera.filter((element) => element.cameraId === cameraId);
    return camera[0];
};

export const utilGetInstsectionCameras = (listCamera, cameraId) => {
    let cameraData = utilGetCamera(listCamera, cameraId);
    // let cameras = listCamera.filter(
    //     (camera) =>
    //         cameraData.intersection.intersectionId ===
    //         camera.intersection.intersectionId
    // );
    return utilGetInstsectionCamerasByIntersectionId(
        listCamera,
        cameraData.intersection.intersectionId
    );
};

export const utilGetInstsectionCamerasByIntersectionId = (
    listCamera,
    intersectionId
) => {
    let cameras = listCamera.filter(
        (camera) => intersectionId === camera.intersection.intersectionId
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

export const utilFormatDateYYYYMMDD = (date) => {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();

    let format =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (day < 10 ? "0" + day : day);

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

export const utilAxios = () => {
    const instance = axios.create({
        headers: {
            "Content-Type": "application/json",
        },
        withCredentials: true,
    });

    return instance;
};

export const utilAxiosWithAuth = (auth) => {
    let instance = null;
    if (process.env.REACT_APP_NEXT_DEV === "true") {
        instance = utilAxios();
    } else {
        instance = axios.create({
            headers: {
                "Content-Type": "application/json",
                "X-AUTH-TOKEN": `${auth}`,
            },
            withCredentials: true,
        });
    }

    return instance;
};

export const utilIsEmptyObj = (obj) => {
    if (
        obj === null ||
        obj === undefined ||
        (obj.constructor === Object && Object.keys(obj).length === 0)
    ) {
        return true;
    }

    return false;
};

export const utilIsEmptyArray = (array) => {
    if (
        array !== null &&
        array !== undefined &&
        Array.isArray(array) &&
        array.length
    ) {
        return false;
    }

    return true;
};

export const utilConvertQtsrlu15Minute = (qtsrlu) => {
    // 15분단위
    return (qtsrlu * 4) / 3600;
};

export const utilConvertSrlu15Minute = (srlu) => {
    // 100m -> km로 변환 // 15분단위
    return (srlu * 100 * 4) / 1000;
};

export const isPrebuiltIntersection = (intersectionId) => {
    var result = false;

    if (
        intersectionId === "I0001" ||
        intersectionId === "I0002" ||
        intersectionId === "I0003" ||
        intersectionId === "I0004" ||
        intersectionId === "I0005"
    ) {
        result = true;
    }

    return result;
};

const initDataArrayinStatChart = () => {
    return Array.from({ length: 24 }, (v, i) => {
        return { x: i, y: 0 };
    });
};

export const utilConvertChartSeriesCarType = (dataCarType) => {
    let seriesChartSrlu = [];
    let seriesChartQtsrlu = [];
    let seriesChartPerson = [];

    let normal_srluDatas = initDataArrayinStatChart();
    let taxi_srluDatas = initDataArrayinStatChart();
    let bus_srluDatas = initDataArrayinStatChart();
    let truck_srluDatas = initDataArrayinStatChart();
    let motocycle_srluDatas = initDataArrayinStatChart();

    let normal_qtsrluDatas = initDataArrayinStatChart();
    let taxi_qtsrluDatas = initDataArrayinStatChart();
    let bus_qtsrluDatas = initDataArrayinStatChart();
    let truck_qtsrluDatas = initDataArrayinStatChart();
    let motocycle_qtsrluDatas = initDataArrayinStatChart();

    let personDatas = initDataArrayinStatChart();

    // skip if no data
    if (!utilIsEmptyObj(dataCarType)) {
        dataCarType.data.forEach((seriesData) => {
            var normal_srluData = normal_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            normal_srluData.y = seriesData.srlu[0];

            var taxi_srluData = taxi_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            taxi_srluData.y = seriesData.srlu[1];

            var bus_srluData = bus_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            bus_srluData.y = seriesData.srlu[2];

            var truck_srluData = truck_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            truck_srluData.y = seriesData.srlu[3];

            var motocycle_srluData = motocycle_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            motocycle_srluData.y = seriesData.srlu[4];

            var normal_qtsrluData = normal_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            normal_qtsrluData.y = (seriesData.qtsrlu[0] / 3600).toFixed(1);

            var taxi_qtsrluData = taxi_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            taxi_qtsrluData.y = (seriesData.qtsrlu[1] / 3600).toFixed(1);

            var bus_qtsrluData = bus_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            bus_qtsrluData.y = (seriesData.qtsrlu[2] / 3600).toFixed(1);

            var truck_qtsrluData = truck_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            truck_qtsrluData.y = (seriesData.qtsrlu[3] / 3600).toFixed(1);

            var motocycle_qtsrluData = motocycle_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            motocycle_qtsrluData.y = (seriesData.qtsrlu[4] / 3600).toFixed(1);

            var personData = personDatas.find(
                (data) => data.x === seriesData.hour
            );
            personData.y = seriesData.p;
        });
    }

    seriesChartSrlu.push({
        name: String.carType_normal,
        data: normal_srluDatas,
    });
    seriesChartSrlu.push({
        name: String.carType_taxi,
        data: taxi_srluDatas,
    });
    seriesChartSrlu.push({
        name: String.carType_bus,
        data: bus_srluDatas,
    });
    seriesChartSrlu.push({
        name: String.carType_truck,
        data: truck_srluDatas,
    });
    seriesChartSrlu.push({
        name: String.carType_motocycle,
        data: motocycle_srluDatas,
    });

    seriesChartQtsrlu.push({
        name: String.carType_normal,
        data: normal_qtsrluDatas,
    });
    seriesChartQtsrlu.push({
        name: String.carType_taxi,
        data: taxi_qtsrluDatas,
    });
    seriesChartQtsrlu.push({
        name: String.carType_bus,
        data: bus_qtsrluDatas,
    });
    seriesChartQtsrlu.push({
        name: String.carType_truck,
        data: truck_qtsrluDatas,
    });
    seriesChartQtsrlu.push({
        name: String.carType_motocycle,
        data: motocycle_qtsrluDatas,
    });

    seriesChartPerson.push({
        name: String.stats_person,
        data: personDatas,
    });

    return { seriesChartSrlu, seriesChartQtsrlu, seriesChartPerson };
};

export const utilConvertChartSeriesCamera = (dataCamera, listCamera) => {
    let seriesChartSrlu = [];
    let seriesChartQtsrlu = [];
    let seriesChartPerson = [];

    if (!utilIsEmptyObj(dataCamera)) {
        dataCamera.forEach((seriesData) => {
            let name = "";
            if (listCamera !== undefined) {
                let cameraInfo = utilGetCamera(listCamera, seriesData.cameraId);
                name =
                    cameraInfo.direction.intersectionName +
                    String.camera_direction_unit;
            }

            let srluDatas = initDataArrayinStatChart();
            let qtsrluDatas = initDataArrayinStatChart();
            let personDatas = initDataArrayinStatChart();

            seriesData.data.forEach((data) => {
                let hour = data.hour;

                let srluData = srluDatas.find((srlu) => srlu.x === hour);
                srluData.y = data.srluSum;

                let qtsrluData = qtsrluDatas.find(
                    (qtsrlu) => qtsrlu.x === hour
                );
                qtsrluData.y = data.qtsrluSum;

                let personData = personDatas.find(
                    (person) => person.x === hour
                );
                personData.y = data.p;
            });

            seriesChartSrlu.push({
                name: name,
                data: srluDatas,
            });
            seriesChartQtsrlu.push({
                name: name,
                data: qtsrluDatas,
            });
            seriesChartPerson.push({
                name: name,
                data: personDatas,
            });
        });
    }

    return {
        seriesChartSrlu,
        seriesChartQtsrlu,
        seriesChartPerson,
    };
};

export const to2Digit = (number) => {
    return ("0" + number.toFixed(0)).slice(-2);
};

const convertWeekOfYearString = (year, weekOfYear) => {
    let startOfYear = new Date();
    startOfYear.setFullYear(year, 0, 1);
    //startOfYear.setMonth(0, 1);

    if (startOfYear.getDay() === 0)
        startOfYear.setDate(startOfYear.getDate() + 1);
    else if (startOfYear.getDay() === 5)
        startOfYear.setDate(startOfYear.getDate() + 3);
    else if (startOfYear.getDay() === 6)
        startOfYear.setDate(startOfYear.getDate() + 2);
    else if (startOfYear.getDay() === 2)
        startOfYear.setDate(startOfYear.getDate() - 1);
    else if (startOfYear.getDay() === 3)
        startOfYear.setDate(startOfYear.getDate() - 2);
    else if (startOfYear.getDay() === 4)
        startOfYear.setDate(startOfYear.getDate() - 3);

    let startOfWeek = new Date(startOfYear);
    startOfWeek.setDate(startOfWeek.getDate() + (weekOfYear - 1) * 7);

    let endOfWeek = new Date(startOfWeek);
    endOfWeek.setDate(endOfWeek.getDate() + 6);

    let startTime = utilFormatDateYYYYMMDD(startOfWeek);
    let endTime = utilFormatDateYYYYMMDD(endOfWeek);

    return startTime + "~" + endTime;
};

const convertDayOfWeekString = (dayOfWeek) => {
    var convertDayOfWeek = String.stats_sunday;
    switch (dayOfWeek) {
        case 1:
            convertDayOfWeek = String.stats_monday;
            break;
        case 2:
            convertDayOfWeek = String.stats_tuesday;
            break;
        case 3:
            convertDayOfWeek = String.stats_wednesday;
            break;
        case 4:
            convertDayOfWeek = String.stats_thursday;
            break;
        case 5:
            convertDayOfWeek = String.stats_friday;
            break;
        case 6:
            convertDayOfWeek = String.stats_saturday;
            break;
        case 7:
            convertDayOfWeek = String.stats_sunday;
            break;

        default:
            break;
    }

    return convertDayOfWeek;
};

const getNameFromSeriesData = (seriesData) => {
    if (
        seriesData.year !== 0 &&
        seriesData.month !== 0 &&
        seriesData.day !== 0
    ) {
        return (
            "20" +
            to2Digit(seriesData.year) +
            "-" +
            to2Digit(seriesData.month) +
            "-" +
            to2Digit(seriesData.day)
        );
    } else if (
        seriesData.year !== 0 &&
        seriesData.month === 0 &&
        seriesData.day === 0 &&
        seriesData.weekOfYear !== 0.0
    ) {
        return convertWeekOfYearString(seriesData.year, seriesData.weekOfYear);
    } else if (
        seriesData.year !== 0 &&
        seriesData.month !== 0 &&
        seriesData.day === 0 &&
        seriesData.dayOfWeek === 0.0 &&
        seriesData.weekOfYear === 0.0
    ) {
        return (
            "20" + to2Digit(seriesData.year) + "-" + to2Digit(seriesData.month)
        );
    } else if (
        seriesData.year === 0 &&
        seriesData.month === 0 &&
        seriesData.day === 0
    ) {
        return convertDayOfWeekString(seriesData.dayOfWeek);
    }

    return "";
};

export const utilConvertChartSeriesPeriod = (dataStat) => {
    let seriesSrluDatas = [];
    let seriesQtsrluDatas = [];
    let seriesSpeedDatas = [];
    let seriesPersonDatas = [];
    let seriesMfdDatas = [];

    let maxQtsrluData = 0;

    if (!utilIsEmptyObj(dataStat)) {
        dataStat.forEach((seriesData) => {
            let name = getNameFromSeriesData(seriesData);

            let srluDatas = initDataArrayinStatChart();
            let qtsrluDatas = initDataArrayinStatChart();
            let speedDatas = initDataArrayinStatChart();
            let personDatas = initDataArrayinStatChart();
            let mfdDatas = initDataArrayinStatChart();

            seriesData.data.forEach((data) => {
                let qtsrlu = data.qtsrlu / 3600; // 1시간 단위
                let srlu = (data.srlu * 100) / 1000; // 100m -> km로 변환 // 1시간단위

                //console.log("updateStatPeriod : " + JSON.stringify(seriesData.data[j]));

                let hour = data.hour;

                let srluData = srluDatas.find((srlu) => srlu.x === hour);
                srluData.y = data.srlu;

                let qtsrluData = qtsrluDatas.find(
                    (qtsrlu) => qtsrlu.x === hour
                );
                qtsrluData.y = data.qtsrlu;

                let speedData = speedDatas.find((speed) => speed.x === hour);
                speedData.y = srlu / qtsrlu;

                let personData = personDatas.find(
                    (person) => person.x === hour
                );
                personData.y = data.p;

                mfdDatas[hour].x = qtsrlu;
                mfdDatas[hour].y = srlu;

                if (qtsrlu > maxQtsrluData) maxQtsrluData = qtsrlu;
            });

            seriesSrluDatas.push({
                name: name,
                data: srluDatas,
            });
            seriesQtsrluDatas.push({
                name: name,
                data: qtsrluDatas,
            });
            seriesSpeedDatas.push({
                name: name,
                data: speedDatas,
            });
            seriesPersonDatas.push({
                name: name,
                data: personDatas,
            });
            seriesMfdDatas.push({
                name: name,
                data: mfdDatas,
            });
        });
    }

    return {
        seriesSrluDatas,
        seriesQtsrluDatas,
        seriesSpeedDatas,
        seriesPersonDatas,
        seriesMfdDatas,
        maxQtsrluData,
    };
};
