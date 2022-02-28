import React, { useEffect, useState } from "react";
import axios from "axios";

import Header from "../component/Header";
import Menu from "../component/Menu";
import KakaoMap from "../component/KakaoMap";
import ChartMfd from "../component/ChartMfd";
import StreamCamera from "../component/StreamCamera";

import { useAuthState } from "../provider/AuthProvider";

import * as Utils from "../utils/utils";
import * as Request from "../request";

const Dashboard = () => {
    const userDetails = useAuthState();

    const [listCamera, setListCamera] = useState([]);
    const [showCameras, setShowCameras] = useState(true);
    const [selectedCamera, setSelectedCamera] = useState("");

    const [listStreamResponse, setListStreamResponse] = useState([]);

    const [dataMfd, setDataMfd] = useState(null);

    const requestData = () => {
        let now = new Date();
        if (now.getSeconds() === 0) {
            requestCameras();
            requestMfd();
        }
    };

    const requestCameras = async (e) => {
        try {
            //console.log(userDetails.token);
            const response = await axios.get(Request.CAMERA_URL, {
                headers: {
                    "Content-Type": "application/json",
                    "X-AUTH-TOKEN": userDetails.token,
                },
                withCredentials: true,
            });

            //console.log(JSON.stringify(response?.data));
            setListCamera(response?.data.cameras);
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

        try {
            //console.log(userDetails.token);
            const response = await axios.get(Request.STAT_MFD_URL, {
                params: {
                    startTime: `${startTime}`,
                    endTime: `${endTime}`,
                    filterBy: "region",
                    filterId: "R01",
                },

                headers: {
                    "Content-Type": "application/json",
                    "X-AUTH-TOKEN": userDetails.token,
                },
                withCredentials: true,
            });

            //console.log(JSON.stringify(response?.data));
            setDataMfd(response?.data?.stat[0]);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        const timerId = setInterval(() => {
            requestData();
        }, 1 * 1000);

        return () => {
            setInterval(timerId);
            requestStreamStop(listStreamResponse);
        };
        //requesetCameras();
    }, []);

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

    const handleClickCamera = (cameraId) => {
        setSelectedCamera(cameraId);

        requestStreamStop(listStreamResponse);

        let streamCameraInfo = makeStreamCameraList(
            Utils.utilGetInstsectionCameras(listCamera, cameraId)
        );

        requestStreamStart(streamCameraInfo);

        //console.log("streamCameraInfo", streamCameraInfo);
    };

    return (
        <div>
            <Header />
            <Menu />
            Dashboard
            <KakaoMap
                style={{
                    width: "100%",
                    height: "100vh",
                }}
                cameras={{
                    list: listCamera,
                    isShow: showCameras,
                    selected: selectedCamera,
                    clickEvent: handleClickCamera,
                }}
            />
            {listStreamResponse &&
                listStreamResponse.map((stream) => (
                    <StreamCamera key={stream.cameraId} stream={stream} />
                ))}
            <ChartMfd dataMfd={dataMfd} />
        </div>
    );
};

export default Dashboard;
