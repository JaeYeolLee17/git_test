import React, { useEffect, useState } from "react";
import TableManagement from "../component/TableManagement";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import KakaoMap from "../component/KakaoMap";
import styles from "../pages/ManagementCamera.module.css";
import * as Common from "../commons/common";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import editBtn from "../assets/images/btn_list_edit_n.svg";

import Grid from "@mui/material/Grid";
import { useNavigate } from "react-router-dom";

type rows = {
    id: string;
    region: string;
    intersection: string;
    cameraDirection: string;
};

type columns = {
    field: string;
    headerName: string;
    flex: number;
    cellRenderer: any;
};

function ManagementCamera() {
    const userDetails = useAuthState();
    const navigate = useNavigate();
    const [rows, setRows] = useState<rows[]>([]);
    const [listCamera, setListCamera] = useState<Array<any>>([]);
    const [selectedCameraNo, setSelectedCameraNo] = useState<string>("");
    const [zoomLevel, setZoomLevel] = useState<number>(6);

    const columns: columns[] = [
        {
            field: "id",
            headerName: "카메라 No.",
            flex: 2,
            cellRenderer: undefined,
        },
        {
            field: "region",
            headerName: "구역",
            flex: 2,
            cellRenderer: undefined,
        },
        {
            field: "intersection",
            headerName: "교차로",
            flex: 2,
            cellRenderer: undefined,
        },
        {
            field: "cameraDirection",
            headerName: "카메라 설치 방향",
            flex: 3,
            cellRenderer: undefined,
        },
        {
            field: "data",
            headerName: "",
            flex: 1,
            cellRenderer: (params: any) => {
                return (
                    <Button
                        onClick={(e) => {
                            navigate(Common.PAGE_MANAGEMENT_CAMERA_DETAIL, {
                                state: listCamera.find(function (data) {
                                    return data.cameraNo === params.data.id;
                                }),
                            });
                        }}
                    >
                        <img src={editBtn} width={20}></img>
                    </Button>
                );
            },
        },
    ];

    useEffect(() => {
        requestCameras();
    }, []);

    const requestAxiosCameras = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.CAMERA_LIST_URL
        );

        return response.data;
    };

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
            setRows((rows) => [
                ...rows,
                {
                    id: result.cameraNo,
                    region: result.intersection.region.regionName,
                    intersection: result.intersection.intersectionName,
                    cameraDirection: result.direction.intersectionName,
                },
            ]);
        });
    }, [resultCameras]);

    useEffect(() => {
        if (errorCameras === null) return;

        console.log("errorCameras", errorCameras);
    }, [errorCameras]);

    const handleClickCamera = (cameraNo: string) => {
        setSelectedCameraNo(cameraNo);
        setZoomLevel(3);
    };

    const onRowClick = (cameraNo: string) => {
        setSelectedCameraNo(cameraNo);
        setZoomLevel(3);
    };

    return (
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item md={7}>
                    <TableManagement
                        columns={columns}
                        rows={rows}
                        selectedId={selectedCameraNo}
                        clickEvent={onRowClick}
                    />
                </Grid>
                <Grid item md={5}>
                    <Box className={styles.box}>
                        <KakaoMap
                            style={{
                                width: "100%",
                                height: "100%",
                                zIndex: "0",
                            }}
                            cameras={{
                                list: listCamera,
                                isShow: true,
                                selected: selectedCameraNo,
                                clickEvent: handleClickCamera,
                            }}
                            zoomLevel={zoomLevel}
                            center={
                                selectedCameraNo === ""
                                    ? undefined
                                    : listCamera.find(function (data) {
                                          return (
                                              data.cameraNo === selectedCameraNo
                                          );
                                      }).gps
                            }
                        />
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}

export default ManagementCamera;
