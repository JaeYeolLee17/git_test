import React, { useState } from "react";
import Chart from "react-apexcharts";

function ChartStat({ loading, name, series, type }) {
    //console.log(name, loading);
    const [chartOption, setChartOption] = useState({
        chart: {
            height: "500px",
            type: type,
            stacked: true,
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

        colors: ["#224d99", "#3b84ff", "#17e6b1", "#ffd500", "#ff5b7d"],
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
        grid: {
            show: true,
            padding: {
                top: 20,
                right: 0,
                bottom: 20,
                left: 0,
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

    return (
        <div>
            <h2>{name}</h2>
            <Chart
                options={chartOption}
                series={series}
                type={type}
                width='1000'
                height='500'
            ></Chart>
        </div>
    );
}

export default ChartStat;
