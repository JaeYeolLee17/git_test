import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import KakaoMap from "../component/KakaoMap";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import Grid from "@mui/material/Grid";
import styles from "./RegionDetail.module.css";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as Common from "../commons/common";
import Box from "@mui/material/Box";

type regionDataType = {
    distance: number;
    serverUrl: string;
    sendCycle: number;
    collectCycle: number;
};

function RegionDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [selectedRegionList, setSelectedRegionList] = useState<Array<any>>(
        []
    );
    const [regionData, setRegionData] = useState<any[]>([]);

    useEffect(() => {
        location.state !== null && onSelectedRegion(location.state);
    }, [location.state]);

    const onSelectedRegion = (selectedRegion: any) => {
        setSelectedRegionList((selectedRegionList) => [
            ...selectedRegionList,
            selectedRegion,
        ]);

        setRegionData([
            {
                name: "regionNo",
                data: selectedRegion.regionNo,
                width: 6,
                required: true,
                disabled: true,
            },
            {
                name: "regionName",
                data: selectedRegion.regionName,
                width: 6,
                required: true,
                disabled: false,
            },
        ]);
    };

    const requestAxiosUpdateRegions = async (regionData: regionDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).put(
            Request.REGION_URL + "/" + selectedRegionList[0].regionNo,
            regionData
        );

        return response.data;
    };

    const {
        loading: loadingRegions,
        error: errorUpdateRegions,
        data: resultUpdateRegions,
        execute: requestUpdateRegions,
    } = useAsyncAxios(requestAxiosUpdateRegions);

    useEffect(() => {
        if (resultUpdateRegions === null) return;

        navigate(Common.PAGE_MANAGEMENT_REGION);
        alert("수정되었습니다.");
    }, [resultUpdateRegions]);

    useEffect(() => {
        if (errorUpdateRegions === null) return;

        console.log("errorRegions", errorUpdateRegions);
    }, [errorUpdateRegions]);

    const onClickEvent = (region: any) => {
        const updatdData = {
            "regionNo": region.regionNo,
            "regionName": region.regionName,
          }

        requestUpdateRegions(updatdData);
    };

    return (
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={8}>
                    <ManagementDetail
                        pageType="edit"
                        response={regionData}
                        clickEvent={onClickEvent}
                    />
                </Grid>
                <Grid item xs={4}>
                    <Box className={styles.box}>
                        <KakaoMap
                            style={{
                                width: "100%",
                                height: "calc(100vh - 80px)",
                                zIndex: "0",
                            }}
                            region={{
                                current: {
                                    regionNo: "1",
                                    regionName: "1",
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

export default RegionDetail;
