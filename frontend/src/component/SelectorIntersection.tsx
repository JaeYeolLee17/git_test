import React, { ReactNode, useEffect, useState } from "react";
import Selector, { SelectorItemType } from "./Selector";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as String from "../commons/string";
import * as Common from "../commons/common";

import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { SelectChangeEvent } from "@mui/material/Select";

import styles from "./Selector.module.css";

function SelectorIntersection({
    currentRegionInfo,
    selectedIntersectionNo,
    onChangedIntersectionList,
    onChangedCurrentIntersection,
}: Common.SelectorIntersection) {
    const userDetails = useAuthState();

    const [listIntersections, setListIntersections] = useState<Array<any>>([]);
    const [listSelectIntersections, setListSelectIntersections] = useState<Array<SelectorItemType>>([{ value: "all", innerHTML: String.total }]);
    const [listSelectIntersectionItem, setListSelectIntersectionItem] = useState<SelectorItemType>({ value: "all", innerHTML: String.total });

    const requestAxiosIntersectionList = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(Request.INTERSECTION_LIST_URL, {
            params: {
                ...(currentRegionInfo.regionNo !== "all" ? { regionNo: currentRegionInfo.regionNo } : {}),
            },
        });
        return response.data;
    };

    const {
        loading,
        error: errorIntersectionList,
        data: resultIntersectionList,
        execute: requestIntersectionList,
    } = useAsyncAxios(requestAxiosIntersectionList);

    useEffect(() => {
        if (resultIntersectionList === null) return;
        // console.log("resultIntersectionList", resultIntersectionList);

        setListIntersections(resultIntersectionList.intersections);
    }, [resultIntersectionList]);

    useEffect(() => {
        if (errorIntersectionList === null) return;

        // console.log("errorIntersectionList", errorIntersectionList);
    }, [errorIntersectionList]);

    // const requestIntersectionList = async (e) => {
    //     try {
    //         const response = await Utils.utilAxiosWithAuth(
    //             userDetails.token
    //         ).get(Request.INTERSECTION_LIST_URL, {
    //             params: {
    //                 ...(currentRegionInfo.regionId !== "all"
    //                     ? { regionId: currentRegionInfo.regionId }
    //                     : {}),
    //             },
    //         });

    //         setListIntersections(response?.data.intersections);
    //     } catch (err) {
    //         console.log(err);
    //     }
    // };

    useEffect(() => {
        requestIntersectionList();
    }, [currentRegionInfo]);

    useEffect(() => {
        if (onChangedIntersectionList !== undefined) {
            onChangedIntersectionList(listIntersections);
        }

        if (Utils.utilIsEmptyArray(listIntersections)) {
            return;
        }

        const topItem = { value: "all", innerHTML: String.total };

        let newList = [];
        newList.push(topItem);

        newList = newList.concat(
            listIntersections
                .filter((intersection) => intersection.region !== null)
                .map((intersection) => {
                    //console.log(region);
                    return {
                        value: intersection.intersectionNo,
                        innerHTML: intersection.intersectionName,
                    };
                })
        );

        setListSelectIntersections(newList);

        setListSelectIntersectionItem(topItem);
    }, [listIntersections]);

    useEffect(() => {
        if (Utils.utilIsEmptyObj(listSelectIntersectionItem)) return;
        const currentIntersectionInfos = listIntersections.filter((intersection) => intersection.intersectionNo === listSelectIntersectionItem.value);

        let currentIntersectionInfo = null;

        if (Utils.utilIsEmptyArray(currentIntersectionInfos)) {
            currentIntersectionInfo = { intersectionNo: "all" };
        } else {
            currentIntersectionInfo = currentIntersectionInfos[0];
        }

        if (onChangedCurrentIntersection !== undefined) {
            onChangedCurrentIntersection(currentIntersectionInfo);
        }
    }, [listSelectIntersectionItem]);

    useEffect(() => {
        if (listSelectIntersectionItem.value === selectedIntersectionNo) return;

        const listSelectItems = listSelectIntersections.filter((selectorIntersection) => selectorIntersection.value === selectedIntersectionNo);

        if (!Utils.utilIsEmptyArray(listSelectItems)) {
            setListSelectIntersectionItem(listSelectItems[0]);
        }
    }, [selectedIntersectionNo]);

    const onChangeIntersections = (e: SelectChangeEvent) => {
        setListSelectIntersectionItem({
            value: e.target.value,
            innerHTML: "",
        });
    };

    return (
        <>
            {listSelectIntersections.length > 0 ? (
                <Selector
                    className={styles.mapSelectorWrapper}
                    list={listSelectIntersections}
                    selected={listSelectIntersectionItem}
                    onChange={onChangeIntersections}
                />
            ) : null}
        </>
    );
}

export default SelectorIntersection;
