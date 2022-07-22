import React, { useEffect, useState } from "react";
import styles from "./DashboardDetail.module.css";
import { Box } from "@mui/material";
import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";
import * as Common from "../commons/common";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as String from "../commons/string";
import CustomDatePicker from "../component/CustomDatePicker";

// import SearchIcon from "@mui/icons-material/Search";
import ChartStatWrapper from "../component/ChartStatWrapper";
import { useAsyncAxios } from "../utils/customHooks";
import { useAuthState } from "../provider/AuthProvider";
import SearchButton from "../component/SearchButton";

function DashboardDetail() {
    const userDetails = useAuthState();

    const [selectedRegionId, setSelectedRegionId] = useState<string>("");
    const [currentRegionInfo, setCurrentRegionInfo] =
        useState<Common.RegionInfo>({ regionId: "all" });
    const [selectedIntersectionId, setSelectedIntersectionId] =
        useState<string>("");
    const [listCamera, setListCamera] = useState<Array<any>>([]);
    const [listIntersections, setListIntersections] = useState<Array<any>>([]);
    const [searchDate, setSearchDate] = useState<string>("");

    const [requestCarTypeNCamera, setRequestCarTypeNCamera] =
        useState<boolean>(false);

    const [seriesChartSrluCarType, setSeriesChartSrluCarType] = useState<
        Array<Common.ChartData>
    >([]);
    const [seriesChartQtsrluCarType, setSeriesChartQtsrluCarType] = useState<
        Array<Common.ChartData>
    >([]);
    const [seriesChartSrluCamera, setSeriesChartSrluCamera] = useState<
        Array<Common.ChartData>
    >([]);
    const [seriesChartQtsrluCamera, setSeriesChartQtsrluCamera] = useState<
        Array<Common.ChartData>
    >([]);
    const [seriesChartPerson, setSeriesChartPerson] = useState<
        Array<Common.ChartData>
    >([]);

    useEffect(() => {
        requestCameras();
    }, []);

    const onChangedCurrentRegion = (regionItem: Common.RegionInfo) => {
        //console.log("regionItem", regionItem);
        setSelectedRegionId(regionItem.regionId);
        // setSelectedRegionName(regionItem.regionName);
        setCurrentRegionInfo(regionItem);
    };

    const onChangedCurrentIntersection = (
        intersectionItem: Common.IntersectionInfo
    ) => {
        //console.log("intersectionItem", intersectionItem);
        setSelectedIntersectionId(intersectionItem.intersectionId);
        // setSelectedIntersectionName(intersectionItem.intersectionName);
    };

    const onChangedSearchDate = (date: string) => {
        setSearchDate(date);
    };

    const requestAxiosCameras = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.CAMERA_LIST_URL
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

        console.log("resultCamerasList", resultCamerasList);
        setListCamera(resultCamerasList.cameras);

        onSearch();
    }, [resultCamerasList]);

    useEffect(() => {
        if (errorCamerasList === null) return;

        console.log("errorCamerasList", errorCamerasList);
    }, [errorCamerasList]);

    const getExtraParams = () => {
        let extraParam = {};

        if (selectedIntersectionId === "" || selectedIntersectionId === "all") {
            if (selectedRegionId !== "" && selectedRegionId !== "all") {
                extraParam = {
                    filterBy: "region",
                    filterId: selectedRegionId,
                };
            }
        } else {
            extraParam = {
                filterBy: "intersection",
                filterId: selectedIntersectionId,
            };
        }

        return extraParam;
    };

    const requestAxiosStatCarType = async () => {
        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        const extraParam = getExtraParams();

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.STAT_DAILY_URL,
            {
                params: {
                    date: searchDate,
                    groupBy: "carType",
                    ...extraParam,
                },
            }
        );

        return response.data;
    };

    const {
        loading: loadingStatCarType,
        error: errorStatCarType,
        data: resultStatCarType,
        execute: requestStatCarType,
    } = useAsyncAxios(requestAxiosStatCarType);

    useEffect(() => {
        if (resultStatCarType === null) return;
        if (resultStatCarType.stat[0] === null) return;

        const statData = resultStatCarType.stat[0];
        //console.log("statData", JSON.stringify(statData));

        const { seriesChartSrlu, seriesChartQtsrlu, seriesChartPerson } =
            Utils.utilConvertChartSeriesCarType(statData);

        // console.log("seriesChartSrlu", seriesChartSrlu);
        // console.log("seriesChartQtsrlu", seriesChartQtsrlu);
        // console.log("seriesChartPerson", seriesChartPerson);

        setSeriesChartSrluCarType(seriesChartSrlu);
        setSeriesChartQtsrluCarType(seriesChartQtsrlu);
        if (requestCarTypeNCamera === false) {
            setSeriesChartPerson(seriesChartPerson);
        }
    }, [resultStatCarType]);

    useEffect(() => {
        if (errorStatCarType === null) return;

        console.log("errorStatCarType", errorStatCarType);
    }, [errorStatCarType]);

    const requestAxiosStatCamera = async () => {
        //console.log("startTime", startTime);
        //console.log("endTime", endTime);

        const extraParam = getExtraParams();

        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.STAT_DAILY_URL,
            {
                params: {
                    date: searchDate,
                    groupBy: "camera",
                    ...extraParam,
                },
            }
        );

        return response.data;
    };

    const {
        loading: loadingStatCamera,
        error: errorStatCamera,
        data: resultStatCamera,
        execute: requestStatCamera,
    } = useAsyncAxios(requestAxiosStatCamera);

    useEffect(() => {
        if (resultStatCamera === null) return;
        if (resultStatCamera.stat === null) return;

        const statData = resultStatCamera.stat;
        //console.log("statData", JSON.stringify(statData));

        const { seriesChartSrlu, seriesChartQtsrlu, seriesChartPerson } =
            Utils.utilConvertChartSeriesCamera(statData, listCamera);

        console.log("seriesChartSrlu", seriesChartSrlu);
        console.log("seriesChartQtsrlu", seriesChartQtsrlu);
        console.log("seriesChartPerson", seriesChartPerson);

        setSeriesChartSrluCamera(seriesChartSrlu);
        setSeriesChartQtsrluCamera(seriesChartQtsrlu);
        if (requestCarTypeNCamera === true) {
            setSeriesChartPerson(seriesChartPerson);
        }
    }, [resultStatCamera]);

    useEffect(() => {
        if (errorStatCamera === null) return;

        console.log("errorStatCamera", errorStatCamera);
    }, [errorStatCamera]);

    const onSearch = () => {
        if (selectedIntersectionId === "" || selectedIntersectionId === "all") {
            setRequestCarTypeNCamera(false);
            requestStatCarType();
        } else {
            setRequestCarTypeNCamera(true);
            requestStatCarType();
            requestStatCamera();
        }
    };

    const commonChartOption = {
        chart: {
            height: "500px",
            type: "bar",
            stacked: true,
        },
        colors: ["#224d99", "#3b84ff", "#17e6b1", "#ffd500", "#ff5b7d"],
        title: {
            align: "center",
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
    };

    return (
        <Box sx={{ display: "flex", flexDirection: "column" }}>
            <Box className={styles.searchBar}>
                <ul className={styles.searchBarUl}>
                    <li>
                        <SelectorRegion
                            selectedRegionId={selectedRegionId}
                            onChangedCurrentRegion={onChangedCurrentRegion}
                        />
                    </li>
                    <li>
                        <SelectorIntersection
                            currentRegionInfo={currentRegionInfo}
                            selectedIntersectionId={selectedIntersectionId}
                            onChangedIntersectionList={(list) => {
                                setListIntersections(list);
                            }}
                            onChangedCurrentIntersection={
                                onChangedCurrentIntersection
                            }
                        />
                    </li>
                    <li>
                        <CustomDatePicker onChangedDate={onChangedSearchDate} />
                    </li>
                    <li>
                        {/* <button
                            type='button'
                            className={styles.searchBtn}
                            onClick={onSearch}
                        >
                            <SearchIcon />
                            <span>검색</span>
                        </button> */}
                        <SearchButton onSearch={onSearch} />
                    </li>
                </ul>
            </Box>
            <Box className={styles.statCharts}>
                <ChartStatWrapper
                    title={String.stats_title_cartype_srlu}
                    loading={loadingStatCarType}
                    series={seriesChartSrluCarType}
                    option={commonChartOption}
                />
                {requestCarTypeNCamera ? (
                    <ChartStatWrapper
                        title={String.stats_title_camera_srlu}
                        loading={loadingStatCamera}
                        series={seriesChartSrluCamera}
                        option={commonChartOption}
                    />
                ) : null}
                <ChartStatWrapper
                    title={String.stats_title_cartype_qtsrlu}
                    loading={loadingStatCarType}
                    series={seriesChartQtsrluCarType}
                    option={commonChartOption}
                />
                {requestCarTypeNCamera ? (
                    <ChartStatWrapper
                        title={String.stats_title_camera_qtsrlu}
                        loading={loadingStatCamera}
                        series={seriesChartQtsrluCamera}
                        option={commonChartOption}
                    />
                ) : null}
                <ChartStatWrapper
                    title={String.stats_title_person}
                    loading={
                        requestCarTypeNCamera
                            ? loadingStatCarType || loadingStatCamera
                            : loadingStatCarType
                    }
                    series={seriesChartPerson}
                    option={commonChartOption}
                />
            </Box>
        </Box>
    );
}

export default DashboardDetail;
