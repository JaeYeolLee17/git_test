import React, { useEffect, useMemo, useState } from "react";

import * as Utils from "../utils/utils";
import * as String from "../commons/string";

function TableMfd({ dataMfd, dataLastWeekMfd, dataLastMonthAvgMfd }: any) {
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
            const todayLastIndex = dataMfd.data.length - 1;
            const todayAvgWaitCar =
                dataMfd.data[todayLastIndex].qtsrlu / (15 * 60);
            const todayPassCar = dataMfd.data[todayLastIndex].srlu;

            const todayXData = Utils.utilConvertQtsrlu15Minute(
                dataMfd.data[todayLastIndex].qtsrlu
            );
            const todayYData = Utils.utilConvertSrlu15Minute(
                dataMfd.data[todayLastIndex].srlu
            );
            const todaySpeed = todayYData / todayXData;

            const lastStatDataHour = dataMfd.data[dataMfd.data.length - 1].hour;
            const lastStatDataMinute =
                dataMfd.data[dataMfd.data.length - 1].min;

            //console.log("Today", todayPassCar, todayAvgWaitCar, todaySpeed);

            defaultData[0].today = todayPassCar;
            defaultData[1].today = todayAvgWaitCar;
            defaultData[2].today = todaySpeed;

            if (!Utils.utilIsEmptyObj(dataLastWeekMfd)) {
                const lastWeekMfd = dataLastWeekMfd.data.filter((data: any) => {
                    if (
                        data.hour === lastStatDataHour &&
                        data.min === lastStatDataMinute
                    ) {
                        return true;
                    }
                    return false;
                });

                if (lastWeekMfd) {
                    const lastWeekAvgWaitCar =
                        lastWeekMfd[0].qtsrlu / (15 * 60);
                    const lastWeekPassCar = lastWeekMfd[0].srlu;

                    const lastWeekXData = Utils.utilConvertQtsrlu15Minute(
                        lastWeekMfd[0].qtsrlu
                    );
                    const lastWeekYData = Utils.utilConvertSrlu15Minute(
                        lastWeekMfd[0].srlu
                    );
                    const lastWeekSpeed = lastWeekYData / lastWeekXData;

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
                const lastMonthAvgMfd = dataLastMonthAvgMfd.data.filter(
                    (data: any) => {
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
                    const lastMonthAvgWaitCar =
                        lastMonthAvgMfd[0].qtsrlu / 4 / (15 * 60);
                    const lastMonthPassCar = lastMonthAvgMfd[0].srlu / 4;

                    const lastMonthXData =
                        Utils.utilConvertQtsrlu15Minute(
                            lastMonthAvgMfd[0].qtsrlu
                        ) / 4;
                    const lastMonthYData =
                        Utils.utilConvertSrlu15Minute(lastMonthAvgMfd[0].srlu) /
                        4;
                    const lastMonthSpeed = lastMonthYData / lastMonthXData;

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

    // using material UI - table
    return (
        <div>
            TABLE
            {/* <ReactTable columns={columns} data={data} /> */}
        </div>
    );
}

export default TableMfd;
