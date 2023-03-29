import { Box, Collapse } from "@mui/material";
import React from "react";

import * as Utils from "../utils/utils";

import styles from "./AvlInfo.module.css";
import iconEmergencyVehicle from "../assets/images/ico-emergency-vehicle.svg";
import iconRouteLine from "../assets/images/ico-route-line.svg";
import iconDepart from "../assets/images/ico-route-departure.svg";
import iconArrive from "../assets/images/ico-route-arrival.svg";
import iconSiren from "../assets/images/ico_emergency_vehicle_list.svg";

function AvlInfo({
    list,
    selected,
    onChangedSelectedEmergencyCarNumber,
}: {
    list: any[];
    selected: string;
    onChangedSelectedEmergencyCarNumber: (carNumber: string) => void;
}) {
    const [showDetail, setShowDetail] = React.useState(true);

    const onToggleDetail = () => {
        setShowDetail(!showDetail);
    };

    const getStatusClass = (status: string) => {
        let name = styles.dipatch;
        switch (status) {
            case "출동":
                name = styles.dipatch;
                break;

            case "현장도착":
                name = styles.siteArrive;
                break;

            case "현장출발":
                name = styles.siteDepart;
                break;

            case "병원도착":
                name = styles.hospitalArrive;
                break;

            default:
                break;
        }

        return name;
    };

    const displayAvlInfo = (list: any[], selected: string) => {
        if (Utils.utilIsEmptyArray(list) === false) {
            return list.map((avlData: any) => {
                return (
                    <Box
                        key={avlData.carNo}
                        className={[styles.emergencyVehicle, selected === avlData.carNo ? styles.emergencyVehicleSelected : ""].join(" ")}
                        onClick={() => onChangedSelectedEmergencyCarNumber(avlData.carNo)}
                    >
                        <Box className={[styles.infoHeader, selected === avlData.carNo ? styles.infoHeaderSelected : ""].join(" ")}>
                            <strong>{avlData.carNo}</strong>
                            <span className={[styles.status, getStatusClass(avlData.status[avlData.status.length - 1].name)].join(" ")}>
                                {avlData.status[avlData.status.length - 1].name}
                            </span>
                        </Box>
                        <Box className={styles.route}>
                            <img className={styles.routeLine} src={iconRouteLine} />
                            <Box className={styles.depart}>
                                <img className={styles.routeImg} src={iconDepart} alt="departure-icon" />
                                <span className={styles.locationText}>{avlData.wardName}</span>
                            </Box>
                            <Box className={styles.arrive}>
                                <img className={styles.routeImg} src={iconArrive} alt="arrive-icon" />
                                <span className={styles.locationText}>
                                    {avlData.path[0].destination === undefined ? "목적지" : avlData.path[0].destination}
                                </span>
                            </Box>
                            <img className={styles.sirenImg} src={iconSiren} alt="siren-icon-img" />
                        </Box>
                    </Box>
                );
            });
        }

        return null;
    };

    return list.length === 0 ? null : (
        <Box>
            <Box className={styles.emergencyPop} onClick={onToggleDetail}>
                <Box className={styles.popContent}>
                    <Box className={styles.popHeader}>
                        <img src={iconEmergencyVehicle} alt="vehicle" />
                        <span>긴급차량</span>
                    </Box>
                    <Box className={styles.popBody}>{list.length}건</Box>
                </Box>
            </Box>
            <Collapse in={showDetail} className={styles.emergencyBoard}>
                <section className={styles.boardTitle}>실시간 긴급차량 정보</section>
                {displayAvlInfo(list, selected)}
            </Collapse>
        </Box>
    );
}

export default AvlInfo;
