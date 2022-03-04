import React, { useState } from "react";
import { Map, MapMarker, Polygon, Circle } from "react-kakao-maps-sdk";

function KakaoMap({ style, region, intersections, cameras }) {
    // const { kakao } = window;
    // let map = null;

    // useEffect(() => {
    //     const mapContainer = document.getElementById("map");

    //     const baseMapOption = {
    //         center: new kakao.maps.LatLng(
    //             35.85810060700929,
    //             128.55729938820272
    //         ),
    //         level: 7,
    //         maxLevel: 12,
    //     };

    //     map = new kakao.maps.Map(mapContainer, baseMapOption);
    // }, []);

    //console.log(cameras.list);

    const [level, setLevel] = useState();

    //console.log("region", region);

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

    const displayCamera = (camera, index) => {
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
    };

    return (
        <Map
            center={{
                // 지도의 중심좌표
                lat: 35.85810060700929,
                lng: 128.55729938820272,
            }}
            style={style}
            level={7}
            onZoomChanged={(map) => setLevel(map.getLevel())}
        >
            {region.isShow && displayRegion()}
            {displayIntersection()}
            {cameras.isShow && cameras.list?.map(displayCamera)}
        </Map>
    );
}

export default KakaoMap;
