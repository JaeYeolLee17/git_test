import React, { useState } from "react";
import {
    Map,
    MapMarker,
    Polygon,
    Circle,
    Polyline,
} from "react-kakao-maps-sdk";
import * as Utils from "../utils/utils";
import * as Common from "../commons/common";

export type KakaoMapStyleType = {
    width: string | number;
    height: string | number;
};

export type KakaoMapRegionType = {
    current: Common.RegionInfo;
    isShow: boolean;
};

export type KakaoMapIntersectionsType = {
    list: any[];
    selected: string;
    clickEvent: (intersectionId: string) => void;
    showEdge?: boolean;
};

export type KakaoMapCamerasType = {
    list: any[];
    isShow: boolean;
    selected: string;
    clickEvent: (cameraId: string, intersectionId: string) => void;
};

export type KakaoMapLinksType = {
    list: any[];
    isShow: boolean;
};

export type KakaoMapTrafficLightsType = {
    list: any[];
    blink: boolean;
    isShow: boolean;
};

export type KakaoMapAvlType = {
    list: any[];
    isShow: boolean;
    selected: string;
};

export const displayRegion = (region: KakaoMapRegionType) => {
    if (region === undefined) return null;
    if (region.current.gps === undefined) return null;
    //console.log("displayRegion", JSON.stringify(region));
    if (
        Utils.utilIsEmptyObj(region.current) === false &&
        Utils.utilIsEmptyArray(region.current.gps) === false
    ) {
        return (
            <Polygon
                path={region.current.gps}
                strokeWeight={2} // 선의 두께입니다
                strokeColor={"#ff00aa"} // 선의 색깔입니다
                strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                fillColor={"#ff00aa"} // 채우기 색깔입니다
                fillOpacity={0.05} // 채우기 불투명도 입니다
                zIndex={1}
            />
        );
    }

    return null;
};

export const displayIntersection = (
    intersections: KakaoMapIntersectionsType
) => {
    if (intersections === undefined) return null;

    // console.log("intersections", JSON.stringify(intersections));

    if (Utils.utilIsEmptyArray(intersections.list) === false) {
        return intersections.list
            .filter(
                (intersection) =>
                    intersections.showEdge || intersection.region !== null
            )
            .map((intersection) => {
                let strokeColor;
                let fillColor;

                if (intersection.intersectionId === intersections.selected) {
                    strokeColor = "#ff8000";
                    fillColor = "#ff9900";
                } else {
                    if (intersection.region !== null) {
                        strokeColor = "#ffff00";
                        fillColor = "#ffee00";
                    } else {
                        strokeColor = "#ffff00";
                        fillColor = "#707070";
                    }
                }

                return (
                    <Circle
                        key={intersection.intersectionId}
                        center={intersection.gps}
                        radius={150}
                        strokeWeight={1} // 선의 두께입니다
                        strokeColor={strokeColor} // 선의 색깔입니다
                        strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                        fillColor={fillColor} // 채우기 색깔입니다
                        fillOpacity={0.4} // 채우기 불투명도 입니다
                        zIndex={5}
                        onClick={(marker: kakao.maps.Circle) => {
                            intersections.clickEvent(
                                intersection.intersectionId
                            );
                        }}
                    />
                );
            });
    }

    return null;
};

export const displayCamera = (cameras: KakaoMapCamerasType, level: number) => {
    if (cameras === undefined) return null;

    // console.log("cameras", JSON.stringify(cameras));

    return cameras.list?.map((camera) => {
        const normalState = true; // TODO
        const isSelected = camera.cameraId === cameras.selected;
        const imageUrl =
            "/images/btn_map_cctv" +
            (normalState ? "" : "_e") +
            "_40_" +
            (camera.degree ? camera.degree : "0") +
            (isSelected ? "_f" : "_n") +
            ".svg";

        return (
            <MapMarker
                key={camera.cameraId}
                position={camera.gps}
                image={{
                    src: `${imageUrl}`,
                    size: {
                        width: level < 4 ? 40 : 30,
                        height: level < 4 ? 40 : 30,
                    },
                    options: {
                        offset: {
                            x: level < 4 ? 20 : 15,
                            y: level < 4 ? 20 : 15,
                        },
                    },
                }}
                onClick={(marker: kakao.maps.Marker) => {
                    cameras.clickEvent(
                        camera.cameraId,
                        camera.intersection.intersectionId
                    );
                }}
            />
        );
    });
};

