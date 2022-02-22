import React, { useEffect } from "react";

function StreamCamera({ stream }) {
    //console.log("stream", stream);

    useEffect(() => {
        let rtspWsUrl = "ws://localhost:" + stream.port;

        var jsmpeg = require("jsmpeg");
        var canvas = document.getElementById(
            "stream-canvas-" + stream.cameraId
        );
        var client = new WebSocket(rtspWsUrl);
        var player = new jsmpeg(client, { canvas: canvas });
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
