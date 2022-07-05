import React, { useState } from "react";

import * as Common from "../commons/common";

import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";
import DashboardMap from "../component/DashboardMap";
import DashboardMfd from "../component/DashboardMfd";

import styles from "./Dashboard.module.css";
import { Transition } from "react-transition-group";
import { Box, FormControlLabel, Switch, ToggleButton } from "@mui/material";
import ToggleImageButton from "../component/ToggleImageButton";

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
    const [selectedRegionId, setSelectedRegionId] = useState<string>("");
    const [currentRegionInfo, setCurrentRegionInfo] =
        useState<Common.RegionInfo>({ regionId: "all" });
    const [listIntersections, setListIntersections] = useState<Array<any>>([]);
    const [selectedIntersectionId, setSelectedIntersectionId] =
        useState<string>("");

    const [transitionState, setTransitionState] = useState<string>("");

    const onChangedCurrentRegion = (regionItem: Common.RegionInfo) => {
        //console.log("regionItem", regionItem);
        setSelectedRegionId(regionItem.regionId);
        setCurrentRegionInfo(regionItem);
    };

    const onChangedCurrentIntersection = (
        intersectionItem: Common.IntersectionInfo
    ) => {
        //console.log("intersectionItem", intersectionItem);
        setSelectedIntersectionId(intersectionItem.intersectionId);
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
                            }}
                        >
                            <Box
                                className={[
                                    styles.dashboardMap,
                                    transitionMap[state],
                                ].join(" ")}
                            >
                                {/* <FormControlLabel
                                    control={
                                        <Switch
                                            checked={chartOpen}
                                            onChange={handleChartOpen}
                                        />
                                    }
                                    label='Show'
                                /> */}

                                <DashboardMap
                                    // className={styles.dashboardMap}
                                    transitionState={transitionState}
                                    currentRegionInfo={currentRegionInfo}
                                    intersections={{
                                        listIntersections: listIntersections,
                                        selectedIntersectionId:
                                            selectedIntersectionId,
                                    }}
                                    onChangedSelectedItem={(item) => {
                                        setSelectedIntersectionId(
                                            item.intersectionId
                                        );
                                    }}
                                />
                                <button
                                    className={styles.btnExpansion}
                                    onClick={handleChartOpen}
                                >
                                    O
                                </button>
                                <Box className={styles.mapSelectWrap}>
                                    <ul>
                                        <li>
                                            <SelectorRegion
                                                selectedRegionId={
                                                    selectedRegionId
                                                }
                                                onChangedCurrentRegion={
                                                    onChangedCurrentRegion
                                                }
                                            />
                                        </li>
                                        <li>
                                            <SelectorIntersection
                                                currentRegionInfo={
                                                    currentRegionInfo
                                                }
                                                selectedIntersectionId={
                                                    selectedIntersectionId
                                                }
                                                onChangedIntersectionList={(
                                                    list
                                                ) => {
                                                    setListIntersections(list);
                                                }}
                                                onChangedCurrentIntersection={
                                                    onChangedCurrentIntersection
                                                }
                                            />
                                        </li>
                                    </ul>
                                </Box>
                            </Box>
                            <Box
                                className={[
                                    styles.dashboardChart,
                                    transitionChart[state],
                                ].join(" ")}
                            >
                                <DashboardMfd
                                    regionId={selectedRegionId}
                                    intersectionId={selectedIntersectionId}
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
