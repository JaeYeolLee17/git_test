import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import KakaoMap from "../component/KakaoMap";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import Grid from '@mui/material/Grid';
import styles from './UserDetail.module.css';
import * as Utils from "../utils/utils";
import * as Request from "../commons/request"
import * as Common from "../commons/common";

type userDataType = {
    distance: number,
    serverUrl: string,
    sendCycle: number,
    collectCycle: number 
}

function UserDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [selectedUserList, setSelectedUserList] = useState<Array<any>>([]);
    const [userData, setUserData] = useState<any[]>([]);

    useEffect(() => {
        location.state !== null && onSelectedUser(location.state);
    }, [location.state])

    const onSelectedUser = (selectedUser: any) => {
        setSelectedUserList(selectedUserList => [...selectedUserList, selectedUser]);
        
        setUserData([{
            name: 'username',
            data: selectedUser.userId,
            width:6,
            required: true,
            disabled: true
        },
        {
            name: 'username',
            data: selectedUser.username,
            width:6,
            required: true,
            disabled: false
        },
        {
            name: 'password',
            data: '',
            width:6,
            required: false,
            disabled: false
        },
        {
            name: 'passwordConfirm',
            data: '',
            width:6,
            required: false,
            disabled: false
        },
        {
            name: 'email',
            data: selectedUser.email,
            width:6,
            required: true,
            disabled: false
        },
        {
            name: 'phone',
            data: selectedUser.phone,
            width:6,
            required: false,
            disabled: false
        },
        {
            name: 'authority',
            data: selectedUser.authority,
            width:6,
            required: false,
            disabled: false
        }
        ]);
    }

    const requestAxiosUpdateUsers = async(userData: userDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        console.log("userData : " + userData);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).post(
            Request.USER_URL + "/" + selectedUserList[0].userId,
            { userData }
        ); 

        return response.data;
    }

    const {
        loading: loadingUsers,
        error: errorUpdateUsers,
        data: resultUpdateUsers,
        execute: requestUpdateUsers,
    } = useAsyncAxios(requestAxiosUpdateUsers);

    useEffect(() => {
        if (resultUpdateUsers === null) return;
        
        navigate(Common.PAGE_MANAGEMENT_USER);
        alert("수정되었습니다.");

    }, [resultUpdateUsers]);

    useEffect(() => {
        if (errorUpdateUsers === null) return;

        console.log("errorUsers", errorUpdateUsers);
    }, [errorUpdateUsers]);

    const onClickEvent = (users :any) => {
        requestUpdateUsers(users);
    };
    
    return(
        <div className={styles.wrapper}>
            <ManagementDetail response={userData} clickEvent={onClickEvent}/>
        </div>
    )
}

export default UserDetail;