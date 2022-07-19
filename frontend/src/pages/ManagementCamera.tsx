import React, { useEffect, useState } from "react"
import TableManagement from "../component/TableManagement"
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils"
import * as Request from "../commons/request"
import KakaoMap from "../component/KakaoMap";
import styles from "../pages/ManagementCamera.module.css"
import * as Common from "../commons/common";

import Grid from '@mui/material/Grid';
import { useNavigate } from "react-router-dom";

type rows = {
    id: string,
    region: string,
    intersection: string,
    cameraDirection: string
}

type columns ={
    field: string,
    headerName: string,
    flex: number,
    renderCell: any
  }

function ManagementCamera() {
    const userDetails = useAuthState();
    const navigate = useNavigate();
    const [rows, setRows] = useState<rows[]>([]);
    const [listCamera, setListCamera] = useState<Array<any>>([]);
    const [selectedCameraId, setSelectedCameraId] = useState<string | null>("");
    const [mapZoomLevel, setMapZoomLevel] = useState<number>(7);
    
    const columns: columns[] = [
        {
            field: 'id',
            headerName: '카메라 ID',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'region',
            headerName: '구역',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'intersection',
            headerName: '교차로',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'cameraDirection',
            headerName: '카메라 설치 방향',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'data',
            headerName: '',
            flex: 1,
            renderCell: (params :any) => {
                return (
                    <button onClick={(e) => {
                        navigate(
                            Common.PAGE_MANAGEMENT_CAMERA_DETAIL, {
                            state :listCamera.find(function(data){ return data.cameraId === params.id })})
                        }
                    }>
                        수정
                    </button>
                )
            }
        }
      ];

    useEffect(() => {
        requestCameras();
    }, []);

    const requestAxiosCameras = async() => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.CAMERA_URL
        );

        return response.data;
    }

    const {
        loading: loadingCameras,
        error: errorCameras,
        data: resultCameras,
        execute: requestCameras,
    } = useAsyncAxios(requestAxiosCameras);

    useEffect(() => {
        if (resultCameras === null) return;

        setListCamera(resultCameras.cameras);

        resultCameras.cameras.map((result: any) => { 
            setRows(rows => [...rows, {id: result.cameraId, region: result.intersection.region.regionName, intersection: result.intersection.intersectionName, cameraDirection: result.direction.intersectionName}]);
        })

    }, [resultCameras]);

    useEffect(() => {
        if (errorCameras === null) return;

        console.log("errorCameras", errorCameras);
    }, [errorCameras]);

    const handleClickCamera = (cameraId: string) => {
        setSelectedCameraId(cameraId);
        alert(cameraId);
    };

    
    const onChangedZoomLevel = (level: number) => {
        console.log("level", level);
        setMapZoomLevel(level);
    };

    const onRowClick = (cameraId: string) => {
        setSelectedCameraId(cameraId);
    };

    return(
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={6}>
                    <TableManagement 
                        columns={columns} 
                        rows={rows} 
                        clickEvent={onRowClick}
                    />
                </Grid>
                <Grid item xs={6}>
                    <KakaoMap
                        style={{
                            width: "100%",
                            height: "calc(100vh - 80px)",
                            zIndex: "0",
                        }}
                        transitionState={undefined}
                        region={undefined}
                        intersections={undefined}
                        cameras={{
                            list: listCamera,
                            isShow: true,
                            selected: selectedCameraId,
                            clickEvent: handleClickCamera,
                        }}
                        links={undefined}
                        trafficLights={undefined}
                        avl={undefined}
                        zoomLevel={undefined}
                        onChangedZoomLevel={onChangedZoomLevel}
                    />
                </Grid>
            </Grid>
        </div>
    )
}

export default ManagementCamera