import React, { useState } from "react";

import styles from "./RTSPStreamer.module.css";
import { Box, Slide } from "@mui/material";

import imgThumbnailTitle from "../assets/images/ico-cctv-thumbnail-title.svg";
import { Transition } from "react-transition-group";

function RTSPStreamer({
    intersectionName,
}: {
    intersectionName: string | undefined;
}) {
    const [openRtsp, setOpenRtsp] = useState<boolean>(false);

    const handleOpenRtsp = () => {
        setOpenRtsp((prev) => !prev);
    };

    return (
        <Box className={styles.rtspContainer}>
            <button className={styles.rtspSlideButton} onClick={handleOpenRtsp}>
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
            <Slide direction='up' in={openRtsp} mountOnEnter unmountOnExit>
                <Box className={styles.rtspWrapper}>
                    {/* <div
                    id='stream-cameras-container'
                    class='camera-cards-wrapper'
                ></div> */}
                </Box>
            </Slide>
        </Box>
    );
}

export default RTSPStreamer;
