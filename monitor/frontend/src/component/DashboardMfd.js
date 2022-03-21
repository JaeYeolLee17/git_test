import React, { useState, useEffect, useMemo } from "react";
import ChartMfd from "./ChartMfd";

import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios, useInterval } from "../utils/customHooks";

import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import TableMfd from "./TableMfd";

function DashboardMfd({ regionId, intersectionId }) {
    const userDetails = useAuthState();

    const [dataMfd, setDataMfd] = useState(null);
    const [dataLastWeekMfd, setDataLastWeekMfd] = useState(null);
    const [dataLastMonthAvgMfd, setDataLastMonthAvgMfd] = useState(null);

    useInterval(() => {
        let now = new Date();
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
        let startTime = Utils.utilFormatDateYYYYMMDD000000(new Date());

        var now = new Date();
        var nowMinute = now.getMinutes();
        var offsetMinute = nowMinute % 15;

        var end = new Date(now.getTime() - offsetMinute * (60 * 1000));
        let endTime = Utils.utilFormatDateYYYYMMDDHHmm00(end);

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        let extraParam = getExtraParams();

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
        let start = new Date();
        start.setDate(start.getDate() - 7);
        let startTime = Utils.utilFormatDateYYYYMMDD000000(start);

        let end = new Date(start);
        end.setDate(end.getDate() + 1);
        let endTime = Utils.utilFormatDateYYYYMMDD000000(end);

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        let extraParam = getExtraParams();

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

        //console.log("resultLastWeekMfd", resultLastWeekMfd);
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
        let start = new Date();
        start.setDate(start.getDate() - 28);
        let startTime = Utils.utilFormatDateYYYYMMDD000000(start);

        let end = new Date();
        let endTime = Utils.utilFormatDateYYYYMMDD000000(end);

        let dayOfWeek = end.getDay();
        if (dayOfWeek === 0) dayOfWeek = 7;

        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        let extraParam = getExtraParams();

        //console.log("extraParam", extraParam);

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

        //console.log("resultLastMonthAvgMfd", resultLastMonthAvgMfd);
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

    // TODO: loadingMfd || loadingLastWeekMfd || loadingLastMonthAvgMfd => loading image
    return (
        <div>
            <ChartMfd
                dataMfd={dataMfd}
                dataLastWeekMfd={dataLastWeekMfd}
                dataLastMonthAvgMfd={dataLastMonthAvgMfd}
            />
            <TableMfd
                dataMfd={dataMfd}
                dataLastWeekMfd={dataLastWeekMfd}
                dataLastMonthAvgMfd={dataLastMonthAvgMfd}
            />
        </div>
    );
}

export default DashboardMfd;
