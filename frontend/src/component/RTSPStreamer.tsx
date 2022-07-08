import React, { useState, useEffect } from "react";

import styles from "./RTSPStreamer.module.css";
import { Box, Slide } from "@mui/material";

import imgThumbnailTitle from "../assets/images/ico-cctv-thumbnail-title.svg";
import useResizeObserver from "use-resize-observer";

function RTSPStreamer({
    intersectionName,
    onChangedHeight,
}: {
    intersectionName: string | undefined;
    onChangedHeight: (height: number) => void;
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

    return (
        <Box ref={ref} className={styles.rtspContainer}>
            {showButton ? (
                <div>
                    <button
                        className={styles.rtspSlideButton}
                        onClick={handleOpenRtsp}
                    >
                        open
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
                        open
                    </button>
                    {/* <Transition in={openRtsp} timeout={300}>
                {(state: string) => {
                    let extraClass = "";
                    if (state === "entering" || state === "entered") {
                        extraClass = styles.rtspTitlebarExpend;
                    }
                    return (
                        <Box
                            className={[styles.rtspTitlebar, extraClass].join(
                                " "
                            )}
                        >
                            <img src={imgThumbnailTitle} />
                            {intersectionName}
                        </Box>
                    );
                }}
            </Transition> */}
                    <Box className={styles.rtspTitlebar}>
                        <img src={imgThumbnailTitle} />
                        {intersectionName}
                    </Box>

                    <Box className={styles.rtspWrapper}>
                        {/* <div
                    id='stream-cameras-container'
                    class='camera-cards-wrapper'
                ></div> */}
                    </Box>
                </div>
            </Slide>
        </Box>
    );
}

export default RTSPStreamer;
