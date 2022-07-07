import React, { useEffect, useMemo, useState } from "react";
import { useTable, Column, HeaderGroup, Cell } from "react-table";

import * as Utils from "../utils/utils";
import * as String from "../commons/string";

import styles from "./TableMfd.module.css";
import { Box } from "@mui/material";

function TableMfd({ dataMfd, dataLastWeekMfd, dataLastMonthAvgMfd }: any) {
    const round = (num: number) => {
        const m = Number((Math.abs(num) * 100).toPrecision(15));
        return (Math.round(m) / 100) * Math.sign(num);
    };

    const columns: Column[] = useMemo(
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
        const defaultData = [
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

            // console.log("Today", todayPassCar, todayAvgWaitCar, todaySpeed);

            defaultData[0].today = round(todayPassCar);
            defaultData[1].today = round(todayAvgWaitCar);
            defaultData[2].today = round(todaySpeed);

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
                    defaultData[0].lastweek = round(lastWeekPassCar);
                    defaultData[1].lastweek = round(lastWeekAvgWaitCar);
                    defaultData[2].lastweek = round(lastWeekSpeed);
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

                    defaultData[0].lastmonthavg = round(lastMonthPassCar);
                    defaultData[1].lastmonthavg = round(lastMonthAvgWaitCar);
                    defaultData[2].lastmonthavg = round(lastMonthSpeed);
                }
            }
        }

        return defaultData;
    }, [dataMfd, dataLastWeekMfd, dataLastMonthAvgMfd]);

    const { getTableProps, getTableBodyProps, headerGroups, rows, prepareRow } =
        useTable({ columns, data });

    const handleIndicate = (column: HeaderGroup) => {
        let color = null;
        switch (column.Header) {
            case String.chart_today:
                color = "#ffcc00";
                break;

            case String.chart_lastweek:
                color = "#306fd9";
                break;

            case String.chart_lastmonthavg:
                color = "#9500ff";
                break;
            default:
                break;
        }
        return color ? (
            <span
                className={styles.circleIndicator}
                style={{ backgroundColor: `${color}` }}
            />
        ) : null;
    };

    const handleCell = (cell: Cell) => {
        const text: string = cell.value;

        switch (cell.value) {
            case String.chart_veh_pass:
            case String.chart_avg_veh_que:
            case String.chart_avg_speed:
                {
                    const newText = text.split("\n");
                    return (
                        <span>
                            {newText[0]}
                            <br />
                            {newText[1]}
                        </span>
                    );
                }
                break;

            default:
                break;
        }

        return cell.render("Cell");
    };

    // using material UI - table
    return (
        <div>
            {/* <ReactTable columns={columns} data={data} /> */}
            <table className={styles.chartTable} {...getTableProps()}>
                <thead>
                    {headerGroups.map((headerGroup) => {
                        const { key, ...restHeaderGroupProps } =
                            headerGroup.getHeaderGroupProps();
                        return (
                            <tr key={key} {...restHeaderGroupProps}>
                                {headerGroup.headers.map((column) => {
                                    const { key, ...restHeaderProps } =
                                        column.getHeaderProps();
                                    return (
                                        <th key={key} {...restHeaderProps}>
                                            <Box
                                                className={
                                                    styles.indicatorContainer
                                                }
                                            >
                                                {handleIndicate(column)}
                                                {column.render("Header")}
                                            </Box>
                                        </th>
                                    );
                                })}
                            </tr>
                        );
                    })}
                </thead>
                <tbody {...getTableBodyProps()}>
                    {rows.map((row) => {
                        prepareRow(row);
                        const { key, ...restRowProps } = row.getRowProps();

                        return (
                            <tr key={key} {...restRowProps}>
                                {row.cells.map((cell) => {
                                    const { key, ...restCellProps } =
                                        cell.getCellProps();
                                    return (
                                        <td key={key} {...restCellProps}>
                                            {handleCell(cell)}
                                        </td>
                                    );
                                })}
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    );
}

export default TableMfd;
