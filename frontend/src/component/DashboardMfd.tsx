import React, { useState, useEffect, useMemo } from "react";
import ChartMfd from "./ChartMfd";

import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios, useInterval } from "../utils/customHooks";

import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import TableMfd from "./TableMfd";

import styles from "./DashboardMfd.module.css";
import { Box } from "@mui/material";

function DashboardMfd({
    regionId,
    regionName,
    intersectionId,
    intersectionName,
}: {
    regionId: string;
    regionName: string | undefined;
    intersectionId: string;
    intersectionName: string | undefined;
}) {
    const userDetails = useAuthState();

    const [dataMfd, setDataMfd] = useState<any>(null);
    const [dataLastWeekMfd, setDataLastWeekMfd] = useState<any>(null);
    const [dataLastMonthAvgMfd, setDataLastMonthAvgMfd] = useState<any>(null);

    useInterval(() => {
        const now = new Date();
        if (now.getSeconds() === 0) {
            requestMfd();
        }
    }, 1000);

    const getExtraParams = () => {
        let extraParam = {};

        if (intersectionId === "" || intersectionId === "all") {
            if (regionId !== "" && regionId !== "all") {
                extraParam = {
                    filterBy: "region",
                    filterId: regionId,
                };
            }
        } else {
            extraParam = {
                filterBy: "intersection",
                filterId: intersectionId,
            };
        }

        return extraParam;
    };

    const requestAxiosMfd = async () => {
        const startTime = Utils.utilFormatDateYYYYMMDD000000(new Date());

        const now = new Date();
        const nowMinute = now.getMinutes();
        const offsetMinute = nowMinute % 15;

        const end = new Date(now.getTime() - offsetMinute * (60 * 1000));
        const endTime = Utils.utilFormatDateYYYYMMDDHHmm00(end);

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        const extraParam = getExtraParams();

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.STAT_MFD_URL,
            {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                    ...extraParam,
                },
            }
        );

        return response.data;
    };

    const {
        loading: loadingMfd,
        error: errorMfd,
        data: resultMfd,
        execute: requestMfd,
    } = useAsyncAxios(requestAxiosMfd);

    useEffect(() => {
        if (resultMfd === null) return;

        //console.log("resultMfd", JSON.stringify(resultMfd.stat[0]));
        //console.log("resultMfd", resultMfd);
        setDataMfd(resultMfd.stat[0]);
    }, [resultMfd]);

    useEffect(() => {
        if (errorMfd === null) return;

        console.log("errorMfd", errorMfd);
    }, [errorMfd]);

    // const requestMfd = async (e) => {
    //     let startTime = Utils.utilFormatDateYYYYMMDD000000(new Date());

    //     var now = new Date();
    //     var nowMinute = now.getMinutes();
    //     var offsetMinute = nowMinute % 15;

    //     var end = new Date(now.getTime() - offsetMinute * (60 * 1000));
    //     let endTime = Utils.utilFormatDateYYYYMMDDHHmm00(end);

    //     //console.log("startTime", startTime);
    //     //console.log("endTime", endTime);

    //     let extraParam = getExtraParams();

    //     //console.log("extraParam", extraParam);

    //     try {
    //         //console.log(userDetails.token);
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.STAT_MFD_URL, {
    //             params: {
    //                 startTime: startTime,
    //                 endTime: endTime,
    //                 ...extraParam,
    //             },
    //         });

    //         //console.log(JSON.stringify(response?.data));
    //         setDataMfd(response?.data?.stat[0]);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    const requestAxiosLastWeekMfd = async () => {
        const start = new Date();
        start.setDate(start.getDate() - 7);
        const startTime = Utils.utilFormatDateYYYYMMDD000000(start);

        const end = new Date(start);
        end.setDate(end.getDate() + 1);
        const endTime = Utils.utilFormatDateYYYYMMDD000000(end);

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        const extraParam = getExtraParams();

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.STAT_MFD_URL,
            {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                    ...extraParam,
                },
            }
        );

        return response.data;
    };

    const {
        loading: loadingLastWeekMfd,
        error: errorLastWeekMfd,
        data: resultLastWeekMfd,
        execute: requestLastWeekMfd,
    } = useAsyncAxios(requestAxiosLastWeekMfd);

    useEffect(() => {
        if (resultLastWeekMfd === null) return;

        // console.log(
        //     "resultLastWeekMfd",
        //     JSON.stringify(resultLastWeekMfd.stat[0])
        // );
        setDataLastWeekMfd(resultLastWeekMfd.stat[0]);
    }, [resultLastWeekMfd]);

    useEffect(() => {
        if (errorLastWeekMfd === null) return;

        console.log("errorLastWeekMfd", errorLastWeekMfd);
    }, [errorLastWeekMfd]);

    // const requestLastWeekMfd = async (e) => {
    //     let start = new Date();
    //     start.setDate(start.getDate() - 7);
    //     let startTime = Utils.utilFormatDateYYYYMMDD000000(start);

    //     let end = new Date(start);
    //     end.setDate(end.getDate() + 1);
    //     let endTime = Utils.utilFormatDateYYYYMMDD000000(end);

    //     //console.log("startTime", startTime);
    //     //console.log("endTime", endTime);

    //     let extraParam = getExtraParams();

    //     //console.log("extraParam", extraParam);

    //     try {
    //         //console.log(userDetails.token);
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.STAT_MFD_URL, {
    //             params: {
    //                 startTime: startTime,
    //                 endTime: endTime,
    //                 ...extraParam,
    //             },
    //         });

    //         //console.log(JSON.stringify(response?.data));
    //         setDataLastWeekMfd(response?.data?.stat[0]);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    const requestAxiosLastMonthAvgMfd = async () => {
        const start = new Date();
        start.setDate(start.getDate() - 28);
        const startTime = Utils.utilFormatDateYYYYMMDD000000(start);

        const end = new Date();
        const endTime = Utils.utilFormatDateYYYYMMDD000000(end);

        let dayOfWeek = end.getDay();
        if (dayOfWeek === 0) dayOfWeek = 7;

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        const extraParam = getExtraParams();

        //console.log("extraParam", extraParam);

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.STAT_MFD_URL,
            {
                params: {
                    startTime: startTime,
                    endTime: endTime,
                    ...extraParam,
                    dayOfWeek: dayOfWeek,
                },
            }
        );

        return response.data;
    };

    const {
        loading: loadingLastMonthAvgMfd,
        error: errorLastMonthAvgMfd,
        data: resultLastMonthAvgMfd,
        execute: requestLastMonthAvgMfd,
    } = useAsyncAxios(requestAxiosLastMonthAvgMfd);

    useEffect(() => {
        if (resultLastMonthAvgMfd === null) return;

        // console.log(
        //     "resultLastMonthAvgMfd",
        //     JSON.stringify(resultLastMonthAvgMfd.stat[0])
        // );
        setDataLastMonthAvgMfd(resultLastMonthAvgMfd.stat[0]);
    }, [resultLastMonthAvgMfd]);

    useEffect(() => {
        if (errorLastMonthAvgMfd === null) return;

        console.log("errorLastMonthAvgMfd", errorLastMonthAvgMfd);
    }, [errorLastMonthAvgMfd]);

    // const requestLastMonthAvgMfd = async (e) => {
    //     let start = new Date();
    //     start.setDate(start.getDate() - 28);
    //     let startTime = Utils.utilFormatDateYYYYMMDD000000(start);

    //     let end = new Date();
    //     let endTime = Utils.utilFormatDateYYYYMMDD000000(end);

    //     let dayOfWeek = end.getDay();
    //     if (dayOfWeek === 0) dayOfWeek = 7;

    //     //console.log("startTime", startTime);
    //     //console.log("endTime", endTime);

    //     let extraParam = getExtraParams();

    //     //console.log("extraParam", extraParam);

    //     try {
    //         //console.log(userDetails.token);
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.STAT_MFD_URL, {
    //             params: {
    //                 startTime: startTime,
    //                 endTime: endTime,
    //                 ...extraParam,
    //                 dayOfWeek: dayOfWeek,
    //             },
    //         });

    //         //console.log(JSON.stringify(response?.data));
    //         setDataLastMonthAvgMfd(response?.data?.stat[0]);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    useEffect(() => {
        if (regionId === "" || intersectionId === "") return;

        setDataLastWeekMfd(null);
        setDataLastMonthAvgMfd(null);

        requestMfd();
        requestLastWeekMfd();
        requestLastMonthAvgMfd();
    }, [regionId, intersectionId]);

    const showLoading = () => {
        let showClass = styles.chartLoadingSpinnerFadeEnd;
        if (loadingMfd || loadingLastWeekMfd || loadingLastMonthAvgMfd) {
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

    // TODO: loadingMfd || loadingLastWeekMfd || loadingLastMonthAvgMfd => loading image
    // console.log("loadingMfd", loadingMfd);
    // console.log("loadingLastWeekMfd", loadingLastWeekMfd);
    // console.log("loadingLastMonthAvgMfd", loadingLastMonthAvgMfd);
    return (
        <div>
            <Box className={styles.chartTitle}>
                <Box className={styles.chartTitleTop}>교통흐름현황</Box>
                <Box className={styles.chartTitleBottom}>
                    {intersectionName !== undefined ? (
                        <span>{intersectionName}</span>
                    ) : regionName !== undefined ? (
                        <span>{regionName}</span>
                    ) : (
                        <span>대구광역시 전체</span>
                    )}
                </Box>
            </Box>
            <Box className={styles.chartMiddle}>
                <Box className={styles.chartMiddleWrapper}>
                    {showLoading()}
                    <ChartMfd
                        dataMfd={dataMfd}
                        dataLastWeekMfd={dataLastWeekMfd}
                        dataLastMonthAvgMfd={dataLastMonthAvgMfd}
                    />
                </Box>
            </Box>
            <Box className={styles.chartLabel}>
                <span
                    className={styles.chartLabelLegend}
                    style={{ backgroundColor: "#00e025" }}
                ></span>
                <span>원활(30km/h▲)</span>
                <span
                    className={styles.chartLabelLegend}
                    style={{ backgroundColor: "#ff8800" }}
                ></span>
                <span>정체(25km/h▲)</span>
                <span
                    className={styles.chartLabelLegend}
                    style={{ backgroundColor: "#df0900" }}
                ></span>
                <span>혼잡(25km/h▼)</span>
            </Box>
            <Box className={styles.chartTableWapper}>
                <TableMfd
                    dataMfd={dataMfd}
                    dataLastWeekMfd={dataLastWeekMfd}
                    dataLastMonthAvgMfd={dataLastMonthAvgMfd}
                />
            </Box>
        </div>
    );
}

export default DashboardMfd;
