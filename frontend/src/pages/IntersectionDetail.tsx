import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import KakaoMap from "../component/KakaoMap";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import Grid from "@mui/material/Grid";
import styles from "./CameraDetail.module.css";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as Common from "../commons/common";

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

    const [selectedIntersectionList, setSelectedIntersectionList] = useState<
        Array<any>
    >([]);
    const [intersectionData, setIntersectionData] = useState<any[]>([]);

    useEffect(() => {
        location.state !== null && onSelectedIntersection(location.state);
    }, [location.state]);

    const onSelectedIntersection = (selectedIntersection: any) => {
        setSelectedIntersectionList((selectedIntersectionList) => [
            ...selectedIntersectionList,
            selectedIntersection,
        ]);

        setIntersectionData([
            {
                name: "intersectionNo",
                data: selectedIntersection.intersectionNo,
                width: 6,
                required: true,
                disabled: true,
            },
            {
                name: "intersectionName",
                data: selectedIntersection.intersectionName,
                width: 6,
                required: true,
                disabled: false,
            },
            {
                name: "region",
                data: selectedIntersection.region.regionName,
                width: 6,
                required: false,
                disabled: true,
            },
            {
                name: "gpsLat",
                data: selectedIntersection.gps.lat,
                width: 6,
                required: true,
                disabled: true,
            },
            {
                name: "gpsLng",
                data: selectedIntersection.gps.lng,
                width: 6,
                required: false,
                disabled: true,
            },
        ]);
    };

    const requestAxiosUpdateIntersections = async (
        intersectionData: intersectionDataType
    ) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        console.log("intersectionData : " + intersectionData);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).post(
            Request.INTERSECTION_URL +
                "/" +
                selectedIntersectionList[0].intersectionNo,
            { intersectionData }
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

    const onClickEvent = (intersections: any) => {
        requestUpdateIntersections(intersections);
    };

    return (
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item xs={8}>
                    <ManagementDetail
                        response={intersectionData}
                        clickEvent={onClickEvent}
                    />
                </Grid>
                <Grid item xs={4}>
                    <KakaoMap
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
                            showEdge: true,
                        }}
                    />
                </Grid>
            </Grid>
        </div>
    );
}

export default IntersectionDetail;
