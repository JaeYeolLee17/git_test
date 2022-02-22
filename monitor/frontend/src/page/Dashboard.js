import React, { useEffect, useState } from "react";
import axios from "axios";

import Header from "../component/Header";
import Menu from "../component/Menu";
import KakaoMap from "../component/KakaoMap";

import { useAuthState } from "../provider/AuthProvider";

import {
    utilGetInstsectionCameras,
    utilIsPremakeIntersection,
} from "../utils/utils";
import StreamCamera from "../component/StreamCamera";

const CAMERA_URL = "/challenge-api/v1/cameras";
const STREAM_URL_START = "/start";
const STREAM_URL_STOP = "/stop";

const Dashboard = () => {
    const userDetails = useAuthState();

    const [listCamera, setListCamera] = useState([]);
    const [showCameras, setShowCameras] = useState(true);
    const [selectedCamera, setSelectedCamera] = useState("");

    const [listStreamResponse, setListStreamResponse] = useState([]);

    const requesetCameras = async (e) => {
        try {
            //console.log(userDetails.token);
            const response = await axios.get(CAMERA_URL, {
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

    useEffect(() => {
        const timerId = setInterval(() => {
            requesetCameras();
        }, 10 * 1000);
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
                utilIsPremakeIntersection(
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
            const response = await axios.post(STREAM_URL_START, {
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
            const response = await axios.post(STREAM_URL_STOP, {
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
            utilGetInstsectionCameras(listCamera, cameraId)
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
        </div>
    );
};

export default Dashboard;
