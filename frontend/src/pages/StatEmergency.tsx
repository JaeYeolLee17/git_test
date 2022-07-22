import { Box, Grid } from "@mui/material";
import React, { useEffect, useState } from "react";
import SearchButton from "../component/SearchButton";
import SelectorPeriod from "../component/SelectorPeriod";
import Chart from "react-apexcharts";

import styles from "./StatEmergency.module.css";
import LoadingChartSpinner from "../component/LoadingChartSpinner";
import { useAuthState } from "../provider/AuthProvider";

// import * as Common from "../commons/common";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import { useAsyncAxios } from "../utils/customHooks";
// import * as String from "../commons/string";

function StatEmergency() {
    const userDetails = useAuthState();

    const [chartOption, setChartOption] = useState<Record<string, any>>({});

    const [searchPeriod, setSearchPeriod] = useState<string>("day");

    const optionEmergency = {
        chart: {
            height: "500px",
            type: "line",
            stacked: false,

            toolbar: {
                show: false,
                tools: {
                    download: false,
                    selection: false,
                    zoom: false,
                    zoomin: false,
                    zoomout: false,
                    pan: false,
                    customIcons: [],
                },
            },
        },
        dataLabels: {
            enabled: false,
        },

        colors: [
            "#3a9f00",
            "#f0483e",
            "#fb9a02",
            "#4579bd",
            "#8866ac",
            "#fdb714",
            "#43d795",
        ],

        series: [
            {
                name: "평균소요시간",
                type: "line",
                data: [],
            },
            {
                name: "출동횟수",
                type: "column",
                data: [],
            },
        ],

        stroke: {
            width: [4, 4],
        },
        markers: {
            size: 5,
            shape: "circle",
        },
        plotOptions: {
            bar: {
                columnWidth: "20%",
            },
        },

        title: {
            align: "center",
        },

        xaxis: {
            type: "category",
            categories: [],
            labels: {
                formatter: function (val: any) {
                    return val;
                },
            },
            axisBorder: {
                show: true,
                color: "#a9abb3",
                height: 1,
                width: "100%",
            },
        },

        yaxis: [
            {
                seriesName: "avgTime",
                labels: {
                    formatter: function (val: any) {
                        return toMMSS(val);
                    },
                    style: {
                        colors: "#666666",
                        fontFamily: "NotoSansCJKKR",
                        fontSize: "13px",
                    },
                },
                axisBorder: {
                    show: true,
                    color: "#3a9f00",
                },
                axisTicks: {
                    show: true,
                },

                title: {
                    text: "평균소요시간",
                    style: {
                        colors: "#666666",
                        fontFamily: "NotoSansCJKKR",
                        fontSize: "13px",
                        fontWeight: "500",
                    },
                },
            },
            {
                opposite: true,
                seriesName: "numDispatch",
                labels: {
                    formatter: function (val: any) {
                        return val.toFixed(0);
                    },
                    style: {
                        colors: "#666666",
                        fontFamily: "NotoSansCJKKR",
                        fontSize: "13px",
                    },
                },
                axisBorder: {
                    show: true,
                    color: "#f0483e",
                },
                axisTicks: {
                    show: true,
                },
                title: {
                    text: "출동횟수",
                    style: {
                        colors: "#666666",
                        fontFamily: "NotoSansCJKKR",
                        fontSize: "13px",
                        fontWeight: "500",
                    },
                },
            },
        ],
        grid: {
            xaxis: {
                lines: {
                    show: true,
                },
            },
            yaxis: {
                lines: {
                    show: true,
                },
            },
        },

        noData: {
            text: "조회된 데이터 없음",
            align: "center",
            verticalAlign: "middle",
            offsetX: 0,
            offsetY: 0,
            style: {
                color: "#8b0000",
                fontSize: "32px",
                fontFamily: "Noto Sans KR",
            },
        },
    };

    useEffect(() => {
        setChartOption(optionEmergency);
    }, []);

    const toMMSS = (s: string) => {
        const sec_num = parseInt(s, 10);
        const hours = Math.floor(sec_num / 3600);
        const minutes = Math.floor((sec_num - hours * 3600) / 60);
        const seconds = sec_num - hours * 3600 - minutes * 60;

        let minuteString = minutes.toString();
        let secondsString = seconds.toString();
        if (minutes < 10) {
            minuteString = "0" + minutes.toString();
        }
        if (seconds < 10) {
            secondsString = "0" + seconds.toString();
        }
        return minuteString + "분 " + secondsString + "초";
    };

    const onChangedPeriod = (period: string) => {
        setSearchPeriod(period);
    };

    const getStatDate = (period: string) => {
        const now = new Date();
        let start = new Date(now);
        const end = new Date(now);

        if (period == "day") {
            start.setDate(start.getDate() - 7); // 7일전
        } else if (period == "weekOfYear" || period == "dayOfWeek") {
            let offsetDay = 1;
            if (end.getDay() == 0) offsetDay = -6;

            end.setDate(end.getDate() - end.getDay() + offsetDay);

            start = new Date(end);
            start.setDate(start.getDate() - 7 * 4); // 4주간
        } else if (period == "month") {
            end.setMonth(end.getMonth(), 1);
            start.setMonth(start.getMonth() - 3, 1); // 3개월
        }

        const startYear = start.getFullYear();
        const startMonth = start.getMonth() + 1;
        const startDate = start.getDate();
        const startTime =
            startYear +
            "-" +
            (startMonth < 10 ? "0" + startMonth : startMonth) +
            "-" +
            (startDate < 10 ? "0" + startDate : startDate) +
            " 00:00:00";

        const endYear = end.getFullYear();
        const endMonth = end.getMonth() + 1;
        const endDate = end.getDate();
        const endTime =
            endYear +
            "-" +
            (endMonth < 10 ? "0" + endMonth : endMonth) +
            "-" +
            (endDate < 10 ? "0" + endDate : endDate) +
            " 00:00:00";

        return { startTime: startTime, endTime: endTime };
    };

    const requestAxiosStatEmergencyPeriod = async () => {
        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        const { startTime, endTime } = getStatDate(searchPeriod);

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.AVL_STAT_PERIOD_URL,
            {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                    byPeriod: searchPeriod,
                },
            }
        );

        return response.data;
    };

    const {
        loading: loadingStatEmergencyPeriod,
        error: errorStatEmergencyPeriod,
        data: resultStatEmergencyPeriod,
        execute: requestStatEmergencyPeriod,
    } = useAsyncAxios(requestAxiosStatEmergencyPeriod);

    useEffect(() => {
        if (resultStatEmergencyPeriod === null) return;
        console.log(
            "resultStatEmergencyPeriod",
            JSON.stringify(resultStatEmergencyPeriod)
        );
    }, [resultStatEmergencyPeriod]);

    useEffect(() => {
        if (errorStatEmergencyPeriod === null) return;

        console.log("errorStatEmergencyPeriod", errorStatEmergencyPeriod);
    }, [errorStatEmergencyPeriod]);

    const requestAxiosStatEmergencyList = async () => {
        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        const { startTime, endTime } = getStatDate(searchPeriod);

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.AVL_STAT_LIST_URL,
            {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                },
            }
        );

        return response.data;
    };

    const {
        loading: loadingStatEmergencyList,
        error: errorStatEmergencyList,
        data: resultStatEmergencyList,
        execute: requestStatEmergencyList,
    } = useAsyncAxios(requestAxiosStatEmergencyList);

    useEffect(() => {
        if (resultStatEmergencyList === null) return;
        console.log(
            "resultStatEmergencyList",
            JSON.stringify(resultStatEmergencyList)
        );
    }, [resultStatEmergencyList]);

    useEffect(() => {
        if (errorStatEmergencyList === null) return;

        console.log("errorStatEmergencyList", errorStatEmergencyList);
    }, [errorStatEmergencyList]);

    const onSearch = () => {
        requestStatEmergencyPeriod();
        requestStatEmergencyList();
    };

    return (
        <>
            <Box className={styles.emergencyStatRow}>
                <Grid
                    item
                    lg={12}
                    md={12}
                    sm={12}
                    xs={12}
                    sx={{ padding: 0, width: "100%" }}
                >
                    <Box className={styles.chartTitle}>
                        <Box className={styles.chartTitleText}>
                            긴급차량 평균소요시간 추이
                        </Box>
                        <Box className={styles.chartSearch}>
                            <Box sx={{ marginRight: "12px" }}>
                                <SelectorPeriod
                                    onChangedPeriod={onChangedPeriod}
                                />
                            </Box>
                            <SearchButton onSearch={onSearch} />
                        </Box>
                    </Box>
                    <Box className={styles.statChartCard}>
                        <Box className={styles.statChart}>
                            <LoadingChartSpinner
                                loading={loadingStatEmergencyPeriod}
                            />
                            <Chart
                                options={chartOption}
                                series={optionEmergency.series}
                                type='line'
                                width='100%'
                                height={optionEmergency.chart.height}
                            ></Chart>
                        </Box>
                    </Box>
                </Grid>
            </Box>
            <Box className={styles.recordTitle}>출동 기록 경로</Box>
            <Box
                className={[styles.emergencyStatRow, styles.record].join(" ")}
            ></Box>
        </>
    );
}

export default StatEmergency;
