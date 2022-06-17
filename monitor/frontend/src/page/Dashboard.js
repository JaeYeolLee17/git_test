import React, { useState } from "react";

import HeaderContent from "../component/HeaderContent";
import Menu from "../component/Menu";
import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";
import DashboardMap from "../component/DashboardMap";
import DashboardMfd from "../component/DashboardMfd";
import { Container } from "@mantine/core";

import styles from "./Dashboard.module.css";

const Dashboard = () => {
    const [selectedRegionId, setSelectedRegionId] = useState("");
    const [currentRegionInfo, setCurrentRegionInfo] = useState({});
    const [listIntersections, setListIntersections] = useState([]);
    const [selectedIntersectionId, setSelectedIntersectionId] = useState("");

    const onChangedCurrentRegion = (regionItem) => {
        //console.log("regionItem", regionItem);
        setSelectedRegionId(regionItem.regionId);
        setCurrentRegionInfo(regionItem);
    };

    const onChangedCurrentIntersection = (intersectionItem) => {
        //console.log("intersectionItem", intersectionItem);
        setSelectedIntersectionId(intersectionItem.intersectionId);
    };

    return (
        <div style={{ position: "relative" }}>
            {/* <DashboardMap
                className={styles.dashboardMap}
                currentRegionInfo={currentRegionInfo}
                intersections={{
                    listIntersections: listIntersections,
                    selectedIntersectionId: selectedIntersectionId,
                }}
                onChangedSelectedItem={(item) => {
                    setSelectedIntersectionId(item.intersectionId);
                }}
            /> */}

            <div className={styles.mapSelectWrap}>
                <SelectorRegion
                    selectedRegionId={selectedRegionId}
                    onChangedCurrentRegion={onChangedCurrentRegion}
                />

                <SelectorIntersection
                    currentRegionInfo={currentRegionInfo}
                    selectedIntersectionId={selectedIntersectionId}
                    onChangedIntersectionList={(list) => {
                        setListIntersections(list);
                    }}
                    onChangedCurrentIntersection={onChangedCurrentIntersection}
                />
            </div>

            {/* <DashboardMfd
                regionId={selectedRegionId}
                intersectionId={selectedIntersectionId}
            /> */}
        </div>
    );
};

export default Dashboard;
