import React, { useState, useEffect } from "react";
import Chart from "react-apexcharts";

import * as String from "../commons/string";
import * as Common from "../commons/common";
import * as Utils from "../utils/utils";

function ChartStat({ loading, series, option }: Common.ChartStatData) {
    //console.log(name, loading);
    const [chartOption, setChartOption] = useState<Record<string, any>>({});

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

    //     setChartOption({
    //         ...chartOption,
    //         chart: {
    //             ...chartOption.chart,
    //             animations: {
    //                 enabled: false,
    //             },
    //         },
    //         annotations: {
    //             position: "back",
    //             yaxis: yaxisData,
    //         },
    //     });
    // };

    useEffect(() => {
        // console.log("option", option);
        const chart_option = {
            ...option,
            chart: {
                ...option.chart,
                toolbar: {
                    show: false,
                },
                zoom: {
                    enabled: false,
                    type: "x",
                },
                animations: {
                    enabled: true,
                    easing: "linear",
                    dynamicAnimation: {
                        speed: 500,
                    },
                },
                // events: {
                //     animationEnd: (chartContext, options) => {
                //         drawYaxisLine(chartContext);
                //     },
                // },
            },
            dataLabels: {
                enabled: false,
                ...option.dataLabels,
            },

            title: {
                align: "center",
            },
            xaxis: {
                type: "numeric",
                min: 0,
                max: 23,
                tickAmount: 24,

                labels: {
                    formatter: function (val: number, index: number) {
                        const formattedNumber =
                            ("0" + val.toFixed(0)).slice(-2) + ":00";
                        return formattedNumber;
                    },
                },
                ...option.xaxis,
            },
            yaxis: {
                labels: {
                    formatter: function (val: number, index: number) {
                        return val
                            .toFixed(0)
                            .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    },
                },
                ...option.yaxis,
            },
            tooltip: {
                ...option.tooltip,
                intersect: true,
                shared: false,
                marker: {
                    show: false,
                },

                x: {
                    show: false,
                },
            },
            legend: {
                showForSingleSeries: true,
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
        };
        setChartOption(chart_option);
    }, [option]);

    // console.log(name, chartOption);

    return (
        <div>
            {/* <h2>{name}</h2> */}
            {!Utils.utilIsEmptyObj(chartOption) && (
                <Chart
                    options={chartOption}
                    series={series}
                    type={option.chart.type}
                    width='100%'
                    height={option.chart.height}
                ></Chart>
            )}
        </div>
    );
}

export default ChartStat;
