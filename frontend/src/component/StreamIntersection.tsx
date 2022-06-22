import React, { useState, useEffect } from "react";

import axios from "axios";

import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import StreamCamera from "./StreamCamera";

type StreamIntersectionType = {
    streamIntersectionCameras: any[];
};

function StreamIntersection({
    streamIntersectionCameras,
}: StreamIntersectionType) {
    const [listStreamResponse, setListStreamResponse] = useState<Array<any>>(
        []
    );

    useEffect(() => {
        requestStreamStop(listStreamResponse);

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
                const idPassword =
                    cameraItem.rtspId + ":" + cameraItem.rtspPassword + "@";

                const url = [
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
                "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4";
            camera.width = 240;
            camera.height = 160;
            // ]]

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

    return (
        <div>
            {listStreamResponse &&
                listStreamResponse.map((stream) => (
                    <StreamCamera key={stream.cameraId} stream={stream} />
                ))}
        </div>
    );
}

export default StreamIntersection;
