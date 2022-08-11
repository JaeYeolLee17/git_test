import React, { useEffect, useState } from "react";
import KakaoMap from "./KakaoMap";

import * as Utils from "../utils/utils";
import * as Common from "../commons/common";
import * as Request from "../commons/request";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios, useInterval } from "../utils/customHooks";
import StreamIntersection from "./StreamIntersection";
import ToggleImageButton from "./ToggleImageButton";

import styles from "./DashboardMap.module.css";
import { Box, Slide } from "@mui/material";

import btnTrafficOn from "../assets/images/ico_map_traffic_f.svg";
import btnTrafficOff from "../assets/images/ico_map_traffic_n.svg";
import btnCameraOn from "../assets/images/ico_map_camera_f.svg";
import btnCameraOff from "../assets/images/ico_map_camera_n.svg";
import btnSignalLampOn from "../assets/images/ico_map_signal_lamp_f.svg";
import btnSignalLampOff from "../assets/images/ico_map_signal_lamp_n.svg";
import btnEmergencyVehicleOn from "../assets/images/ico_map_emergency_vehicle_f.svg";
import btnEmergencyVehicleOff from "../assets/images/ico_map_emergency_vehicle_n.svg";
import btnAreaOn from "../assets/images/ico_map_area_f.svg";
import btnAreaOff from "../assets/images/ico_map_area_n.svg";

import btnZoomPlus from "../assets/images/ico_plus_b.svg";
import btnZoomMinus from "../assets/images/ico_minus_b.svg";
import TrafficInformation from "./TrafficInformation";
import RTSPStreamer from "./RTSPStreamer";
import AvlInfo from "./AvlInfo";

