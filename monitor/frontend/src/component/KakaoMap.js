import React, { useState, useEffect } from "react";
import {
    Map,
    MapMarker,
    Polygon,
    Circle,
    Polyline,
    useMap,
} from "react-kakao-maps-sdk";
import * as Utils from "../utils/utils";
import * as Common from "../common";

function KakaoMap({ style, region, intersections, cameras, links }) {
    const [kakaoMap, setKakaoMap] = useState(null);
    const [level, setLevel] = useState(7);

    const displayRegion = () => {
        if (region.current) {
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

    const displayIntersection = () => {
        if (intersections.list) {
            return intersections.list
                .filter(
                    (intersection) =>
                        intersections.showEdge || intersection.region !== null
                )
                .map((intersection) => {
                    let strokeColor;
                    let fillColor;

                    if (
                        intersection.intersectionId === intersections.selected
                    ) {
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
                            onClick={(marker) => {
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

    const displayCamera = () => {
        return cameras.list?.map((camera) => {
            let normalState = true; // TODO
            let isSelected = camera.cameraId === cameras.selected;
            let imageUrl =
                "/images/btn_map_cctv" +
                (normalState ? "" : "_e") +
                "_40_" +
                (camera.degree ? camera.degree : "0") +
                (isSelected ? "_f" : "_n") +
                ".png";

            return (
                <MapMarker
                    key={camera.cameraId}
                    position={camera.gps}
                    image={{
                        src: `${imageUrl}`,
                        size: {
                            widht: level < 4 ? 40 : 30,
                            height: level < 4 ? 40 : 30,
                        },
                        options: {
                            offset: {
                                x: level < 4 ? 20 : 15,
                                y: level < 4 ? 20 : 15,
                            },
                        },
                    }}
                    onClick={(marker) => {
                        cameras.clickEvent(
                            camera.cameraId,
                            camera.intersection.intersectionId
                        );
                    }}
                />
            );
        });
    };

    const displayLinks = () => {
        if (links.list) {
            console.log("links", links);
            if (level >= 5) {
                return links.list.map((link, index) => {
                    let qtsrlu = (link.data[0].qtsrlu * 4) / 3600;
                    let srlu = (link.data[0].srlu * 100 * 4) / 1000;
                    let speed = (srlu / qtsrlu).toFixed(2);
                    let color = Utils.utilGetSpeedColor(speed);

                    return (
                        <>
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
                        </>
                    );
                });
            } else {
                return links.list.map((link, index) => {
                    let qtsr = (link.data[0].qtsr * 4) / 3600;
                    let sr = (link.data[0].sr * 100 * 4) / 1000;

                    let qtlu = (link.data[0].qtlu * 4) / 3600;
                    let lu = (link.data[0].lu * 100 * 4) / 1000;

                    let srSpeed = (sr / qtsr).toFixed(2);
                    let luSpeed = (lu / qtlu).toFixed(2);

                    let srColor = Utils.utilGetSpeedColor(srSpeed);
                    let luColor = Utils.utilGetSpeedColor(luSpeed);

                    let luPath = Utils.utilConvertParallelLines(
                        kakaoMap,
                        level,
                        link.link.gps
                    );

                    return (
                        <>
                            <Polyline
                                path={luPath}
                                strokeWeight={8} // 선의 두께 입니다
                                strokeColor={Common.trafficColorBorder} // 선의 색깔입니다
                                strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                                strokeStyle={"solid"} // 선의 스타일입니다
                                zIndex={2}
                            />
                            <Polyline
                                path={luPath}
                                endArrow={true}
                                strokeWeight={4} // 선의 두께 입니다
                                strokeColor={luColor} // 선의 색깔입니다
                                strokeOpacity={0.9} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                                strokeStyle={"solid"} // 선의 스타일입니다
                                zIndex={3}
                            />

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
                        </>
                    );
                });
            }
        }

        return null;
    };

    const handleMap = (map) => {
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
            {region.isShow && displayRegion()}
            {displayIntersection()}
            {cameras.isShow && displayCamera()}
            {links.isShow && displayLinks()}
        </Map>
    );
}

export default KakaoMap;
