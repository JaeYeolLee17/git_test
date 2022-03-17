import React, { useState } from "react";

import Header from "../component/Header";
import Menu from "../component/Menu";
import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";
import DashboardMap from "../component/DashboardMap";
import DashboardMfd from "../component/DashboardMfd";

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
        <div>
            <Header />
            <Menu />

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
