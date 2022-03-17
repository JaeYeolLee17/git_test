import React, { useState, useEffect } from "react";
import Selector from "./Selector";

import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as String from "../commons/string";
import { useAuthState } from "../provider/AuthProvider";

function SelectorRegion({ selectedRegionId, onChangedCurrentRegion }) {
    const userDetails = useAuthState();

    const [listRegions, setListRegions] = useState([]);
    const [listSelectRegions, setListSelectRegions] = useState([]);
    const [listSelectRegionItem, setListSelectRegionItem] = useState({});

    const requestRegionList = async (e) => {
        try {
            //console.log(userDetails.token);
            const response = await Utils.utilAxiosWithAuth(
                userDetails.token
            ).get(Request.REGIONS_LIST_URL);

            //console.log(JSON.stringify(response?.data));
            setListRegions(response?.data.regions);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        requestRegionList();
    }, []);

    useEffect(() => {
        if (Utils.utilIsEmptyArray(listRegions)) {
            return;
        }

        //console.log("list", listRegions);

        let topItem = { value: "all", innerHTML: String.total };

        let newList = [];
        newList.push(topItem);

        newList = newList.concat(
            listRegions.map((region) => {
                console.log(region);
                return { value: region.regionId, innerHTML: region.regionName };
            })
        );

        setListSelectRegions(newList);

        setListSelectRegionItem(topItem);
    }, [listRegions]);

    useEffect(() => {
        if (Utils.utilIsEmptyObj(listSelectRegionItem)) {
            return;
        }

        let currentRegionInfos = listRegions.filter(
            (region) => region.regionId === listSelectRegionItem.value
        );

        let currentRegionInfo = null;

        if (Utils.utilIsEmptyArray(currentRegionInfos)) {
            currentRegionInfo = { regionId: "all" };
        } else {
            currentRegionInfo = currentRegionInfos[0];
        }

        //console.log("currentRegionInfo", currentRegionInfo);

        if (onChangedCurrentRegion !== undefined) {
            onChangedCurrentRegion(currentRegionInfo);
        }
    }, [listSelectRegionItem]);

    useEffect(() => {
        if (listSelectRegionItem.value === selectedRegionId) return;

        let listSelectItems = listSelectRegions.filter(
            (selectorRegion) => selectorRegion.value === selectedRegionId
        );

        if (!Utils.utilIsEmptyArray(listSelectItems)) {
            setListSelectRegionItem(listSelectItems[0]);
        }
    }, [selectedRegionId]);

    const onChangeRegions = (e) => {
        setListSelectRegionItem({
            value: e.target.value,
            innerHTML: e.target[e.target.selectedIndex].innerHTML,
        });
    };

    return (
        <>
            {listSelectRegions.length > 0 ? (
                <Selector
                    list={listSelectRegions}
                    selected={listSelectRegionItem}
                    onChange={onChangeRegions}
                />
            ) : null}
        </>
    );
}

export default SelectorRegion;