export const displayLinks = (
    links: KakaoMapLinksType,
    kakaoMap: kakao.maps.Map,
    level: number
) => {
    if (links === undefined) return null;
    if (kakaoMap === undefined) return null;

    // console.log("links", JSON.stringify(links));

    if (links.list) {
        //console.log("links", links);
        if (level >= 5) {
            return links.list.map((link, index) => {
                const qtsrlu = Utils.utilConvertQtsrlu15Minute(
                    link.data[0].qtsrlu
                );
                const srlu = Utils.utilConvertSrlu15Minute(link.data[0].srlu);

                //const speed = (srlu / qtsrlu).toFixed(2);
                const speed = srlu / qtsrlu;
                const color = Utils.utilGetSpeedColor(speed);

                const key = link.link.startId + "_" + link.link.endId;

                return (
                    <div key={key}>
                        <Polyline
                            path={link.link.gps}
                            strokeWeight={8} // 선의 두께 입니다
                            strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
                            strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                            strokeStyle={"solid"} // 선의 스타일입니다
                            zIndex={2}
                        />
                        <Polyline
                            path={link.link.gps}
                            endArrow={true}
                            strokeWeight={4} // 선의 두께 입니다
                            strokeColor={color} // 선의 색깔입니다
                            strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                            strokeStyle={"solid"} // 선의 스타일입니다
                            zIndex={3}
                        />
                    </div>
                );
            });
        } else {
            return links.list.map((link, index) => {
                // let qtsr = (link.data[0].qtsr * 4) / 3600;
                // let sr = (link.data[0].sr * 100 * 4) / 1000;
                const qtsr = Utils.utilConvertQtsrlu15Minute(link.data[0].qtsr);
                const sr = Utils.utilConvertSrlu15Minute(link.data[0].sr);
                // let qtlu = (link.data[0].qtlu * 4) / 3600;
                // let lu = (link.data[0].lu * 100 * 4) / 1000;
                const qtlu = Utils.utilConvertQtsrlu15Minute(link.data[0].qtlu);
                const lu = Utils.utilConvertSrlu15Minute(link.data[0].lu);

                // const srSpeed = (sr / qtsr).toFixed(2);
                // const luSpeed = (lu / qtlu).toFixed(2);
                const srSpeed = sr / qtsr;
                const luSpeed = lu / qtlu;

                const srColor = Utils.utilGetSpeedColor(srSpeed);
                const luColor = Utils.utilGetSpeedColor(luSpeed);

                const luPath = Utils.utilConvertParallelLines(
                    kakaoMap,
                    level,
                    link.link.gps
                );

                const key = link.link.startId + "_" + link.link.endId;

                return (
                    <div key={key}>
                        {luPath && (
                            <Polyline
                                path={luPath}
                                strokeWeight={8} // 선의 두께 입니다
                                strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
                                strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                                strokeStyle={"solid"} // 선의 스타일입니다
                                zIndex={2}
                            />
                        )}
                        {luPath && (
                            <Polyline
                                path={luPath}
                                endArrow={true}
                                strokeWeight={4} // 선의 두께 입니다
                                strokeColor={luColor} // 선의 색깔입니다
                                strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                                strokeStyle={"solid"} // 선의 스타일입니다
                                zIndex={3}
                            />
                        )}

                        <Polyline
                            path={link.link.gps}
                            strokeWeight={8} // 선의 두께 입니다
                            strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
                            strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                            strokeStyle={"solid"} // 선의 스타일입니다
                            zIndex={2}
                        />
                        <Polyline
                            path={link.link.gps}
                            endArrow={true}
                            strokeWeight={4} // 선의 두께 입니다
                            strokeColor={srColor} // 선의 색깔입니다
                            strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                            strokeStyle={"solid"} // 선의 스타일입니다
                            zIndex={3}
                        />
                    </div>
                );
            });
        }
    }

    return null;
};

