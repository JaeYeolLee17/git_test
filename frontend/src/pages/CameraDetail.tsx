import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import KakaoMap from "../component/KakaoMap";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import Grid from "@mui/material/Grid";
import styles from "./CameraDetail.module.css";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as Common from "../commons/common";
import * as String from "../commons/string";

import Box from "@mui/material/Box";

type cameraDataType = {
    distance: number;
    serverUrl: string;
    sendCycle: number;
    collectCycle: number;
};

function CameraDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [selectedCameraList, setSelectedCameraList] = useState<Array<any>>(
        []
    );
    const [cameraData, setCameraData] = useState<any[]>([]);
    const [centerGps, setCenterGps] = useState<any>(undefined);

    useEffect(() => {
        const state = location.state;
        state !== null && onSelectedCamera(state);
    }, [location.state]);

    const onSelectedCamera = (selectedCamera: any) => {
        setSelectedCameraList((selectedCameraList) => [
            ...selectedCameraList,
            selectedCamera,
        ]);

        setCenterGps({ ...selectedCamera.gps });
        setCameraData([
            {
                name: "cameraNo",
                data: selectedCamera.cameraNo,
                width: 6,
                required: true,
                disabled: true,
            },
            {
                name: "empty",
                data: "",
                width: 6,
                required: false,
                disabled: false,
            },
            {
                name: "intersectionName",
                data:
                    selectedCamera.intersection === null
                        ? ""
                        : selectedCamera.intersection.intersectionName,
                width: 6,
                required: true,
                disabled: true,
            },
            {
                name: "directionName",
                data:
                    selectedCamera.direction === null
                        ? ""
                        : selectedCamera.direction.intersectionName,
                width: 6,
                required: true,
                disabled: true,
            },
            {
                name: "gpsLat",
                data: selectedCamera.gps === null ? "" : selectedCamera.gps.lat,
                width: 6,
                required: true,
                disabled: true,
            },
            {
                name: "gpsLng",
                data: selectedCamera.gps === null ? "" : selectedCamera.gps.lng,
                width: 6,
                required: true,
                disabled: true,
            },
            {
                name: "distance",
                data: selectedCamera.distance,
                width: 4,
                required: false,
                disabled: false,
            },
            {
                name: "empty2",
                data: "",
                width: 8,
                required: false,
                disabled: false,
            },
            {
                name: "rtspUrl",
                data: selectedCamera.rtspUrl,
                width: 4,
                required: false,
                disabled: true,
            },
            {
                name: "rtsId",
                data: selectedCamera.rtsId,
                width: 4,
                required: false,
                disabled: true,
            },
            {
                name: "rtsPassword",
                data: selectedCamera.rtsPassword,
                width: 4,
                required: false,
                disabled: true,
            },
            {
                name: "smallWidth",
                data: selectedCamera.smallWidth,
                width: 3,
                required: false,
                disabled: true,
            },
            {
                name: "smallHeight",
                data: selectedCamera.smallHeight,
                width: 3,
                required: false,
                disabled: true,
            },
            {
                name: "largeWidth",
                data: selectedCamera.largeWidth,
                width: 3,
                required: false,
                disabled: true,
            },
            {
                name: "largeHeight",
                data: selectedCamera.largeHeight,
                width: 3,
                required: false,
                disabled: true,
            },
            {
                name: "serverUrl",
                data: selectedCamera.serverUrl,
                width: 4,
                required: false,
                disabled: false,
            },
            {
                name: "sendCycle",
                data: selectedCamera.sendCycle,
                width: 4,
                required: false,
                disabled: false,
            },
            {
                name: "collectCycle",
                data: selectedCamera.collectCycle,
                width: 4,
                required: false,
                disabled: false,
            },
            {
                name: "startLine",
                data:
                    selectedCamera.road === null
                        ? ""
                        : selectedCamera.road.startLine,
                width: 4,
                required: false,
                disabled: true,
            },
            {
                name: "uturn",
                data:
                    selectedCamera.road === null
                        ? ""
                        : selectedCamera.road.uturn,
                width: 4,
                required: false,
                disabled: true,
            },
            {
                name: "crosswalk",
                data:
                    selectedCamera.road === null
                        ? ""
                        : selectedCamera.road.crosswalk,
                width: 4,
                required: false,
                disabled: true,
            },
            {
                name: "lane",
                data:
                    selectedCamera.road === null
                        ? ""
                        : selectedCamera.road.lane,
                width: 4,
                required: false,
                disabled: true,
            },
        ]);
    };

    const title: Map<string, string> = new Map([
        ["cameraNo", String.camera_id],
        ["intersectionName", String.camera_intersection_name],
        ["directionName", String.direction_name],
        ["gpsLat", String.camera_gps_lat],
        ["gpsLng", String.camera_gps_lng],
        ["distance", String.distance],
        ["rtspUrl", String.rtsp_url],
        ["rtsId", String.rts_id],
        ["rtsPassword", String.rts_password],
        ["smallWidth", String.small_width],
        ["smallHeight", String.small_height],
        ["largeWidth", String.large_width],
        ["largeHeight", String.large_height],
        ["serverUrl", String.server_url],
        ["sendCycle", String.send_cycle],
        ["collectCycle", String.collect_cylcle],
        ["startLine", String.start_line],
        ["uturn", String.u_turn],
        ["crosswalk", String.crosswalk],
        ["lane", String.lane],
    ]);

    const requestAxiosUpdateCameras = async (updateData: cameraDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).put(
            Request.CAMERA_URL + "/" + selectedCameraList[0].cameraNo,
            updateData
        );

        return response.data;
    };

    const {
        loading: loadingCameras,
        error: errorUpdateCameras,
        data: resultUpdateCameras,
        execute: requestUpdateCameras,
    } = useAsyncAxios(requestAxiosUpdateCameras);

    useEffect(() => {
        if (resultUpdateCameras === null) return;

        navigate(Common.PAGE_MANAGEMENT_CAMERA);
        alert("수정되었습니다.");
    }, [resultUpdateCameras]);

    useEffect(() => {
        if (errorUpdateCameras === null) return;

        console.log("errorCameras", errorUpdateCameras);
    }, [errorUpdateCameras]);

    const onClickEvent = (type: string, camera: any) => {
        const updateData = {
            cameraNo: camera.cameraNo,
            intersection: {
                intersectionNo:
                    selectedCameraList[0].intersection.intersectionNo,
                intersectionName: camera.intersectionName,
            },
            direction: {
                intersectionNo: selectedCameraList[0].direction.intersectionNo,
                intersectionName: camera.directionName,
            },
            gps: {
                lat: camera.gpsLat,
                lng: camera.gpsLng,
            },
            distance: camera.distance,
            rtspUrl: camera.rtspUrl,
            rtspId: camera.rtsId,
            rtspPassword: camera.rtspPassword,
            serverUrl: camera.serverUrl,
            sendCycle: camera.sendCycle,
            collectCycle: camera.collectCycle,
            smallWidth: camera.smallWidth,
            smallHeight: camera.smallHeight,
            largeWidth: camera.largeWidth,
            largeHeight: camera.largeHeight,
            road: {
                startLine: camera.startLine,
                lane: camera.lane,
                uturn: camera.uturn,
                crosswalk: camera.crosswalk,
            },
        };

        requestUpdateCameras(updateData);
    };

    const handleClickMap = (
        target: kakao.maps.Map,
        mouseEvent: kakao.maps.event.MouseEvent
    ) => {
        setSelectedCameraList((prevState) => {
            const newState = prevState.map((obj) => {
                obj.gps.lat = mouseEvent.latLng.getLat();
                obj.gps.lng = mouseEvent.latLng.getLng();
                return obj;
            });

            return newState;
        });

        setCameraData((prevState) => {
            const newState = prevState.map((obj) => {
                if (obj.name === "gpsLat") {
                    return { ...obj, data: mouseEvent.latLng.getLat() };
                } else if (obj.name === "gpsLng") {
                    return { ...obj, data: mouseEvent.latLng.getLng() };
                }

                return obj;
            });

            return newState;
        });
    };

    return (
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={7}>
                    <ManagementDetail
                        type='edit'
                        title={title}
                        response={cameraData}
                        clickEvent={onClickEvent}
                    />
                </Grid>
                <Grid item xs={5}>
                    <Box className={styles.box}>
                        <KakaoMap
                            style={{
                                width: "100%",
                                height: "calc(100vh - 80px)",
                                zIndex: "0",
                            }}
                            cameras={{
                                list: selectedCameraList,
                                isShow: true,
                                selected: null,
                                clickEvent: () => {
                                    undefined;
                                },
                            }}
                            center={{
                                lat: centerGps !== undefined && centerGps.lat,
                                lng: centerGps !== undefined && centerGps.lng,
                            }}
                            zoomLevel={3}
                            onClickMap={handleClickMap}
                        />
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}

export default CameraDetail;
