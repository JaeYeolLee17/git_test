import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import KakaoMap from "../component/KakaoMap";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import Grid from '@mui/material/Grid';
import styles from './RegionDetail.module.css';
import * as Utils from "../utils/utils";
import * as Request from "../commons/request"
import * as Common from "../commons/common";
import Box from '@mui/material/Box';

type regionDataType = {
    distance: number,
    serverUrl: string,
    sendCycle: number,
    collectCycle: number 
}

function RegionDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [selectedRegionList, setSelectedRegionList] = useState<Array<any>>([]);
    const [regionData, setRegionData] = useState<any[]>([]);

    useEffect(() => {
        location.state !== null && onSelectedRegion(location.state);
    }, [location.state])

    const onSelectedRegion = (selectedRegion: any) => {
        setSelectedRegionList(selectedRegionList => [...selectedRegionList, selectedRegion]);
        
        setRegionData([{
            name: 'regionId',
            data: selectedRegion.regionId,
            width:6,
            required: true,
            disabled: true
        },
        {
            name: 'regionName',
            data: selectedRegion.regionName,
            width:6,
            required: true,
            disabled: false
        }
        ]);
    }

    const requestAxiosUpdateRegions = async(regionData: regionDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        console.log("regionData : " + regionData);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).post(
            Request.REGION_URL + "/" + selectedRegionList[0].regionId,
            { regionData }
        ); 

        return response.data;
    }

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


    const onChangedZoomLevel = (level: number) => {
        console.log("level", level);
    };

    const onClickEvent = (regions :any) => {
        requestUpdateRegions(regions);
    };
    
    return(
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={8}>
                    <ManagementDetail response={regionData} clickEvent={onClickEvent}/>
                </Grid>
                <Grid item xs={4}>
                    <Box className={styles.box}>
                        <KakaoMap
                            style={{
                                width: "100%",
                                height: "calc(100vh - 80px)",
                                zIndex: "0",
                            }}
                            transitionState={undefined}
                            region={{
                                current: {
                                    regionId: '1',
                                    regionName: '1',
                                    gps: [{
                                        lat: 128.3,
                                        lng: 38
                                    }]
                                },
                                isShow: true
                            }}
                            intersections={undefined}
                            cameras={undefined}
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

export default RegionDetail;