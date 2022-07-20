import React, { useEffect, useState } from "react";
import KakaoMap from "./KakaoMap";

import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios, useInterval } from "../utils/customHooks";
import StreamIntersection from "./StreamIntersection";
import ToggleImageButton from "./ToggleImageButton";

function DashboardMap({
    currentRegionInfo,
    intersections,
    onChangedSelectedItem,
}) {
    const userDetails = useAuthState();

    const [listCamera, setListCamera] = useState([]);
    const [selectedCameraId, setSelectedCameraId] = useState("");

    const [selectedIntersectionId, setSelectedIntersectionId] = useState("");

    const [listLink, setListLink] = useState([]);

    const [listTrafficLights, setListTrafficLights] = useState([]);
    const [blinkTrafficLights, setBlinkTrafficLights] = useState(false);

    const [listAvlDatas, setListAvlDatas] = useState([]);
    const [selectedAvl, setSelectedAvl] = useState("");

    const [showRegion, setShowRegion] = useState(true);
    const [showCameras, setShowCameras] = useState(true);
    const [showLinks, setShowLinks] = useState(true);
    const [showTrafficLights, setShowTrafficLights] = useState(true);
    const [showAvlDatas, setShowAvlDatas] = useState(true);

    const [streamIntersectionCameras, setStreamIntersectionCameras] = useState(
        []
    );

    const requestData = () => {
        let now = new Date();
        if (now.getSeconds() === 0) {
            requestCameras();
            requestLink();
        }

        if (showTrafficLights) requestTrafficLight();
        if (showAvlDatas) requestAvlDatas();
    };

    useInterval(() => {
        requestData();
    }, 1000);

    const requestAxiosCameras = async () => {
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
        var now = new Date();
        var nowMinute = now.getMinutes();
        var offsetMinute = nowMinute % 15;

        var end = new Date(now.getTime() - offsetMinute * (60 * 1000));
        let endTime = Utils.utilFormatDateYYYYMMDDHHmm00(end);

        var start = new Date(end.getTime() - 15 * (60 * 1000));
        let startTime = Utils.utilFormatDateYYYYMMDDHHmm00(start);

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

    const requestAxiosTrafficLight = async () => {
        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.TSI_URL
        );
        return response.data;
    };

    const {
        loading: loadingTrafficLight,
        error: errorTrafficLight,
        data: resultTrafficLight,
        execute: requestTrafficLight,
    } = useAsyncAxios(requestAxiosTrafficLight);

    useEffect(() => {
        if (resultTrafficLight === null) return;

        //console.log("resultTrafficLight", resultTrafficLight);
        setBlinkTrafficLights(!blinkTrafficLights);
        setListTrafficLights(resultTrafficLight.tsi);
    }, [resultTrafficLight]);

    useEffect(() => {
        if (errorTrafficLight === null) return;

        console.log("errorTrafficLight", errorTrafficLight);
    }, [errorTrafficLight]);

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
        setShowTrafficLights(
            localStorage.showTrafficLights === undefined ||
                localStorage.showTrafficLights === "true"
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
        localStorage.showTrafficLights = showTrafficLights;
        localStorage.showAvlDatas = showAvlDatas;
    };

    useEffect(() => {
        getLocalStorageData();
        requestCameras();
        requestLink();
    }, []);

    useEffect(() => {
        setLocalStorageData();
    }, [showRegion, showCameras, showLinks, showTrafficLights, showAvlDatas]);

    useEffect(() => {
        setSelectedIntersectionId(intersections?.selectedIntersectionId);
    }, [intersections]);

    useEffect(() => {
        //console.log("selectedIntersectionId", selectedIntersectionId);
        if (selectedIntersectionId === null || selectedIntersectionId === "") {
            return;
        }

        setStreamIntersectionCameras(
            Utils.utilGetInstsectionCamerasByIntersectionId(
                listCamera,
                selectedIntersectionId
            )
        );
    }, [selectedIntersectionId]);

    const handleClickCamera = (cameraId, intersectionId) => {
        setSelectedCameraId(cameraId);
        setSelectedIntersectionId(intersectionId);

        if (onChangedSelectedItem !== undefined) {
            onChangedSelectedItem({
                cameraId: cameraId,
                intersectionId: intersectionId,
            });
        }

        // play streamming
        // requestStreamStop(listStreamResponse);

        // let streamCameraInfo = makeStreamCameraList(
        //     Utils.utilGetInstsectionCameras(listCamera, cameraId)
        // );

        // requestStreamStart(streamCameraInfo);

        //console.log("intersectionId", intersectionId);
    };

    const handleClickIntersection = (intersectionId) => {
        //console.log("intersectionId", intersectionId);
        setSelectedCameraId(null);
        setSelectedIntersectionId(intersectionId);

        if (onChangedSelectedItem !== undefined) {
            onChangedSelectedItem({
                cameraId: null,
                intersectionId: intersectionId,
            });
        }
    };

    const onClickRegion = (e) => {
        setShowRegion(!showRegion);
    };

    const onClickCamera = (e) => {
        setShowCameras(!showCameras);
    };

    const onClickLinks = (e) => {
        setShowLinks(!showLinks);
    };

    const onClickTrafficLight = (e) => {
        setShowTrafficLights(!showTrafficLights);
    };

    const onClickAvl = (e) => {
        setShowAvlDatas(!showAvlDatas);
    };

    return (
        <div>
            <ToggleImageButton
                bOn={showRegion}
                onClick={onClickRegion}
                text={"region"}
            />
            <ToggleImageButton
                bOn={showCameras}
                onClick={onClickCamera}
                text={"camera"}
            />
            <ToggleImageButton
                bOn={showLinks}
                onClick={onClickLinks}
                text={"links"}
            />
            <ToggleImageButton
                bOn={showTrafficLights}
                onClick={onClickTrafficLight}
                text={"TrafficLight"}
            />
            <ToggleImageButton
                bOn={showAvlDatas}
                onClick={onClickAvl}
                text={"avl"}
            />

            <KakaoMap
                style={{
                    width: "100%",
                    height: "100vh",
                }}
                region={{
                    current: currentRegionInfo,
                    isShow: showRegion,
                }}
                intersections={{
                    list: intersections?.listIntersections,
                    selected: selectedIntersectionId,
                    clickEvent: handleClickIntersection,
                    //showEdge: true,
                }}
                cameras={{
                    list: listCamera,
                    isShow: showCameras,
                    selected: selectedCameraId,
                    clickEvent: handleClickCamera,
                }}
                links={{
                    list: listLink,
                    isShow: showLinks,
                }}
                trafficLights={{
                    list: listTrafficLights,
                    blink: blinkTrafficLights,
                    isShow: showTrafficLights,
                }}
                avl={{
                    list: listAvlDatas,
                    selected: selectedAvl,
                    isShow: showAvlDatas,
                }}
            />
            <StreamIntersection
                streamIntersectionCameras={streamIntersectionCameras}
            />
        </div>
    );
}

export default DashboardMap;
