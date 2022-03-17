import React, { useState } from "react";
import Header from "../component/Header";
import Menu from "../component/Menu";
import SelectorIntersection from "../component/SelectorIntersection";
import SelectorRegion from "../component/SelectorRegion";

const DashboardDetail = () => {
    const [currentRegionInfo, setCurrentRegionInfo] = useState({});

    const onChangedCurrentRegion = (regionItem) => {
        //console.log("regionItem", regionItem);
        setCurrentRegionInfo(regionItem);
    };

    const onChangedCurrentIntersection = (intersectionItem) => {
        //console.log("intersectionItem", intersectionItem);
    };

    return (
        <div>
            <Header />
            <Menu />
            <SelectorRegion onChangedCurrentRegion={onChangedCurrentRegion} />

            <SelectorIntersection
                currentRegionInfo={currentRegionInfo}
                onChangedCurrentIntersection={onChangedCurrentIntersection}
            />
        </div>
    );
};

export default DashboardDetail;
