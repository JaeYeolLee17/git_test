import { Slide, Box } from "@mui/material";
import React from "react";

import styles from "./TrafficInformation.module.css";

function TrafficInformation({
    show,
    time,
    bottom,
}: {
    show: boolean;
    time: string;
    bottom: number;
}) {
    return (
        <Slide direction='up' in={show} unmountOnExit>
            <Box
                className={styles.mapTrafficInformation}
                sx={{ bottom: `${bottom + 16}px` }}
            >
                <Box className={styles.mapTrafficWrapper}>
                    <Box className={styles.mapTrafficRow}>
                        <span>기준 :</span>
                        <span className={styles.mapTrafficMessage}>{time}</span>
                    </Box>
                    <Box className={styles.mapTrafficRow}>
                        <span
                            className={[
                                styles.mapTrafficColor,
                                styles.mapTrafficColorGreen,
                            ].join(" ")}
                        ></span>
                        <span>원활(30km/h▲)</span>
                    </Box>
                    <Box className={styles.mapTrafficRow}>
                        <span
                            className={[
                                styles.mapTrafficColor,
                                styles.mapTrafficColorOrange,
                            ].join(" ")}
                        ></span>
                        <span>정체(25km/h▲)</span>
                    </Box>
                    <Box className={styles.mapTrafficRow}>
                        <span
                            className={[
                                styles.mapTrafficColor,
                                styles.mapTrafficColorRed,
                            ].join(" ")}
                        ></span>
                        <span>혼잡(25km/h▼)</span>
                    </Box>
                </Box>
            </Box>
        </Slide>
    );
}

export default TrafficInformation;
