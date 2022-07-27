import React, { useEffect, useState } from "react"
import TableManagement from "../component/TableManagement"
import { useAuthState } from "../provider/AuthProvider";
import { useAsyncAxios } from "../utils/customHooks";
import * as Utils from "../utils/utils"
import * as Request from "../commons/request"
import * as Common from "../commons/common";
import Button from '@mui/material/Button';
import editBtn from '../assets/images/btn_list_edit_n.svg'
import deleteBtn from '../assets/images/btn_list_delete_n.svg'

import { useNavigate } from "react-router-dom";

type rows = {
    id: string,
    wardId: string,
    wardName: string
}

type columns ={
    field: string,
    headerName: string,
    headerAlign: string,
    align: string,
    flex: number,
    renderCell: any
  }

function ManagementEmergency() {
    const userDetails = useAuthState();
    const navigate = useNavigate();
    const [rows, setRows] = useState<rows[]>([]);
    const [listEmergency, setListEmergency] = useState<Array<any>>([]);
    const [selectedEmergencyId, setSelectedEmergencyId] = useState<string | null>("");
    const [mapZoomLevel, setMapZoomLevel] = useState<number>(7);
    
    const columns: columns[] = [
        {
            field: 'id',
            headerName: '차량번호',
            headerAlign: 'center',
            align: 'center',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'wardId',
            headerName: '소속기관 아이디',
            headerAlign: 'center',
            align: 'center',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'wardName',
            headerName: '소속기관명',
            headerAlign: 'center',
            align: 'center',
            flex: 1,
            renderCell: undefined
        },
        {
            field: 'data',
            headerName: '',
            headerAlign: 'center',
            align: 'center',
            flex: 1,
            renderCell: (params :any) => {
                return (
                    <>
                        <Button onClick={(e) => {
                            navigate(
                                Common.PAGE_MANAGEMENT_EMERGENCY_DETAIL, {
                                state :listEmergency.find(function(data){ return data.carNo === params.id })})
                            }
                        }>
                            <img src={editBtn} width={20} />
                        </Button>
                        <Button onClick={(e) => {requestDeleteEmergency(params.id)}}>
                            <img src={deleteBtn} width={20} />
                        </Button>
                    </>
                )
            }
        }
      ];

    useEffect(() => {
        requestEmergencys();
    }, []);

    const requestAxiosEmergencys = async() => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).get(
            Request.EMERGENCY_LIST_URL
        );

        return response.data;
    }

    const {
        loading: loadingEmergencys,
        error: errorEmergencys,
        data: resultEmergencys,
        execute: requestEmergencys,
    } = useAsyncAxios(requestAxiosEmergencys);

    useEffect(() => {
        if (resultEmergencys === null) return;

        setListEmergency(resultEmergencys.cars);

        resultEmergencys.cars.map((result: any) => { 
            setRows(rows => 
                [...rows, 
                    {
                        id: result.carNo, 
                        wardId: result.wardId, 
                        wardName: result.wardName
                    }
                ]
            );
        })

    }, [resultEmergencys]);

    useEffect(() => {
        if (errorEmergencys === null) return;

        console.log("errorEmergencys", errorEmergencys);
    }, [errorEmergencys]);

    const requestAxiosDeleteEmergency = async(carNo: string) => {
        if (userDetails === null) return null;
        if (userDetails?.token === null) return null;

        const response = await Utils.utilAxiosWithAuth(userDetails.token).delete(
            Request.EMERGENCY_URL + "/" + carNo
        );

        return response.data;
    }

    const {
        loading: loadingDeleteEmergency,
        error: errorDeleteEmergency,
        data: resultDeleteEmergency,
        execute: requestDeleteEmergency,
    } = useAsyncAxios(requestAxiosDeleteEmergency);

    useEffect(() => {
        if (resultDeleteEmergency === null) return;

        alert('삭제되었습니다.');

    }, [resultDeleteEmergency]);

    useEffect(() => {
        if (errorDeleteEmergency === null) return;

        console.log("errorDeleteEmergency", errorDeleteEmergency);
    }, [errorDeleteEmergency]);

    const handleClickEmergency = (emergencyId: string) => {
        setSelectedEmergencyId(emergencyId);
        alert(emergencyId);
    };

    
    const onChangedZoomLevel = (level: number) => {
        console.log("level", level);
        setMapZoomLevel(level);
    };

    const onRowClick = (emergencyId: string) => {
        setSelectedEmergencyId(emergencyId);
    };

    return(
        <div style={{height: '700px' }}>
            <TableManagement 
                columns={columns} 
                rows={rows} 
                clickEvent={onRowClick}
            />
        </div>
    )
}

export default ManagementEmergency