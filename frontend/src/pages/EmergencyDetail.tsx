import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import ManagementDetail from "../component/ManagementDetail";
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import { useNavigate } from "react-router-dom";
import styles from "./EmergencyDetail.module.css";
import * as Utils from "../utils/utils";
import * as Request from "../commons/request"
import * as Common from "../commons/common";

type emergencyDataType = {
    distance: number,
    serverUrl: string,
    sendCycle: number,
    collectCycle: number 
}

function EmergencyDetail() {
    const location = useLocation();
    const userDetails = useAuthState();
    const navigate = useNavigate();

    const [selectedEmergencyList, setSelectedEmergencyList] = useState<Array<any>>([]);
    const [emergencyData, setEmergencyData] = useState<any[]>([]);

    useEffect(() => {
        location.state !== null && onSelectedEmergency(location.state);
    }, [location.state])

    const onSelectedEmergency = (selectedEmergency: any) => {
        setSelectedEmergencyList(selectedEmergencyList => [...selectedEmergencyList, selectedEmergency]);
        
        setEmergencyData([{
            name: "carNo",
            data: selectedEmergency.carNo,
            width:6,
            required: true,
            disabled: true
        },
        {
            name: "",
            data: "",
            width:6,
            required: false,
            disabled: false
        },
        {
            name: "password",
            data: "",
            width:6,
            required: false,
            disabled: false
        },
        {
            name: "passwordConfirm",
            data: "",
            width:6,
            required: false,
            disabled: false
        },
        {
            name: "wardId",
            data: selectedEmergency.wardId,
            width:6,
            required: false,
            disabled: false
        },
        {
            name: "wardName",
            data: selectedEmergency.wardName,
            width:6,
            required: true,
            disabled: false
        }
        ]);
    }

    const requestAxiosUpdateEmergencys = async(emergencyData: emergencyDataType) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        console.log("emergencyData : " + emergencyData);

        const response = await Utils.utilAxiosWithAuth(userDetails.token).post(
            Request.EMERGENCY_URL + "/" + selectedEmergencyList[0].emergencyId,
            { emergencyData }
        ); 

        return response.data;
    }

    const {
        loading: loadingEmergencys,
        error: errorUpdateEmergencys,
        data: resultUpdateEmergencys,
        execute: requestUpdateEmergencys,
    } = useAsyncAxios(requestAxiosUpdateEmergencys);

    useEffect(() => {
        if (resultUpdateEmergencys === null) return;
        
        navigate(Common.PAGE_MANAGEMENT_EMERGENCY);
        alert("수정되었습니다.");

    }, [resultUpdateEmergencys]);

    useEffect(() => {
        if (errorUpdateEmergencys === null) return;

        console.log("errorEmergencys", errorUpdateEmergencys);
    }, [errorUpdateEmergencys]);

    const onClickEvent = (emergencys :any) => {
        requestUpdateEmergencys(emergencys);
    };
    
    return(
        <div className={styles.wrapper}>
            <ManagementDetail 
                pageType="edit" 
                response={emergencyData} 
                clickEvent={onClickEvent}
            />
        </div>
    )
}

export default EmergencyDetail;