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

    useEffect(() => {
        location.state !== null && onSelectedCamera(location.state);
    }, [location.state]);

    const onSelectedCamera = (selectedCamera: any) => {
        setSelectedCameraList(selectedCameraList => [...selectedCameraList, selectedCamera]);
        
        setCameraData([{
            name: "cameraId",
            data: selectedCamera.cameraId,
            width: 6,
            required: true,
            disabled: true
        },
        {
            name: "",
            data: "",
            width: 6,
            required: false,
            disabled: false
        },
        {
            name: "intersectionName",
            data: selectedCamera.intersection.intersectionName,
            width:6,
            required: true,
            disabled: true
        },
        {
            name: "directionName",
            data: selectedCamera.direction.intersectionName,
            width: 6,
            required: true,
            disabled: true
        },
        {
            name: "gpsLat",
            data: selectedCamera.gps.lat,
            width: 6,
            required: true,
            disabled: true
        },
        {
            name: "gpsLng",
            data: selectedCamera.gps.lng,
            width: 6,
            required: false,
            disabled: true
        },
        {
            name: "distance",
            data: selectedCamera.distance,
            width: 4,
            required: false,
            disabled: false
        },
        {
            name: "",
            data: "",
            width: 8,
            required: false,
            disabled: false
        },
        {
            name: "rtspUrl",
            data: selectedCamera.rtspUrl,
            width: 4,
            required: false,
            disabled: true
        },
        {
            name: "rtsId",
            data: selectedCamera.rtsId,
            width: 4,
            required: false,
            disabled: true
        },
        {
            name: "rtsPassword",
            data: selectedCamera.rtsPassword,
            width: 4,
            required: false,
            disabled: true
        },
        {
            name: "smallWidth",
            data: selectedCamera.smallWidth,
            width: 3,
            required: false,
            disabled: true
        },
        {
            name: "smallHeight",
            data: selectedCamera.smallHeight,
            width: 3,
            required: false,
            disabled: true
        },
        {
            name: "largeWidth",
            data: selectedCamera.largeWidth,
            width: 3,
            required: false,
            disabled: true
        },
        {
            name: "largeHeight",
            data: selectedCamera.largeHeight,
            width: 3,
            required: false,
            disabled: true
        },
        {
            name: "serverUrl",
            data: selectedCamera.serverUrl,
            width: 4,
            required: false,
            disabled: false
        },
        {
            name: "sendCycle",
            data: selectedCamera.sendCycle,
            width: 4,
            required: false,
            disabled: false
        },
        {
            name: "collectCycle",
            data: selectedCamera.collectCycle,
            width: 4,
            required: false,
            disabled: false
        },
        {
            name: "startLine",
            data: selectedCamera.roadInfo.startLine,
            width: 4,
            required: false,
            disabled: true
        },
        {
            name: "uTurn",
            data: selectedCamera.roadInfo.uTurn,
            width: 4,
            required: false,
            disabled: true
        },
        {
            name: "crosswalk",
            data: selectedCamera.roadInfo.crosswalk,
            width: 4,
            required: false,
            disabled: true
        },
        {
            name: "lane",
            data: selectedCamera.roadInfo.lane,
            width: 4,
            required: false,
            disabled: true
        },
        ]);
    };

    const requestAxiosUpdateCameras = async (cameraData: cameraDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        //console.log("cameraData : " + cameraData);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).post(
            Request.CAMERA_URL + "/" + selectedCameraList[0].cameraNo,
            { cameraData }
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

    const onClickEvent = (cameras: any) => {
        requestUpdateCameras(cameras);
    };

    return (
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={8}>
                    <ManagementDetail
                        response={cameraData}
                        clickEvent={onClickEvent}
                    />
                </Grid>
                <Grid item xs={4}>
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
                    />
                </Grid>
            </Grid>
        </div>
    );
}

export default CameraDetail;