const getTrafficSignalType = (
    trafficLights: KakaoMapTrafficLightsType,
    signalDatas: any
) => {
    let signalType = "d";

    signalDatas.signalInfo.forEach((signal: any) => {
        if (signal.info === 1 || signal.info === 2) {
            switch (signal.status) {
                case 0:
                    break;
                case 1:
                    if (signalType === "d" || signalType === "y") {
                        signalType = "r";
                    }
                    break;
                case 2:
                    if (signalType === "d") {
                        signalType = "y";
                    }
                    break;
                case 3:
                case 6:
                    if (signalType === "s" || signalType === "l") {
                        signalType = "sl";
                    } else {
                        if (signal.info === 1) {
                            signalType = "s";
                        } else if (signal.info === 2) {
                            signalType = "l";
                        }
                    }
                    break;

                case 4:
                    if (trafficLights.blink === true) {
                        signalType = "r";
                    } else {
                        signalType = "d";
                    }
                    break;

                case 5:
                    if (trafficLights.blink === true) {
                        signalType = "y";
                    } else {
                        signalType = "d";
                    }
                    break;

                default:
                    signalType = "d";
                    break;
            }
        }
    });

    return signalType;
};

const toRad = (brng: number) => {
    return (brng * Math.PI) / 180;
};

const toDeg = (brng: number) => {
    return (brng * 180) / Math.PI;
};

const getMoveGPSPosition = (
    gps: Common.GpsPosition,
    bearing: number,
    distance: number
) => {
    let originalLat = gps.lat;
    let originalLng = gps.lng;

    distance = distance / 6371;
    bearing = toRad(bearing);

    originalLat = toRad(originalLat);
    originalLng = toRad(originalLng);

    const tranlateLat = Math.asin(
        Math.sin(originalLat) * Math.cos(distance) +
            Math.cos(originalLat) * Math.sin(distance) * Math.cos(bearing)
    );

    const tranlateLng =
        originalLng +
        Math.atan2(
            Math.sin(bearing) * Math.sin(distance) * Math.cos(originalLat),
            Math.cos(distance) - Math.sin(originalLat) * Math.sin(tranlateLat)
        );

    if (isNaN(tranlateLat) || isNaN(tranlateLng)) return null;

    return { lat: toDeg(tranlateLat), lng: toDeg(tranlateLng) };
};

export const displayTrafficLights = (
    trafficLights: KakaoMapTrafficLightsType
) => {
    if (trafficLights === undefined) return null;

    // console.log("trafficLights", JSON.stringify(trafficLights));

    const defaultTrafficeIntervalTime = 1000 * 60 * 1;
    if (Utils.utilIsEmptyArray(trafficLights.list) === false) {
        return trafficLights.list.map((tsiData) => {
            const currentDateTime = new Date();
            const lastSignalDateTime = new Date(tsiData.time);
            const signalDateInterval =
                currentDateTime.getTime() - lastSignalDateTime.getTime();

            let signalType = "d";
            if (tsiData.error !== 0) {
                signalType = "e";
            } else if (
                signalDateInterval > defaultTrafficeIntervalTime ||
                tsiData.time === undefined
            ) {
                signalType = "e";
            }

            signalType = "d"; // TODO: Code for Demo

            // if (tsiData.nodeId === "1570190800") {
            //     console.log("tsiData", tsiData);
            // }

            const datas = tsiData.signal.map((signalData: any) => {
                if (signalType !== "e") {
                    signalType = getTrafficSignalType(
                        trafficLights,
                        signalData
                    );
                }

                let imageUrl = "/images/ico_map_signal_lamp_";
                const signalDirectionType = signalData.direction / 45;
                let imageType = 4;
                if (
                    signalType === "s" ||
                    signalType === "l" ||
                    signalType === "sl"
                ) {
                    if (signalDirectionType < 4)
                        imageType = signalDirectionType + 4;
                    else imageType = signalDirectionType - 4;
                } else {
                    imageType = signalDirectionType % 2;
                }
                imageUrl += String(imageType) + "_" + signalType + ".svg";

                const position = getMoveGPSPosition(
                    tsiData.gps,
                    signalData.direction,
                    0.05
                );

                if (position === null) return null;

                return (
                    <MapMarker
                        key={tsiData.nodeId + "_" + signalData.direction}
                        position={position}
                        image={{
                            src: imageUrl,
                            size: {
                                width: 40,
                                height: 40,
                            },
                            options: {
                                offset: {
                                    x: 20,
                                    y: 20,
                                },
                            },
                        }}
                    />
                );
            });

            return <div key={tsiData.nodeId}>{datas}</div>;
        });
    }

    return null;
};

