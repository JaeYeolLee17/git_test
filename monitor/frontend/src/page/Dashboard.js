import React, { useEffect, useState } from "react";
import axios from "axios";

import Header from "../component/Header";
import Menu from "../component/Menu";
import KakaoMap from "../component/KakaoMap";

import { useAuthState } from "../provider/AuthProvider";

const CAMERA_URL = "/challenge-api/v1/cameras";

const Dashboard = () => {
    const userDetails = useAuthState();

    const [listCamera, setListCamera] = useState([]);
    const [showCameras, setShowCameras] = useState(true);
    const [selectedCamera, setSelectedCamera] = useState("");

    const requesetCameras = async (e) => {
        try {
            //console.log(userDetails.token);
            const response = await axios.get(CAMERA_URL, {
                headers: {
                    "Content-Type": "application/json",
                    "X-AUTH-TOKEN": userDetails.token,
                },
                withCredentials: true,
            });

            //console.log(JSON.stringify(response?.data));
            setListCamera(response?.data.cameras);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        const timerId = setInterval(() => {
            requesetCameras();
        }, 10 * 1000);
        return () => setInterval(timerId);
    }, []);

    return (
        <div>
            <Header />
            <Menu />
            Dashboard
            <KakaoMap
                style={{
                    width: "100%",
                    height: "100vh",
                }}
                cameras={{
                    list: listCamera,
                    isShow: showCameras,
                    selected: selectedCamera,
                }}
            />
        </div>
    );
};

export default Dashboard;
