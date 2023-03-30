import React, { useState, useEffect } from "react";
import { Box } from "@mui/material";
import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";
// import SearchIcon from "@mui/icons-material/Search";

import styles from "./StatTraffic.module.css";

import * as Common from "../commons/common";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as String from "../commons/string";
import SelectorPeriod from "../component/SelectorPeriod";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import ChartStatWrapper from "../component/ChartStatWrapper";
import SearchButton from "../component/SearchButton";

function StatTraffic() {
    const userDetails = useAuthState();

    const [selectedRegionNo, setSelectedRegionNo] = useState<string>("");
    const [currentRegionInfo, setCurrentRegionInfo] = useState<Common.RegionInfo>({ regionNo: "all" });
    const [selectedIntersectionNo, setSelectedIntersectionNo] = useState<string>("");
    const [listIntersections, setListIntersections] = useState<Array<any>>([]);

    const [searchPeriod, setSearchPeriod] = useState<string>("");

    const [seriesSrluDatas, setSeriesSrluDatas] = useState<Array<Common.ChartData>>([]);
    const [seriesQtsrluDatas, setSeriesQtsrluDatas] = useState<Array<Common.ChartData>>([]);
    const [seriesSpeedDatas, setSeriesSpeedDatas] = useState<Array<Common.ChartData>>([]);
    const [seriesPersonDatas, setSeriesPersonDatas] = useState<Array<Common.ChartData>>([]);
    const [seriesMfdDatas, setSeriesMfdDatas] = useState<Array<Common.ChartData>>([]);

    const [firstLoad, setFirstLoad] = useState<boolean>(true);
    const [maxQtsrluUnitData, setMaxQtsrluUnitData] = useState<number>(0);
    const [offsetUnitData, setOffsetUnitData] = useState<number>(100);

    const onChangedCurrentRegion = (regionItem: Common.RegionInfo) => {
        setSelectedRegionNo(regionItem.regionNo);
        // setSelectedRegionName(regionItem.regionName);
        setCurrentRegionInfo(regionItem);
    };

    const onChangedCurrentIntersection = (intersectionItem: Common.IntersectionInfo) => {
        setSelectedIntersectionNo(intersectionItem.intersectionNo);
        // setSelectedIntersectionName(intersectionItem.intersectionName);
    };

    const onChangedPeriod = (period: string) => {
        setSearchPeriod(period);
    };

    const getStatDate = (period: string) => {
        // TODO: Code for Demo
        const now = new Date();

        let start = new Date(now);
        const end = new Date(now);
        if (period === "DAY") {
            start.setDate(start.getDate() - 7); // 7일전
        } else if (period === "WEEK_OF_YEAR" || period === "DAY_OF_WEEK") {
            let offsetDay = 1;
            if (end.getDay() == 0) offsetDay = -6;

            end.setDate(end.getDate() - end.getDay() + offsetDay);

            start = new Date(end);
            start.setDate(start.getDate() - 7 * 4); // 4주간
        } else if (period === "MONTH") {
            end.setMonth(end.getMonth(), 1);
            start.setMonth(start.getMonth() - 3, 1); // 3개월
        }

        const startYear = start.getFullYear();
        const startMonth = start.getMonth() + 1;
        const startDate = start.getDate();
        const startTime =
            startYear + "-" + (startMonth < 10 ? "0" + startMonth : startMonth) + "-" + (startDate < 10 ? "0" + startDate : startDate) + " 00:00:00";

        const endYear = end.getFullYear();
        const endMonth = end.getMonth() + 1;
        const endDate = end.getDate();
        const endTime = endYear + "-" + (endMonth < 10 ? "0" + endMonth : endMonth) + "-" + (endDate < 10 ? "0" + endDate : endDate) + " 00:00:00";

        return { startTime: startTime, endTime: endTime };
    };

    const getExtraParams = () => {
        let extraParam = {};

        if (selectedIntersectionNo === "" || selectedIntersectionNo === "all") {
            if (selectedRegionNo !== "" && selectedRegionNo !== "all") {
                extraParam = {
                    filterBy: "REGION",
                    filterValue: selectedRegionNo,
                };
            }
        } else {
            extraParam = {
                filterBy: "INTERSECTION",
                filterValue: selectedIntersectionNo,
            };
        }

        return extraParam;
    };

    const requestAxiosStatPeriod = async () => {
        const extraParam = getExtraParams();
        const { startTime, endTime } = getStatDate(searchPeriod);

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(Request.STAT_PERIOD_URL, {
            params: {
                startTime: startTime,
                endTime: endTime,
                period: searchPeriod,
                ...extraParam,
            },
        });

        return response.data;
    };

    const {
        loading: loadingStatPeriod,
        error: errorStatPeriod,
        data: resultStatPeriod,
        execute: requestStatPeriod,
    } = useAsyncAxios(requestAxiosStatPeriod);

    useEffect(() => {
        if (resultStatPeriod === null) return;
        if (Utils.utilIsEmptyArray(resultStatPeriod.stats) === true) return;

        const statData = resultStatPeriod.stats;

        const { seriesSrluDatas, seriesQtsrluDatas, seriesSpeedDatas, seriesPersonDatas, seriesMfdDatas, maxQtsrluData } =
            Utils.utilConvertChartSeriesPeriod(statData);

        setSeriesSrluDatas(seriesSrluDatas);
        setSeriesQtsrluDatas(seriesQtsrluDatas);
        setSeriesSpeedDatas(seriesSpeedDatas);
        setSeriesPersonDatas(seriesPersonDatas);
        setSeriesMfdDatas(seriesMfdDatas);

        let maxData = maxQtsrluData;

        let offsetUnit = 100;
        if (maxData > 10000) {
            offsetUnit = 1000;
        } else if (maxData < 100) {
            offsetUnit = 10;
        }

        const xDataOffset = offsetUnit - (maxData % offsetUnit);
        maxData += xDataOffset;

        setOffsetUnitData(offsetUnit);
        setMaxQtsrluUnitData(maxData);

        // setSeriesChartSrluCarType(seriesChartSrlu);
        // setSeriesChartQtsrluCarType(seriesChartQtsrlu);
        // if (requestCarTypeNCamera === false) {
        //     setSeriesChartPerson(seriesChartPerson);
        // }
    }, [resultStatPeriod]);

    useEffect(() => {
        if (errorStatPeriod === null) return;

        console.log("errorStatPeriod", errorStatPeriod);
    }, [errorStatPeriod]);

    useEffect(() => {
        if (firstLoad === true) {
            if (selectedRegionNo !== "" && selectedIntersectionNo !== "" && searchPeriod !== "") {
                onSearch();
                setFirstLoad(false);
            }
        }
    }, [selectedRegionNo, selectedIntersectionNo, searchPeriod]);

    const onSearch = () => {
        requestStatPeriod();
    };

    const commonChartOptions = {
        chart: {
            height: "500px",
            type: "line",
        },
        plotOptions: {
            line: {
                curve: "smooth",
            },
            bar: {
                borderRadius: 2,
                columnWidth: "85%",
            },
        },
        stroke: {
            width: [2, 2, 2, 2, 2, 2, 2],
        },

        colors: ["#3a9f00", "#f0483e", "#fb9a02", "#4579bd", "#8866ac", "#fdb714", "#43d795"],

        axisBorder: {
            show: true,
            color: "#a9abb3",
            height: 1,
            width: "100%",
        },

        markers: {
            size: 5,
            shape: "circle",
        },
    };

    const optionsPersion = {
        ...commonChartOptions,
        chart: {
            ...commonChartOptions.chart,
            type: "bar",
        },
    };

    const optionMfd = {
        ...commonChartOptions,
        dataLabels: {
            enabled: true,
            formatter: function (
                value: any,
                {
                    seriesIndex,
                    dataPointIndex,
                    w,
                }: {
                    seriesIndex: number;
                    dataPointIndex: number;
                    w: any;
                }
            ) {
                return Utils.utilLeadingZeros(dataPointIndex, 2) + ":00";
            },
            style: {
                colors: [
                    function ({ seriesIndex, dataPointIndex, w }: { seriesIndex: number; dataPointIndex: number; w: any }) {
                        if (w.config.series[seriesIndex].data[dataPointIndex] === undefined) return;

                        const yValue = w.config.series[seriesIndex].data[dataPointIndex].y;
                        const xValue = w.config.series[seriesIndex].data[dataPointIndex].x;

                        return Utils.utilGetSpeedColor(yValue / xValue);
                    },
                ],
            },
        },
        xaxis: {
            type: "numeric",
            min: 0,
            max: maxQtsrluUnitData,
            tickAmount: maxQtsrluUnitData / offsetUnitData,
            tooltip: {
                enabled: false,
            },
            crosshairs: {
                show: false,
            },
            labels: {
                formatter: function (val: number, index: number) {
                    return val.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                },
            },
        },

        tooltip: {
            x: {
                show: false,
            },
            y: {
                formatter: function (
                    value: any,
                    {
                        series,
                        seriesIndex,
                        dataPointIndex,
                        w,
                    }: {
                        series: any[];
                        seriesIndex: number;
                        dataPointIndex: number;
                        w: any;
                    }
                ) {
                    const xValue = w.config.series[seriesIndex].data[dataPointIndex].x.toFixed(2);
                    const yValue = w.config.series[seriesIndex].data[dataPointIndex].y.toFixed(2);
                    const dataTime = w.config.series[seriesIndex].name + " " + Utils.utilLeadingZeros(dataPointIndex, 2) + ":00";
                    return dataTime + "<br>" + String.stats_qtsrlu + " : " + xValue + "<br>" + String.stats_srlu + " : " + yValue;
                },
                title: {
                    formatter: function (
                        value: any,
                        {
                            series,
                            seriesIndex,
                            dataPointIndex,
                            w,
                        }: {
                            series: any[];
                            seriesIndex: number;
                            dataPointIndex: number;
                            w: any;
                        }
                    ) {
                        return "";
                    },
                },
            },
        },
    };

    return (
        <Box sx={{ display: "flex", flexDirection: "column" }}>
            <Box className={styles.searchBar}>
                <ul className={styles.searchBarUl}>
                    <li>
                        <SelectorPeriod onChangedPeriod={onChangedPeriod} />
                    </li>
                    <li>
                        <SelectorRegion selectedRegionNo={selectedRegionNo} onChangedCurrentRegion={onChangedCurrentRegion} />
                    </li>
                    <li>
                        <SelectorIntersection
                            currentRegionInfo={currentRegionInfo}
                            selectedIntersectionNo={selectedIntersectionNo}
                            onChangedIntersectionList={(list) => {
                                setListIntersections(list);
                            }}
                            onChangedCurrentIntersection={onChangedCurrentIntersection}
                        />
                    </li>
                    <li>
                        {/* <button
                            type='button'
                            className={styles.searchBtn}
                            onClick={onSearch}
                        >
                            <SearchIcon />
                            <span>검색</span>
                        </button> */}
                        <SearchButton onSearch={onSearch} />
                    </li>
                </ul>
            </Box>
            <Box className={styles.statCharts}>
                <ChartStatWrapper title={String.stats_title_srlu} loading={loadingStatPeriod} series={seriesSrluDatas} option={commonChartOptions} />
                <ChartStatWrapper
                    title={String.stats_title_qtsrlu}
                    loading={loadingStatPeriod}
                    series={seriesQtsrluDatas}
                    option={commonChartOptions}
                />
                <ChartStatWrapper
                    title={String.stats_title_speed}
                    loading={loadingStatPeriod}
                    series={seriesSpeedDatas}
                    option={commonChartOptions}
                />
                <ChartStatWrapper title={String.stats_title_mfd} loading={loadingStatPeriod} series={seriesMfdDatas} option={optionMfd} />
                <ChartStatWrapper title={String.stats_title_person} loading={loadingStatPeriod} series={seriesPersonDatas} option={optionsPersion} />
            </Box>
        </Box>
    );
}

// export const stats_title_srlu = "시간대별 통과 차량 수";
// export const stats_title_qtsrlu = "시간대별 평균 통과 예정 차량 수";
// export const stats_title_cartype_srlu = "시간대별 통과 차량 수(차종별)";
// export const stats_title_cartype_qtsrlu =
//     "시간대별 평균 통과 예정 차량 수(차종별)";
// export const stats_title_camera_srlu = "시간대별 통과 차량 수(방향별)";
// export const stats_title_camera_qtsrlu =
//     "시간대별 평균 통과 예정 차량 수(방향별)";
// export const stats_title_speed = "시간대별 평균 속도";
// export const stats_title_mfd = "시간대별 교통 흐름 상태";
// export const stats_title_person = "시간대별 보행자 수";
export default StatTraffic;
