import React, { useState, useEffect } from "react";
import Chart from "react-apexcharts";
import * as Utils from "../utils/utils";
import * as String from "../string";

function ChartMfd({ dataMfd }) {
    const [chartSeries, setChartSeries] = useState([]);
    const [chartOption, setChartOption] = useState({
        // chart: {
        //     height: "100%",
        //     type: "line",
        //     dropShadow: {
        //         enabled: true,
        //         color: "#000",
        //         top: 20,
        //         left: 0,
        //         blur: 8,
        //         opacity: 0.2,
        //     },
        //     toolbar: {
        //         show: false,
        //     },
        //     zoom: {
        //         enabled: false,
        //     },
        //     animations: {
        //         enabled: true,
        //         easing: "linear",
        //         dynamicAnimation: {
        //             speed: 500,
        //         },
        //     },
        //     background: "#f7f7f7",
        //     style: {
        //         borderRadius: "12px",
        //         padding: "60px 20px 24px 12px",
        //     },
        // },
        // plotOptions: {
        //     line: {
        //         curve: "smooth",
        //     },
        // },
        // stroke: {
        //     width: [4],
        // },
        // colors: ["#ffcc00", "#9500ff", "#306fd9"],
        // series: [
        //     {
        //         data: [0],
        //     },
        // ],
        // xaxis: {
        //     type: "numeric",
        //     tooltip: {
        //         enabled: false,
        //     },
        //     crosshairs: {
        //         show: false,
        //     },
        //     title: {
        //         style: {
        //             fontSize: "13px",
        //             padding: "12px",
        //             fontFamily: "Noto Sans KR, sans-serif",
        //         },
        //     },
        //     axisBorder: {
        //         show: true,
        //         color: "#666666",
        //         height: 1,
        //         width: "100%",
        //     },
        //     labels: {
        //         style: {
        //             fontSize: "13px",
        //             fontFamily: "Noto Sans KR, sans-serif",
        //         },
        //         formatter: function (val, index) {
        //             return val.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        //         },
        //     },
        //     axisTicks: {
        //         show: true,
        //         borderType: "solid",
        //         color: "#666666",
        //         height: 6,
        //         offsetX: 0,
        //         offsetY: 0,
        //     },
        // },
        // yaxis: {
        //     /* 			    axisBorder: {
        //         show: true,
        //         color: '#a9abb3',
        //         width: 1,
        //     }, */
        //     title: {
        //         style: {
        //             fontSize: "13px",
        //             padding: "12px",
        //             fontFamily: "Noto Sans KR, sans-serif",
        //         },
        //     },
        //     labels: {
        //         style: {
        //             fontSize: "13px",
        //             fontFamily: "Noto Sans KR, sans-serif",
        //         },
        //         formatter: function (val, index) {
        //             return val.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        //         },
        //     },
        // },
        // grid: {
        //     show: true,
        //     borderColor: "#90A4AE",
        //     row: {
        //         colors: ["white"],
        //         opacity: 1,
        //     },
        //     lines: {
        //         show: true,
        //     },
        //     padding: {
        //         top: 0,
        //         right: 20,
        //         bottom: 0,
        //         left: 0,
        //     },
        // },
        // legend: {
        //     showForSingleSeries: false,
        //     position: "top",
        //     horizontalAlign: "right",
        //     floating: true,
        //     offsetY: -25,
        //     offsetX: -5,
        // },
        // markers: {
        //     size: [4, 7, 7],
        //     shape: "circle",
        // },
        // noData: {
        //     text: String.chart_no_data,
        //     align: "center",
        //     verticalAlign: "middle",
        //     offsetX: 0,
        //     offsetY: 0,
        //     style: {
        //         color: "#8b0000",
        //         fontSize: "32px",
        //         fontFamily: "Noto Sans KR",
        //     },
        // },
        // tooltip: {
        //     intersect: true,
        //     shared: false,
        //     marker: {
        //         show: false,
        //     },
        //     x: {
        //         show: false,
        //     },
        // },
    });

    var maxXData = 0;

    useEffect(() => {
        //console.log("dataMfd", dataMfd);
        if (!dataMfd) return;

        let nameCurrent =
            dataMfd.year + "-" + dataMfd.month + "-" + dataMfd.day;
        let currentData = dataMfd.data.map((dataWithTime) => {
            var xData = (dataWithTime.qtsrlu * 4) / 3600; // 15분단위
            var yData = (dataWithTime.srlu * 100 * 4) / 1000; // 100m -> km로 변환 // 15분단위

            if (xData > maxXData) maxXData = xData;

            return { x: xData, y: yData };
        });

        // //let newSeries = [...chartSeries];
        // let newSeries = chartSeries.filter(
        //     (series) => series.key !== "current"
        // );
        // let objIndex = newSeries.findIndex((obj) => obj.name === nameCurrent);

        // // if (objIndex === -1) {
        // //     newSeries.push({ name: nameCurrent, data: currentData });
        // // } else {
        // //     newSeries[objIndex].data = currentData;
        // // }
        // newSeries.push({
        //     key: "current",
        //     name: nameCurrent,
        //     data: currentData,
        // });
        // console.log("newSeries", newSeries);

        //let newSeries = [...chartSeries];
        let newSeries = [];
        newSeries.push({ name: nameCurrent, data: currentData });

        //setChartSeries(newSeries);
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
                        let dataTime =
                            Utils.utilLeadingZeros(
                                dataMfd.data[dataPointIndex].hour,
                                2
                            ) +
                            ":" +
                            Utils.utilLeadingZeros(
                                dataMfd.data[dataPointIndex].min,
                                2
                            );
                        var endHour = dataMfd.data[dataPointIndex].hour;
                        var endMinute = dataMfd.data[dataPointIndex].min + 15;
                        if (endMinute === 60) {
                            endHour += 1;
                            endMinute = 0;
                        }
                        var endDataTime =
                            Utils.utilLeadingZeros(endHour, 2) +
                            ":" +
                            Utils.utilLeadingZeros(endMinute, 2);

                        return (
                            dataTime +
                            " ~ " +
                            endDataTime +
                            "<br>" +
                            "x : " +
                            xValue +
                            "<br>" +
                            "y : " +
                            yValue
                        );
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
                    //if (statData.data[dataPointIndex].min == 0) {
                    //	var dataTime = $utils.leadingZeros(statData.data[dataPointIndex].hour, 2) + ":" + $utils.leadingZeros(statData.data[dataPointIndex].min, 2);
                    //	return dataTime;
                    //}

                    //var dataTime = statData.data[dataPointIndex].hour + ":" + statData.data[dataPointIndex].min;
                    //return dataTime;
                    //var dataTime = $utils.leadingZeros(statData.data[dataPointIndex].hour, 2);
                    //return "";
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

                            return "#00e025";
                        },
                    ],
                },
            },

            // xaxis: {
            //     //min: 0,
            //     //max: maxXData,
            //     //tickAmount: maxXData / offsetUnit,
            // },

            // tooltip: {
            //     ...chartOption.tooltip,
            //     y: {
            //         ...chartOption.tooltip.y,
            //         formatter: function (
            //             value,
            //             { series, seriesIndex, dataPointIndex, w }
            //         ) {
            //             let xValue =
            //                 w.config.series[seriesIndex].data[
            //                     dataPointIndex
            //                 ].x.toFixed(2);
            //             let yValue =
            //                 w.config.series[seriesIndex].data[
            //                     dataPointIndex
            //                 ].y.toFixed(2);
            //             let dataTime =
            //                 utilLeadingZeros(
            //                     data.data[dataPointIndex].hour,
            //                     2
            //                 ) +
            //                 ":" +
            //                 utilLeadingZeros(data.data[dataPointIndex].min, 2);
            //             var endHour = data.data[dataPointIndex].hour;
            //             var endMinute = data.data[dataPointIndex].min + 15;
            //             if (endMinute === 60) {
            //                 endHour += 1;
            //                 endMinute = 0;
            //             }
            //             var endDataTime =
            //                 utilLeadingZeros(endHour, 2) +
            //                 ":" +
            //                 utilLeadingZeros(endMinute, 2);

            //             return (
            //                 dataTime +
            //                 " ~ " +
            //                 endDataTime +
            //                 "<br>" +
            //                 "x : " +
            //                 xValue +
            //                 "<br>" +
            //                 "y : " +
            //                 yValue
            //             );
            //         },
            //         // title: {
            //         //     formatter: function (
            //         //         value,
            //         //         { series, seriesIndex, dataPointIndex, w }
            //         //     ) {
            //         //         return "";
            //         //     },
            //         // },
            //     },
        });

        setChartSeries(newSeries);
    }, [dataMfd]);

    // useEffect(() => {
    //     //console.log("basicData", basicData);
    //     setChartOption({
    //         dataLabels: {
    //             enabled: true,
    //             formatter: function (
    //                 value,
    //                 { seriesIndex, dataPointIndex, w }
    //             ) {
    //                 console.log("dataLabels : " + JSON.stringify(basicData));
    //                 if (
    //                     basicData !== undefined &&
    //                     dataPointIndex === basicData.data.length - 1
    //                 ) {
    //                     var hour = basicData.data[dataPointIndex].hour;
    //                     var minute = basicData.data[dataPointIndex].min + 15;
    //                     if (minute === 60) {
    //                         hour += 1;
    //                         minute = 0;
    //                     }

    //                     var dataTime =
    //                         utilLeadingZeros(hour, 2) +
    //                         ":" +
    //                         utilLeadingZeros(minute, 2);

    //                     console.log("dataLabels dataTime : " + dataTime);
    //                     return dataTime;
    //                 }
    //                 //if (statData.data[dataPointIndex].min == 0) {
    //                 //	var dataTime = $utils.leadingZeros(statData.data[dataPointIndex].hour, 2) + ":" + $utils.leadingZeros(statData.data[dataPointIndex].min, 2);
    //                 //	return dataTime;
    //                 //}

    //                 //var dataTime = statData.data[dataPointIndex].hour + ":" + statData.data[dataPointIndex].min;
    //                 //return dataTime;
    //                 //var dataTime = $utils.leadingZeros(statData.data[dataPointIndex].hour, 2);
    //                 //return "";
    //             },
    //             style: {
    //                 colors: [
    //                     function ({ seriesIndex, dataPointIndex, w }) {
    //                         if (
    //                             w.config.series[seriesIndex].data[
    //                                 dataPointIndex
    //                             ] === undefined
    //                         )
    //                             return;

    //                         var yValue =
    //                             w.config.series[seriesIndex].data[
    //                                 dataPointIndex
    //                             ].y;
    //                         var xValue =
    //                             w.config.series[seriesIndex].data[
    //                                 dataPointIndex
    //                             ].x;

    //                         return "#00e025";
    //                     },
    //                 ],
    //             },
    //         },

    //         xaxis: {
    //             //min: 0,
    //             //max: maxXData,
    //             //tickAmount: maxXData / offsetUnit,
    //         },

    //         /* 		    tooltip: {
    //                  y: {
    //                    formatter: function(value, { series, seriesIndex, dataPointIndex, w }) {
    //                        var xValue = w.config.series[seriesIndex].data[dataPointIndex].x.toFixed(2);
    //                        var yValue = w.config.series[seriesIndex].data[dataPointIndex].y.toFixed(2);
    //                        var dataTime = $utils.leadingZeros(statData.data[dataPointIndex].hour, 2) + ":" + $utils.leadingZeros(statData.data[dataPointIndex].min, 2);
    //                          return dataTime + "<br>" + '<spring:message code='stats.qtsrlu'/> : ' + xValue + "<br>" + '<spring:message code='stats.srlu'/> : ' + yValue;
    //                    },
    //                 title: {
    //                     formatter: function(value, { series, seriesIndex, dataPointIndex, w }) {
    //                              return "";
    //                        },
    //                 },
    //                }
    //         }, */
    //     });
    // }, [basicData]);

    // var options = {
    //     chart: {
    //         height: "100%",
    //         type: "line",
    //         dropShadow: {
    //             enabled: true,
    //             color: "#000",
    //             top: 20,
    //             left: 0,
    //             blur: 8,
    //             opacity: 0.2,
    //         },
    //         toolbar: {
    //             show: false,
    //         },
    //         zoom: {
    //             enabled: false,
    //         },
    //         animations: {
    //             enabled: true,
    //             easing: "linear",
    //             dynamicAnimation: {
    //                 speed: 500,
    //             },
    //         },
    //         background: "#f7f7f7",

    //         style: {
    //             borderRadius: "12px",
    //             padding: "60px 20px 24px 12px",
    //         },
    //     },
    //     plotOptions: {
    //         line: {
    //             curve: "smooth",
    //         },
    //     },
    //     stroke: {
    //         width: [4],
    //     },

    //     colors: ["#ffcc00", "#9500ff", "#306fd9"],
    //     series: [
    //         {
    //             data: [0],
    //         },
    //     ],

    //     xaxis: {
    //         type: "numeric",
    //         tooltip: {
    //             enabled: false,
    //         },
    //         crosshairs: {
    //             show: false,
    //         },
    //         title: {
    //             style: {
    //                 fontSize: "13px",
    //                 padding: "12px",
    //                 fontFamily: "Noto Sans KR, sans-serif",
    //             },
    //         },
    //         axisBorder: {
    //             show: true,
    //             color: "#666666",
    //             height: 1,
    //             width: "100%",
    //         },
    //         labels: {
    //             style: {
    //                 fontSize: "13px",
    //                 fontFamily: "Noto Sans KR, sans-serif",
    //             },
    //             formatter: function (val, index) {
    //                 return val.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    //             },
    //         },
    //         axisTicks: {
    //             show: true,
    //             borderType: "solid",
    //             color: "#666666",
    //             height: 6,
    //             offsetX: 0,
    //             offsetY: 0,
    //         },
    //     },
    //     yaxis: {
    //         /* 			    axisBorder: {
    //             show: true,
    //             color: '#a9abb3',
    //             width: 1,
    //         }, */
    //         title: {
    //             style: {
    //                 fontSize: "13px",
    //                 padding: "12px",
    //                 fontFamily: "Noto Sans KR, sans-serif",
    //             },
    //         },
    //         labels: {
    //             style: {
    //                 fontSize: "13px",
    //                 fontFamily: "Noto Sans KR, sans-serif",
    //             },
    //             formatter: function (val, index) {
    //                 return val.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    //             },
    //         },
    //     },
    //     grid: {
    //         show: true,
    //         borderColor: "#90A4AE",
    //         row: {
    //             colors: ["white"],
    //             opacity: 1,
    //         },
    //         lines: {
    //             show: true,
    //         },
    //         padding: {
    //             top: 0,
    //             right: 20,
    //             bottom: 0,
    //             left: 0,
    //         },
    //     },
    //     legend: {
    //         showForSingleSeries: false,
    //         position: "top",
    //         horizontalAlign: "right",
    //         floating: true,
    //         offsetY: -25,
    //         offsetX: -5,
    //     },
    //     markers: {
    //         size: [4, 7, 7],
    //         shape: "circle",
    //     },
    //     noData: {
    //         text: "chart.no.data",
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
    //     tooltip: {
    //         intersect: true,
    //         shared: false,
    //         marker: {
    //             show: false,
    //         },

    //         x: {
    //             show: false,
    //         },
    //         y: {
    //             formatter: function (
    //                 value,
    //                 { series, seriesIndex, dataPointIndex, w }
    //             ) {
    //                 //    if (w.config.series[seriesIndex].name == 'lastWeek' || w.config.series[seriesIndex].name == 'lastMonth') {
    //                 //        var xValue = w.config.series[seriesIndex].data[dataPointIndex].x.toFixed(2);
    //                 //        var yValue = w.config.series[seriesIndex].data[dataPointIndex].y.toFixed(2);
    //                 //        var dataTime = $utils.leadingZeros(lastStatDataHour, 2) + ":" + $utils.leadingZeros(lastStatDataMinute, 2);
    //                 //     var endHour = lastStatDataHour;
    //                 //     var endMinute = lastStatDataMinute + 15;
    //                 //     if (endMinute == 60) {
    //                 //         endHour += 1;
    //                 //         endMinute = 0;
    //                 //     }
    //                 //        var endDataTime = $utils.leadingZeros(endHour, 2) + ":" + $utils.leadingZeros(endMinute, 2);
    //                 //        var title;
    //                 //        if (w.config.series[seriesIndex].name == 'lastWeek') {
    //                 //            title = '<spring:message code='chart.lastweek' />';
    //                 //        } else {
    //                 //            title = '<spring:message code='chart.lastmonthavg' />';
    //                 //        }
    //                 //        return title + " : " + dataTime + " ~ " + endDataTime + "<br>" + '<spring:message code='stats.qtsrlu'/> : ' + xValue + "<br>" + '<spring:message code='stats.srlu'/> : ' + yValue;
    //                 //    } else {
    //                 //        var xValue = w.config.series[seriesIndex].data[dataPointIndex].x.toFixed(2);
    //                 //        var yValue = w.config.series[seriesIndex].data[dataPointIndex].y.toFixed(2);
    //                 //        var dataTime = $utils.leadingZeros(statData.data[dataPointIndex].hour, 2) + ":" + $utils.leadingZeros(statData.data[dataPointIndex].min, 2);
    //                 //     var endHour = statData.data[dataPointIndex].hour;
    //                 //     var endMinute = statData.data[dataPointIndex].min + 15;
    //                 //     if (endMinute == 60) {
    //                 //         endHour += 1;
    //                 //         endMinute = 0;
    //                 //     }
    //                 //        var endDataTime = $utils.leadingZeros(endHour, 2) + ":" + $utils.leadingZeros(endMinute, 2);
    //                 //          return dataTime + " ~ " + endDataTime + "<br>" + '<spring:message code='stats.qtsrlu'/> : ' + xValue + "<br>" + '<spring:message code='stats.srlu'/> : ' + yValue;
    //                 //    }
    //                 //    return null;
    //             },
    //             title: {
    //                 formatter: function (
    //                     value,
    //                     { series, seriesIndex, dataPointIndex, w }
    //                 ) {
    //                     return "";
    //                 },
    //             },
    //         },
    //     },
    // };

    //console.log("chartSeries", chartSeries);
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
