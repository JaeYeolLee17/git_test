import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import KakaoMap from "../component/KakaoMap";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import Grid from "@mui/material/Grid";
import styles from "./CameraDetail.module.css";
import Box from "@mui/material/Box";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as Common from "../commons/common";
import * as String from "../commons/string";
import OsmMap from "../component/OsmMap";

type intersectionDataType = {
    distance: number;
    serverUrl: string;
    sendCycle: number;
    collectCycle: number;
};

function IntersectionDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [selectedIntersectionList, setSelectedIntersectionList] = useState<Array<any>>([]);
    const [intersectionData, setIntersectionData] = useState<any[]>([]);

    const [centerGps, setCenterGps] = useState<any>(undefined);

    useEffect(() => {
        location.state !== null && onSelectedIntersection(location.state);
    }, [location.state]);

    const onSelectedIntersection = (selectedIntersection: any) => {
        setSelectedIntersectionList((selectedIntersectionList) => [...selectedIntersectionList, selectedIntersection]);

        setCenterGps({ ...selectedIntersection.gps });

        setIntersectionData([
            {
                name: "intersectionNo",
                data: selectedIntersection.intersectionNo,
                width: 12,
                required: true,
                disabled: true,
            },
            {
                name: "intersectionName",
                data: selectedIntersection.intersectionName,
                width: 12,
                required: true,
                disabled: false,
            },
            {
                name: "regionName",
                data: selectedIntersection.region === null ? "" : selectedIntersection.region.regionName,
                width: 12,
                required: false,
                disabled: true,
            },
            {
                name: "gpsLat",
                data: selectedIntersection.gps === null ? "" : selectedIntersection.gps.lat,
                width: 12,
                required: true,
                disabled: true,
            },
            {
                name: "gpsLng",
                data: selectedIntersection.gps === null ? "" : selectedIntersection.gps.lng,
                width: 12,
                required: false,
                disabled: true,
            },
        ]);
    };

    const title: Map<string, string> = new Map([
        ["intersectionNo", String.intersection_no],
        ["intersectionName", String.intersection_name],
        ["regionName", String.region_name],
        ["gpsLat", String.gps_lat],
        ["gpsLng", String.gps_lng],
    ]);

    const requestAxiosUpdateIntersections = async (intersectionData: intersectionDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).put(
            Request.INTERSECTION_URL + "/" + selectedIntersectionList[0].intersectionNo,
            intersectionData
        );

        return response.data;
    };

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

    const onClickEvent = (type: string, intersection: any) => {
        const updateData = {
            intersectionNo: selectedIntersectionList[0].intersectionNo,
            intersectionName: intersection.intersectionName,
            gps: {
                lat: intersection.gpsLat,
                lng: intersection.gpsLng,
            },
            region: {
                regionNo: selectedIntersectionList[0].region.regionNo,
                regionName: intersection.regionName,
            },
        };

        requestUpdateIntersections(updateData);
    };

    const changeLocation = (lat: number, lng: number) => {
        setSelectedIntersectionList((prevState) => {
            const newState = prevState.map((obj) => {
                obj.gps.lat = lat;
                obj.gps.lng = lng;
                return obj;
            });

            return newState;
        });

        setIntersectionData((prevState) => {
            const newState = prevState.map((obj) => {
                if (obj.name === "gpsLat") {
                    return { ...obj, data: lat };
                } else if (obj.name === "gpsLng") {
                    return { ...obj, data: lng };
                }

                return obj;
            });

            return newState;
        });
    };

    return (
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={5}>
                    <ManagementDetail type="edit" title={title} response={intersectionData} clickEvent={onClickEvent} />
                </Grid>
                <Grid item xs={7}>
                    <Box className={styles.box}>
                        <OsmMap
                            style={{
                                width: "100%",
                                height: "calc(100vh - 80px)",
                                zIndex: "0",
                            }}
                            intersections={{
                                list: selectedIntersectionList,
                                selected: null,
                                clickEvent: () => {
                                    undefined;
                                },
                                dragEvent: (lat, lng) => {
                                    changeLocation(lat, lng);
                                }, //OsmMap
                                showEdge: true,
                            }}
                            //center={{
                            //    lat: centerGps !== undefined && centerGps.lat,
                            //    lng: centerGps !== undefined && centerGps.lng,
                            //}}
                            //zoomLevel={3}
                            //onClickMap={changeLocation} //KakaoMap
                        />
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}

export default IntersectionDetail;
