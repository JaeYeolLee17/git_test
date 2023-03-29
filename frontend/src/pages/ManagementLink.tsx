import React, { useEffect, useState } from "react";
import { useAuthState } from "../provider/AuthProvider";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import { useAsyncAxios } from "../utils/customHooks";
import { Box, Button, Grid } from "@mui/material";
import editBtn from "../assets/images/btn_list_edit_n.svg";
import * as Common from "../commons/common";

import KakaoMap from "../component/KakaoMap";

import styles from "../pages/ManagementLink.module.css";
import TableManagement from "../component/TableManagement";
import { useNavigate } from "react-router-dom";
import OsmMap from "../component/OsmMap";

type rows = {
    id: string;
    start: string;
    end: string;
};

type columns = {
    field: string;
    headerName: string;
    flex: number;
    cellRenderer: any;
};

function ManagementLink() {
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [rows, setRows] = useState<rows[]>([]);
    const [listEditLink, setListEditLink] = useState<Array<any>>([]);
    // const [selectedNo, setSelectedNo] = useState<string>("");
    const [selectedLink, setSelectedLink] = useState<any>({});

    const columns: columns[] = [
        {
            field: "id",
            headerName: "링크 No.",
            flex: 2,
            cellRenderer: undefined,
        },
        {
            field: "start",
            headerName: "시작 교차로",
            flex: 2,
            cellRenderer: undefined,
        },
        {
            field: "end",
            headerName: "종료 교차로",
            flex: 2,
            cellRenderer: undefined,
        },
        {
            field: "data",
            headerName: "",
            flex: 1,
            cellRenderer: (params: any) => {
                return (
                    <Button
                        onClick={(e) => {
                            navigate(Common.PAGE_MANAGEMENT_LINK_DETAIL, {
                                state: {
                                    selected: listEditLink.find(function (data) {
                                        return data.linkId === params.data.id;
                                    }),
                                    list: listEditLink,
                                },
                            });
                        }}
                    >
                        <img src={editBtn} width={20}></img>
                    </Button>
                );
            },
        },
    ];

    useEffect(() => {
        requestLinks();
    }, []);

    const requestAxiosLinks = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(Request.LINK_LIST_URL);

        return response.data;
    };

    const { loading: loadingLinks, error: errorLinks, data: resultLinks, execute: requestLinks } = useAsyncAxios(requestAxiosLinks);

    useEffect(() => {
        if (resultLinks === null) return;

        setListEditLink(resultLinks.links);

        resultLinks.links.map((result: any) => {
            setRows((rows) => [
                ...rows,
                {
                    id: result.linkId,
                    start: result.start.intersectionName,
                    end: result.end.intersectionName,
                },
            ]);
        });
    }, [resultLinks]);

    useEffect(() => {
        if (errorLinks === null) return;

        console.log("errorLinks", errorLinks);
    }, [errorLinks]);

    const onRowClick = (selectedId: string) => {
        //setSelectedNo(selectedId);
        setSelectedLink(
            listEditLink.find((data) => {
                return data.linkId === selectedId;
            })
        );
    };

    return (
        <div className={styles.wrapper}>
            <Grid container spacing={2}>
                <Grid item md={7}>
                    <TableManagement columns={columns} rows={rows} selectedId={selectedLink.selectedId} clickEvent={onRowClick} />
                </Grid>
                <Grid item md={5}>
                    <Box className={styles.box}>
                        <OsmMap
                            style={{
                                width: "100%",
                                height: "100%",
                                zIndex: "0",
                            }}
                            zoomLevel={6}
                            editLinks={{
                                list: listEditLink,
                                selected: selectedLink,
                            }}
                            center={Utils.utilIsEmptyObj(selectedLink) ? undefined : selectedLink.start.gps}
                        />
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}

export default ManagementLink;
