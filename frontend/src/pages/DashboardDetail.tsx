import React, { useState } from "react";
import styles from "./DashboardDetail.module.css";
import { Box } from "@mui/material";
import SelectorRegion from "../component/SelectorRegion";
import SelectorIntersection from "../component/SelectorIntersection";
import * as Common from "../commons/common";
import CustomDatePicker from "../component/CustomDatePicker";

import SearchIcon from "@mui/icons-material/Search";

function DashboardDetail() {
    const [selectedRegionId, setSelectedRegionId] = useState<string>("");
    const [currentRegionInfo, setCurrentRegionInfo] =
        useState<Common.RegionInfo>({ regionId: "all" });
    const [selectedIntersectionId, setSelectedIntersectionId] =
        useState<string>("");
    const [listIntersections, setListIntersections] = useState<Array<any>>([]);
    const [searchDate, setSearchDate] = useState<string>("");

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

    return (
        <div>
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
                        <button type='button' className={styles.searchBtn}>
                            <SearchIcon />
                            <span>검색</span>
                        </button>
                    </li>
                </ul>
            </Box>
        </div>
    );
}

export default DashboardDetail;
