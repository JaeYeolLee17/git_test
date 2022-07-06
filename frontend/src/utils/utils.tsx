import * as Common from "../commons/common";
import axios from "axios";
import * as String from "../commons/string";

//const { kakao } = window;

export const utilGetCamera = (listCamera: any[], cameraId: string) => {
    const camera: any[] = listCamera.filter(
        (element) => element.cameraId === cameraId
    );
    return camera[0];
};

export const utilGetInstsectionCameras = (
    listCamera: any[],
    cameraId: string
) => {
    const cameraData: any = utilGetCamera(listCamera, cameraId);
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
    listCamera: any[],
    intersectionId: string
) => {
    const cameras: any[] = listCamera.filter(
        (camera) => intersectionId === camera.intersection.intersectionId
    );
    return cameras;
};

export const utilIsPremakeIntersection = (intersectionId: string) => {
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

export const utilFormatDateYYYYMMDD000000 = (date: Date) => {
    const year: number = date.getFullYear();
    const month: number = date.getMonth() + 1;
    const day: number = date.getDate();
    const format: string =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (day < 10 ? "0" + day : day) +
        " 00:00:00";

    return format;
};

export const utilFormatDateYYYYMMDDHHmm00 = (date: Date) => {
    const year: number = date.getFullYear();
    const month: number = date.getMonth() + 1;
    const day: number = date.getDate();
    const hour: number = date.getHours();
    const minute: number = date.getMinutes();

    const format: string =
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

export const utilFormatDateYYYYMMDD = (date: Date) => {
    const year: number = date.getFullYear();
    const month: number = date.getMonth() + 1;
    const day: number = date.getDate();

    const format: string =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (day < 10 ? "0" + day : day);

    return format;
};

export const utilLeadingZeros = (n: number, digits: number) => {
    let zero = "";
    const nString = n.toString();

    if (nString.length < digits) {
        for (let i = 0; i < digits - nString.length; i++) zero += "0";
    }
    return zero + nString;
};

export const utilGetSpeedColor = (speed: number) => {
    if (speed >= Common.trafficSpeedNormal) {
        return Common.trafficColorNormal;
    } else if (speed < Common.trafficSpeedBusy) {
        return Common.trafficColorBusy;
    } else {
        return Common.trafficColorSlowly;
    }
};

export const utilConvertParallelLines = (
    map: kakao.maps.Map,
    level: number,
    list: Common.GpsPosition[]
) => {
    const path: Common.GpsPosition[] = [];
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

    const mapProjection = map.getProjection();

    const originalPath = list.map((gps) => {
        return new kakao.maps.LatLng(gps.lat, gps.lng);
    });

    originalPath.map((kakaoGps, index: number, array: kakao.maps.LatLng[]) => {
        //console.log("index", index);
        //console.log("array", array[0]);
        if (index === 0) return null;

        const p1: kakao.maps.Point = mapProjection.pointFromCoords(
            array[index - 1]
        );
        const p2: kakao.maps.Point = mapProjection.pointFromCoords(
            array[index]
        );
        let theta: number = Math.atan2(p1.x - p2.x, p1.y - p2.y) + Math.PI / 2;

        // // 방향 바로잡기
        if (theta > Math.PI) theta -= Math.PI * 2;

        // // 간격 계산
        const dx: number = Math.round(gap * Math.sin(theta));
        const dy: number = Math.round(gap * Math.cos(theta));

        // // 간격 적용된 left right point
        const p1l: kakao.maps.Point = new kakao.maps.Point(
            p1.x - dx,
            p1.y - dy
        );
        const p2l: kakao.maps.Point = new kakao.maps.Point(
            p2.x - dx,
            p2.y - dy
        );

        if (index === 1) {
            const position1 = mapProjection.coordsFromPoint(p1l);
            path.push({ lat: position1.getLat(), lng: position1.getLng() });
        }
        const position2 = mapProjection.coordsFromPoint(p2l);
        path.push({ lat: position2.getLat(), lng: position2.getLng() });

        return "";
    });

    if (level <= 2 && path.length > 1) {
        const i = path.length - 1;

        const temp1 = new kakao.maps.LatLng(path[i - 1].lat, path[i - 1].lng);
        const temp2 = new kakao.maps.LatLng(path[i].lat, path[i].lng);

        const p1 = mapProjection.pointFromCoords(temp1);
        const p2 = mapProjection.pointFromCoords(temp2);
        let theta = Math.atan2(p1.x - p2.x, p1.y - p2.y) + Math.PI / 2;

        // 방향 바로잡기
        if (theta > Math.PI) theta -= Math.PI * 2;

        // 간격 계산
        const dx = Math.round(10 * Math.sin(theta));
        const dy = Math.round(10 * Math.cos(theta));

        // 간격 적용된 left right point
        const p2l = new kakao.maps.Point(p2.x - dx, p2.y - dy);

        const position = mapProjection.coordsFromPoint(p2l);
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

export const utilAxiosWithAuth = (auth: string) => {
    let instance = null;
    if (process.env.REACT_APP_NEXT_DEV === true) {
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

export const utilIsEmptyObj = (obj: Record<string, any>) => {
    // if (
    //     obj === null ||
    //     obj === undefined ||
    //     (obj.constructor === Object && Object.keys(obj).length === 0)
    // ) {
    //     return true;
    // }

    // return false;

    for (const _key in obj) {
        return false;
    }
    return true;
};

export const utilIsEmptyArray = (array: Array<any>) => {
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

export const utilConvertQtsrlu15Minute = (qtsrlu: number) => {
    // 15분단위
    return (qtsrlu * 4) / 3600;
};

export const utilConvertSrlu15Minute = (srlu: number) => {
    // 100m -> km로 변환 // 15분단위
    return (srlu * 100 * 4) / 1000;
};

export const isPrebuiltIntersection = (intersectionId: string) => {
    let result = false;

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

export const utilConvertChartSeriesCarType = (dataCarType: any) => {
    const seriesChartSrlu: Common.ChartData[] = [];
    const seriesChartQtsrlu: Common.ChartData[] = [];
    const seriesChartPerson: Common.ChartData[] = [];

    const normal_srluDatas: Common.ChartElement[] = initDataArrayinStatChart();
    const taxi_srluDatas: Common.ChartElement[] = initDataArrayinStatChart();
    const bus_srluDatas: Common.ChartElement[] = initDataArrayinStatChart();
    const truck_srluDatas: Common.ChartElement[] = initDataArrayinStatChart();
    const motocycle_srluDatas: Common.ChartElement[] =
        initDataArrayinStatChart();

    const normal_qtsrluDatas: Common.ChartElement[] =
        initDataArrayinStatChart();
    const taxi_qtsrluDatas: Common.ChartElement[] = initDataArrayinStatChart();
    const bus_qtsrluDatas: Common.ChartElement[] = initDataArrayinStatChart();
    const truck_qtsrluDatas: Common.ChartElement[] = initDataArrayinStatChart();
    const motocycle_qtsrluDatas: Common.ChartElement[] =
        initDataArrayinStatChart();

    const personDatas: Common.ChartElement[] = initDataArrayinStatChart();

    // skip if no data
    if (!utilIsEmptyObj(dataCarType)) {
        dataCarType.data.forEach((seriesData: any) => {
            const normal_srluData = normal_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (normal_srluData !== undefined)
                normal_srluData.y = seriesData.srlu[0];

            const taxi_srluData = taxi_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (taxi_srluData !== undefined)
                taxi_srluData.y = seriesData.srlu[1];

            const bus_srluData = bus_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (bus_srluData !== undefined) bus_srluData.y = seriesData.srlu[2];

            const truck_srluData = truck_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (truck_srluData !== undefined)
                truck_srluData.y = seriesData.srlu[3];

            const motocycle_srluData = motocycle_srluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (motocycle_srluData !== undefined)
                motocycle_srluData.y = seriesData.srlu[4];

            const normal_qtsrluData = normal_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (normal_qtsrluData !== undefined)
                normal_qtsrluData.y = (seriesData.qtsrlu[0] / 3600).toFixed(1);

            const taxi_qtsrluData = taxi_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (taxi_qtsrluData !== undefined)
                taxi_qtsrluData.y = (seriesData.qtsrlu[1] / 3600).toFixed(1);

            const bus_qtsrluData = bus_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (bus_qtsrluData !== undefined)
                bus_qtsrluData.y = (seriesData.qtsrlu[2] / 3600).toFixed(1);

            const truck_qtsrluData = truck_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (truck_qtsrluData !== undefined)
                truck_qtsrluData.y = (seriesData.qtsrlu[3] / 3600).toFixed(1);

            const motocycle_qtsrluData = motocycle_qtsrluDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (motocycle_qtsrluData !== undefined)
                motocycle_qtsrluData.y = (seriesData.qtsrlu[4] / 3600).toFixed(
                    1
                );

            const personData = personDatas.find(
                (data) => data.x === seriesData.hour
            );
            if (personData !== undefined) personData.y = seriesData.p;
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

export const utilConvertChartSeriesCamera = (
    dataCamera: any,
    listCamera: any
) => {
    const seriesChartSrlu: Common.ChartData[] = [];
    const seriesChartQtsrlu: Common.ChartData[] = [];
    const seriesChartPerson: Common.ChartData[] = [];

    if (!utilIsEmptyObj(dataCamera)) {
        dataCamera.forEach((seriesData: any) => {
            let name = "";
            if (listCamera !== undefined) {
                const cameraInfo = utilGetCamera(
                    listCamera,
                    seriesData.cameraId
                );
                name =
                    cameraInfo.direction.intersectionName +
                    String.camera_direction_unit;
            }

            const srluDatas: Common.ChartElement[] = initDataArrayinStatChart();
            const qtsrluDatas: Common.ChartElement[] =
                initDataArrayinStatChart();
            const personDatas: Common.ChartElement[] =
                initDataArrayinStatChart();

            seriesData.data.forEach((data: any) => {
                const hour = data.hour;

                const srluData = srluDatas.find((srlu) => srlu.x === hour);
                if (srluData !== undefined) srluData.y = data.srluSum;

                const qtsrluData = qtsrluDatas.find(
                    (qtsrlu) => qtsrlu.x === hour
                );
                if (qtsrluData !== undefined) qtsrluData.y = data.qtsrluSum;

                const personData = personDatas.find(
                    (person) => person.x === hour
                );
                if (personData !== undefined) personData.y = data.p;
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

export const to2Digit = (number: number) => {
    return ("0" + number.toFixed(0)).slice(-2);
};

const convertWeekOfYearString = (year: number, weekOfYear: number) => {
    const startOfYear: Date = new Date();
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

    const startOfWeek: Date = new Date(startOfYear);
    startOfWeek.setDate(startOfWeek.getDate() + (weekOfYear - 1) * 7);

    const endOfWeek: Date = new Date(startOfWeek);
    endOfWeek.setDate(endOfWeek.getDate() + 6);

    const startTime: string = utilFormatDateYYYYMMDD(startOfWeek);
    const endTime: string = utilFormatDateYYYYMMDD(endOfWeek);

    return startTime + "~" + endTime;
};

const convertDayOfWeekString = (dayOfWeek: number) => {
    let convertDayOfWeek = String.stats_sunday;
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

const getNameFromSeriesData = (seriesData: any) => {
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

export const utilConvertChartSeriesPeriod = (dataStat: any) => {
    const seriesSrluDatas: Common.ChartData[] = [];
    const seriesQtsrluDatas: Common.ChartData[] = [];
    const seriesSpeedDatas: Common.ChartData[] = [];
    const seriesPersonDatas: Common.ChartData[] = [];
    const seriesMfdDatas: Common.ChartData[] = [];

    let maxQtsrluData = 0;

    if (!utilIsEmptyObj(dataStat)) {
        dataStat.forEach((seriesData: any) => {
            const name = getNameFromSeriesData(seriesData);

            const srluDatas: Common.ChartElement[] = initDataArrayinStatChart();
            const qtsrluDatas: Common.ChartElement[] =
                initDataArrayinStatChart();
            const speedDatas: Common.ChartElement[] =
                initDataArrayinStatChart();
            const personDatas: Common.ChartElement[] =
                initDataArrayinStatChart();
            const mfdDatas: Common.ChartElement[] = initDataArrayinStatChart();

            seriesData.data.forEach((data: any) => {
                const qtsrlu = data.qtsrlu / 3600; // 1시간 단위
                const srlu = (data.srlu * 100) / 1000; // 100m -> km로 변환 // 1시간단위

                //console.log("updateStatPeriod : " + JSON.stringify(seriesData.data[j]));

                const hour = data.hour;

                const srluData = srluDatas.find((srlu) => srlu.x === hour);
                if (srluData !== undefined) srluData.y = data.srlu;

                const qtsrluData = qtsrluDatas.find(
                    (qtsrlu) => qtsrlu.x === hour
                );
                if (qtsrluData !== undefined) qtsrluData.y = data.qtsrlu;

                const speedData = speedDatas.find((speed) => speed.x === hour);
                if (speedData !== undefined) speedData.y = srlu / qtsrlu;

                const personData = personDatas.find(
                    (person) => person.x === hour
                );
                if (personData !== undefined) personData.y = data.p;

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

export const getCurrentDateTime = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    const date = now.getDate();
    const hour = now.getHours();
    const minutes = now.getMinutes();
    const seconds = now.getSeconds();

    const today =
        year +
        "-" +
        (month < 10 ? "0" + month : month) +
        "-" +
        (date < 10 ? "0" + date : date);
    const time =
        (hour < 12 ? "오전" : "오후") +
        " " +
        (hour < 10 ? ((hour + 11) % 12) + 1 : ((hour + 11) % 12) + 1) +
        ":" +
        (minutes < 10 ? "0" + minutes : minutes) +
        ":" +
        (seconds < 10 ? "0" + seconds : seconds);

    return { date: today, time: time };
};

import imgCamera_0_n from "../assets/images/btn_map_cctv_40_0_n.svg";
import imgCamera_0_f from "../assets/images/btn_map_cctv_40_0_f.svg";
import imgCamera_1_n from "../assets/images/btn_map_cctv_40_1_n.svg";
import imgCamera_1_f from "../assets/images/btn_map_cctv_40_1_f.svg";
import imgCamera_2_n from "../assets/images/btn_map_cctv_40_2_n.svg";
import imgCamera_2_f from "../assets/images/btn_map_cctv_40_2_f.svg";
import imgCamera_3_n from "../assets/images/btn_map_cctv_40_3_n.svg";
import imgCamera_3_f from "../assets/images/btn_map_cctv_40_3_f.svg";
import imgCamera_4_n from "../assets/images/btn_map_cctv_40_4_n.svg";
import imgCamera_4_f from "../assets/images/btn_map_cctv_40_4_f.svg";
import imgCamera_5_n from "../assets/images/btn_map_cctv_40_5_n.svg";
import imgCamera_5_f from "../assets/images/btn_map_cctv_40_5_f.svg";
import imgCamera_6_n from "../assets/images/btn_map_cctv_40_6_n.svg";
import imgCamera_6_f from "../assets/images/btn_map_cctv_40_6_f.svg";
import imgCamera_7_n from "../assets/images/btn_map_cctv_40_7_n.svg";
import imgCamera_7_f from "../assets/images/btn_map_cctv_40_7_f.svg";
import imgCamera_8_n from "../assets/images/btn_map_cctv_40_8_n.svg";
import imgCamera_8_f from "../assets/images/btn_map_cctv_40_8_f.svg";
import imgCamera_9_n from "../assets/images/btn_map_cctv_40_9_n.svg";
import imgCamera_9_f from "../assets/images/btn_map_cctv_40_9_f.svg";
import imgCamera_10_f from "../assets/images/btn_map_cctv_40_10_f.svg";
import imgCamera_10_n from "../assets/images/btn_map_cctv_40_10_n.svg";
import imgCamera_11_n from "../assets/images/btn_map_cctv_40_11_n.svg";
import imgCamera_11_f from "../assets/images/btn_map_cctv_40_11_f.svg";
import imgCamera_12_n from "../assets/images/btn_map_cctv_40_12_n.svg";
import imgCamera_12_f from "../assets/images/btn_map_cctv_40_12_f.svg";
import imgCamera_13_n from "../assets/images/btn_map_cctv_40_13_n.svg";
import imgCamera_13_f from "../assets/images/btn_map_cctv_40_13_f.svg";
import imgCamera_14_n from "../assets/images/btn_map_cctv_40_14_n.svg";
import imgCamera_14_f from "../assets/images/btn_map_cctv_40_14_f.svg";
import imgCamera_15_n from "../assets/images/btn_map_cctv_40_15_n.svg";
import imgCamera_15_f from "../assets/images/btn_map_cctv_40_15_f.svg";
import imgCamera_16_n from "../assets/images/btn_map_cctv_40_16_n.svg";
import imgCamera_16_f from "../assets/images/btn_map_cctv_40_16_f.svg";
import imgCamera_17_n from "../assets/images/btn_map_cctv_40_17_n.svg";
import imgCamera_17_f from "../assets/images/btn_map_cctv_40_17_f.svg";
import imgCamera_18_n from "../assets/images/btn_map_cctv_40_18_n.svg";
import imgCamera_18_f from "../assets/images/btn_map_cctv_40_18_f.svg";
import imgCamera_19_n from "../assets/images/btn_map_cctv_40_19_n.svg";
import imgCamera_19_f from "../assets/images/btn_map_cctv_40_19_f.svg";
import imgCamera_20_n from "../assets/images/btn_map_cctv_40_20_n.svg";
import imgCamera_20_f from "../assets/images/btn_map_cctv_40_20_f.svg";
import imgCamera_21_n from "../assets/images/btn_map_cctv_40_21_n.svg";
import imgCamera_21_f from "../assets/images/btn_map_cctv_40_21_f.svg";
import imgCamera_22_n from "../assets/images/btn_map_cctv_40_22_n.svg";
import imgCamera_22_f from "../assets/images/btn_map_cctv_40_22_f.svg";
import imgCamera_23_n from "../assets/images/btn_map_cctv_40_23_n.svg";
import imgCamera_23_f from "../assets/images/btn_map_cctv_40_23_f.svg";
import imgCamera_24_n from "../assets/images/btn_map_cctv_40_24_n.svg";
import imgCamera_24_f from "../assets/images/btn_map_cctv_40_24_f.svg";
import imgCamera_25_n from "../assets/images/btn_map_cctv_40_25_n.svg";
import imgCamera_25_f from "../assets/images/btn_map_cctv_40_25_f.svg";
import imgCamera_26_n from "../assets/images/btn_map_cctv_40_26_n.svg";
import imgCamera_26_f from "../assets/images/btn_map_cctv_40_26_f.svg";
import imgCamera_27_n from "../assets/images/btn_map_cctv_40_27_n.svg";
import imgCamera_27_f from "../assets/images/btn_map_cctv_40_27_f.svg";
import imgCamera_28_n from "../assets/images/btn_map_cctv_40_28_n.svg";
import imgCamera_28_f from "../assets/images/btn_map_cctv_40_28_f.svg";
import imgCamera_29_n from "../assets/images/btn_map_cctv_40_29_n.svg";
import imgCamera_29_f from "../assets/images/btn_map_cctv_40_29_f.svg";
import imgCamera_30_n from "../assets/images/btn_map_cctv_40_30_n.svg";
import imgCamera_30_f from "../assets/images/btn_map_cctv_40_30_f.svg";
import imgCamera_31_n from "../assets/images/btn_map_cctv_40_31_n.svg";
import imgCamera_31_f from "../assets/images/btn_map_cctv_40_31_f.svg";
import imgCamera_32_n from "../assets/images/btn_map_cctv_40_32_n.svg";
import imgCamera_32_f from "../assets/images/btn_map_cctv_40_32_f.svg";
import imgCamera_33_n from "../assets/images/btn_map_cctv_40_33_n.svg";
import imgCamera_33_f from "../assets/images/btn_map_cctv_40_33_f.svg";
import imgCamera_34_n from "../assets/images/btn_map_cctv_40_34_n.svg";
import imgCamera_34_f from "../assets/images/btn_map_cctv_40_34_f.svg";
import imgCamera_35_n from "../assets/images/btn_map_cctv_40_35_n.svg";
import imgCamera_35_f from "../assets/images/btn_map_cctv_40_35_f.svg";

import imgCamera_e_0_n from "../assets/images/btn_map_cctv_e_40_0_n.svg";
import imgCamera_e_0_f from "../assets/images/btn_map_cctv_e_40_0_f.svg";
import imgCamera_e_1_n from "../assets/images/btn_map_cctv_e_40_1_n.svg";
import imgCamera_e_1_f from "../assets/images/btn_map_cctv_e_40_1_f.svg";
import imgCamera_e_2_n from "../assets/images/btn_map_cctv_e_40_2_n.svg";
import imgCamera_e_2_f from "../assets/images/btn_map_cctv_e_40_2_f.svg";
import imgCamera_e_3_n from "../assets/images/btn_map_cctv_e_40_3_n.svg";
import imgCamera_e_3_f from "../assets/images/btn_map_cctv_e_40_3_f.svg";
import imgCamera_e_4_n from "../assets/images/btn_map_cctv_e_40_4_n.svg";
import imgCamera_e_4_f from "../assets/images/btn_map_cctv_e_40_4_f.svg";
import imgCamera_e_5_n from "../assets/images/btn_map_cctv_e_40_5_n.svg";
import imgCamera_e_5_f from "../assets/images/btn_map_cctv_e_40_5_f.svg";
import imgCamera_e_6_n from "../assets/images/btn_map_cctv_e_40_6_n.svg";
import imgCamera_e_6_f from "../assets/images/btn_map_cctv_e_40_6_f.svg";
import imgCamera_e_7_n from "../assets/images/btn_map_cctv_e_40_7_n.svg";
import imgCamera_e_7_f from "../assets/images/btn_map_cctv_e_40_7_f.svg";
import imgCamera_e_8_n from "../assets/images/btn_map_cctv_e_40_8_n.svg";
import imgCamera_e_8_f from "../assets/images/btn_map_cctv_e_40_8_f.svg";
import imgCamera_e_9_n from "../assets/images/btn_map_cctv_e_40_9_n.svg";
import imgCamera_e_9_f from "../assets/images/btn_map_cctv_e_40_9_f.svg";
import imgCamera_e_10_f from "../assets/images/btn_map_cctv_e_40_10_f.svg";
import imgCamera_e_10_n from "../assets/images/btn_map_cctv_e_40_10_n.svg";
import imgCamera_e_11_n from "../assets/images/btn_map_cctv_e_40_11_n.svg";
import imgCamera_e_11_f from "../assets/images/btn_map_cctv_e_40_11_f.svg";
import imgCamera_e_12_n from "../assets/images/btn_map_cctv_e_40_12_n.svg";
import imgCamera_e_12_f from "../assets/images/btn_map_cctv_e_40_12_f.svg";
import imgCamera_e_13_n from "../assets/images/btn_map_cctv_e_40_13_n.svg";
import imgCamera_e_13_f from "../assets/images/btn_map_cctv_e_40_13_f.svg";
import imgCamera_e_14_n from "../assets/images/btn_map_cctv_e_40_14_n.svg";
import imgCamera_e_14_f from "../assets/images/btn_map_cctv_e_40_14_f.svg";
import imgCamera_e_15_n from "../assets/images/btn_map_cctv_e_40_15_n.svg";
import imgCamera_e_15_f from "../assets/images/btn_map_cctv_e_40_15_f.svg";
import imgCamera_e_16_n from "../assets/images/btn_map_cctv_e_40_16_n.svg";
import imgCamera_e_16_f from "../assets/images/btn_map_cctv_e_40_16_f.svg";
import imgCamera_e_17_n from "../assets/images/btn_map_cctv_e_40_17_n.svg";
import imgCamera_e_17_f from "../assets/images/btn_map_cctv_e_40_17_f.svg";
import imgCamera_e_18_n from "../assets/images/btn_map_cctv_e_40_18_n.svg";
import imgCamera_e_18_f from "../assets/images/btn_map_cctv_e_40_18_f.svg";
import imgCamera_e_19_n from "../assets/images/btn_map_cctv_e_40_19_n.svg";
import imgCamera_e_19_f from "../assets/images/btn_map_cctv_e_40_19_f.svg";
import imgCamera_e_20_n from "../assets/images/btn_map_cctv_e_40_20_n.svg";
import imgCamera_e_20_f from "../assets/images/btn_map_cctv_e_40_20_f.svg";
import imgCamera_e_21_n from "../assets/images/btn_map_cctv_e_40_21_n.svg";
import imgCamera_e_21_f from "../assets/images/btn_map_cctv_e_40_21_f.svg";
import imgCamera_e_22_n from "../assets/images/btn_map_cctv_e_40_22_n.svg";
import imgCamera_e_22_f from "../assets/images/btn_map_cctv_e_40_22_f.svg";
import imgCamera_e_23_n from "../assets/images/btn_map_cctv_e_40_23_n.svg";
import imgCamera_e_23_f from "../assets/images/btn_map_cctv_e_40_23_f.svg";
import imgCamera_e_24_n from "../assets/images/btn_map_cctv_e_40_24_n.svg";
import imgCamera_e_24_f from "../assets/images/btn_map_cctv_e_40_24_f.svg";
import imgCamera_e_25_n from "../assets/images/btn_map_cctv_e_40_25_n.svg";
import imgCamera_e_25_f from "../assets/images/btn_map_cctv_e_40_25_f.svg";
import imgCamera_e_26_n from "../assets/images/btn_map_cctv_e_40_26_n.svg";
import imgCamera_e_26_f from "../assets/images/btn_map_cctv_e_40_26_f.svg";
import imgCamera_e_27_n from "../assets/images/btn_map_cctv_e_40_27_n.svg";
import imgCamera_e_27_f from "../assets/images/btn_map_cctv_e_40_27_f.svg";
import imgCamera_e_28_n from "../assets/images/btn_map_cctv_e_40_28_n.svg";
import imgCamera_e_28_f from "../assets/images/btn_map_cctv_e_40_28_f.svg";
import imgCamera_e_29_n from "../assets/images/btn_map_cctv_e_40_29_n.svg";
import imgCamera_e_29_f from "../assets/images/btn_map_cctv_e_40_29_f.svg";
import imgCamera_e_30_n from "../assets/images/btn_map_cctv_e_40_30_n.svg";
import imgCamera_e_30_f from "../assets/images/btn_map_cctv_e_40_30_f.svg";
import imgCamera_e_31_n from "../assets/images/btn_map_cctv_e_40_31_n.svg";
import imgCamera_e_31_f from "../assets/images/btn_map_cctv_e_40_31_f.svg";
import imgCamera_e_32_n from "../assets/images/btn_map_cctv_e_40_32_n.svg";
import imgCamera_e_32_f from "../assets/images/btn_map_cctv_e_40_32_f.svg";
import imgCamera_e_33_n from "../assets/images/btn_map_cctv_e_40_33_n.svg";
import imgCamera_e_33_f from "../assets/images/btn_map_cctv_e_40_33_f.svg";
import imgCamera_e_34_n from "../assets/images/btn_map_cctv_e_40_34_n.svg";
import imgCamera_e_34_f from "../assets/images/btn_map_cctv_e_40_34_f.svg";
import imgCamera_e_35_n from "../assets/images/btn_map_cctv_e_40_35_n.svg";
import imgCamera_e_35_f from "../assets/images/btn_map_cctv_e_40_35_f.svg";

const imagesCamera: { [key: string]: any } = {
    imgCamera_0_n,
    imgCamera_0_f,
    imgCamera_1_n,
    imgCamera_1_f,
    imgCamera_2_n,
    imgCamera_2_f,
    imgCamera_3_n,
    imgCamera_3_f,
    imgCamera_4_n,
    imgCamera_4_f,
    imgCamera_5_n,
    imgCamera_5_f,
    imgCamera_6_n,
    imgCamera_6_f,
    imgCamera_7_n,
    imgCamera_7_f,
    imgCamera_8_n,
    imgCamera_8_f,
    imgCamera_9_n,
    imgCamera_9_f,
    imgCamera_10_f,
    imgCamera_10_n,
    imgCamera_11_n,
    imgCamera_11_f,
    imgCamera_12_n,
    imgCamera_12_f,
    imgCamera_13_n,
    imgCamera_13_f,
    imgCamera_14_n,
    imgCamera_14_f,
    imgCamera_15_n,
    imgCamera_15_f,
    imgCamera_16_n,
    imgCamera_16_f,
    imgCamera_17_n,
    imgCamera_17_f,
    imgCamera_18_n,
    imgCamera_18_f,
    imgCamera_19_n,
    imgCamera_19_f,
    imgCamera_20_n,
    imgCamera_20_f,
    imgCamera_21_n,
    imgCamera_21_f,
    imgCamera_22_n,
    imgCamera_22_f,
    imgCamera_23_n,
    imgCamera_23_f,
    imgCamera_24_n,
    imgCamera_24_f,
    imgCamera_25_n,
    imgCamera_25_f,
    imgCamera_26_n,
    imgCamera_26_f,
    imgCamera_27_n,
    imgCamera_27_f,
    imgCamera_28_n,
    imgCamera_28_f,
    imgCamera_29_n,
    imgCamera_29_f,
    imgCamera_30_n,
    imgCamera_30_f,
    imgCamera_31_n,
    imgCamera_31_f,
    imgCamera_32_n,
    imgCamera_32_f,
    imgCamera_33_n,
    imgCamera_33_f,
    imgCamera_34_n,
    imgCamera_34_f,
    imgCamera_35_n,
    imgCamera_35_f,
    imgCamera_e_0_n,
    imgCamera_e_0_f,
    imgCamera_e_1_n,
    imgCamera_e_1_f,
    imgCamera_e_2_n,
    imgCamera_e_2_f,
    imgCamera_e_3_n,
    imgCamera_e_3_f,
    imgCamera_e_4_n,
    imgCamera_e_4_f,
    imgCamera_e_5_n,
    imgCamera_e_5_f,
    imgCamera_e_6_n,
    imgCamera_e_6_f,
    imgCamera_e_7_n,
    imgCamera_e_7_f,
    imgCamera_e_8_n,
    imgCamera_e_8_f,
    imgCamera_e_9_n,
    imgCamera_e_9_f,
    imgCamera_e_10_f,
    imgCamera_e_10_n,
    imgCamera_e_11_n,
    imgCamera_e_11_f,
    imgCamera_e_12_n,
    imgCamera_e_12_f,
    imgCamera_e_13_n,
    imgCamera_e_13_f,
    imgCamera_e_14_n,
    imgCamera_e_14_f,
    imgCamera_e_15_n,
    imgCamera_e_15_f,
    imgCamera_e_16_n,
    imgCamera_e_16_f,
    imgCamera_e_17_n,
    imgCamera_e_17_f,
    imgCamera_e_18_n,
    imgCamera_e_18_f,
    imgCamera_e_19_n,
    imgCamera_e_19_f,
    imgCamera_e_20_n,
    imgCamera_e_20_f,
    imgCamera_e_21_n,
    imgCamera_e_21_f,
    imgCamera_e_22_n,
    imgCamera_e_22_f,
    imgCamera_e_23_n,
    imgCamera_e_23_f,
    imgCamera_e_24_n,
    imgCamera_e_24_f,
    imgCamera_e_25_n,
    imgCamera_e_25_f,
    imgCamera_e_26_n,
    imgCamera_e_26_f,
    imgCamera_e_27_n,
    imgCamera_e_27_f,
    imgCamera_e_28_n,
    imgCamera_e_28_f,
    imgCamera_e_29_n,
    imgCamera_e_29_f,
    imgCamera_e_30_n,
    imgCamera_e_30_f,
    imgCamera_e_31_n,
    imgCamera_e_31_f,
    imgCamera_e_32_n,
    imgCamera_e_32_f,
    imgCamera_e_33_n,
    imgCamera_e_33_f,
    imgCamera_e_34_n,
    imgCamera_e_34_f,
    imgCamera_e_35_n,
    imgCamera_e_35_f,
};

export const getCameraImageByKey = (key: string) => {
    return imagesCamera[key];
};

import imgSignalLamp_0_d from "../assets/images/ico_map_signal_lamp_0_d.svg";
import imgSignalLamp_0_e from "../assets/images/ico_map_signal_lamp_0_e.svg";
import imgSignalLamp_0_g from "../assets/images/ico_map_signal_lamp_0_g.svg";
import imgSignalLamp_0_l from "../assets/images/ico_map_signal_lamp_0_l.svg";
import imgSignalLamp_0_r from "../assets/images/ico_map_signal_lamp_0_r.svg";
import imgSignalLamp_0_s from "../assets/images/ico_map_signal_lamp_0_s.svg";
import imgSignalLamp_0_sl from "../assets/images/ico_map_signal_lamp_0_sl.svg";
import imgSignalLamp_0_y from "../assets/images/ico_map_signal_lamp_0_y.svg";
import imgSignalLamp_1_d from "../assets/images/ico_map_signal_lamp_1_d.svg";
import imgSignalLamp_1_e from "../assets/images/ico_map_signal_lamp_1_e.svg";
import imgSignalLamp_1_g from "../assets/images/ico_map_signal_lamp_1_g.svg";
import imgSignalLamp_1_l from "../assets/images/ico_map_signal_lamp_1_l.svg";
import imgSignalLamp_1_r from "../assets/images/ico_map_signal_lamp_1_r.svg";
import imgSignalLamp_1_s from "../assets/images/ico_map_signal_lamp_1_s.svg";
import imgSignalLamp_1_sl from "../assets/images/ico_map_signal_lamp_1_sl.svg";
import imgSignalLamp_1_y from "../assets/images/ico_map_signal_lamp_1_y.svg";
import imgSignalLamp_2_l from "../assets/images/ico_map_signal_lamp_2_l.svg";
import imgSignalLamp_2_s from "../assets/images/ico_map_signal_lamp_2_s.svg";
import imgSignalLamp_2_sl from "../assets/images/ico_map_signal_lamp_2_sl.svg";
import imgSignalLamp_3_l from "../assets/images/ico_map_signal_lamp_3_l.svg";
import imgSignalLamp_3_s from "../assets/images/ico_map_signal_lamp_3_s.svg";
import imgSignalLamp_3_sl from "../assets/images/ico_map_signal_lamp_3_sl.svg";
import imgSignalLamp_4_l from "../assets/images/ico_map_signal_lamp_4_l.svg";
import imgSignalLamp_4_s from "../assets/images/ico_map_signal_lamp_4_s.svg";
import imgSignalLamp_4_sl from "../assets/images/ico_map_signal_lamp_4_sl.svg";
import imgSignalLamp_5_l from "../assets/images/ico_map_signal_lamp_5_l.svg";
import imgSignalLamp_5_s from "../assets/images/ico_map_signal_lamp_5_s.svg";
import imgSignalLamp_5_sl from "../assets/images/ico_map_signal_lamp_5_sl.svg";
import imgSignalLamp_6_l from "../assets/images/ico_map_signal_lamp_6_l.svg";
import imgSignalLamp_6_s from "../assets/images/ico_map_signal_lamp_6_s.svg";
import imgSignalLamp_6_sl from "../assets/images/ico_map_signal_lamp_6_sl.svg";
import imgSignalLamp_7_l from "../assets/images/ico_map_signal_lamp_7_l.svg";
import imgSignalLamp_7_s from "../assets/images/ico_map_signal_lamp_7_s.svg";
import imgSignalLamp_7_sl from "../assets/images/ico_map_signal_lamp_7_sl.svg";

const imagesSignalLamp: { [key: string]: any } = {
    imgSignalLamp_0_d,
    imgSignalLamp_0_e,
    imgSignalLamp_0_g,
    imgSignalLamp_0_l,
    imgSignalLamp_0_r,
    imgSignalLamp_0_s,
    imgSignalLamp_0_sl,
    imgSignalLamp_0_y,
    imgSignalLamp_1_d,
    imgSignalLamp_1_e,
    imgSignalLamp_1_g,
    imgSignalLamp_1_l,
    imgSignalLamp_1_r,
    imgSignalLamp_1_s,
    imgSignalLamp_1_sl,
    imgSignalLamp_1_y,
    imgSignalLamp_2_l,
    imgSignalLamp_2_s,
    imgSignalLamp_2_sl,
    imgSignalLamp_3_l,
    imgSignalLamp_3_s,
    imgSignalLamp_3_sl,
    imgSignalLamp_4_l,
    imgSignalLamp_4_s,
    imgSignalLamp_4_sl,
    imgSignalLamp_5_l,
    imgSignalLamp_5_s,
    imgSignalLamp_5_sl,
    imgSignalLamp_6_l,
    imgSignalLamp_6_s,
    imgSignalLamp_6_sl,
    imgSignalLamp_7_l,
    imgSignalLamp_7_s,
    imgSignalLamp_7_sl,
};

export const getTrafficLightImageByKey = (key: string) => {
    return imagesSignalLamp[key];
};
