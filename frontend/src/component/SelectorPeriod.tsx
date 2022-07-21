import React, { useState, useEffect } from "react";
import Selector, { SelectorItemType } from "./Selector";

import { SelectChangeEvent } from "@mui/material/Select";

import styles from "./Selector.module.css";

function SelectorPeriod({
    onChangedPeriod,
}: {
    onChangedPeriod?: (period: string) => void;
}) {
    const listPeriod = [
        { value: "day", innerHTML: "일간 추이" },
        { value: "weekOfYear", innerHTML: "주간 추이" },
        { value: "month", innerHTML: "월간 추이" },
        { value: "dayOfWeek", innerHTML: "요일별 추이" },
    ];

    const [listSelectPeriodItem, setListSelectPeriodItem] =
        useState<SelectorItemType>({ value: "day", innerHTML: "일간 추이" });

    const onChangePeriod = (e: SelectChangeEvent) => {
        setListSelectPeriodItem({
            value: e.target.value,
            innerHTML: "",
        });
    };

    useEffect(() => {
        if (onChangedPeriod !== undefined)
            onChangedPeriod(listSelectPeriodItem.value);
    }, [listSelectPeriodItem]);

    return (
        <Selector
            className={styles.mapSelectorWrapper}
            list={listPeriod}
            selected={listSelectPeriodItem}
            onChange={onChangePeriod}
        />
    );
}

export default SelectorPeriod;
