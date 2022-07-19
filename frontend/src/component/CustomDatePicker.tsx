import moment from "moment";
import React, { useEffect, useState } from "react";
import styles from "./CustomDatePicker.module.css";

// import { AdapterMoment } from "@mui/x-date-pickers/AdapterMoment";
// import { LocalizationProvider } from "@mui/x-date-pickers";
// import { DatePicker } from "@mui/x-date-pickers/DatePicker";
// import { TextField } from "@mui/material";

function CustomDatePicker({
    onChangedDate,
    defaultDate,
}: {
    onChangedDate?: (date: string) => void;
    defaultDate?: string;
}) {
    const [date, setDate] = useState<string>("");
    useEffect(() => {
        moment.locale("ko");
        if (defaultDate === undefined) {
            setDate(moment().format("YYYY-MM-DD"));
        } else {
            setDate(defaultDate);
        }
    }, []);

    useEffect(() => {
        if (onChangedDate !== undefined) onChangedDate(date);
    }, [date]);

    const onChangeDate = (e: React.FormEvent<HTMLInputElement>) => {
        setDate(e.currentTarget.value);
    };

    // const [value, setValue] = React.useState<Date | null>(null);

    return (
        // <LocalizationProvider dateAdapter={AdapterMoment}>
        //     <DatePicker
        //         className={styles.datePickerWrapper}
        //         label='Basic example'
        //         value={value}
        //         onChange={(newValue) => {
        //             setValue(newValue);
        //         }}
        //         renderInput={(params) => <TextField {...params} />}
        //     />
        // </LocalizationProvider>
        <div className={styles.datePickerWrapper}>
            <label htmlFor='searchStartDate' className={styles.datePickerLabel}>
                시작일
            </label>
            <input
                type='date'
                className={styles.datePickerInput}
                id='searchStartDate'
                placeholder='시작일'
                value={date}
                onChange={onChangeDate}
            />
        </div>
    );
}

export default CustomDatePicker;