export const displayAvl = (avl: KakaoMapAvlType) => {
    if (avl === undefined) return null;

    if (Utils.utilIsEmptyArray(avl.list) === false) {
        return avl.list.map((avlData) => {
            //console.log("avlData", avlData);

            const naviPath = avlData.path.slice(-1)[0].gps;
            //console.log("naviPath", naviPath);

            let currPosition = null;
            if (Utils.utilIsEmptyArray(avlData.track) === false) {
                currPosition = avlData.track[0].gps;
            }
            //console.log("currPosition", currPosition);

            const destPosition = avlData.gps;
            //console.log("destPosition", destPosition);
            const status = avlData.status.slice(-1)[0].name;
            //console.log("status", status);

            const statusInfo: { [key: string]: string } = {
                출동: "r",
                현장도착: "y",
                현장출발: "b",
                병원도착: "g",
            };

            const vehicleImageUrl =
                "/images/btn_map_emergency_" +
                statusInfo[status] +
                (avlData.carNo === avl.selected ? "_p" : "_n") +
                ".svg";

            //console.log(vehicleImageUrl);

            return (
                <div key={avlData.carNo}>
                    <Polyline
                        path={naviPath}
                        strokeWeight={8} // 선의 두께 입니다
                        strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
                        strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                        strokeStyle={"solid"} // 선의 스타일입니다
                        zIndex={8}
                    />
                    <Polyline
                        path={naviPath}
                        strokeWeight={4} // 선의 두께 입니다
                        strokeColor={"#306fd9"} // 선의 색깔입니다
                        strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                        strokeStyle={"shortdot"} // 선의 스타일입니다
                        zIndex={8}
                    />
                    <MapMarker
                        position={destPosition}
                        image={{
                            src: "/images/ico_map_emergency_vehicle_pin.png",
                            size: {
                                width: 40,
                                height: 40,
                            },
                            options: {
                                offset: {
                                    x: 20,
                                    y: 20,
                                },
                            },
                        }}
                        zIndex={8}
                    />
                    {currPosition && (
                        <MapMarker
                            position={currPosition}
                            image={{
                                src: vehicleImageUrl,
                                size: {
                                    width: 67,
                                    height: 89,
                                },
                                options: {
                                    offset: {
                                        x: 34,
                                        y: 81,
                                    },
                                },
                            }}
                            zIndex={9}
                        />
                    )}
                </div>
            );
        });
    }

    return null;
};

