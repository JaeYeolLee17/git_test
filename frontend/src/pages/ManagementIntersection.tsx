import React, { useEffect, useState } from "react";
import TableManagement from "../component/TableManagement";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import KakaoMap from "../component/KakaoMap";
import styles from "../pages/ManagementIntersection.module.css";
import * as Common from "../commons/common";

import Grid from "@mui/material/Grid";
import { useNavigate } from "react-router-dom";

type rows = {
    id: string;
    name: string;
    region: string;
};

type columns = {
    field: string;
    headerName: string;
    flex: number;
    renderCell: any;
};

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
            flex: 1,
            renderCell: undefined,
        },
        {
            field: "name",
            headerName: "교차로 이름",
            flex: 1,
            renderCell: undefined,
        },
        {
            field: "region",
            headerName: "구역",
            flex: 1,
            renderCell: undefined,
        },
        {
            field: "data",
            headerName: "",
            flex: 1,
            renderCell: (params: any) => {
                return (
                    <button
                        onClick={(e) => {
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
                        수정
                    </button>
                );
            },
        },
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
                        intersections={{
                            list: listIntersection,
                            selected: selectedintersectionNo,
                            clickEvent: handleClickIntersection,
                            showEdge: true,
                        }}
                    />
                </Grid>
            </Grid>
        </div>
    );
}

export default ManagementIntersection;
