import React, { useState } from "react";
import { Map, MapMarker, useMap } from "react-kakao-maps-sdk";

function KakaoMap({ style, cameras }) {
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
                    cameras.clickEvent(camera.cameraId);
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
            {cameras.list?.map(displayCamera)}
        </Map>
    );
}

export default KakaoMap;
