import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup, Polygon, Polyline, Circle, useMapEvents } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import * as Utils from "../utils/utils";
import * as Common from "../commons/common";
import L from "leaflet";
import styles from "./OsmMap.module.css";

import imgEmergencyVehiclePin from "../assets/images/ico_map_emergency_vehicle_pin.png";

export type OsmMapStyleType = {
    width: string | number;
    height: string | number;
    zIndex: string;
};

export type OsmMapRegionType = {
    current: Common.RegionInfo;
    isShow: boolean;
};

export type OsmMapIntersectionsType = {
    list: any[];
    selected: string | null;
    clickEvent: (intersectionNo: string) => void;
    showEdge?: boolean;
};

export type OsmMapCamerasType = {
    list: any[];
    isShow: boolean;
    selected: string | null;
    clickEvent: (cameraNo: string, intersectionNo: string) => void;
};

export type OsmMapLinksType = {
    list: any[];
    isShow: boolean;
};

export type OsmMapTsiType = {
    list: any[];
    blink: boolean;
    isShow: boolean;
};

export type OsmMapAvlType = {
    list: any[];
    isShow: boolean;
    selected: string;
};

export type kakaoMapCenterType = {
    lat: number;
    lng: number;
};

export type OsmMapEditLinksType = {
    list: any[];
    selected: any;
};

export const displayRegion = (region: OsmMapRegionType) => {
    if (region === undefined) return null;
    if (region.current.gps === undefined || region.current.gps === null) return null;
    if (Utils.utilIsEmptyObj(region.current) === false && Utils.utilIsEmptyArray(region.current.gps) === false) {
        return (
            <Polygon
                positions={region.current.gps}
                pathOptions={{ weight: 2, color: "#ff00aa", opacity: 1, fillColor: "#ff00aa", fillOpacity: 0.05 }}
                //zIndex={1}
            />
        );
    }

    return null;
};

