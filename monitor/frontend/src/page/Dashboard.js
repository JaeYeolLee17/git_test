import React, { useEffect, useState } from "react";
import axios from "axios";

import Header from "../component/Header";
import Menu from "../component/Menu";
import KakaoMap from "../component/KakaoMap";
import ChartMfd from "../component/ChartMfd";
import StreamCamera from "../component/StreamCamera";

import Selector from "../component/Selector";

import { useAuthState } from "../provider/AuthProvider";

import * as Utils from "../utils/utils";
import * as Request from "../request";
import * as String from "../string";
import { useInterval } from "../utils/customHooks";

const Dashboard = () => {
    const userDetails = useAuthState();

    const [listCamera, setListCamera] = useState([]);
    const [showCameras, setShowCameras] = useState(true);
    const [selectedCamera, setSelectedCamera] = useState("");

    const [listRegions, setListRegions] = useState([]);
    const [listSelectRegions, setListSelectRegions] = useState([]);
    const [listSelectRegionItem, setListSelectRegionItem] = useState({});
    const [showRegion, setShowRegion] = useState(true);
    const [curretnRegionInfo, setCurretnRegionInfo] = useState({});

    const [listIntersections, setListIntersections] = useState([]);
    const [listSelectIntersections, setListSelectIntersections] = useState([]);
    const [listSelectIntersectionItem, setListSelectIntersectionItem] =
        useState("");
    const [selectedIntersection, setSelectedIntersection] = useState("");

    const [listLink, setListLink] = useState([]);
    const [showLinks, setShowLinks] = useState(true);

    const [listStreamResponse, setListStreamResponse] = useState([]);

    const [dataMfd, setDataMfd] = useState(null);
    const [dataLastWeekMfd, setDataLastWeekMfd] = useState(null);
    const [dataLastMonthAvgMfd, setDataLastMonthAvgMfd] = useState(null);

    const requestData = () => {
        let now = new Date();
        if (now.getSeconds() === 0) {
            requestCameras();
            requestMfd();
            requestLink();
        }
    };

    useInterval(() => {
        requestData();
    }, 1000);

    // const requestUsers = async (e) => {
    //     try {
    //         //console.log(userDetails.token);
    //         const response = await Utils.utilAxios.get(
    //             Request.USERS_URL
    //         );

    //         console.log(JSON.stringify(response?.data));
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    const requestCameras = async (e) => {
        try {
            //console.log(userDetails.token);
            // const response = await axios.get(
            //     Request.CAMERA_URL,
            //     {
            //         headers: {
            //             "Content-Type": "application/json",
            //             "X-AUTH-TOKEN": userDetails.token,
            //         },
            //         withCredentials: true,
            //     }
            // );
            const response = await Utils.utilAxiosWithAuth(
                userDetails.token
            ).get(Request.CAMERA_URL);

            //console.log(JSON.stringify(response?.data));
            setListCamera(response?.data.cameras);
        } catch (err) {
            console.log(err);
        }
    };

    const requestRegionList = async (e) => {
        try {
            //console.log(userDetails.token);
            const response = await Utils.utilAxiosWithAuth(
                userDetails.token
            ).get(Request.REGIONS_LIST_URL);

            //console.log(JSON.stringify(response?.data));
            setListRegions(response?.data.regions);
        } catch (err) {
            console.log(err);
        }
    };

    const requestIntersectionList = async (e) => {
        try {
            const response = await Utils.utilAxiosWithAuth(
                userDetails.token
            ).get(Request.INTERSECTIONS_LIST_URL, {
                params: {
                    ...(listSelectRegionItem.value !== "all"
                        ? { regionId: listSelectRegionItem.value }
                        : {}),
                },
            });

            //console.log(JSON.stringify(response?.data));
            setListIntersections(response?.data.intersections);
        } catch (err) {
            console.log(err);
        }
    };

    const requestMfd = async (e) => {
        let startTime = Utils.utilFormatDateYYYYMMDD000000(new Date());

        var now = new Date();
        var nowMinute = now.getMinutes();
        var offsetMinute = nowMinute % 15;

        var end = new Date(now.getTime() - offsetMinute * (60 * 1000));
        let endTime = Utils.utilFormatDateYYYYMMDDHHmm00(end);

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        let extraParam = {};

        if (listSelectIntersectionItem.value === "all") {
            if (listSelectRegionItem.value !== "all") {
                extraParam = {
                    filterBy: "region",
                    filterId: listSelectRegionItem.value,
                };
            }
        } else {
            extraParam = {
                filterBy: "intersection",
                filterId: listSelectIntersectionItem.value,
            };
        }

        //console.log("extraParam", extraParam);

        try {
            //console.log(userDetails.token);
            const response = await Utils.utilAxiosWithAuth(
                userDetails.token
            ).get(Request.STAT_MFD_URL, {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                    ...extraParam,
                },
            });

            //console.log(JSON.stringify(response?.data));
            setDataMfd(response?.data?.stat[0]);
        } catch (err) {
            console.log(err);
        }
    };

    const requestLastWeekMfd = async (e) => {
        let start = new Date();
        start.setDate(start.getDate() - 7);
        let startTime = Utils.utilFormatDateYYYYMMDD000000(start);

        let end = new Date(start);
        end.setDate(end.getDate() + 1);
        let endTime = Utils.utilFormatDateYYYYMMDD000000(end);

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        let extraParam = {};

        if (listSelectIntersectionItem.value === "all") {
            if (listSelectRegionItem.value !== "all") {
                extraParam = {
                    filterBy: "region",
                    filterId: listSelectRegionItem.value,
                };
            }
        } else {
            extraParam = {
                filterBy: "intersection",
                filterId: listSelectIntersectionItem.value,
            };
        }

        //console.log("extraParam", extraParam);

        try {
            //console.log(userDetails.token);
            const response = await Utils.utilAxiosWithAuth(
                userDetails.token
            ).get(Request.STAT_MFD_URL, {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                    ...extraParam,
                },
            });

            //console.log(JSON.stringify(response?.data));
            setDataLastWeekMfd(response?.data?.stat[0]);
        } catch (err) {
            console.log(err);
        }
    };

    const requestLastMonthAvgMfd = async (e) => {
        let start = new Date();
        start.setDate(start.getDate() - 28);
        let startTime = Utils.utilFormatDateYYYYMMDD000000(start);

        let end = new Date();
        let endTime = Utils.utilFormatDateYYYYMMDD000000(end);

        let dayOfWeek = end.getDay();
        if (dayOfWeek === 0) dayOfWeek = 7;

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        let extraParam = {};

        if (listSelectIntersectionItem.value === "all") {
            if (listSelectRegionItem.value !== "all") {
                extraParam = {
                    filterBy: "region",
                    filterId: listSelectRegionItem.value,
                };
            }
        } else {
            extraParam = {
                filterBy: "intersection",
                filterId: listSelectIntersectionItem.value,
            };
        }

        //console.log("extraParam", extraParam);

        try {
            //console.log(userDetails.token);
            const response = await Utils.utilAxiosWithAuth(
                userDetails.token
            ).get(Request.STAT_MFD_URL, {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                    ...extraParam,
                    dayOfWeek: dayOfWeek,
                },
            });

            //console.log(JSON.stringify(response?.data));
            setDataLastMonthAvgMfd(response?.data?.stat[0]);
        } catch (err) {
            console.log(err);
        }
    };

    const requestLink = async (e) => {
        var now = new Date();
        var nowMinute = now.getMinutes();
        var offsetMinute = nowMinute % 15;

        var end = new Date(now.getTime() - offsetMinute * (60 * 1000));
        let endTime = Utils.utilFormatDateYYYYMMDDHHmm00(end);

        var start = new Date(end.getTime() - 15 * (60 * 1000));
        let startTime = Utils.utilFormatDateYYYYMMDDHHmm00(start);

        try {
            //console.log(userDetails.token);
            const response = await Utils.utilAxiosWithAuth(
                userDetails.token
            ).get(Request.STAT_LINK_URL, {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                },
            });

            //console.log(JSON.stringify(response?.data));
            setListLink(response?.data?.stat);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        requestRegionList();
        // const timerId = setInterval(() => {
        //     requestData();
        // }, 1 * 1000);

        // return () => {
        //     clearInterval(timerId);
        //     requestStreamStop(listStreamResponse);
        // };
        requestCameras();

        //requestUsers();
    }, []);

    useEffect(() => {
        if (listRegions.length === 0) return;

        let topItem = { value: "all", innerHTML: String.total };

        let newList = [];
        newList.push(topItem);

        newList = newList.concat(
            listRegions.map((region) => {
                //console.log(region);
                return { value: region.regionId, innerHTML: region.regionName };
            })
        );

        setListSelectRegions(newList);

        setListSelectRegionItem(topItem);
    }, [listRegions]);

    useEffect(() => {
        if (listSelectRegionItem) {
            console.log("listSelectRegionItem", listSelectRegionItem);
            let currentRegionInfo = listRegions.filter(
                (region) => region.regionId === listSelectRegionItem.value
            );
            setCurretnRegionInfo(currentRegionInfo[0]);

            requestIntersectionList();
        }
    }, [listSelectRegionItem]);

    useEffect(() => {
        if (listIntersections.length === 0) return;

        //console.log("listIntersections", listIntersections);

        let topItem = { value: "all", innerHTML: String.total };

        let newList = [];
        newList.push(topItem);

        newList = newList.concat(
            listIntersections
                .filter((intersection) => intersection.region !== null)
                .map((intersection) => {
                    //console.log(region);
                    return {
                        value: intersection.intersectionId,
                        innerHTML: intersection.intersectionName,
                    };
                })
        );

        setListSelectIntersections(newList);

        setListSelectIntersectionItem(topItem);
    }, [listIntersections]);

    useEffect(() => {
        console.log("listSelectIntersectionItem", listSelectIntersectionItem);

        //setDataMfd(null);
        setDataLastWeekMfd(null);
        setDataLastMonthAvgMfd(null);

        requestMfd();
        requestLastWeekMfd();
        requestLastMonthAvgMfd();

        requestLink();
    }, [listSelectIntersectionItem]);

    const makeStreamCameraList = (list) => {
        const bHighResolution = false;

        let infos = list.map((cameraItem) => {
            let camera = {};

            camera.id = cameraItem.cameraId;
            camera.user = cameraItem.rtspId;
            camera.password = cameraItem.rtspPassword;

            if (bHighResolution === true) {
                camera.width = cameraItem.largeWidth;
                camera.height = cameraItem.largeHeight;
            } else {
                camera.width = cameraItem.smallWidth;
                camera.height = cameraItem.smallHeight;
            }

            if (
                Utils.utilIsPremakeIntersection(
                    cameraItem.intersection.intersectionId
                )
            ) {
                if (bHighResolution === true) {
                    camera.url = cameraItem.rtspUrl + "s";
                } else {
                    camera.url = cameraItem.rtspUrl;
                }
            } else {
                var idPassword =
                    cameraItem.rtspId + ":" + cameraItem.rtspPassword + "@";

                var url = [
                    cameraItem.rtspUrl.slice(0, 7),
                    idPassword,
                    cameraItem.rtspUrl.slice(7),
                ].join("");

                if (bHighResolution === true) {
                    camera.url = url + "&subtype=0";
                } else {
                    camera.url = url + "&subtype=1";
                }
            }

            // For Test [[
            camera.url =
                "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
            camera.width = 240;
            camera.height = 160;
            // ]]

            return camera;
        });

        return infos;
    };

    const requestStreamStart = async (streamCameraInfo) => {
        try {
            console.log(streamCameraInfo);
            const response = await axios.post(Request.STREAM_URL_START, {
                data: streamCameraInfo,
            });

            //console.log(JSON.stringify(response?.data));
            console.log(JSON.stringify(response));

            const result = response?.data?.result;
            console.log("result", result);

            setListStreamResponse(response?.data?.streams);
        } catch (err) {
            console.log(err);
        }
    };

    const requestStreamStop = async (streamCameraInfo) => {
        try {
            console.log(streamCameraInfo);
            const response = await axios.post(Request.STREAM_URL_STOP, {
                data: streamCameraInfo,
            });

            //console.log(JSON.stringify(response?.data));
            //console.log(JSON.stringify(response));

            //const result = response?.data?.result;
            //console.log("result", result);

            setListStreamResponse(response?.data?.streams);
        } catch (err) {
            console.log(err);
        }
    };

    const handleClickCamera = (cameraId, intersectionId) => {
        setSelectedCamera(cameraId);
        setSelectedIntersection(intersectionId);

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
        setSelectedCamera(null);
        setSelectedIntersection(intersectionId);
    };

    const onChangeRegions = (e) => {
        setListSelectRegionItem({
            value: e.target.value,
            innerHTML: e.target[e.target.selectedIndex].innerHTML,
        });
    };

    const onChangeIntersections = (e) => {
        console.log(e);
        setListSelectIntersectionItem({
            value: e.target.value,
            innerHTML: e.target[e.target.selectedIndex].innerHTML,
        });
    };

    const onClickRegion = (e) => {
        setShowRegion(!showRegion);
    };

    const onClickCamera = (e) => {
        // setListSelectRegionItem("R01");
        setShowCameras(!showCameras);
    };

    const onClickLinks = (e) => {
        setShowLinks(!showLinks);
    };

    return (
        <div>
            <Header />
            <Menu />
            {listSelectRegions.length > 0 ? (
                <Selector
                    list={listSelectRegions}
                    selected={listSelectRegionItem}
                    onChange={onChangeRegions}
                />
            ) : null}
            {listSelectIntersections.length > 0 ? (
                <Selector
                    list={listSelectIntersections}
                    selected={listSelectIntersectionItem}
                    onChange={onChangeIntersections}
                />
            ) : null}
            <button onClick={onClickRegion}>region</button>
            <button onClick={onClickCamera}>camera</button>
            <button onClick={onClickLinks}>links</button>
            <KakaoMap
                style={{
                    width: "100%",
                    height: "100vh",
                }}
                region={{
                    current: curretnRegionInfo,
                    isShow: showRegion,
                }}
                intersections={{
                    list: listIntersections,
                    selected: selectedIntersection,
                    clickEvent: handleClickIntersection,
                    //showEdge: true,
                }}
                cameras={{
                    list: listCamera,
                    isShow: showCameras,
                    selected: selectedCamera,
                    clickEvent: handleClickCamera,
                }}
                links={{
                    list: listLink,
                    isShow: showLinks,
                }}
            />
            {listStreamResponse &&
                listStreamResponse.map((stream) => (
                    <StreamCamera key={stream.cameraId} stream={stream} />
                ))}
            {/* {listSelectRegions.length > 0 ? (
                <Selector
                    list={listSelectRegions}
                    selected={listSelectRegionItem}
                    onChange={onChangeRegions}
                />
            ) : null}
            {listSelectIntersections.length > 0 ? (
                <Selector
                    list={listSelectIntersections}
                    selected={listSelectIntersectionItem}
                    onChange={onChangeIntersections}
                />
            ) : null} */}
            <ChartMfd
                dataMfd={dataMfd}
                dataLastWeekMfd={dataLastWeekMfd}
                dataLastMonthAvgMfd={dataLastMonthAvgMfd}
            />
        </div>
    );
};

export default Dashboard;
