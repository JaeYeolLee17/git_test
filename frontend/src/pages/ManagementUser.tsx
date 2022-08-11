import React, { useEffect, useState } from "react";
import TableManagement from "../component/TableManagement";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as Common from "../commons/common";
import IconButton from "@mui/material/IconButton";
import Button from "@mui/material/Button";
import editBtn from "../assets/images/btn_list_edit_n.svg"
import deleteBtn from "../assets/images/btn_list_delete_n.svg"
import AddIcon from "@mui/icons-material/Add";
import styles from "./ManagementUser.module.css";

import { useNavigate } from "react-router-dom";

type rows = {
    id: string,
    username: string,
    authority: string
}

type columns ={
    field: string,
    headerName: string,
    flex: number,
    cellRenderer: any
  }

function ManagementUser() {
    const userDetails = useAuthState();
    const navigate = useNavigate();
    const [rows, setRows] = useState<rows[]>([]);
    const [listUser, setListUser] = useState<Array<any>>([]);
    const [selectedUserId, setSelectedUserId] = useState<string>("");

    const columns: columns[] = [
        {
            field: "id",
            headerName: "아이디",
            flex: 1,
            cellRenderer: undefined,
        },
        {
            field: "username",
            headerName: "이름",
            flex: 1,
            cellRenderer: undefined,
        },
        {
            field: "authority",
            headerName: "권한",
            flex: 1,
            cellRenderer: undefined,
        },
        {
            field: "data",
            headerName: "",
            flex: 1,
            cellRenderer: (params :any) => {
                return (
                    <>
                        <Button 
                            onClick={(e) => {
                                navigate(Common.PAGE_MANAGEMENT_USER_DETAIL, {
                                    state: listUser.find(function (data) {
                                        return data.username === params.data.id;
                                    }),
                                });
                            }}
                        >
                            <img src={editBtn} width={20}></img>
                        </Button>
                        <Button onClick={(e) => {requestDeleteUser(params.data.id)}}>
                            <img src={deleteBtn} width={20}></img>
                        </Button>
                    </>
                )
            }
        }
      ];

    useEffect(() => {
        requestUsers();
    }, []);

    const requestAxiosUsers = async () => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.USER_LIST_URL
        );

        return response.data;
    };

    const {
        loading: loadingUsers,
        error: errorUsers,
        data: resultUsers,
        execute: requestUsers,
    } = useAsyncAxios(requestAxiosUsers);

    useEffect(() => {
        if (resultUsers === null) return;

        setListUser(resultUsers.users);

        //console.log("resultUsers.users", resultUsers.users);

        resultUsers.users.map((result: any) => {
            setRows((rows) => [
                ...rows,
                {
                    id: result.username,
                    username: result.nickname,
                    authority: result.authority,
                },
            ]);
        });
    }, [resultUsers]);

    useEffect(() => {
        if (errorUsers === null) return;

        console.log("errorUsers", errorUsers);
    }, [errorUsers]);

    const requestAxiosDeleteUser = async(userId: string) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).delete(
            Request.USER_URL + "/" + userId
        );

        return response.data;
    }

    const {
        loading: loadingDeleteUser,
        error: errorDeleteUser,
        data: resultDeleteUser,
        execute: requestDeleteUser,
    } = useAsyncAxios(requestAxiosDeleteUser);

    useEffect(() => {
        if (resultDeleteUser === null) return;
        alert("삭제되었습니다.")

    }, [resultDeleteUser]);

    useEffect(() => {
        if (errorDeleteUser === null) return;

        console.log("errorDeleteUser", errorDeleteUser);
    }, [errorDeleteUser]);

    const onRowClick = (username: string) => {
        setSelectedUserId(username);
    };

    const onAddUserClick = () => {
        navigate(Common.PAGE_MANAGEMENT_USER_DETAIL, {
            state: null,
        });
    }

    return (
        <div style={{ position: "relative"}}>
            <IconButton 
                className={styles.imgButton}
                onClick={(e) => onAddUserClick()}>
                <AddIcon />
            </IconButton>
            <TableManagement
                columns={columns}
                rows={rows}
                selectedId={selectedUserId}
                clickEvent={onRowClick}
            />
        </div>
    );
}

export default ManagementUser;