export const displayIntersection = (intersections: OsmMapIntersectionsType | undefined) => {
    if (intersections === undefined) return null;
    // console.log("intersections", JSON.stringify(intersections));
    if (Utils.utilIsEmptyArray(intersections.list) === false) {
        return intersections.list
            .filter((intersection) => intersections.showEdge || intersection.region !== null)
            .map((intersection) => {
                let strokeColor;
                let fillColor;

                if (intersection.intersectionNo === intersections.selected) {
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

                if (intersection.gps === null) return null;

                return (
                    <Circle
                        key={intersection.intersectionNo}
                        center={intersection.gps}
                        radius={150}
                        pathOptions={{ weight: 1, color: strokeColor, opacity: 1, fillColor: fillColor, fillOpacity: 0.4 }}
                        //zIndex={5}
                        eventHandlers={{
                            click: () => {
                                intersections.clickEvent(intersection.intersectionNo);
                            },
                        }}
                    />
                );
            });
    }

    return null;
};

export const displayCamera = (cameras: OsmMapCamerasType, level: number) => {
    if (cameras === undefined) return null;

    // console.log("cameras", JSON.stringify(cameras));
    return cameras.list?.map((camera) => {
        const normalState = true; // TODO
        const isSelected = camera.cameraNo === cameras.selected;
        const imageUrl = "imgCamera" + (normalState ? "" : "_e") + "_" + (camera.degree ? camera.degree : "0") + (isSelected ? "_f" : "_n");

        const imageData = Utils.getCameraImageByKey(imageUrl);
        if (camera.gps === null) return null;

        const icon = L.icon({
            iconUrl: imageData,
            iconRetinaUrl: imageData,
            //iconAnchor: level < 4 ? [20, 20] : [15, 15],
            iconSize: level < 4 ? [40, 40] : [30, 30],
        });

        return (
            <Marker
                key={camera.cameraNo}
                position={camera.gps}
                icon={icon}
                eventHandlers={{
                    click: () => {
                        cameras.clickEvent(camera.cameraNo, camera.intersection.intersectionNo);
                    },
                }}
            />
        );
    });
};

//TODO kakaoMap -> osmMap
export const displayLinks = (links: OsmMapLinksType, level: number, kakaoMap?: kakao.maps.Map) => {
    if (links === undefined) return null;
    if (kakaoMap === undefined) return null;

    if (links.list) {
        //console.log("links", links);
        if (level >= 5) {
            return links.list.map((link, index) => {
                const qtsrlu = Utils.utilConvertQtsrlu15Minute(link.data[0].qtsrlu);
                const srlu = Utils.utilConvertSrlu15Minute(link.data[0].srlu);
                const speed = srlu / qtsrlu;
                const color = Utils.utilGetSpeedColor(speed);
                const key = link.link.startId + "_" + link.link.endId;

                return (
                    <div key={key}>
                        <Polyline
                            positions={link.link.gps}
                            pathOptions={{ weight: 8, color: Common.trafficColorBorder, opacity: 1 }}
                            //strokeStyle={"solid"}
                            //zIndex={2}
                        />
                        <Polyline
                            positions={link.link.gps}
                            pathOptions={{ weight: 4, color: color, opacity: 0.9 }}
                            /*endArrow={true}
                            strokeStyle={"solid"}
                            zIndex={3}*/
                        />
                    </div>
                );
            });
        } else {
            return links.list.map((link, index) => {
                const qtsr = Utils.utilConvertQtsrlu15Minute(link.data[0].qtsr);
                const sr = Utils.utilConvertSrlu15Minute(link.data[0].sr);
                const qtlu = Utils.utilConvertQtsrlu15Minute(link.data[0].qtlu);
                const lu = Utils.utilConvertSrlu15Minute(link.data[0].lu);
                const srSpeed = sr / qtsr;
                const luSpeed = lu / qtlu;

                const srColor = Utils.utilGetSpeedColor(srSpeed);
                const luColor = Utils.utilGetSpeedColor(luSpeed);

                const luPath = Utils.utilConvertParallelLines(kakaoMap, level, link.link.gps);

                const key = link.link.startId + "_" + link.link.endId;

                return (
                    <div key={key}>
                        {luPath && (
                            <Polyline
                                positions={luPath}
                                pathOptions={{ weight: 8, color: Common.trafficColorBorder, opacity: 1 }}
                                //strokeStyle={"solid"}
                                //zIndex={2}
                            />
                        )}
                        {luPath && (
                            <Polyline
                                positions={luPath}
                                pathOptions={{ weight: 4, color: luColor, opacity: 0.9 }}
                                //endArrow={true}
                                //strokeStyle={"solid"}
                                //zIndex={3}
                            />
                        )}
                        <Polyline
                            positions={link.link.gps}
                            pathOptions={{ weight: 8, color: Common.trafficColorBorder, opacity: 1 }}
                            //strokeStyle={"solid"}
                            //zIndex={2}
                        />
                        <Polyline
                            positions={link.link.gps}
                            pathOptions={{ weight: 4, color: srColor, opacity: 0.9 }}
                            //endArrow={true}
                            //strokeStyle={"solid"}
                            //zIndex={3}
                        />
                    </div>
                );
            });
        }
    }

    return null;
};

const getTrafficSignalType = (tsi: OsmMapTsiType, signalDatas: any) => {
    const displaySignals = signalDatas.tsiSignalInfos.filter((signalInfo: any) => signalInfo.info === "LEFT" || signalInfo.info === "STRAIGHT");

    const greenLeftSignals = displaySignals.filter((signalInfo: any) => signalInfo.info === "LEFT" && signalInfo.status === "GREEN");

    const greenStrightSignals = displaySignals.filter((signalInfo: any) => signalInfo.info === "STRAIGHT" && signalInfo.status === "GREEN");

    if (greenLeftSignals.length > 0 && greenStrightSignals.length > 0) {
        return "sl";
    } else if (greenLeftSignals.length > 0) {
        return "l";
    } else if (greenStrightSignals.length > 0) {
        return "s";
    }

    const redSignals = displaySignals.filter((signalInfo: any) => signalInfo.status === "RED");
    if (redSignals.length > 0) {
        return "r";
    }

    const yellowSignals = displaySignals.filter((signalInfo: any) => signalInfo.status === "YELLOW");
    if (yellowSignals.length > 0) {
        return "y";
    }

    const redFlashingSignals = displaySignals.filter((signalInfo: any) => signalInfo.status === "RED_FLAHING");
    if (redFlashingSignals.length > 0) {
        if (tsi.blink === true) {
            return "r";
        } else {
            return "d";
        }
    }

    const yellowFlashingSignals = displaySignals.filter((signalInfo: any) => signalInfo.status === "YELLOW_FLAHING");
    if (yellowFlashingSignals.length > 0) {
        if (tsi.blink === true) {
            return "y";
        } else {
            return "d";
        }
    }
    return "d";
};

const toRad = (brng: number) => {
    return (brng * Math.PI) / 180;
};

const toDeg = (brng: number) => {
    return (brng * 180) / Math.PI;
};

const getMoveGPSPosition = (gps: Common.GpsPosition, bearing: number, distance: number) => {
    let originalLat = gps.lat;
    let originalLng = gps.lng;

    distance = distance / 6371;
    bearing = toRad(bearing);

    originalLat = toRad(originalLat);
    originalLng = toRad(originalLng);

    const tranlateLat = Math.asin(Math.sin(originalLat) * Math.cos(distance) + Math.cos(originalLat) * Math.sin(distance) * Math.cos(bearing));

    const tranlateLng =
        originalLng +
        Math.atan2(
            Math.sin(bearing) * Math.sin(distance) * Math.cos(originalLat),
            Math.cos(distance) - Math.sin(originalLat) * Math.sin(tranlateLat)
        );

    if (isNaN(tranlateLat) || isNaN(tranlateLng)) return null;

    return { lat: toDeg(tranlateLat), lng: toDeg(tranlateLng) };
};

export const displayEditLinks = (editLinks: OsmMapEditLinksType) => {
    console.log("links", editLinks);

    if (editLinks === undefined) return null;

    if (editLinks.list) {
        //console.log("links", links);
        return editLinks.list.map((link, index) => {
            const data: any = [];
            link.gps.map((value: any) => data.push([value.lat, value.lng]));
            console.log("data", data);
            return (
                <div key={link.linkId}>
                    {link.linkId === editLinks.selected.linkId ? (
                        <>
                            <Polyline
                                positions={editLinks.selected.gps}
                                pathOptions={{ weight: 8, color: Common.trafficColorBorder, opacity: 1 }}
                                /*strokeStyle={"solid"}
                                zIndex={2}*/
                            />
                            <Polyline
                                positions={editLinks.selected.gps}
                                pathOptions={{ weight: 4, color: Common.trafficColorBorder, opacity: 0.9 }}
                                /*endArrow={true}
                                strokeStyle={"solid"}
                                zIndex={3}*/
                            />
                        </>
                    ) : (
                        <>
                            <Polyline
                                positions={data}
                                pathOptions={{ weight: 8, color: Common.trafficColorBorder, opacity: 1 }}
                                /*strokeStyle={"solid"}
                                zIndex={2}*/
                            />
                            <Polyline
                                positions={data}
                                pathOptions={{ weight: 4, color: Common.trafficColorNormal, opacity: 0.9 }}
                                /*endArrow={true}
                                strokeStyle={"solid"}
                                zIndex={3}*/
                            />
                        </>
                    )}
                </div>
            );
        });
    }

    return null;
};

export const displayTsi = (tsi: OsmMapTsiType) => {
    if (tsi === undefined) return null;

    const defaultTrafficeIntervalTime = 1000 * 60 * 1;
    if (Utils.utilIsEmptyArray(tsi.list) === false) {
        return tsi.list.map((tsiData) => {
            const currentDateTime = new Date();
            const lastSignalDateTime = new Date(tsiData.time);
            const signalDateInterval = currentDateTime.getTime() - lastSignalDateTime.getTime();

            let signalType = "d";
            if (tsiData.error !== 0) {
                signalType = "e";
            } else if (signalDateInterval > defaultTrafficeIntervalTime || tsiData.time === undefined) {
                signalType = "e";
            }

            signalType = "d"; // TODO: Code for Demo

            if (tsiData.nodeId === 2222) {
                return null;
            }

            //console.log("tsiData", tsiData);

            const datas = tsiData.tsiSignals.map((signalData: any) => {
                //console.log("signalData", signalData);
                if (signalType !== "e") {
                    signalType = getTrafficSignalType(tsi, signalData);
                }

                let imageUrl = "imgSignalLamp_";
                const signalDirectionType = signalData.direction / 45;
                let imageType = 4;
                if (signalType === "s" || signalType === "l" || signalType === "sl") {
                    if (signalDirectionType < 4) imageType = signalDirectionType + 4;
                    else imageType = signalDirectionType - 4;
                } else {
                    imageType = signalDirectionType % 2;
                }
                imageUrl += String(imageType) + "_" + signalType;
                const imageData = Utils.getTrafficLightImageByKey(imageUrl);

                const position = getMoveGPSPosition(tsiData.gps, signalData.direction, 0.05);

                if (position === null) return null;

                const icon = L.icon({
                    iconUrl: imageData,
                    iconRetinaUrl: imageData,
                    iconSize: [40, 40],
                });

                return <Marker key={tsiData.nodeId + "_" + signalData.direction} position={position} icon={icon} />;
            });

            return <div key={tsiData.nodeId}>{datas}</div>;
        });
    }

    return null;
};

export const displayAvl = (avl: OsmMapAvlType) => {
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

            const vehicleImageUrl = "imgEmergencyVehicle_" + statusInfo[status] + (avlData.carNo === avl.selected ? "_p" : "_n");
            const vehicleImageData = Utils.getEmergencyVehicleImageByKey(vehicleImageUrl);

            const icon = L.icon({
                iconUrl: vehicleImageData,
                iconRetinaUrl: vehicleImageData,
                iconSize: [40, 40],
            });

            //console.log(vehicleImageUrl);

            return (
                <div key={avlData.carNo}>
                    <Polyline
                        positions={naviPath}
                        pathOptions={{ weight: 8, color: Common.trafficColorBorder, opacity: 1 }}
                        /* strokeStyle={"solid"}
                        zIndex={8}*/
                    />
                    <Polyline
                        positions={naviPath}
                        pathOptions={{ weight: 4, color: "#306fd9", opacity: 0.9 }}
                        /*strokeStyle={"shortdot"} 
                        zIndex={8} */
                    />
                    <Marker
                        position={destPosition}
                        icon={icon}
                        //zIndex={8}
                    />
                    {currPosition && (
                        <Marker
                            position={currPosition}
                            icon={icon}
                            //zIndex={9}
                        />
                    )}
                </div>
            );
        });
    }

    return null;
};

