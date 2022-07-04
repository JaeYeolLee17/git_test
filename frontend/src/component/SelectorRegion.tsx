import React, { ReactNode, useState, useEffect } from "react";
import Selector, { SelectorItemType } from "./Selector";

import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as String from "../commons/string";
import * as Common from "../commons/common";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { SelectChangeEvent } from "@mui/material/Select";

import styles from "./Selector.module.css";

function SelectorRegion({
    selectedRegionId,
    onChangedRegionList,
    onChangedCurrentRegion,
}: Common.SelectorRegion) {
    const userDetails = useAuthState();

    const [listRegions, setListRegions] = useState<Array<any>>([]);
    const [listSelectRegions, setListSelectRegions] = useState<
        Array<SelectorItemType>
    >([]);
    const [listSelectRegionItem, setListSelectRegionItem] =
        useState<SelectorItemType>({ value: "all", innerHTML: String.total });

    const requestRegionList = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.REGIONS_LIST_URL
        );

        return response.data;
    };

    const {
        loading,
        error: errorRegionList,
        data: resultRegionList,
    } = useAsyncAxios(requestRegionList, true);

    useEffect(() => {
        if (resultRegionList === null) return;

        // console.log("resultRegionList", resultRegionList);
        setListRegions(resultRegionList.regions);
    }, [resultRegionList]);

    useEffect(() => {
        if (errorRegionList === null) return;

        // console.log("errorRegionList", errorRegionList);
    }, [errorRegionList]);

    // const requestRegionList = async (e) => {
    //     try {
    //         //console.log(userDetails.token);
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.REGIONS_LIST_URL);

    //         //console.log(JSON.stringify(response?.data));
    //         setListRegions(response?.data.regions);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    // useEffect(() => {
    //     requestRegion();
    // }, []);

    useEffect(() => {
        if (onChangedRegionList !== undefined) {
            onChangedRegionList(listRegions);
        }

        if (Utils.utilIsEmptyArray(listRegions)) {
            return;
        }

        //console.log("list", listRegions);

        const topItem = { value: "all", innerHTML: String.total };

        let newList = [];
        newList.push(topItem);

        newList = newList.concat(
            listRegions.map((region) => {
                //console.log(region);
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

        const currentRegionInfos = listRegions.filter(
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

        const listSelectItems = listSelectRegions.filter(
            (selectorRegion) => selectorRegion.value === selectedRegionId
        );

        if (!Utils.utilIsEmptyArray(listSelectItems)) {
            setListSelectRegionItem(listSelectItems[0]);
        }
    }, [selectedRegionId]);

    const onChangeRegions = (e: SelectChangeEvent) => {
        setListSelectRegionItem({
            value: e.target.value,
            innerHTML: "",
        });
    };

    return (
        <>
            {listSelectRegions.length > 0 ? (
                <Selector
                    className={styles.mapSelectorWrapper}
                    list={listSelectRegions}
                    selected={listSelectRegionItem}
                    onChange={onChangeRegions}
                />
            ) : null}
        </>
    );
}

export default SelectorRegion;
