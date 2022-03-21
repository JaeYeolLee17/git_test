import React, { useEffect, useMemo, useState } from "react";

import * as Utils from "../utils/utils";
import * as String from "../commons/string";
import ReactTable from "./ReactTable";

function TableMfd({ dataMfd, dataLastWeekMfd, dataLastMonthAvgMfd }) {
    const columns = useMemo(
        () => [
            {
                accessor: "category",
                Header: "",
            },
            {
                accessor: "today",
                Header: String.chart_today,
            },
            {
                accessor: "lastweek",
                Header: String.chart_lastweek,
            },
            {
                accessor: "lastmonthavg",
                Header: String.chart_lastmonthavg,
            },
        ],
        []
    );

    const data = useMemo(() => {
        let defaultData = [
            {
                category: String.chart_veh_pass,
                today: 0,
                lastweek: 0,
                lastmonthavg: 0,
            },
            {
                category: String.chart_avg_veh_que,
                today: 0,
                lastweek: 0,
                lastmonthavg: 0,
            },
            {
                category: String.chart_avg_speed,
                today: 0,
                lastweek: 0,
                lastmonthavg: 0,
            },
        ];

        if (!Utils.utilIsEmptyObj(dataMfd)) {
            let todayLastIndex = dataMfd.data.length - 1;
            let todayAvgWaitCar = (
                dataMfd.data[todayLastIndex].qtsrlu /
                (15 * 60)
            ).toFixed(2);
            let todayPassCar = dataMfd.data[todayLastIndex].srlu;

            var todayXData = Utils.utilConvertQtsrlu15Minute(
                dataMfd.data[todayLastIndex].qtsrlu
            );
            var todayYData = Utils.utilConvertSrlu15Minute(
                dataMfd.data[todayLastIndex].srlu
            );
            var todaySpeed = (todayYData / todayXData).toFixed(2);

            let lastStatDataHour = dataMfd.data[dataMfd.data.length - 1].hour;
            let lastStatDataMinute = dataMfd.data[dataMfd.data.length - 1].min;

            //console.log("Today", todayPassCar, todayAvgWaitCar, todaySpeed);

            defaultData[0].today = todayPassCar;
            defaultData[1].today = todayAvgWaitCar;
            defaultData[2].today = todaySpeed;

            if (!Utils.utilIsEmptyObj(dataLastWeekMfd)) {
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
                    var lastWeekAvgWaitCar = (
                        lastWeekMfd[0].qtsrlu /
                        (15 * 60)
                    ).toFixed(2);
                    var lastWeekPassCar = lastWeekMfd[0].srlu;

                    let lastWeekXData = Utils.utilConvertQtsrlu15Minute(
                        lastWeekMfd[0].qtsrlu
                    );
                    let lastWeekYData = Utils.utilConvertSrlu15Minute(
                        lastWeekMfd[0].srlu
                    );
                    var lastWeekSpeed = (lastWeekYData / lastWeekXData).toFixed(
                        2
                    );

                    // console.log(
                    //     "Last Week",
                    //     lastWeekPassCar,
                    //     lastWeekAvgWaitCar,
                    //     lastWeekSpeed
                    // );
                    defaultData[0].lastweek = lastWeekPassCar;
                    defaultData[1].lastweek = lastWeekAvgWaitCar;
                    defaultData[2].lastweek = lastWeekSpeed;
                }
            }

            if (!Utils.utilIsEmptyObj(dataLastMonthAvgMfd)) {
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
                    var lastMonthAvgWaitCar = (
                        lastMonthAvgMfd[0].qtsrlu /
                        4 /
                        (15 * 60)
                    ).toFixed(2);
                    var lastMonthPassCar = lastMonthAvgMfd[0].srlu / 4;

                    let lastMonthXData =
                        Utils.utilConvertQtsrlu15Minute(
                            lastMonthAvgMfd[0].qtsrlu
                        ) / 4;
                    let lastMonthYData =
                        Utils.utilConvertSrlu15Minute(lastMonthAvgMfd[0].srlu) /
                        4;
                    var lastMonthSpeed = (
                        lastMonthYData / lastMonthXData
                    ).toFixed(2);

                    // console.log(
                    //     "Last Month",
                    //     lastMonthPassCar,
                    //     lastMonthAvgWaitCar,
                    //     lastMonthSpeed
                    // );

                    defaultData[0].lastmonthavg = lastMonthPassCar;
                    defaultData[1].lastmonthavg = lastMonthAvgWaitCar;
                    defaultData[2].lastmonthavg = lastMonthSpeed;
                }
            }
        }

        return defaultData;
    }, [dataMfd, dataLastWeekMfd, dataLastMonthAvgMfd]);

    return (
        <div>
            <ReactTable columns={columns} data={data} />
        </div>
    );
}

export default TableMfd;
