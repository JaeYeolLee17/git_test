import React, { useEffect, useState } from "react";
import TableManagement from "../component/TableManagement";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as Common from "../commons/common";

import { useNavigate } from "react-router-dom";

type rows = {
    id: string;
    username: string;
    authority: string;
};

type columns = {
    field: string;
    headerName: string;
    flex: number;
    renderCell: any;
};

function ManagementUser() {
    const userDetails = useAuthState();
    const navigate = useNavigate();
    const [rows, setRows] = useState<rows[]>([]);
    const [listUser, setListUser] = useState<Array<any>>([]);
    const [selectedUserId, setSelectedUserId] = useState<string | null>("");

    const columns: columns[] = [
        {
            field: "id",
            headerName: "아이디",
            flex: 1,
            renderCell: undefined,
        },
        {
            field: "username",
            headerName: "이름",
            flex: 1,
            renderCell: undefined,
        },
        {
            field: "authority",
            headerName: "권한",
            flex: 1,
            renderCell: undefined,
        },
        {
            field: "data",
            headerName: "",
            flex: 1,
            renderCell: (params: any) => {
                return (
                    <button
                        onClick={(e) => {
                            navigate(Common.PAGE_MANAGEMENT_USER_DETAIL, {
                                state: listUser.find(function (data) {
                                    return data.username === params.id;
                                }),
                            });
                        }}
                    >
                        수정
                    </button>
                );
            },
        },
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

    const handleClickUser = (username: string) => {
        setSelectedUserId(username);
        alert(username);
    };

    const onRowClick = (username: string) => {
        setSelectedUserId(username);
    };

    return (
        <div style={{ height: "700px" }}>
            <TableManagement
                columns={columns}
                rows={rows}
                clickEvent={onRowClick}
            />
        </div>
    );
}

export default ManagementUser;
