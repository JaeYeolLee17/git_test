import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import KakaoMap from "../component/KakaoMap";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import Grid from '@mui/material/Grid';
import styles from './CameraDetail.module.css';
import * as Utils from "../utils/utils";
import * as Request from "../commons/request"
import * as Common from "../commons/common";
import Box from '@mui/material/Box';

type intersectionDataType = {
    distance: number,
    serverUrl: string,
    sendCycle: number,
    collectCycle: number 
}

function IntersectionDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [selectedIntersectionList, setSelectedIntersectionList] = useState<Array<any>>([]);
    const [intersectionData, setIntersectionData] = useState<any[]>([]);

    useEffect(() => {
        location.state !== null && onSelectedIntersection(location.state);
    }, [location.state])

    const onSelectedIntersection = (selectedIntersection: any) => {
        setSelectedIntersectionList(selectedIntersectionList => [...selectedIntersectionList, selectedIntersection]);
        
        setIntersectionData([{
            name: 'intersectionId',
            data: selectedIntersection.intersectionId,
            width:6,
            required: true,
            disabled: true
        },
        {
            name: '',
            data: '',
            width: 6,
            required: false,
            disabled: false
        },
        {
            name: 'intersectionName',
            data: selectedIntersection.intersectionName,
            width:6,
            required: true,
            disabled: false
        },
        {
            name: 'region',
            data: selectedIntersection.region.regionName,
            width:6,
            required: false,
            disabled: true
        },
        {
            name: 'gpsLat',
            data: selectedIntersection.gps.lat,
            width:6,
            required: true,
            disabled: true
        },
        {
            name: 'gpsLng',
            data: selectedIntersection.gps.lng,
            width:6,
            required: false,
            disabled: true
        }
        ]);
    }

    const requestAxiosUpdateIntersections = async(intersectionData: intersectionDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        console.log("intersectionData : " + intersectionData);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).post(
            Request.INTERSECTION_URL + "/" + selectedIntersectionList[0].intersectionId,
            { intersectionData }
        ); 

        return response.data;
    }

    const {
        loading: loadingIntersections,
        error: errorUpdateIntersections,
        data: resultUpdateIntersections,
        execute: requestUpdateIntersections,
    } = useAsyncAxios(requestAxiosUpdateIntersections);

    useEffect(() => {
        if (resultUpdateIntersections === null) return;
        
        navigate(Common.PAGE_MANAGEMENT_INTERSECTION);
        alert("수정되었습니다.");

    }, [resultUpdateIntersections]);

    useEffect(() => {
        if (errorUpdateIntersections === null) return;

        console.log("errorIntersections", errorUpdateIntersections);
    }, [errorUpdateIntersections]);


    const onChangedZoomLevel = (level: number) => {
        console.log("level", level);
    };

    const onClickEvent = (intersections :any) => {
        requestUpdateIntersections(intersections);
    };
    
    return(
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={8}>
                    <ManagementDetail response={intersectionData} clickEvent={onClickEvent}/>
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
                            region={undefined}
                            intersections={{
                                list: selectedIntersectionList,
                                selected: null,
                                clickEvent: () => {undefined},
                                showEdge: true,
                            }}
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

export default IntersectionDetail;