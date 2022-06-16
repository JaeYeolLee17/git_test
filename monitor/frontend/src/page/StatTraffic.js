import React, { useState, useEffect } from "react";
import HeaderContent from "../component/HeaderContent";
import Menu from "../component/Menu";
import Selector from "../component/Selector";
import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";

import * as Request from "../commons/request";
import * as Utils from "../utils/utils";
import * as String from "../commons/string";
import { useAsyncAxios } from "../utils/customHooks";
import { useAuthState } from "../provider/AuthProvider";
import { useAlert } from "react-alert";
import ChartStat from "../component/ChartStat";

function StatTraffic() {
    const listPeriod = [
        { value: "day", innerHTML: "day" },
        { value: "weekOfYear", innerHTML: "weekOfYear" },
        { value: "month", innerHTML: "month" },
        { value: "dayOfWeek", innerHTML: "dayOfWeek" },
    ];

    const option = {
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

        colors: [
            "#3a9f00",
            "#f0483e",
            "#fb9a02",
            "#4579bd",
            "#8866ac",
            "#fdb714",
            "#43d795",
        ],
        title: {
            align: "center",
        },

        markers: {
            size: 5,
            shape: "circle",
        },
    };

    // const commonOption = {
    //     chart: {
    //         height: "500px",
    //         type: "line",

    //         toolbar: {
    //             show: false,
    //         },
    //         zoom: {
    //             enabled: false,
    //             type: "x",
    //         },
    //         animations: {
    //             enabled: true,
    //             easing: "linear",
    //             dynamicAnimation: {
    //                 speed: 500,
    //             },
    //         },
    //         events: {
    //             animationEnd: (chartContext, options) => {
    //                 drawYaxisLine(chartContext);
    //             },
    //         },
    //     },

    //     plotOptions: {
    //         line: {
    //             curve: "smooth",
    //         },
    //         bar: {
    //             borderRadius: 2,
    //             columnWidth: "85%",
    //         },
    //     },
    //     stroke: {
    //         width: [2, 2, 2, 2, 2, 2, 2],
    //     },

    //     colors: [
    //         "#3a9f00",
    //         "#f0483e",
    //         "#fb9a02",
    //         "#4579bd",
    //         "#8866ac",
    //         "#fdb714",
    //         "#43d795",
    //     ],
    //     title: {
    //         align: "center",
    //     },

    //     markers: {
    //         size: 5,
    //         shape: "circle",
    //     },
    //     dataLabels: {
    //         enabled: false,
    //     },

    //     xaxis: {
    //         type: "numeric",
    //         min: 0,
    //         max: 23,
    //         tickAmount: 24,

    //         labels: {
    //             formatter: function (val, index) {
    //                 var formattedNumber =
    //                     ("0" + val.toFixed(0)).slice(-2) + ":00";
    //                 return formattedNumber;
    //             },
    //         },
    //     },
    //     yaxis: {
    //         labels: {
    //             formatter: function (val, index) {
    //                 return val.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    //             },
    //         },
    //     },
    //     tooltip: {
    //         intersect: true,
    //         shared: false,
    //         marker: {
    //             show: false,
    //         },

    //         x: {
    //             show: false,
    //         },
    //     },
    //     legend: {
    //         showForSingleSeries: true,
    //     },
    //     noData: {
    //         text: String.chart_no_data,
    //         align: "center",
    //         verticalAlign: "middle",
    //         offsetX: 0,
    //         offsetY: 0,
    //         style: {
    //             color: "#8b0000",
    //             fontSize: "32px",
    //             fontFamily: "Noto Sans KR",
    //         },
    //     },
    // };

    // const drawYaxisLine = (chart) => {
    //     var yaxisData = [];
    //     var yDatas = chart.w.globals.yAxisScale[0].result;

    //     yDatas.forEach((yData) => {
    //         yaxisData.push({
    //             y: yData,
    //             strokeDashArray: 1,
    //             borderColor: "#a9abb3",
    //         });
    //     });

    //     // setChartOption({
    //     //     ...chartOption,
    //     //     annotations: {
    //     //         position: "back",
    //     //         yaxis: yaxisData,
    //     //     },
    //     // });
    // };

    const optionPersion = {
        ...option,
        chart: {
            ...option.chart,
            type: "bar",
        },
        dataLabels: {
            enabled: false,
        },
    };

    const chartInitData = {
        seriesSrluDatas: [],
        seriesQtsrluDatas: [],
        seriesSpeedDatas: [],
        seriesPersonDatas: [],
        seriesMfdDatas: [],
        maxQtsrluData: 0,
    };

    const [firstLoad, setFirstLoad] = useState(true);

    const [currentRegionInfo, setCurrentRegionInfo] = useState({});
    const [currentIntersectionInfo, setCurrentIntersectionInfo] = useState({});
    const [currentPeriod, setCurrentPeriod] = useState("day");

    const [chartData, setChartData] = useState({});

    const [chartOption, setChartOption] = useState(option);
    const [chartPersonOption, setChartPersonOption] = useState(optionPersion);
    const [chartMfdOption, setChartMfdOption] = useState(option);

    const userDetails = useAuthState();
    const alert = useAlert();

    const onChangedCurrentRegion = (regionItem) => {
        //console.log("regionItem", regionItem);
        setCurrentRegionInfo(regionItem);
    };

    const onChangedCurrentIntersection = (intersectionItem) => {
        //console.log("intersectionItem", intersectionItem);
        setCurrentIntersectionInfo(intersectionItem);
    };

    const onChangePeriod = (e) => {
        setCurrentPeriod(e.target.value);
    };

    const getStatDate = (period) => {
        // TODO: Code for Demo
        //var now = new Date();
        var now = new Date();

        var start = new Date(now);
        var end = new Date(now);
        if (period === "day") {
            start.setDate(start.getDate() - 7); // 7일전
        } else if (period === "weekOfYear" || period === "dayOfWeek") {
            var offsetDay = 1;
            if (end.getDay() === 0) offsetDay = -6;

            end.setDate(end.getDate() - end.getDay() + offsetDay);

            start = new Date(end);
            start.setDate(start.getDate() - 7 * 4); // 4주간
        } else if (period === "month") {
            end.setMonth(end.getMonth(), 1);
            start.setMonth(start.getMonth() - 3, 1); // 3개월
        }

        let startTime = Utils.utilFormatDateYYYYMMDD000000(start);
        let endTime = Utils.utilFormatDateYYYYMMDD000000(end);

        return { startTime: startTime, endTime: endTime };
    };

    const requestAxiosPeriodStat = async (option) => {
        //console.log("option", option);
        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.STAT_PERIOD_URL,
            option
        );

        return response.data;
    };

    const {
        loading: loadingPeriodStat,
        error: errorPeriodStat,
        data: resultPeriodStat,
        execute: requestPeriodStat,
    } = useAsyncAxios(requestAxiosPeriodStat);

    useEffect(() => {
        if (resultPeriodStat === null) return;

        // console.log("resultPeriodStat", resultPeriodStat);
        // const {
        //     seriesSrluDatas,
        //     seriesQtsrluDatas,
        //     seriesSpeedDatas,
        //     seriesPersonDatas,
        //     seriesMfdDatas,
        //     maxQtsrluData,
        // } = Utils.utilConvertChartSeriesPeriod(resultPeriodStat.stat);

        let data = Utils.utilConvertChartSeriesPeriod(resultPeriodStat.stat);
        setChartData(data);

        let maxQtsrluData = data.maxQtsrluData;
        let offsetUnit = 100;
        if (maxQtsrluData > 10000) {
            offsetUnit = 1000;
        } else if (maxQtsrluData < 100) {
            offsetUnit = 10;
        }

        let xDataOffset = offsetUnit - (maxQtsrluData % offsetUnit);
        maxQtsrluData += xDataOffset;

        let optionsMfd = {
            ...chartMfdOption,
            dataLabels: {
                ...chartMfdOption.dataLabels,
                enabled: true,
                formatter: function (
                    value,
                    { seriesIndex, dataPointIndex, w }
                ) {
                    return Utils.to2Digit(dataPointIndex) + ":00";
                },
                style: {
                    colors: [
                        function ({ seriesIndex, dataPointIndex, w }) {
                            if (
                                w.config.series[seriesIndex].data[
                                    dataPointIndex
                                ] === undefined
                            )
                                return;

                            var yValue =
                                w.config.series[seriesIndex].data[
                                    dataPointIndex
                                ].y;
                            var xValue =
                                w.config.series[seriesIndex].data[
                                    dataPointIndex
                                ].x;

                            return Utils.utilGetSpeedColor(yValue / xValue);
                        },
                    ],
                },
            },
            xaxis: {
                ...chartMfdOption.xaxis,
                type: "numeric",
                min: 0,
                max: maxQtsrluData,
                tickAmount: maxQtsrluData / offsetUnit,
                tooltip: {
                    enabled: false,
                },
                crosshairs: {
                    show: false,
                },
                labels: {
                    formatter: function (val, index) {
                        return val
                            .toFixed(0)
                            .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    },
                },
            },

            tooltip: {
                ...chartMfdOption.tooltip,
                x: {
                    show: false,
                },
                y: {
                    formatter: function (
                        value,
                        { series, seriesIndex, dataPointIndex, w }
                    ) {
                        var xValue =
                            w.config.series[seriesIndex].data[
                                dataPointIndex
                            ].x.toFixed(2);
                        var yValue =
                            w.config.series[seriesIndex].data[
                                dataPointIndex
                            ].y.toFixed(2);
                        var dataTime =
                            w.config.series[seriesIndex].name +
                            " " +
                            Utils.to2Digit(dataPointIndex) +
                            ":00";
                        return dataTime /* + "<br>" + '<spring:message code='stats.qtsrlu'/> : ' + xValue + "<br>" + '<spring:message code='stats.srlu'/> : ' + yValue*/;
                    },
                    title: {
                        formatter: function (
                            value,
                            { series, seriesIndex, dataPointIndex, w }
                        ) {
                            return "";
                        },
                    },
                },
            },
        };

        setChartMfdOption(optionsMfd);

        // console.log("seriesSrluDatas", seriesSrluDatas);
        // console.log("seriesQtsrluDatas", seriesQtsrluDatas);
        // console.log("seriesSpeedDatas", seriesSpeedDatas);
        // console.log("seriesPersonDatas", seriesPersonDatas);
        // console.log("seriesMfdDatas", seriesMfdDatas);
        // console.log("maxQtsrluData", maxQtsrluData);
    }, [resultPeriodStat]);

    useEffect(() => {
        if (errorPeriodStat === null) return;

        console.log("errorPeriodStat", errorPeriodStat);
    }, [errorPeriodStat]);

    const onSearch = () => {
        let time = getStatDate(currentPeriod);

        let extraParam = {
            startTime: time.startTime,
            endTime: time.endTime,
            byPeriod: currentPeriod,
        };

        if (currentIntersectionInfo.intersectionId !== "all") {
            extraParam = {
                ...extraParam,
                filterBy: "intersection",
                filterId: currentIntersectionInfo.intersectionId,
            };
        } else if (currentRegionInfo.regionId !== "all") {
            extraParam = {
                ...extraParam,
                filterBy: "region",
                filterId: currentRegionInfo.regionId,
            };
        }

        if (
            Utils.isPrebuiltIntersection(currentIntersectionInfo.intersectionId)
        ) {
            alert.info("Not Support PrebuildIntersection");
            return;
        }

        console.log("extraParam", extraParam);
        requestPeriodStat({ params: extraParam });
    };

    useEffect(() => {
        if (
            firstLoad &&
            !Utils.utilIsEmptyObj(currentRegionInfo) &&
            !Utils.utilIsEmptyObj(currentIntersectionInfo)
        ) {
            setFirstLoad(false);
            onSearch();
        }
    }, [currentRegionInfo, currentIntersectionInfo]);

    return (
        <div>
            {/* <HeaderContent />
            <Menu /> */}

            <Selector list={listPeriod} onChange={onChangePeriod} />
            <SelectorRegion onChangedCurrentRegion={onChangedCurrentRegion} />
            <SelectorIntersection
                currentRegionInfo={currentRegionInfo}
                onChangedCurrentIntersection={onChangedCurrentIntersection}
            />

            <button onClick={onSearch}>Search</button>

            {!Utils.utilIsEmptyArray(chartData.seriesSrluDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_srlu}
                    series={chartData.seriesSrluDatas}
                    option={chartOption}
                />
            )}
            {!Utils.utilIsEmptyArray(chartData.seriesQtsrluDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_qtsrlu}
                    series={chartData.seriesQtsrluDatas}
                    option={chartOption}
                />
            )}
            {!Utils.utilIsEmptyArray(chartData.seriesSpeedDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_speed}
                    series={chartData.seriesSpeedDatas}
                    option={chartOption}
                />
            )}
            {!Utils.utilIsEmptyArray(chartData.seriesMfdDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_mfd}
                    series={chartData.seriesMfdDatas}
                    option={chartMfdOption}
                />
            )}
            {!Utils.utilIsEmptyArray(chartData.seriesPersonDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_person}
                    series={chartData.seriesPersonDatas}
                    option={chartPersonOption}
                />
            )}
        </div>
    );
}

export default StatTraffic;
