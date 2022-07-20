import React, { useState } from "react";
import { Box, Card, Collapse } from "@mui/material";

import styles from "./ChartStatWrapper.module.css";

import * as Common from "../commons/common";

import ArrowDropUpIcon from "@mui/icons-material/ArrowDropUp";
import ChartStat from "./ChartStat";

function ChartStatWrapper({
    title,
    loading,
    series,
}: {
    title: string;
    loading: boolean;
    series: Common.ChartData[];
}) {
    const [showChart, setShowChart] = useState<boolean>(true);

    const commonChartOption = {
        chart: {
            height: "500px",
            type: "bar",
            stacked: true,
        },
        colors: ["#224d99", "#3b84ff", "#17e6b1", "#ffd500", "#ff5b7d"],
        title: {
            align: "center",
        },
        grid: {
            show: true,
            padding: {
                top: 20,
                right: 0,
                bottom: 20,
                left: 0,
            },
        },
    };

    const showLoading = () => {
        let showClass = styles.chartLoadingSpinnerFadeEnd;
        if (loading) {
            showClass = styles.chartLoadingSpinnerFade;
        }

        return (
            <div
                className={[styles.chartLoadingSpinnerOverlay, showClass].join(
                    " "
                )}
            >
                <div className={styles.chartLoadingSpinner}></div>
            </div>
        );
    };
    return (
        <Box className={styles.statChartWwrapper}>
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
                    {showLoading()}
                    <Box className={styles.statChart}>
                        <ChartStat
                            loading={loading}
                            series={series}
                            option={commonChartOption}
                        />
                    </Box>
                </Card>
            </Collapse>
        </Box>
    );
}

export default ChartStatWrapper;
