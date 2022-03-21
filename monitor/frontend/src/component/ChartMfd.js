import React, { useState, useEffect } from "react";
import Chart from "react-apexcharts";
import * as Utils from "../utils/utils";
import * as String from "../commons/string";
//import * as Common from "../common";

function ChartMfd({ dataMfd, dataLastWeekMfd, dataLastMonthAvgMfd }) {
    const [chartSeries, setChartSeries] = useState([]);
    const [chartOption, setChartOption] = useState({});

    const makeDataTime = (data) => {
        let dataTime =
            Utils.utilLeadingZeros(data.hour, 2) +
            ":" +
            Utils.utilLeadingZeros(data.min, 2);
        var endHour = data.hour;
        var endMinute = data.min + 15;
        if (endMinute === 60) {
            endHour += 1;
            endMinute = 0;
        }
        var endDataTime =
            Utils.utilLeadingZeros(endHour, 2) +
            ":" +
            Utils.utilLeadingZeros(endMinute, 2);

        let time = dataTime + " ~ " + endDataTime;

        return time;
    };

    useEffect(() => {
        let newSeries = [];
        let maxXData = 0;

        if (dataMfd) {
            let nameCurrent =
                dataMfd.year + "-" + dataMfd.month + "-" + dataMfd.day;
            let currentData = dataMfd.data.map((dataWithTime) => {
                var xData = Utils.utilConvertQtsrlu15Minute(
                    dataWithTime.qtsrlu
                );
                var yData = Utils.utilConvertSrlu15Minute(dataWithTime.srlu);

                if (xData > maxXData) maxXData = xData;

                return { time: makeDataTime(dataWithTime), x: xData, y: yData };
            });

            let lastStatDataHour = dataMfd.data[dataMfd.data.length - 1].hour;
            let lastStatDataMinute = dataMfd.data[dataMfd.data.length - 1].min;

            //console.log("lastStatDataHour", lastStatDataHour);
            //console.log("lastStatDataMinute", lastStatDataMinute);

            newSeries.push({ name: nameCurrent, data: currentData });

            if (dataLastWeekMfd) {
                let lastWeekMfd = dataLastWeekMfd.data.filter((data) => {
                    if (
                        data.hour === lastStatDataHour &&
                        data.min === lastStatDataMinute
                    ) {
                        return true;
                    }
                    return false;
                });

                if (lastWeekMfd) {
                    let xData = Utils.utilConvertQtsrlu15Minute(
                        lastWeekMfd[0].qtsrlu
                    );
                    let yData = Utils.utilConvertSrlu15Minute(
                        lastWeekMfd[0].srlu
                    );

                    if (xData > maxXData) maxXData = xData;

                    let dataLastWeek = [];
                    dataLastWeek.push({
                        time: makeDataTime(lastWeekMfd[0]),
                        x: xData,
                        y: yData,
                    });
                    newSeries.push({ name: "lastWeek", data: dataLastWeek });
                }
            }

            if (dataLastMonthAvgMfd) {
                let lastMonthAvgMfd = dataLastMonthAvgMfd.data.filter(
                    (data) => {
                        if (
                            data.hour === lastStatDataHour &&
                            data.min === lastStatDataMinute
                        ) {
                            return true;
                        }
                        return false;
                    }
                );

                if (lastMonthAvgMfd) {
                    let xData =
                        Utils.utilConvertQtsrlu15Minute(
                            lastMonthAvgMfd[0].qtsrlu
                        ) / 4;
                    let yData =
                        Utils.utilConvertSrlu15Minute(lastMonthAvgMfd[0].srlu) /
                        4;

                    if (xData > maxXData) maxXData = xData;

                    let dataLastMonthAvg = [];
                    dataLastMonthAvg.push({
                        time: makeDataTime(lastMonthAvgMfd[0]),
                        x: xData,
                        y: yData,
                    });
                    newSeries.push({
                        name: "lastMonth",
                        data: dataLastMonthAvg,
                    });
                }
            }
        }

        let offsetUnit = 100;
        if (maxXData > 10000) {
            offsetUnit = 1000;
        } else if (maxXData < 100) {
            offsetUnit = 10;
        }

        let xDataOffset = offsetUnit - (maxXData % offsetUnit);
        maxXData += xDataOffset;

        setChartSeries(newSeries);
        setChartOption({
            chart: {
                height: "100%",
                type: "line",
                dropShadow: {
                    enabled: true,
                    color: "#000",
                    top: 20,
                    left: 0,
                    blur: 8,
                    opacity: 0.2,
                },
                toolbar: {
                    show: false,
                },
                zoom: {
                    enabled: false,
                },
                animations: {
                    enabled: true,
                    easing: "linear",
                    dynamicAnimation: {
                        speed: 500,
                    },
                },
                background: "#f7f7f7",

                style: {
                    borderRadius: "12px",
                    padding: "60px 20px 24px 12px",
                },
            },
            plotOptions: {
                line: {
                    curve: "smooth",
                },
            },
            stroke: {
                width: [4],
            },

            colors: ["#ffcc00", "#9500ff", "#306fd9"],
            series: [
                {
                    data: [0],
                },
            ],

            xaxis: {
                type: "numeric",
                min: 0,
                max: maxXData,
                tickAmount: maxXData / offsetUnit,
                tooltip: {
                    enabled: false,
                },
                crosshairs: {
                    show: false,
                },
                title: {
                    style: {
                        fontSize: "13px",
                        padding: "12px",
                        fontFamily: "Noto Sans KR, sans-serif",
                    },
                },
                axisBorder: {
                    show: true,
                    color: "#666666",
                    height: 1,
                    width: "100%",
                },
                labels: {
                    style: {
                        fontSize: "13px",
                        fontFamily: "Noto Sans KR, sans-serif",
                    },
                    formatter: function (val, index) {
                        return val
                            .toFixed(0)
                            .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    },
                },
                axisTicks: {
                    show: true,
                    borderType: "solid",
                    color: "#666666",
                    height: 6,
                    offsetX: 0,
                    offsetY: 0,
                },
            },
            yaxis: {
                /* 			    axisBorder: {
                    show: true,
                    color: '#a9abb3',
                    width: 1,
                }, */
                title: {
                    style: {
                        fontSize: "13px",
                        padding: "12px",
                        fontFamily: "Noto Sans KR, sans-serif",
                    },
                },
                labels: {
                    style: {
                        fontSize: "13px",
                        fontFamily: "Noto Sans KR, sans-serif",
                    },
                    formatter: function (val, index) {
                        return val
                            .toFixed(0)
                            .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    },
                },
            },
            grid: {
                show: true,
                borderColor: "#90A4AE",
                row: {
                    colors: ["white"],
                    opacity: 1,
                },
                lines: {
                    show: true,
                },
                padding: {
                    top: 0,
                    right: 20,
                    bottom: 0,
                    left: 0,
                },
            },
            legend: {
                showForSingleSeries: false,
                position: "top",
                horizontalAlign: "right",
                floating: true,
                offsetY: -25,
                offsetX: -5,
            },
            markers: {
                size: [4, 7, 7],
                shape: "circle",
            },
            noData: {
                text: String.chart_no_data,
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
            tooltip: {
                intersect: true,
                shared: false,
                marker: {
                    show: false,
                },

                x: {
                    show: false,
                },

                y: {
                    formatter: function (
                        value,
                        { series, seriesIndex, dataPointIndex, w }
                    ) {
                        let xValue =
                            w.config.series[seriesIndex].data[
                                dataPointIndex
                            ].x.toFixed(2);
                        let yValue =
                            w.config.series[seriesIndex].data[
                                dataPointIndex
                            ].y.toFixed(2);
                        let time =
                            w.config.series[seriesIndex].data[dataPointIndex]
                                .time;

                        let title = null;
                        if (w.config.series[seriesIndex].name === "lastWeek") {
                            title = String.chart_lastweek;
                        } else if (
                            w.config.series[seriesIndex].name === "lastMonth"
                        ) {
                            title = String.chart_lastmonthavg;
                        }

                        let info =
                            time +
                            "<br>" +
                            String.stats_qtsrlu +
                            " : " +
                            xValue +
                            "<br>" +
                            String.stats_srlu +
                            " : " +
                            yValue;

                        if (title) {
                            return title + " : " + info;
                        }

                        return info;
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
            dataLabels: {
                enabled: true,
                formatter: function (
                    value,
                    { seriesIndex, dataPointIndex, w }
                ) {
                    //console.log("dataLabels : " + JSON.stringify(data));
                    if (
                        dataMfd !== undefined &&
                        dataPointIndex === dataMfd.data.length - 1
                    ) {
                        var hour = dataMfd.data[dataPointIndex].hour;
                        var minute = dataMfd.data[dataPointIndex].min + 15;
                        if (minute === 60) {
                            hour += 1;
                            minute = 0;
                        }

                        var dataTime =
                            Utils.utilLeadingZeros(hour, 2) +
                            ":" +
                            Utils.utilLeadingZeros(minute, 2);

                        //console.log("dataLabels dataTime : " + dataTime);
                        return dataTime;
                    }

                    return "";
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

                            // if (yValue / xValue >= Common.trafficSpeedNormal) {
                            //     return Common.trafficColorNormal;
                            // } else if (
                            //     yValue / xValue <
                            //     Common.trafficSpeedBusy
                            // ) {
                            //     return Common.trafficColorBusy;
                            // } else {
                            //     return Common.trafficColorSlowly;
                            // }
                        },
                    ],
                },
            },
        });
    }, [dataMfd, dataLastWeekMfd, dataLastMonthAvgMfd]);

    return (
        <Chart
            options={chartOption}
            series={chartSeries}
            type='line'
            width='500'
            height='500'
        ></Chart>
    );
}

export default ChartMfd;