function KakaoMap({
    style,
    region,
    intersections,
    cameras,
    links,
    trafficLights,
    avl,
}: {
    style: KakaoMapStyleType;
    region: KakaoMapRegionType;
    intersections: KakaoMapIntersectionsType;
    cameras: KakaoMapCamerasType;
    links: KakaoMapLinksType;
    trafficLights: KakaoMapTrafficLightsType;
    avl: KakaoMapAvlType;
}) {
    const [kakaoMap, setKakaoMap] = useState<kakao.maps.Map>();
    const [level, setLevel] = useState<number>(7);

    // const displayRegion = () => {
    //     if (region === undefined) return null;
    //     //console.log("displayRegion", JSON.stringify(region));
    //     if (
    //         Utils.utilIsEmptyObj(region.current) === false &&
    //         Utils.utilIsEmptyArray(region.current.gps) === false
    //     ) {
    //         return (
    //             <Polygon
    //                 path={region.current.gps}
    //                 strokeWeight={2} // 선의 두께입니다
    //                 strokeColor={"#ff00aa"} // 선의 색깔입니다
    //                 strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                 fillColor={"#ff00aa"} // 채우기 색깔입니다
    //                 fillOpacity={0.05} // 채우기 불투명도 입니다
    //                 zIndex={1}
    //             />
    //         );
    //     }

    //     return null;
    // };

    // const displayIntersection = () => {
    //     if (intersections === undefined) return null;
    //     //console.log("displayIntersection");
    //     if (Utils.utilIsEmptyArray(intersections.list) === false) {
    //         return intersections.list
    //             .filter(
    //                 (intersection) =>
    //                     intersections.showEdge || intersection.region !== null
    //             )
    //             .map((intersection) => {
    //                 let strokeColor;
    //                 let fillColor;

    //                 if (
    //                     intersection.intersectionId === intersections.selected
    //                 ) {
    //                     strokeColor = "#ff8000";
    //                     fillColor = "#ff9900";
    //                 } else {
    //                     if (intersection.region !== null) {
    //                         strokeColor = "#ffff00";
    //                         fillColor = "#ffee00";
    //                     } else {
    //                         strokeColor = "#ffff00";
    //                         fillColor = "#707070";
    //                     }
    //                 }

    //                 return (
    //                     <Circle
    //                         key={intersection.intersectionId}
    //                         center={intersection.gps}
    //                         radius={150}
    //                         strokeWeight={1} // 선의 두께입니다
    //                         strokeColor={strokeColor} // 선의 색깔입니다
    //                         strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                         fillColor={fillColor} // 채우기 색깔입니다
    //                         fillOpacity={0.4} // 채우기 불투명도 입니다
    //                         zIndex={5}
    //                         onClick={(marker) => {
    //                             intersections.clickEvent(
    //                                 intersection.intersectionId
    //                             );
    //                         }}
    //                     />
    //                 );
    //             });
    //     }

    //     return null;
    // };

    // const displayCamera = () => {
    //     if (cameras === undefined) return null;
    //     //console.log("displayCamera");
    //     return cameras.list?.map((camera) => {
    //         let normalState = true; // TODO
    //         let isSelected = camera.cameraId === cameras.selected;
    //         let imageUrl =
    //             "/images/btn_map_cctv" +
    //             (normalState ? "" : "_e") +
    //             "_40_" +
    //             (camera.degree ? camera.degree : "0") +
    //             (isSelected ? "_f" : "_n") +
    //             ".svg";

    //         return (
    //             <MapMarker
    //                 key={camera.cameraId}
    //                 position={camera.gps}
    //                 image={{
    //                     src: `${imageUrl}`,
    //                     size: {
    //                         widht: level < 4 ? 40 : 30,
    //                         height: level < 4 ? 40 : 30,
    //                     },
    //                     options: {
    //                         offset: {
    //                             x: level < 4 ? 20 : 15,
    //                             y: level < 4 ? 20 : 15,
    //                         },
    //                     },
    //                 }}
    //                 onClick={(marker) => {
    //                     cameras.clickEvent(
    //                         camera.cameraId,
    //                         camera.intersection.intersectionId
    //                     );
    //                 }}
    //             />
    //         );
    //     });
    // };

    // const displayLinks = () => {
    //     if (links === undefined) return null;
    //     //console.log("displayLinks");
    //     if (links.list) {
    //         //console.log("links", links);
    //         if (level >= 5) {
    //             return links.list.map((link, index) => {
    //                 let qtsrlu = Utils.utilConvertQtsrlu15Minute(
    //                     link.data[0].qtsrlu
    //                 );
    //                 let srlu = Utils.utilConvertSrlu15Minute(link.data[0].srlu);

    //                 let speed = (srlu / qtsrlu).toFixed(2);
    //                 let color = Utils.utilGetSpeedColor(speed);

    //                 let key = link.link.startId + "_" + link.link.endId;

    //                 return (
    //                     <div key={key}>
    //                         <Polyline
    //                             path={link.link.gps}
    //                             strokeWeight={8} // 선의 두께 입니다
    //                             strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
    //                             strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                             strokeStyle={"solid"} // 선의 스타일입니다
    //                             zIndex={2}
    //                         />
    //                         <Polyline
    //                             path={link.link.gps}
    //                             endArrow={true}
    //                             strokeWeight={4} // 선의 두께 입니다
    //                             strokeColor={color} // 선의 색깔입니다
    //                             strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                             strokeStyle={"solid"} // 선의 스타일입니다
    //                             zIndex={3}
    //                         />
    //                     </div>
    //                 );
    //             });
    //         } else {
    //             return links.list.map((link, index) => {
    //                 // let qtsr = (link.data[0].qtsr * 4) / 3600;
    //                 // let sr = (link.data[0].sr * 100 * 4) / 1000;
    //                 let qtsr = Utils.utilConvertQtsrlu15Minute(
    //                     link.data[0].qtsr
    //                 );
    //                 let sr = Utils.utilConvertSrlu15Minute(link.data[0].sr);
    //                 // let qtlu = (link.data[0].qtlu * 4) / 3600;
    //                 // let lu = (link.data[0].lu * 100 * 4) / 1000;
    //                 let qtlu = Utils.utilConvertQtsrlu15Minute(
    //                     link.data[0].qtlu
    //                 );
    //                 let lu = Utils.utilConvertSrlu15Minute(link.data[0].lu);

    //                 let srSpeed = (sr / qtsr).toFixed(2);
    //                 let luSpeed = (lu / qtlu).toFixed(2);

    //                 let srColor = Utils.utilGetSpeedColor(srSpeed);
    //                 let luColor = Utils.utilGetSpeedColor(luSpeed);

    //                 let luPath = Utils.utilConvertParallelLines(
    //                     kakaoMap,
    //                     level,
    //                     link.link.gps
    //                 );

    //                 let key = link.link.startId + "_" + link.link.endId;

    //                 return (
    //                     <div key={key}>
    //                         <Polyline
    //                             path={luPath}
    //                             strokeWeight={8} // 선의 두께 입니다
    //                             strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
    //                             strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                             strokeStyle={"solid"} // 선의 스타일입니다
    //                             zIndex={2}
    //                         />
    //                         <Polyline
    //                             path={luPath}
    //                             endArrow={true}
    //                             strokeWeight={4} // 선의 두께 입니다
    //                             strokeColor={luColor} // 선의 색깔입니다
    //                             strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                             strokeStyle={"solid"} // 선의 스타일입니다
    //                             zIndex={3}
    //                         />

    //                         <Polyline
    //                             path={link.link.gps}
    //                             strokeWeight={8} // 선의 두께 입니다
    //                             strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
    //                             strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                             strokeStyle={"solid"} // 선의 스타일입니다
    //                             zIndex={2}
    //                         />
    //                         <Polyline
    //                             path={link.link.gps}
    //                             endArrow={true}
    //                             strokeWeight={4} // 선의 두께 입니다
    //                             strokeColor={srColor} // 선의 색깔입니다
    //                             strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                             strokeStyle={"solid"} // 선의 스타일입니다
    //                             zIndex={3}
    //                         />
    //                     </div>
    //                 );
    //             });
    //         }
    //     }

    //     return null;
    // };

    // const getTrafficSignalType = (signalDatas) => {
    //     let signalType = "d";

    //     signalDatas.signalInfo.forEach((signalInfo) => {
    //         if (signalInfo.info === 1 || signalInfo.info === 2) {
    //             switch (signalInfo.status) {
    //                 case 0:
    //                     break;
    //                 case 1:
    //                     if (signalType === "d" || signalType === "y") {
    //                         signalType = "r";
    //                     }
    //                     break;
    //                 case 2:
    //                     if (signalType === "d") {
    //                         signalType = "y";
    //                     }
    //                     break;
    //                 case 3:
    //                 case 6:
    //                     if (signalType === "s" || signalType === "l") {
    //                         signalType = "sl";
    //                     } else {
    //                         if (signalInfo.info === 1) {
    //                             signalType = "s";
    //                         } else if (signalInfo.info === 2) {
    //                             signalType = "l";
    //                         }
    //                     }
    //                     break;

    //                 case 4:
    //                     if (trafficLights.blink === true) {
    //                         signalType = "r";
    //                     } else {
    //                         signalType = "d";
    //                     }
    //                     break;

    //                 case 5:
    //                     if (trafficLights.blink === true) {
    //                         signalType = "y";
    //                     } else {
    //                         signalType = "d";
    //                     }
    //                     break;

    //                 default:
    //                     signalType = "d";
    //                     break;
    //             }
    //         }
    //     });

    //     return signalType;
    // };

    // const toRad = (brng) => {
    //     return (brng * Math.PI) / 180;
    // };

    // const toDeg = (brng) => {
    //     return (brng * 180) / Math.PI;
    // };

    // const getMoveGPSPosition = (gps, bearing, distance) => {
    //     var originalLat = gps.lat;
    //     var originalLng = gps.lng;

    //     distance = distance / 6371;
    //     bearing = toRad(bearing);

    //     originalLat = toRad(originalLat);
    //     originalLng = toRad(originalLng);

    //     var tranlateLat = Math.asin(
    //         Math.sin(originalLat) * Math.cos(distance) +
    //             Math.cos(originalLat) * Math.sin(distance) * Math.cos(bearing)
    //     );

    //     var tranlateLng =
    //         originalLng +
    //         Math.atan2(
    //             Math.sin(bearing) * Math.sin(distance) * Math.cos(originalLat),
    //             Math.cos(distance) -
    //                 Math.sin(originalLat) * Math.sin(tranlateLat)
    //         );

    //     if (isNaN(tranlateLat) || isNaN(tranlateLng)) return null;

    //     return { lat: toDeg(tranlateLat), lng: toDeg(tranlateLng) };
    // };

    // const displayTrafficLights = () => {
    //     if (trafficLights === undefined) return null;

    //     const defaultTrafficeIntervalTime = 1000 * 60 * 1;
    //     //console.log("displayTrafficLights");
    //     if (Utils.utilIsEmptyArray(trafficLights.list) === false) {
    //         return trafficLights.list.map((tsiData) => {
    //             let currentDateTime = new Date();
    //             let lastSignalDateTime = new Date(tsiData.time);
    //             let signalDateInterval =
    //                 currentDateTime.getTime() - lastSignalDateTime.getTime();

    //             var signalType = "d";
    //             if (tsiData.error !== 0) {
    //                 signalType = "e";
    //             } else if (
    //                 signalDateInterval > defaultTrafficeIntervalTime ||
    //                 tsiData.time === undefined
    //             ) {
    //                 signalType = "e";
    //             }

    //             signalType = "d"; // TODO: Code for Demo

    //             // if (tsiData.nodeId === "1570190800") {
    //             //     console.log("tsiData", tsiData);
    //             // }

    //             let datas = tsiData.signal.map((signalData) => {
    //                 if (signalType !== "e") {
    //                     signalType = getTrafficSignalType(signalData);
    //                 }

    //                 let imageUrl = "/images/ico_map_signal_lamp_";
    //                 let signalDirectionType = signalData.direction / 45;
    //                 let imageType = 4;
    //                 if (
    //                     signalType === "s" ||
    //                     signalType === "l" ||
    //                     signalType === "sl"
    //                 ) {
    //                     if (signalDirectionType < 4)
    //                         imageType = signalDirectionType + 4;
    //                     else imageType = signalDirectionType - 4;
    //                 } else {
    //                     imageType = signalDirectionType % 2;
    //                 }
    //                 imageUrl += String(imageType) + "_" + signalType + ".svg";

    //                 let position = getMoveGPSPosition(
    //                     tsiData.gps,
    //                     signalData.direction,
    //                     0.05
    //                 );

    //                 return (
    //                     <MapMarker
    //                         // key={tsiData.nodeId + "_" + signalData.direction}
    //                         position={position}
    //                         image={{
    //                             src: imageUrl,
    //                             size: {
    //                                 widht: 40,
    //                                 height: 40,
    //                             },
    //                             options: {
    //                                 offset: {
    //                                     x: 20,
    //                                     y: 20,
    //                                 },
    //                             },
    //                         }}
    //                     />
    //                 );
    //             });

    //             return <div key={tsiData.nodeId}>{datas}</div>;
    //         });
    //     }

    //     return null;
    // };

    // const displayAvl = () => {
    //     if (avl === undefined) return null;

    //     if (Utils.utilIsEmptyArray(avl.list) === false) {
    //         return avl.list.map((avlData) => {
    //             //console.log("avlData", avlData);

    //             let naviPath = avlData.path.slice(-1)[0].gps;
    //             //console.log("naviPath", naviPath);

    //             let currPosition = null;
    //             if (Utils.utilIsEmptyArray(avlData.track) === false) {
    //                 currPosition = avlData.track[0].gps;
    //             }
    //             //console.log("currPosition", currPosition);

    //             let destPosition = avlData.gps;
    //             //console.log("destPosition", destPosition);
    //             let status = avlData.status.slice(-1)[0].name;
    //             //console.log("status", status);

    //             let statusInfo = {
    //                 출동: "r",
    //                 현장도착: "y",
    //                 현장출발: "b",
    //                 병원도착: "g",
    //             };

    //             let vehicleImageUrl =
    //                 "/images/btn_map_emergency_" +
    //                 statusInfo[status] +
    //                 (avlData.carNo === avl.selected ? "_p" : "_n") +
    //                 ".svg";

    //             //console.log(vehicleImageUrl);

    //             return (
    //                 <div key={avlData.carNo}>
    //                     <Polyline
    //                         path={naviPath}
    //                         strokeWeight={8} // 선의 두께 입니다
    //                         strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
    //                         strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                         strokeStyle={"solid"} // 선의 스타일입니다
    //                         zIndex={8}
    //                     />
    //                     <Polyline
    //                         path={naviPath}
    //                         strokeWeight={4} // 선의 두께 입니다
    //                         strokeColor={"#306fd9"} // 선의 색깔입니다
    //                         strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    //                         strokeStyle={"shortdot"} // 선의 스타일입니다
    //                         zIndex={8}
    //                     />
    //                     <MapMarker
    //                         position={destPosition}
    //                         image={{
    //                             src: "/images/ico_map_emergency_vehicle_pin.png",
    //                             size: {
    //                                 widht: 40,
    //                                 height: 40,
    //                             },
    //                             options: {
    //                                 offset: {
    //                                     x: 20,
    //                                     y: 20,
    //                                 },
    //                             },
    //                         }}
    //                         zIndex={8}
    //                     />
    //                     {currPosition && (
    //                         <MapMarker
    //                             position={currPosition}
    //                             image={{
    //                                 src: vehicleImageUrl,
    //                                 size: {
    //                                     widht: 67,
    //                                     height: 89,
    //                                 },
    //                                 options: {
    //                                     offset: {
    //                                         x: 34,
    //                                         y: 81,
    //                                     },
    //                                 },
    //                             }}
    //                             zIndex={9}
    //                         />
    //                     )}
    //                 </div>
    //             );
    //         });
    //     }

    //     return null;
    // };

    const handleMap = (map: kakao.maps.Map) => {
        setKakaoMap(map);
    };

    return (
        <Map
            center={{
                // 지도의 중심좌표
                lat: 35.85810060700929,
                lng: 128.55729938820272,
            }}
            style={style}
            level={level}
            onCreate={(map) => handleMap(map)}
            onZoomChanged={(map) => setLevel(map.getLevel())}
        >
            {region?.isShow && displayRegion(region)}
            {displayIntersection(intersections)}
            {cameras?.isShow && displayCamera(cameras, level)}
            {links?.isShow &&
                kakaoMap !== undefined &&
                displayLinks(links, kakaoMap, level)}
            {trafficLights?.isShow && displayTrafficLights(trafficLights)}
            {avl?.isShow && displayAvl(avl)}
        </Map>
    );
}

export default KakaoMap;
