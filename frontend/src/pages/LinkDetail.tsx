import {
    Box,
    Button,
    FormControl,
    Grid,
    InputLabel,
    List,
    ListItem,
    ListItemText,
    TextField,
} from "@mui/material";
import React, { useEffect, useState } from "react";

import { useNavigate, useLocation } from "react-router-dom";
import KakaoMap from "../component/KakaoMap";
import { useAuthState } from "../provider/AuthProvider";

import * as String from "../commons/string";
import * as Utils from "../utils/utils";
import * as Common from "../commons/common";
import * as Request from "../commons/request";

import styles from "./LinkDetail.module.css";
import { useAsyncAxios } from "../utils/customHooks";

type CustomizedState = {
    selected: any;
    list: any[];
};

function LinkDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [editLinkList, setEditLinkList] = useState<Array<any>>([]);
    const [selectedLink, setSelectedLink] = useState<any>({});

    const [updateLinkData, setUpdateLinkData] = useState<any[]>([]);

    const title: Map<string, string> = new Map([
        ["linkId", String.link_no],
        ["start", String.link_start],
        ["end", String.link_end],
        ["gps", String.link_gps],
    ]);

    useEffect(() => {
        const state = location.state as CustomizedState;

        setEditLinkList(state.list);
        setSelectedLink(state.selected);

        //console.log("state", state);
    }, [location.state]);

    useEffect(() => {
        // console.log("selectedLink", selectedLink);
        if (!Utils.utilIsEmptyObj(selectedLink)) {
            const gpsString = JSON.stringify(selectedLink.gps);
            setUpdateLinkData([
                {
                    name: "linkId",
                    data: selectedLink.linkId,
                    width: 12,
                    type: "input",
                    required: true,
                    disabled: true,
                },
                {
                    name: "start",
                    data: selectedLink.start.intersectionName,
                    width: 12,
                    type: "input",
                    required: true,
                    disabled: true,
                },
                {
                    name: "end",
                    data: selectedLink.end.intersectionName,
                    width: 12,
                    type: "input",
                    required: true,
                    disabled: true,
                },
                {
                    name: "gps",
                    data: gpsString,
                    width: 12,
                    type: "list",
                    required: true,
                    disabled: true,
                },
            ]);
        }
    }, [selectedLink]);

    const onUpdateEvent = () => {
        console.log("selectedLink", selectedLink);
        requestUpdateLink(selectedLink);
    };

    const popSelectedGps = () => {
        const newGps = [...selectedLink.gps];
        newGps.pop();
        setSelectedLink({ ...selectedLink, gps: newGps });
    };

    const handleClickMap = (
        target: kakao.maps.Map,
        mouseEvent: kakao.maps.event.MouseEvent
    ) => {
        const newGps = [...selectedLink.gps];
        newGps.push({
            lat: mouseEvent.latLng.getLat(),
            lng: mouseEvent.latLng.getLng(),
        });
        setSelectedLink({ ...selectedLink, gps: newGps });
    };

    const displayInput = () => {
        if (updateLinkData !== undefined) {
            return updateLinkData.map((item: any) => {
                // console.log("item", item);
                return (
                    <Grid item xs={item.width} key={item.name}>
                        {item.name.substring(0, 5) === "empty" && <></>}

                        {item.name.substring(0, 5) !== "empty" &&
                            item.type === "input" && (
                                <FormControl
                                    variant='standard'
                                    style={{
                                        width: "100%",
                                        marginBottom: "1rem",
                                    }}
                                >
                                    <InputLabel
                                        shrink
                                        htmlFor={item.name}
                                        className={styles.inputLabel}
                                        required={item.required}
                                    >
                                        {title.get(item.name)}
                                    </InputLabel>
                                    <TextField
                                        id={item.name}
                                        disabled={item.disabled}
                                        defaultValue={item.data}
                                        className={styles.input}
                                        placeholder={item.hint}
                                        type={item.type}
                                    ></TextField>
                                </FormControl>
                            )}
                        {item.name.substring(0, 5) !== "empty" &&
                            item.type === "list" && (
                                <FormControl
                                    variant='standard'
                                    style={{
                                        width: "100%",
                                        marginBottom: "1rem",
                                    }}
                                >
                                    <InputLabel
                                        shrink
                                        htmlFor={item.name}
                                        className={styles.inputLabel}
                                        required={item.required}
                                    >
                                        {title.get(item.name)}
                                    </InputLabel>
                                    <TextField
                                        multiline
                                        id={item.name}
                                        disabled={item.disabled}
                                        value={item.data}
                                        className={styles.input}
                                        placeholder={item.hint}
                                        type={item.type}
                                        onChange={(e) => {
                                            console.log(e.target.value);
                                        }}
                                    ></TextField>
                                </FormControl>
                            )}
                    </Grid>
                );
            });
        }

        return null;
    };

    const requestAxiosUpdateLink = async (link: any) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).put(
            Request.LINK_URL + "/" + link.linkId,
            link
        );

        return response.data;
    };

    const {
        loading: loadingLink,
        error: errorUpdateLink,
        data: resultUpdateLink,
        execute: requestUpdateLink,
    } = useAsyncAxios(requestAxiosUpdateLink);

    useEffect(() => {
        if (resultUpdateLink === null) return;

        navigate(Common.PAGE_MANAGEMENT_LINK);
        alert("수정되었습니다.");
    }, [resultUpdateLink]);

    useEffect(() => {
        if (errorUpdateLink === null) return;

        console.log("errorLink", errorUpdateLink);
    }, [errorUpdateLink]);

    return (
        <div>
            <Grid container spacing={1}>
                <Grid item xs={6}>
                    <Box
                        className={styles.box}
                        component='form'
                        sx={{
                            "& .MuiTextField-root": { m: 1, width: "25ch" },
                        }}
                        noValidate
                        autoComplete='off'
                    >
                        <Grid container spacing={1}>
                            {displayInput()}
                        </Grid>
                        <div style={{ textAlign: "center" }}>
                            <Button
                                variant='outlined'
                                className={styles.backBtn}
                                onClick={() => navigate(-1)}
                            >
                                {String.back}
                            </Button>
                            <Button
                                variant='outlined'
                                className={styles.saveBtn}
                                onClick={onUpdateEvent}
                            >
                                {String.save}
                            </Button>
                        </div>
                    </Box>
                </Grid>
                <Grid item xs={6}>
                    <Box>
                        <KakaoMap
                            style={{
                                width: "100%",
                                height: "calc(95vh - 80px)",
                                zIndex: "0",
                            }}
                            zoomLevel={3}
                            editLinks={{
                                list: editLinkList,
                                selected: selectedLink,
                            }}
                            center={
                                Utils.utilIsEmptyObj(selectedLink)
                                    ? undefined
                                    : selectedLink.start.gps
                            }
                            onClickMap={handleClickMap}
                        />

                        <div style={{ textAlign: "center" }}>
                            <Button
                                variant='outlined'
                                className={styles.saveBtn}
                                onClick={popSelectedGps}
                            >
                                {String.undo}
                            </Button>
                        </div>
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}

export default LinkDetail;
