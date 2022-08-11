import React, { useEffect, useState } from "react";
import TableManagement from "../component/TableManagement";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import KakaoMap from "../component/KakaoMap";
import styles from "../pages/ManagementIntersection.module.css";
import * as Common from "../commons/common";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import editBtn from "../assets/images/btn_list_edit_n.svg"

import Grid from "@mui/material/Grid";
import { useNavigate } from "react-router-dom";

type rows = {
    id: string,
    name: string,
    region: string
}

type columns ={
    field: string,
    headerName: string,
    flex: number,
    cellRenderer: any
  }

function ManagementIntersection() {
    const userDetails = useAuthState();
    const navigate = useNavigate();
    const [rows, setRows] = useState<rows[]>([]);
    const [listIntersection, setListIntersection] = useState<Array<any>>([]);
    const [selectedintersectionNo, setSelectedIntersectionNo] = useState<string>("");

    const columns: columns[] = [
        {
            field: "id",
            headerName: "교차로 No.",
            flex: 2,
            cellRenderer: undefined
        },
        {
            field: "name",
            headerName: "교차로 이름",
            flex: 3,
            cellRenderer: undefined
        },
        {
            field: "region",
            headerName: "구역",
            flex: 3,
            cellRenderer: undefined
        },
        {
            field: "data",
            headerName: "",
            flex: 1,
            cellRenderer: (params :any) => {
                return (
                    <Button onClick={(e) => {
                        navigate(
                            Common.PAGE_MANAGEMENT_INTERSECTION_DETAIL,
                            {
                                state: listIntersection.find(function (
                                    data
                                ) {
                                    return (
                                        data.intersectionNo === params.data.id
                                    );
                                }),
                            }
                        );
                    }}
                >
                        <img src={editBtn} width={20}/>
                    </Button>
                )
            }
        }
      ];

    useEffect(() => {
        requestIntersections();
    }, []);

    const requestAxiosIntersections = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.INTERSECTION_LIST_URL
        );

        return response.data;
    };

    const {
        loading: loadingIntersections,
        error: errorIntersections,
        data: resultIntersections,
        execute: requestIntersections,
    } = useAsyncAxios(requestAxiosIntersections);

    useEffect(() => {
        if (resultIntersections === null) return;

        setListIntersection(resultIntersections.intersections);

        resultIntersections.intersections.map((result: any) => {
            setRows((rows) => [
                ...rows,
                {
                    id: result.intersectionNo,
                    name: result.intersectionName,
                    region:
                        result.region === null ? "-" : result.region.regionName,
                },
            ]);
        });
    }, [resultIntersections]);

    useEffect(() => {
        if (errorIntersections === null) return;

        console.log("errorIntersection", errorIntersections);
    }, [errorIntersections]);

    const handleClickIntersection = (intersectionNo: string) => {
        setSelectedIntersectionNo(intersectionNo);
    };

    const onRowClick = (intersectionId: string) => {
        setSelectedIntersectionNo(intersectionId);
    };

    return (
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={7}>
                    <TableManagement 
                        columns={columns} 
                        rows={rows}
                        selectedId={selectedintersectionNo}
                        clickEvent={onRowClick}
                    />
                </Grid>
                <Grid item xs={5}>
                    <Box className={styles.box}>
                    <KakaoMap
                        style={{
                            width: "100%",
                            height: "100%",
                            zIndex: "0",
                        }}
                        intersections={{
                            list: listIntersection,
                            selected: selectedintersectionNo,
                            clickEvent: handleClickIntersection,
                            showEdge: true,
                        }}
                        zoomLevel={6}
                        center={{
                            lat: 35.8795650131115,
                            lng: 128.571339656397
                        }}
                    />
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}

export default ManagementIntersection;