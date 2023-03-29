import React, { useState } from "react";

import * as Common from "../commons/common";

import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";
import DashboardMap from "../component/DashboardMap";
import DashboardMfd from "../component/DashboardMfd";

import styles from "./Dashboard.module.css";
import { CSSTransition, Transition } from "react-transition-group";
import { Box, FormControlLabel, Switch, ToggleButton } from "@mui/material";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";

const transitionMap: { [key: string]: string } = {
    entering: styles.dashboardMapExtend,
    entered: styles.dashboardMapExtend,
    exiting: "",
    exited: "",
};

const transitionChart: { [key: string]: string } = {
    entering: styles.dashboardChartHidden,
    entered: styles.dashboardChartHidden,
    exiting: "",
    exited: "",
};

const Dashboard = () => {
    const [chartOpen, setChartOpen] = useState<boolean>(false);
    const [selectedRegionNo, setSelectedRegionNo] = useState<string>("");
    const [selectedRegionName, setSelectedRegionName] = useState<string | undefined>("");
    const [currentRegionInfo, setCurrentRegionInfo] = useState<Common.RegionInfo>({ regionNo: "all" });
    const [listIntersections, setListIntersections] = useState<Array<any>>([]);
    const [selectedIntersectionNo, setSelectedIntersectionNo] = useState<string>("");
    const [selectedIntersectionName, setSelectedIntersectionName] = useState<string | undefined>("");

    const [transitionState, setTransitionState] = useState<string>("");

    const onChangedCurrentRegion = (regionItem: Common.RegionInfo) => {
        setSelectedRegionNo(regionItem.regionNo);
        setSelectedRegionName(regionItem.regionName);
        setCurrentRegionInfo(regionItem);
    };

    const onChangedCurrentIntersection = (intersectionItem: Common.IntersectionInfo) => {
        setSelectedIntersectionNo(intersectionItem.intersectionNo);
        setSelectedIntersectionName(intersectionItem.intersectionName);
    };

    const handleChartOpen = () => {
        setChartOpen((prev) => !prev);
    };

    return (
        <div>
            <Transition
                in={chartOpen}
                timeout={400}
                onEntering={() => setTransitionState("entering")}
                onEntered={() => setTransitionState("entered")}
                onExiting={() => setTransitionState("exiting")}
                onExited={() => setTransitionState("exited")}
            >
                {(state: string) => {
                    return (
                        <Box
                            sx={{
                                display: "flex",
                                height: "100%",
                                overflow: "hidden",
                            }}
                        >
                            <Box className={[styles.dashboardMap, transitionMap[state]].join(" ")}>
                                {/* <FormControlLabel
                                    control={
                                        <Switch
                                            checked={chartOpen}
                                            onChange={handleChartOpen}
                                        />
                                    }
                                    label="Show"
                                /> */}

                                <DashboardMap
                                    // className={styles.dashboardMap}
                                    transitionState={transitionState}
                                    currentRegionInfo={currentRegionInfo}
                                    intersections={{
                                        listIntersections: listIntersections,
                                        selectedIntersectionNo: selectedIntersectionNo,
                                        selectedIntersectionName: selectedIntersectionName,
                                    }}
                                    onChangedSelectedItem={(item) => {
                                        setSelectedIntersectionNo(item.intersectionNo);
                                    }}
                                />
                                <button className={styles.btnExpansion} onClick={handleChartOpen}>
                                    {chartOpen ? <ArrowBackIosNewIcon fontSize="small" /> : <ArrowForwardIosIcon fontSize="small" />}
                                </button>
                                <Box className={styles.mapSelectWrap}>
                                    <ul>
                                        <li>
                                            <SelectorRegion selectedRegionNo={selectedRegionNo} onChangedCurrentRegion={onChangedCurrentRegion} />
                                        </li>
                                        <li>
                                            <SelectorIntersection
                                                currentRegionInfo={currentRegionInfo}
                                                selectedIntersectionNo={selectedIntersectionNo}
                                                onChangedIntersectionList={(list) => {
                                                    setListIntersections(list);
                                                }}
                                                onChangedCurrentIntersection={onChangedCurrentIntersection}
                                            />
                                        </li>
                                    </ul>
                                </Box>
                            </Box>
                            <Box className={[styles.dashboardChart, transitionChart[state]].join(" ")}>
                                <DashboardMfd
                                    regionNo={selectedRegionNo}
                                    regionName={selectedRegionName}
                                    intersectionNo={selectedIntersectionNo}
                                    intersectionName={selectedIntersectionName}
                                />
                            </Box>
                        </Box>
                    );
                }}
            </Transition>
        </div>
    );
};

export default Dashboard;
