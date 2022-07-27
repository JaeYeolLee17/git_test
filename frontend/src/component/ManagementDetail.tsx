import React, { useState } from "react";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Grid from "@mui/material/Grid";
import InputBase from '@mui/material/InputBase';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import { alpha, styled } from '@mui/material/styles';

import * as String from "../commons/string";
import { useNavigate } from "react-router-dom";
import styles from "../component/ManagementDetail.module.css";

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

    const BootstrapInput = styled(InputBase)(({ theme }) => ({
        'label + &': {
            marginTop: theme.spacing(3),
        },
        '& .MuiInputBase-input': {
            position: 'relative',
            width: '100%',
            fontSize: 16,
            fontWeight: '500',
            color: 'black',
            backgroundColor: '#ffffff',
            border: '1px solid #dee2e6',
            borderRadius: '0.25rem',
            padding: '0.45rem 0.9rem',
            '&:focus': {
                boxShadow: `${alpha(theme.palette.primary.main, 0.25)} 0 0 0 0.2rem`,
                borderColor: theme.palette.primary.main,
            },
            '&:disabled': {
                background: '#e9ecef',
                color: '#000000'
            },
        },
    }));

    return(
        <Box
            className={styles.box}
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
                        {item.name === ''
                        ? <></> 
                        : <FormControl variant="standard" style={{width: '100%', marginBottom: '1rem'}}>
                            <InputLabel shrink htmlFor={item.name} className={styles.inputLabel} required={item.required}>
                                {item.name}
                            </InputLabel>
                            <BootstrapInput 
                                id={item.name}
                                
                                disabled={item.disabled}
                                defaultValue={item.data}
                                onChange={(e) => onChange(e.target)} 
                            />
                        </FormControl>
                        }
                    </Grid>
                ))
                ) : null}
            </Grid>
            <div style={{textAlign : 'center'}}>
                <Button variant="outlined" className={styles.backBtn} onClick={() => navigate(-1)}>{String.back}</Button>
                <Button variant="outlined" className={styles.saveBtn} onClick={() => {clickEvent(inputs)}}>{String.save}</Button>
            </div>
        </Box>
    )
}

export default ManagementDetail;