function OsmMap({
    style,
    transitionState,
    region,
    intersections,
    cameras,
    links,
    editLinks,
    tsi,
    avl,
    center,
    zoomLevel,
    onChangedZoomLevel,
    onClickMap,
}: {
    style: OsmMapStyleType;
    transitionState?: string | undefined;
    region?: OsmMapRegionType | undefined;
    intersections?: OsmMapIntersectionsType | undefined;
    cameras?: OsmMapCamerasType | undefined;
    links?: OsmMapLinksType | undefined;
    editLinks?: OsmMapEditLinksType | undefined;
    tsi?: OsmMapTsiType | undefined;
    avl?: OsmMapAvlType | undefined;
    center?: kakaoMapCenterType | undefined;
    zoomLevel?: number | undefined;
    onChangedZoomLevel?: (level: number) => void;
    onClickMap?: (target: kakao.maps.Map, mouseEvent: kakao.maps.event.MouseEvent) => void;
}) {
    const [kakaoMap, setOsmMap] = useState<kakao.maps.Map>();
    const [level, setLevel] = useState<number>(7);

    const [tempCenter, setTempCenter] = useState<kakao.maps.LatLng>();

    useEffect(() => {
        if (transitionState === "entering" || transitionState === "exiting") {
            //console.log(transitionState, kakaoMap?.getCenter());
            setTempCenter(kakaoMap?.getCenter());
        } else if (transitionState === "entered" || transitionState === "exited") {
            if (tempCenter != undefined) {
                //console.log(transitionState, tempCenter);
                kakaoMap?.relayout();
                kakaoMap?.setCenter(tempCenter);
            }
        }
    }, [transitionState]);

    useEffect(() => {
        if (zoomLevel !== undefined) setLevel(zoomLevel);
    }, [zoomLevel]);

    const handleMap = (map: kakao.maps.Map) => {
        setOsmMap(map);
    };

    const onZoomChanged = (map: kakao.maps.Map) => {
        setLevel(map.getLevel());

        if (onChangedZoomLevel !== undefined) onChangedZoomLevel(map.getLevel());
    };

    const handleClick = (target: kakao.maps.Map, mouseEvent: kakao.maps.event.MouseEvent) => {
        if (onClickMap !== undefined) onClickMap(target, mouseEvent);
    };

    /*const mapEvents = useMapEvents({
        zoomend: () => {
            setLevel(mapEvents.getZoom());
            if (onChangedZoomLevel !== undefined) onChangedZoomLevel(mapEvents.getZoom());
        },
    });*/

    return (
        <MapContainer
            center={
                center === undefined || ""
                    ? {
                          lat: 35.85810060700929,
                          lng: 128.55729938820272,
                      }
                    : center
            }
            style={style}
            className={styles.darkMap}
            zoom={13}
            scrollWheelZoom={true}
            minZoom={8}
        >
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://cartodb-basemaps-{s}.global.ssl.fastly.net/dark_all/{z}/{x}/{y}.png"
            />
            {region?.isShow && displayRegion(region)}
            {displayIntersection(intersections)}
            {cameras?.isShow && displayCamera(cameras, level)}
            {links?.isShow && displayLinks(links, level, kakaoMap)}
            {/* {links?.isShow && kakaoMap !== undefined && displayLinks(links, kakaoMap, level)} */}
            {editLinks && displayEditLinks(editLinks)}
            {tsi?.isShow && displayTsi(tsi)}
            {avl?.isShow && displayAvl(avl)}
        </MapContainer>
    );
}

export default OsmMap;
