import React, { useEffect, useState } from "react";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';

import * as String from "../commons/string";
import { useNavigate } from "react-router-dom";
import styles from "../component/ManagementDetail.module.css";
import { TextField } from "@mui/material";

type tableDataType = {
    name: string,
    data: string,
    hint?: string | undefined,
    type?: string | undefined,
    width: number,
    required: boolean,
    disabled: boolean
}

type responseType = {
    type : string,
    title : Map<string, string>;
    response : tableDataType[];
    clickEvent : any;
}

function ManagementDetail({type, title, response, clickEvent}: responseType) {
    const navigate = useNavigate();
    const [inputs, setInputs] = useState({}); 
    const changeData: any = {};

    useEffect(() => {
        response.map((value) => changeData[value.name] = value.data);
        setInputs(changeData);
    }, [response])
 
    const onChangeEvent = (item: any, data: any) => {
        setInputs((prev) => {
            return {...prev, [item]:data}
        })
    }

    return(
        <Box
            className={styles.box}
            component="form"
            sx={{
            "& .MuiTextField-root": { m: 1, width: "25ch" },
            }}
            noValidate
            autoComplete="off"
        >
            <Grid container spacing={1}>
                {response !== undefined ? (
                response.map((item: tableDataType) => (
                    <Grid item xs={item.width} key={item.name}>
                        {item.name.substring(0, 5) === "empty" && <></>}
                        {item.type === "select" &&
                        <FormControl fullWidth style={{width: "100%", marginBottom: "1rem"}}>
                            <InputLabel required id={item.name} className={styles.selectLabel}>{item.name}</InputLabel>
                            <Select
                                labelId={item.name}
                                id={item.name}
                                defaultValue={item.data === undefined ? "ROLE_USER" : item.data}
                                className={styles.select}
                                onChange={(e) => onChangeEvent(item.name, e.target.value)}
                            >
                                <MenuItem value={'ROLE_ADMIN'}>최고관리자</MenuItem>
                                <MenuItem value={'ROLE_MANAGER'}>관리자</MenuItem>
                                <MenuItem value={'ROLE_USER'}>사용자</MenuItem>
                            </Select>
                        </FormControl>
                        }
                        {(item.name.substring(0, 5) !== "empty" && item.type !== "select") &&
                            <FormControl variant="standard" style={{width: "100%", marginBottom: "1rem"}}>
                                <InputLabel shrink htmlFor={item.name} className={styles.inputLabel} required={item.required}>
                                    {title.get(item.name)}
                                </InputLabel>
                                <TextField
                                    id={item.name}
                                    disabled={item.disabled}
                                    defaultValue={item.data}
                                    className={styles.input}
                                    placeholder={item.hint}
                                    type={item.type}
                                    onChange={(e) => onChangeEvent(item.name, e.target.value)}
                                >
                                </TextField>
                            </FormControl>
                        }
                    </Grid>
                ))
                ) : null}
            </Grid>
            <div style={{textAlign : "center"}}>
                <Button variant="outlined" className={styles.backBtn} onClick={() => navigate(-1)}>{String.back}</Button>
                <Button variant="outlined" className={styles.saveBtn} onClick={() => {clickEvent(type, inputs)}}>{String.save}</Button>
            </div>
        </Box>
    )
}

export default ManagementDetail;