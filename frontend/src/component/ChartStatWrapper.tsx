import React from "react";
import { Box, Card } from "@mui/material";

import styles from "./ChartStatWrapper.module.css";

import * as Common from "../commons/common";

import ArrowDropUpIcon from "@mui/icons-material/ArrowDropUp";
import ChartStat from "./ChartStat";
import { style } from "@mui/system";

function ChartStatWrapper({
    title,
    loading,
    series,
}: {
    title: string;
    loading: boolean;
    series: Common.ChartData[];
}) {
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
            <Box className={styles.statChartTitleWrapper}>
                <Box className={styles.statChartTitle}>
                    {title}
                    <ArrowDropUpIcon sx={{ fontSize: "44px" }} />
                </Box>
            </Box>
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
        </Box>
    );
}

export default ChartStatWrapper;
