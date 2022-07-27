import React, { useState, useEffect } from "react";

import styles from "./RTSPStreamer.module.css";
import { Box, Slide } from "@mui/material";

import imgThumbnailTitle from "../assets/images/ico-cctv-thumbnail-title.svg";
import useResizeObserver from "use-resize-observer";
import StreamIntersection from "./StreamIntersection";

import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";

function RTSPStreamer({
    intersectionName,
    streamIntersectionCameras,
    selectedCameraNo,
    onChangedHeight,
    onChangedSelectedCameraNo,
}: {
    intersectionName: string | undefined;
    streamIntersectionCameras: any[];
    selectedCameraNo: string | null;
    onChangedHeight: (height: number) => void;
    onChangedSelectedCameraNo: (cameraNo: string) => void;
}) {
    const [openRtsp, setOpenRtsp] = useState<boolean>(false);
    const [showButton, setShowButton] = useState<boolean>(true);

    const { ref, height = 0 } = useResizeObserver<HTMLDivElement>();

    const handleOpenRtsp = () => {
        setOpenRtsp((prev) => !prev);
    };

    const onSlideEntering = () => {
        setShowButton(false);
    };

    const onSlideExited = () => {
        setShowButton(true);
    };

    useEffect(() => {
        setOpenRtsp(true);
    }, [intersectionName]);

    useEffect(() => {
        onChangedHeight(height);
    }, [height]);

    const onChangedSelectedCamera = (cameraNo: string) => {
        onChangedSelectedCameraNo(cameraNo);
    };

    return (
        <Box ref={ref} className={styles.rtspContainer}>
            {showButton ? (
                <div>
                    <button
                        className={styles.rtspSlideButton}
                        onClick={handleOpenRtsp}
                    >
                        <KeyboardArrowUpIcon fontSize='small' />
                    </button>
                    <Box className={styles.rtspTitlebar}>
                        <img src={imgThumbnailTitle} />
                        {intersectionName}
                    </Box>
                </div>
            ) : null}

            <Slide
                direction='up'
                in={openRtsp}
                mountOnEnter
                unmountOnExit
                onEntering={onSlideEntering}
                onExited={onSlideExited}
            >
                <div>
                    <button
                        className={styles.rtspSlideButton}
                        onClick={handleOpenRtsp}
                    >
                        {openRtsp ? (
                            <KeyboardArrowDownIcon fontSize='small' />
                        ) : (
                            <KeyboardArrowUpIcon fontSize='small' />
                        )}
                    </button>
                    <Box className={styles.rtspTitlebar}>
                        <img src={imgThumbnailTitle} />
                        {intersectionName}
                    </Box>

                    <Box className={styles.rtspWrapper}>
                        <StreamIntersection
                            selectedCameraNo={selectedCameraNo}
                            streamIntersectionCameras={
                                streamIntersectionCameras
                            }
                            onChangedSelectedCameraNo={onChangedSelectedCamera}
                        />
                    </Box>
                </div>
            </Slide>
        </Box>
    );
}

export default RTSPStreamer;
