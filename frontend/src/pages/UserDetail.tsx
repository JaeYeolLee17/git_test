import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import styles from "./UserDetail.module.css";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request";
import * as Common from "../commons/common";

type userDataType = {
    distance: number;
    serverUrl: string;
    sendCycle: number;
    collectCycle: number;
};

function UserDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [selectedUserList, setSelectedUserList] = useState<Array<any>>([]);
    const [userData, setUserData] = useState<any[]>([]);
    const [pageType, setPageType] = useState("add");

    const passwordRule = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;

    useEffect(() => {
        onSelectedUser(location.state);
        location.state === null ? setPageType("add") : setPageType("edit");

    }, [location.state]);

    const onSelectedUser = (selectedUser: any) => {
        setSelectedUserList((selectedUserList) => [
            ...selectedUserList,
            selectedUser,
        ]);

        setUserData([
            {
                name: "username",
                data: selectedUser === null ? "" : selectedUser.username ,
                width: 6,
                required: true,
                disabled: selectedUser === null ? false : true,
            },
            {
                name: "nickname",
                data: selectedUser === null ? "" : selectedUser.nickname,
                width: 6,
                required: true,
                disabled: false,
            },
            {
                name: selectedUser === null ? "empty1" : "oldPassword",
                data: "",
                width: 6,
                type: "password",
                required: false,
                disabled: false,
            },
            {
                name: "empty2",
                data: "",
                width: 6,
                required: false,
                disabled: false,
            },
            {
                name: "newPassword",
                data: "",
                hint: "특수문자가 하나 이상 포함된 최소 8자리 비밀번호",
                width: 6,
                type: "password",
                required: false,
                disabled: false,
            },
            {
                name: "newPasswordConfirm",
                data: "",
                width: 6,
                type: "password",
                required: false,
                disabled: false,
            },
            {
                name: "email",
                data: selectedUser === null ? "" : selectedUser.email,
                width: 6,
                required: true,
                disabled: false,
            },
            {
                name: "phone",
                data: selectedUser === null ? "" : selectedUser.phone,
                hint: "핸드폰 번호, 010-0000-0000",
                width: 6,
                required: false,
                disabled: false,
            },
            {
                name: "authority",
                data: selectedUser === null ? "ROLE_USER" : selectedUser.authority,
                width: 6,
                type: "select",
                required: false,
                disabled: false,
            },
        ]);
    };

    //addUser
    const requestAxiosAddUser = async (userData: userDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        console.log("userData : " + userData);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).post(
            Request.USER_URL,
            userData 
        );

        return response.data;
    };

    const {
        loading: loadingAddUser,
        error: errorAddUser,
        data: resultAddUser,
        execute: requestAddUser,
    } = useAsyncAxios(requestAxiosAddUser);

    useEffect(() => {
        if (resultAddUser === null) return;

        navigate(Common.PAGE_MANAGEMENT_USER);
        alert("등록되었습니다.");
    }, [resultAddUser]);

    useEffect(() => {
        if (errorAddUser === null) return;

        console.log("errorUser", errorAddUser);
    }, [errorAddUser]);

    //updateUser
    const requestAxiosUpdateUser = async (userData: userDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        console.log("userData : " + userData);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).put(
            Request.USER_URL + "/" + selectedUserList[0].username,
            userData 
        );

        return response.data;
    };

    const {
        loading: loadingUser,
        error: errorUpdateUser,
        data: resultUpdateUser,
        execute: requestUpdateUser,
    } = useAsyncAxios(requestAxiosUpdateUser);

    useEffect(() => {
        if (resultUpdateUser === null) return;

        navigate(Common.PAGE_MANAGEMENT_USER);
        alert("수정되었습니다.");
    }, [resultUpdateUser]);

    useEffect(() => {
        if (errorUpdateUser === null) return;

        console.log("errorUser", errorUpdateUser);
    }, [errorUpdateUser]);

    const onClickEvent = (pageType :string, user: any) => {

        const updateUserData: any = {
            "username": user.username,
            "nickname": user.nickname,
            "email": user.email,
            "phone": user.phone,
            "authority": user.authority
        }

        switch(pageType){
            case "add":
                addUserstep(user, updateUserData);
            break;
            case "edit":
                editUserStep(user, updateUserData);
            break;
            default:
                break;
        }
        
        pageType === "add" ? requestAddUser(updateUserData) : requestUpdateUser(updateUserData);
    };

    const addUserstep = (user: any, updateUserData: any) => {
        if(user.newPassword !== user.newPasswordConfirm){
            alert("비밀번호 확인이 일치하지 않습니다.");
            return;
        } else if(!passwordRule.test(user.newPassword)){
            alert('비밀번호를 숫자, 영문, 특수문자 8자~16자로 입력하세요');
            return;
        } else {
            updateUserData["password"] =  user.newPassword;
        }
    };

    const editUserStep = (user: any, updateUserData:any) => {
        if(user.newPassword !== ""){
            if(user.oldPassword === ""){
                alert("기존 비밀번호를 입력하세요.");
                return;
            } else if(user.newPasswordConfirm === ""){
                alert("비밀번호 확인을 입력해주세요.");
                return;
            } else if(user.newPassword !== user.newPasswordConfirm){
                alert("비밀번호 확인이 일치하지 않습니다.");
                return;
            } else if(!passwordRule.test(user.newPassword)){
                alert('비밀번호를 숫자, 영문, 특수문자 8자~16자로 입력하세요');
                return;
            } else {
                updateUserData["oldPassword"] =  user.oldPassword;
                updateUserData["newPassword"] =  user.newPassword;
            }
        }
    };

    return (
        <div className={styles.wrapper}>
            <ManagementDetail pageType={pageType} response={userData} clickEvent={onClickEvent} />
        </div>
    );
}

export default UserDetail;
