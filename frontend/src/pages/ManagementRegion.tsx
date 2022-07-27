import React, { useEffect, useState } from "react";
import TableManagement from "../component/TableManagement";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import KakaoMap from "../component/KakaoMap";
import styles from "../pages/ManagementRegion.module.css";
import * as Common from "../commons/common";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import editBtn from "../assets/images/btn_list_edit_n.svg"

import Grid from "@mui/material/Grid";
import { useNavigate } from "react-router-dom";

type rows = {
    id: string;
    name: string;
};

type columns ={
    field: string,
    headerName: string,
    headerAlign: string,
    align: string,
    flex: number,
    renderCell: any
  }

function ManagementRegion() {
    const userDetails = useAuthState();
    const navigate = useNavigate();
    const [rows, setRows] = useState<rows[]>([]);
    const [listRegion, setListRegion] = useState<Array<any>>([]);
    const [selectedRegionNo, setSelectedRegionNo] = useState<string | null>("");

    const columns: columns[] = [
        {
            field: "id",
            headerName: "구역 No.",
            headerAlign: "center",
            align: "center",
            flex: 2,
            renderCell: undefined
        },
        {
            field: "name",
            headerName: "구역 이름",
            headerAlign: "center",
            align: "center",
            flex: 2,
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
                        navigate(Common.PAGE_MANAGEMENT_REGION_DETAIL, {
                            state: listRegion.find(function (data) {
                                return data.regionNo === params.id;
                            }),
                        });
                    }}
                    >
                        <img src={editBtn} width={20}/>
                    </Button>
                )
            }
        }
      ];

    useEffect(() => {
        requestRegions();
    }, []);

    const requestAxiosRegions = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.REGIONS_LIST_URL
        );

        return response.data;
    };

    const {
        loading: loadingRegions,
        error: errorRegions,
        data: resultRegions,
        execute: requestRegions,
    } = useAsyncAxios(requestAxiosRegions);

    useEffect(() => {
        if (resultRegions === null) return;

        setListRegion(resultRegions.regions);

        resultRegions.regions.map((result: any) => {
            setRows((rows) => [
                ...rows,
                {
                    id: result.regionNo,
                    name: result.regionName,
                },
            ]);
        });
    }, [resultRegions]);

    useEffect(() => {
        if (errorRegions === null) return;

        console.log("errorRegions", errorRegions);
    }, [errorRegions]);

    const handleClickRegion = (regionNo: string) => {
        setSelectedRegionNo(regionNo);
        alert(regionNo);
    };

    const onRowClick = (regionNo: string) => {
        setSelectedRegionNo(regionNo);
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
                            region={{
                                current: {
                                    regionNo: "test",
                                    regionName: "test",
                                    gps: [
                                        {
                                            lat: 128.3,
                                            lng: 38,
                                        },
                                    ],
                                },
                                isShow: true,
                            }}
                        />
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}

export default ManagementRegion;
