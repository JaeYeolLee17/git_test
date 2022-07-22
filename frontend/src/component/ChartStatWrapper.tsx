import React, { useState } from "react";
import { Box, Card, Collapse } from "@mui/material";

import styles from "./ChartStatWrapper.module.css";

import * as Common from "../commons/common";

import ArrowDropUpIcon from "@mui/icons-material/ArrowDropUp";
import ChartStat from "./ChartStat";
import LoadingChartSpinner from "./LoadingChartSpinner";

function ChartStatWrapper({
    title,
    loading,
    series,
    option,
}: {
    title: string;
    loading: boolean;
    series: Common.ChartData[];
    option: Record<string, any>;
}) {
    const [showChart, setShowChart] = useState<boolean>(true);

    // const showLoading = () => {
    //     let showClass = styles.chartLoadingSpinnerFadeEnd;
    //     if (loading) {
    //         showClass = styles.chartLoadingSpinnerFade;
    //     }

    //     return (
    //         <div
    //             className={[styles.chartLoadingSpinnerOverlay, showClass].join(
    //                 " "
    //             )}
    //         >
    //             <div className={styles.chartLoadingSpinner}></div>
    //         </div>
    //     );
    // };
    return (
        <Box className={styles.statChartWrapper}>
            <Box
                className={styles.statChartTitleWrapper}
                onClick={() => setShowChart(!showChart)}
            >
                <Box className={styles.statChartTitle}>
                    {title}
                    <ArrowDropUpIcon
                        sx={{
                            fontSize: "44px",
                            transition: "0.3s",
                            transform: showChart ? "" : "rotate(0.5turn)",
                        }}
                    />
                </Box>
            </Box>
            <Collapse in={showChart}>
                <Card className={styles.statChartCard}>
                    {/* {showLoading()} */}
                    <LoadingChartSpinner loading={loading} />
                    <Box className={styles.statChart}>
                        <ChartStat
                            loading={loading}
                            series={series}
                            option={option}
                        />
                    </Box>
                </Card>
            </Collapse>
        </Box>
    );
}

export default ChartStatWrapper;
