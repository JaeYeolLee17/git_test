import React, { useState } from "react";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Grid from "@mui/material/Grid";
import * as String from "../commons/string";
import { useNavigate } from "react-router-dom";

type tableDataType = {
    name: string,
    data: string,
    width: number,
    required: boolean,
    disabled: boolean
}

type responseType = {
    response : tableDataType[];
    clickEvent : any;
}

function ManagementDetail({response, clickEvent}: responseType) {
    const navigate = useNavigate();
    const [inputs, setInputs] = useState({});

    const onChange = (data: any) => {
        const {name, value} = data;
        setInputs({
            ...inputs,
            [name]: value
        })
    }

    return(
        <Box
            component="form"
            sx={{
            '& .MuiTextField-root': { m: 1, width: '25ch' },
            }}
            noValidate
            autoComplete="off"
        >
            
            <Grid container spacing={1}>
                {response !== undefined ? (
                response.map((item: tableDataType) => (
                    <Grid item xs={item.width} key={item.name}>
                        <TextField
                            name={item.name}
                            label={item.name}
                            defaultValue={item.data}
                            required={item.required}
                            disabled={item.disabled}
                            variant="standard"
                            onChange={(e) => onChange(e.target)}
                        />
                    </Grid>
                ))
                ) : null}
            </Grid>
            <Button variant="outlined">이전</Button>
            <Button variant="outlined" onClick={() => {clickEvent(inputs)}}>저장</Button>
        </Box>
    )
}

export default ManagementDetail;