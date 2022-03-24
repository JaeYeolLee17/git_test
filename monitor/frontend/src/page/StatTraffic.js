import React, { useState, useEffect } from "react";
import Header from "../component/Header";
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

        console.log("resultPeriodStat", resultPeriodStat);
        const {
            seriesSrluDatas,
            seriesQtsrluDatas,
            seriesSpeedDatas,
            seriesPersonDatas,
            seriesMfdDatas,
            maxQtsrluData,
        } = Utils.utilConvertChartSeriesPeriod(resultPeriodStat.stat);

        setChartData(Utils.utilConvertChartSeriesPeriod(resultPeriodStat.stat));

        console.log("seriesSrluDatas", seriesSrluDatas);
        console.log("seriesQtsrluDatas", seriesQtsrluDatas);
        console.log("seriesSpeedDatas", seriesSpeedDatas);
        console.log("seriesPersonDatas", seriesPersonDatas);
        console.log("seriesMfdDatas", seriesMfdDatas);
        console.log("maxQtsrluData", maxQtsrluData);
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
            <Header />
            <Menu />

            <Selector list={listPeriod} onChange={onChangePeriod} />
            <SelectorRegion onChangedCurrentRegion={onChangedCurrentRegion} />
            <SelectorIntersection
                currentRegionInfo={currentRegionInfo}
                onChangedCurrentIntersection={onChangedCurrentIntersection}
            />

            <button onClick={onSearch}>Search</button>

            {console.log(
                "chartData.seriesSrluDatas",
                chartData.seriesSrluDatas
            )}

            {!Utils.utilIsEmptyArray(chartData.seriesSrluDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_srlu}
                    series={chartData.seriesSrluDatas}
                    option={option}
                />
            )}
            {!Utils.utilIsEmptyArray(chartData.seriesQtsrluDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_qtsrlu}
                    series={chartData.seriesQtsrluDatas}
                    option={option}
                />
            )}
            {!Utils.utilIsEmptyArray(chartData.seriesSpeedDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_speed}
                    series={chartData.seriesSpeedDatas}
                    option={option}
                />
            )}
            {!Utils.utilIsEmptyArray(chartData.seriesPersonDatas) && (
                <ChartStat
                    loading={loadingPeriodStat}
                    name={String.stats_title_person}
                    series={chartData.seriesPersonDatas}
                    option={optionPersion}
                />
            )}
        </div>
    );
}

export default StatTraffic;
