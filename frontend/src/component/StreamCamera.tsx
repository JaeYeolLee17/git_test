import { Box } from "@mui/material";
import React, { useEffect } from "react";

import styles from "./StreamCamera.module.css";

type StreamCameraType = {
    stream: any;
    className?: string;
};

function StreamCamera({ stream, className }: StreamCameraType) {
    useEffect(() => {
        const rtspWsUrl = process.env.REACT_APP_STREAM_WS_URI + stream.port;

        const jsmpeg = require("jsmpeg");
        const canvas = document.getElementById("stream-canvas-" + stream.cameraId);
        const client = new WebSocket(rtspWsUrl);
        const player = new jsmpeg(client, { canvas: canvas });
    }, []);

    return <canvas className={className} id={`stream-canvas-${stream.cameraId}`}></canvas>;
}

export default StreamCamera;
