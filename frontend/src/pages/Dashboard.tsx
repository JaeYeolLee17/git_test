import React, { useState } from "react";

import * as Common from "../commons/common";

import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";
import DashboardMap from "../component/DashboardMap";
import DashboardMfd from "../component/DashboardMfd";

// import styles from "./Dashboard.module.css";

const Dashboard = () => {
    const [selectedRegionId, setSelectedRegionId] = useState<string>("");
    const [currentRegionInfo, setCurrentRegionInfo] =
        useState<Common.RegionInfo>({ regionId: "all" });
    const [listIntersections, setListIntersections] = useState<Array<any>>([]);
    const [selectedIntersectionId, setSelectedIntersectionId] =
        useState<string>("");

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

    return (
        <div>
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

            <DashboardMap
                // className={styles.dashboardMap}
                currentRegionInfo={currentRegionInfo}
                intersections={{
                    listIntersections: listIntersections,
                    selectedIntersectionId: selectedIntersectionId,
                }}
                onChangedSelectedItem={(item) => {
                    setSelectedIntersectionId(item.intersectionId);
                }}
            />

            <DashboardMfd
                regionId={selectedRegionId}
                intersectionId={selectedIntersectionId}
            />
        </div>
    );
};

export default Dashboard;
