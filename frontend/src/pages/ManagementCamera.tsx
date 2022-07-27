import React, { useEffect, useState } from "react"
import TableManagement from "../component/TableManagement"
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils"
import * as Request from "../commons/request"
import KakaoMap from "../component/KakaoMap";
import styles from "../pages/ManagementCamera.module.css"
import * as Common from "../commons/common";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import editBtn from '../assets/images/btn_list_edit_n.svg'

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
    headerAlign: string,
    align: string,
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
            headerAlign: 'center',
            align: 'center',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'region',
            headerName: '구역',
            headerAlign: 'center',
            align: 'center',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'intersection',
            headerName: '교차로',
            headerAlign: 'center',
            align: 'center',
            flex: 1,            
            renderCell: undefined
        },
        {
            field: 'cameraDirection',
            headerName: '카메라 설치 방향',
            headerAlign: 'center',
            align: 'center',
            flex: 2,
            renderCell: undefined
        },
        {
            field: 'data',
            headerName: '',
            headerAlign: 'center',
            align: 'center',
            flex: 0.5,
            renderCell: (params :any) => {
                return (
                    <Button
                        onClick={(e) => {
                            navigate(
                                Common.PAGE_MANAGEMENT_CAMERA_DETAIL, {
                                state :listCamera.find(function(data){ return data.cameraId === params.id })})
                        }
                    }>
                        <img src={editBtn} width={20}></img>
                    </Button>
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
            Request.CAMERA_LIST_URL
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
            setRows(rows => 
                [...rows, 
                    {
                        id: result.cameraId, 
                        region: result.intersection.region.regionName, 
                        intersection: result.intersection.intersectionName, 
                        cameraDirection: result.direction.intersectionName
                    }
                ]
            );
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
                <Grid item xs={7}>
                    <TableManagement 
                            columns={columns} 
                            rows={rows} 
                            clickEvent={onRowClick}
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
                    </Box>
                </Grid>
            </Grid>
        </div>
    )
}

export default ManagementCamera