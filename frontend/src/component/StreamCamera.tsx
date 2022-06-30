import React, { useEffect } from "react";

type StreamCameraType = {
    stream: any;
};

function StreamCamera({ stream }: StreamCameraType) {
    //console.log("stream", stream);

    useEffect(() => {
        const rtspWsUrl = "ws://localhost:" + stream.port;

        const jsmpeg = require("jsmpeg");
        const canvas = document.getElementById(
            "stream-canvas-" + stream.cameraId
        );
        const client = new WebSocket(rtspWsUrl);
        const player = new jsmpeg(client, { canvas: canvas });
    }, []);

    return (
        <div
            style={{
                width: "400px",
                height: "300px",
                border: "solid 1px",
            }}
        >
            <canvas
                id={`stream-canvas-${stream.cameraId}`}
                style={{
                    width: "400px",
                    height: "300px",
                }}
            ></canvas>
        </div>
    );
}

export default StreamCamera;
