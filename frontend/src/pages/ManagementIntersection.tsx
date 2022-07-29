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
    headerAlign: string,
    align: string,
    flex: number,
    renderCell: any
  }

function ManagementIntersection() {
    const userDetails = useAuthState();
    const navigate = useNavigate();
    const [rows, setRows] = useState<rows[]>([]);
    const [listIntersection, setListIntersection] = useState<Array<any>>([]);
    const [selectedintersectionNo, setSelectedIntersectionNo] = useState<
        string | null
    >("");

    const columns: columns[] = [
        {
            field: "id",
            headerName: "교차로 No.",
            headerAlign: "center",
            align: "center",
            flex: 2,
            renderCell: undefined
        },
        {
            field: "name",
            headerName: "교차로 이름",
            headerAlign: "center",
            align: "center",
            flex: 3,
            renderCell: undefined
        },
        {
            field: "region",
            headerName: "구역",
            headerAlign: "center",
            align: "center",
            flex: 3,
            renderCell: undefined
        },
        {
            field: "data",
            headerName: "",
            headerAlign: "center",
            align: "center",
            flex: 1,
            renderCell: (params: any) => {
                return (
                    <Button onClick={(e) => {
                        navigate(
                            Common.PAGE_MANAGEMENT_INTERSECTION_DETAIL,
                            {
                                state: listIntersection.find(function (
                                    data
                                ) {
                                    return (
                                        data.intersectionNo === params.id
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
        alert(intersectionNo);
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
                        intersections={{
                            list: listIntersection,
                            selected: selectedintersectionNo,
                            clickEvent: handleClickIntersection,
                            showEdge: true,
                        }}
                    />
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}

export default ManagementIntersection;
