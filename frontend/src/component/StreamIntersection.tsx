import React, { useState, useEffect } from "react";

import axios from "axios";

import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import StreamCamera from "./StreamCamera";
import { Box } from "@mui/material";

import { useInterval } from "../utils/customHooks";

import styles from "./StreamIntersection.module.css";
import imgCameraNoShow from "../assets/images/cameraNoShow.svg";

type StreamIntersectionType = {
    streamIntersectionCameras: any[];
    selectedCameraNo: string | null;
    onChangedSelectedCameraNo: (cameraNo: string) => void;
};

function StreamIntersection({
    streamIntersectionCameras,
    selectedCameraNo,
    onChangedSelectedCameraNo,
}: StreamIntersectionType) {
    const [listStreamResponse, setListStreamResponse] = useState<Array<any>>(
        []
    );

    const [showNoCamera, setShowNoCamera] = useState<boolean>(false);

    useEffect(() => {
        requestStreamStop(listStreamResponse);

        setShowNoCamera(false);

        //console.log("streamIntersectionCameras", streamIntersectionCameras);

        if (Utils.utilIsEmptyArray(streamIntersectionCameras) === false) {
            const streamCameraInfo = makeStreamCameraList(
                streamIntersectionCameras
            );
            requestStreamStart(streamCameraInfo);
        }
    }, [streamIntersectionCameras]);

    const makeStreamCameraList = (list: any[]) => {
        const bHighResolution = process.env.REACT_APP_STREAM_HIGH_RESOLUTION;

        const infos = list.map((cameraItem) => {
            const camera: any = {};

            camera.id = cameraItem.cameraNo;
            camera.user = cameraItem.rtspId;
            camera.password = cameraItem.rtspPassword;

            if (bHighResolution === "true") {
                camera.width = cameraItem.largeWidth;
                camera.height = cameraItem.largeHeight;
            } else {
                camera.width = cameraItem.smallWidth;
                camera.height = cameraItem.smallHeight;
            }

            if (
                Utils.utilIsPremakeIntersection(
                    cameraItem.intersection.intersectionNo
                )
            ) {
                if (bHighResolution === "true") {
                    camera.url = cameraItem.rtspUrl + "s";
                } else {
                    camera.url = cameraItem.rtspUrl;
                }
            } else {
                const idPassword =
                    cameraItem.rtspId + ":" + cameraItem.rtspPassword + "@";

                const url = [
                    cameraItem.rtspUrl.slice(0, 7),
                    idPassword,
                    cameraItem.rtspUrl.slice(7),
                ].join("");

                if (bHighResolution === "true") {
                    camera.url = url + "&subtype=0";
                } else {
                    camera.url = url + "&subtype=1";
                }
            }

            // // For Test [[
            // camera.url =
            //     "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4";
            // camera.width = 240;
            // camera.height = 160;
            // // ]]

            return camera;
        });

        return infos;
    };

    const requestStreamStart = async (streamCameraInfo: any[]) => {
        if (Utils.utilIsEmptyArray(streamCameraInfo)) return;

        try {
            //console.log(streamCameraInfo);
            const response = await axios.post(Request.STREAM_URL_START, {
                data: streamCameraInfo,
            });

            //console.log(JSON.stringify(response?.data));
            //console.log(JSON.stringify(response));

            const result = response?.data?.result;
            //console.log("result", result);

            setListStreamResponse(response?.data?.streams);
        } catch (err) {
            console.log(err);
        }
    };

    const requestStreamStop = async (streamCameraInfo: any[]) => {
        if (Utils.utilIsEmptyArray(streamCameraInfo)) return;

        try {
            //console.log("streamCameraInfo", streamCameraInfo);
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

    useInterval(() => {
        setShowNoCamera(true);
    }, 10000);

    const showCameraStream = (cameraNo: string) => {
        if (
            listStreamResponse === undefined ||
            listStreamResponse.filter === undefined
        )
            return <Box className={styles.rtspVideo} />;

        const streams = listStreamResponse.filter((stream) => {
            return stream.cameraId === cameraNo;
        });

        return streams[0] !== undefined ? (
            <StreamCamera className={styles.rtspVideo} stream={streams[0]} />
        ) : (
            <Box className={styles.rtspVideo} />
        );
    };

    const onCameraClicked = (cameraNo: string) => {
        onChangedSelectedCameraNo(cameraNo);
    };

    return (
        <Box className={styles.cameraCardsWrapper}>
            {streamIntersectionCameras &&
                streamIntersectionCameras.map((camera) => {
                    return (
                        <Box
                            key={camera.cameraNo}
                            className={
                                selectedCameraNo === camera.cameraNo
                                    ? [
                                          styles.cameraCard,
                                          styles.cameraCardSelected,
                                      ].join(" ")
                                    : styles.cameraCard
                            }
                            onClick={() => {
                                onCameraClicked(camera.cameraNo);
                            }}
                        >
                            <Box className={styles.loadingSpinner} />
                            {showNoCamera ? (
                                <Box className={styles.cameraCardNoshow}>
                                    <Box className={styles.noshowContent}>
                                        <img src={imgCameraNoShow} />
                                        <Box className={styles.noshowText}>
                                            카메라 준비중
                                        </Box>
                                    </Box>
                                </Box>
                            ) : null}
                            {showCameraStream(camera.cameraNo)}
                            <Box
                                className={
                                    selectedCameraNo === camera.cameraNo
                                        ? [
                                              styles.cameraCardText,
                                              styles.cameraCardTextSelected,
                                          ].join(" ")
                                        : styles.cameraCardText
                                }
                            >
                                {camera.direction.intersectionName} 방면
                            </Box>
                        </Box>
                    );
                })}
        </Box>
    );
}

export default StreamIntersection;