function DashboardMap({
    transitionState,
    currentRegionInfo,
    intersections,
    onChangedSelectedItem,
}: {
    transitionState: string;
    currentRegionInfo: Common.RegionInfo;
    intersections: {
        listIntersections: any[];
        selectedIntersectionNo: string;
        selectedIntersectionName: string | undefined;
    };
    onChangedSelectedItem: ({
        cameraNo,
        intersectionNo,
    }: {
        cameraNo: string | null;
        intersectionNo: string;
    }) => void;
}) {
    const userDetails = useAuthState();

    const [listCamera, setListCamera] = useState<Array<any>>([]);
    const [selectedCameraNo, setSelectedCameraNo] = useState<string | null>("");

    const [selectedIntersectionNo, setSelectedIntersectionNo] =
        useState<string>("");

    const [listLink, setListLink] = useState<Array<any>>([]);

    const [listTsi, setListTsi] = useState<Array<any>>([]);
    const [blinkTsi, setBlinkTsi] = useState<boolean>(false);

    const [listAvlDatas, setListAvlDatas] = useState<Array<any>>([]);
    const [selectedAvl, setSelectedAvl] = useState<string>("");

    const [showRegion, setShowRegion] = useState<boolean>(true);
    const [showCameras, setShowCameras] = useState<boolean>(true);
    const [showLinks, setShowLinks] = useState<boolean>(true);
    const [showTsi, setShowTsi] = useState<boolean>(true);
    const [showAvlDatas, setShowAvlDatas] = useState<boolean>(true);

    const [streamIntersectionCameras, setStreamIntersectionCameras] = useState<
        Array<any>
    >([]);

    const [mapZoomLevel, setMapZoomLevel] = useState<number>(7);

    const [requestLinkEndTime, setRequestLinkEndTime] = useState<string>("");

    const [trafficInformationBottom, setTrafficInformationBottom] =
        useState<number>(0);

    const requestData = () => {
        const now = new Date();
        if (now.getSeconds() === 0) {
            requestCameras();
            requestLink();
        }

        if (showTsi) requestTsi();
        if (showAvlDatas) requestAvlDatas();
    };

    useInterval(() => {
        requestData();
    }, 1000);

    const requestAxiosCameras = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.CAMERA_LIST_URL
        );
        return response.data;
    };

    const {
        loading: loadingCamerasList,
        error: errorCamerasList,
        data: resultCamerasList,
        execute: requestCameras,
    } = useAsyncAxios(requestAxiosCameras);

    useEffect(() => {
        if (resultCamerasList === null) return;

        //console.log("resultCamerasList", resultCamerasList);
        setListCamera(resultCamerasList.cameras);
    }, [resultCamerasList]);

    useEffect(() => {
        if (errorCamerasList === null) return;

        console.log("errorCamerasList", errorCamerasList);
    }, [errorCamerasList]);

    // const requestCameras = async (e) => {
    //     try {
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.CAMERA_LIST_URL);

    //         //console.log(JSON.stringify(response?.data));
    //         setListCamera(response?.data.cameras);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    const requestAxiosLink = async () => {
        const now = new Date();
        const nowMinute = now.getMinutes();
        const offsetMinute = nowMinute % 15;

        const end = new Date(now.getTime() - offsetMinute * (60 * 1000));
        const endTime = Utils.utilFormatDateYYYYMMDDHHmm00(end);

        const start = new Date(end.getTime() - 15 * (60 * 1000));
        const startTime = Utils.utilFormatDateYYYYMMDDHHmm00(start);

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        setRequestLinkEndTime(endTime);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.STAT_LINK_URL,
            {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                },
            }
        );

        return response.data;
    };

    const {
        loading: loadinLink,
        error: errorLink,
        data: resultLink,
        execute: requestLink,
    } = useAsyncAxios(requestAxiosLink);

    useEffect(() => {
        if (resultLink === null) return;

        //console.log("resultLink", resultLink);
        setListLink(resultLink.stat);
    }, [resultLink]);

    useEffect(() => {
        if (errorLink === null) return;

        console.log("errorLink", errorLink);
    }, [errorLink]);

    // const requestLink = async (e) => {
    //     var now = new Date();
    //     var nowMinute = now.getMinutes();
    //     var offsetMinute = nowMinute % 15;

    //     var end = new Date(now.getTime() - offsetMinute * (60 * 1000));
    //     let endTime = Utils.utilFormatDateYYYYMMDDHHmm00(end);

    //     var start = new Date(end.getTime() - 15 * (60 * 1000));
    //     let startTime = Utils.utilFormatDateYYYYMMDDHHmm00(start);

    //     try {
    //         //console.log(userDetails.token);
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.STAT_LINK_URL, {
    //             params: {
    //                 startTime: startTime,
    //                 endTime: endTime,
    //             },
    //         });

    //         //console.log(JSON.stringify(response?.data));
    //         setListLink(response?.data?.stat);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    const requestAxiosTsi = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.TSI_URL
        );
        return response.data;
    };

    const {
        loading: loadingTsi,
        error: errorTsi,
        data: resultTsi,
        execute: requestTsi,
    } = useAsyncAxios(requestAxiosTsi);

    useEffect(() => {
        if (resultTsi === null) return;

        //console.log("resultTsi", resultTsi);
        setBlinkTsi(!blinkTsi);
        setListTsi(resultTsi.tsi);
    }, [resultTsi]);

    useEffect(() => {
        if (errorTsi === null) return;

        console.log("errorTsi", errorTsi);
    }, [errorTsi]);

    // const requestTrafficLight = async (e) => {
    //     try {
    //         //console.log(userDetails.token);
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.TSI_URL);

    //         //console.log(JSON.stringify(response?.data));
    //         setBlinkTrafficLights(!blinkTrafficLights);
    //         setListTrafficLights(response?.data?.tsi);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    const requestAxiosAvlDatas = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.AVL_URL
        );
        return response.data;
    };

    const {
        loading: loadingAvlDatas,
        error: errorAvlDatas,
        data: resultAvlDatas,
        execute: requestAvlDatas,
    } = useAsyncAxios(requestAxiosAvlDatas);

    useEffect(() => {
        if (resultAvlDatas === null) return;

        //console.log("resultTrafficLight", resultTrafficLight);
        setListAvlDatas(resultAvlDatas.avl);
    }, [resultAvlDatas]);

    useEffect(() => {
        if (errorAvlDatas === null) return;

        console.log("errorAvlDatas", errorAvlDatas);
    }, [errorAvlDatas]);

    // const requestAvlDatas = async (e) => {
    //     try {
    //         //console.log(userDetails.token);
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.AVL_URL);

    //         //console.log(JSON.stringify(response?.data));
    //         setListAvlDatas(response?.data?.avl);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    const getLocalStorageData = () => {
        setShowRegion(
            localStorage.showRegion === undefined ||
                localStorage.showRegion === "true"
        );
        setShowCameras(
            localStorage.showCameras === undefined ||
                localStorage.showCameras === "true"
        );
        setShowLinks(
            localStorage.showLinks === undefined ||
                localStorage.showLinks === "true"
        );
        setShowTsi(
            localStorage.showTsi === undefined ||
                localStorage.showTsi === "true"
        );
        setShowAvlDatas(
            localStorage.showAvlDatas === undefined ||
                localStorage.showAvlDatas === "true"
        );
    };

    const setLocalStorageData = () => {
        localStorage.showRegion = showRegion;
        localStorage.showCameras = showCameras;
        localStorage.showLinks = showLinks;
        localStorage.showTsi = showTsi;
        localStorage.showAvlDatas = showAvlDatas;
    };

    useEffect(() => {
        getLocalStorageData();
        requestCameras();
        requestLink();
    }, []);

    useEffect(() => {
        setSelectedAvl("");
    }, [showAvlDatas]);

    useEffect(() => {
        setLocalStorageData();
    }, [showRegion, showCameras, showLinks, showTsi, showAvlDatas]);

    useEffect(() => {
        if (intersections?.selectedIntersectionName === undefined) {
            setSelectedCameraNo(null);
        }
        setSelectedIntersectionNo(intersections?.selectedIntersectionNo);

        if (intersections?.selectedIntersectionName === undefined) {
            setTrafficInformationBottom(0);
        }
    }, [intersections]);

    useEffect(() => {
        //console.log("selectedIntersectionNo", selectedIntersectionNo);
        if (selectedIntersectionNo === null || selectedIntersectionNo === "") {
            return;
        }

        setStreamIntersectionCameras(
            Utils.utilGetInstsectionCamerasByIntersectionNo(
                listCamera,
                selectedIntersectionNo
            )
        );
    }, [selectedIntersectionNo]);

    const handleClickCamera = (cameraNo: string, intersectionNo: string) => {
        setSelectedCameraNo(cameraNo);
        setSelectedIntersectionNo(intersectionNo);

        if (onChangedSelectedItem !== undefined) {
            onChangedSelectedItem({
                cameraNo: cameraNo,
                intersectionNo: intersectionNo,
            });
        }

        // play streamming
        // requestStreamStop(listStreamResponse);

        // let streamCameraInfo = makeStreamCameraList(
        //     Utils.utilGetInstsectionCameras(listCamera, cameraNo)
        // );

        // requestStreamStart(streamCameraInfo);

        //console.log("intersectionNo", intersectionNo);
    };

    const handleClickIntersection = (intersectionNo: string) => {
        //console.log("intersectionNo", intersectionNo);
        setSelectedCameraNo(null);
        setSelectedIntersectionNo(intersectionNo);

        if (onChangedSelectedItem !== undefined) {
            onChangedSelectedItem({
                cameraNo: null,
                intersectionNo: intersectionNo,
            });
        }
    };

    const onClickRegion = (e: React.MouseEvent<HTMLButtonElement>) => {
        setShowRegion(!showRegion);
    };

    const onClickCamera = (e: React.MouseEvent<HTMLButtonElement>) => {
        setShowCameras(!showCameras);
    };

    const onClickLinks = (e: React.MouseEvent<HTMLButtonElement>) => {
        setShowLinks(!showLinks);
    };

    const onClickTsi = (e: React.MouseEvent<HTMLButtonElement>) => {
        setShowTsi(!showTsi);
    };

    const onClickAvl = (e: React.MouseEvent<HTMLButtonElement>) => {
        setShowAvlDatas(!showAvlDatas);
    };

    const onClickZoomPlus = (e: React.MouseEvent<HTMLButtonElement>) => {
        if (mapZoomLevel !== undefined) setMapZoomLevel(mapZoomLevel + 1);
    };

    const onClickZoomMinus = (e: React.MouseEvent<HTMLButtonElement>) => {
        if (mapZoomLevel !== undefined) setMapZoomLevel(mapZoomLevel - 1);
    };

    const onChangedZoomLevel = (level: number) => {
        setMapZoomLevel(level);
    };

    const handleRTSPStreamerHeight = (height: number) => {
        setTrafficInformationBottom(height);
    };

    const onChangedSelectedCameraNo = (cameraNo: string) => {
        setSelectedCameraNo(cameraNo);
    };

    const onChangedSelectedEmergencyCarNumber = (carNumber: string) => {
        if (carNumber === selectedAvl) setSelectedAvl("");
        else setSelectedAvl(carNumber);
    };

    return (
        <div>
            <Box className={styles.mapBtnsWrap}>
                <ul>
                    <li>
                        <ToggleImageButton
                            bOn={showLinks}
                            onClick={onClickLinks}
                            imageOn={btnTrafficOn}
                            imageOff={btnTrafficOff}
                            tooltip={"links"}
                        />
                    </li>
                    <li>
                        <ToggleImageButton
                            bOn={showCameras}
                            onClick={onClickCamera}
                            imageOn={btnCameraOn}
                            imageOff={btnCameraOff}
                            tooltip={"camera"}
                        />
                    </li>
                    <li>
                        <ToggleImageButton
                            bOn={showTsi}
                            onClick={onClickTsi}
                            imageOn={btnSignalLampOn}
                            imageOff={btnSignalLampOff}
                            tooltip={"Tsi"}
                        />
                    </li>
                    <li>
                        <ToggleImageButton
                            bOn={showAvlDatas}
                            onClick={onClickAvl}
                            imageOn={btnEmergencyVehicleOn}
                            imageOff={btnEmergencyVehicleOff}
                            tooltip={"avl"}
                        />
                    </li>
                    <li>
                        <ToggleImageButton
                            bOn={showRegion}
                            onClick={onClickRegion}
                            imageOn={btnAreaOn}
                            imageOff={btnAreaOff}
                            tooltip={"region"}
                        />
                    </li>
                </ul>
            </Box>

            <Box className={styles.mapZoombtnsWrap}>
                <ul>
                    <li>
                        <button
                            className={styles.mapZoombtns}
                            onClick={onClickZoomPlus}
                        >
                            <img src={btnZoomPlus} />
                        </button>
                    </li>
                    <li>
                        <button
                            className={styles.mapZoombtns}
                            onClick={onClickZoomMinus}
                        >
                            <img src={btnZoomMinus} />
                        </button>
                    </li>
                </ul>
            </Box>

            <TrafficInformation
                show={showLinks}
                time={requestLinkEndTime}
                bottom={trafficInformationBottom}
            />

            {intersections?.selectedIntersectionName !== undefined ? (
                <RTSPStreamer
                    intersectionName={intersections?.selectedIntersectionName}
                    streamIntersectionCameras={streamIntersectionCameras}
                    selectedCameraNo={selectedCameraNo}
                    onChangedHeight={handleRTSPStreamerHeight}
                    onChangedSelectedCameraNo={onChangedSelectedCameraNo}
                />
            ) : null}

            <KakaoMap
                style={{
                    width: "100%",
                    height: "calc(100vh - 80px)",
                    zIndex: "0",
                }}
                transitionState={transitionState}
                region={{
                    current: currentRegionInfo,
                    isShow: showRegion,
                }}
                intersections={{
                    list: intersections?.listIntersections,
                    selected: selectedIntersectionNo,
                    clickEvent: handleClickIntersection,
                    //showEdge: true,
                }}
                cameras={{
                    list: listCamera,
                    isShow: showCameras,
                    selected: selectedCameraNo,
                    clickEvent: handleClickCamera,
                }}
                links={{
                    list: listLink,
                    isShow: showLinks,
                }}
                tsi={{
                    list: listTsi,
                    blink: blinkTsi,
                    isShow: showTsi,
                }}
                avl={{
                    list: listAvlDatas,
                    selected: selectedAvl,
                    isShow: showAvlDatas,
                }}
                zoomLevel={mapZoomLevel}
                onChangedZoomLevel={onChangedZoomLevel}
            />
            {showAvlDatas && (
                <AvlInfo
                    list={listAvlDatas}
                    selected={selectedAvl}
                    onChangedSelectedEmergencyCarNumber={
                        onChangedSelectedEmergencyCarNumber
                    }
                />
            )}
            {/* <StreamIntersection
                streamIntersectionCameras={streamIntersectionCameras}
            /> */}
        </div>
    );
}

export default DashboardMap;
