import React, { useState, useEffect } from "react";
import Chart from "react-apexcharts";

function ChartStat({ loading, name, series, option }) {
    //console.log(name, loading);
    const [chartOption, setChartOption] = useState({
        chart: {
            height: option?.chart?.height,
            type: option?.chart?.type,

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
            events: {
                // animationEnd: function (chartContext, options) {
                //     drawYaxisLine(chartContext);
                //   }
            },
        },
        dataLabels: {
            enabled: false,
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
                formatter: function (val, index) {
                    var formattedNumber =
                        ("0" + val.toFixed(0)).slice(-2) + ":00";
                    return formattedNumber;
                },
            },
        },
        yaxis: {
            labels: {
                formatter: function (val, index) {
                    return val.toFixed(0).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                },
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
        },
        legend: {
            showForSingleSeries: true,
        },
        // noData: {
        //     text: '<spring:message code='chart.no.data' />',
        //     align: 'center',
        //     verticalAlign: 'middle',
        //     offsetX: 0,
        //     offsetY: 0,
        //     style: {
        //       color: '#8b0000',
        //       fontSize: '32px',
        //       fontFamily: 'Noto Sans KR'
        //     }
        // }
    });

    useEffect(() => {
        setChartOption({ ...chartOption, ...option });
    }, [option]);

    return (
        <div>
            <h2>{name}</h2>
            <Chart
                options={chartOption}
                series={series}
                type={chartOption.chart.type}
                width='1500'
                height={chartOption.chart.height}
            ></Chart>
        </div>
    );
}

export default ChartStat;
