import React, { useState, useEffect } from "react";
import Header from "../component/Header";
import Menu from "../component/Menu";
import SelectorIntersection from "../component/SelectorIntersection";
import SelectorRegion from "../component/SelectorRegion";

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as String from "../commons/string";
import { useAsyncAxios } from "../utils/customHooks";
import { useAuthState } from "../provider/AuthProvider";
import ChartStat from "../component/ChartStat";

const DashboardDetail = () => {
    const RequestType = {
        All: "All",
        PreBuiltIntersection: "PreBuiltIntersection",
        Intersection: "OnlyCarType",
    };

    const option = {
        chart: {
            height: "500px",
            type: "bar",
            stacked: true,
        },
        colors: ["#224d99", "#3b84ff", "#17e6b1", "#ffd500", "#ff5b7d"],

        grid: {
            show: true,
            padding: {
                top: 20,
                right: 0,
                bottom: 20,
                left: 0,
            },
        },
    };

    const [firstLoad, setFirstLoad] = useState(true);
    const [currentRegionInfo, setCurrentRegionInfo] = useState({});
    const [currentIntersectionInfo, setCurrentIntersectionInfo] = useState({});
    const [searchDate, setSearchDate] = useState(new Date());

    const [listCamera, setListCamera] = useState([]);

    const [dataDailyStatCarType, setDataDailyStatCarType] = useState({});
    const [dataDailyStatCamera, setDataDailyStatCamera] = useState({});

    const [requestType, setRequestType] = useState(RequestType.All);
    const [seriesChartSrluCarType, setSeriesChartSrluCarType] = useState([]);
    const [seriesChartQtsrluCarType, setSeriesChartQtsrluCarType] = useState(
        []
    );
    const [seriesChartSrluCamera, setSeriesChartSrluCamera] = useState([]);
    const [seriesChartQtsrluCamera, setSeriesChartQtsrluCamera] = useState([]);
    const [seriesChartPerson, setSeriesChartPerson] = useState([]);

    const userDetails = useAuthState();

    const onChangedCurrentRegion = (regionItem) => {
        //console.log("regionItem", regionItem);
        setCurrentRegionInfo(regionItem);
    };

    const onChangedCurrentIntersection = (intersectionItem) => {
        //console.log("intersectionItem", intersectionItem);
        setCurrentIntersectionInfo(intersectionItem);
    };

    const requestAxiosCameras = async () => {
        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.CAMERA_URL
        );
        return response.data;
    };

    const {
        loading: loadingCamerasList,
        error: errorCamerasList,
        data: resultCamerasList,
        execute: requestCameras,
    } = useAsyncAxios(requestAxiosCameras);

    useEffect(() => {
        if (resultCamerasList === null) return;

        //console.log("resultCamerasList", resultCamerasList);
        setListCamera(resultCamerasList.cameras);
    }, [resultCamerasList]);

    useEffect(() => {
        if (errorCamerasList === null) return;

        console.log("errorCamerasList", errorCamerasList);
    }, [errorCamerasList]);

    const requestAxiosDailyStat = async (option) => {
        //console.log("option", option);
        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.STAT_DAILY_URL,
            option
        );

        return response.data;
    };

    const {
        loading: loadingDailyStatCamera,
        error: errorDailyStatCamera,
        data: resultDailyStatCamera,
        execute: requestDailyStatCamera,
    } = useAsyncAxios(requestAxiosDailyStat);

    const {
        loading: loadingDailyStatCarType,
        error: errorDailyStatCarType,
        data: resultDailyStatCarType,
        execute: requestDailyStatCarType,
    } = useAsyncAxios(requestAxiosDailyStat);

    useEffect(() => {
        if (resultDailyStatCamera === null) return;

        //console.log("resultDailyStatCamera", resultDailyStatCamera);
        setDataDailyStatCamera(resultDailyStatCamera.stat);
    }, [resultDailyStatCamera]);

    useEffect(() => {
        if (errorDailyStatCamera === null) return;

        console.log("errorDailyStatCamera", errorDailyStatCamera);
    }, [errorDailyStatCamera]);

    useEffect(() => {
        if (resultDailyStatCarType === null) return;

        //console.log("resultDailyStatCarType", resultDailyStatCarType);
        setDataDailyStatCarType(resultDailyStatCarType.stat[0]);
    }, [resultDailyStatCarType]);

    useEffect(() => {
        if (errorDailyStatCarType === null) return;

        console.log("errorDailyStatCarType", errorDailyStatCarType);
    }, [errorDailyStatCarType]);

    const onSearch = () => {
        setDataDailyStatCamera({});
        setDataDailyStatCarType({});

        let date = Utils.utilFormatDateYYYYMMDD(searchDate);

        // console.log("date", date);
        // console.log("currentRegionInfo", currentRegionInfo);
        // console.log("currentIntersectionInfo", currentIntersectionInfo);

        if (
            currentIntersectionInfo.intersectionId === "" ||
            currentIntersectionInfo.intersectionId === "all"
        ) {
            let extraParamCarType = { date: date, groupBy: "carType" };

            if (
                currentRegionInfo.regionId !== "" &&
                currentRegionInfo.regionId !== "all"
            ) {
                extraParamCarType = {
                    ...extraParamCarType,
                    filterBy: "region",
                    filterId: currentRegionInfo.regionId,
                };
            }
            //console.log("extraParamCarType", extraParamCarType);
            setRequestType(RequestType.All);
            requestDailyStatCarType({ params: extraParamCarType });
        } else if (
            Utils.isPrebuiltIntersection(currentIntersectionInfo.intersectionId)
        ) {
            let extraParamCamera = {
                date: date,
                groupBy: "camera",
                filterBy: "intersection",
                filterId: currentIntersectionInfo.intersectionId,
            };

            //console.log("extraParamCamera", extraParamCamera);
            setRequestType(RequestType.PreBuiltIntersection);
            requestDailyStatCamera({ params: extraParamCamera });
        } else {
            let extraParamCarType = {
                date: date,
                groupBy: "carType",
                filterBy: "intersection",
                filterId: currentIntersectionInfo.intersectionId,
            };
            //console.log("extraParamCarType", extraParamCarType);
            let extraParamCamera = {
                date: date,
                groupBy: "camera",
                filterBy: "intersection",
                filterId: currentIntersectionInfo.intersectionId,
            };
            //console.log("extraParamCamera", extraParamCamera);

            setRequestType(RequestType.Intersection);
            requestDailyStatCarType({ params: extraParamCarType });
            requestDailyStatCamera({ params: extraParamCamera });
        }
    };

    useEffect(() => {
        requestCameras();
    }, []);

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

    useEffect(() => {
        // console.log("dataDailyStatCamera", dataDailyStatCamera);
        // console.log("dataDailyStatCarType", dataDailyStatCarType);
        // console.log("listCamera", listCamera);

        if (!Utils.utilIsEmptyObj(dataDailyStatCarType)) {
            const { seriesChartSrlu, seriesChartQtsrlu, seriesChartPerson } =
                Utils.utilConvertChartSeriesCarType(dataDailyStatCarType);

            //console.log("seriesChartSrlu", seriesChartSrlu);
            //console.log("seriesChartQtsrlu", seriesChartQtsrlu);

            setSeriesChartSrluCarType(seriesChartSrlu);
            setSeriesChartQtsrluCarType(seriesChartQtsrlu);

            if (requestType === RequestType.All) {
                //console.log("seriesChartPerson", seriesChartPerson);
                setSeriesChartPerson(seriesChartPerson);
            }
        }

        if (
            !Utils.utilIsEmptyObj(dataDailyStatCamera) &&
            !Utils.utilIsEmptyArray(listCamera)
        ) {
            const { seriesChartSrlu, seriesChartQtsrlu, seriesChartPerson } =
                Utils.utilConvertChartSeriesCamera(
                    dataDailyStatCamera,
                    listCamera
                );

            //console.log("seriesChartSrlu", seriesChartSrlu);
            //console.log("seriesChartQtsrlu", seriesChartQtsrlu);

            setSeriesChartSrluCamera(seriesChartSrlu);

            if (requestType === RequestType.Intersection) {
                //console.log("seriesChartPerson", seriesChartPerson);
                setSeriesChartQtsrluCamera(seriesChartQtsrlu);
                setSeriesChartPerson(seriesChartPerson);
            }
        }
    }, [listCamera, dataDailyStatCamera, dataDailyStatCarType]);

    //console.log("seriesChartPerson", seriesChartPerson);
    // console.log("loadingDailyStatCarType", loadingDailyStatCarType);
    // console.log("loadingDailyStatCamera", loadingDailyStatCamera);

    return (
        <div>
            <Header />
            <Menu />

            <SelectorRegion onChangedCurrentRegion={onChangedCurrentRegion} />
            <SelectorIntersection
                currentRegionInfo={currentRegionInfo}
                onChangedCurrentIntersection={onChangedCurrentIntersection}
            />

            <DatePicker
                dateFormat={"yyyy년 MM월 dd일"}
                selected={searchDate}
                onChange={(date) => setSearchDate(date)}
            />

            <button onClick={onSearch}>Search</button>

            {(requestType === RequestType.All ||
                requestType === RequestType.Intersection) && (
                <ChartStat
                    loading={loadingDailyStatCarType}
                    name={String.stats_title_cartype_srlu}
                    series={seriesChartSrluCarType}
                    option={option}
                />
            )}
            {(requestType === RequestType.Intersection ||
                requestType === RequestType.PreBuiltIntersection) && (
                <ChartStat
                    loading={loadingDailyStatCamera}
                    name={String.stats_title_camera_srlu}
                    series={seriesChartSrluCamera}
                    option={option}
                />
            )}
            {(requestType === RequestType.All ||
                requestType === RequestType.Intersection) && (
                <ChartStat
                    loading={loadingDailyStatCarType}
                    name={String.stats_title_cartype_qtsrlu}
                    series={seriesChartQtsrluCarType}
                    option={option}
                />
            )}
            {requestType === RequestType.Intersection && (
                <ChartStat
                    loading={loadingDailyStatCamera}
                    name={String.stats_title_camera_qtsrlu}
                    series={seriesChartQtsrluCamera}
                    option={option}
                />
            )}
            {(requestType === RequestType.All ||
                requestType === RequestType.Intersection) && (
                <ChartStat
                    loading={loadingDailyStatCamera || loadingDailyStatCarType}
                    name={String.stats_title_person}
                    series={seriesChartPerson}
                    option={option}
                />
            )}
        </div>
    );
};

export default DashboardDetail;
