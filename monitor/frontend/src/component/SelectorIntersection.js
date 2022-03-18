import React, { useEffect, useState } from "react";
import Selector from "./Selector";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as String from "../commons/string";

import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";

function SelectorIntersection({
    currentRegionInfo,
    selectedIntersectionId,
    onChangedIntersectionList,
    onChangedCurrentIntersection,
}) {
    const userDetails = useAuthState();

    const [listIntersections, setListIntersections] = useState([]);
    const [listSelectIntersections, setListSelectIntersections] = useState([]);
    const [listSelectIntersectionItem, setListSelectIntersectionItem] =
        useState({});

    const requestAxiosIntersectionList = async () => {
        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.INTERSECTIONS_LIST_URL,
            {
                params: {
                    ...(currentRegionInfo.regionId !== "all"
                        ? { regionId: currentRegionInfo.regionId }
                        : {}),
                },
            }
        );
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
    //         ).get(Request.INTERSECTIONS_LIST_URL, {
    //             params: {
    //                 ...(currentRegionInfo.regionId !== "all"
    //                     ? { regionId: currentRegionInfo.regionId }
    //                     : {}),
    //             },
    //         });

    //         //console.log(JSON.stringify(response?.data));
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

        //console.log("listIntersections", listIntersections);

        let topItem = { value: "all", innerHTML: String.total };

        let newList = [];
        newList.push(topItem);

        newList = newList.concat(
            listIntersections
                .filter((intersection) => intersection.region !== null)
                .map((intersection) => {
                    //console.log(region);
                    return {
                        value: intersection.intersectionId,
                        innerHTML: intersection.intersectionName,
                    };
                })
        );

        setListSelectIntersections(newList);

        setListSelectIntersectionItem(topItem);
    }, [listIntersections]);

    useEffect(() => {
        if (Utils.utilIsEmptyObj(listSelectIntersectionItem)) return;
        let currentIntersectionInfos = listIntersections.filter(
            (intersection) =>
                intersection.intersectionId === listSelectIntersectionItem.value
        );

        let currentIntersectionInfo = null;

        if (Utils.utilIsEmptyArray(currentIntersectionInfos)) {
            currentIntersectionInfo = { intersectionId: "all" };
        } else {
            currentIntersectionInfo = currentIntersectionInfos[0];
        }

        if (onChangedCurrentIntersection !== undefined) {
            onChangedCurrentIntersection(currentIntersectionInfo);
        }
    }, [listSelectIntersectionItem]);

    useEffect(() => {
        if (listSelectIntersectionItem.value === selectedIntersectionId) return;

        let listSelectItems = listSelectIntersections.filter(
            (selectorIntersection) =>
                selectorIntersection.value === selectedIntersectionId
        );

        if (!Utils.utilIsEmptyArray(listSelectItems)) {
            setListSelectIntersectionItem(listSelectItems[0]);
        }
    }, [selectedIntersectionId]);

    const onChangeIntersections = (e) => {
        //console.log(e);
        setListSelectIntersectionItem({
            value: e.target.value,
            innerHTML: e.target[e.target.selectedIndex].innerHTML,
        });
    };

    return (
        <>
            {listSelectIntersections.length > 0 ? (
                <Selector
                    list={listSelectIntersections}
                    selected={listSelectIntersectionItem}
                    onChange={onChangeIntersections}
                />
            ) : null}
        </>
    );
}

export default SelectorIntersection;
